package dk.bregnvig.formula1.scraping.impl;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.bid.FastestLapBid;

class FastestLapParser extends AbstractParser<FastestLapParser.FastestLapRow> {
	
	private List<String> headers = new ArrayList<String>(8);
	
	public FastestLapParser() {
		headers.add("Pos");
		headers.add("No");
		headers.add("Driver");
		headers.add("Team");
		headers.add("Lap");
		headers.add("Time Of Day");
		headers.add("Avg Speed");
		headers.add("Time");
	}

	@Override
	List<String> getHeaders() {
		return headers;
	}

	@Override
	String getHtmlFileName() {
		return "fastest_laps.html";
	}

	@Override
	FastestLapParser.FastestLapRow parserRow(NodeList tableColumns) {
		return new FastestLapRow(tableColumns);
	}
	
	static class FastestLapRow {
		private static int pos = 0;
		private static final int POS_POSITION = pos++;
		private static final int NO_POSITION = pos++;
		private static final int DRIVER_POSITION = pos++;

		private NodeList tableColumns;
		
		private FastestLapRow(NodeList tableColumns) {
			this.tableColumns = tableColumns;
		}
		
		int getPosition() {
			return Integer.parseInt(tableColumns.elementAt(POS_POSITION).getFirstChild().getText());
		}

		int getDriverNumber() {
			return Integer.parseInt(tableColumns.elementAt(NO_POSITION).getFirstChild().getText());
		}

		String getDriverName() {
			return ((LinkTag)tableColumns.elementAt(DRIVER_POSITION).getFirstChild()).getLinkText();
		}

		@Override
		public String toString() {
			return "Position: " + getPosition() + " Driver: " + getDriverNumber() +". " + getDriverName();
		}
	}

	@Override
	void parse(Race race, RaceResult result) {
		super.parse(race, result);
		Driver driver = getDrivers().get(getRows().get(0).getDriverNumber());
		FastestLapBid fastestLapResult = new FastestLapBid();
		fastestLapResult.setDriver(driver);
		result.setFastestLap(fastestLapResult);
	}
	
}
