package kermis.maplocations;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public final class DistanceManager {

    private static String mServerAddress = "http://188.166.115.129:4545/distance";

    public static double calculateKilometerDistanceFromServer(Marker marker1, Marker marker2) {
        String bodyText = getMappedPointObject(marker1.getPosition(), marker2.getPosition()); // get Json String version of object

        HttpPostMethod con = new HttpPostMethod();

        try {
            String result = con.execute(mServerAddress, bodyText).get();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            DistanceObject obj = gson.fromJson(result, DistanceObject.class); // map Json String into object

            if (obj != null)
                return (double) Math.round((obj.getDistance()/1000) * 100) / 100; // round off to two decimal places
        }
        catch(Exception e)
        {
        }

        return 0;
    }

    private static String getMappedPointObject(LatLng latLngObj1, LatLng latLngObj2)
    {
        // map latlng objects to Point1 and Point2 objects, and then wrap them
        Point1 point1 = new Point1();
        point1.setLat(latLngObj1.latitude);
        point1.setLng(latLngObj1.longitude);
        Point2 point2 = new Point2();
        point2.setLat(latLngObj2.latitude);
        point2.setLng(latLngObj2.longitude);
        PointsObject obj = new PointsObject();
        obj.setPoint1(point1);
        obj.setPoint2(point2);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        return gson.toJson(obj);
    }
}
