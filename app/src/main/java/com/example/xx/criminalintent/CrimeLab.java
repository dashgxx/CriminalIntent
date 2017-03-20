package com.example.xx.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.xx.criminalintent.database.CrimeDBScheme.*;

/**
 * Created by XX on 2017/3/9.
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    //private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public  static CrimeLab get(Context context)
    {
        if(sCrimeLab==null)
            sCrimeLab=new CrimeLab(context);
        return  sCrimeLab;
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes=new ArrayList<>();
        CrimeCursorWrapper cursorWrapper=queryCrimes(null,null);

        try {
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast())
            {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return crimes;
    }

    public void addCrime(Crime crime)
    {
        ContentValues contentValues=getContentValues(crime);

        mDatabase.insert(CrimeTable.NAME,null,contentValues);
    }

    public void deleteCrime(UUID id)
    {
        mDatabase.delete(CrimeTable.NAME, CrimeTable.Column.UUID+" = ?", new String[]{id.toString()});
    }

    public void updateCrime(Crime crime)
    {
        String uuidString = crime.getId().toString();
        ContentValues values=getContentValues(crime);
        mDatabase.update(CrimeTable.NAME,values,CrimeTable.Column.UUID+ " = ?",new String[]{uuidString});
    }

    public Crime getCrime(UUID id)
    {
        CrimeCursorWrapper cursorWrapper=queryCrimes(
                CrimeTable.Column.UUID+" = ?",
                new String[]{id.toString()});

        try {
            if(cursorWrapper.getCount()==0)
                return null;

            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        }finally {
            cursorWrapper.close();
        }
    }

    public File getPhotoFile(Crime crime)
    {
        File dir=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(dir==null)
            return null;

        return new File(dir,crime.getPhotoFilename());
    }

    private CrimeLab(Context context)
    {
        mContext=context.getApplicationContext();
        mDatabase=new CrimeBaseHelper(mContext).getWritableDatabase();
        //mCrimes=new ArrayList<>();
    }

    private static ContentValues getContentValues(Crime crime)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(CrimeTable.Column.UUID,crime.getId().toString());
        contentValues.put(CrimeTable.Column.TITLE,crime.getTitle());
        contentValues.put(CrimeTable.Column.DATE,crime.getDate().getTime());
        contentValues.put(CrimeTable.Column.SOLVED,crime.isSolved()?1:0);
        contentValues.put(CrimeTable.Column.SUSPECT,crime.getSuspect());
        return contentValues;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs)
    {
        Cursor cursor=mDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }
}
