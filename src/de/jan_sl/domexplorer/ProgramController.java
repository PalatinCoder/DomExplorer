package de.jan_sl.domexplorer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class controls the program.
 * It dispatches every action and is listener for all GUI events
 * @author Administrator
 *
 */
public class ProgramController implements java.awt.event.ActionListener, java.awt.event.KeyListener, IDomParser {
	
	DomExplorerWindow domExplorerWindow;
	RenderingWindow renderingWindow;
	TextRenderer textRenderer;
	IoOperations ioManager;
	DomParser domParser;
	
	DefaultMutableTreeNode page = null;
	
	/**
	 * Constructor, actual Program entry point
	 * It initializes all components (view, ioManager, parser) and reads the last visited url
	 */
	public ProgramController() {
		String lastPage = null;
		try {
			lastPage = new String(Files.readAllBytes(Paths.get("./last-visited")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.ioManager = new IoOperations();
		this.domExplorerWindow = new DomExplorerWindow(this);
		this.domParser = new DomParser(this, domExplorerWindow);
		
		if (lastPage != null) this.domExplorerWindow.setUrl(lastPage);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (((Component) e.getSource()).getName()) {
		case "buttonLoad":
			loadPage();			
			break;
		case "buttonSave":
			if (page == null) break;
			
			JFileChooser fileChooser = new JFileChooser();
			int res = fileChooser.showSaveDialog(domExplorerWindow);
			if (res == JFileChooser.APPROVE_OPTION) {
				this.textRenderer = new TextRenderer(fileChooser.getSelectedFile().getAbsolutePath(), domExplorerWindow);
				this.textRenderer.render(this.page);
			}
			break;
		case "buttonDraw":
			if (page == null) break;
			
			this.renderingWindow = new RenderingWindow();
			this.renderingWindow.render(this.page);
			break;
		default:
			break;
		}
	}

	@Override
	public void domTreeFinished(DefaultMutableTreeNode page) {
		domExplorerWindow.setDomTree(page);
		this.page = page;
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
	
	/**
	 * start loading the page.
	 * The method request an IO Operation via ioManager and saves the last visited url to a file
	 */
	public void loadPage() {
		String url = domExplorerWindow.getUrl();
		if (url.isEmpty()) {
			domExplorerWindow.setStatusBarText("URL eingeben!");
		} else {
			domExplorerWindow.setStatusBarText("Request " + url);
			ioManager.requestIo(domParser, domExplorerWindow, url);
		}
		
		// save last opened page
		File lastPage = new File("./last-visited");
		try {
			if (!lastPage.exists()) lastPage.createNewFile();
			FileWriter writer = new FileWriter(lastPage);
			writer.write(url);
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
