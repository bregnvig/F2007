package dk.bregnvig.formula1.client.widget;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.bregnvig.formula1.client.F2007;
import dk.bregnvig.formula1.client.widget.control.BigLabel;

public class RulesPanel extends ContentPanel {

	public RulesPanel(F2007 mediator, MainPanel mainPanel) {
		super(mediator, mainPanel);
		add(qualifyRules());
		add(fastestRules());
		add(podiumRules());
		add(selectedDriverRules());
		add(firstCrashRules());
		add(polePostionRules());
		add(wbcRules());
		setSpacing(10);
	}

	public Panel qualifyRules() {
		VerticalPanel qualifyRules = new VerticalPanel();
		qualifyRules.add(new BigLabel("Gæt qualify"));
		HTML rule = new HTML();
		qualifyRules.add(rule);
		rule.setHTML("<p>" +
			"Du skal gætte de seks første i qualify. Det betyder at det er kvalificerings" +
			" resultatet der gælder. Ikke startsplaceringen søndag morgen.<br/>" +
			"Der er to point for et korrekt gæt. Et point hvis du er en ved siden af. Derefter nul point." +
			"</p>");
		rule.setStyleName("gwt-Label");
		
		return qualifyRules;
	}

	public Panel fastestRules() {
		VerticalPanel fastestLapRules = new VerticalPanel();
		fastestLapRules.add(new BigLabel("Gæt hurtigste omgang"));
		HTML rule = new HTML();
		fastestLapRules.add(rule);
		rule.setHTML("<p>" +
			"Du skal gætte hvilken køre der laver den hurtigst omgang i løbet.<br/>" +
			"Der er to point for korrekt gættet kører. Ellers nul point." +
			"</p>");
		rule.setStyleName("gwt-Label");
		
		return fastestLapRules;
	}
	
	public Panel podiumRules() {
		VerticalPanel podiumRules = new VerticalPanel();
		podiumRules.add(new BigLabel("Gæt podium"));
		HTML rule = new HTML();
		podiumRules.add(rule);
		rule.setHTML("<p>" +
			"Du skal gætte de tre på podiet.<br/>" +
			"Der er to point for et korrekt gæt. Et point hvis du er en ved siden af. Derefter nul point." +
			"</p>");
		rule.setStyleName("gwt-Label");
		
		return podiumRules;
	}

	public Panel selectedDriverRules() {
		VerticalPanel selectedDriverRules = new VerticalPanel();
		selectedDriverRules.add(new BigLabel("Gæt udvalgte køre"));
		HTML rule = new HTML();
		selectedDriverRules.add(rule);
		rule.setHTML("<p>" +
			"Du skal gætte den udvalgte køres start og slut position.<br/>" +
			"Hvis køren udgår som den første vil hans slut position være nummer 22. Så selvom køren i FIA" +
			" regi er <i>retired</i> tæller det stadigvæk som en position i vores spil.<br/>" +
			"Der er tre point for et korrekt gæt. To point hvis du er en ved siden af. Et point hvis du er to ved siden af. Derefter nul point." +
			"</p>");
		rule.setStyleName("gwt-Label");
		
		return selectedDriverRules;
	}
	
	public Panel firstCrashRules() {
		VerticalPanel firstCrashRules = new VerticalPanel();
		firstCrashRules.add(new BigLabel("Gæt først udgået"));
		HTML rule = new HTML();
		firstCrashRules.add(rule);
		rule.setHTML("<p>" +
			"Du skal gætte hvilken køre der først udgår.<br/>" +
			"Hvis køreren ikke stiller op er han ikke udgået, men dit bud overgår automatisk til erstatningskøreren.<br/>" +
			"Der er tre point for et korrekt gæt. To point hvis du er én ved siden af. Et point hvis du er to ved siden af. Derefter nul point." +
			"</p>");
		rule.setStyleName("gwt-Label");
		
		return firstCrashRules;
	}
	
	public Panel polePostionRules() {
		VerticalPanel polePositionRules = new VerticalPanel();
		polePositionRules.add(new BigLabel("Gæt poleposition tiden"));
		HTML rule = new HTML();
		polePositionRules.add(rule);
		rule.setHTML("<p>" +
			"Du skal gætte den hurtigste tid i den tredie qualify.<br/>" +
			"Dette bud giver ingen points og benyttes <b>kun</b> i forbindelse med WBC. Budet benyttes kun i tilfældet af to spillere har" +
			" samme antal points. I det tilfælde vil den der er tættest på poleposition tiden være vinder." +
			"</p>");
		rule.setStyleName("gwt-Label");
		
		return polePositionRules;
	}

	public Panel wbcRules() {
		VerticalPanel wbcRules = new VerticalPanel();
		wbcRules.add(new BigLabel("WBC"));
		HTML rule = new HTML();
		wbcRules.add(rule);
		rule.setHTML("<p>" +
			"WBC er det samlede mesterskab. Ved afgørelsen af hvert løb vil der blive uddelt point efter samme metode" +
			" som i formel 1. Med andre ord, hvis du vinder et løb bliver du tildelt ti WBC point. Bliver du nummer 2 " +
			" bliver du tildelt 8 point og så videre.<br/>" +
			"Alle spillere bliver tildelt WBC point - selv dem der ikke deltager. Men kun deltagere spiller med om den store pulje." +
			"</p>");
		rule.setStyleName("gwt-Label");
		
		return wbcRules;
	}
	
	protected Widget getScreenTitle() {
		return new  BigLabel("Regler");
	}

}
