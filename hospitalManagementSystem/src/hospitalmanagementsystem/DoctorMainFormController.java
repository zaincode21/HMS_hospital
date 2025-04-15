/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmanagementsystem;

import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

/**
 *
 * @author serge
 */
public class DoctorMainFormController  implements Initializable {
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
    private TableColumn<?, ?> appointment_col_action;

    @FXML
    private TableColumn<?, ?> appointment_col_contactNumber;

    @FXML
    private TableColumn<?, ?> appointment_col_date;

    @FXML
    private TableColumn<?, ?> appointment_col_dateDelete;

    @FXML
    private TableColumn<?, ?> appointment_col_dateModify;

    @FXML
    private TableColumn<?, ?> appointment_col_description;

    @FXML
    private TableColumn<?, ?> appointment_col_gender;

    @FXML
    private TableColumn<?, ?> appointment_col_name;

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
    private TableView<?> appointment_tableView;

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
    
  
    
    public void switchForm(ActionEvent event){
        if(event.getSource() == dashboard_btn){
            dashboard_form.setVisible(true);
            patients_form.setVisible(false);
            appointment_form.setVisible(false);     
        }else if(event.getSource() == patient_btn){
            dashboard_form.setVisible(false);
            patients_form.setVisible(true);
            appointment_form.setVisible(false);    
        }else if(event.getSource() == appointments_btn){
            dashboard_form.setVisible(false);
            patients_form.setVisible(false);
            appointment_form.setVisible(true);
            
        }
    }
      @Override
    public void initialize(URL location, ResourceBundle resources) {
     
        
    }
    
}
