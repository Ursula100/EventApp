package com.example.eventapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class RegisterSceneController {

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField email;

    @FXML
    private Text feedback;
    @FXML
    private Text emailFeedback;

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private Text passwordFeedback;

    @FXML
    private TextField sMedia1;

    @FXML
    private TextField sMedia2;

    @FXML
    private TextField telephone;

    @FXML
    private TextField website;

    //Method to register user and saving user's details
    @FXML
    void onRegister(ActionEvent event){
        System.out.println("I'm Here!");
        feedback.setText("");
        passwordFeedback.setText("");
        emailFeedback.setText("");
        boolean goodToGo = true;
        try {
            /*Choose to make name, email, and passwords fields compulsory, so not blank*/
            if (email.getText().isBlank() || password.getText().isBlank() || name.getText().isBlank() || confirmPassword.getText().isBlank()) {
                feedback.setText("Fill all required Fields");
                goodToGo = false;
            }
            //Checking when email is not blank and email does not match set pattern.
            if (!email.getText().matches("^[a-z][a-zA-Z0-9]{2,}@[a-z]{2,}[.][a-z]{2,}$")){//email starts with a lower case letter,should have at least 2 characters in each part, contains an @ and a dot(.)
                emailFeedback.setText("Invalid Email. Email should be in the for b@a.c where a,b, and c should at least 2 characters each");
                goodToGo = false;
            }
            if (password.getText().length() < 8) {
                passwordFeedback.setText("Password should be 8 or more characters");
                goodToGo = false;
            }
            /*Checking that password and confirm password fields match*/
            if (!(password.getText().equals(confirmPassword.getText()))) {
                passwordFeedback.setText("Passwords do not match");
                goodToGo = false;
            }
            //Checks that the email provided is not already linked to an organiser's account
            if(EventApp.getEmails().contains(email.getText())){
                emailFeedback.setText("This email is already registered.");
                goodToGo = false;
            }
            //After all checks are satisfied
            if(goodToGo) {
                EventApp.getEmails().add(email.getText());/*Adds email to Emails HashSet*/
                EventApp.save(EventApp.getEmails(), "emails.ser"); //saves user's registration email and updates emails record
                Organiser org = new Organiser(name.getText(), email.getText(), password.getText(), telephone.getText(), website.getText(), sMedia1.getText(), sMedia2.getText());/*Creates new organiser*/
                EventApp.getOrganiserDetailsMap().put(email.getText(), org); /*Adds the new organiser to the organiser map*/
                EventApp.save(EventApp.getOrganiserDetailsMap(), "organisersMap.ser");//updates organiser's records in file.
                //Redirect user to login scene.
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("LoginScene.fxml"));
                    Scene scene = new Scene((Parent)fxmlLoader.load(), 700.0, 600.0);
                    stage.setTitle("EventApp|Login");
                    stage.setScene(scene);
                    stage.show();
                }
                    catch (IOException e) {
                        System.out.println(e);
                    }
                }
        }
        catch (Exception e) {
            System.out.println(" exception");
        }
    }


    //Method to move back to main scene
    @FXML
    void onBackButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("MainScene.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700.0, 600.0);
            stage.setTitle("EventApp");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


