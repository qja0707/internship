package com.timegraph.dataProcess;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MakingDate {

	public String dateToday() {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		
		return format.format(now);
	}
}
