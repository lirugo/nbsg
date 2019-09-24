package com.nbsg;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        URL url = null;
        HttpURLConnection con = null;
        int status = 0;
        //Send GET
        try {
            url = new URL("https://sheets.googleapis.com/v4/spreadsheets/1t-BwSgwfRCykyCib9FnNgRJzTSgS0-7jlwVvVPbtSSk/values/Sheet?valueRenderOption=FORMATTED_VALUE&key=AIzaSyC7JCEtRBQaxgKrG91S2WpXceZXOZjZnOM");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            status = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Read response
        List<Notification> notifList = null;
        if(status == 200){
            JsonArray dates;
            StringBuffer content = null;
            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Parse response
            JsonObject response = new JsonParser().parse(content.toString()).getAsJsonObject();
            dates = response.getAsJsonArray("values");

            //Create Notif list
            notifList = new ArrayList<>();
            for(JsonElement row : dates){
                JsonArray obj = row.getAsJsonArray();
                //Parse date
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(obj.get(1).getAsString());
                } catch (ParseException e) {
                    //It is no date value
                }
                if(date != null){
                    notifList.add(new Notification(obj.get(1).getAsString(), obj.get(0).getAsString()));
                }
            }
        }

        //Close connection
        con.disconnect();

        //Register TG Bot
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        TgBot tgBot = new TgBot();
        try {
            telegramBotsApi.registerBot(tgBot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

        //Send notif to TG bot
                tgBot.sendNotification(notifList);

    }
}