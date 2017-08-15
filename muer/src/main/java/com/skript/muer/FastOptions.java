package com.skript.muer;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;

import com.skript.muer.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class FastOptions extends Activity {

	private RecyclerView recyclerView;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;
	private ArrayList<String> macroList;
	private String FILENAME = "macros.dat";

	@Override
	protected void onResume() {
		super.onResume();
		macroList = readMacros();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fast_options);
		macroList = new ArrayList<String>();

		recyclerView = (RecyclerView) findViewById(R.id.RecyclerGuy);
		layoutManager = new StaggeredGridLayoutManager(7,StaggeredGridLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(layoutManager);

		macroList = readMacros();

		adapter = new RecyclerAdapter(macroList,getApplicationContext());
		recyclerView.setAdapter(adapter);

		adapter.notifyDataSetChanged();

	}

	private ArrayList<String> readMacros() {

		try {

			FileInputStream file = new FileInputStream(getFilesDir().getPath().toString() + FILENAME );
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);

			//deserialize the List
			return (ArrayList<String>)input.readObject();
		}
		catch(Exception ex){
			System.out.println("ERR" + ex);
		}

		ArrayList<String> defaultList = new ArrayList<String>();

		defaultList.add("wave;say Hello! I love macros!");
		defaultList.add("/help");
		return defaultList;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_fast_options, menu);
		return true;
	}

}
