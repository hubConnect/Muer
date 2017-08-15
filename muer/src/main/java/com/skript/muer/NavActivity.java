package com.skript.muer;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;

public class NavActivity extends Activity {
	GestureDetector GD;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nav);
		
		GD = new GestureDetector(new SwipeGestureDetector());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nav, menu);
		return true;
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
	    private static final int SWIPE_THRESHOLD_VELOCITY = 150;

	    public boolean onDoubleTap(MotionEvent e) 
        {
            finish();
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            return true;
        }

	    public boolean onFling(MotionEvent e1, MotionEvent e2,
	                         float velocityX, float velocityY) {
	      try {
	    	  
	    	  double xDir = e1.getX() - e2.getX();
	    	  double yDir = e1.getY() - e2.getY();
	    	  
	    	  Log.d("Console","xVel = " + xDir);
	    	  Log.d("Console","yVel = " + yDir);
	    	  
	        if ( xDir > SWIPE_THRESHOLD_VELOCITY) {
	        	
	        	if( yDir > SWIPE_THRESHOLD_VELOCITY) {
	        		//Up-Left swipe
	        		GameWindow.writeToOutput("northwest");
	        	} else  if ( yDir < -SWIPE_THRESHOLD_VELOCITY){
	        		//Down-Left swipe
	        		GameWindow.writeToOutput("southwest");
	        	} else {
	        		//Left swipe
	        		GameWindow.writeToOutput("west");
	        	}

	        	return true;
	        } 
	        
	        if (xDir < -SWIPE_THRESHOLD_VELOCITY) {

	        	if( yDir > SWIPE_THRESHOLD_VELOCITY) {
	        		//Up-Right swipe
	        		GameWindow.writeToOutput("northeast");
	        	} else  if ( yDir < -SWIPE_THRESHOLD_VELOCITY){
	        		//Down-Right swipe
	        		GameWindow.writeToOutput("southeast");
	        	} else {
	        		//Right swipe
	        		GameWindow.writeToOutput("east");
	        	}
	        	
	        	return true;
	        	
	        }
	        
	        if ( yDir < -SWIPE_THRESHOLD_VELOCITY) {
	        	//Down swipe
	        	GameWindow.writeToOutput("south");
	        	return true;
	        }
	        

	        if ( yDir > SWIPE_THRESHOLD_VELOCITY) {
	        	//Up swipe
	        	GameWindow.writeToOutput("north");
	        	return true;
	        }
	      } catch (Exception e) {
	        Log.e("YourActivity", "Error on gestures");
	      }
	      
	      return false;
	    }
	  }
}
