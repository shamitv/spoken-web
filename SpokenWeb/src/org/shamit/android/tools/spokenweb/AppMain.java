package org.shamit.android.tools.spokenweb;

import java.util.Locale;

import org.shamit.android.tools.spokenweb.app.ContextProvider;
import org.shamit.android.tools.spokenweb.app.StatusUpdateThread;
import org.shamit.android.tools.spokenweb.app.StatusUpdater;
import org.shamit.android.tools.spokenweb.app.js.JavaScriptTTS;
import org.shamit.android.tools.spokenweb.app.speach.tts.Speaker;
import org.shamit.android.tools.spokenweb.app.web.ChromeClient;
import org.shamit.android.tools.spokenweb.app.web.WebClient;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class AppMain extends Activity implements OnClickListener, TextToSpeech.OnInitListener, Speaker, ContextProvider, StatusUpdater{

	private TextToSpeech tts;
    private int MY_DATA_CHECK_CODE = 0;
    protected WebClient webClient = null;
    protected ChromeClient chromeClient = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_main);
        Button speakButton = (Button)findViewById(R.id.speakButton);
        speakButton.setOnClickListener(this);
        Button stopButton = (Button)findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);        
        initWebView();
        ttsInitIntent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_app_main, menu);
		return true;
	}

    @SuppressLint("SetJavaScriptEnabled")
	public void initWebView(){
    	webClient = new WebClient(this);
    	chromeClient = new ChromeClient();
    	WebView myWebView = (WebView) findViewById(R.id.webViewPage);
    	WebSettings webSettings = myWebView.getSettings();
    	webSettings.setJavaScriptEnabled(true);
    	myWebView.setWebViewClient(webClient);
    	myWebView.setWebChromeClient(chromeClient);
    	myWebView.addJavascriptInterface(new JavaScriptTTS(this,this), "OrgShamitWebReaderAPI");
    }

    private void ttsInitIntent(){
        //check for TTS data 
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
            	tts = new TextToSpeech(this, this);
            }
            else {
                    //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
    
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.speakButton){
			loadURLinWebView();
		}else{
			if(v.getId()==R.id.stopButton){
				stopTTS();
			}
		}
		
	}
	
	private void stopTTS() {
		if (tts != null){
			tts.stop();
		}
	}

	private boolean ttsReady=false;
	
	private void speakImpl(String text){
		if(ttsReady){
			if(text!=null){
				//TODO: Split test in smaller chunks
				//4.1 and newer versions do not accept string larger than 4k chars.
				//So, for now just limit text to 4k
				if(text.length()>=4000){
					text=text.substring(0, 4000-2);
				}
				tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			}
		}
	}
	
	

	private String loadURLinWebView(){
		String ret = null;
		EditText inp = (EditText)findViewById(R.id.speakText);
		WebView webView = (WebView) findViewById(R.id.webViewPage);
		webView.loadUrl(inp.getText().toString());
		return ret;
	}
	
	
	private void showMessage(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
	    builder.setTitle("Info");
	    AlertDialog dialog = builder.create();
	    dialog.show();
	}
	
    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    
    
    
    @Override
    public void onInit(int status) {
 
        if (status == TextToSpeech.SUCCESS) {
 
            int result = tts.setLanguage(Locale.US);
 
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }else{
            	ttsReady=true;
            }
 
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
 
    }

	@Override
	public Context getContextForSpeaking() {
		return this;
	}

	@Override
	public void speak(String text) {
		speakImpl(text);
	}

	@Override
	public void setCurrentUrl(String url) {
		EditText inp = (EditText)findViewById(R.id.speakText);
		StatusUpdateThread t = new StatusUpdateThread(url, inp);
		runOnUiThread(t); 
	}

	

}
