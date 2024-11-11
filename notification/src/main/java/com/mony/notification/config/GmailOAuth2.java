package com.mony.notification.config;

import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GmailOAuth2 {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "monymail";

    public static Gmail getGmailService() throws IOException, GeneralSecurityException {
        // Carregar as credenciais OAuth2 diretamente do arquivo JSON da Service Account
        InputStream in = GmailOAuth2.class.getClassLoader().getResourceAsStream("credentials.json");
        if (in == null) {
            throw new FileNotFoundException("Arquivo de credenciais não encontrado.");
        }

        // Criar as credenciais usando o arquivo JSON da Service Account
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton(GmailScopes.GMAIL_SEND));

        // Criação do transporte HTTP
        HttpTransport transport = new NetHttpTransport();

        // Adaptar as credenciais para o tipo necessário
        HttpCredentialsAdapter credentialsAdapter = new HttpCredentialsAdapter(credentials);

        // Criação do cliente Gmail com as credenciais
        return new Gmail.Builder(transport, JSON_FACTORY, credentialsAdapter)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
