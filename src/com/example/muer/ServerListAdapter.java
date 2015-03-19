package com.example.muer;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ServerListAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	
	public ServerListAdapter(Context theContext)
	{
		layoutInflater = LayoutInflater.from(theContext);
	}

	public int getCount() {
		
		return DataController.getItemCount();
	}

	public Object getItem(int arg0) {

		return DataController.get(arg0);
	}


	public long getItemId(int arg0) {

		return arg0;
	}


	public View getView(int position, View oldView,
	           ViewGroup parent) 
	{

    	if (oldView == null) {                                        
            oldView = layoutInflater.inflate(
                     R.layout.server_connection_cell,parent, false);
         } 

        Server tempCourse = DataController.get(position);
         
        ((TextView) oldView.findViewById(R.id.gameName)).
         setText("Name: " + tempCourse.getName());
         
        ((TextView) oldView.findViewById(R.id.hostName)).
         setText("Capacity: " + tempCourse.getURL());
        
        ((TextView) oldView.findViewById(R.id.portNumber)).
         setText("Port: " + tempCourse.getPort());
        
        ((TextView) oldView.findViewById(R.id.gameDescription)).
        setText("Description: " + tempCourse.getDescription());
         
         Log.d("Console","Getting");

         return oldView;
      }

}
