package com.example.xx.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by XX on 2017/3/9.
 */

public class CrimeListActivity extends SingleFragentActivity
    implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks
{
    @Override
    protected Fragment createFragment() {
        boolean visible=getIntent().getBooleanExtra(CrimeListFragment.SAVED_SUBTITLE_VISIBLE,false);
        return CrimeListFragment.newInstance(visible);
    }

    @Override
    protected int getResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime,boolean visible) {
        if (findViewById(R.id.detail_fragment_container)==null)
        {
            Intent intent=
                    CrimePagerActivity.newIntent(this,crime.getId(),visible);
            startActivity(intent);
        }
        else
        {
            CrimeFragment crimeFragment=CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container,crimeFragment)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment
                =(CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI(crime);
    }

    @Override
    public void onCrimeDeleted() {
        CrimeListFragment crimeListFragment
                =(CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction()
                .remove(fm.findFragmentById(R.id.detail_fragment_container))
                .commit();
    }
}
