package dk.bregnvig.formula1.server.context;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.server.security.TokenSecurity;

/**
 * This aspect makes sure that items store in the thread local are removed when the call has completed.
 * @author 4u603ctpsz9
 *
 */
@Aspect
@Order(1)
public class WebContextAspect {
	
	private WebContext context;
	private TokenSecurity tokenSecurity;

	@Before("execution(* dk.bregnvig.formula1.server.*ServiceImpl.*(..)) || " +
			"execution(* dk.bregnvig.formula1.server.restful.*Controller.*(..))")
	public void storeContext() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		String authorizationHeader = request.getHeader("authorization");
		if (authorizationHeader == null) {
			context.setPlayer((Player) requestAttributes.getRequest().getSession().getAttribute(SessionAttributes.PLAYER));
		} else {
			try {
				String token = authorizationHeader.replace("Basic", "").trim();
				context.setPlayer(tokenSecurity.validate(token));
			} catch (CredentialException ce) {
				//Ignore
			}
		}
	}



	@After("execution(* dk.bregnvig.formula1.server.*ServiceImpl.*(..))")
	public void removeContext() {
		context.remove();
	}
	
	public void setContext(WebContext context) {
		this.context = context;
	}

	public void setTokenSecurity(TokenSecurity tokenSecurity) {
		this.tokenSecurity = tokenSecurity;
	}
}
