/**
 * 
 */
package org.shamit.android.tools.spokenweb.app;

import android.widget.TextView;

/**
 * @author shamit
 *
 */
public class StatusUpdateThread implements Runnable {

	private TextView vwTxt; 
	private String text;  
	
	public StatusUpdateThread(String text,TextView vwTxt) {
		super();
		this.vwTxt = vwTxt;
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		vwTxt.setText(text);
	}

}
