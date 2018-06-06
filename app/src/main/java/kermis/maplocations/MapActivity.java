package kermis.maplocations;

import android.app.AlertDialog;
import android.support.v4.app.FragmentActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import java.util.*;
import java.text.SimpleDateFormat;
import android.content.Context;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import android.graphics.Color;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.Typeface;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, DialogValuesCallback {

    private GoogleMap mMap;
    private Button mBtnSelectDate;
    private Button mBtnCalculateDistance;
    private EditText mTxtCurrentDate;
    private MarkerManager mMarkerManager;
    private Calendar mCurrentSelectedDate; // keep track of date selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maplocations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        mTxtCurrentDate = findViewById(R.id.txtCurrentDate);
        mTxtCurrentDate.setKeyListener(null); // make sure that current date display is uneditable
        mBtnSelectDate = findViewById(R.id.btnSelectDate);
        mBtnCalculateDistance = findViewById(R.id.btnCalculateDistance);
        mBtnCalculateDistance.setVisibility(View.GONE); // make sure calculate distance button is invisible on load

        mCurrentSelectedDate = Calendar.getInstance();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // set info window adapter for marker descriptions
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context applicationContext = getApplicationContext();
                LinearLayout linearLayout = new LinearLayout(applicationContext);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                // set attributes for header
                TextView markerTitle = new TextView(applicationContext);
                markerTitle.setTextColor(Color.BLACK);
                markerTitle.setGravity(Gravity.CENTER);
                markerTitle.setTypeface(null, Typeface.BOLD);
                markerTitle.setText(marker.getTitle());

                // set attributes for body
                TextView markerSnippet = new TextView(applicationContext);
                markerSnippet.setTextColor(Color.GRAY);
                markerSnippet.setText(marker.getSnippet());

                // add to the linear layout
                linearLayout.addView(markerTitle);
                linearLayout.addView(markerSnippet);

                return linearLayout;
            }
        });

        populateMarkersOnMap(mCurrentSelectedDate); // populate markers on map with current date
    }

    public void onBtnSelectDateClick(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Bundle argumentBundle = new Bundle();
        argumentBundle.putInt("year", mCurrentSelectedDate.get(Calendar.YEAR));
        argumentBundle.putInt("month", mCurrentSelectedDate.get(Calendar.MONTH));
        argumentBundle.putInt("day", mCurrentSelectedDate.get(Calendar.DATE));
        datePickerFragment.setArguments(argumentBundle);
        datePickerFragment.show(this.getFragmentManager(), "datePicker");
    }

    // run when calculate distance button is clicked by user
    public void onBtnCalculateDistanceClick(View v)
    {
        Marker [] selectedMarkers = mMarkerManager.getSelectedMarkers();

        // make sure that two markers have been selected before getting the distance between them from the server
        if (selectedMarkers.length == 2) {
            double calculatedDistance = DistanceManager.calculateKilometerDistanceFromServer(selectedMarkers[0], selectedMarkers[1]); // in kilometers
            showDialog("Info", "Distance between these two locations is : " + calculatedDistance + " kilometers.");
        }
    }

    // receive data from date picker after the user enters OK, and then repopulate map with data returned from server
    @Override
    public void getDateValueFromDialog(Calendar c){
        populateMarkersOnMap(c);
    }

    // method for showing a basic dialog locally
    private void showDialog (String title, String message)
    {
        AlertDialog dialog = new AlertDialog.Builder(MapActivity.this).create();
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }

    private void populateMarkersOnMap(Calendar date)
    {
        mMap.clear(); // clean the map first
        mMarkerManager = new MarkerManager(mBtnCalculateDistance); // create a new, fresh marker manager
        mMap.setOnMarkerClickListener(mMarkerManager); // add listener onto map
        Location [] locationsFromServer = CoordinatesManager.getLocationsFromServer(date);

        if (locationsFromServer.length > 0) {
            LatLngBounds.Builder rangeBuilder = new LatLngBounds.Builder(); // used to zoom the camera after markers have been placed

            for (int i = 0; i < locationsFromServer.length; i++) {
                LatLng currentLocationLatLng = new LatLng(locationsFromServer[i].getLatitude(), locationsFromServer[i].getLongitude());
                rangeBuilder.include(currentLocationLatLng); // keep track of the current range
                String id = ("ID: "+ locationsFromServer[i].getId());

                // build teh string for the marker description
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(System.getProperty("line.separator"));
                stringBuilder.append("Date created: " + locationsFromServer[i].getDateCreated());
                stringBuilder.append(System.getProperty("line.separator"));
                stringBuilder.append("Date disabled: " + locationsFromServer[i].getDateDisabled());

                // finally add the marker to the map
                mMap.addMarker(new MarkerOptions().position(currentLocationLatLng).title(id).snippet(stringBuilder.toString()));
            }

            final LatLngBounds bounds = rangeBuilder.build();
            final int padding = 10;

            try {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            } catch (IllegalStateException ise)
            {
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                    }
                });
            }
        }
        else
        {
            showDialog("Info", "There are no active records on the date you selected.");
        }

        mCurrentSelectedDate = date;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String cleanDate = format.format(date.getTime());
        mTxtCurrentDate.setText("Results for: " + cleanDate);
    }
}
