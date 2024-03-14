package com.andremachado.cursomc.services;

import com.andremachado.cursomc.domain.Cliente;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.andremachado.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmatioEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido pedido);
	
	void sendHtmlEmail(MimeMessage msg);

    void sendNewPasswordEmail(Cliente cliente, String novaSenha);
}
