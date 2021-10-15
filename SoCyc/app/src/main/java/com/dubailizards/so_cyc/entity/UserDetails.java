package com.dubailizards.so_cyc.entity;

import android.util.Log;

public class UserDetails {
    String firstName;
    String lastName;
    String country;
    String state;

    public UserDetails()
    {
    }

    public UserDetails(String firstName,
            String lastName,
            String country,
            String state)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.state = state;
    }

    // Getters

    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public String getCountry()
    {
        return country;
    }
    public String getState()
    {
        return state;
    }


// Setters
    public void setFirstName(String _firstName)
    {
        firstName = _firstName;
    }
    public void setLastName(String _lastName)
    {
        lastName = _lastName;
    }
    public void setCountry(String _country)
    {
        country = _country;
    }
    public void setState(String _state)
    {
        state = _state;
    }

    /**
     * A debug function.
     * prints all the class attributes
     */
    public void PrintAll()
    {
        Log.d("UserDetails","firstName: " + firstName);
        Log.d("UserDetails","lastName: " + lastName);
        Log.d("UserDetails","country: " + country);
        Log.d("UserDetails","state: " + state);
    }
}
