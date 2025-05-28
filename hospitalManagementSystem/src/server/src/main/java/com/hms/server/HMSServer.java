package com.hms.server;

import com.hms.server.service.AuthenticationService;
import com.hms.server.service.ExportService;
import com.hms.server.util.HibernateUtil;
import org.hibernate.Session;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HMSServer {
    private static final int PORT_1 = 81;
    private static final int PORT_2 = 6000;
    private final ExecutorService executorService;
    private final AuthenticationService authService;
    private final ExportService exportService;

    public HMSServer() {
        this.executorService = Executors.newFixedThreadPool(10);
        this.authService = new AuthenticationService();
        this.exportService = new ExportService();
    }

    public void start() {
        // Start server on port 81
        new Thread(() -> startServer(PORT_1)).start();
        
        // Start server on port 6000
        new Thread(() -> startServer(PORT_2)).start();
    }

    private void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server failed to start on port " + port + ": " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            Request request = (Request) in.readObject();
            Response response;

            switch (request.getType()) {
                case LOGIN:
                    response = authService.handleLogin(request);
                    break;
                case GENERATE_OTP:
                    response = authService.generateOTP(request);
                    break;
                case VERIFY_OTP:
                    response = authService.verifyOTP(request);
                    break;
                case EXPORT:
                    response = exportService.handleExport(request);
                    break;
                default:
                    response = new Response(ResponseStatus.ERROR, "Unknown request type");
            }

            out.writeObject(response);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Initialize Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("Hibernate initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize Hibernate: " + e.getMessage());
            return;
        }

        // Start the server
        new HMSServer().start();
    }
} 