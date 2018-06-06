package kermis.maplocations;

public class Location {

    private int id;
    private String date_created;
    private String date_disabled;
    private double lat;
    private double lng;

    public Location(int id, String dateCreated, String dateDisabled, double latitude, double longtitude){
        this.id = id;
        date_created = dateCreated;
        date_disabled = dateDisabled;
        lat = latitude;
        lng = longtitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public String getDateCreated() {
        return date_created;
    }

    public void setDateCreated(String _dateCreated) {
        this.date_created = _dateCreated;
    }

    public String getDateDisabled() {
        return date_disabled;
    }

    public void setDateDisabled(String _dateDisabled) {
        this.date_disabled = _dateDisabled;
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(double _latitude) {
        this.lat = _latitude;
    }

    public double getLongitude() {
        return lng;
    }

    public void setLongitude(double _longitude) {
        this.lng = _longitude;
    }
}
