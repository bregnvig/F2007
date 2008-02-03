package dk.bregnvig.formula1.client.widget.bid;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.validation.MaxValueRule;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.widget.MainPanel;
import dk.bregnvig.formula1.client.widget.control.AbstractWizardPage;
import dk.bregnvig.formula1.client.widget.control.BigLabel;
import dk.bregnvig.formula1.client.widget.control.FormLabel;
import dk.bregnvig.formula1.client.widget.control.FormTextBox;

public class EnterPolePositionPanel extends AbstractWizardPage {
	
	private ClientRace race;
	private PolePostionPanel polePosition;
	
	public EnterPolePositionPanel(F2007 mediator, MainPanel mainPanel, ClientRace race) {
		super(mediator, mainPanel);
		this.race = race;
		FlexTable grid = new FlexTable();
		grid.setCellPadding(10);
		grid.setWidget(0, 0, new BigLabel("Pole position " + race.getName()));
		grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, HorizontalPanel.ALIGN_CENTER);
		grid.getFlexCellFormatter().setColSpan(0, 0, 2);
		grid.setWidget(1, 0, new FormLabel("Forventet pole position tid"));
		grid.setWidget(1, 1, polePosition = new PolePostionPanel());
		
		add(grid);
		setCellHorizontalAlignment(grid, VerticalPanel.ALIGN_CENTER);
	}
	
	protected String getScreenTitle() {
		return "Gæt pole position tiden i " + race.getName();
	}

	protected Rule[] getRules() {
		Rule[] rules = new Rule[3];
		rules[0] = new MaxValueRule(polePosition.getMinutes(), "Minutter", 1);
		rules[1] = new MaxValueRule(polePosition.getSeconds(), "Sekunder", 59);
		rules[2] = new MaxValueRule(polePosition.getMiliseconds(), "Millisekunder", 999);
		return rules;
	}
	
	public int getPolePosition() {
		return polePosition.getPolePositionTime();
	}
	
	private class PolePostionPanel extends HorizontalPanel {
		
		private FormTextBox minutes;
		private FormTextBox seconds;
		private FormTextBox milliseconds;
		
		public PolePostionPanel() {
			
			minutes = new FormTextBox("Minutter");
			minutes.setMaxLength(1);
			minutes.setVisibleLength(1);
			minutes.setTabIndex(1);
			
			seconds = new FormTextBox("Sekunder");
			seconds.setMaxLength(2);
			seconds.setVisibleLength(2);
			seconds.setTabIndex(2);
			
			milliseconds = new FormTextBox("Millisekunder");
			milliseconds.setMaxLength(3);
			milliseconds.setVisibleLength(3);
			milliseconds.setTabIndex(3);
			
			add(minutes);
			add(new Label(":"));
			add(seconds);
			add(new Label(":"));
			add(milliseconds);
		}

		TextBox getMinutes() {
			return minutes;
		}

		TextBox getSeconds() {
			return seconds;
		}

		TextBox getMiliseconds() {
			return milliseconds;
		}
		
		int getPolePositionTime() {
			int millis = Integer.parseInt(minutes.getText()) * 60 * 1000;
			millis += Integer.parseInt(seconds.getText()) * 1000;
			millis += Integer.parseInt(milliseconds.getText());
			return millis;
		}
	}

}
