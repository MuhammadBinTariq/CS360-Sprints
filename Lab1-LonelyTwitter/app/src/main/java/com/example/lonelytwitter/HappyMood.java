package com.example.lonelytwitter;

import java.util.Date;

public class HappyMood extends Mood
{
    public HappyMood(){}    // automatically calls super()

    public HappyMood(Date date)
    {
        super(date);
    }

    @Override
    public String getMood()
    {
        return "Happy";
    }
}
