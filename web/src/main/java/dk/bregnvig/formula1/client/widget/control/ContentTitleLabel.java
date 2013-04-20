package dk.bregnvig.formula1.client.widget.control;


public class ContentTitleLabel extends BigLabel {

	public ContentTitleLabel() {
		initialize();
	}

	public ContentTitleLabel(String text) {
		super(text);
		initialize();
	}

	public ContentTitleLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		initialize();
	}

	private void initialize() {
		addStyleDependentName("content-title");
	}
}
