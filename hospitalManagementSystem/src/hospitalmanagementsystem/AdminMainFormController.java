/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmanagementsystem;

import static java.lang.String.format;
import static java.lang.String.format;
import static java.lang.String.format;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

/**
 *
 * @author serge
 */
public class AdminMainFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> Appointment_appointmentID;

    @FXML
    private Label Appointments_form;

    @FXML
    private TableColumn<?, ?> Doctors_col_active;

    @FXML
    private TableColumn<?, ?> Doctors_col_address;

    @FXML
    private TableColumn<?, ?> Doctors_col_contactNumber;

    @FXML
    private TableColumn<?, ?> Doctors_col_doctorID;

    @FXML
    private TableColumn<?, ?> Doctors_col_email;

    @FXML
    private TableColumn<?, ?> Doctors_col_gender;

    @FXML
    private TableColumn<?, ?> Doctors_col_name;

    @FXML
    private TableColumn<?, ?> Doctors_col_specielization;

    @FXML
    private TableColumn<?, ?> Doctors_col_status;

    @FXML
    private AnchorPane Doctors_form;

    @FXML
    private TableView<?> Doctors_tableView;

    @FXML
    private TableColumn<?, ?> Patients_col_PatientID;

    @FXML
    private TableColumn<?, ?> Patients_col_action;

    @FXML
    private TableColumn<?, ?> Patients_col_contactNumber;

    @FXML
    private TableColumn<?, ?> Patients_col_date;

    @FXML
    private TableColumn<?, ?> Patients_col_dateDelete;

    @FXML
    private TableColumn<?, ?> Patients_col_dateModify;

    @FXML
    private TableColumn<?, ?> Patients_col_description;

    @FXML
    private TableColumn<?, ?> Patients_col_gender;

    @FXML
    private TableColumn<?, ?> Patients_col_status;

    @FXML
    private TableView<?> Patients_form;

    @FXML
    private TableColumn<?, ?> appointment_action;

    @FXML
    private TableColumn<?, ?> appointment_contactNumber;

    @FXML
    private TableColumn<?, ?> appointment_date;

    @FXML
    private TableColumn<?, ?> appointment_dateDelete;

    @FXML
    private TableColumn<?, ?> appointment_dateModify;

    @FXML
    private TableColumn<?, ?> appointment_description;

    @FXML
    private AnchorPane appointment_form;

    @FXML
    private TableColumn<?, ?> appointment_gender;

    @FXML
    private TableColumn<?, ?> appointment_name;

    @FXML
    private TableColumn<?, ?> appointment_status;

    @FXML
    private TableView<?> appointment_tableView;

    @FXML
    private Button appointments_btn;

    @FXML
    private Label current_form;

    @FXML
    private Label dashboard_AD;

    @FXML
    private Label dashboard_AP;

    @FXML
    private Label dashboard_TP;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_tA;

    @FXML
    private Label date_time;

    @FXML
    private Button doctors_btn;

    @FXML
    private BarChart<?, ?> doctors_chart_DD;

    @FXML
    private AreaChart<?, ?> doctors_chart_PD;

    @FXML
    private TableColumn<?, ?> doctors_col_Specialized;

    @FXML
    private TableColumn<?, ?> doctors_col_Status;

    @FXML
    private TableColumn<?, ?> doctors_col_doctorID;

    @FXML
    private TableColumn<?, ?> doctors_col_name;

    @FXML
    private TableView<?> doctors_tableView;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Label nav_adminID;

    @FXML
    private Label nav_username;

    @FXML
    private Button patients_btn;

    @FXML
    private AnchorPane patients_form;

    @FXML
    private Button payment_btn;

    @FXML
    private Button profile_btn;

    @FXML
    private Circle top_profile;

    @FXML
    private Label top_username;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            Doctors_form.setVisible(false);
            patients_form.setVisible(false);
            appointment_form.setVisible(false);
        } else if (event.getSource() == doctors_btn) {
            dashboard_form.setVisible(false);
            Doctors_form.setVisible(true);
            patients_form.setVisible(false);
            appointment_form.setVisible(false);
        } else if (event.getSource() == patients_btn) {
            dashboard_form.setVisible(false);
            Doctors_form.setVisible(false);
            patients_form.setVisible(true);
            appointment_form.setVisible(false);
        } else if (event.getSource() == appointments_btn) {
            dashboard_form.setVisible(false);
            Doctors_form.setVisible(false);
            patients_form.setVisible(false);
            appointment_form.setVisible(true);
        }
    }

    public void displayAdminIDUsername() {
        String sql = " SELECT * FROM admin WHERE username= '"
                + Data.admin_username + "'";

        connect = Database.connectDB();
        
        try{
            prepare = connect .prepareStatement(sql);
            result = prepare.executeQuery();
            
            if(result.next()){
                nav_adminID.setText(result.getString("admin_id"));
                String tempUsername = result .getString("username");
                tempUsername = tempUsername.substring(0, 1).toUpperCase() + tempUsername.substring(1);
                nav_username.setText(tempUsername);
            }
        }catch(Exception e){e.printStackTrace();}
    }

    public void runTime() {
        new Thread() {

            public void run() {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                while (true) {
                    try {

                        Thread.sleep(1000); // 1000 ms = 1s

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Platform.runLater(() -> {
                        date_time.setText(format.format(new Date()));
                    });
                }
            }
        }.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runTime();
        displayAdminIDUsername();
    }

}
