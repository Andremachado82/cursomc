package com.andremachado.cursomc.services;

import com.andremachado.cursomc.services.exceptions.FileException;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class ImageService {

    public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
        String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
        if (!"png".equalsIgnoreCase(ext) && !"jpg".equalsIgnoreCase(ext))
            throw new FileException("Somente imagens .PNG e .JPG são permitidas");
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadedFile.getInputStream());
                bufferedImage = removeAlphaChannel(bufferedImage);
            return  bufferedImage;
        } catch (IOException e) {
            throw new FileException("Erro ao ler o arquivo de imagem");
        }

    }

    private BufferedImage removeAlphaChannel(BufferedImage img) {
        if (!img.getColorModel().hasAlpha()) {
            return img;
        }

        BufferedImage target = createImage(img.getWidth(), img.getHeight());
        Graphics2D g = target.createGraphics();
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return target;
    }
    private static BufferedImage createImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }


    public InputStream getInputStream(BufferedImage bufferedImage, String extensao) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, extensao, outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new FileException("Erro ao ler arquivo de imagem " + e.getMessage());
        }
    }

    public  BufferedImage cortarImagem(BufferedImage sourceImage) {
        int min = (Math.min(sourceImage.getHeight(), sourceImage.getWidth()));
        return Scalr.crop(
                sourceImage,
                (sourceImage.getWidth()) / 2 - (min / 2),
                (sourceImage.getHeight() / 2) - (min / 2),
                min,
                min
        );
    }

    public BufferedImage resize(BufferedImage sourceImage, int size) {
        return Scalr.resize(sourceImage, Scalr.Method.ULTRA_QUALITY, size);
    }
}
