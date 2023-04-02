package com.example.eventapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginSceneController {

    @FXML
    private TextField email;

    @FXML
    private Text feedback;

    @FXML
    private PasswordField password;

    private boolean goodToGo = true;

    @FXML
    void clickLoginButton(ActionEvent event) {
        /*ToDO: Check on Login. May need a while loop*/
        feedback.setText("");
        // Validate the fields
            if (email.getText().isBlank() || password.getText().isBlank()) {
                feedback.setText("Some fields are blank");
                goodToGo = false;
            }
            /*Admin Login*/
            if (email.getText().equals("admin@gmail.com") && password.getText().equals("adm1n?")) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("AdminScene.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 700.0, 600.0);
                    stage.setTitle("EventApp|Administrator");
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    System.out.println("hey!");
                }
            }
            else{
                if (!EventApp.getEmails().contains(email.getText())) {
                    //   if email HashSet does NOT contains the login email entered
                    feedback.setText("No such email address registered. Go to Register");
                    goodToGo = false;
                }
                else if (!EventApp.getOrganiserDetailsMap().get(email.getText()).getPassword().equals(password.getText())) {
                    feedback.setText("Incorrect email or password");
                    password.clear();
                    goodToGo = false;
                }
                else if (goodToGo) {
                    int id = EventApp.getOrganiserDetailsMap().get(email.getText()).getOId();
                    //Make sure only enabled users can log in
                    if (EventApp.getOrganiser(id).isEnabled()) {
                        EventApp.setLoggedInUserId(id);
                        String username = EventApp.getOrganiserDetailsMap().get(email.getText()).getName();
                        EventApp.setLoggedInUser(username);
                        /*Organiser login*/
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("OrganiserScene.fxml"));
                            Scene scene = new Scene(fxmlLoader.load(), 700.0, 600.0);
                            stage.setTitle("EventApp| " + EventApp.getLoggedInUser());
                            stage.setScene(scene);
                            stage.show();
                        } catch (Exception e) {
                            System.out.println("hey there!");
                        }
                    }
                    else feedback.setText(("Admin deactivated your account!")); //If organiser has been deactivated
                }
            }
    }// End of clickLoginButton Method

    @FXML
    void onClickBackButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EventApp.class.getResource("MainScene.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700.0, 600.0);
            stage.setTitle("EventApp");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println("Oh!");
        }
    }/*end of onClickBackButton method*/

}/*end of controller class*/
