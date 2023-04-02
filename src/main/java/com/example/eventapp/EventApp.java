
package com.example.eventapp;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings("ALL")
public class EventApp extends Application implements Serializable{
    private static HashSet<String> emails; // Storing user's emails in set. For checks during login
    private static HashMap<String, Organiser> organiserDetailsMap;//Storing organiser's details in hashmap with the organiser's email as the key.
    private static EventMain eventMain;
    private static String loggedInUser; //Making the logged-in organiser's name available throughout the program to allow customisation
    private static  int loggedInUserId; //Making the logged-in organiser's id available throughout the program to allow customisation and checks

    @Override
    public void start(Stage stage) throws IOException {
        eventMain = new EventMain();
        emails = new HashSet<>();
        organiserDetailsMap = new HashMap<>();
        //hard-code addition of 2 organisers in hashmap using hardcode constructor
        Organiser org = new Organiser(21010001,"Ursula", "ursula@me.ie","eventapp","0891111111");
        organiserDetailsMap.put("ursula@me.ie",org);
        Organiser org2 = new Organiser(21010002,"Ursula Tamen", "ursu@me.ie","12345678","0891111112");
        organiserDetailsMap.put("ursu@me.ie",org2);
        //Adding the 2 organisers' emails to the emails hashSet
        emails.add("ursula@me.ie");
        emails.add("ursu@me.ie");
        //retrieving arraylist of all events from its file and assigning it to EventMain.events arraylist
        getEventMain().events = getEventMain().read();
        //retrieving hashSet of emails from its file.
        readEmails();
        //retrieving hashmap of organisers of all events from its file.
        readOrganiserMap();
        FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("MainScene.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 700.0, 600.0);
        stage.setTitle("EventApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static HashSet<String> getEmails() {
        return emails;
    }

    public static void setEmails(HashSet<String> emails) {
        EventApp.emails = emails;
    }

    public static HashMap<String, Organiser> getOrganiserDetailsMap() {
        return organiserDetailsMap;
    }

    public static void setOrganiserDetailsMap(HashMap<String, Organiser> organiserDetailsMap) {
        EventApp.organiserDetailsMap = organiserDetailsMap;
    }

    private static void initialData(){

    }
    public static EventMain getEventMain() {
        return eventMain;
    }

    public static void setEventMain(EventMain eventMain) {
        EventApp.eventMain = eventMain;
    }

    public static String getLoggedInUser() {return loggedInUser;}

    public static void setLoggedInUser(String loggedInUser) {
        EventApp.loggedInUser = loggedInUser;
    }
    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    public static void setLoggedInUserId(int loggedInUserId) {
        EventApp.loggedInUserId = loggedInUserId;
    }

    //Method which returns Organiser object with organiser id equal to that passed as parameter. Used for searches.
    public static Organiser getOrganiser(int id){
        ArrayList<Organiser> organisers = new ArrayList<Organiser>(EventApp.getOrganiserDetailsMap().values());// assigns all the values of the organiser hashMap to the variable as an arraylist of organisers
        Organiser org = null;
        for (Organiser o:organisers) {
            if(o.getOId()==id)
                org = o;
        }
        return org;
    }

    //Saves object of the Object superclass passed as first parameter into the file passed a second parameter.(emails.ser for emails hashset and organisersMap.ser for organiserh hashmap).
    static void save(Object o, String fileName) throws FileNotFoundException {//From stackoverflow
        try {
            FileOutputStream writeData = new FileOutputStream(fileName);
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            writeStream.writeObject(o);
            writeStream.flush();
            writeStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Retrieves the emails hash set from its file, emails.ser and assigns it to  EventApp.emails
    private static void readEmails() throws FileNotFoundException{
        HashSet<String> emailsHashSet = null;
        try{
            FileInputStream readData = new FileInputStream("emails.ser");
            ObjectInputStream readStream = new ObjectInputStream(readData);

            emailsHashSet= (HashSet<String>) readStream.readObject();
            readStream.close();
            System.out.println(emailsHashSet.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        EventApp.emails = emailsHashSet;
    }

    //Retrieves the organisers' hashmap from its file, organisersmap.ser and assigns it to EventApp.organiserDetailsMap
    private static void readOrganiserMap() throws FileNotFoundException{
        HashMap<String, Organiser> orgMap = null;
        try{
            FileInputStream readData = new FileInputStream("organisersMap.ser");
            ObjectInputStream readStream = new ObjectInputStream(readData);

            orgMap= (HashMap<String, Organiser>) readStream.readObject();
            readStream.close();
            System.out.println(orgMap.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        EventApp.organiserDetailsMap = orgMap;
    }

}
