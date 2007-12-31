package dk.bregnvig.formula1.client.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.validation.Issue;
import dk.bregnvig.formula1.client.validation.Rule;

public abstract class ContentPanel extends VerticalPanel {

	private F2007 mediator;
	private MainPanel mainPanel;
	
	public ContentPanel(F2007 mediator, MainPanel mainPanel) {
		this.mediator = mediator;
		this.mainPanel = mainPanel;
		
		mainPanel.setScreenTitle(getScreenTitle());
		
		setWidth("80%");
		setHeight("90%");
	}
	
	abstract String getScreenTitle();

	public F2007 getMediator() {
		return mediator;
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}

	protected void unmarkFields(Rule[] rules) {
		for (int i = 0; i < rules.length; i++) {
			for (Iterator iterator = rules[i].getFields().iterator(); iterator.hasNext();) {
				TextBoxBase textbox = (TextBoxBase) iterator.next();
				textbox.removeStyleDependentName("error");
				textbox.setTitle("");
			}
		}
	}
	
	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.validation.Issue>
	 */
	protected void markIssues(List issues) {
		for (Iterator iterator = issues.iterator(); iterator.hasNext();) {
			Issue issue = (Issue) iterator.next();
			issue.getField().addStyleDependentName("error");
			issue.getField().setTitle(issue.getIssue());
		}
	}
	
	/**
	 * @gwt.typeArgs <dk.bregnvig.formula1.client.validation.Issue>
	 */
	protected List validate(Rule[] rules) {
		List result = new ArrayList();
		
		for (int i = 0; i < rules.length; i++) {
			result.addAll(rules[i].validate());
			if (rules[i].supendValidation() == true) {
				break;
			}
		}
		return result;
	}
	
}
