package com.dubailizards.so_cyc.entity;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    /**
     * A public Constructor
     * empty constructor
     */
    public EventDetails() { }

    /**
     * A public Constructor
     * Overloaded Constructor
     * @param profilePictureURL set profilePictureURL
     * @param eventTitle set eventTitle
     * @param eventAddress set eventAddress
     * @param eventDescription set eventDescription
     * @param eventHostID set eventHostID
     * @param eventDate set eventDate
     * @param eventStartTime set eventStartTime
     * @param eventEndTime set eventEndTime
     * @param hostDisplayName set hostDisplayName
     */
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
    /**
     * A public getter function
     * get eventHostID
     */
    public String getEventHostID()
    {
        return eventHostID;
    }
    /**
     * A public getter function
     * get hostDisplayName
     */
    public String getHostDisplayName()
    {
        return hostDisplayName;
    }
    /**
     * A public getter function
     * get profilePictureURL
     */
    public String getProfilePictureURL()
    {
        return profilePictureURL;
    }
    /**
     * A public getter function
     * get eventTitle
     */
    public String getEventTitle()
    {
        return eventTitle;
    }
    /**
     * A public getter function
     * get eventAddress
     */
    public String getEventAddress()
    {
        return eventAddress;
    }
    /**
     * A public getter function
     * get eventDescription
     */
    public String getEventDescription()
    {
        return eventDescription;
    }
    /**
     * A public getter function
     * get eventDate
     */
    public String getEventDate()
    {
        return eventDate;
    }
    /**
     * A public getter function
     * get eventStartTime
     */
    public String getEventStartTime()
    {
        return eventStartTime;
    }
    /**
     * A public getter function
     * get eventEndTime
     */
    public String getEventEndTime()
    {
        return eventEndTime;
    }

    //Setters
    /**
     * A public setter function
     * @param id set eventHostID
     */
    public void setEventHostID(String id)
    {
        eventHostID = id;
    }
    /**
     * A public setter function
     * @param name set hostDisplayName
     */
    public void setHostDisplayName(String name)
    {
        hostDisplayName = name;
    }
    /**
     * A public setter function
     * @param url set profilePictureURL
     */
    public void setProfilePictureURL(String url)
    {
        profilePictureURL = url;
    }
    /**
     * A public setter function
     * @param title set eventTitle
     */
    public void setEventTitle(String title)
    {
        eventTitle = title;
    }
    /**
     * A public setter function
     * @param address set eventAddress
     */
    public void setEventAddress(String address)
    {
        eventAddress = address;
    }
    /**
     * A public setter function
     * @param date set eventDate
     */
    public void setEventDate(String date)
    {
        eventDate = date;
    }
    /**
     * A public setter function
     * @param description set eventDescription
     */
    public void setEventDescription(String description)
    {
        eventDescription = description;
    }
    /**
     * A public setter function
     * @param time set eventStartTime
     */
    public void setEventStartTime(String time)
    {
        eventStartTime = time;
    }
    /**
     * A public setter function
     * @param time set eventEndTime
     */
    public void setEventEndTime(String time)
    {
        eventEndTime = time;
    }

    /**
     * A public boolean function
     * On call, checks validity of the EventDetail object by making sure all fields are set
     */
    public String CheckValidity(){
        // If any are null return false
        if (eventHostID == null || eventHostID.isEmpty())
            return "Please fill up all fields!";
        if (hostDisplayName == null || hostDisplayName.isEmpty())
            return "Please fill up all fields!";
        if (profilePictureURL == null || profilePictureURL.isEmpty())
            return "Please fill up all fields!";
        if (eventTitle == null || eventTitle.isEmpty())
            return "Please fill up all fields!";
        if (eventAddress == null || eventAddress.isEmpty())
            return "Please fill up all fields!";
        if (eventDate == null || eventDate.isEmpty())
            return "Please fill up all fields!";
        if (eventDescription == null || eventDescription.isEmpty())
            return "Please fill up all fields!";
        if (eventStartTime == null || eventStartTime.isEmpty())
            return "Please fill up all fields!";
        if (eventEndTime == null || eventEndTime.isEmpty())
            return "Please fill up all fields!";

        SimpleDateFormat sdfrmt = new SimpleDateFormat("ddMMyy");
        sdfrmt.setLenient(false);
        try {
            Date javaDate = sdfrmt.parse(eventDate);
        }catch (ParseException e)
        {
            return "Please fill up the date as DDMMYY!";
        }

        try {
            int startTime = Integer.parseInt(eventStartTime);
            int endTime = Integer.parseInt(eventEndTime);

            if (startTime > 2359 || endTime > 2359)
                return "Please fill up a Time from 0000 to 2359!";
            if (startTime % 100 > 59 || endTime % 100 > 59)
                return "Please fill up a Time with minutes between 0 to 59!";
        }
        catch(NumberFormatException e)
        {
            return "Please fill up a Time from 0000 to 2359!";
        }
        return "";
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
