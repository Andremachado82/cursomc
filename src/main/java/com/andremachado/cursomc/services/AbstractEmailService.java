package com.andremachado.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.andremachado.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	
	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	TemplateEngine templateEngine;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Override
	public void sendOrderConfirmatioEmail(Pedido pedido) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(pedido);
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido pedido) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(pedido.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido confirmado! Código: " + pedido.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(pedido.toString());
		return sm;
	}
	
	protected String htmlFromTemplatePedido(Pedido pedido) {
		Context context = new Context();
		context.setVariable("pedido", pedido);
		return templateEngine.process("email/confirmacaoPedido", context);
	}
	
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido pedido) {
		try {
			MimeMessage mm = prepareMimeMessageFromPedido(pedido);
			sendHtmlEmail(mm);
			
		} catch (MessagingException e) {
			sendOrderConfirmatioEmail(pedido);
		}
	}

	protected MimeMessage prepareMimeMessageFromPedido(Pedido pedido) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setTo(pedido.getCliente().getEmail());
		helper.setFrom(sender);
		helper.setSubject("Pedido confirmado! Código: " + pedido.getId());
		helper.setSentDate(new Date(System.currentTimeMillis()));
		helper.setText(htmlFromTemplatePedido(pedido), true);		
		return mimeMessage;
	}
}
