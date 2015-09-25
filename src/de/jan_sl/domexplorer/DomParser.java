package de.jan_sl.domexplorer;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Administrator
 *
 */
public class DomParser implements IPageLoaded {
	/**
	 * @see de.jan_sl.domexplorer.IDomParser
	 */
	private IDomParser domListener;
	/**
	 * @see de.jan_sl.domexplorer.IWindowStatusBar
	 */
	private IWindowStatusBar statusBarDelegate;


	/**
	 * Constructor
	 * @param domListener The listener for the parsed DOM
	 * @param statusBarDelegate Delegate to send status bar messages to
	 * 
	 * @see de.jan_sl.domexplorer.IWindowStatusBar
	 * @see de.jan_sl.domexplorer.IDomParser
	 */
	public DomParser(IDomParser domListener, IWindowStatusBar statusBarDelegate) {
		this.domListener = domListener;
		this.statusBarDelegate = statusBarDelegate;
	}


	/**
	 * This Method starts parsing the markup. When finished, the complete TreeNode is returned via the IDomParser interface.
	 * @see de.jan_sl.domexplorer.IPageLoaded#onPageLoaded(java.lang.String)
	 * @see de.jan_sl.domexplorer.IDomParser
	 */
	@Override
	public void onPageLoaded(String markup) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		
		nextDomNode(new StringBuilder(markup), root);
		
		domListener.domTreeFinished(root);
	}
	
	/**
	 * Overloading the actual method with less parameters.
	 * @param markup The complete markup
	 * @param parent the parent tree node
	 * 
	 * @see de.jan_sl.domexplorer.DomParser#nextDomNode(StringBuilder, DefaultMutableTreeNode, int, String)
	 */
	private void nextDomNode(StringBuilder markup, DefaultMutableTreeNode parent) {
		this.nextDomNode(markup, parent, 0, new String());
	}

	/**
	 * Parses the markup tag by tag, child nodes via recursion
	 * @param markup The complete markup of the page
	 * @param parent The parent TreeNode in the DOM Tree
	 * @param recursionDepth the current recursion depth
	 * @param currentOpenTag the tag which is currently open, telling which closing tag we're looking for
	 */
	private void nextDomNode(StringBuilder markup, DefaultMutableTreeNode parent, int recursionDepth, String currentOpenTag) {
		
		int start;
		int end;
		
		// get the next tag
		start = markup.indexOf("<") +1;
		end = markup.indexOf(">") +1;
		String sCurrentTag = "";
		
		boolean isInsideTag = false;
		if (start > end) isInsideTag = true;
		
		sCurrentTag = markup.substring(isInsideTag ? 0 : start, end-1);
		String currentTagName = "";
		HashMap<String, String> currentTagAttributes = new HashMap<String, String>();
		
		boolean currentIsComment = sCurrentTag.startsWith("!");
		boolean currentIsClosingTag = sCurrentTag.startsWith("/");
		
	// // parse tag and attributes
		
		// parse text between tags
		
		if (!isInsideTag && !currentIsComment) {

			int nextTag = markup.indexOf("<");
			String textNode = new String();

			if (nextTag > 0) textNode = markup.substring(0, nextTag);

			if (!textNode.isEmpty() && !textNode.matches("\\s+")) {				
				parent.add(new DefaultMutableTreeNode(textNode));
			}

			// remove parsed text
			markup = markup.delete(0, nextTag);

			// - deleted - if (!currentIsClosingTag) end = -1;
		}

		// handle opening tags
		if (!currentIsComment && !currentIsClosingTag) {
			
			if (!sCurrentTag.contains(" ")) {  // no attributes
				currentTagName = sCurrentTag;
			
			} else {	// has attributes
				
				int i = sCurrentTag.indexOf(" ");
				currentTagName = sCurrentTag.substring(0, i);
				String sCurrentAttributes = sCurrentTag.substring(i).replaceAll("^ |\"$", "");
				
				// parse attributes here
				String[] pairs = sCurrentAttributes.split("\" ");
				for (String attribute : pairs) {
					String[] x = attribute.split("=\"");
					if (x.length > 1) currentTagAttributes.put(x[0], x[1]);
				}
			}
			
			// create node
			
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(currentTagName);
			if (currentTagAttributes.size() > 0) {
				Iterator<String> itr = currentTagAttributes.keySet().iterator();
				while (itr.hasNext()) {
					String current = itr.next();
					node.add(new DefaultMutableTreeNode("["+current + " -> " + currentTagAttributes.get(current) + "]"));
				}
			}
			
			parent.add(node);
			
			// remove parsed markup
			markup = markup.delete(0, markup.indexOf(">")+1);
		
			// parse children and add them to current node
			// if tag contains "/>", it is self closing. meta & link must not have children
			if (!(currentTagName.matches("meta|br|link|img") || sCurrentTag.contains("/>"))) {
				nextDomNode(markup, node, recursionDepth+1, currentTagName);
			}
			
		}
		
		// parse text between tags
		
		// (1) inside a tag are attributes, no text 
		// (2) comments are ignored 
		// (3) if a closing tag is forgotten, it's possible that we reach the end of the markup during child node processing (at line 138)
		
		if (!isInsideTag && !currentIsComment && markup.length() > 1) {
		
			int nextTag = markup.indexOf("<");
			String textNode = new String();
			
			if (nextTag > 0) textNode = markup.substring(0, nextTag);
			if (!textNode.isEmpty() && !textNode.matches("\\s+")) {				
				parent.add(new DefaultMutableTreeNode(textNode));
			}
			
			// remove parsed text
			markup = markup.delete(0, nextTag);
			if (!currentIsClosingTag) end = -1;
		
		}
		
		// handle closing tags
		if (currentIsClosingTag && markup.length() > 1) {
			boolean dontDelete = false;
			// if the closing tag is missing, don't delete it
			if (!sCurrentTag.equals("/" + currentOpenTag)) {
				dontDelete = true;
				statusBarDelegate.addStatusBarText("ERROR: " + currentOpenTag + " was not closed");
			}
			// remove parsed end tag
			if (!dontDelete) markup = markup.delete(0, markup.indexOf(">")+1);
			if(recursionDepth > 0) return;
		}
		
		// call again until markup is completely parsed
		if (markup.length() > 1 && markup.indexOf("<") >= 0) {
			nextDomNode(markup.delete(0, end+1), parent, recursionDepth, currentOpenTag);
		} else {
			if (recursionDepth > 0) statusBarDelegate.addStatusBarText("ERROR: Markup contains errors");
		}
	}
}
