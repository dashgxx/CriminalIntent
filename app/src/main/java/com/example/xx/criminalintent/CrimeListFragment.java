package com.example.xx.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by XX on 2017/3/9.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    private static final int REQUEST_CRIME=1;
    public static final String EXTRA_CHANGED_INDEX="com.example.xx.criminalintent.changed_index";

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mTitleTextView,mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(),mCrime.getTitle()+" clicked!",Toast.LENGTH_SHORT).show();

            UUID uuid=mCrime.getId();
            Intent intent=CrimeActivity.newIntent(getActivity(),uuid);
            startActivityForResult(intent,REQUEST_CRIME);
        }

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView=(TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView=(TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox=(CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        public void bindCrime(Crime crime)
        {
            mCrime=crime;
            mSolvedCheckBox.setChecked(crime.isSolved());
            mTitleTextView.setText(crime.getTitle());
            final String dateString = DateFormat.format("E,MMM dd,yyyy",crime.getDate()).toString();
            mDateTextView.setText(dateString);
        }

    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>
    {
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes) {
            super();

            mCrimes=crimes;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            View view=layoutInflater.inflate(R.layout.list_item_crime,parent,false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime=mCrimes.get(position);
            holder.bindCrime(crime);
        }


    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView=(RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CRIME)
            if(resultCode== Activity.RESULT_OK)
            {
                int index=data.getIntExtra(EXTRA_CHANGED_INDEX,-1);
                if(index>=0)
                    mAdapter.notifyItemChanged(index);
            }
    }

    @Override
    public void onResume() {
        super.onResume();
        //updateUI();
    }

    private void updateUI()
    {
        if(mAdapter==null)
        {
            mAdapter=new CrimeAdapter(CrimeLab.get(getActivity()).getCrimes());
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else
            mAdapter.notifyDataSetChanged();
    }
}
