package com.hms.server.service;

import com.hms.server.Request;
import com.hms.server.Response;
import com.hms.server.ResponseStatus;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class ExportService {
    
    public Response handleExport(Request request) {
        String format = (String) request.getParameter("format");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> data = (List<Map<String, Object>>) request.getParameter("data");
        String[] headers = (String[]) request.getParameter("headers");

        try {
            byte[] exportedData;
            String fileName;

            switch (format.toLowerCase()) {
                case "pdf":
                    exportedData = exportToPDF(data, headers);
                    fileName = "report.pdf";
                    break;
                case "excel":
                    exportedData = exportToExcel(data, headers);
                    fileName = "report.xlsx";
                    break;
                case "csv":
                    exportedData = exportToCSV(data, headers);
                    fileName = "report.csv";
                    break;
                default:
                    return new Response(ResponseStatus.ERROR, "Unsupported export format");
            }

            return new Response(ResponseStatus.SUCCESS, "Export successful")
                    .addParameter("fileName", fileName)
                    .addParameter("data", exportedData);

        } catch (Exception e) {
            return new Response(ResponseStatus.ERROR, "Export failed: " + e.getMessage());
        }
    }

    private byte[] exportToPDF(List<Map<String, Object>> data, String[] headers) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();
        
        // Add title
        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Hospital Management System Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Create table
        com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(headers.length);
        table.setWidthPercentage(100);

        // Add headers
        for (String header : headers) {
            table.addCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        }

        // Add data
        for (Map<String, Object> row : data) {
            for (String header : headers) {
                table.addCell(String.valueOf(row.get(header)));
            }
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }

    private byte[] exportToExcel(List<Map<String, Object>> data, String[] headers) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");

        // Create header row
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        for (Map<String, Object> row : data) {
            Row dataRow = sheet.createRow(rowNum++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = dataRow.createCell(i);
                cell.setCellValue(String.valueOf(row.get(headers[i])));
            }
        }

        // Autosize columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    private byte[] exportToCSV(List<Map<String, Object>> data, String[] headers) throws IOException {
        StringWriter writer = new StringWriter();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers))) {
            for (Map<String, Object> row : data) {
            Object[] rowData = new Object[headers.length];
            for (int i = 0; i < headers.length; i++) {
                rowData[i] = row.get(headers[i]);
            }
            csvPrinter.printRecord(rowData);
        }

                    csvPrinter.flush();
            return writer.toString().getBytes();
        }
    }
} 