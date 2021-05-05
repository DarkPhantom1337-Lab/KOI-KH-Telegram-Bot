package ua.darkphantom1337.koi.kh;

import org.telegram.telegrambots.api.methods.ActionType;
import org.telegram.telegrambots.api.methods.send.SendChatAction;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.darkphantom1337.koi.kh.entitys.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Utils {

    public String getTime(String format){ // hh:mm:ss - hh часы - mm - минуты - ss - секунды
        return new SimpleDateFormat(format).format(new Date());
    }

    public String getDate(String format){ // dd/MM/YYYY - dd - дни - MM - месяц - YYYY - год
        return new SimpleDateFormat(format).format(new Date());
    }

    public String getTime(String format, Date date){ // hh:mm:ss
        return new SimpleDateFormat(format).format(date);
    }

    public String getDate(String format, Date date){ // dd/MM/YYYY
        return new SimpleDateFormat(format).format(date);
    }

    public String getUserPhone(Message msg){
        if(msg.getContact() != null)
            return msg.getContact().getPhoneNumber();
        else return null;
    }

    public String getUserName(Message msg){
        if(msg.getContact() != null)
            return msg.getContact().getFirstName();
        else return null;
    }

    public Integer getUserID(Message msg){
        if(msg.getContact() != null)
            return msg.getContact().getUserID();
        else return 0000;
    }

    public static Boolean isYesContact(Message msg){
        if(msg.getContact() != null)
            return true;
        else return false;
    }

    public String correctStr(String str){
        return str.replaceAll("[^A-Za-zА-Яа-я0-9 ]","");
    }

    public String longToString(List<Long> list, String splitter) { // s.s.s.s.s.s
        String s = "";
        int i = 1;
        for (Long sl : list) {
            s += sl + (i != list.size() ? splitter : "");
            i++;
        }
        return s;
    }

    public String stringToString(List<String> list, String splitter) { // s.s.s.s.s.s
        String s = "";
        int i = 1;
        for (String sl : list) {
            s += sl + (i != list.size() ? splitter : "");
            i++;
        }
        return s;
    }

    public String objectToString(List<?> list, String splitter) { // s.s.s.s.s.s
        String s = "";
        int i = 1;
        for (Object sl : list) {
            s += sl.toString() + (i != list.size() ? splitter : "");
            i++;
        }
        return s;
    }

    public void logAsyncInfo(User user, Message msg){
        CompletableFuture.runAsync(() -> {
            System.out.println("\u001B[33m[KOI-KH] [" + getDate("dd/MM/YYYY") + "] [" + getTime("HH:mm:ss") + "] \u001B[32m[INFO] [MESSAGE] \u001B[37m-> UID: " + user.getUID() + " TID: " + user.getTID() + " CID: " + msg.getChatId() + " MID: " + msg.getMessageId() + " Type: " + getMessageType(msg) + " Data: " + getMessageData(msg));
        });
    }

    public void logAsyncCallback(User user, Message msg, String data){
        CompletableFuture.runAsync(() -> {
            System.out.println("\u001B[33m[KOI-KH] [" + getDate("dd/MM/YYYY") + "] [" + getTime("HH:mm:ss") + "] \u001B[32m[INFO] [CALLBACK] \u001B[37m-> UID: " + user.getUID() + " TID: " + user.getTID() + " CID: " + msg.getChatId() + " MID: " + msg.getMessageId() + " Type: " + getMessageType(msg) + " MsgText: " + getMessageData(msg) + " Data: " + data);
        });
    }

    public String getMessageType(Message msg){
        if (msg.hasText())
            return "TEXT";
        if (msg.hasDocument())
            return "DOCUMENT";
        if (msg.hasPhoto())
            return "PHOTO";
        if (msg.hasLocation())
            return "LOCATION";
        if (msg.hasInvoice())
            return "INVOICE";
        return "UNDEFINED";
    }

    public String getMessageData(Message msg){
        if (msg.hasText())
            return msg.getText();
        if (msg.hasDocument())
            return msg.getDocument().getFileName() + "|"  + msg.getDocument().getMimeType() + "|" + msg.getDocument().getFileId();
        if (msg.hasPhoto())
            return "UserPhoto";
        if (msg.hasLocation())
            return msg.getLocation().getLatitude() + "|" + msg.getLocation().getLongitude();
        if (msg.hasInvoice())
            return msg.getInvoice().getTitle() + "|" + msg.getInvoice().getCurrency();
        return "UNDEFINED";
    }

    public void changeBotAction(Long chatID,ActionType actionType){
        try {
            Bot.bot.execute(new SendChatAction().setChatId(chatID).setAction(actionType));
        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }

}
