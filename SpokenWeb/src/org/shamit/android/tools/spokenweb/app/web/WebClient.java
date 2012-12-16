package org.shamit.android.tools.spokenweb.app.web;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebClient extends WebViewClient {
	
	Context ctx;

	public WebClient(Context c){ 
		ctx=c;
	}
	
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
    	return false;
    }

	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
	 */
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		Log.d("TTS","Loaded "+url);
		injectJS(view);
	}
	
	private void injectJS(WebView view){
		String src = getJSSrc();
		src = "javascript:" + src;
		Log.d("TTS","Injecting JS  "+src);
		view.loadUrl(src );
	}
	
	protected String getJSSrc(){
		InputStream is;
        String src= "";
        try {
            is = ctx.getAssets().open("ttsapi.js");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            src = new String(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return src;
	}
}
