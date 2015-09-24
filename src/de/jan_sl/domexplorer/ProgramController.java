package de.jan_sl.domexplorer;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Administrator
 *
 */
public class ProgramController implements java.awt.event.ActionListener, java.awt.event.KeyListener, IDomParser {
	
	DomExplorerWindow domExplorerWindow;
	RenderingWindow renderingWindow;
	IoOperations ioManager;
	DomParser domParser;
	boolean shouldRender;
	
	/**
	 * empty constructor
	 */
	public ProgramController(boolean shouldRender) {
		this.ioManager = new IoOperations();
		this.domExplorerWindow = new DomExplorerWindow(this);
		this.domParser = new DomParser(this, domExplorerWindow);
		this.shouldRender = shouldRender;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Laden")) loadPage();
	}

	@Override
	public void domTreeFinished(DefaultMutableTreeNode page) {
		domExplorerWindow.setDomTree(page);
		if (shouldRender) renderingWindow = new RenderingWindow(page);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() instanceof javax.swing.JTextField) {
			javax.swing.JTextField source = (javax.swing.JTextField) e.getSource();
			if (source.getName().equals("urlBar") && e.getKeyCode() == KeyEvent.VK_ENTER) loadPage();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public void loadPage() {
		String url = domExplorerWindow.getUrl();
		if (url.isEmpty()) {
			domExplorerWindow.setStatusBarText("URL eingeben!");
		} else {
			domExplorerWindow.setStatusBarText("Request " + url);
			ioManager.requestIo(domParser, domExplorerWindow, url);
		}
	}

}
