package com.example.xx.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import static com.example.xx.criminalintent.database.CrimeDBScheme.*;

/**
 * Created by XX on 2017/3/17.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime()
    {
        String uuidString = getString(getColumnIndex(CrimeTable.Column.UUID));
        String title = getString(getColumnIndex(CrimeTable.Column.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Column.DATE));
        int solved = getInt(getColumnIndex(CrimeTable.Column.SOLVED));
        String suspect=getString(getColumnIndex(CrimeTable.Column.SUSPECT));

        Crime crime=new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(solved==1);
        crime.setSuspect(suspect);

        return crime;
    }
}
