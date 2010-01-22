package dk.bregnvig.formula1.server.context;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import dk.bregnvig.formula1.Player;

/**
 * This aspect makes sure that items store in the thread local are removed when the call has completed.
 * @author 4u603ctpsz9
 *
 */
@Aspect
@Order(1)
public class WebContextAspect {
	
	private WebContext context;

	@Before("execution(* dk.bregnvig.formula1.server.*ServiceImpl.*(..))")
	public void storeContext() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		context.setPlayer((Player) requestAttributes.getRequest().getSession().getAttribute(SessionAttributes.PLAYER));
	}



	@After("execution(* dk.bregnvig.formula1.server.*ServiceImpl.*(..))")
	public void removeContext() {
		context.remove();
	}
	
	public void setContext(WebContext context) {
		this.context = context;
	}
}
