package dk.bregnvig.formula1.client.widget;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.validation.CompositeRule;
import dk.bregnvig.formula1.client.validation.IntegerRule;
import dk.bregnvig.formula1.client.validation.MaxLengthRule;
import dk.bregnvig.formula1.client.validation.RequiredRule;
import dk.bregnvig.formula1.client.validation.Rule;
import dk.bregnvig.formula1.client.widget.control.ContentTitleLabel;
import dk.bregnvig.formula1.client.widget.control.DriverImage;
import dk.bregnvig.formula1.client.widget.control.FormLabel;
import dk.bregnvig.formula1.client.widget.control.FormTitleLabel;

public class AdminDriverPanel extends ContentPanel {

	private MessagePanel messagePanel;
	private DriverInfoForm form;
	private boolean editing = false;
	private Button updateDriverInfo; 
	private final ListBox drivers;
	private List fetchedDrivers;
	private DriverImage driverImage;
	
	public AdminDriverPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		// setHeight("50%");
		Label header = new ContentTitleLabel("Opret eller rediger en køre");
		add(header);
		add(messagePanel = new MessagePanel());
		setCellHeight(messagePanel, "18px");
		
		drivers = new ListBox();
		drivers.setStyleName("form");
		drivers.addClickListener(new ClickListener() {
			public void onClick(Widget arg0) {
				if (drivers.getSelectedIndex() == 0) {
					form.setDriver(new ClientDriver());
					setEditing(false);
					return;
				}
				long id  = Long.parseLong(drivers.getValue(drivers.getSelectedIndex()));
				for (int i = 0; i < fetchedDrivers.size(); i++) {
					ClientDriver driver = (ClientDriver) fetchedDrivers.get(i);
					if (driver.getId().longValue() == id) {
						setEditing(true);
						form.setDriver(driver);
					}
				}
			}
		});
		loadDrivers();
		add(drivers);
		
		HorizontalPanel formPanel = new HorizontalPanel();
		formPanel.add(form = new DriverInfoForm());
		formPanel.add(driverImage = new DriverImage());
		formPanel.setCellVerticalAlignment(driverImage, HorizontalPanel.ALIGN_MIDDLE);
		add(formPanel);
		setCellVerticalAlignment(formPanel, VerticalPanel.ALIGN_TOP);
	}
	
	private void loadDrivers() {
		
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable exception) {
				getMediator().reportError(exception.getMessage());
			}

			public void onSuccess(Object argument) {
				fetchedDrivers = (List) argument;
				int previousSelection = drivers.getSelectedIndex();
				previousSelection = previousSelection == -1 ? 0 : previousSelection;
				drivers.clear();
				drivers.addItem("Opret");
				for (int i = 0; i < fetchedDrivers.size(); i++) {
					ClientDriver driver = (ClientDriver) fetchedDrivers.get(i);
					drivers.addItem(driver.getName(), driver.getId().toString());
				}
				drivers.setSelectedIndex(previousSelection);
			}
		};
		getMediator().getGameService().findAllDrivers(callback);
	}

	private class DriverInfoForm extends FlexTable {

		private Label driverNameLabel;
		private TextBox driverName;
		
		private Label driverNumberLabel;
		private TextBox driverNumber;
		
		private CheckBox active;
		
		private ClientDriver driver;

		public DriverInfoForm() {
			setStyleName("form");
			int row = 0; 
			driver = new ClientDriver();
			setWidget(row, 0, new FormTitleLabel("Køre information"));
			getFlexCellFormatter().setColSpan(row++, 0, 2);
			setWidget(row, 0, driverNameLabel = new FormLabel("Navn"));
			setWidget(row++, 1, driverName = new TextBox());

			setWidget(row, 0, driverNumberLabel = new FormLabel("Nummer"));
			setWidget(row++, 1, driverNumber = new TextBox());
			
			setWidget(row, 0, new FormLabel("Aktiv"));
			setWidget(row++, 1, active = new CheckBox());

			updateDriverInfo = new Button("Opret", new ClickListener() {

				public void onClick(Widget arg0) {
					if (validate() == true) {
						
						driver.setName(driverName.getText());
						driver.setNumber(Integer.parseInt(driverNumber.getText()));
						driver.setActive(active.isChecked());
						
						AsyncCallback callback = new AsyncCallback() {

							public void onFailure(Throwable exception) {
								getMediator().reportError(exception.getMessage());
							}

							public void onSuccess(Object arg0) {
								String message = isEditing() ? "Køreren er blevet opdateret" : "Køreren er blevet oprettet"; 
								messagePanel.setStatus(message);
								if (isEditing() == false) {
									getMediator().getSeason().getDrivers().add(driver);
									setDriver(new ClientDriver());
								}
							}

						};
						if (isEditing()) {
							getMediator().getGameService().updateDriver(driver, callback);
						} else {
							getMediator().getGameService().createDriver(driver, callback);
						}
						loadDrivers();
					}
				}
			});
			setWidget(row, 1, updateDriverInfo);
			getFlexCellFormatter().setColSpan(row++, 1, 2);
		}
		
		public void setDriver(ClientDriver driver) {
			if (driver == null) {
				return;
			}
			this.driver = driver;
			driverName.setText(driver.getName());
			driverNumber.setText(Integer.toString(driver.getNumber()));
			active.setChecked(driver.isActive());
			driverImage.setDriver(driver);
		}

		private boolean validate() {
			Rule[] rules = new Rule[] {
				new CompositeRule().addRule(new RequiredRule(driverName, driverNameLabel)).addRule(new MaxLengthRule(driverName, driverNameLabel, 255)),
				new CompositeRule().addRule(new RequiredRule(driverNumber, driverNumberLabel)).addRule(new IntegerRule(driverNumber, driverNumberLabel)).addRule(new MaxLengthRule(driverNumber, driverNameLabel, 255)),
				};
			List issues = AdminDriverPanel.this.validate(rules);

			if (issues.size() != 0) {
				messagePanel.setError("Kunne ikke opdatere dine informationer. Se de markerede felter");
			}

			unmarkFields(rules);
			markIssues(issues);

			return issues.size() == 0 ? true : false;
		}
	}
	
	private boolean isEditing() {
		return editing;
	}
	
	private void setEditing(boolean editing) {
		if (editing == true) {
			updateDriverInfo.setText("Opdater");
		} else {
			updateDriverInfo.setText("Opret");
		}
		this.editing = editing;

	}

	protected String getScreenTitle() {
		return "Administrer køre";
	}
}
