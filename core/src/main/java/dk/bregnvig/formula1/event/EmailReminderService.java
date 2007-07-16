package dk.bregnvig.formula1.event;

public class EmailReminderService extends AbstractRaceTimer {
	
	public void invoke() {
		System.err.println("Race " + getRace().getName());
	}
}
