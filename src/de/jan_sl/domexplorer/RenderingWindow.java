/**
 * 
 */
package de.jan_sl.domexplorer;

import java.awt.Color;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * This class renders an outline of a given DOM Tree
 * @author Administrator
 *
 */
public class RenderingWindow extends JFrame {
	
	JScrollPane scrollView = new JScrollPane();
	
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
		this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		//this.scrollPane.add(this.myContentPane);
		//this.getContentPane().add(this.scrollPane);
		this.setLocationByPlatform(true);
		this.setSize(800,600);
		this.setVisible(false);	
	}
	
	public void render(DefaultMutableTreeNode rootNode) {
		
		System.out.println(rootNode.getChildCount());
		
		processNode(rootNode.getChildAt(0), this.scrollView.getViewport());
		
		this.getContentPane().add(this.scrollView);
		this.scrollView.setSize(this.scrollView.getParent().getSize());
		this.setVisible(true);
	}

	private void processNode(TreeNode treeNode, Container parent) {
		
		String node = treeNode.toString().trim();
		if (node.length() > 10) {
			node = node.substring(0, 10);
		}
		
		if (treeNode.getChildCount() == 0) {
			parent.add(new JLabel(node));
			return;
		}
		
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new LineBorder(Color.BLACK));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		contentPanel.setSize(parent.getSize());
		JLabel contentLabel = new JLabel(node);
		contentPanel.add(contentLabel);
		
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			processNode(treeNode.getChildAt(i), contentPanel);
		}
		
		parent.add(contentPanel);
	}
}
