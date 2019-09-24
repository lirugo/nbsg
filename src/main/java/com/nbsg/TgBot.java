package com.nbsg;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

public class TgBot extends TelegramLongPollingBot {

    private String chatId = "334259691";

    @Override
    public void onUpdateReceived(Update update) {
    }

    private String emodjiHandUp = "\uD83D\uDC48";
    public synchronized void sendNotification(List<Notification> ns) {
        String message = "Total information \n";
        for(Notification n:ns){
            message += n.getDateTime() + " " + (n.getTitle().equals("") ? "FREE " + emodjiHandUp : n.getTitle()) + "\n";
        }


        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "NBSG";
    }

    @Override
    public String getBotToken() {
        return "707531464:AAF92NGUow7JSeBwgYab1GiHxOGg3O5xYFo";
    }
}
