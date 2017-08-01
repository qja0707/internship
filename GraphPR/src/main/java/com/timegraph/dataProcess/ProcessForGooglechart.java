package com.timegraph.dataProcess;

import java.util.List;

public class ProcessForGooglechart {
	
	public List<List<Object>> dateToString(List<List<Object>> items){
		for (List<Object> list : items) {
			String newItem = String.valueOf(list.get(0));
			newItem = newItem.substring(0, 10);
			newItem = "\""+newItem+"\"";
			list.set(0, newItem);
		}
		return items;
	}
}
