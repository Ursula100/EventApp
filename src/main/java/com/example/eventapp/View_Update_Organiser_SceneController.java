package com.example.eventapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class View_Update_Organiser_SceneController {

    @FXML
    private TextField email;

    @FXML
    private CheckBox enableCheckBox;

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField password;

    @FXML
    private TextField sM1;

    @FXML
    private TextField sM2;

    @FXML
    private TextField telephone;

    @FXML
    private Button updateButton;

    @FXML
    private TextField website;


    //Redirects us back to admin scene
    @FXML
    void onBack(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdminScene.fxml")));
        Scene tableViewScene = new Scene(tableViewParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();

    }

    //Method to edit and update selected organiser's records
    @FXML
    void onUpdate(ActionEvent event) throws FileNotFoundException {
        int orgId = (int) Integer.parseInt(id.getText());
        Organiser org = (Organiser) EventApp.getOrganiser(orgId);
        org.setName(name.getText());
        org.setEmail(email.getText());
        org.setTelephone(telephone.getText());
        org.setWebsite("" + website.getText());
        org.setsMedia1("" + sM1.getText());
        org.setsMedia2("" + sM2.getText());
        org.setPassword(password.getText());
        org.setEnabled(enableCheckBox.isSelected());
        EventApp.save(EventApp.getEmails(), "emails.ser");
        EventApp.save(EventApp.getOrganiserDetailsMap(), "organisersMap.ser");
    }

    //displays of selected organiser from tableView in admin scene.
    public void initData(Organiser o) {
        id.setText("" + o.getOId());
        name.setText("" + o.getName());
        email.setText(o.getEmail());
        telephone.setText("" + o.getTelephone());
        website.setText("" + o.getWebsite());
        sM1.setText("" + o.getsMedia1());
        sM2.setText("" + o.getsMedia2());
        password.setText(o.getPassword());
        enableCheckBox.setSelected(o.isEnabled());
    }
}

