package dk.bregnvig.formula1.scraping.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.bid.GridResult;

class QualifyParser extends AbstractParser<QualifyParser.QualifyRow> {
	
	private List<String> headers = new ArrayList<String>(8);
	
	public QualifyParser() {
		headers.add("Pos");
		headers.add("No");
		headers.add("Driver");
		headers.add("Team");
		headers.add("Q1");
		headers.add("Q2");
		headers.add("Q3");
		headers.add("Laps");
	}

	@Override
	List<String> getHeaders() {
		return headers;
	}

	@Override
	String getHtmlFileName() {
		return "results.html";
	}

	@Override
	QualifyParser.QualifyRow parserRow(NodeList tableColumns) {
		return new QualifyRow(tableColumns);
	}
	
	static class QualifyRow {
		private static final int POS_POSITION = 0;
		private static final int NO_POSITION = 1;
		private static final int DRIVER_POSITION = 2;
		private static final int Q3_POSITION = 6;

		private NodeList tableColumns;
		
		private QualifyRow(NodeList tableColumns) {
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
		
		int getPolePositionTime() {
			String time = tableColumns.elementAt(Q3_POSITION).getFirstChild().getText();
			StringTokenizer minuttes = new StringTokenizer(time, ":");
			int result = Integer.parseInt(minuttes.nextToken().trim())*60*1000;
			StringTokenizer rest = new StringTokenizer(minuttes.nextToken().trim(), ".");
			result += Integer.parseInt(rest.nextToken().trim())*1000;
			result += Integer.parseInt(rest.nextToken().trim());
			return result;
		}

		@Override
		public String toString() {
			return "Position: " + getPosition() + " Driver: " + getDriverNumber() +". " + getDriverName() + " Pole position time: " + getPolePositionTime();
		}
	}
	
	private GridResult getGridResult() {
		GridResult result = new GridResult();
		result.setPosition1(getDrivers().get(getRows().get(0).getDriverNumber()));
		result.setPosition2(getDrivers().get(getRows().get(1).getDriverNumber()));
		result.setPosition3(getDrivers().get(getRows().get(2).getDriverNumber()));
		result.setPosition4(getDrivers().get(getRows().get(3).getDriverNumber()));
		result.setPosition5(getDrivers().get(getRows().get(4).getDriverNumber()));
		result.setPosition6(getDrivers().get(getRows().get(5).getDriverNumber()));
		result.setPosition7(getDrivers().get(getRows().get(6).getDriverNumber()));
		return result;
	}


	@Override
	void parse(Race race, RaceResult result) {
		super.parse(race, result);
		result.setPolePositionTimeMillis(getRows().get(0).getPolePositionTime());
		result.setGrid(getGridResult());
	}
}
