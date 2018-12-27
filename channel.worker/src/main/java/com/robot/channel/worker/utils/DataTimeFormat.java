package com.robot.channel.worker.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataTimeFormat {
    private static final Logger logger = LoggerFactory.getLogger(DataTimeFormat.class);
    public static int DATE_TIMEZONE_OFFSET = 8;

    public static String dataOrTimeFormat(String dataOrTime, String oldFormat, String newFormat)
            throws Exception {
        try {
            SimpleDateFormat oldDF = new SimpleDateFormat(oldFormat);
            oldDF.setLenient(false);
            SimpleDateFormat newDF = new SimpleDateFormat(newFormat);

            Date date = oldDF.parse(dataOrTime);

            return newDF.format(date);
        } catch (Exception e) {
            throw new Exception("Failed", e);
        }
    }

 
    public static Date generalizedTimeToDateString(String generalizedTime) {
        String formatString = "yyyyMMddHHmmss";
        try {
            DateFormat format = new SimpleDateFormat(formatString);

            if (generalizedTime != null) {
                boolean flag = false;
                String tag = "";
                if (generalizedTime.indexOf("+") > 0) {
                    tag = "+";
                    flag = true;
                } else if (generalizedTime.indexOf("-") > 0) {
                    tag = "-";
                    flag = true;
                }
                logger.info("tag:" + tag);
                logger.info("flag:" + flag);
                if (flag) {
                    String[] dateArray = generalizedTime.split("\\" + tag);
                    Date date = format.parse(dateArray[0]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(date.getTime());
                    calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                    int offset =
                            DATE_TIMEZONE_OFFSET - Integer.parseInt(dateArray[1].substring(0, 2));
                    calendar.add(Calendar.HOUR, offset);
                    if (dateArray[1].length() == 4) {
                        offset =
                                (offset > 0 ? 1 : -1) * Integer.parseInt(dateArray[1].substring(2));
                        calendar.add(Calendar.MINUTE, offset);
                    }
                    logger.info("return calendar.getTime():" + calendar.getTime());
                    return calendar.getTime();
                } else {
                    logger.info("return format.parse(generalizedTime):"
                            + format.parse(generalizedTime));
                    return format.parse(generalizedTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("return null");
        return null;
    }

}
