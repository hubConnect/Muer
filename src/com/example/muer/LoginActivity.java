package com.example.muer;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    static ListView lv;
    static Context ct;
    static ServerListAdapter sla;
    static String[] connection;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(com.example.muer.R.layout.activity_login);
        Parse.initialize(this, "NHD2DZbRxC9DCES8IigkVR9pU89wUWmdoWJOd5Pz",
        		"0yWYwvHeLudtLiG8xuEFsWHuA3BnkFq28VunJYdl"); 
        
        ct = this;
        
        connection = new String[4];

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(com.example.muer.R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
        loadData();

    }



	public void loadData() {

		ParseQuery serverQuery = new ParseQuery("GameServerList");
		Log.d("Console","Executing");
	    serverQuery.findInBackground(new FindCallback() {
	    	public void done(List<ParseObject> objectList, ParseException e) {
	    		if(e == null) {	

	    			DataController.clearList();
	    			
		            	Log.d("Console","Added + " );
		            	
		            for(ParseObject next : objectList) {
		            	
		            	Server serverTemp = new Server(Integer.parseInt(next.getString("Port")),next.getString("HostURL"),next.getString("Description"),next.getString("GameName"));
		            	DataController.add(serverTemp);
		            	Log.d("L8", "" + DataController.getItemCount());
		            }

		    		Server tmp =  DataController.get(0);
		    		connection[0] = tmp.getURL();
		    		connection[1] = String.valueOf(tmp.getPort());
		    		((Button) findViewById(com.example.muer.R.id.sendbutton)).
		    			setText("Connect to " + tmp.getName());
	    		} else
	    			Log.d("L8","Server came back with an exception : " + e);
	    		
	    		sla.notifyDataSetChanged();
	    		
	    		lv.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						
						Server tmpServer = DataController.get(arg2);
						connection[0] = tmpServer.getURL();
						connection[1] = String.valueOf(tmpServer.getPort());

			    		((Button) findViewById(com.example.muer.R.id.sendbutton)).
			    			setText("Connect to " + tmpServer.getName());
						Log.d("Console", connection[0] + connection[1]);
					}
	    			
	    		
	    		});
	    		
	    	}
	    });
			
			
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.muer.R.menu.activity_login, menu);
        return true;
    }
    
    
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Connect";
                case 1:
                    return "Options";
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Create a new TextView and set its text to the fragment's section
            // number argument value.
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            textView.setText("Error");
            
            int arg = getArguments().getInt(ARG_SECTION_NUMBER);
            
            if(arg == 1) {
            	RelativeLayout frag =  (RelativeLayout) inflater.inflate(com.example.muer.R.layout.connect_screen,null);

            	Button connectionButton = (Button) frag.findViewById(com.example.muer.R.id.sendbutton);
            	
                connectionButton.setOnClickListener(new View.OnClickListener() {
        			
        			@Override
        			public void onClick(View arg0) {
        				String [] arr = new String[2];
        				

        		    	Intent i = new Intent(ct,GameWindow.class);
        		    	i.putExtra("CONNECTION_INFO", connection);
        		    	
        		    	startActivity(i);
        			}
        		});
                return frag;
            }
            else  if(arg == 2) {
            	
            	RelativeLayout ll = (RelativeLayout) inflater.inflate(com.example.muer.R.layout.options_menu,null);
            	
            	lv = (ListView) ll.findViewById(com.example.muer.R.id.serverList);
            	
            	sla = new ServerListAdapter(ct);
            	
            	lv.setAdapter(sla);
            	
            	return ll;
            }
            return textView;
        }

    }

}
