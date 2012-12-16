package org.shamit.android.tools.spokenweb.app.speach.tts;

import android.content.Context;

public interface Speaker {
	public void speak(String text);
	public Context getContextForSpeaking();
}
