package com.nitin.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationUtil {
	private static Logger log = LoggerFactory.getLogger(ApplicationUtil.class);
	
	public static boolean isObjectEmpty(Object object) {
		if(object == null) return true;
		else if(object instanceof String) {
			if (((String)object).trim().length() == 0) {
				return true;
			}
		} else if(object instanceof Collection) {
			return isCollectionEmpty((Collection<?>)object);
		}
		return false;
	}

	private static boolean isCollectionEmpty(Collection<?> object) {
		if (object == null || object.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static Date convertToDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Date mydate = null;
		try {
			mydate = df.parse(date);
		} catch (Exception e){
			log.error("Error parsing the date : "+e.getMessage());
		}
		log.debug("The date is converted from  : "+ date +" to : "+ mydate.toString());
		return mydate;
	}
	
	public static int compareDate(Date startDate, Date endDate) {
		return startDate.compareTo(endDate);
	}

}
