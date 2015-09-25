/**
 * 
 */
package de.jan_sl.domexplorer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class renders an outline of a given DOM Tree
 * @author Administrator
 *
 */
public class RenderingWindow extends JFrame {
	
	JScrollPane scrollView = new JScrollPane();
	
	private static final long serialVersionUID = -7588087496824177354L;
	
	/**
	 * create the window
	 * @param pageTree the page tree from the dom parser
	 */
	public RenderingWindow() {
		this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		//this.scrollPane.add(this.myContentPane);
		//this.getContentPane().add(this.scrollPane);
		this.setLocationByPlatform(true);
		this.setSize(800,600);
		this.setVisible(false);	
	}
	
	/**
	 * render the page
	 * @param rootNode the root node of the dom tree
	 */
	public void render(DefaultMutableTreeNode rootNode) {
		
		DefaultMutableTreeNode head = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0);
		DefaultMutableTreeNode body = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(1);
		
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> en = head.preorderEnumeration();
		while (en.hasMoreElements()) {
			DefaultMutableTreeNode currentNode = en.nextElement();
			if (currentNode.toString().equals("title")) this.setTitle(currentNode.getChildAt(0).toString().trim());
		}
		
		processNode(body, this.scrollView.getViewport());
		
		this.getContentPane().add(this.scrollView);
		this.setVisible(true);
	}

	/**
	 * process a single node. this method is called recursively to process the given node's child nodes
	 * @param treeNode the node to process
	 * @param parent the parent GUI component in which the given node shall be displayed
	 */
	private void processNode(DefaultMutableTreeNode treeNode, Container parent) {		
		
		// create the contentPanel for the element
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		
		// determine if the element text should be rendered, of if it's a tag
		// \\[.*? means that the string starts with an [, so attributes are filtered
		if (!treeNode.toString().trim().matches("body|header|div|nav|article|p|a|h1|h2|h3|h4|\\[.*?")) {
			Component contentLabel = null;
			
			// parse text nodes
			switch (treeNode.getParent().toString()) {
			case "a":
				contentLabel = new JLabel(treeNode.toString().trim());
				contentLabel.setForeground(Color.BLUE);
				break;
			case "h1":
			case "h2":
			case "h3":
			case "h4":
				int size = 24 - 3*(Integer.parseInt(treeNode.getParent().toString().substring(1))-1);
				contentLabel = new JLabel(treeNode.toString().trim());
				contentLabel.setFont(new Font("SansSerif", Font.BOLD, size));
				break;
			case "p":
				contentLabel = new JTextArea(treeNode.toString().trim());
				((JTextArea) contentLabel).setEditable(false);
				contentLabel.setFont(new Font("Serif", Font.PLAIN, 16));
			default:
				contentLabel = new JLabel(treeNode.toString().trim());
				contentLabel.setFont(new Font("Serif", Font.PLAIN, 16));
				break;
			}
			
			if (contentLabel != null) contentPanel.add(contentLabel);
		}
		
		// parse all children 
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			processNode((DefaultMutableTreeNode) treeNode.getChildAt(i), contentPanel);
		}
		
		parent.add(contentPanel);
	}
}
