package de.jan_sl.domexplorer;

/**
 * This Interface is used for backend components to display messages on the UI
 * @author Administrator
 *
 */
public interface IWindowStatusBar {
	/**
	 * Add Text to the status bar
	 * @param msg The Message
	 */
	public void addStatusBarText(String msg);
}
