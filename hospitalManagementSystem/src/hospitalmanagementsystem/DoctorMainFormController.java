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
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import static javax.management.remote.JMXConnectorFactory.connect;

/**
 *
 * @author serge
 */
public class DoctorMainFormController implements Initializable {

    @FXML
    private TextField Appointment_appointmentID;

    @FXML
    private TableColumn<?, ?> Appointment_col_appointmentID;

    @FXML
    private Label Appointments_form;

    @FXML
    private Label Patients_PA_dateCreated;

    @FXML
    private Label Patients_PA_password;

    @FXML
    private Label Patients_PA_patientID;

    @FXML
    private Button Patients_PI_addBtn;

    @FXML
    private Label Patients_PI_address;

    @FXML
    private Label Patients_PI_gender;

    @FXML
    private Label Patients_PI_mobileNumber;

    @FXML
    private Label Patients_PI_patientName;

    @FXML
    private Button Patients_PI_recordBtn;

    @FXML
    private TextField Patients_PatientID;

    @FXML
    private TextField Patients_PatientName;

    @FXML
    private TextArea Patients_address;

    @FXML
    private Button Patients_confirmBtn;

    @FXML
    private ComboBox<?> Patients_gender;

    @FXML
    private TextField Patients_mobileNumber;

    @FXML
    private TextField Patients_password;

    @FXML
    private TextArea appointment_address;

    @FXML
    private TableColumn<AppointmentData, String> appointment_col_action;

    @FXML
    private TableColumn<AppointmentData, String> appointment_col_contactNumber;

    @FXML
    private TableColumn<AppointmentData, String> appointment_col_date;

    @FXML
    private TableColumn<AppointmentData, String> appointment_col_dateDelete;

    @FXML
    private TableColumn<AppointmentData, String> appointment_col_dateModify;

    @FXML
    private TableColumn<AppointmentData, String> appointment_col_description;

    @FXML
    private TableColumn<AppointmentData, String> appointment_col_gender;

    @FXML
    private TableColumn<AppointmentData, String> appointment_col_name;

    @FXML
    private TableColumn<?, ?> appointment_col_status;

    @FXML
    private TextField appointment_description;

    @FXML
    private TextField appointment_diagnosis;

    @FXML
    private AnchorPane appointment_form;

    @FXML
    private AnchorPane appointment_form1;

    @FXML
    private ComboBox<?> appointment_gender;

    @FXML
    private TextField appointment_mobileNumber;

    @FXML
    private TextField appointment_name;

    @FXML
    private TextField appointment_status;

    @FXML
    private TableView<AppointmentData> appointment_tableView;

    @FXML
    private TextField appointment_treatment;

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
    private Button patient_btn;

    @FXML
    private AnchorPane patients_form;

    @FXML
    private Button profile_btn;

    @FXML
    private Circle top_profile;

    @FXML
    private Label top_username;

//  DATABASE TOOLS
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public ObservableList<AppointmentData> appointmentGetData() {

        ObservableList<AppointmentData> listData = FXCollections.observableArrayList();

        String sql = " SELECT * FROM appointment WHERE date_delete is NULL";
        connect = Database.connectDB();
        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            AppointmentData appData;
            while (result.next()) {

                appData = new AppointmentData(result.getInt("appointment_id"),
                         result.getString("name"), result.getString("gender"),
                         result.getLong("mobile_number"), result.getString("description"),
                         result.getDate("date"), result.getDate("date_modify"),
                         result.getDate("date_delete"), result.getString("status"));

                listData.add(appData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    public ObservableList<AppointmentData> appoinmentListData;

    public void appointmentShowData() {
        appoinmentListData = appointmentGetData();
        Appointment_col_appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointment_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        appointment_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        appointment_col_contactNumber.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        appointment_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointment_col_date.setCellValueFactory(new PropertyValueFactory<>("data"));
        appointment_col_dateModify.setCellValueFactory(new PropertyValueFactory<>("dataModify"));
        appointment_col_dateDelete.setCellValueFactory(new PropertyValueFactory<>("dateDelete"));
        appointment_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        appointment_tableView.setItems(appoinmentListData);

    }

    public void displayAdminIDNumberName() {

        String name = Data.doctor_name;
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        nav_username.setText(name);
        nav_adminID.setText(Data.doctor_id);
        top_username.setText(name);

    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            patients_form.setVisible(false);
            appointment_form.setVisible(false);
        } else if (event.getSource() == patient_btn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(true);
            appointment_form.setVisible(false);
        } else if (event.getSource() == appointments_btn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(false);
            appointment_form.setVisible(true);

        }
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

        displayAdminIDNumberName();
        runTime();
        appointmentShowData();

    }

}
