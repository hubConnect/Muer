package com.skript.muer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


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
    static RecyclerView lv;
    static Context ct;
    static ServerRecyclerAdapter sla;
    static String[] connection;
    static String FILENAME = "servers.dat";

    public static EditText nametext;
    public static EditText porttext;
    public static EditText addytext;


    private File dir;

    public static void readServers() {

        DataController.clearList();

        try {

            FileInputStream file = new FileInputStream(ct.getFilesDir().getPath().toString() + FILENAME );
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the List
            ArrayList<Server> recoveredQuarks = (ArrayList<Server>)input.readObject();
            //display its data

            for(Server quark: recoveredQuarks){
                DataController.add(quark);
            }
        }
        catch(Exception ex){
            System.out.println("ERR" + ex);

            ArrayList<Server> recoveredQuarks = new ArrayList<Server>();
            //display its data
            if(recoveredQuarks.isEmpty()) {
                recoveredQuarks.add(new Server(7680,"eclipse.cs.pdx.edu","","New Moon"));
                recoveredQuarks.add(new Server(9000,"abandonedrealms.com","","Abandoned Realms"));
                recoveredQuarks.add(new Server(5656,"legendsofthejedi.com","","Legends of The Jedi"));
            }
            for(Server quark: recoveredQuarks){
                DataController.add(quark);
            }
        }
    }

    private void setup () {
        File sdCard = Environment.getExternalStorageDirectory();
        dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");
        dir.mkdirs();


    }

    public static void writeServers() {

        for ( Server tmp:DataController.List() ) {

            System.out.println(tmp);
        }
        try {
            FileOutputStream fos = new FileOutputStream (ct.getFilesDir().getPath().toString() + FILENAME );

            OutputStream buffer = new BufferedOutputStream(fos);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject ( DataController.List() );
            output.close ();

        } catch ( Exception ex ) {
            ex.printStackTrace ();
            System.out.println("FUCK" + ex);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(com.skript.muer.R.layout.activity_login);
        Parse.initialize(this, "NHD2DZbRxC9DCES8IigkVR9pU89wUWmdoWJOd5Pz",
                "0yWYwvHeLudtLiG8xuEFsWHuA3BnkFq28VunJYdl");

        ct = this;

        connection = new String[4];

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(com.skript.muer.R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        setup();
        testFunc();
    }

    private void testFunc() {

        connection[0] = "eclipse.cs.pdx.edu";
        connection[1] = "7680";

        readServers();

    }


	public void loadData() {

		ParseQuery serverQuery = new ParseQuery("GameServerList");

	    serverQuery.findInBackground(new FindCallback() {
	    	public void done(List<ParseObject> objectList, ParseException e) {
	    		if(e == null) {

	    			DataController.clearList();

		    		Server tmp =  DataController.get(0);
		    		connection[0] = tmp.getURL();
		    		connection[1] = String.valueOf(tmp.getPort());
		    		((Button) findViewById(com.skript.muer.R.id.sendbutton)).
		    			setText("Connect to " + tmp.getName());
	    		} else
	    			Log.d("L8","Server came back with an exception : " + e);




	    	}
	    });



			
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.skript.muer.R.menu.activity_login, menu);
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
                    return "Servers";
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
            	final RelativeLayout frag =  (RelativeLayout) inflater.inflate(com.skript.muer.R.layout.connect_screen,null);

            	Button connectionButton = (Button) frag.findViewById(com.skript.muer.R.id.sendbutton);


                connectionButton.setOnClickListener(new View.OnClickListener() {
        			
        			@Override
        			public void onClick(View arg0) {
        				String [] arr = new String[2];


                        EditText name = (EditText)frag.findViewById(R.id.accountName);
                        EditText password = (EditText)frag.findViewById(R.id.accountPassword);

                        if(!name.getText().toString().equals("") && !password.getText().toString().equals("")) {
                            connection[2] = name.getText().toString();
                            connection[3] = password.getText().toString();
                        }

        		    	Intent i = new Intent(ct,GameWindow.class);
        		    	i.putExtra("CONNECTION_INFO", connection);



        		    	startActivity(i);
        			}
        		});

                return frag;
            }
            else  if(arg == 2) {

                final RelativeLayout frag =  (RelativeLayout) inflater.inflate(R.layout.options_menu,null);

            	StaggeredGridLayoutManager ll = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL);



            	if(lv == null ) {

                    lv = (RecyclerView) frag.findViewById(R.id.ServerRecycler);
                }



                ((Button)frag.findViewById(R.id.addServerButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            RelativeLayout rLay = (RelativeLayout) frag.findViewById(R.id.AddServerView);

                            String portText = ((EditText) rLay.findViewById(R.id.portEditText)).getText().toString();
                            String nameText = ((EditText) rLay.findViewById(R.id.nameEditText)).getText().toString();
                            String addyText = ((EditText) rLay.findViewById(R.id.serverEditText)).getText().toString();

                            if (portText.toString() == "" || nameText.toString() == "" || addyText.toString() == "") {
                                System.out.println("Failed");
                            } else {
                                //LoginActivity.writeServers();
                                System.out.println(portText + "ONETWOTHREE" + nameText + addyText);

                                DataController.add(new Server(Integer.parseInt(portText), addyText, "", nameText));
                                //DataController.add(new Server(Port,addyText,"",nameText));
                                LoginActivity.writeServers();
                                LoginActivity.readServers();

                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });

//
//                lv.setOnItemClickListener(new OnItemClickListener(){
//
//                    @Override
//                    public void onItemClick(AdapterView<?> arg0, View arg1,
//                                            int arg2, long arg3) {
//                        if( arg2 > 0 ) {
//                            Server tmpServer = DataController.get(arg2 - 1);
//                            connection[0] = tmpServer.getURL();
//                            connection[1] = String.valueOf(tmpServer.getPort());
//                        }
//                    }
//
//
//                });
//
                readServers();

            	sla = new ServerRecyclerAdapter(DataController.List(),ct);
                lv.setLayoutManager(ll);
                lv.setAdapter(sla);
            	
            	return frag;
            }
            return textView;
        }

    }

}
