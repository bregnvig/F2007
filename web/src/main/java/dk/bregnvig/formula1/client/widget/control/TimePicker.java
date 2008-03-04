package dk.bregnvig.formula1.client.widget.control;

import java.util.Date;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;

public class TimePicker extends FlowPanel {
	
	private ListBox hour = new ListBox();
	private ListBox minute = new ListBox();
	
	private Date time;
	
	public TimePicker(Date time) {
		for(int i = 0; i < 24; i++) {
			hour.addItem(Integer.toString(i));
		}
		for (int i = 0; i < 60; i ++) {
			minute.addItem((i < 10 ? "0" : "") + Integer.toString(i));
		}
		setTime(time);
		add(hour);
		add(minute);
	}
	
	public void setTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		this.time = date;
		initHours();
		initMinutes();
	}
	
	public void setDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		date.setHours(time.getHours());
		date.setMinutes(time.getMinutes());
		date.setSeconds(0);
		time = date;
	}
	
	public Date getDate() {
		time.setSeconds(0);
		time.setHours(hour.getSelectedIndex());
		time.setMinutes(minute.getSelectedIndex());
		return time;
	}
	
	private void initHours() {

		hour.setItemSelected(time.getHours(), true);
	}
	
	private void initMinutes() {
		minute.setItemSelected(time.getMinutes(), true);
	}
}
