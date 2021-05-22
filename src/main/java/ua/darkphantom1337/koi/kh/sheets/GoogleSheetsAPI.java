package ua.darkphantom1337.koi.kh.sheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GoogleSheetsAPI {

    private String APPLICATION_NAME = "DarkSheetsAPI";
    private JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private String TOKENS_DIRECTORY_PATH = "tokens";

    private List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private String CREDENTIALS_FILE_PATH = "/credentials.json";
    private Sheets sheets_service;
    private String sheetID = "1HYpa09S3DUQUko8Z6hqWXHMHwqK0okZ4A5otCifDe_U";
    public GoogleSheetsAPI api;

    public GoogleSheetsAPI(){
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            sheets_service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME).build();
            api = this;
        } catch (Exception e){
            System.out.println("[DarkSheetsAPI] -> Error in initialization sheets.");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws Exception {
        InputStream in = GoogleSheetsAPI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null)
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String[] args) throws Exception {
        GoogleSheetsAPI api = new GoogleSheetsAPI();
        System.out.println("Get cell A5 value: " + api.getSheetValue("A5"));
        System.out.println("Update cell A5 value result: " + api.setSheetValue("A5", "DarkPhantom1337 лучший"));
        System.out.println("Get cell A5 value: " + api.getSheetValue("A5"));
    }

    public String getSheetValue(String cell){
        try {
            ValueRange response = sheets_service.spreadsheets().values()
                    .get(sheetID, cell).execute();
            List<List<Object>> values = response.getValues();
            if (values == null || values.isEmpty())
                return "NoDataFound";
             else
                return (String) values.get(0).get(0);
        } catch (Exception e){
            System.out.println("[DarkSheetsAPI] -> Error in getting sheet values. Cell: " + cell);
            e.printStackTrace();
        }
        return "NoDataFound";
    }

    public UpdateValuesResponse setSheetValue(String cell, String value) {
        try {
            ValueRange body = new ValueRange().setValues(Arrays.asList(Arrays.asList(value)));
            UpdateValuesResponse result = sheets_service.spreadsheets().values().update(sheetID, cell, body)
                    .setValueInputOption("RAW").execute();
            return result;
        } catch (Exception e) {
            System.out.println("[DarkSheetsAPI] -> Error in updating sheets values.");
            e.printStackTrace();
            return null;
        }
    }

}