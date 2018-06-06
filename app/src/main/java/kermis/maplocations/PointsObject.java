package kermis.maplocations;

public class PointsObject
{
    private Point1 point1;
    private Point2 point2;

    public Point1 getPoint1 ()
    {
        return point1;
    }

    public void setPoint1 (Point1 point1)
    {
        this.point1 = point1;
    }

    public Point2 getPoint2 ()
    {
        return point2;
    }

    public void setPoint2 (Point2 point2)
    {
        this.point2 = point2;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [point1 = "+point1+", point2 = "+point2+"]";
    }
}



