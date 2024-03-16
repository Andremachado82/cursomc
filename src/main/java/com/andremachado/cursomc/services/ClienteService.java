package com.andremachado.cursomc.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;


import com.andremachado.cursomc.domain.enums.Perfil;
import com.andremachado.cursomc.security.UserSS;
import com.andremachado.cursomc.services.exceptions.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andremachado.cursomc.domain.Cidade;
import com.andremachado.cursomc.domain.Cliente;
import com.andremachado.cursomc.domain.Endereco;
import com.andremachado.cursomc.domain.enums.TipoCliente;
import com.andremachado.cursomc.dto.ClienteDto;
import com.andremachado.cursomc.dto.ClienteNewDto;
import com.andremachado.cursomc.repositories.ClienteRepository;
import com.andremachado.cursomc.repositories.EnderecoRepository;
import com.andremachado.cursomc.services.exceptions.DataIntegrityException;
import com.andremachado.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    BCryptPasswordEncoder bPasswordEncoder;

    @Autowired
    ImageService imageService;

    @Autowired
    private S3Service s3Service;

    @Value("${img.prefix.client.profile}")
    private String prefix;

    @Value("${img.profile.size}")
    private Integer sizeImgPrefix;

    public Cliente findClienteById(Integer id) {

        UserSS userSS = UserService.authenticated();
        if (userSS != null && !userSS.hasRole(Perfil.ADMIN) && !id.equals(userSS.getId())) {
            throw new AuthorizationException("Acesso negado.");
        }

        Optional<Cliente> obj = clienteRepository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente findByEmail(String email) {
        UserSS userSS = UserService.authenticated();
        if (userSS == null || !userSS.hasRole(Perfil.ADMIN) && !email.equals(userSS.getUsername()))
            throw new AuthorizationException("Acesso negado");

        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) throw new ObjectNotFoundException("Cliente não encontrado com o email : " + userSS.getUsername());
        return cliente;
    }

    @Transactional
    public Cliente insert(Cliente cliente) {
        enderecoRepository.saveAll(cliente.getEnderecos());
        return clienteRepository.save(cliente);
    }

    public Cliente update(Cliente cliente) {
        Cliente newObj = findClienteById(cliente.getId());
        updateData(newObj, cliente);
//		BeanUtils.copyProperties(cliente, newObj, "cpfOuCnpj", "tipo");

        return clienteRepository.save(newObj);
    }

    public void deleleById(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ObjectNotFoundException(
                    "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName());
        }
        try {
            clienteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir um cliente que possui produtos");
        }
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

        return clienteRepository.findAll(pageRequest);
    }

    public Cliente fromDto(ClienteDto clienteDto) {
        return new Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null, null);
    }

    public Cliente fromDto(ClienteNewDto clienteNewDto) {
        Cliente cliente = new Cliente(null, clienteNewDto.getNome(), clienteNewDto.getEmail(),
                clienteNewDto.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDto.getTipo()),
                bPasswordEncoder.encode(clienteNewDto.getSenha()));

        Cidade cidade = new Cidade(clienteNewDto.getCidadeId(), null, null);

        Endereco endereco = new Endereco(null, clienteNewDto.getLogradouro(), clienteNewDto.getNumero()
                , clienteNewDto.getComplemento(), clienteNewDto.getBairro(), clienteNewDto.getCep()
                , cliente, cidade);

        cliente.getEnderecos().add(endereco);
        cliente.getTelefones().add(clienteNewDto.getTelefone1());

        if (clienteNewDto.getTelefone2() != null) {
            cliente.getTelefones().add(clienteNewDto.getTelefone2());
        }

        if (clienteNewDto.getTelefone3() != null) {
            cliente.getTelefones().add(clienteNewDto.getTelefone3());
        }

        return cliente;
    }

    private void updateData(Cliente newObj, Cliente cliente) {
        newObj.setNome(cliente.getNome());
        newObj.setEmail(cliente.getEmail());
    }

    public URI uploadProfileImage(MultipartFile multipartFile) {
        UserSS userSS = UserService.authenticated();
        if (userSS == null) throw new AuthorizationException("Acesso negado");
        BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
        jpgImage = imageService.cortarImagem(jpgImage);
        jpgImage = imageService.resize(jpgImage, sizeImgPrefix);
        String fileName = prefix + userSS.getId() + ".jpg";
        return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
    }
}
