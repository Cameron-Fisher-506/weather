package za.co.weather.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DTUtilsTest
{

    @Test
    public void getDateTimeFromDecimal()
    {
        Long decimal = 1611118634L;
        String result = DTUtils.getDateTimeFromDecimal(decimal);
        assertEquals("2021-01-20 08:57:14", result);
    }

    @Test
    public void getDateFromDecimal()
    {
        Long decimal = 1611118634L;
        String result = DTUtils.getDateFromDecimal(decimal);
        assertEquals("2021-01-20", result);
    }

    @Test
    public void getTimeFromDecimal()
    {
        Long decimal = 1611118634L;
        String result = DTUtils.getTimeFromDecimal(decimal);
        assertEquals("08:57", result);
    }

}
