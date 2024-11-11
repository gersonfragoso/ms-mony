package com.mony.notification.config;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GmailOAuth2 {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "monymail";

    public static Gmail getGmailService() throws IOException, GeneralSecurityException {
        // Carregar as credenciais OAuth2
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/java/com/mony/notification/config/credentials/credentials.json"))
                .createScoped(Collections.singleton(GmailScopes.GMAIL_SEND));

        // Criação do serviço Gmail
        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}

