package com.example.xx.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by XX on 2017/3/13.
 */

public class DateickerFragment extends DialogFragment {

    private DatePicker mDatePicker;

    public static final String ARG_DATE="date";

    public static DateickerFragment newInstance(Date date) {

        Bundle args = new Bundle();

        args.putSerializable(ARG_DATE,date);

        DateickerFragment fragment = new DateickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

/*    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_date,container,false);



        return view;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date=(Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        final int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_date,null);

        mDatePicker=(DatePicker)view.findViewById(R.id.dialog_date_datepicker);
        mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Date date=new GregorianCalendar(i,i1,i2).getTime();

                getArguments().putSerializable(ARG_DATE,date);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year=mDatePicker.getYear();
                        int month=mDatePicker.getMonth();
                        int day=mDatePicker.getDayOfMonth();
                        Date date=new GregorianCalendar(year,month,day).getTime();
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode,Date date)
    {
        if(getTargetFragment()==null)
            return;

        Intent intent=new Intent();
        intent.putExtra(ARG_DATE,date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
