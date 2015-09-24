/**
 * 
 */
package de.jan_sl.domexplorer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Renders a given DOM Tree as textual representation, to a given output path
 * @author Administrator
 *
 */
public class TextRenderer {
	File file;;
	IWindowStatusBar statusBarDelegate;
	
	public TextRenderer(String path, IWindowStatusBar statusBarDelegate) {
		this.statusBarDelegate = statusBarDelegate;
		this.file = new File(path);
	}
	
	public void render(DefaultMutableTreeNode pageTree) {
		
		String eol = System.getProperty("line.separator");
		
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> en = pageTree.preorderEnumeration();
		PrintWriter writer = null;
		
		try {
			if (!file.exists()) file.createNewFile();
			
			writer = new PrintWriter(new BufferedWriter( new FileWriter(this.file)));

			while (en.hasMoreElements()) {
				DefaultMutableTreeNode node = en.nextElement();
				String intendation = "";
				for (int i=0; i<node.getLevel(); i++) { intendation += "\t"; }
				
				writer.write(intendation + node.toString().trim() + eol);
			}
		} catch (IOException e) {
			this.statusBarDelegate.addStatusBarText("ERROR: IOException: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
				this.statusBarDelegate.addStatusBarText("File written sucessfully");
			}
		}
	}
}