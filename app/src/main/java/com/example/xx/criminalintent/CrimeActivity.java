package com.example.xx.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragentActivity {

    private static final String EXTRA_CRIME_ID="com.example.xx.criminalintent.crime_id";

    public static Intent newIntent(Context context, UUID id)
    {
        Intent intent=new Intent(context,CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {

        UUID uuid=(UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(uuid);
    }
}
