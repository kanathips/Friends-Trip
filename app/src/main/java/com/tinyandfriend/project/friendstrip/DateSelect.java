package com.tinyandfriend.project.friendstrip;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import static com.tinyandfriend.project.friendstrip.R.id.editText;

/**
 * Created by NewWy on 22/10/2559.
 */

public class DateSelect implements DatePickerDialog.OnDateSetListener{


    private final DatePickerDialog datePicker;
    private final Context context;
    private final EditText editText;

    public DateSelect(Context context, final EditText editText){

        this.context = context;
        this.editText = editText;

        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePickerDialog(context, this, yy, mm, dd);
        datePicker.getDatePicker().setMinDate(new Date().getTime());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(month+1)
                + "-" + String.valueOf(year);
        editText.setText(date);
        editText.setError(null);
    }

    public void show(){
        datePicker.show();
    }

    public Context getContext() {
        return context;
    }

    public EditText getEditText() {
        return editText;
    }

    public DatePickerDialog getDatePicker() {
        return datePicker;
    }


}
