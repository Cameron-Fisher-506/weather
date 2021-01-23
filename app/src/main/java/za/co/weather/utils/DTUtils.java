package za.co.weather.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DTUtils
{
    public static String getCurrentDateTime()
    {
        String toReturn = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        now = calendar.getTime();

        toReturn = simpleDateFormat.format(now);

        return toReturn;

    }

    public static String getForecastDate(int increaseBy)
    {
        String toReturn = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, increaseBy);
        now = calendar.getTime();

        toReturn = simpleDateFormat.format(now);

        return toReturn;

    }

    public static String getDayFromDate(String strDate)
    {
        String toReturn = null;

        try
        {
            if(strDate != null)
            {
                SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                Date date = format1.parse(strDate);

                if(date != null)
                {
                    DateFormat dateFormat = new SimpleDateFormat("EEEE");
                    toReturn = dateFormat.format(date);
                }

            }
        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: DTUtils - getDayFromDate"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }


        return toReturn;
    }

    public static String getTimeFromDecimal(Long decimalDate)
    {
        String toReturn = "";

        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");
        Date now = new Date(decimalDate * 1000);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY,2); // this will add two hours
        now = calendar.getTime();
        toReturn = sdfDate.format(now);

        return toReturn;
    }

    public static String getDateTimeFromDecimal(Long decimalDate)
    {
        String toReturn = "";

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(decimalDate * 1000);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY,2); // this will add two hours
        now = calendar.getTime();
        toReturn = sdfDate.format(now);

        return toReturn;
    }

    public static String getDateFromDecimal(Long decimalDate)
    {
        String toReturn = "";

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(decimalDate * 1000);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY,2); // this will add two hours
        now = calendar.getTime();
        toReturn = sdfDate.format(now);

        return toReturn;
    }
}
