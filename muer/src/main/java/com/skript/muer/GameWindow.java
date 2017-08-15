package com.skript.muer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class GameWindow extends Activity {

	static Context ct;
	GestureDetector GD;
	TextView tv;
	EditText outputContent = null;
	Button outputButton = null;
    final Context context = this;
    Handler mHandler = null;
	static TextView textContent = null;
    static ScrollView scroller = null;
    static netTask gameTask;
    static Socket connectionSocket;
    static int foregroundColor;
    static int backgroundColor;
    static int defaultForegroundColor = TextColor.Intense_White;
    static int defaultBackgroundColor = TextColor.Black;

	int foregroundColorCode = defaultForegroundColor;
	int backgroundColorCode = defaultBackgroundColor;

	private static String FILENAME = "macros.dat";


	private static ArrayList<String> readMacros() {

		try {

			FileInputStream file = new FileInputStream(ct.getFilesDir().getPath().toString() + FILENAME );
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);

			//deserialize the List
			return (ArrayList<String>)input.readObject();
		}
		catch(Exception ex){
			System.out.println("ERR" + ex);
		}
		return null;
	}

	public static void setMacros(ArrayList<String> macros) {

		try {
			FileOutputStream fos = new FileOutputStream (ct.getFilesDir().getPath().toString() + FILENAME );

			OutputStream buffer = new BufferedOutputStream(fos);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject ( macros );
			output.close ();

		} catch ( Exception ex ) {
			ex.printStackTrace ();
		}

	}
	public static void addMacro(String name) {

		ArrayList<String> macros = readMacros();

		if(macros == null)
			macros = new ArrayList<String>();

		macros.add(name);
		setMacros(macros);

	}


	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game_window);
		
		ct = this;
		foregroundColor = defaultForegroundColor;
		backgroundColor = defaultBackgroundColor;
		GD = new GestureDetector( new SwipeGestureDetector());
		tv = (TextView) findViewById(R.id.gameName);
		outputContent = (EditText) findViewById(com.skript.muer.R.id.outputContent);
		outputButton = (Button) findViewById(com.skript.muer.R.id.sendButton);
