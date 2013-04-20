package dk.bregnvig.formula1.client.widget.control;

public interface WizardCompletedListener {
	
	/** 
	 * Is being called by the wizard, when the wizard has completed without any validation errors
	 * @param pages
	 */
	void completed(AbstractWizardPage[] pages);
}
