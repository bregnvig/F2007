package dk.bregnvig.formula1.scraping.impl;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.bid.FirstCrashResult;
import dk.bregnvig.formula1.bid.PodiumResult;
import dk.bregnvig.formula1.bid.SelectedDriverBid;

class ResultParser extends AbstractParser<ResultParser.ResultRow> {
	
	private List<String> headers = new ArrayList<String>(8);
	private int number = 1;
	
	public ResultParser() {
		headers.add("Pos");
		headers.add("No");
		headers.add("Driver");
		headers.add("Team");
		headers.add("Laps");
		headers.add("Time/Retired");
		headers.add("Grid");
		headers.add("Pts");
	}

	
	private PodiumResult getPodiumResult() {
		PodiumResult result = new PodiumResult();
		result.setPosition1(getDrivers().get(getRows().get(0).getDriverNumber()));
		result.setPosition2(getDrivers().get(getRows().get(1).getDriverNumber()));
		result.setPosition3(getDrivers().get(getRows().get(2).getDriverNumber()));
		result.setPosition4(getDrivers().get(getRows().get(3).getDriverNumber()));
		return result;
	}
	
	private FirstCrashResult getFirstCrashResult() {
		FirstCrashResult result = new FirstCrashResult();
		ResultRow first = null;
		ResultRow second = null;
		ResultRow third = null;
		for(int i = getRows().size()-1; i >= 0; i--) {
			ResultRow row = getRows().get(i);
			if (row.isDisqualified() == true) {
				continue;
			}
			if (row.isRetired() == false) {
				break;
			}
			if (first == null) {
				first = row;
			} else if (second == null) {
				second = row;
			} else if (third == null) {
				third = row;
			} else {
				break;
			}
		}
		
		if (first != null) {
			result.setCrash1(getDrivers().get(first.getDriverNumber()));
		}
		if (second != null) {
			result.setCrash2(getDrivers().get(second.getDriverNumber()));
		}
		if (third != null) {
			result.setCrash3(getDrivers().get(third.getDriverNumber()));
		}
		return result;
	}
	
	private SelectedDriverBid getSelectedDriverResult(Race race) {
		SelectedDriverBid result = new SelectedDriverBid();
		for (ResultRow resultRow : getRows()) {
			if(race.getSelectedDriver().getNumber() == resultRow.getDriverNumber()) {
				result.setStartPosition(resultRow.getGridPosition());
				result.setEndPosition(resultRow.getPosition());
				break;
			}
		}
		return result;
	}
	
//	private FastestLapBid getFastestLapResult() {
//		FastestLapBid result = new FastestLapBid();
//		result.setDriver(driver)
//	}
	
	
	
	static class ResultRow {
		private static int pos = 0;
		private static final int POS_POSITION = pos++;
		private static final int NO_POSITION = pos++;
		private static final int DRIVER_POSITION = pos++;
		private static final int TEAM_POSITION = pos++;
		private static final int LAPS_POSITION = pos++;
		@SuppressWarnings("unused")
		private static final int TIME_RETIRED_POSITION = pos++;
		private static final int GRID_POSITION = pos++;
		private static final int POINTS_POSITION = pos++;
		
		private NodeList tableColumns;
		private int resultPosition;
		
		private ResultRow(NodeList tableColumns, int resultPosition) {
			this.tableColumns = tableColumns;
			this.resultPosition = resultPosition;
		}
		
		int getPosition() {
			return resultPosition;
		}

		int getDriverNumber() {
			return Integer.parseInt(tableColumns.elementAt(NO_POSITION).getFirstChild().getText());
		}

		String getDriverName() {
			return ((LinkTag)tableColumns.elementAt(DRIVER_POSITION).getFirstChild()).getLinkText();
		}

		String getTeamName() {
			return tableColumns.elementAt(TEAM_POSITION).getFirstChild().getText();
		}

		int getLaps() {
			return Integer.parseInt(tableColumns.elementAt(LAPS_POSITION).getFirstChild().getText());
		}
		
		int getGridPosition() {
			return Integer.parseInt(tableColumns.elementAt(GRID_POSITION).getFirstChild().getText());
		}

		int getPoints() {
			String points = tableColumns.elementAt(POINTS_POSITION).getFirstChild().getText();
			return points.isEmpty() ? 0 : Integer.parseInt(points);
		}

		boolean isRetired() {
			boolean retired = true;
			try {
				Integer.parseInt(tableColumns.elementAt(POS_POSITION).getFirstChild().getText());
				retired = false;
			} catch (NumberFormatException e) {	}
			return retired;
		}
		
		boolean isDisqualified() {
			return tableColumns.elementAt(POS_POSITION).getFirstChild().getText().equals("DSQ");
		}
		
		@Override
		public String toString() {
			return "Position: " + getPosition() + " Driver: " + getDriverNumber() +". " + getDriverName() + " Retired: " + isRetired() + " Grid position: " + getGridPosition();
		}
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
	ResultRow parserRow(NodeList tableColumns) {
		return new ResultRow(tableColumns, number++);
	}

	@Override
	public void parse(Race race, RaceResult result) {
		number = 1;
		super.parse(race, result);
		result.setPodium(getPodiumResult());
		result.setFirstCrash(getFirstCrashResult());
		result.setSelectedDriver(getSelectedDriverResult(race));
	}

}




