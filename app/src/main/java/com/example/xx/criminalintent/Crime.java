package com.example.xx.criminalintent;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by XX on 2017/3/7.
 */

public class Crime {
    private UUID mId;
    private String mTitle;

    public Crime()
    {
        mId=UUID.randomUUID();
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
}
