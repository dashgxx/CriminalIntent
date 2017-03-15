package com.example.xx.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Created by XX on 2017/3/9.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    private LinearLayout mLinearLayout;
    private Button mNewCrimeButton;

    private boolean mSubtitleVisible=false;

    private static final int REQUEST_CRIME=1;
    public static final String EXTRA_CHANGED_INDEX="com.example.xx.criminalintent.changed_index";
    public static final String SAVED_SUBTITLE_VISIBLE="subtitle_visible";

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mTitleTextView,mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(),mCrime.getTitle()+" clicked!",Toast.LENGTH_SHORT).show();

            UUID uuid=mCrime.getId();
            //Intent intent=CrimeActivity.newIntent(getActivity(),uuid);
            //startActivityForResult(intent,REQUEST_CRIME);
            createCrimePagerActivity(uuid);
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

    public static CrimeListFragment newInstance(boolean visible) {
        
        Bundle args = new Bundle();

        args.putBoolean(SAVED_SUBTITLE_VISIBLE,visible);

        CrimeListFragment fragment = new CrimeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
            mSubtitleVisible=savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        else
            mSubtitleVisible=getArguments().getBoolean(SAVED_SUBTITLE_VISIBLE);

        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);

        mLinearLayout=(LinearLayout)view.findViewById(R.id.blank_crime_list);

        mNewCrimeButton=(Button)view.findViewById(R.id.button_new_crime);
        mNewCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewCrime();
            }
        });

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
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem item=menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible)
            item.setTitle(R.string.hide_subtitle);
        else
            item.setTitle(R.string.show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_crime:
                createNewCrime();
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible=!mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    private void updateUI()
    {
        int count=CrimeLab.get(getActivity()).getCrimes().size();
        if (count==0)
        {
            mLinearLayout.setVisibility(View.VISIBLE);
            mCrimeRecyclerView.setVisibility(View.INVISIBLE);
        }
        else
        {
            mLinearLayout.setVisibility(View.INVISIBLE);
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
        }

        if(mAdapter==null)
        {
            mAdapter=new CrimeAdapter(CrimeLab.get(getActivity()).getCrimes());
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else
            mAdapter.notifyDataSetChanged();

        updateSubtitle();
    }

    private void updateSubtitle()
    {
        String subtitle=null;
        if(mSubtitleVisible)
        {
            int count=CrimeLab.get(getActivity()).getCrimes().size();
            subtitle=getResources().getQuantityString(R.plurals.subtitle_format,count,count);
        }

        AppCompatActivity activity=(AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
    
    private void createCrimePagerActivity(UUID uuid) {
        Intent intent= CrimePagerActivity.newIntent(getActivity(),uuid);
        intent.putExtra(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
        startActivity(intent);
    }


    private void createNewCrime() {
        Crime crime=new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        createCrimePagerActivity(crime.getId());
    }


}
