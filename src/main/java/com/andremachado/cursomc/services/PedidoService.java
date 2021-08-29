package com.andremachado.cursomc.services;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andremachado.cursomc.domain.ItemPedido;
import com.andremachado.cursomc.domain.PagamentoComBoleto;
import com.andremachado.cursomc.domain.Pedido;
import com.andremachado.cursomc.domain.enums.EstadoPagamento;
import com.andremachado.cursomc.repositories.ItemPedidoRepository;
import com.andremachado.cursomc.repositories.PagamentoRepository;
import com.andremachado.cursomc.repositories.PedidoRepository;
import com.andremachado.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	BoletoService boletoService;
	
	@Autowired
	PagamentoRepository pagamentoRepository;
	
	@Autowired
	ProdutoService produtoService;
	
	@Autowired
	ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	EmailService emailService;
	
	public Pedido findPedidoById(Integer id) {
		Optional<Pedido> obj = pedidoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteService.findClienteById(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		}
		pedido = pedidoRepository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		for (ItemPedido ip: pedido.getItens()) {
			ip.setDesconto(0.0);	
			ip.setProduto(produtoService.findProdutoById(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		emailService.sendOrderConfirmationHtmlEmail(pedido);
		return pedido;
	}
}
