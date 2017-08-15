package com.skript.muer;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ServerListAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	
	public ServerListAdapter(Context theContext)
	{
		layoutInflater = LayoutInflater.from(theContext);
	}

	public int getCount() {
		
		return DataController.getItemCount() + 1;
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


		if (position > 0) {

			oldView = layoutInflater.inflate(
						R.layout.server_connection_cell, parent, false);


			Server tempCourse = DataController.get(position - 1);


			((TextView) oldView.findViewById(R.id.gameName)).
					setText("Name: " + tempCourse.getName());

			((TextView) oldView.findViewById(R.id.hostName)).
					setText("URL: " + tempCourse.getURL());

			((TextView) oldView.findViewById(R.id.portNumber)).
					setText("Port: " + tempCourse.getPort());




		} else {

			oldView = layoutInflater.inflate(
					R.layout.add_server_cell, parent, false);


			((EditText)oldView.findViewById(R.id.descriptionText)).addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					DataController.serverName = editable.toString();
				}
			});
			((EditText)oldView.findViewById(R.id.portText)).addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					DataController.portNumber = editable.toString();
				}
			});
			((EditText)oldView.findViewById(R.id.serverText)).addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					DataController.serverAddress = editable.toString();
				}
			});



			((Button)oldView.findViewById(R.id.addbutton)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					String portText = DataController.portNumber;
					String nameText =	DataController.serverName;
					String addyText = DataController.serverAddress;

					if(portText.toString() == "" || nameText.toString() == "" || addyText.toString() == "") {
						System.out.println("Failed");
					} else {
						LoginActivity.writeServers();
					}

					System.out.println(portText + "ONETWOTHREE" + nameText + addyText);

					DataController.add(new Server(Integer.parseInt(portText),addyText,"",nameText));
					//DataController.add(new Server(Port,addyText,"",nameText));
					LoginActivity.writeServers();
					LoginActivity.readServers();

				}
			});

		}


		oldView.setBackgroundResource(R.drawable.rounded);
		return oldView;
	}

}
