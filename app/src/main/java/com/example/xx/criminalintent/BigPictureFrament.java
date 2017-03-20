package com.example.xx.criminalintent;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by XX on 2017/3/20.
 */

public class BigPictureFrament extends DialogFragment {

    private ImageView mBigPicture;

    private static final String ARG_PATH="path";

    public static BigPictureFrament newInstance(String path) {

        Bundle args = new Bundle();

        args.putSerializable(ARG_PATH,path);

        BigPictureFrament fragment = new BigPictureFrament();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String path=(String)getArguments().getSerializable(ARG_PATH);

        mBigPicture=new ImageView(getActivity());
        mBigPicture.setImageBitmap(PictureUtils.getScaledBitmap(path,getActivity()));
        mBigPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigPictureFrament.this.dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(mBigPicture)
                .create();
    }
}