;
		
		Intent intent = getIntent();
				
		mHandler = new Handler();
		
		((TextView) findViewById(R.id.gameContent)).setMovementMethod(new ScrollingMovementMethod());

		gameTask = new netTask();
		 String[] extras = intent.getStringArrayExtra("CONNECTION_INFO");
		gameTask.execute(extras);

		if((extras[2] != null && !extras[2].equals("")) && (extras[3] != null && !extras[3].equals(""))) {
			outputContent.setText(extras[2] + ";" + extras[3]);
		}

		outputButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					writeToOutput(outputContent.getText().toString());
					outputContent.setText("");
				}	
		});

		outputContent.setOnKeyListener(new OnKeyListener () {

				@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
					
				if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
			{
					final String stringToWrite = outputContent.getText().toString();
					
					Thread t = new Thread(){
					    public void run(){

								
							writeToOutput(stringToWrite);
					    }
					};
					t.start();
				
					writeToInput(Color.YELLOW,stringToWrite + '\n');
					outputContent.setText("");
						
					return true;
				} else if(event.getAction() == KeyEvent.ACTION_DOWN && 
						keyCode == KeyEvent.KEYCODE_BACK) {
					try {
						connectionSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					gameTask.cancel(true);
					finish();
					return true;
				}
				return false;
			}
				
		});

	}
	

	protected static void writeColoredString(StringBuilder sb,int ForegroundColor, int BackgroundColor) {

		final String ts = new String(sb);
		final int fgc = ForegroundColor;
		final int bgc = BackgroundColor;
		((Activity) ct).runOnUiThread(new Runnable(){

			@Override
			public void run() {
				writeToInput(bgc,fgc,new String(ts));
				
			}
			
		});
							
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game_window, menu);
		return true;
	}

	public static void writeToInput(String inputString) {

		writeToInput(backgroundColor,foregroundColor,inputString);
	}

	public static void writeToInput(int ForegroundColorCode,String inputString) {

		writeToInput(backgroundColor,ForegroundColorCode,inputString);
	}

	public static void writeToInput(final int BackgroundColorCode, final int ForegroundColorCode,final String inputString) {

		((Activity) ct).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Spannable spanstring = new SpannableString(inputString);
				spanstring.setSpan(new ForegroundColorSpan(ForegroundColorCode), 0, inputString.length(),
						SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

				spanstring.setSpan(new BackgroundColorSpan(BackgroundColorCode), 0, inputString.length(),
						SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
				textContent.append(spanstring);
				scrollToBottom();
			}
		});
	}
	
	public static void writeToOutput(String outputString) {
		if(!outputString.startsWith("/")) {
			final String[] stringsToPrint = outputString.split(";");

			new Thread(new Runnable() {
				public void run() {

					PrintWriter writeOutput;
					try {
						for (String command : stringsToPrint) {

							writeOutput = new PrintWriter(connectionSocket.getOutputStream(), true);
							writeOutput.println(command);
							writeOutput.flush();
						}
					} catch (IOException e) {

						e.printStackTrace();
					}

				}
			}).start();
		} else {
			outputString.replace("/","");
			customCommand(outputString);
		}
	}

	private static void customCommand(String command) {

		System.out.println(command);
		String newCommand = command.substring(1,command.length());
		String newerCommand = newCommand.split(" ")[0];

		newCommand = newCommand.replaceFirst("\\w+ ","");

		switch (newerCommand) {

			case "addmacro":
				addMacro(newCommand);
				break;
			case "help":
				writeToInput(TextColor.Intense_White,"\n");
				writeToInput(TextColor.Intense_White," Available commands -\n");
				writeToInput(TextColor.Intense_White,"  addmacro \"string\"  - You may use a semi-colon to seperate commands.\n");
				writeToInput(TextColor.Intense_White,"  Swipe right to open the NavStar, double-tap to close it.\n");
				writeToInput(TextColor.Intense_White,"  Swipe left to open the macro menu, back button to close it.\n");
				writeToInput(TextColor.Intense_White,"  Double-tap to open dictation mode, double-tap again to close it.\n");
				writeToInput(TextColor.Intense_White,"\n");
				break;
			case "test":
				System.out.println("Success " + newCommand);
				break;
			default:
				writeToInput(TextColor.Intense_Yellow,"Command not recognized.");
				break;

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
	    private static final int SWIPE_MIN_DISTANCE = 120;
	    private static final int SWIPE_MAX_OFF_PATH = 200;
	    private static final int SWIPE_THRESHOLD_VELOCITY = 200;


	    public boolean onDoubleTap(MotionEvent e) 
        {
	        Intent intent = new Intent(GameWindow.this, MicActivity.class);
	        startActivity(intent);
            overridePendingTransition(R.anim.hyperspace_in,R.anim.hyperspace_out);
            return true;
        }

	    public boolean onFling(MotionEvent e1, MotionEvent e2,
	                         float velocityX, float velocityY) {
	      try {
	    	  
	        float diffAbs = Math.abs(e1.getY() - e2.getY());
	        float diff = e1.getX() - e2.getX();

	        Log.d("Console","Velocity Y - " + velocityY);
	        Log.d("Console","Velocity X - " + velocityX);
	        
	        if (diffAbs > SWIPE_MAX_OFF_PATH)
	          return false;
	        
	        // Left swipe
	        if (diff > SWIPE_MIN_DISTANCE
	        		&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        
	        	scrollToBottom();
		        Intent intent = new Intent(GameWindow.this, FastOptions.class);
		        startActivity(intent);
		        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

	        // Right swipe
	        } else if (-diff > SWIPE_MIN_DISTANCE
	        		&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	
	        	scrollToBottom();
		        Intent intent = new Intent(GameWindow.this, NavActivity.class);
		        startActivity(intent);
		        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
	        }
	      } catch (Exception e) {
	    	  
	        Log.e("YourActivity", "Error on gestures");
	      }
	      return false;
	    }
	  }
		public static void scrollToBottom()
		{
		    scroller.post(new Runnable()
		    { 
		        public void run()
		        { 
		            scroller.smoothScrollTo(0, textContent.getBottom());
		        } 
		    });
		}

		class netTask extends AsyncTask<String,String,String> {
			boolean Intensify = false;

			StringBuilder sb;

			protected String doInBackground(String... arg0) {
				
				textContent = (TextView) findViewById(R.id.gameContent);
				scroller = (ScrollView) findViewById(com.skript.muer.R.id.scrollView1);
				
				boolean isConnected = false;

				sb = new StringBuilder();

		    	byte[] arrayOfByte = new byte[10000];

				outputContent.setFocusableInTouchMode(true);
				outputContent.requestFocus();

				try {			
					
					connectionSocket = new Socket(arg0[0],Integer.parseInt(arg0[1]));
				    InputStream streamInput = connectionSocket.getInputStream();
					isConnected = true; 
					while(isConnected) {
						
						int j = 0;
						
					    try {
				    			j = streamInput.read(arrayOfByte, 0, arrayOfByte.length);
				    			
				    			if (j == -1)
				    			{
				    				
					    			Log.d("Console","j = -1");
				    				throw new Exception("Error while reading socket.");
				    			} else if (j == 0) {
				    				
					    			Log.d("Console","Continuing");
					    			continue;
					    		} else {

									
									final byte[] tmpBytes = arrayOfByte;
									
											int [] ansiSequence = new int[30];
											int tmpIndex = 0;
											StringBuilder sb = new StringBuilder();
											
											for(int i = 0; i < j ; i ++) {
												
												if(tmpBytes[i] <= 1 || tmpBytes[i] == 24 || tmpBytes[i] == 13 || tmpBytes[i] == 31)
													i++;

												if(tmpBytes[i] == '\033') {
													i++;
													if(sb.length() > 0)
													{
														
														final String sbSt = new String(sb);
														final int bgc = backgroundColorCode;
														final int fgc = foregroundColorCode;
														runOnUiThread(new Runnable(){

															@Override
															public void run() {
																writeToInput(bgc,fgc,sbSt);
															}
															
														});
														sb = new StringBuilder();
													}
													if(tmpBytes[i] == '[') {
														i++;
														while(tmpBytes[i] != 'm') {
															ansiSequence[tmpIndex] = (int) tmpBytes[i];
															tmpIndex++;
															i++;
														}
														
														setColorMode(new String(ansiSequence,0,tmpIndex));
														tmpIndex = 0;
													}
												} 
												else 
													sb.append((char)tmpBytes[i]);
											}
											final String sbSt = new String(sb);
											final int bgc = backgroundColorCode;
											final int fgc = foregroundColorCode;
											runOnUiThread(new Runnable(){

												@Override
												public void run() {
													writeToInput(bgc,fgc,sbSt);
												}
												
											});
											sb = new StringBuilder();
										}
										
					    		
						} catch (Exception e) {
							
			    			Handler handlerException = GameWindow.this.mHandler;
			    			String strException = e.getMessage();
	    					final String strMessage = "Error while receiving from server:\r\nConnection terminated";
	    					
			    			Runnable rExceptionThread = new Runnable()
			    			{
			    				@SuppressLint("ShowToast")
								public void run()
			    				{
			    					Toast.makeText(context, strMessage, 3000).show();
			    				}
			    			};

			    			handlerException.post(rExceptionThread);
			    			
			    			if(strException.indexOf("reset") != -1 || strException.indexOf("rejected") != -1)
			    			{
			    				isConnected = false;
								try 
								{
									connectionSocket.close();
								}
								catch (IOException e1) 
								{
									e1.printStackTrace();
								}
								
			    			}
			    			
							isConnected = false;
						}
						
					}		
					
			        finish();
			        
				} catch (IOException e) {
					  
					Log.d("[GET REQUEST]", "Network exception", e);
				}

				
				return sb.toString();
			}
			

			protected void setColorMode(String string) {

				StringTokenizer strTok = new StringTokenizer(string,";");
				Log.d("ANSI",string);
				while(strTok.hasMoreTokens()) {
					int token = Integer.parseInt(strTok.nextToken());
					switch(token) {
					
					case 1:
						Intensify = true;
						break;
						
					case 34:
						if(Intensify)
							foregroundColorCode = TextColor.Intense_Blue;
						else
							foregroundColorCode = TextColor.Blue;
						break;
					
					case 37:
						if(Intensify)
							foregroundColorCode = TextColor.Intense_White;
						else
							foregroundColorCode = TextColor.White;
						break;
						
					case 33:
						Log.d("ANSI","Case was 33");
						if(Intensify)
							foregroundColorCode = TextColor.Intense_Yellow;
						else
							foregroundColorCode = TextColor.Yellow;
						break;
						
					case 35:
						if(Intensify)
							foregroundColorCode = TextColor.Intense_Magenta;
						else
							foregroundColorCode = TextColor.Magenta;
						break;
						
					case 36:
						Log.d("ANSI","Case was 36");
						if(Intensify)
							foregroundColorCode = TextColor.Intense_Cyan;
						else
							foregroundColorCode = TextColor.Cyan;
						break;
						
					case 32:
						if(Intensify)
							foregroundColorCode = TextColor.Intense_Green;
						else
							foregroundColorCode = TextColor.Green;
						break;
					
					case 38:
						Log.d("Color","CUSTOM");
						break;
						
					case 31:
						if(Intensify)
							foregroundColorCode = TextColor.Intense_Red;
						else
							foregroundColorCode = TextColor.Red;
						break;

					case 10:
						foregroundColorCode = defaultForegroundColor;		
						break; 
					case 0:
						Intensify = false;
						foregroundColorCode = defaultForegroundColor;
					case 39:
						foregroundColorCode = defaultForegroundColor;
						break;
						
					case 40:
						backgroundColorCode = TextColor.Black;
						break;
					
					case 41:
						backgroundColorCode = TextColor.White;
						break;
					
					case 42:
						backgroundColorCode = TextColor.Green;
						break;
						
					case 43:
						backgroundColorCode = TextColor.Yellow;
						break;
						
					case 44:
						backgroundColorCode = TextColor.Blue;
						break;
						
					case 45:
						backgroundColorCode = TextColor.Magenta;
						break;
						
					case 46:
						backgroundColorCode = TextColor.Cyan;
						break;
						
					case 47:
						backgroundColorCode = TextColor.White;
						break;
						
					case 49:
						backgroundColorCode = defaultBackgroundColor;
						break;
					case 90:
						foregroundColorCode = TextColor.Black;
						break;
						
					case 91:
						foregroundColorCode = TextColor.Intense_Red;
						break;
						
					case 92:
						foregroundColorCode = TextColor.Intense_Green;
						break;
						
					case 93:
						foregroundColorCode = TextColor.Intense_Yellow;
						break;
						
					case 94:
						foregroundColorCode = TextColor.Intense_Blue;
						break;
						
					case 95:
						foregroundColorCode = TextColor.Intense_Magenta;
						break;
						
					case 96:
						foregroundColorCode = TextColor.Intense_Cyan;
						break;
						
					case 97:
						foregroundColorCode = TextColor.Intense_White;
						break;


					case 100:
						backgroundColorCode = TextColor.Black;
						break;
					case 101:
						backgroundColorCode = TextColor.Intense_Red;
						break;
						
					case 102:
						backgroundColorCode = TextColor.Intense_Green;
						break;
						
					case 103:
						backgroundColorCode = TextColor.Intense_Yellow;
						break;
						
					case 104:
						backgroundColorCode = TextColor.Intense_Blue;
						break;
						
					case 105:
						backgroundColorCode = TextColor.Intense_Magenta;
						break;
						
					case 106:
						backgroundColorCode = TextColor.Intense_Cyan;
						break;
						
					case 107:
						backgroundColorCode = TextColor.Intense_White;
						break;
						
					default:
						foregroundColorCode = defaultForegroundColor;
						backgroundColorCode = defaultBackgroundColor;
						break;
					}
				}				
				
			}
					
		}

}
