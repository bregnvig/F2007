package dk.bregnvig.formula1.util;

import org.springframework.mail.javamail.MimeMessagePreparator;

import dk.bregnvig.formula1.Player;

public interface PlayerMessagePreparator extends MimeMessagePreparator {

	Player getPlayer();
	
}
