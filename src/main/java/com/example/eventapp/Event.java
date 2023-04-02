package com.example.eventapp;


import java.io.Serializable;
import java.time.LocalDate;

public class Event implements Serializable { //Implementation of serializable interface here and other classes is to permit serialisation of objects of these classes
    private static int next_eId =22010001; //Base count for event Id
    private int id;
    private String eName;
    private String eLocation;
    private String eVenue;
    private String eType;
    private String eDescription;
    private double eCost;
    private LocalDate date;
    private String time;

    private String imageName;
    private String toCome; /* true if event has not yet taken place ie if event's date has not passed  yet*/
    private int organiserId;

    //Event constructor for non-hardcoded events.
    //Event is associated with the organiser's id.
    public Event(String eName, String eLocation, String eVenue, String eType, String eDescription, double eCost, LocalDate date, String time, String imageName) {
        this.id = next_eId++;
        this.eName = eName;
        this.eLocation = eLocation;
        this.eVenue = eVenue;
        this.eType = eType;
        this.eDescription = eDescription;
        this.eCost = eCost;
        this.date = date;
        this.time = time;
        this.toCome = "Coming";
        this.imageName = imageName;
        this.organiserId = EventApp.getLoggedInUserId(); //getting logged organiser's id that is id of the organiser who created the event. It is declared as a static variable in EventApp class. Chose organiser's id because it ia a uni
    }

    //Event constructor for hard-coded events.
    //Explicitly tied to existing organiser's id.
    public Event(String eName, String eLocation, String eVenue, String eType, String eDescription, double eCost, LocalDate date, String time, String imageName, int organiserId) {
        this.id = next_eId++;
        this.eName = eName;
        this.eLocation = eLocation;
        this.eVenue = eVenue;
        this.eType = eType;
        this.eDescription = eDescription;
        this.eCost = eCost;
        this.date = date;
        this.toCome = "Coming";
        this.imageName = imageName;
        this.organiserId = organiserId;
        this.time = time;
    }

    //Event constructor for default welcome event for organiser when they register or are added
    //Has fixed id of 22010000 and default welcome.jpg as imageName.
    //It is not explicitly associated with the organiser's id.
    //Event date is the date the organiser registered or was added gotten from Organiser class.
    public Event(String eName, String eLocation, String eVenue, String eType, String eDescription, double eCost, String time, String imageName) {
        this.id = 22010000;
        this.eName = eName;
        this.eLocation = eLocation;
        this.eVenue = eVenue;
        this.eType = eType;
        this.eDescription = eDescription;
        this.eCost = eCost;
        this.date = Organiser.getDateRegistered();
        this.time = time;
        this.imageName = imageName;
    }

    //public static int getNext_eId() {return next_eId;} never used

    //public static void setNext_eId(int eventId) {Event.next_eId = eventId;} never used

    //Method to return event's id
    public int getId() {
        return id;
    }

    //Method to set event's id.
    public void setId(int id) {
        this.id = id;
    }

    //Method to get event's name/title.
    public String getEName() {
        return eName;
    }

    //Method to set event's name/title
    public void setEName(String eName) {
        this.eName = eName;
    }

    //Method to get event's location
    public String getELocation() {return eLocation;}

    //Method to set event's location.
    public void setELocation(String eLocation) {
        this.eLocation = eLocation;
    }

    //Method to get event's Venue.
    public String getEVenue() {
        return eVenue;
    }

    //Method to set event's venue.
    public void setEVenue(String eVenue) {
        this.eVenue = eVenue;
    }

    //Method to get event's type.
    public String getEType() { return eType;}

    //Method to set event's type.
    public void setEType(String eType) {
        this.eType = eType;
    }

    //Method to get event's location description.
    public String getEDescription() {
        return eDescription;
    }

    //Method to set event's description.
    public void setEDescription(String eDescription) {
        this.eDescription = eDescription;
    }

    //Method to get event's cost/fee.
    public double getECost() {
        return eCost;
    }

    //Method to set event's cost/fee.
    public void setECost(double eCost) {
        this.eCost = eCost;
    }

    //Method to get event's date.
    public LocalDate getDate() {
        return date;
    }

    //Method to set event's date.
    public void setDate(LocalDate date) {
        this.date = date;
    }

    //Method to determine is event's date has passed or is till to come.
    //The method compares today's (present) to event's date and returns a boolean: true if event's date has not yet passed and false otherwise.
    public boolean isToCome() {
        LocalDate today = LocalDate.now(); // assigning present date (from LocalDate.now()) to variable today.
        int t = today.compareTo(getDate()); //compareTo() method returns >0 today's date is after event's date (getDate()), 0 if today's day is the same as event's date, and <0 if today's date is before event's date.
        return t <= 0;
    }

    //Method to set of toComing. If event is to come or already passed.
    public void setToCome(String toCome) {
        this.toCome = toCome;
    }

    //Method to get event's organiser's id.
    public int getOrganiserId() {return organiserId;}

    //public void setOrganiserId(int organiser) {this.organiserId = organiser;} never used

    //Method to get event's time.
    public String getTime() {return time;}

    //Method to set event's time.
    public void setTime(String time) {this.time = time;}

    //Method to get event's imageName(name of event's image stored in programme's img subfolder in the src folder).
    public String getImageName() {
        return imageName;
    }

    //Method to set event's imageName(name of event's image stored in programme's img subfolder in the src folder).
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    //override the superclass' (Event's) toString method to determine presentation of event data in String form.
    @Override
    public String toString() {
        setToCome(isToCome()? "Coming!" : "Event closed" ); //Set's toCome variable as "coming!" if event's date has not yet passed  that isToCome() is true, and set's it to "Event closed" otherwise.
        return  "Event Id:  " + id + "\n\n" +
                "Title:  " + eName + "\n\n" +
                "Description:  " + eDescription + "\n\n" +
                "Type:  " + eType + "\n\n" +
                "Location:  " + eLocation + "\n\n" +
                "Venue:  " + eVenue + "\n\n" +
                "Participation Fee:  " + eCost + "\n\n"  +
                "Date:  " + date +"\n\n" +
                "Time:  " + time + "\n\n" +
                "Status:   " + toCome + "\n" ;
    }

}

