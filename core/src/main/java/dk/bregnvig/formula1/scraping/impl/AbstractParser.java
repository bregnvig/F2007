package dk.bregnvig.formula1.scraping.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;

abstract class AbstractParser <T> {
	
	private Log log = LogFactory.getLog(AbstractParser.class);
	private String url;
	private List<T> rows;
	private Map<Integer, Driver> drivers = new HashMap<Integer, Driver>();

	abstract List<String> getHeaders();
	
	abstract T parserRow(NodeList tableColumns);
	
	abstract String getHtmlFileName();
	
	public void setURL(String url) {
		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("URL cannot be null");
		}
		if (url.endsWith(getHtmlFileName()) == false) {
			url += getHtmlFileName();
		}
		this.url = url;
	}
	
	NodeFilter getResultTableFilter() {
		return new HasAttributeFilter("class", "raceResults");		
	}
	
	private NodeList getResultTable() {
		
		try {
			Parser parser = new Parser(url);
			NodeList result = parser.parse(getResultTableFilter());
			verifyTable(result);
			return result;
		} catch (ParserException e) {
			String message = "Could not parse url " + url;
			log.warn(message, e);
			throw new IllegalStateException(message, e);
		}
	}
	
	private void verifyTable(NodeList list) {
		Node tableRow = list.elementAt(0).getChildren().extractAllNodesThatMatch(new NodeClassFilter(TableRow.class)).elementAt(0);
		if (tableRow instanceof TableRow == false) {
			String message ="Expected a TableRow. Got a " + tableRow.getClass().getName();
			log.warn(message);
			throw new IllegalStateException(message);
		}
		NodeList tableHeaders = tableRow.getChildren().extractAllNodesThatMatch(new NodeClassFilter(TableHeader.class));
		boolean result = true;
		for (int i = 0; i < getHeaders().size(); i++) {
			result &= tableHeaders.elementAt(i).getFirstChild().getText().equals(getHeaders().get(i));
		}
		if (result == false) {
			String message ="Did not have expected table header. Header size " + tableHeaders.size();
			log.warn(message);
			throw new IllegalStateException(message);
		}
	}
	
	private void createRows(NodeList table) {
		NodeList tableRows = table.elementAt(0).getChildren().extractAllNodesThatMatch(new NodeClassFilter(TableRow.class));
		rows = new ArrayList<T>();
		for (int i = 1; i < tableRows.size(); i++) {
			rows.add(parserRow(tableRows.elementAt(i).getChildren().extractAllNodesThatMatch(new NodeClassFilter(TableColumn.class))));
		}
	}
	
	void parse(Race race, RaceResult result) {
		if (url == null) {
			throw new IllegalStateException("URL has not been set");
		}
		for (Driver driver : race.getDrivers()) {
			drivers.put(driver.getNumber(), driver);
		}
		createRows(getResultTable());
	}
	
	List<T> getRows() {
		return rows;
	}

	Map<Integer, Driver> getDrivers() {
		return drivers;
	}
}
