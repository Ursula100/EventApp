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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminSceneController implements Initializable {

    @FXML
    private TextField email;

    @FXML
    private TextField id;

    @FXML
    private TableView<Organiser> tableView;

    @FXML
    private ListView<Event> listView;

    @FXML
    private Button showOrgEvents;

    @FXML
    private Button enableButton;

    @FXML
    private Button disableButton;

    @FXML
    private Text numOfEvents;

    @FXML
    private Button view_updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<Organiser, Date> regDateCol;

    @FXML
    private TableColumn<Organiser,String> sMCol1;

    @FXML
    private TableColumn<Organiser, String> emailCol;

    @FXML
    private TableColumn<Organiser, Integer> idCol;

    @FXML
    private TableColumn<Organiser, String> nameCol;

    @FXML
    private TableColumn<Organiser, String> sMCol2;


    @FXML
    private TableColumn<Organiser, String> telCol;

    @FXML
    private TableColumn<Organiser, String> websiteCol;

    //displays selected organiser's events
    @FXML
    void OnOrgEvents(ActionEvent event) { //shows selected organiser's events
        Organiser organiser = (Organiser) tableView.getSelectionModel().getSelectedItem();//get selected organiser and cast for precaution
        ArrayList<Event> s = new ArrayList<>();
        for(Event e: EventMain.events){
            if(e.getOrganiserId()==(organiser.getOId()))
                s.add(e);
        }
        numOfEvents.setText("" + s.size()); //Display number of events the organiser has.
        //adding selected organiser's arraylist of events to listview
        ObservableList<Event> list = FXCollections.observableArrayList(s);
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
                            Image image = new Image(is, 350, 350, false, true);
                            imageView.setImage(image);
                            setGraphic(imageView);
                            setText(item.toString());
                        } catch (FileNotFoundException m) {
                            m.printStackTrace();
                        }
                        //Else if event has an image associated with it
                    else try {
                        is = new FileInputStream("src/img/" + imgName); //This combination forms the image's path
                        Image image = new Image(is, 350, 350, false, true);//assigning image and set the image's properties.
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

    //selects organiser object from tableView
    @FXML
    void onSelectOrg(){
        showOrgEvents.setDisable(false);//enable showOrgEvents button
        deleteButton.setDisable(false);//enable delete button
        Organiser organiser = (Organiser) tableView.getSelectionModel().getSelectedItem();
        if (organiser.isEnabled()) { //if selected organiser is enabled then the enable button is disabled
            enableButton.setDisable(true);
            disableButton.setDisable(false);
        } else { //if selected organiser is disabled then the disable button is disabled
            enableButton.setDisable(false);
            disableButton.setDisable(true);
        }
    }

    //disables selected organiser
    @FXML
    void onDisable(ActionEvent event) {
        Organiser organiser = (Organiser) tableView.getSelectionModel().getSelectedItem();//get selected organiser and cast for precaution
        organiser.setEnabled(false);//disables organiser
        disableButton.setDisable(true);//disables disable button
        enableButton.setDisable(false);//enables enable button
    }

    //enables selected organiser
    @FXML
    void onEnable(ActionEvent event) {
        Organiser organiser = (Organiser) tableView.getSelectionModel().getSelectedItem();//get selected organiser from table view
        organiser.setEnabled(true);//enables organiser
        enableButton.setDisable(true);//disables enableButton
        disableButton.setDisable(false);//enables disableButton
    }

    @FXML
    void onLogOut(ActionEvent event) {//Go back to mainScene
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("MainScene.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700.0, 600.0);
            stage.setTitle("EventApp");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //Method to search for organiser using either email or organiser's id
    @FXML
    void onSearch(ActionEvent event) {
        //ArrayList<Organiser> s = new ArrayList<>();
        if (!email.getText().isBlank() && id.getText().isBlank()) { //using organiser's id only
            if (EventApp.getOrganiserDetailsMap().containsKey(email.getText())) {
                Organiser o = EventApp.getOrganiserDetailsMap().get(email.getText());
                ObservableList<Organiser> org = FXCollections.observableArrayList(o);
                tableView.setItems(org);
            } else System.out.println("No Organiser with that email");
        }
        else if (email.getText().isBlank() && !id.getText().isBlank()) {// using organiser's email
            int orgId = (int) Integer.parseInt(id.getText());
            Organiser o = EventApp.getOrganiser(orgId);
            if (o != null) {
                ObservableList<Organiser> org = FXCollections.observableArrayList(o);
                tableView.setItems(org);
            }
            else {
                System.out.print("No organiser with that Id found");
                Organiser p = null;
                ObservableList<Organiser> org = FXCollections.observableArrayList(p);
                tableView.setItems(org);
            }
        }
        else System.out.println("Can only search using one of the two options");
    }

    //Method to delete an organiser object along with all organiser's events
    //Saves and updates organiser records
    //Shows pop up warning to require confirmation of deletion intention.
    @FXML
    void onDelete(ActionEvent event) {
        Organiser organiser = tableView.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        Label lb = new Label("Deleting this organiser will automatically delete the events created by the organiser. Are you sure you want to delete this Organiser?");
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        dialogVbox.getChildren().addAll(lb, yesButton, noButton);
        dialogVbox.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(dialogVbox, 300, 110);
        stage.setScene(dialogScene);
        stage.show();
        //Action when deletion is confirmed by clicking yes button.
        yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.close();
                EventApp.getOrganiserDetailsMap().remove(organiser.getEmail());
                EventApp.getEmails().remove(organiser.getEmail());
                EventMain.events.removeIf(e -> e.getOrganiserId() == (organiser.getOId()));
                ObservableList<Organiser> org = FXCollections.observableArrayList(EventApp.getOrganiserDetailsMap().values());
                tableView.setItems(org);
                try {
                    EventApp.getEventMain().save(EventMain.events);
                } catch (FileNotFoundException e) {
                    System.out.println("Deleted organiser's events' file not found");
                }
                try {
                    EventApp.save(EventApp.getEmails(), "emails.ser");
                } catch (FileNotFoundException e) {
                    System.out.println("Emails' file not found");
                }
                try {
                    EventApp.save(EventApp.getOrganiserDetailsMap(), "organisersMap.ser");
                } catch (FileNotFoundException e) {
                    System.out.println("Organisers' file not found");
                }
            }
        });
    noButton.setOnMouseClicked(e -> stage.close());//if no button is clicked then no further action is performed
    }

    @FXML
    void onDetailedView_Update(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("View_Update_OrganiserScene.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        //access the controller and call a method
        View_Update_Organiser_SceneController controller = loader.getController();
        Organiser organiserToDisplay =(Organiser) tableView.getSelectionModel().getSelectedItem(); //cast as Organiser
        if(organiserToDisplay == null) //sometimes no organiser will have been selected
            return;

        controller.initData(organiserToDisplay);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Organiser> org = FXCollections.observableArrayList(EventApp.getOrganiserDetailsMap().values());
        idCol.setCellValueFactory(new PropertyValueFactory<>("oId"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        telCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        websiteCol.setCellValueFactory(new PropertyValueFactory<>("website"));
        sMCol1.setCellValueFactory(new PropertyValueFactory<>("sMedia1"));
        sMCol2.setCellValueFactory(new PropertyValueFactory<>("sMedia2"));
        regDateCol.setCellValueFactory(new PropertyValueFactory<>("dateRegistered"));
        tableView.setItems(org);
    }
}


