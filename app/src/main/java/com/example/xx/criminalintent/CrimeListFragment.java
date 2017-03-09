package com.example.xx.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by XX on 2017/3/9.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mTitleTextView,mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),mCrime.getTitle()+" clicked!",Toast.LENGTH_SHORT).show();
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

    private void updateUI()
    {
        mAdapter=new CrimeAdapter(CrimeLab.get(getActivity()).getCrimes());
        mCrimeRecyclerView.setAdapter(mAdapter);
    }
}
