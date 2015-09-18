package com.example.realnapster.jsonparser;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ServiceHandler {


    public static String GETResult(String urlString) {
        InputStream inputStream = null;
        String result = "";

        URL url = null;
        try {
            //create URL
            url = new URL(urlString);
            //create HttpConnection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //define request method POST/GET
            //con.setRequestMethod("GET");

            //read the response
            inputStream = new BufferedInputStream(con.getInputStream());

            if (inputStream != null)
                result = convertToString(inputStream);
            else
                result = "Did not work!";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Result Received",result);
        return result;

    }

    private static String convertToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = reader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
