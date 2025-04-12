/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmanagementsystem;

import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author serge
 */
public class PatientPageController implements Initializable {

    @FXML
    private TextField login_PatientID;

    @FXML
    private CheckBox login_checkBox;

    @FXML
    private AnchorPane login_form;

    @FXML
    private Button login_loginBtn;

    @FXML
    private PasswordField login_password;

    @FXML
    private Hyperlink login_registerHere;

    @FXML
    private TextField login_showpassword;

    @FXML
    private ComboBox<?> login_user;

    @FXML
    private AnchorPane main_form;
 //DATABASE TOOL IS DICLARED ON HERE
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private final AlertMessage alert = new AlertMessage();
    @FXML
    void loginAccount(ActionEvent event) {
         if (login_PatientID.getText().isEmpty()
                || login_password.getText().isEmpty()) {
            alert.errorMessage("Incorrect Patient ID/password");
        } else {
            String sql = "SELECT * FROM patient WHERE patient_id = ? AND password = ? AND date_delete  is NULL";
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

                String checkStatus = "SELECT status FROM patient WHERE patient_id = '"
                        + login_PatientID.getText() + "'AND password = '"
                        + login_password.getText() + "' AND status='Confirm'";

                prepare = connect.prepareStatement(checkStatus);
                result = prepare.executeQuery();

                if (result.next()) {
                    alert.errorMessage("Need the confirmition of the admin");
                    
                    if (!login_password.isVisible()) {
                        if (!login_showpassword.getText().equals(login_password.getText())) {
                            login_showpassword.setText(login_password.getText());
                        } else {
                            if (!login_showpassword.getText().equals(login_password.getText())) {
                                login_password.setText(login_showpassword.getText());
                            }
                        }
                    }
                    
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, login_PatientID.getText());
                    prepare.setString(2, login_password.getText());

                    result = prepare.executeQuery();

                    if (result.next()) {
                        alert.successMessage("LOgin Succesfully");
                    } else {
                        alert.errorMessage("Incorrect Username/Password");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void loginShowPassword(ActionEvent event) {
        if (login_checkBox.isSelected()) {
            login_showpassword.setText(login_password.getText());
            login_password.setVisible(false);
            login_showpassword.setVisible(true);
        } else {
            login_password.setText(login_showpassword.getText());
            login_password.setVisible(true);
            login_showpassword.setVisible(false);
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

    @FXML
    void switchForm(ActionEvent event) {
        
    }

    @FXML
    void switchPage(ActionEvent event) {

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

        } else if (login_user.getSelectionModel().getSelectedItem() == "Patient Portal") {
            
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         userList();
    }

}
