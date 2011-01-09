package dk.bregnvig.formula1.server.restful;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.client.service.GameService;

@Controller
public class GameRestfulController {
	
	@Autowired private GameService service;
 	@Autowired public HttpServletRequest request;
 	
	@RequestMapping("/login/{playerName}/{password}")
	public @ResponseBody ClientPlayer login(@PathVariable String playerName, @PathVariable String password) throws CredentialException {
		System.out.println(playerName + ":" + password + ":" + request);
		return service.login(playerName, password);
	}
	
	@ExceptionHandler(CredentialException.class)
	public void handleCredentialException(CredentialException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
	}
}
