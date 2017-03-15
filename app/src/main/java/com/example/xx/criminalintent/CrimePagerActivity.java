package com.example.xx.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.net.ConnectException;
import java.util.List;
import java.util.UUID;

/**
 * Created by XX on 2017/3/12.
 */

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    private static final String EXTRA_CRIME_ID="com.example.xx.criminalintent.crime_id";

    public static Intent newIntent(Context context,UUID uuid)
    {
        Intent intent=new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,uuid);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mCrimes=CrimeLab.get(this).getCrimes();

        mViewPager=(ViewPager)findViewById(R.id.activity_crime_pager_view_pagers);
        FragmentManager fm=getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime=mCrimes.get(position);
                return  CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        UUID uuid=(UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        for (int i=0;i<mCrimes.size();i++)
        {
            if(mCrimes.get(i).getId().equals(uuid))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        boolean visible=getIntent().getBooleanExtra(CrimeListFragment.SAVED_SUBTITLE_VISIBLE,false);
        Intent intent=new Intent(this,CrimeListActivity.class);
        intent.putExtra(CrimeListFragment.SAVED_SUBTITLE_VISIBLE,visible);
        return intent;
    }
}
