package dk.bregnvig.formula1.server.security;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import dk.bregnvig.formula1.PlayerRole;
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.server.context.WebContext;

@Aspect
@Order(99)
public class AuthorizationAspect {
	
	private Log log = LogFactory.getLog(AuthorizationAspect.class);
	private WebContext context;
	
	//@Before("this(dk.bregnvig.formula1.client.SeasonService)")
	@Before("execution(* dk.bregnvig.formula1.server.*ServiceImpl.*(..))")
	public void enforceAuthorization(JoinPoint joinpoint) throws CredentialException {

		MethodSignature signature = (MethodSignature) joinpoint.getSignature();
		
		PlayerRole[] roles = getRoles(joinpoint.getTarget().getClass(), signature.getMethod());
		
		if (isWithoutSecurity(roles)) {
			return;
		}
		
		if (isUserAvailable() == false) {
			String message = "Anonymous user trying to access Class: " + joinpoint.getTarget().getClass().getName() + " Method: " + signature.getMethod().getName();
			log.warn(message);
			throw new CredentialException(message);
		}
		
		if (context.getPlayer().isPlayerInRoles(roles) == false) {
			String message = context.getPlayer().getPlayername() + "tried to access restricted material. Method: " + signature.getMethod().getName();
			log.warn(message);
			throw new CredentialException(message);
		}
	}



	private boolean isUserAvailable() {
		return context.getPlayer() != null;
	}

	private boolean isWithoutSecurity(PlayerRole[] roles) {
		return roles == null;
	}
	
	/**
	 * Returns an array of roles. Null if no roles are required
	 * @param method
	 * @return
	 */
	private PlayerRole[] getRoles(Class<?> targetClass, Method method) {

		try {
			Method targetMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
			Authorization authorization = targetMethod.getAnnotation(Authorization.class);
			if (authorization != null) {
				return authorization.roles();
			} else {
				authorization = targetClass.getAnnotation(Authorization.class);
				if (authorization != null) {
					return authorization.roles();
				}
			}
		} catch (Exception e) {
			String message = "Unable to determine available roles";
			log.error(message, e);
			throw new IllegalStateException(message, e);
		}
		return null;
	}

	public void setContext(WebContext context) {
		this.context = context;
	}

}