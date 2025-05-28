package hospitalmanagementsystem;

import com.hms.server.Request;
import com.hms.server.Response;
import com.hms.server.ResponseStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label messageLabel;
    
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 81;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields", true);
            return;
        }

        try {
            Request loginRequest = new Request(RequestType.LOGIN);
            loginRequest.addParameter("username", username);
            loginRequest.addParameter("password", password);
            
            Response loginResponse = sendRequest(loginRequest);
            if (loginResponse.getStatus() == ResponseStatus.SUCCESS) {
                showMessage("Login successful!", false);
                // TODO: Navigate to main application window
            } else {
                showMessage("Invalid credentials", true);
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