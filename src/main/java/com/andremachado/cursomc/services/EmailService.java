package com.andremachado.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.andremachado.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmatioEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
}
