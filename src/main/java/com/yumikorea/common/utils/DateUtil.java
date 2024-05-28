package com.yumikorea.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;

public class DateUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String FROM_DATE_SUFFIX = " 00:00:00";
    private static final String TO_DATE_SUFFIX = " 23:59:59";

    // Private constructor to prevent instantiation
    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }

    // Method to convert a string to a Date using the default format
    public static Date parseDate(String dateStr, int fromOrTo) throws ParseException {
    	if ( fromOrTo == 0 ) {
    		return parseDate(dateStr + FROM_DATE_SUFFIX, DEFAULT_DATE_FORMAT );
    		
    	} else if ( fromOrTo == 1 ) {
    		return parseDate(dateStr + TO_DATE_SUFFIX, DEFAULT_DATE_FORMAT );
    		
    	}else {
    		throw new IllegalStateException(EAdminMessages.WRONG_PARAMETER.getMessage());
    	}
    }

    // Method to convert a string to a Date using a specified format
	public static Date parseDate(String dateStr, String format) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	return sdf.parse(dateStr);
    }
    
    public static Date parseDate(String dateStr) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    	return sdf.parse(dateStr);        	
    }

    // Method to format a Date as a string using the default format
    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_FORMAT);
    }

    // Method to format a Date as a string using a specified format
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
    	return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
    	return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    
    public static Boolean isFirstDateGtOther(Date date1, Date date2) {
    	Boolean rst = false;
    	if( date1.getTime() > date2.getTime() ) {
    		rst = true;
    	}
    	return rst;
    }
    
    public static Boolean isFirstDateGtOther(String date1, String date2) throws ParseException {
    	Boolean rst = false;
    	if( parseDate(date1).getTime() > parseDate(date2).getTime() ) {
    		rst = true;
    	}
    	return rst;
    }
    
    public static String getDateMinus(Date date, int minusValue) {
    	long d = date.getTime() - (minusValue * 1000 * 60 * 60 * 24);
    	return formatDate(new Date(d), EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
    }
    
    
    public static Date addMonth(Date date, int addValue) {
		LocalDateTime dateTime = DateUtil.dateToLocalDateTime(date);
		LocalDateTime tempDateTime;
		Date rst ;
		// 양의 수일 경우 + add
		if( addValue > 0 ) {
			tempDateTime = dateTime.plusMonths(addValue);
			rst = DateUtil.localDateTimeToDate(tempDateTime);
		}else if( addValue == 0 ) {
			rst = date;
		} else {
			tempDateTime = dateTime.minus(Math.abs(addValue), ChronoUnit.MONTHS);
			rst = DateUtil.localDateTimeToDate(tempDateTime);
			
		}
    	
    	return rst;
    }
    
    
    public static String formatDateString( String date ) {
    	if( date == null ) return "";
    	if( date.length() < 8 ) return date;
    	date = date.replaceAll("-","").replaceAll("/","").replaceAll("\\.","").replaceAll(" ","");
    	date = date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8);
    	return date;
    }
    
}