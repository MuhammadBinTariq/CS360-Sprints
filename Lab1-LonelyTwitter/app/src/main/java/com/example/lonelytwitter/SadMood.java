package com.example.lonelytwitter;

import java.util.Date;

public class SadMood extends Mood
{
    public SadMood() {}     // automatically calls super()

    public SadMood(Date date)
    {
        super(date);
    }

    @Override
    public String getMood() {
        return "Sad";
    }
}
