package dk.bregnvig.formula1.autoresult.impl;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.autoresult.AutomaticResult;

/**
 * This class is not thread safe
 * @author flemming
 *
 */
public class AutomaticResultImpl implements AutomaticResult {

	private String url;
	
	public void setURL(String url) {
		this.url = url;
	}
	
	public RaceResult getRaceResult(Race race) {
		QualifyParser qualifyParser = new QualifyParser();
		FastestLapParser fastestLapParser = new FastestLapParser();
		ResultParser resultParser = new ResultParser();
		
		RaceResult result = new RaceResult();
		qualifyParser.setURL(getQualifyURL());
		qualifyParser.parse(race, result);
		
		fastestLapParser.setURL(getFastestLapURL());
		fastestLapParser.parse(race, result);
		
		resultParser.setURL(getResultURL());
		resultParser.parse(race, result);
		
		return result;
	}
	
	String getResultURL() {
		if (url.endsWith("/")) {
			return url;
		}
		return url.substring(0, url.lastIndexOf('/')+1);
	}
	
	String getFastestLapURL() {
		return getResultURL();
	}
	
	String getQualifyURL() {
		String resultURL = getResultURL();
		String partURl = resultURL.substring(0, resultURL.lastIndexOf('/', resultURL.length()-2)+1);
		String textNumber = resultURL.substring(resultURL.lastIndexOf('/', resultURL.length()-2)+1, resultURL.lastIndexOf('/'));
		return partURl + Integer.toString(Integer.parseInt(textNumber)-1) + "/";
	}
}
