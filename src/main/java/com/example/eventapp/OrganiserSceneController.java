package com.example.eventapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrganiserSceneController implements Initializable {

    @FXML
    private DatePicker datepicker;

    @FXML
    private ComboBox<String> eLocationComboBox;

    @FXML
    private ComboBox<String> eTypeComboBox;

    @FXML
    private TextField eventId;

    @FXML
    private ListView<Event> listView;

    @FXML
    private Button detailedViewButton;

    @FXML
    private Button deleteButton;

    private static boolean for_Next = true;

    public static boolean isFor_Next() {
        return for_Next;
    }

    public static void setFor_Next(boolean for_Next) {
        OrganiserSceneController.for_Next = for_Next;
    }

    //Redirects user to Main scene when logOut button is clicked
    @FXML
    void onLogOut(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("MainScene.fxml"));
            Scene scene = new Scene((Parent) fxmlLoader.load(), 700.0, 600.0);
            stage.setTitle("EventApp");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println("hey o!");
        }
    }

    //Method which returns selected event from listView
    @FXML
    Event onSelectedEvent(){
        this.deleteButton.setDisable(false);
        this.detailedViewButton.setDisable(false);
        return listView.getSelectionModel().getSelectedItem();
    }

    //Method which Redirects user to another scene where selected event's will be shown, when detailed view button is clicked
    @FXML
    void onDetailedViewButton(ActionEvent event) throws IOException {
        setFor_Next(false); // determines that create button in next scene will be disabled and update button will be enabled because I used the same scene for both actions
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Update_Create_Event_Scene.fxml"));
            //loader.setLocation(getClass().getResource("MainScene.fxml"));
            Parent listViewParent = loader.load();
            System.out.println("I'm on");
            Scene listViewScene = new Scene(listViewParent);

            //access the controller and call a method
            Update_Create_Event_SceneController controller = loader.getController();
            Event eventToDisplay = (Event) listView.getSelectionModel().getSelectedItem(); //cast as Event
            System.out.println("I'm on point 2");
            if(eventToDisplay == null) //sometimes no organiser will have been selected
                return;

            controller.initData(eventToDisplay);
            System.out.println("I'm on point 3");

            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(listViewScene);
            window.show();

        }
        catch(Exception e){
            System.out.println("OnDetailedView Error");
        }
    }

    //Method to search and display for events using either event's date or event's id
    @FXML
    void onSearch(ActionEvent event) throws NullPointerException {
        ArrayList<Event> s = new ArrayList<>();
        //Using only event id
        if(!eventId.getText().isBlank() && (datepicker.getValue() == null)){
            try {
                s.add(EventApp.getEventMain().searchMyEventByID((int) Integer.parseInt(eventId.getText())));
            }
            catch (NumberFormatException e) {
                System.out.println("Number Format error");
            }
        }
        //Using event date only
        else if (eventId.getText().isBlank() && (datepicker.getValue() != null)){
            LocalDate ld = datepicker.getValue();
            for (Event e: EventMain.events) {
                if(e.getDate().equals(ld))
                    if(e.getOrganiserId() == EventApp.getLoggedInUserId())
                        s.add(e);
            }
        }
        //Using both event's id and date
        else if(!eventId.getText().isBlank() && (datepicker.getValue() != null)){
            LocalDate ld = datepicker.getValue();
            Event e = EventApp.getEventMain().searchMyEventByID((int) Integer.parseInt(eventId.getText()));
            if(e.getDate().equals(ld))
                s.add(e);
        }
        ObservableList<Event> eventsList = FXCollections.observableArrayList(s);
        listView.setItems(eventsList);
    }

    //Redirects us to another scene to create events when button is clicked
    @FXML
    void onUpdate_Create(ActionEvent event) {
        setFor_Next(true); // determines that create button in next scene will be enabled and update button will be disabled because I used the same scene for both actions
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("Update_Create_Event_Scene.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 620, 440);
            stage.setTitle("EventApp|" + EventApp.getLoggedInUser() + "|Create Event"); //includes logged-in user's name in app's title for personalisation
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            System.out.println("me");
        }
    }

    //Listens and response to combo box choice change for filtering organiser's events
    @FXML
    void comboBoxChange(ActionEvent e){
        this.detailedViewButton.setDisable(true);
        System.out.println("Here"+ EventApp.getEventMain().listMyEvents().size());// Prints out how many events the logged-in organiser has
        ArrayList<Event> s = new ArrayList<>();
            if(eLocationComboBox.getValue().equals("All") && eTypeComboBox.getValue().equals("All")){
                s = EventApp.getEventMain().listMyEvents();
            }
            else if(eLocationComboBox.getValue().equals("All") && !eTypeComboBox.getValue().equals("All")){
                s = EventApp.getEventMain().getMyEventsByType(eTypeComboBox.getValue());
            }
            else if(!eLocationComboBox.getValue().equals("All") && eTypeComboBox.getValue().equals("All")){
                s = EventApp.getEventMain().getMyEventsByLocation(eLocationComboBox.getValue());
            }
            else if(!(eLocationComboBox.getValue().equals("All") || eTypeComboBox.getValue().equals("All"))){
                s = EventApp.getEventMain().getEvents(eTypeComboBox.getValue(), eLocationComboBox.getValue());
            }
            ObservableList<Event> eventsList = FXCollections.observableArrayList(s);
        listView.setItems(eventsList);
    }

    //Method to delete event and update events records.
    //Logged-in organiser can only delete events they created
    @FXML
    void onClickDelete(ActionEvent event) throws NullPointerException {
        if (onSelectedEvent().getOrganiserId() == EventApp.getLoggedInUserId()) {//only if it is the organiser who created the event
            //Pop up warning for deletion
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            Label lb = new Label("Are you sure you want to delete this event?");
            Button yesButton = new Button("Yes");
            Button noButton = new Button("No");
            //dialogVbox.getChildren().add(new Text("This is a Dialog"));
            dialogVbox.getChildren().addAll(lb, yesButton, noButton);
            dialogVbox.setAlignment(Pos.CENTER);
            Scene dialogScene = new Scene(dialogVbox, 300, 110);
            stage.setScene(dialogScene);
            stage.show();
            //If the user confirms deletion by clicking on yes, the selected event is deleted.
            yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    stage.close();
                    Event s = (Event) listView.getSelectionModel().getSelectedItem();
                    EventApp.getEventMain().deleteEvent(s);
                    ObservableList<Event> eventsList = FXCollections.observableArrayList(EventApp.getEventMain().listAllEvents());
                    listView.setItems(eventsList); //update listView
                }
            });
            noButton.setOnMouseClicked(e -> stage.close());
        }
        //If the event was not created by the logged-in user, event is not deleted.
        else {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            Label lb = new Label("You are can not delete this event because you are not its organiser");
            Button okbutton = new Button("OK");
            dialogVbox.getChildren().addAll(lb, okbutton);
            dialogVbox.setAlignment(Pos.CENTER);
            Scene dialogScene = new Scene(dialogVbox, 300, 110);
            stage.setScene(dialogScene);
            stage.show();
            okbutton.setOnMouseClicked(e -> stage.close());
        }
    }

    //Initialises the properties and objects of the scene.
    //Add image associated with each event to the listview
 @Override
    public void initialize(URL location, ResourceBundle resources){
      ObservableList<Event> eventsList = FXCollections.observableArrayList(EventApp.getEventMain().listMyEvents());
      ObservableList<String> eLocations = FXCollections.observableArrayList(EventApp.getEventMain().getMyELocations());
      ObservableList<String> eventTypes = FXCollections.observableArrayList(EventApp.getEventMain().getMyETypes());
      listView.setItems(eventsList);
      eLocationComboBox.setItems(eLocations);
      eTypeComboBox.setItems(eventTypes);
      eLocationComboBox.setValue("All");
      eTypeComboBox.setValue("All");
     listView.setCellFactory(item -> new ListCell<>() {
         private ImageView imageView = new ImageView();
         @Override
         public void updateItem(Event item, boolean empty) {
             super.updateItem(item, empty);
             if (empty) {
                 setText(null);
                 setGraphic(null);
             } else {
                 InputStream is = null;
                 String imgName = item.getImageName();
                 if (imgName == null)
                     try {
                         is = new FileInputStream("src/img/default.jpg");
                         Image image = new Image(is, 200, 200, true, true);
                         imageView.setImage(image);
                         setGraphic(imageView);
                         setText(item.toString());
                     } catch (FileNotFoundException m) {
                         m.printStackTrace();
                     }
                 else try {
                     is = new FileInputStream("src/img/" + imgName);
                     Image image = new Image(is, 500, 350, false, true);
                     imageView.setImage(image);
                     setGraphic(imageView);
                     setText(item.toString());
                     setFont(Font.font(14.0));
                 } catch (FileNotFoundException m) {
                     m.printStackTrace();
                 }
             }
         }
     });
    }

    //method which when called retrieves and displays all the organiser's events
    @FXML
    void OnAllMyEvents(ActionEvent event) {
        ArrayList<Event> events = new ArrayList<>(EventApp.getEventMain().listMyEvents());
        ObservableList<Event> eventsList = FXCollections.observableArrayList(events);
        listView.setItems(eventsList);
    }
}
