package com.dubailizards.so_cyc.entity;

import android.util.Log;

public class EventDetails {
    String eventPictureURL;
    String eventTitle;
    String eventAddress;
    String eventDescription;
    int eventHostID;
    String eventDate;
    int eventStartTime;
    int eventEndTime;
    int eventID;

    public EventDetails()
    {
    }

    public EventDetails(String eventPictureURL,
            String eventTitle,
            String eventAddress,
            String eventDescription,
            int eventHostID,
            String eventDate,
            int eventStartTime,
            int eventEndTime,
            int eventID)
    {
        this.eventPictureURL = eventPictureURL;
        this.eventTitle = eventTitle;
        this.eventAddress = eventAddress;
        this.eventDescription = eventDescription;
        this.eventHostID = eventHostID;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventID = eventID;
    }

    //Getters
    public String getEventPictureURL()
    {
        return eventPictureURL;
    }
    public String getEventTitle()
    {
        return eventTitle;
    }
    public String getEventAddress()
    {
        return eventAddress;
    }
    public String getEventDescription()
    {
        return eventDescription;
    }
    public String getEventDate()
    {
        return eventDate;
    }
    public int getEventHostID()
    {
        return eventHostID;
    }
    public int getEventStartTime()
    {
        return eventStartTime;
    }
    public int getEventEndTime()
    {
        return eventEndTime;
    }
    public int getEventID()
    {
        return eventID;
    }

    //Setters
    public void setEventPictureURL(String url)
    {
        eventPictureURL = url;
    }
    public void setEventTitle(String title)
    {
        eventTitle = title;
    }
    public void setEventAddress(String address)
    {
        eventAddress = address;
    }
    public void setEventHostID(int id)
    {
        eventHostID = id;
    }
    public void setEventDate(String date)
    {
        eventDate = date;
    }
    public void setEventDescription(String description)
    {
        eventDescription = description;
    }
    public void setEventStartTime(int time)
    {
        eventStartTime = time;
    }
    public void setEventEndTime(int time)
    {
        eventEndTime = time;
    }
    public void setEventID(int id)
    {
        eventID = id;
    }

    /**
     * A debug function.
     * prints all the class attributes
     */
    public void PrintAll()
    {
        Log.d("EventDetails","eventPictureURL: " + eventPictureURL);
        Log.d("EventDetails","eventTitle: " + eventTitle);
        Log.d("EventDetails","eventAddress: " + eventAddress);
        Log.d("EventDetails","eventHostID: " + eventHostID);
        Log.d("EventDetails","eventDate: " + eventDate);
        Log.d("EventDetails","eventDescription: " + eventDescription);
        Log.d("EventDetails","eventStartTime: " + eventStartTime);
        Log.d("EventDetails","eventEndTime: " + eventEndTime);
        Log.d("EventDetails","eventID: " + eventID);
    }

}
