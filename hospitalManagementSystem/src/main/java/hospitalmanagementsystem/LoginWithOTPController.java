

import com.hms.server.Request;
import com.hms.server.Response;
import com.hms.server.ResponseStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class LoginWithOTPController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private RadioButton emailRadio;
    @FXML private RadioButton smsRadio;
    @FXML private TextField contactField;
    @FXML private TextField otpField;
    @FXML private Button requestOTPButton;
    @FXML private Button verifyOTPButton;
    @FXML private Label messageLabel;
    
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 81;
    private String sessionId;

    @FXML
    private void handleRequestOTP(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String contact = contactField.getText();
        
        if (username.isEmpty() || password.isEmpty() || contact.isEmpty()) {
            showMessage("Please fill in all fields", true);
            return;
        }

        try {
            // First verify credentials
            Request loginRequest = new Request(RequestType.LOGIN);
            loginRequest.addParameter("username", username);
            loginRequest.addParameter("password", password);
            
            Response loginResponse = sendRequest(loginRequest);
            if (loginResponse.getStatus() == ResponseStatus.SUCCESS) {
                sessionId = (String) loginResponse.getParameter("sessionId");
                
                // Now request OTP
                Request otpRequest = new Request(RequestType.GENERATE_OTP);
                if (emailRadio.isSelected()) {
                    otpRequest.addParameter("email", contact);
                } else {
                    otpRequest.addParameter("phoneNumber", contact);
                }
                
                Response otpResponse = sendRequest(otpRequest);
                if (otpResponse.getStatus() == ResponseStatus.SUCCESS) {
                    showMessage("OTP sent successfully! Please check your " + 
                              (emailRadio.isSelected() ? "email" : "phone"), false);
                    otpField.setVisible(true);
                    verifyOTPButton.setVisible(true);
                } else {
                    showMessage("Failed to send OTP: " + otpResponse.getMessage(), true);
                }
            } else {
                showMessage("Invalid credentials", true);
            }
        } catch (Exception e) {
            showMessage("Error: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleVerifyOTP(ActionEvent event) {
        String otp = otpField.getText();
        String contact = contactField.getText();
        
        if (otp.isEmpty()) {
            showMessage("Please enter OTP", true);
            return;
        }

        try {
            Request verifyRequest = new Request(RequestType.VERIFY_OTP);
            verifyRequest.addParameter("recipient", contact);
            verifyRequest.addParameter("otp", otp);
            
            Response response = sendRequest(verifyRequest);
            if (response.getStatus() == ResponseStatus.SUCCESS) {
                showMessage("Login successful!", false);
                // TODO: Navigate to main application window
            } else {
                showMessage("Invalid OTP", true);
            }
        } catch (Exception e) {
            showMessage("Error: " + e.getMessage(), true);
        }
    }

    private Response sendRequest(Request request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            
            out.writeObject(request);
            return (Response) in.readObject();
        }
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + (isError ? "red" : "green"));
    }
} 