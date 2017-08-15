package com.skript.muer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DataController {
	
		private static ArrayList<Server> dataList;
		
		// the always-festive static initializer
		static
		{
			dataList = new ArrayList<Server>();
		}
		
		public static void clearList() {
			dataList.clear();
		}
		
		public static int getItemCount()
		{
			return dataList.size();
		}
		public static List<Server> List() { return dataList; }
		public static Server get(int index)
		{
			return dataList.get(index);
		}
		public static String portNumber = "";
		public static String serverName = "";
		public static String serverAddress = "";
		public static void add(Server newItem)
		{
			dataList.add(newItem);
		}
		public static void remove ( Server newItem ) { dataList.remove(newItem); }
}
