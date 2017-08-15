package com.skript.muer;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Button;
import android.widget.TextView;

public class MicActivity extends Activity implements OnKeyListener {

    GestureDetector GD;
    private TextView mText;
    private SpeechRecognizer sr;
    private static final String TAG = "MyStt3Activity";
    Button speakButton;
    View OKL;
    
	   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
	   super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_mic);    
       GD = new GestureDetector(new SwipeGestureDetector());
       speakButton = (Button) findViewById(R.id.micButton);
       
       OKL = new View(this);
       
       Context ct = this;
       
       OKL.setOnKeyListener(this);
       
       speakButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
	                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);        
	                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");

	                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5); 
                    sr.startListening(intent);
                    Log.i("111111","11111111");
	            }

		});
        sr = SpeechRecognizer.createSpeechRecognizer(this);       
        sr.setRecognitionListener(new listener());        
   }

	   class listener implements RecognitionListener          
	   {
	            public void onReadyForSpeech(Bundle params)
	            {
	                     Log.d(TAG, "onReadyForSpeech");
	            }
	            public void onBeginningOfSpeech()
	            {
	                     Log.d(TAG, "onBeginningOfSpeech");
	            }
	            public void onRmsChanged(float rmsdB)
	            {
	                     Log.d(TAG, "onRmsChanged");
	            }
	            public void onBufferReceived(byte[] buffer)
	            {
	                     Log.d(TAG, "onBufferReceived");
	            }
	            public void onEndOfSpeech()
	            {
	                     Log.d(TAG, "onEndofSpeech");
	            }
	            public void onError(int error)
	            {
	                     Log.d(TAG,  "error " +  error);
	            }
	            public void onResults(Bundle results)                   
	            {
	                     String str = new String();
	                     Log.d(TAG, "onResults " + results);
	                     ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
	                     for (int i = 0; i < data.size(); i++)
	                     {
	                               Log.d(TAG, "result " + data.get(i));
	                               str += data.get(i);
	                     }
	                     GameWindow.writeToOutput(data.get(0));
	            }
	            public void onPartialResults(Bundle partialResults)
	            {
	                     Log.d(TAG, "onPartialResults");
	            }
	            public void onEvent(int eventType, Bundle params)
	            {
	                     Log.d(TAG, "onEvent " + eventType);
	            }
	   }

	public boolean dispatchTouchEvent(MotionEvent ev){
	    super.dispatchTouchEvent(ev);    
	    return GD.onTouchEvent(ev); 
	}

	 public boolean onTouchEvent(MotionEvent event) {
		    if (GD.onTouchEvent(event)) {
		      return true;
		    }
		    return super.onTouchEvent(event);
		  }
	 
	  private class SwipeGestureDetector 
	          extends SimpleOnGestureListener {
	    // Swipe properties, you can change it to make the swipe 
	    // longer or shorter and speed

	    public boolean onDoubleTap(MotionEvent e) 
       {
           finish();
           overridePendingTransition(R.anim.hyperspace_in,R.anim.hyperspace_out);
           return true;
       }
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mic, menu);
		return true;
	}


	public boolean onKey(View v, int keyCode, KeyEvent event) {
			 if(event.getAction() == KeyEvent.ACTION_DOWN && 
				keyCode == KeyEvent.KEYCODE_BACK) {
	

           finish();
           overridePendingTransition(R.anim.hyperspace_in,R.anim.hyperspace_out);
			return true;
		}
		return false;
	}
}
