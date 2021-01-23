package za.co.weather.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocationUtilsTest
{
    @Test
    public void meterToKilometer()
    {
        Double result = LocationUtils.meterToKilometer(10000L);
        assertEquals(10.0, result,0);
    }
}
