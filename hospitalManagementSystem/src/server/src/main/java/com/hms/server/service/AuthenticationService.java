package com.hms.server.service;

import com.hms.server.Request;
import com.hms.server.Response;
import com.hms.server.ResponseStatus;
import com.hms.server.entity.User;
import com.hms.server.util.HibernateUtil;
import org.hibernate.Session;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationService {
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final Map<String, String> sessionStore = new ConcurrentHashMap<>();
    private static final int OTP_VALIDITY_MINUTES = 5;

    public Response handleLogin(Request request) {
        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();

            if (user != null && user.getPassword().equals(password)) {
                String sessionId = generateSessionId();
                sessionStore.put(sessionId, username);
                return new Response(ResponseStatus.SUCCESS, "Login successful")
                        .addParameter("sessionId", sessionId);
            }
        }

        return new Response(ResponseStatus.ERROR, "Invalid credentials");
    }

    public Response generateOTP(Request request) {
        String email = (String) request.getParameter("email");
        String phoneNumber = (String) request.getParameter("phoneNumber");

        String otp = generateRandomOTP();
        String recipient = email != null ? email : phoneNumber;

        if (email != null) {
            sendEmailOTP(email, otp);
        } else if (phoneNumber != null) {
            sendSMSOTP(phoneNumber, otp);
        }

        otpStore.put(recipient, otp);
        
        // Schedule OTP removal after validity period
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                otpStore.remove(recipient);
            }
        }, OTP_VALIDITY_MINUTES * 60 * 1000);

        return new Response(ResponseStatus.SUCCESS, "OTP sent successfully");
    }

    public Response verifyOTP(Request request) {
        String recipient = (String) request.getParameter("recipient");
        String otp = (String) request.getParameter("otp");

        String storedOTP = otpStore.get(recipient);
        if (storedOTP != null && storedOTP.equals(otp)) {
            otpStore.remove(recipient);
            return new Response(ResponseStatus.SUCCESS, "OTP verified successfully");
        }

        return new Response(ResponseStatus.ERROR, "Invalid OTP");
    }

    private String generateRandomOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    private void sendEmailOTP(String email, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        javax.mail.Session mailSession = javax.mail.Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your.email@gmail.com", "your-app-specific-password");
            }
        });

        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("your.email@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("HMS Login OTP");
            message.setText("Your OTP for HMS login is: " + otp);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendSMSOTP(String phoneNumber, String otp) {
        // Implement SMS sending logic using a third-party SMS service
        // This is a placeholder - you'll need to implement the actual SMS sending logic
        System.out.println("Sending SMS OTP " + otp + " to " + phoneNumber);
    }

    public boolean validateSession(String sessionId) {
        return sessionStore.containsKey(sessionId);
    }

    public void invalidateSession(String sessionId) {
        sessionStore.remove(sessionId);
    }
} 