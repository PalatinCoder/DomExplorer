package de.jan_sl.domexplorer;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

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
public class DomExplorerWindow extends JFrame implements IWindowStatusBar {
	
	private static final long serialVersionUID = 1L;

	IoOperations ioManager = new IoOperations();
	
	private JPanel topPanel;
	private JTextPane statusBar;
	private JTextField urlBar;
	private JButton buttonLoad, buttonSave, buttonDraw;
	private JTree treeView;
	private JScrollPane scrollView;
	
	/**
	 * Create the domExplorerWindow
	 */
	public DomExplorerWindow(Object actionListener) {
		topPanel = new JPanel();
		
		urlBar = new JTextField(20);
		urlBar.addKeyListener((KeyListener) actionListener);
		urlBar.setName("urlBar");
		topPanel.add(urlBar);
		
		buttonLoad = new JButton("Seite laden");
		buttonLoad.addActionListener((ActionListener) actionListener);
		buttonLoad.setName("buttonLoad");
		topPanel.add(buttonLoad);
		
		buttonSave = new JButton("Struktur speichern");
		buttonSave.addActionListener((ActionListener) actionListener);
		buttonSave.setName("buttonSave");
		topPanel.add(buttonSave);
		
		buttonDraw = new JButton("Struktur zeichnen");
		buttonDraw.addActionListener((ActionListener) actionListener);
		buttonDraw.setName("buttonDraw");
		topPanel.add(buttonDraw);
		
		statusBar = new JTextPane();
		statusBar.setAlignmentX(LEFT_ALIGNMENT);
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setEditable(false);
		this.getContentPane().add(BorderLayout.SOUTH, statusBar);
		
		this.getContentPane().add(BorderLayout.NORTH, topPanel);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("DOM Explorer");
		this.pack();
		this.setVisible(true);
	}
	

	/**
	 * @see de.jan_sl.domexplorer.IWindowStatusBar#addStatusBarText(java.lang.String)
	 */
	@Override
	public void addStatusBarText(String msg) {
		this.statusBar.setText(this.statusBar.getText() + " | " + msg);
	}
	
	/**
	 * Set the status bar text
	 * @param msg The message to show
	 */
	public void setStatusBarText(String msg) {
		this.statusBar.setText(msg);
	}

	/**
	 * Set the parsed DOM Tree
	 * @see de.jan_sl.domexplorer.IDomParser#setDomTree(javax.swing.tree.DefaultMutableTreeNode)
	 */
	protected void setDomTree(DefaultMutableTreeNode root) {
		
		try { this.getContentPane().remove(scrollView); } catch (NullPointerException ex) {	}
		
		this.treeView = new JTree(root);
		this.scrollView = new JScrollPane(this.treeView);
		this.getContentPane().add(this.scrollView);
		this.pack();
	}

	/**
	 * the url from the text field
	 * @return String the url
	 */
	protected String getUrl() {
		return this.urlBar.getText();
	}
	
	/**
	 * Set the url in the text field
	 * @param url String the url
	 */
	protected void setUrl(String url) {
		this.urlBar.setText(url);
	}
}
