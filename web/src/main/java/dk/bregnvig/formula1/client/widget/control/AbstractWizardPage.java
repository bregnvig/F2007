package dk.bregnvig.formula1.client.widget.control;

import java.util.List;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.validation.Issue;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.widget.ContentPanel;
import dk.bregnvig.formula1.client.widget.MainPanel;

public abstract class AbstractWizardPage extends ContentPanel {

	public AbstractWizardPage(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		setWidth("100%");
		setHeight("100%");
	}
	
	protected abstract Rule[] getRules();
	
	public boolean validate() {
		List<Issue> issues = validate(getRules());
		unmarkFields(getRules());
		markIssues(issues);
		
		return issues.size() == 0 ? true : false;
	}
}
