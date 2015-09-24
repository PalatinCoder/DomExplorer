/**
 * 
 */
package de.jan_sl.domexplorer;

/**
 * @author Administrator
 *
 */
public class ProgramLoader {

	/**
	 * @param args -showOutline render an outline of the given DOM
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		boolean showOutline = false;
		if (args.length > 0 && args[0].equals("-showOutline")) showOutline = true;
		ProgramController controller =  new ProgramController(showOutline);
	}

}
