package de.jan_sl.domexplorer;

/**
 * This Interface is used when the HttpRequest is finished
 * @author Administrator
 */
public interface IPageLoaded {
	/**
	 * @param markup The markup of the requested page
	 */
	public void onPageLoaded(String markup);
}
