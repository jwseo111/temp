package com.itsm.dranswer.apis;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public String requestGetWithString(String reqUrl) throws IOException, ParseException, IllegalAccessException {
        HttpURLConnection conn = null;
        JSONObject responseJson = null;

        conn = getConnection(reqUrl);

        if(isSuccess(conn.getResponseCode())){
            responseJson = convertJson(getResponse(conn.getInputStream()));
            return responseJson.get("response").toString();
        }else{
            responseJson = convertJson(getResponse(conn.getErrorStream()));
            throw new IllegalAccessException(((JSONObject)responseJson.get("error")).get("message").toString());
        }

    }

    private boolean isSuccess(int responseCode){
        if (responseCode == 200 ) {
            return true;
        }
        return false;
    }

    private HttpURLConnection getConnection(String reqUrl) throws IOException {
        HttpURLConnection conn = null;

        try {
            URL url = null;
            url = new URL(reqUrl);

            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setRequestMethod("GET");

        } catch (MalformedURLException e) {
            throw new MalformedURLException("invalid url");
        } catch (IOException e) {
            throw new IOException("connection refuse");
        }

        return conn;
    }

    private String getResponse(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                throw new IOException("Error while reading");
            }
            sb.append(line);
        }

        return sb.toString();
    }

    private JSONObject convertJson(String jsonString) throws ParseException {

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(jsonString);

        return responseJson;
    }
}
