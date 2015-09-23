package de.jan_sl.domexplorer;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This Interface is used, when the DOM is parsed to return the TreeNode Object back to the UI
 * @author Administrator
 *
 */
public interface IDomParser {
	/**
	 * Sets the DOM TreeNode Object in the UI
	 * @param page The complete DOM Tree
	 */
	public void setDomTree(DefaultMutableTreeNode page);
}
