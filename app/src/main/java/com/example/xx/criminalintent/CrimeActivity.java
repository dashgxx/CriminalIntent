package com.example.xx.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeActivity extends SingleFragentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
