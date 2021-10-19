import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONObject;

public class OpenWeatherMain {

     public static void main(String[] args) {
        String apiURL = "http://api.openweathermap.org/data/2.5/weather?q=";
        String apiID  = "&appid=7094e857031edaa18481765f4bda7b28&units=metric";
       //  http://api.openweathermap.org/data/2.5/weather?q=Tel-Aviv&appid=7094e857031edaa18481765f4bda7b28&units=metric
        printRequiredCities(apiURL, apiID);
    }

    private static void printRequiredCities(String apiURL, String apiID){
        String[] citiesNames = { "Tel-Aviv", "Singapore", "Auckland", "Ushuaia", "Miami", "London", "Berlin", "Reykjavik", "Cape Town", "Kathmandu" };
        String longestDayCity = null;
        String shortestDayCity = null;
        float shortestDay = 0;
        float longestDay = 0;
        float shortestTemp = 0;
        float longestTemp = 0;

        System.out.println("All cities print:");
        System.out.println();
        for (String currentCity : citiesNames) {
            try {
                JSONObject currentJsonObject = getJSONByCityName(currentCity, apiURL, apiID);
                float temp = currentJsonObject.getJSONObject("main").getFloat("temp");
                long sunrise = currentJsonObject.getJSONObject("sys").getLong("sunrise");
                long sunset = currentJsonObject.getJSONObject("sys").getLong("sunset");

                float dayLong = sunset - sunrise;

                if (longestDayCity == null || longestDay < dayLong) {
                    longestDay = dayLong;
                    longestDayCity = currentCity;
                    longestTemp = temp;
                }

                if (shortestDayCity == null || shortestDay > dayLong) {
                    shortestDay = dayLong;
                    shortestDayCity = currentCity;
                    shortestTemp = temp;
                }

                System.out.println("City Name: " + currentCity + " Temp: " + temp + " Sunrise: " + sunrise + " Sunset: " + sunset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println();
        System.out.println("Shortest daylight city is: "+ shortestDayCity+ " and the temp is: " + shortestTemp);
        System.out.println("Longest daylight city is: "+ longestDayCity+ " and the temp is: " + longestTemp);

    }

    private static JSONObject getJSONByCityName(String i_CityName, String apiURL, String apiID) throws Exception {
        String urlString = apiURL + i_CityName + apiID;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        URL urlRequest = new URL(urlString);
        try {
            inputStream = urlRequest.openStream();
        } catch (IOException e) {
            System.err.println("Had issue connect to server");
        }

        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
        //System.out.println(readAllData(bufferedReader));
        return new JSONObject(readAllData(bufferedReader));
    }

    private static String readAllData(BufferedReader i_Reader) throws IOException {
        StringBuilder dataString = new StringBuilder();
        int charAsInt;

        while ((charAsInt = i_Reader.read()) != -1) {
            dataString.append((char) charAsInt);
        }
        return dataString.toString();
    }
}
