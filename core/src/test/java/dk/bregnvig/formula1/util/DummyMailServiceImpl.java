package dk.bregnvig.formula1.util;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import dk.bregnvig.formula1.Player;

public class DummyMailServiceImpl implements JavaMailSender {

	public Set<Player> players = new HashSet<Player>();
	
	public MimeMessage createMimeMessage() {
		throw new UnsupportedOperationException();
	}

	public MimeMessage createMimeMessage(InputStream arg0) throws MailException {
		throw new UnsupportedOperationException();
	}

	public void send(MimeMessage arg0) throws MailException {
		throw new UnsupportedOperationException();
	}

	public void send(MimeMessage[] arg0) throws MailException {
		throw new UnsupportedOperationException();
	}

	public void send(MimeMessagePreparator arg0) throws MailException {
		PlayerMessagePreparator preparator = (PlayerMessagePreparator) arg0;
		players.add(preparator.getPlayer());
	}

	public void send(MimeMessagePreparator[] arg0) throws MailException {
		for (MimeMessagePreparator mimeMessagePreparator : arg0) {
			send(mimeMessagePreparator);
		}
	}

	public void send(SimpleMailMessage arg0) throws MailException {
		throw new UnsupportedOperationException();
	}

	public void send(SimpleMailMessage[] arg0) throws MailException {
		throw new UnsupportedOperationException();
	}

}
