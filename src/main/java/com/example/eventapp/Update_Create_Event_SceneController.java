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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Update_Create_Event_SceneController implements Initializable {

    @FXML
    private Button createButton;

    @FXML
    private Button searchButton;

    @FXML
    private DatePicker datepicker;

    @FXML
    private TextArea description;

    @FXML
    private TextField fee;

    @FXML
    private Text feedback;

    @FXML
    private Text selectedImg;

    @FXML
    private TextField id;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField location;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private TextField title;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button updateButton;

    @FXML
    private TextField venue;

    @FXML
    void onClickBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("OrganiserScene.fxml"));
            Scene scene = new Scene((Parent) fxmlLoader.load(), 700.0, 600.0);
            stage.setTitle("EventApp|" + EventApp.getLoggedInUser());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("EventSceneController logout error");
        }
    }

    @FXML
    void onClickCreate(ActionEvent event) throws FileNotFoundException {
        double price = (double) Double.parseDouble(fee.getText());
        LocalDate date = (LocalDate) datepicker.getValue();

        String defaultImg = "src/img/default.jpg"; //initially set's event's value to default image in case no image is uploaded

        Event e = new Event(title.getText(), location.getText(), venue.getText(), typeComboBox.getValue(), description.getText(), price, date, timeComboBox.getValue(), defaultImg);
        EventApp.getEventMain().addEvent(e);
        if (!selectedImg.getText().isBlank()) { //if an image is updated
            File imgSource = new File(selectedImg.getText());

            String imgName = e.getId() + ".jpg";

            File imgDestination = new File("src/img/" + imgName); /*Saves as jpeg image in img folder and names it with reference to the event's id for example 21010001.jpg */

            FileChannel sourceChannel = null;
            FileChannel destChannel = null;

            try {
                sourceChannel = new FileInputStream(imgSource).getChannel();
                destChannel = new FileOutputStream(imgDestination).getChannel();
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
                System.out.println(imgDestination.getPath());
            }
            catch (IOException exception) {System.out.println("IOException Image Selection");}
            catch (Exception exception) {System.out.println("Exception Image Selection");}

            finally {
                try {
                    if (sourceChannel != null) {
                        sourceChannel.close();
                    }
                    if (destChannel != null) {
                        destChannel.close();
                    }
                    System.out.println("Channels closed");
                    }
                catch (IOException exception) {System.out.println("IOException Close Channel");}
                catch (Exception exception) {System.out.println("Exception Close Channel");}
            }
            e.setImageName(imgName);
        }
        EventApp.getEventMain().save(EventMain.events); //updates events arraylist file records to include recent addition.
        feedback.setText("Event Successfully created!");
    }

    @FXML
    void onClickDatePicker(ActionEvent event) {

    }

    @FXML
    void onClickSearch(ActionEvent event) {
        int eventId = (int) Integer.parseInt(id.getText());
        Event e = EventApp.getEventMain().searchEventByID(eventId);
        if (e != null) {
            if (e.getOrganiserId() != EventApp.getLoggedInUserId()) {
                updateButton.setDisable(true);
                feedback.setText("You can not update this event. Not this events organiser");
            } else updateButton.setDisable(false);
            id.setText("" + e.getId()); //int to String
            title.setText(e.getEName());
            description.setText(e.getEDescription());
            fee.setText("" + e.getECost());//cost to String
            location.setText(e.getELocation());
            venue.setText(e.getEVenue());
            typeComboBox.setValue("" + e.getEType());
            timeComboBox.setValue("" + e.getTime());
            datepicker.setValue(e.getDate());
        } else {
            updateButton.setDisable(true);
            title.setText("");
            description.setText("");
            fee.setText("");
            location.setText("");
            venue.setText("");
            typeComboBox.setValue("");
            timeComboBox.setValue("");
            datepicker.setValue(null);
            feedback.setText("No event with this Id");
        }
    }

    @FXML
    void onClickUpdate(ActionEvent event) throws FileNotFoundException {
        double eventFee = (double) Double.parseDouble(fee.getText());
        int eventId = (int) Integer.parseInt(id.getText());
        LocalDate ld = (LocalDate) datepicker.getValue();

        File imgSource = new File(selectedImg.getText());

        String imgName = eventId + ".jpg" ;

        File imgDestination = new File("src/img/" + imgName); /*Saves as jpeg image in img folder and names it with reference to the event's id for example 21010001.jpg */

        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        try {
            sourceChannel = new FileInputStream(imgSource).getChannel();
            destChannel = new FileOutputStream(imgDestination).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            System.out.println(imgDestination.getPath());
        } catch (IOException e) {
            System.out.println("IOException Image Selection");
        } catch (Exception e) {
            System.out.println("Exception Image Selection");

        }
        finally {
            try {
                if (sourceChannel != null) {
                    sourceChannel.close();
                }
                if (destChannel != null) {
                    destChannel.close();
                }
                System.out.println("Channels closed");
            } catch (IOException e) {
                System.out.println("IOException Close Channel");
            } catch (Exception e) {
                System.out.println("Exception Close Channel");
            }
        }

        EventApp.getEventMain().updateEventDetails(eventId, title.getText(), location.getText(), venue.getText(), typeComboBox.getValue(), description.getText(), eventFee, ld, timeComboBox.getValue(), imgName);
        EventApp.getEventMain().save(EventMain.events);
        feedback.setText("Event has been updated!");
    }

    @FXML
    void onUploadButtonClicked(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Event Picture");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        File source = fileChooser.showOpenDialog(stage);

        selectedImg.setText(source.getPath());

        try {
            String imageSelected = source.getName();
            System.out.println(imageSelected);
        }
        catch (Exception e) {
            System.out.println("Exception Image Selection");
        }

        String imageName = source.getPath();
        System.out.println(imageName);
        Image image = new Image(source.getAbsolutePath());
        imageView.setImage(image);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> eTimes = FXCollections.observableArrayList("6.00", "7.00", "8.00", "9.00", "10.00", "11.00", "12.00", "13.00", "14.00", "15.00", "16.00", "17.00", "18.00", "19.00", "20.00", "21.00", "22.00", "23.00");
        ObservableList<String> eventTypes = FXCollections.observableArrayList(EventApp.getEventMain().getETypes());
        timeComboBox.setItems(eTimes);
        typeComboBox.setItems(eventTypes);
        if (!OrganiserSceneController.isFor_Next())
            createButton.setDisable(true);
        else {
            id.setEditable(false);
            updateButton.setDisable(true);
            searchButton.setDisable(true);
            }
    }

    public void initData(Event e) {
        id.setText(""+ e.getId()); //int to String
        title.setText(e.getEName());
        description.setText(e.getEDescription());
        fee.setText("" + e.getECost());//cost to String
        location.setText(e.getELocation());
        venue.setText(e.getEVenue());
        typeComboBox.setValue("" + e.getEType());
        timeComboBox.setValue("" + e.getTime());
        datepicker.setValue(e.getDate());
        InputStream is = null;
        try {
            is = new FileInputStream("src/img/" + e.getImageName());
        } catch (FileNotFoundException m) {
            m.printStackTrace();
        }
        Image image = new Image(is);
        imageView.setImage(image);
        if(e.getOrganiserId()!= EventApp.getLoggedInUserId()) {
            updateButton.setDisable(true);
            feedback.setText("You can not update this event. Not this events organiser");
        }
        else updateButton.setDisable(false);
    }
}
