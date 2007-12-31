package dk.bregnvig.formula1.client.validation;

import java.util.List;
import java.util.Set;

public interface Rule {
	
	
	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.validation.Issue>
	 */
	List validate();
	
	/**
	 * Returns true if any futher validation should be suspended. 
	 * @return
	 */
	boolean supendValidation();
	
	/**
	 * @gwt.typeArgs <com.google.gwt.user.client.ui.TextBoxBase>
	 */
	Set getFields();
	
}
