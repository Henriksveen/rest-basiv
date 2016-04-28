///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.basiv.server.Services;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import java.util.Arrays;
//
///**
// *
// * @author Ivar Østby
// */
//public class GoogleTest {
//    private String CLIENT_ID = "1057885418811-4f04mik9v1t65af68hutbtdd3heb4eit.apps.googleusercontent.com";
//    
//    
//    void yolo() {
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jsonFactory)
//                .setAudience(Arrays.asList(CLIENT_ID))
//                // If you retrieved the token on Android using the Play Services 8.3 API or newer, set
//                // the issuer to "https://accounts.google.com". Otherwise, set the issuer to
//                // "accounts.google.com". If you need to verify tokens from multiple sources, build
//                // a GoogleIdTokenVerifier for each issuer and try them both.
//                .setIssuer("https://accounts.google.com")
//                .build();
//
//// (Receive idTokenString by HTTPS POST)
//        GoogleIdToken idToken = verifier.verify(idTokenString);
//
//        if (idToken != null) {
//            Payload payload = idToken.getPayload();
//
//            // Print user identifier
//            String userId = payload.getSubject();
//            System.out.println("User ID: " + userId);
//
//            // Get profile information from payload
//            String email = payload.getEmail();
//            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//            String name = (String) payload.get("name");
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");
//
//        // Use or store profile information
//            // ...
//        } else {
//            System.out.println("Invalid ID token.");
//        }
//    }
//}
