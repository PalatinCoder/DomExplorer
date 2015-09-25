package de.jan_sl.domexplorer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class handles all Input/Output Operations
 * @author Administrator
 *
 */
public class IoOperations {

	/**
	 * Empty constructor
	 */
	public IoOperations() {
	}
	
	/**
	 * Request a IO Operation. This method decides by the url whether it loads a local file or a network resource. 
	 * @param pageLoadedListener The IPageLoaded delegate to pass the resulting markup to.
	 * @param statusBarDelegate The IWindowStatusBar delegate to display messages on the UI
	 * @param _url The url as string. Prefix "file:///" will load a local file. If no prefix is given, default to http://
	 * 
	 * @see de.jan_sl.domexplorer.IPageLoaded
	 * @see de.jan_sl.domexplorer.IWindowStatusBar
	 */
	public void requestIo(IPageLoaded pageLoadedListener, IWindowStatusBar statusBarDelegate, String _url) {
		if (_url.substring(0, 8).equals("file:///")) requestFile(pageLoadedListener, statusBarDelegate, _url.substring(8));
		if (!_url.substring(0, 4).equals("http") && !_url.substring(0, 4).equals("file")) {
			_url = "http://" + _url;
			requestHttpGet(pageLoadedListener, statusBarDelegate, _url);
		}
	}
	
	/**
	 * Request a page via HTTP GET. The result is passed back via IPageLoaded and IWindowStatusBar is used to display progress.
	 * @param pageLoadedListener The IPageLoaded delegate to pass the resulting markup to.
	 * @param statusBarDelegate The IWindowStatusBar delegate to display messages on the UI
	 * @param _url The url as string
	 * 
	 * @see de.jan_sl.domexplorer.IPageLoaded
	 * @see de.jan_sl.domexplorer.IWindowStatusBar
	 */
	private void requestHttpGet(IPageLoaded pageLoadedListener, IWindowStatusBar statusBarDelegate, String _url) {
		
		Thread request = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					
					URL url = new URL(_url);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuilder builder = new StringBuilder();
					
					String line = "";
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					
					statusBarDelegate.addStatusBarText(conn.getResponseCode() + " " + conn.getResponseMessage());
					
					pageLoadedListener.onPageLoaded(builder.toString());
					
				} catch (IOException e) {
					e.printStackTrace();
					statusBarDelegate.addStatusBarText("ERROR: IOException " + e.getLocalizedMessage());
				}
			}
		});
		
		request.start();

	}
	
	/**
	 * Request a file from the local filesystem . The result is passed back via IPageLoaded and IWindowStatusBar is used to display progress.
	 * @param pageLoadedListener The IPageLoaded delegate to pass the resulting markup to.
	 * @param statusBarDelegate The IWindowStatusBar delegate to display messages on the UI
	 * @param _url The url as string
	 * 
	 * @see de.jan_sl.domexplorer.IPageLoaded
	 * @see de.jan_sl.domexplorer.IWindowStatusBar
	 */
	private void requestFile(IPageLoaded pageLoadedListener, IWindowStatusBar statusBarDelegate, String _url) {
		try {
			List<String> file = Files.readAllLines(Paths.get(_url));
			StringBuilder builder = new StringBuilder();
			for (String line : file) {
				builder.append(line);
			}

			pageLoadedListener.onPageLoaded(builder.toString());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			statusBarDelegate.addStatusBarText("ERROR: File not found " + e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			statusBarDelegate.addStatusBarText("ERROR: IOException " + e.getLocalizedMessage());
		}
	}

}
