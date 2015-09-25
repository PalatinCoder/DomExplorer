/**
 * 
 */
package de.jan_sl.domexplorer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Renders a given DOM Tree as textual representation, to a given output path
 * @author Administrator
 *
 */
public class TextRenderer {
	/**
	 * File Object representing the output file
	 */
	File file;
	IWindowStatusBar statusBarDelegate;
	
	/**
	 * Constructor
	 * @param path The path of the file in which the structure shall be written
	 * @param statusBarDelegate The statusBarDelegate
	 * @see de.jan_sl.domexplorer.IWindowStatusBar
	 */
	public TextRenderer(String path, IWindowStatusBar statusBarDelegate) {
		this.statusBarDelegate = statusBarDelegate;
		this.file = new File(path);
	}
	
	/**
	 * render the page tree as textual representation, and write to the given file
	 * @param pageTree The DOM Tree
	 */
	public void render(DefaultMutableTreeNode pageTree) {
		
		String eol = System.getProperty("line.separator");
		
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> en = pageTree.preorderEnumeration();
		
		try {
			if (!file.exists()) file.createNewFile();
			
			FileWriter writer = new FileWriter(this.file);

			while (en.hasMoreElements()) {
				DefaultMutableTreeNode node = en.nextElement();
				String intendation = "";
				for (int i=0; i<node.getLevel(); i++) { intendation += "\t"; }
				
				writer.write(intendation + node.toString().trim() + eol);
			}
			
			writer.close();
		} catch (IOException e) {
			this.statusBarDelegate.addStatusBarText("ERROR: IOException: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
