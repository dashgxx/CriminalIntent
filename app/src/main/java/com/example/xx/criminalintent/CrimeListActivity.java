package com.example.xx.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by XX on 2017/3/9.
 */

public class CrimeListActivity extends SingleFragentActivity {
    @Override
    protected Fragment createFragment() {
        boolean visible=getIntent().getBooleanExtra(CrimeListFragment.SAVED_SUBTITLE_VISIBLE,false);
        return CrimeListFragment.newInstance(visible);
    }
}
