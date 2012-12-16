package org.shamit.android.tools.spokenweb.app.js;

import org.shamit.android.tools.spokenweb.app.StatusUpdater;
import org.shamit.android.tools.spokenweb.app.speach.tts.Speaker;

import android.content.Context;
import android.widget.Toast;

public class JavaScriptTTS {
	Speaker spk;
	StatusUpdater status;
    /** Instantiate the interface and set the context */
	public JavaScriptTTS(Speaker speaker, StatusUpdater status) {
        spk = speaker;
        this.status = status;
    } 

    /** Speak text from the web page */
    public void speakText(String msg) {
    	spk.speak(msg);
    }

    /** Can be used by web page to update URL*/
    public void setCurrentUrl(String url) {
    	status.setCurrentUrl(url);
    }    
    
    
}
