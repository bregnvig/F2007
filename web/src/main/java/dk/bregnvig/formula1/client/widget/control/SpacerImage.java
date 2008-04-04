package dk.bregnvig.formula1.client.widget.control;

import com.google.gwt.user.client.ui.Image;

public class SpacerImage extends Image {

	public SpacerImage(int width, int height) {
		super();
		setUrl("clear.cache.gif");
		setWidth(Integer.toString(width) + "px");
		setHeight(Integer.toString(height)+"px");
	}
	
	public SpacerImage(int size) {
		setUrl("clear.cache.gif");
		setWidth(Integer.toString(size) + "px");
		setHeight(Integer.toString(size)+"px");
		
	}
}


