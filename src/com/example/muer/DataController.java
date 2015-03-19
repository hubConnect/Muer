package com.example.muer;

import java.util.LinkedList;

	
	public class DataController {
	
		private static LinkedList<Server> dataList;
		
		// the always-festive static initializer
		static
		{
			dataList = new LinkedList<Server>();
		}
		
		public static void clearList() {
			dataList.clear();
		}
		
		public static int getItemCount()
		{
			return dataList.size();
		}
		
		public static Server get(int index)
		{
			return dataList.get(index);
		}
		
		public static void add(Server newItem)
		{
			dataList.add(newItem);
		}
}
