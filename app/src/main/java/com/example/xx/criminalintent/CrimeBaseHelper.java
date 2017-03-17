package com.example.xx.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.xx.criminalintent.database.CrimeDBScheme.*;

/**
 * Created by XX on 2017/3/17.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE_NAME="crimeBase.db";

    public CrimeBaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("create table ? ( ? , ? , ? , ? , ? )",
//                new String[]
//                        {
//                                CrimeTable.NAME,
//                                "_id integer primary key autoincrement",
//                                CrimeTable.Column.UUID,
//                                CrimeTable.Column.TITLE,
//                                CrimeTable.Column.DATE,
//                                CrimeTable.Column.SOLVED
//                        });
        sqLiteDatabase.execSQL("create table "+CrimeTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                CrimeTable.Column.UUID+", "+
                CrimeTable.Column.TITLE+", "+
                CrimeTable.Column.DATE+", "+
                CrimeTable.Column.SOLVED+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
