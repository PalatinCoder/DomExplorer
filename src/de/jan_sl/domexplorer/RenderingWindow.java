/**
 * 
 */
package de.jan_sl.domexplorer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class renders an outline of a given DOM Tree
 * @author Administrator
 *
 */
public class RenderingWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7588087496824177354L;

	/**
	 * create the window
	 * @param pageTree the page tree from the dom parser
	 */
	public RenderingWindow(DefaultMutableTreeNode pageTree) {
		this.getContentPane().add(new JLabel("Hallo welt"));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationByPlatform(true);
		this.pack();
		this.setVisible(true);
	}
}
