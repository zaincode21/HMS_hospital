

import com.hms.server.Request;
import com.hms.server.Response;
import com.hms.server.ResponseStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;

public class ExportReportController {
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private RadioButton pdfRadio;
    @FXML private RadioButton excelRadio;
    @FXML private RadioButton csvRadio;
    @FXML private TableView<Map<String, Object>> previewTable;
    @FXML private Label messageLabel;
    
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 81;
    private List<Map<String, Object>> reportData;
    private String[] headers;

    @FXML
    public void initialize() {
        reportTypeCombo.setItems(FXCollections.observableArrayList(
            "Patient Records",
            "Doctor Appointments",
            "Financial Summary"
        ));
    }

    @FXML
    private void handlePreview(ActionEvent event) {
        if (!validateInputs()) return;
        
        try {
            fetchReportData();
            updatePreviewTable();
        } catch (Exception e) {
            showMessage("Error loading preview: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleExport(ActionEvent event) {
        if (!validateInputs()) return;
        
        try {
            if (reportData == null) {
                fetchReportData();
            }
            
            String format = getSelectedFormat();
            Request exportRequest = new Request(RequestType.EXPORT);
            exportRequest.addParameter("format", format);
            exportRequest.addParameter("data", reportData);
            exportRequest.addParameter("headers", headers);
            
            Response response = sendRequest(exportRequest);
            if (response.getStatus() == ResponseStatus.SUCCESS) {
                byte[] exportedData = (byte[]) response.getParameter("data");
                String fileName = (String) response.getParameter("fileName");
                
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName(fileName);
                File file = fileChooser.showSaveDialog(null);
                
                if (file != null) {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(exportedData);
                        showMessage("Report exported successfully!", false);
                    }
                }
            } else {
                showMessage("Export failed: " + response.getMessage(), true);
            }
        } catch (Exception e) {
            showMessage("Error exporting report: " + e.getMessage(), true);
        }
    }

    private boolean validateInputs() {
        if (reportTypeCombo.getValue() == null) {
            showMessage("Please select a report type", true);
            return false;
        }
        if (startDate.getValue() == null || endDate.getValue() == null) {
            showMessage("Please select date range", true);
            return false;
        }
        if (startDate.getValue().isAfter(endDate.getValue())) {
            showMessage("Start date must be before end date", true);
            return false;
        }
        return true;
    }

    private void fetchReportData() throws IOException, ClassNotFoundException {
        Request request = new Request(RequestType.SEARCH);
        request.addParameter("reportType", reportTypeCombo.getValue());
        request.addParameter("startDate", startDate.getValue());
        request.addParameter("endDate", endDate.getValue());
        
        Response response = sendRequest(request);
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            reportData = (List<Map<String, Object>>) response.getParameter("data");
            headers = (String[]) response.getParameter("headers");
        } else {
            throw new IOException("Failed to fetch report data: " + response.getMessage());
        }
    }

    private void updatePreviewTable() {
        previewTable.getColumns().clear();
        
        for (String header : headers) {
            TableColumn<Map<String, Object>, Object> column = new TableColumn<>(header);
            column.setCellValueFactory(new PropertyValueFactory<>(header));
            previewTable.getColumns().add(column);
        }
        
        ObservableList<Map<String, Object>> items = FXCollections.observableArrayList(reportData);
        previewTable.setItems(items);
    }

    private String getSelectedFormat() {
        if (pdfRadio.isSelected()) return "pdf";
        if (excelRadio.isSelected()) return "excel";
        return "csv";
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