package dk.bregnvig.formula1.client.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;

public class CompositeRule implements Rule {

	private Set<Widget> fields = new HashSet<Widget>(5); 
	
	private List<Rule> rules = new ArrayList<Rule>();
	
	public CompositeRule addRule(Rule rule) {
		rules.add(rule);
		fields.addAll(rule.getFields());
		return this;
	}

	/**
	 * @return A list of issueds or an empty list. Never null.
	 */
	public List<Issue> validate() {
		
		List<Issue> issues = new ArrayList<Issue>();
		
		for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext();) {
			Rule rule = iterator.next();
			issues.addAll(rule.validate());
		}
		return issues;
	}

	public Set<Widget> getFields() {
		return fields;
	}

	public boolean supendValidation() {
		return false;
	}
}
