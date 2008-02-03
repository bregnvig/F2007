package dk.bregnvig.formula1.client.widget.bid;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;

public class PositionSelector extends FlowPanel {

	private final ListBox postions;
	
	public PositionSelector(int positions) {
		this.postions = new ListBox();
		this.postions.addItem("Vælg");
		for (int i = 0; i < positions; i++) {
			this.postions.addItem(Integer.toString(i+1));
		}
		add(this.postions);
	}
	
	public int getSelectedPostion() {
		return postions.getSelectedIndex();
	}
	
	void setSelectedPosition(int position) {
		postions.setSelectedIndex(position);
	}
}
