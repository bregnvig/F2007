package dk.bregnvig.formula1.client.widget.control;

import java.util.Date;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;

import dk.bregnvig.formula1.client.domain.ClientRace;

public class CountDownLabel extends Label {
	
	private boolean useSeconds = true;
	private int days;
	private int hours;
	private int minutes;
	private int totalMinutes;
	private int totalSeconds;
	private CountDownTimer timer;
	
	public CountDownLabel(ClientRace race) {
		setRace(race);
	}
	
	public CountDownLabel() {
		setText(" ");
		setHeight("14px");
	}
	
	public void useSeconds() {
		useSeconds = true;
	}
	
	public void useMinutes() {
		useSeconds = false;
	}
	
	public void setRace(ClientRace race) {
		
		if (race == null) {
			throw new IllegalStateException("Race cannot be null");
		}
		
		if (timer != null) {
			setText(" ");
			timer.cancel();
		}
		
		if (race.isClosed() || race.isCompleted()) {
			return;
		}
		Date now = new Date();
		Date raceDate = (race.isOpened() ? race.getCloseDate() : race.getOpenDate());
		long difference = raceDate.getTime() - now.getTime();
		difference = Math.abs(difference);
		days = (int) (difference / (1000 * 60 *60 * 24));
		hours = (int) ((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
		minutes = (int) ((difference % (1000 * 60 * 60)) / (1000*60));
		totalMinutes = (int) ((difference / (1000*60)));
		totalSeconds = (int) ((difference / (1000)));
		timer = new CountDownTimer();
		timer.scheduleRepeating(1000);
	}

	private class CountDownTimer extends Timer {

		public void run() {
			if (totalSeconds % 60 == 0) {
				if (minutes == 0) {
					if (isTimeExpired() == false) {
						minutes = 60;
					} else {
						setText("-- minutter");
						cancel();
						return;
					}
				}
				if (minutes == 60) {
					if (hours == 0 && days > 0) {
						hours = 24;
						days--;
					}
					hours--;
				}
				minutes--;
				totalMinutes--;
			}
			totalSeconds--;
			if (useSeconds == true) {
				setText(Integer.toString(totalSeconds) + (totalSeconds == 1 ? " sekund" : " sekunder"));
			} else {
				setText(getMaxValue());
			}
			String title = "";
			if (days != 0) {
				title = Integer.toString(days) + (days == 1 ? " dag, " : " dage, ");
			}
			title += Integer.toString(hours) + (hours == 1 ? " time" : " timer");
			title += " og " + Integer.toString(minutes) + (minutes == 1 ? " minut" : " minutter");
			setTitle(title.trim());
		}
		
		private String getMaxValue() {
			if (days != 0) {
				return Integer.toString(days) +  (days == 1 ? " dag" : " dage");
			} else if (hours != 0) {
				return Integer.toString(hours) +  (hours == 1 ? " time" : " timer");
			} else {
				return Integer.toString(minutes) +  (minutes == 1 ? " minut" : " minutter");
			}
		}
	}

	public boolean isTimeExpired() {
		return hours == 0 && days == 0;
	}
	
}
