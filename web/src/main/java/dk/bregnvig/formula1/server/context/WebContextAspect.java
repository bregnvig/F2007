package dk.bregnvig.formula1.server.context;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.core.annotation.Order;

import dk.bregnvig.formula1.server.security.User;

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
		context.setUser((User) ServletUtils.getRequest().getSession().getAttribute("user"));
		System.out.println("************************** " + ServletUtils.getRequest().getSession().getAttribute("user"));
	}



	@After("execution(* dk.bregnvig.formula1.server.*ServiceImpl.*(..))")
	public void removeContext() {
		context.remove();
		System.out.println("************************** " + ServletUtils.getRequest().getSession().getAttribute("user"));
	}
	
	public void setContext(WebContext context) {
		this.context = context;
	}
}
