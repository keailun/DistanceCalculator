package kermis.maplocations;

import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import java.util.*;

public class MarkerManager implements GoogleMap.OnMarkerClickListener {
    private List<Marker> mSelectedMarkersTracker;
    private Button mCalculateDistanceButton;

    public MarkerManager(Button calculate){
         mSelectedMarkersTracker = new ArrayList<Marker>();
         mCalculateDistanceButton = calculate;
    }

    // remove marker from tracker array
    private void removeFromTracker(Marker marker)
    {
        mSelectedMarkersTracker.remove(marker);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    }

    // addMarkerToTracker marker to tracker array
    private void addMarkerToTracker(Marker marker)
    {
        mSelectedMarkersTracker.add(marker);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
    }

    private void clearAllMarkersFromTracker()
    {
        for (Marker marker : mSelectedMarkersTracker)
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        mSelectedMarkersTracker.clear();
    }

    public Marker [] getSelectedMarkers ()
    {
        return mSelectedMarkersTracker.toArray(new Marker[mSelectedMarkersTracker.size()]);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mSelectedMarkersTracker.contains(marker)) {
            removeFromTracker(marker);
        }
        else
        {
            if (mSelectedMarkersTracker.size() == 2)  // reset
                clearAllMarkersFromTracker();

            addMarkerToTracker(marker);
        }

        // only show calculate distance button when two markers are selected
        mCalculateDistanceButton.setVisibility(mSelectedMarkersTracker.size() == 2 ? View.VISIBLE : View.GONE);
        return false;
    }
}
