package com.example.lonelytwitter;

import java.util.Date;

public class ImportantTweet extends Tweet
{
    public ImportantTweet(String message)
    {
        super(message);
    }

    public ImportantTweet(Date date, String message)
    {
        super(date, message);
    }

    @Override
    public Boolean isImportant()
    {
        return Boolean.TRUE;
    }
}
