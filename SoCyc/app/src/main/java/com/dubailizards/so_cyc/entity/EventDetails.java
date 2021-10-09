package com.dubailizards.so_cyc.entity;

public class EventDetails {
    String eventPictureURL;
    String eventTitle;
    String eventAddress;
    String eventDescription;
    int eventHostID;
    int eventDate;
    int eventStartTime;
    int eventEndTime;
    int eventID;

    public EventDetails(String eventPictureURL,
            String eventTitle,
            String eventAddress,
            String eventDescription,
            int eventHostID,
            int eventDate,
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
    public String GetEventPictureURL()
    {
        return eventPictureURL;
    }
    public String GetEventTitle()
    {
        return eventTitle;
    }
    public String GetEventAddress()
    {
        return eventAddress;
    }
    public String GetEventDescription()
    {
        return eventDescription;
    }
    public int GetEventDate()
    {
        return eventDate;
    }
    public int GetEventHostID()
    {
        return eventHostID;
    }
    public int GetEventStartTime()
    {
        return eventStartTime;
    }
    public int GetEventEndTime()
    {
        return eventEndTime;
    }
    public int GetEventID()
    {
        return eventID;
    }

    //Setters
    public void SetEventPictureURL(String url)
    {
        eventPictureURL = url;
    }
    public void SetEventTitle(String title)
    {
        eventTitle = title;
    }
    public void SetEventAddress(String address)
    {
        eventAddress = address;
    }
    public void SetEventHostID(int id)
    {
        eventHostID = id;
    }
    public void SetEventDate(int date)
    {
        eventDate = date;
    }
    public void SetEventDescription(String description)
    {
        eventDescription = description;
    }
    public void SetEventStartTime(int time)
    {
        eventStartTime = time;
    }
    public void SetEventEndTime(int time)
    {
        eventEndTime = time;
    }
    public void SetEventID(int id)
    {
        eventID = id;
    }
}
