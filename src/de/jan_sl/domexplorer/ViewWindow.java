package de.jan_sl.domexplorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * DOM Explorer Window
 * @author Administrator
 *
 */
public class ViewWindow extends JFrame implements java.awt.event.KeyListener, java.awt.event.ActionListener, IDomParser, IWindowStatusBar {
	
	private static final long serialVersionUID = 1L;

	IoOperations ioManager = new IoOperations();
	
	/**
	 * @see de.jan_sl.domexplorer.domparser
	 */
	DomParser domParser = new DomParser(this);
	
	private JPanel topPanel;
	private JTextPane statusBar;
	private JTextField urlBar;
	private JButton buttonLoad;
	private JTree treeView;
	private JScrollPane scrollView;
	
	/**
	 * Create the window
	 */
	public ViewWindow() {
		topPanel = new JPanel();
		
		urlBar = new JTextField(20);
		urlBar.addKeyListener(this);
		topPanel.add(urlBar);
		
		buttonLoad = new JButton("Laden");
		buttonLoad.addActionListener(this);
		topPanel.add(buttonLoad);
		
		statusBar = new JTextPane();
		statusBar.setAlignmentX(LEFT_ALIGNMENT);
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setEditable(false);
		this.getContentPane().add(BorderLayout.SOUTH, statusBar);
		
		this.getContentPane().add(BorderLayout.NORTH, topPanel);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("DOM Explorer");
		this.setSize(500,500);
		this.setVisible(true);
		
		// TODO DEBUG
		//this.urlBar.setText("jan-sl.de");
		//loadPage();
		// END DEBUG
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				loadPage();
				break;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonLoad) loadPage();
	}
	
	/**
	 * Builds the url from the textbox and starts loading the page
	 */
	private void loadPage() {
		String url = urlBar.getText();
		this.statusBar.setText("Request " + url);
		
		try { 
			this.getContentPane().remove(scrollView);
			this.repaint();
		} catch(NullPointerException ex) {}
		
		ioManager.requestIo(domParser, this, url);
	}
	
	/* (non-Javadoc)
	 * @see de.jan_sl.domexplorer.IWindowStatusBar#addStatusBarText(java.lang.String)
	 */
	@Override
	public void addStatusBarText(String msg) {
		this.statusBar.setText(this.statusBar.getText() + " | " + msg);
	}

	/* (non-Javadoc)
	 * @see de.jan_sl.domexplorer.IDomParser#setDomTree(javax.swing.tree.DefaultMutableTreeNode)
	 */
	@Override
	public void setDomTree(DefaultMutableTreeNode root) {
		
		try { this.getContentPane().remove(scrollView); } catch (NullPointerException ex) {	}
		
		this.treeView = new JTree(root);
		this.treeView.setRootVisible(false);
		this.scrollView = new JScrollPane(this.treeView);
		this.getContentPane().add(this.scrollView);
		this.pack();
	}
}
