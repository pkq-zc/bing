package com.buydeem.bing.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zengchao on 2019/12/13.
 */
public class DateTimeUtils {
  public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
  public static final String TIME = "HH:mm:ss";

  public static Date string2Date(String timeStr, String format) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.parse(timeStr);
  }

  public static String date2String(Date date){
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME);
    return sdf.format(date);
  }

}
