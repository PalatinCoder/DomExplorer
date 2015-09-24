/**
 * 
 */
package de.jan_sl.domexplorer;

import java.awt.Color;
import java.util.Enumeration;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class renders an outline of a given DOM Tree
 * @author Administrator
 *
 */
public class RenderingWindow extends JFrame {
	
	JScrollPane scrollPane = new JScrollPane();
	JPanel myContentPane = new JPanel();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7588087496824177354L;

	DefaultMutableTreeNode bodyNode = null;
	
	/**
	 * create the window
	 * @param pageTree the page tree from the dom parser
	 */
	public RenderingWindow() {
		this.myContentPane.setLayout(new BoxLayout(myContentPane, BoxLayout.PAGE_AXIS));
		this.scrollPane.add(this.myContentPane);
		this.getContentPane().add(this.scrollPane);
		this.setLocationByPlatform(true);
		this.setSize(800,600);
		this.setVisible(false);	
	}
	
	public void render(DefaultMutableTreeNode pageTree) {	
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> en = pageTree.preorderEnumeration();
		
		while (en.hasMoreElements()) {
			processNode(en.nextElement());
		}
		
		this.setVisible(true);
	}

	private void processNode(DefaultMutableTreeNode currentNode) {
		// grab the title
		if (currentNode.getLevel() > 0 && currentNode.getParent().toString().equals("title")) this.setTitle(currentNode.toString().trim());
		
		// get body node
		if (currentNode.toString().equals("body")) this.bodyNode = currentNode;
		
		// return if we're not inside the body
		if (bodyNode == null || !bodyNode.isNodeRelated(currentNode)) return;
		
		if (currentNode.toString().matches("header|article|h1|h2|h3|h4|nav|div|p")) {
			JPanel contentPanel = new JPanel();
			contentPanel.setBorder(new LineBorder(Color.BLACK));
			contentPanel.add(new JLabel(currentNode.toString()));
			this.scrollPane.add(contentPanel); // TODO Components werden nicht angezeigt!
		}
	}
}
