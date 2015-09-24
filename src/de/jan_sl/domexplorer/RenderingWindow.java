/**
 * 
 */
package de.jan_sl.domexplorer;

import java.util.Enumeration;

import javax.swing.JFrame;
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
	public RenderingWindow() {
		this.setLocationByPlatform(true);
		this.setSize(800,600);
		this.setVisible(false);	
	}
	
	public void render(DefaultMutableTreeNode pageTree) {
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> en = pageTree.preorderEnumeration();
		
		while (en.hasMoreElements()) {
			interpretNode(en.nextElement());
		}
		
		this.setVisible(true);
	}

	private void interpretNode(DefaultMutableTreeNode currentNode) {
		if (currentNode.getLevel() > 0 && currentNode.getParent().toString().equals("title")) this.setTitle(currentNode.toString().trim());
	}
}
