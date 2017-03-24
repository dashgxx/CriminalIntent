package com.example.xx.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by XX on 2017/3/19.
 */

public class PhonePickerFragment extends DialogFragment {

    private static final String ARG_BUTTON_LIST="button_list";
    public static final String EXTRA_PHONE_NUMBER ="phone_number";

    private String[] mButtonList;

    public static PhonePickerFragment newInstance(String[] buttonList) {

        Bundle args = new Bundle();

        args.putSerializable(ARG_BUTTON_LIST,buttonList);

        PhonePickerFragment fragment = new PhonePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mButtonList =(String[]) getArguments().getSerializable(ARG_BUTTON_LIST);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.phone_picker_title)
                .setItems(mButtonList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK, mButtonList[i]);
                    }
                })
                .create();
        //return super.onCreateDialog(savedInstanceState);
    }

    private void sendResult(int resultCode,String phoneNumber)
    {
        if(getTargetFragment()==null)
            return;

        Intent intent=new Intent();
        intent.putExtra(EXTRA_PHONE_NUMBER, phoneNumber);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
