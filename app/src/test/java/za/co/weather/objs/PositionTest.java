package za.co.weather.objs;

import org.json.JSONObject;
import org.junit.Test;
import za.co.weather.utils.LocationUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PositionTest
{

    @Test
    public void populatePosition()
    {
        try
        {
            String strJSONObject = "{\n" +
                    "  \"coord\": {\n" +
                    "    \"lon\": 28.0436,\n" +
                    "    \"lat\": -26.2023\n" +
                    "  },\n" +
                    "  \"weather\": [\n" +
                    "    {\n" +
                    "      \"id\": 800,\n" +
                    "      \"main\": \"Clear\",\n" +
                    "      \"description\": \"clear sky\",\n" +
                    "      \"icon\": \"01d\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"base\": \"stations\",\n" +
                    "  \"main\": {\n" +
                    "    \"temp\": 19.54,\n" +
                    "    \"feels_like\": 19.87,\n" +
                    "    \"temp_min\": 18,\n" +
                    "    \"temp_max\": 21.11,\n" +
                    "    \"pressure\": 1021,\n" +
                    "    \"humidity\": 82\n" +
                    "  },\n" +
                    "  \"visibility\": 10000,\n" +
                    "  \"wind\": {\n" +
                    "    \"speed\": 2.57,\n" +
                    "    \"deg\": 180\n" +
                    "  },\n" +
                    "  \"clouds\": {\n" +
                    "    \"all\": 0\n" +
                    "  },\n" +
                    "  \"dt\": 1611118634,\n" +
                    "  \"sys\": {\n" +
                    "    \"type\": 1,\n" +
                    "    \"id\": 1935,\n" +
                    "    \"country\": \"ZA\",\n" +
                    "    \"sunrise\": 1611113590,\n" +
                    "    \"sunset\": 1611162250\n" +
                    "  },\n" +
                    "  \"timezone\": 7200,\n" +
                    "  \"id\": 993800,\n" +
                    "  \"name\": \"Johannesburg\",\n" +
                    "  \"cod\": 200\n" +
                    "}";

            JSONObject jsonObject = new JSONObject(strJSONObject);

            Position position = new Position();
            position.populate(jsonObject);

            assertEquals("Johannesburg", position.getCity());
            assertEquals("-26.2023", position.getLatitude());
            assertEquals("28.0436", position.getLongitude());

            assertNotEquals(null, position.getWeather());
            assertEquals(Math.round(19.54), position.getWeather().getTemp(),0);
            assertEquals(Math.round(19.87), position.getWeather().getFeelsLike(),0);
            assertEquals(Math.round(18), position.getWeather().getMinTemp(),0);
            assertEquals(Math.round(21.11), position.getWeather().getMaxTemp(),0);
            assertEquals(1021, position.getWeather().getPressure(),0);
            assertEquals(82, position.getWeather().getHumidity(),0);
            assertEquals(LocationUtils.meterToKilometer(10000L), position.getWeather().getVisibility(),0);
            assertEquals(180, position.getWeather().getWindDegrees(),0);
            assertEquals(2.57, position.getWeather().getWindSpeed(),0);
            assertEquals("2021-01-20", position.getWeather().getDate());
            assertEquals("2021-01-20 08:57:14", position.getWeather().getTimestamp());
            assertEquals("07:33", position.getWeather().getSunrise());
            assertEquals("21:04", position.getWeather().getSunset());
            assertEquals("Clear", position.getWeather().getMain());
            assertEquals("clear sky", position.getWeather().getDescription());

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
