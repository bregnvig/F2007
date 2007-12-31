package dk.bregnvig.formula1.client.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CompositeRule implements Rule {

	/**
	 * @gwt.typeArgs <com.google.gwt.user.client.ui.TextBoxBase>
	 */
	private Set fields = new HashSet(5); 
	
	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.validation.Rule>
	 */
	private List rules = new ArrayList();
	
	public CompositeRule addRule(Rule rule) {
		rules.add(rule);
		fields.addAll(rule.getFields());
		return this;
	}

	/**
	 * @return A list of issueds or an empty list. Never null.
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.validation.Issue>
	 */
	public List validate() {
		
		List issues = new ArrayList();
		
		for (Iterator iterator = rules.iterator(); iterator.hasNext();) {
			Rule rule = (Rule) iterator.next();
			issues.addAll(rule.validate());
		}
		return issues;
	}

	public Set getFields() {
		return fields;
	}

	public boolean supendValidation() {
		return false;
	}
}
