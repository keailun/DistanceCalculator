package kermis.maplocations;

import java.util.*;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.widget.DatePicker;
import android.app.Dialog;
import android.os.Bundle;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        // set the maximum date
        Calendar calendar = Calendar.getInstance();//get the current day
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()); //set today's date as the max date
        // Create a new instance of DatePickerDialog and return it
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        DialogValuesCallback activity = (DialogValuesCallback) getActivity();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        activity.getDateValueFromDialog(c); // invoke listener to send data through to activity
        dismiss();
    }
}