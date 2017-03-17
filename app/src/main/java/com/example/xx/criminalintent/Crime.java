package com.example.xx.criminalintent;

import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by XX on 2017/3/7.
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime()
    {
        mId=UUID.randomUUID();
        mDate=new Date();
    }

    public Crime(UUID id)
    {
        mId=id;
        mDate=new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    @Override
    public boolean equals(Object obj) {
        //return super.equals(obj);
        if (!(obj instanceof Crime))
            return false;
        Crime crime=(Crime)obj;
        return mId.equals(crime.getId());
    }
}
