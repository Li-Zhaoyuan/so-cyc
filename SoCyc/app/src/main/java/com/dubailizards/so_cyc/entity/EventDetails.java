package com.dubailizards.so_cyc.entity;

import android.util.Log;

public class EventDetails {
    String eventHostID;
    String hostDisplayName;
    String profilePictureURL;
    String eventTitle;
    String eventAddress;
    String eventDescription;
    String eventDate;
    String eventStartTime;
    String eventEndTime;

    public EventDetails() { }

    public EventDetails(String profilePictureURL,
            String eventTitle,
            String eventAddress,
            String eventDescription,
            String eventHostID,
            String eventDate,
            String eventStartTime,
            String eventEndTime,
            String hostDisplayName)
    {
        this.profilePictureURL = profilePictureURL;
        this.eventTitle = eventTitle;
        this.eventAddress = eventAddress;
        this.eventDescription = eventDescription;
        this.eventHostID = eventHostID;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.hostDisplayName = hostDisplayName;
    }

    //Getters
    public String getEventHostID()
    {
        return eventHostID;
    }
    public String getHostDisplayName()
    {
        return hostDisplayName;
    }
    public String getProfilePictureURL()
    {
        return profilePictureURL;
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
    public String getEventStartTime()
    {
        return eventStartTime;
    }
    public String getEventEndTime()
    {
        return eventEndTime;
    }

    //Setters
    public void setEventHostID(String id)
    {
        eventHostID = id;
    }
    public void setHostDisplayName(String name)
    {
        hostDisplayName = name;
    }
    public void setProfilePictureURL(String url)
    {
        profilePictureURL = url;
    }
    public void setEventTitle(String title)
    {
        eventTitle = title;
    }
    public void setEventAddress(String address)
    {
        eventAddress = address;
    }
    public void setEventDate(String date)
    {
        eventDate = date;
    }
    public void setEventDescription(String description)
    {
        eventDescription = description;
    }
    public void setEventStartTime(String time)
    {
        eventStartTime = time;
    }
    public void setEventEndTime(String time)
    {
        eventEndTime = time;
    }

    /**
     * A public boolean function
     * On call, checks validity of the EventDetail object by making sure all fields are set
     */
    public boolean CheckValidity(){
        // If any are null return false
        if (eventHostID == null || eventHostID.isEmpty())
            return false;
        if (hostDisplayName == null || hostDisplayName.isEmpty())
            return false;
        if (profilePictureURL == null || profilePictureURL.isEmpty())
            return false;
        if (eventTitle == null || eventTitle.isEmpty())
            return false;
        if (eventAddress == null || eventAddress.isEmpty())
            return false;
        if (eventDate == null || eventDate.isEmpty())
            return false;
        if (eventDescription == null || eventDescription.isEmpty())
            return false;
        if (eventStartTime == null || eventStartTime.isEmpty())
            return false;
        if (eventEndTime == null || eventEndTime.isEmpty())
            return false;
        return true;
    }

    /**
     * A debug function.
     * prints all the class attributes
     */
    public void PrintAll()
    {
        Log.d("EventDetails","eventHostID: " + eventHostID);
        Log.d("EventDetails","hostDisplayName: " + hostDisplayName);
        Log.d("EventDetails","eventPictureURL: " + profilePictureURL);
        Log.d("EventDetails","eventTitle: " + eventTitle);
        Log.d("EventDetails","eventAddress: " + eventAddress);
        Log.d("EventDetails","eventHostID: " + eventHostID);
        Log.d("EventDetails","eventDate: " + eventDate);
        Log.d("EventDetails","eventDescription: " + eventDescription);
        Log.d("EventDetails","eventStartTime: " + eventStartTime);
        Log.d("EventDetails","eventEndTime: " + eventEndTime);
    }
}
