package com.example.eventapp;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EventMain implements Serializable {
    public static ArrayList<Event> events; // Declaring an arraylist of objects of the class Event called events

    public EventMain() {
        events = new ArrayList<>(); //constructing the arraylist
        //Next, add 2 events to events arraylist.
        events.add(new Event("Test", "Waterford","SETU", "Talk", "Everybody", (double)25.00, LocalDate.parse("14-05-2024", DateTimeFormatter.ofPattern("dd-MM-yyyy")),"20.00","22010001.jpg" ,21010001));
        events.add(new Event("Nature's Gift", "Wexford", "KingsHall", "Conference", "Long Awaited", (double)10.00, LocalDate.parse( "11-02-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy")),"14.00", "22010002.jpg" ,21010002));
    }

    //Method which returns whole arraylist of all events.
    public ArrayList<Event> listAllEvents() {
        return events;
    }

    //Method to return an arraylist of all logged organiser's events. It goes through each event in the arraylist and checks that the event's organiser is equal to the logged-in user.
    //It also adds creates and adds a personalised welcoming event object to the arraylist returned.
    public  ArrayList<Event> listMyEvents(){
        ArrayList<Event> s = new ArrayList<>();
        Event welcome = new Event("Welcome " + EventApp.getLoggedInUser() + "!", "EventApp","Right here.", "Successful Registration", "You are now one of our event organisers. We are happy to have you.", (double)0.00,"20.00","welcome.jpg"); //EventApp.getLoggedInUser() is the name of the logged-in user so it offers some personalisation.
        s.add(welcome); // adds welcome event to arraylist to be returned
        for (Event e: events) {
            //only add events whose organiser id is the same as the logged-in organiser's id. We use organiser id because it is unique.
            if(e.getOrganiserId() == EventApp.getLoggedInUserId())
                s.add(e);
        }
        return s; //return arraylist of all logged-in organiser's events.
    }


    //A method to add a new event to events arraylist. Returns void.
    //It first checks if the arraylists does not already contain said event, in which case it adds the event to the arraylist.
    public void addEvent(Event e) {
        if (!events.contains(e)) {
            events.add(e);
            System.out.print("Event Created");
        }
        else System.out.println("Event already exists");
    }


    public void deleteEvent(Event e) {
      events.remove(e);
        try{
            EventApp.getEventMain().save(events);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }


    //Methods that searches through the whole arraylist of all events and returns the event whose id matches the id provided as parameter
    public Event searchEventByID(int id) {
        Event p = null;
        for (Event event : events) {
            if (event.getId() == id)
                p = event;
        }
        return p;
    }

    //Methods that searches through an arraylist of organiser's events and returns the event whose id matches the id provided as parameter
    public Event searchMyEventByID(int id) {
        Event p = null;
        for (Event event : listMyEvents()) {
            if (event.getId() == id)
                p = event;
        }
        return p;
    }

    //Method to update an event.
    public void updateEventDetails(int eventId, String name, String location, String venue, String type, String description, double price, LocalDate date, String time, String image) { //To complete
        Event e = searchEventByID(eventId);
        e.setEName(name);
        e.setELocation(location);
        e.setEVenue(venue);
        e.setEType(type);
        e.setEDescription(description);
        e.setECost(price);
        e.setDate(date);
        e.setTime(time);
        e.setImageName(image);
    }

    //This method the gets locations of all events and returns them as an arraylist
    public ArrayList<String> getELocations(){
        ArrayList<String> s = new ArrayList<>();
        s.add("All");
        for (Event e : events) {
           s.add(e.getELocation());
        }
        return s;
    }

    //This method gets the locations of all the logged in organiser's events and returns them as an arraylist
    public ArrayList<String> getMyELocations(){
        ArrayList<String> s = new ArrayList<>();
        s.add("All");
        for (Event e : listMyEvents()) {//moving through each object of the organiser's arraylist of event's
            s.add(e.getELocation());
        }
        return s;
    }

    //This method the gets types of all events and returns them as an arraylist
    public ArrayList<String> getETypes(){
        ArrayList<String> s = new ArrayList<>();
        s.add("All");
        for (Event e : EventMain.events) {
            s.add(e.getEType());
        }
        return s;
    }

    //This method gets the type of all the logged in organiser's events and returns them as an arraylist
    public ArrayList<String> getMyETypes(){
        ArrayList<String> s = new ArrayList<>();
        s.add("All");
        for (Event e : listMyEvents()) {//moving through each object of the organiser's arraylist of event's
            s.add(e.getEType());
        }
        return s;
    }

    //This method searches for events whose location is the same as that passed as a parameter, and returns them as an arraylist of Events
    public ArrayList<Event> getEventsByLocation(String location){
        ArrayList<Event> s = new ArrayList<>();
        for (Event e: events) {
            if(location.equals(e.getELocation())){
                s.add(e);
            }
        }
        return s;
    }

    //This method searches for organiser's events whose location is the same as that passed as a parameter, and returns them as an arraylist of Events
    public ArrayList<Event> getMyEventsByLocation(String location){
        ArrayList<Event> s = new ArrayList<>();
        for (Event e: listMyEvents()) {
            if(location.equals(e.getELocation())){
                s.add(e);
            }
        }
        return s;
    }

    ////This method searches for the events whose type is the same as that passed as a parameter, and returns them as an arraylist of Events
    public ArrayList<Event> getEventsByType(String type){
        ArrayList<Event> s = new ArrayList<>();
        for (Event e: events) {
            if(type.equals(e.getEType())){
                s.add(e);
            }
        }
        return s;
    }

    //This method searches for organiser's events whose location is the same as that passed as a parameter, and returns them as an arraylist of Events
    public ArrayList<Event> getMyEventsByType(String type){
        ArrayList<Event> s = new ArrayList<>();
        for (Event e: listMyEvents()) {
            if(type.equals(e.getEType())){
                s.add(e);
            }
        }
        return s;
    }

    //This method searches for events whose location is the same as that passed as a parameter, and returns them as an arraylist of Events
    public ArrayList<Event> getEvents(String type, String location){
        ArrayList<Event> s = new ArrayList<>();
        for (Event e: events) {
            if(type.equals(e.getEType()) && location.equals(e.getELocation())){
                s.add(e);
            }
        }
        return s;
    }

    //This method saves the object of the super class Object o passed as a parameter in a file called events.ser. Will use it to store my arraylist of events, EventMain.events.
    public void save(Object o) throws  FileNotFoundException {
        try {
            FileOutputStream writeData = new FileOutputStream("events.ser");
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            writeStream.writeObject(o);
            writeStream.flush();
            writeStream.close();

        } catch (IOException e) {
            System.out.println("Error in updating file records");
        }
    }

    //This method reads events.ser file and returns output as an Arraylist of events. It's to note that it is the arraylist of events that is stored in said file. So we are retrieving our arraylist of e vents record.
    public ArrayList<Event> read() throws FileNotFoundException{
        ArrayList<Event> eventsArrayList = null;
        try{
            FileInputStream readData = new FileInputStream("events.ser");
            ObjectInputStream readStream = new ObjectInputStream(readData);

            eventsArrayList = (ArrayList<Event>) readStream.readObject(); //cast output as an arraylists of events just to make sure there are no errors
            readStream.close();//closing the output stream
            System.out.println(eventsArrayList.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
       return eventsArrayList; //returns arraylist of events retrieved from the events.ser file.
    }
}
