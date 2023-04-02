package com.example.eventapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @FXML
    private ComboBox<String> eLocation;

    @FXML
    private ComboBox<String> eventType;

    @FXML
    private ListView<Event> listView;


    //Loads the login scene.
    @FXML
    void clickLoginButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("LoginScene.fxml"));
            Scene scene = new Scene((Parent) fxmlLoader.load(), 700.0, 600.0);
            stage.setTitle("EventApp|Login");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Ayop");
        }
    }

    //redirects to register scene.
    @FXML
    void clickRegisterButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("RegisterScene.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700.0, 600.0);
            stage.setTitle("EventApp|Register");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Listens and response to combo box choice change for filtering
    @FXML
    void comboBoxChange(ActionEvent e) {
        System.out.println("Here" + EventMain.events.size());
        ArrayList<Event> s = new ArrayList<>();
        if (eLocation.getValue().equals("All") && eventType.getValue().equals("All")) {
            s = EventApp.getEventMain().listAllEvents();
        } else if (eLocation.getValue().equals("All") && !eventType.getValue().equals("All")) {
            s = EventApp.getEventMain().getEventsByType(eventType.getValue());
        } else if (!eLocation.getValue().equals("All") && eventType.getValue().equals("All")) {
            s = EventApp.getEventMain().getEventsByLocation(eLocation.getValue());
        } else if (!(eLocation.getValue().equals("All") || eventType.getValue().equals("All"))) {
            s = EventApp.getEventMain().getEvents(eventType.getValue(), eLocation.getValue());
        }
        ObservableList<Event> eventsList = FXCollections.observableArrayList(s);
        listView.setItems(eventsList);
    }

    //Initialises the properties and objects of the scene.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> eLocations = FXCollections.observableArrayList(EventApp.getEventMain().getELocations());
        eLocation.setItems(eLocations);
        eLocation.setValue("All");

        ObservableList<String> eventTypes = FXCollections.observableArrayList(EventApp.getEventMain().getETypes());
        eventType.setItems(eventTypes);
        eventType.setValue("All");

        //adding arraylist of events to listview
        ObservableList<Event> list = FXCollections.observableArrayList(EventApp.getEventMain().listAllEvents());
        listView.setItems(list);

        //Coding the listView to add images associated with each event in each cell.
        listView.setCellFactory(item -> new ListCell<>() {
            private ImageView imageView = new ImageView(); // creates image view for listView cell
            @Override
            public void updateItem(Event item, boolean empty) { //overriding super class' update item to suit my need.
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                }
                else {
                    InputStream is = null; //create and initialise input stream
                    String imgName = item.getImageName(); //get and assign event's imageName which serves as part of its image path
                    //If there is no image associated with the event display a default image
                    if (imgName == null)
                        try {
                            is = new FileInputStream("src/img/default.jpg");
                            Image image = new Image(is, 500, 350, false, true);
                            imageView.setImage(image);
                            setGraphic(imageView);
                            setText(item.toString());
                        } catch (FileNotFoundException m) {
                            m.printStackTrace();
                        }
                    //Else if event has an image associated with it
                    else try {
                        is = new FileInputStream("src/img/" + imgName); //This combination forms the image's path
                        Image image = new Image(is, 500, 350, false, true);//assigning image and set the image's properties.
                        //Add event's image to the cells imageView
                        imageView.setImage(image);
                        setGraphic(imageView);
                        //Set text as item's (event's) toString representation
                        setText(item.toString());
                        setFont(Font.font(14.0));
                    } catch (FileNotFoundException m) {
                        m.printStackTrace();
                    }
                }
            }
        });
    }

}