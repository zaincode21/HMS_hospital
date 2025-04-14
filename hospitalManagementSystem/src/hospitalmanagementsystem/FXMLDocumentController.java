/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmanagementsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author serge
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private CheckBox login_checkBox;

    @FXML
    private AnchorPane login_form;

    @FXML
    private Button login_loginBtn;

    @FXML
    private PasswordField login_password;
    @FXML
    private TextField login_showpassword;

    @FXML
    private Hyperlink login_registerHere;

    @FXML
    private TextField login_usename;

    @FXML
    private ComboBox<?> login_user;

    @FXML
    private AnchorPane main_form;

    @FXML
    private CheckBox register_checkBox;

    @FXML
    private TextField register_email;

    @FXML
    private AnchorPane register_form;

    @FXML
    private Hyperlink register_loginHere;

    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_showPassword;

    @FXML
    private Button register_signupBtn;

    @FXML
    private TextField register_username;

    // DATABASE TOOLS 
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private final AlertMessage alert = new AlertMessage();

    public void loginAccount() {
        if (login_usename.getText().isEmpty()
                || login_password.getText().isEmpty()) {
            alert.errorMessage("Incorrect Username / Password");
        } else {
            String checkUsername = "SELECT * FROM admin WHERE username = ? AND password = ?";

            connect = Database.connectDB();

            try {
                if (!login_password.isVisible()) {
                    if (!login_showpassword.getText().equals(login_password.getText())) {
                        login_showpassword.setText(login_password.getText());
                    } else {
                        if (!login_showpassword.getText().equals(login_password.getText())) {
                            login_password.setText(login_showpassword.getText());
                        }
                    }
                }
                prepare = connect.prepareStatement(checkUsername);
                prepare.setString(1, login_usename.getText());
                prepare.setString(2, login_password.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    
                    Data.admin_username = login_usename.getText();
                    alert.successMessage("Login successfully!");
                    
                    Parent root = FXMLLoader.load(getClass().getResource("AdminMForm.fxml"));
                    Stage stage = new Stage();
                    
                    stage.setTitle("Hospital Management System | Admin Portal");
                    stage.setScene(new Scene(root));
                    stage.show();
                    
                    // TO HIDE YOUR ADMIN PAGE (LOGIN FORM)
                    login_loginBtn.getScene().getWindow().hide();

                } else {
                    alert.errorMessage("incorrect USername/password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //SHOW ME PASSWORD ON LOGIN PAGE
    public void loginShowPassword() {
        if (login_checkBox.isSelected()) {
            login_showpassword.setText(login_password.getText());
            login_showpassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showpassword.getText());
            login_showpassword.setVisible(false);
            login_password.setVisible(true);
        }
    }

    public void registerAccount() {
        if (register_email.getText().isEmpty()
                || register_username.getText().isEmpty()
                || register_password.getText().isEmpty()) {
            alert.errorMessage("Please fill all fields");
        } else {
            String checkUsername = "SELECT * FROM admin WHERE username = ?";
            connect = Database.connectDB();

            try {
                if (!register_showPassword.isVisible()) {
                    if (!register_showPassword.getText().equals(register_password.getText())) {
                        register_showPassword.setText(register_password.getText());

                    }
                } else {
                    if (!register_showPassword.getText().equals(register_password.getText())) {
                        register_password.setText(register_showPassword.getText());
                    }
                }
                // Check if the username already exists
                prepare = connect.prepareStatement(checkUsername);
                prepare.setString(1, register_username.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    alert.errorMessage(register_username.getText() + " is already registered!");
                } else if (register_password.getText().length() < 8) {
                    // CONDUCTION OF CHECKING IF USER INSERT LESS THAN 8 DIGITAL
                    alert.errorMessage("Invalid password, at least 8 characters needs");

                } else {
                    String insertDate = "INSERT INTO admin (email, username, password, date) VALUES (?, ?, ?, ?)";

                    // Get the current date
                    java.util.Date date = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare = connect.prepareStatement(insertDate);
                    prepare.setString(1, register_email.getText());
                    prepare.setString(2, register_username.getText());
                    prepare.setString(3, register_password.getText());
                    prepare.setString(4, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert.successMessage("Registered Successfully!!");
                    registerClear();

                    login_form.setVisible(true);
                    register_form.setVisible(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerClear() {
        register_email.clear();
        register_username.clear();
        register_password.clear();
        register_showPassword.clear();

    }

    public void register_ShowPassword() {
        if (register_checkBox.isSelected()) {
            register_showPassword.setText(register_password.getText());
            register_showPassword.setVisible(true);
            register_password.setVisible(false);
        } else {
            register_password.setText(register_showPassword.getText());
            register_showPassword.setVisible(false);
            register_password.setVisible(true);
        }

    }

    public void userList() {

        List<String> listU = new ArrayList<>();

        for (String data : Users.user) {
            listU.add(data);
        }
        ObservableList listData = FXCollections.observableList(listU);
        login_user.setItems(listData);
    }

    public void switchPage() {
        if (login_user.getSelectionModel().getSelectedItem() == "Admin portal") {

            try {

                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();

                stage.setTitle("Hospital management system");
                
                stage.setMinWidth(340);
                stage.setMinHeight(580);

                stage.setScene(new Scene(root));
                stage.show();
                
               
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (login_user.getSelectionModel().getSelectedItem() == "Doctor Portal") {
             try {

                Parent root = FXMLLoader.load(getClass().getResource("DoctorPage.fxml"));
                Stage stage = new Stage();

                stage.setTitle("Hospital management system");
                
                stage.setMinWidth(340);
                stage.setMinHeight(580);

                stage.setScene(new Scene(root));

                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (login_user.getSelectionModel().getSelectedItem() == "patient Portal") {
              try {

                Parent root = FXMLLoader.load(getClass().getResource("PatientPage.fxml"));
                Stage stage = new Stage();

                stage.setTitle("Hospital management system");
                
                stage.setMinWidth(340);
                stage.setMinHeight(580);

                stage.setScene(new Scene(root));

                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         login_user.getScene().getWindow().hide();
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == login_registerHere) {
            //REGISTRATION FORM WILL SHOW
            login_form.setVisible(false);
            register_form.setVisible(true);
        } else if (event.getSource() == register_loginHere) {
            //LOGIN FORM WILL SHOW
            login_form.setVisible(true);
            register_form.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userList();
    }

}
