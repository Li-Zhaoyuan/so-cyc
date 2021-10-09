package com.dubailizards.so_cyc.entity;

public class UserDetails {
    String firstName;
    String lastName;
    String country;
    String state;

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

    public String GetFirstName()
    {
        return firstName;
    }
    public String GetLastName()
    {
        return lastName;
    }
    public String GetCountry()
    {
        return country;
    }

    public String GetState()
    {
        return state;
    }


// Setters
    public void SetFirstName(String _firstName)
    {
        firstName = _firstName;
    }
    public void SetLastName(String _lastName)
    {
        lastName = _lastName;
    }
    public void SetCountry(String _country)
    {
        country = _country;
    }
    public void SetState(String _state)
    {
        state = _state;
    }
}
