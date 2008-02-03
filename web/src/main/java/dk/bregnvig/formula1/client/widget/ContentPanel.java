package dk.bregnvig.formula1.client.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.validation.Issue;
import dk.bregnvig.formula1.client.validation.Rule;

public abstract class ContentPanel extends VerticalPanel {

	private F2007 mediator;
	private MainPanel mainPanel;
	
	public ContentPanel(F2007 mediator, MainPanel mainPanel) {
		this.mediator = mediator;
		this.mainPanel = mainPanel;
		
		setWidth("80%");
		setHeight("90%");
	}

	protected void onLoad() {
		super.onLoad();
		mainPanel.setScreenTitle(getScreenTitle());
	}

	protected abstract String getScreenTitle();

	public F2007 getMediator() {
		return mediator;
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}

	protected void unmarkFields(Rule[] rules) {
		for (int i = 0; i < rules.length; i++) {
			for (Iterator iterator = rules[i].getFields().iterator(); iterator.hasNext();) {
				Widget widget = (Widget) iterator.next();
				if (widget.getStylePrimaryName().equals("error") == false) {
					widget.removeStyleDependentName("error");
				} else {
					widget.removeStyleName("error");
				}
				widget.setTitle("");
			}
		}
	}
	
	/**
	 * @gwt.typeArgs issues <dk.bregnvig.formula1.client.validation.Issue>
	 */
	protected void markIssues(List issues) {
		for (Iterator iterator = issues.iterator(); iterator.hasNext();) {
			Issue issue = (Issue) iterator.next();
			if (issue.getField().getStylePrimaryName().length() != 0) {
				issue.getField().addStyleDependentName("error");
			} else {
				issue.getField().setStyleName("error");
			}
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
