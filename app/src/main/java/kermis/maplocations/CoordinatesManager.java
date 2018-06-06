package kermis.maplocations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public final class CoordinatesManager {

    private static String mServerAddress = "http://188.166.115.129:4545/coordinates?time=";

    public static Location[] getLocationsFromServer(Calendar date) {

        String url = mServerAddress + (date.getTimeInMillis() / 1000); // attach unix timestamp to URL

        HttpGetMethod con = new HttpGetMethod();

        try {
            String result = con.execute(url).get();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(result, Location[].class);
        }
        catch(Exception e)
        {
        }
        return null;
    }
}