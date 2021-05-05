package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.api.objects.Document;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.entitys.User;

import java.util.*;

public class UserGlobalHandler {

    private Bot bot = Bot.bot;

    public Boolean handleUserMessage(User user, Message msg){
        Long chatID = msg.getChatId();
        Integer messageID = msg.getMessageId();
        bot.u.logAsyncInfo(user, msg);
        if (msg.hasText()){
            return this.handleUserTextMessage(user, chatID, messageID, msg.getText());
        }
        if (msg.hasDocument()){
            return this.handleUserDocumentMessage(user, chatID, messageID, msg.getDocument());
        }
        if (msg.hasPhoto()){
            return this.handleUserPhotoMessage(user, chatID, messageID, msg.getPhoto());
        }
        return false;
    }

    private Boolean handleUserTextMessage(User user, Long chatID, Integer messageID, String text){

        return false;
    }

    private Boolean handleUserDocumentMessage(User user, Long chatID, Integer messageID, Document document){
        return false;
    }

    private Boolean handleUserPhotoMessage(User user, Long chatID, Integer messageID, List<PhotoSize> photo){
        return false;
    }

}
