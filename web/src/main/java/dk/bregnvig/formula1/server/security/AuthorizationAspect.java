package dk.bregnvig.formula1.server.security;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

@Aspect
@Order(99)
public class AuthorizationAspect {
	
	private Log log = LogFactory.getLog(AuthorizationAspect.class);

	@Before("this(dk.bregnvig.formula1.client.SeasonService)")
	//@Before("execution(* dk.bregnvig.formula1.server.*ServiceImpl.*(..))")
	public void enforceAuthorization(JoinPoint joinpoint) {

		MethodSignature signature = (MethodSignature) joinpoint.getSignature();
		System.err.println("*******************************" + signature.getMethod().getName());
	}
	
	/**
	 * Returns an array of roles. Null if no roles are required
	 * @param method
	 * @return
	 */
	private UserRole[] getRoles(Method method) {

		try {
			Class<?> declaringClass = method.getDeclaringClass();
			Authorization authorization = method.getAnnotation(Authorization.class);
			if (authorization != null) {
				return authorization.roles();
			} else {
				authorization = declaringClass.getAnnotation(Authorization.class);
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
}