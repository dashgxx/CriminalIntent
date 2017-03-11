package com.example.xx.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.io.BufferedReader;
import java.util.UUID;

/**
 * Created by XX on 2017/3/7.
 */

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    private boolean hasModified=false;

    private static final String ARG_CRIME_ID="crime_id";

    public static CrimeFragment newInstance(UUID id) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID uuid=(UUID) getArguments().getSerializable(ARG_CRIME_ID);

        mCrime=CrimeLab.get(getActivity()).getCrime(uuid);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleField=(EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
                returnResult();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton=(Button) v.findViewById(R.id.crime_date);
        final String dateString = DateFormat.format("E,MMM dd,yyyy",mCrime.getDate()).toString();
        mDateButton.setText(dateString);
        mDateButton.setEnabled(false);

        mSolvedCheckBox=(CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
                returnResult();
            }
        });
        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void returnResult()
    {
        if(!hasModified)
        {
            Intent data=new Intent();
            int index=CrimeLab.get(getActivity()).getCrimes().indexOf(mCrime);
            data.putExtra(CrimeListFragment.EXTRA_CHANGED_INDEX,index);
            getActivity().setResult(Activity.RESULT_OK,data);
        }
    }
}
