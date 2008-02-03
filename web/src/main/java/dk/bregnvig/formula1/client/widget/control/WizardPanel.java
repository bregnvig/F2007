package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WizardPanel extends DockPanel {
	
	private AbstractWizardPage[] pages;
	private AbstractWizardPage currentCenter;
	
	private int currentPageIndex = 1;

	public WizardPanel(AbstractWizardPage[] pages) {
		this.pages = pages;
		setWidth("97%");
		turnPage();
		ButtonPanel buttonPanel = new ButtonPanel(); 
		add(buttonPanel, DockPanel.SOUTH);
		buttonPanel.changeState();
	}
	
	private class ButtonPanel extends HorizontalPanel {
		
		private final Button previous = new Button("Forrige");
		private final Button next = new Button("Næste");
		private final Button finish = new Button("Færdig");
		private final Label pages = new Label();
		
		public ButtonPanel() {
			setWidth("100%");
			setHeight("40px");
			setStyleName("northBorder");
			previous.setEnabled(false);
			previous.addClickListener(new ClickListener() {
				public void onClick(Widget arg0) {
					if (currentCenter.validate() && currentPageIndex > 1) {
						currentPageIndex--;
						turnPage();
						changeState();
					}
				}
			});
			
			next.setEnabled(false);
			next.addClickListener(new ClickListener() {
				public void onClick(Widget arg0) {
					if (currentCenter.validate() && WizardPanel.this.pages.length > currentPageIndex) {
						currentPageIndex++;
						turnPage();
						changeState();
					}
				}
			});
			finish.setEnabled(false);
			finish.addClickListener(new ClickListener() {
				public void onClick(Widget arg0) {
					if (currentCenter.validate()) {
						Window.alert("Færdig...");
					}
				}
			});
			FlowPanel tempPanel = new FlowPanel();
			tempPanel.setStyleName("buttonPanel");
			tempPanel.add(previous);
			tempPanel.add(next);
			tempPanel.add(finish);
			FlowPanel pagesPanel = new FlowPanel();
			pagesPanel.setStyleName("buttonPanel");
			pagesPanel.add(pages);
			add(pagesPanel);
			setCellHorizontalAlignment(pages, HorizontalPanel.ALIGN_LEFT);
			add(tempPanel);
			setCellHorizontalAlignment(tempPanel, HorizontalPanel.ALIGN_RIGHT);
		}
		
		public void changeState() {
			previous.setEnabled(currentPageIndex != 1);
			next.setEnabled(WizardPanel.this.pages.length != currentPageIndex);
			finish.setEnabled(WizardPanel.this.pages.length == currentPageIndex);
			pages.setText("Side " + (currentPageIndex) + " af " + WizardPanel.this.pages.length);
		}
	}
	
	private void turnPage() {
		if (currentCenter != null) {
			remove(currentCenter);
		}
		currentCenter = pages[currentPageIndex-1];
		add(currentCenter, DockPanel.CENTER);
		setCellHeight(currentCenter, "400px");
		setCellVerticalAlignment(currentCenter, DockPanel.ALIGN_MIDDLE);
	}
	
	

}
