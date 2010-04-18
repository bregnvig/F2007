package dk.bregnvig.formula1.client.validation;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;

public interface Rule {
	
	
	List<Issue> validate();
	
	/**
	 * Returns true if any futher validation should be suspended. 
	 * @return
	 */
	boolean supendValidation();
	
	Set<Widget> getFields();
	
}
