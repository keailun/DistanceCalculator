package kermis.maplocations;

public class DistanceObject
{
    private double distance;

    public double getDistance ()
    {
        return distance;
    }

    public void setDistance (double distance)
    {
        this.distance = distance;
    }

    @Override
    public String toString()
    {
        return distance + "";
    }
}
