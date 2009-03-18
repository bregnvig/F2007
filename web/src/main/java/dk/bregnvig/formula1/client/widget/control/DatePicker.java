package dk.bregnvig.formula1.client.widget.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class DatePicker extends FlowPanel {
	
	private ListBox year = new ListBox();
	private ListBox month = new ListBox();
	private ListBox day = new ListBox();
	
	private Date date;
	
	private List<Month> months;
	
	public DatePicker(Date date) {

		setDate(date);
		
		year.addClickListener(new ClickListener() {
			public void onClick(Widget arg0) {
				initMonth();
			}
		});
		
		month.addClickListener(new ClickListener() {
			public void onClick(Widget arg0) {
				initDay();
			}
		});

		add(day);
		add(month);
		add(year);
	}
	
	public void setDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		this.date = date;
		initYear();
		initMonth();
		day.setSelectedIndex(date.getDate()-1);
	}
	
	public Date getDate() {
		int year = Integer.parseInt(this.year.getItemText(this.year.getSelectedIndex()));
		Date date = new Date(year-1900, month.getSelectedIndex(), this.day.getSelectedIndex()+1);
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}
	
	private void initYear() {
		year.clear();
		for(int i = 0; i < 3; i++) {
			year.addItem(Integer.toString(date.getYear()+1900+i));
		}
	}
	
	private void initMonth() {
		month.clear();
		months = new ArrayList(12);
		for (int i = 0; i < 12; i ++) {
			Month month = new Month(Integer.parseInt(year.getItemText(year.getSelectedIndex())), i);
			months.add(month);
			this.month.addItem(month.getName(), Integer.toString(month.getNumber()));
		}
		month.setItemSelected(date.getMonth(), true);
		initDay();
	}
	
	private Month getSelectedMonth() {
		return (Month) months.get(month.getSelectedIndex());
	}
	
	private void initDay() {
		int previousIndex = day.getSelectedIndex();
		day.clear();
		Month selectedMonth = (Month) months.get(month.getSelectedIndex());
		for (int i = 0; i < getSelectedMonth().getDays(); i++) {
			day.addItem(Integer.toString(i+1));
		}
		if (selectedMonth.getDays() < previousIndex+1) {
			day.setSelectedIndex(selectedMonth.getDays()-1);
		} else {
			day.setSelectedIndex(previousIndex < 0 ? 0 : previousIndex);
		}
	}
	
	public static class Month {
		private int number;
		private int year;
		
		public Month(int year, int number) {
			this.number = number;
			this.year = year;
		}
		
		public int getNumber() {
			return number;
		}
		
		public String getName() {
			return (String) names.get(getNumber());
		}
		
		public int getDays() {
			if (number == 0 || number == 2 || number == 4 || number == 6 || number == 7 || number == 9 || number == 11) {
				return 31;
			}
			if (number == 1) {
				if (year % 4 == 0) {
					return 29;
				} else {
					return 28;
				}
			}
			return 30;
		}

		
		private static List<String> names = new ArrayList(12);
		static {
			names.add("Januar");
			names.add("Februar");
			names.add("Marts");
			names.add("April");
			names.add("Maj");
			names.add("Juni");
			names.add("Juli");
			names.add("August");
			names.add("September");
			names.add("Oktober");
			names.add("November");
			names.add("December");
		}
	}
}
