package ua.darkphantom1337.koi.kh;

import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import ua.darkphantom1337.koi.kh.database.DataBase;
import ua.darkphantom1337.koi.kh.entitys.User;
import ua.darkphantom1337.koi.kh.entitys.mails.ContentType;
import ua.darkphantom1337.koi.kh.entitys.mails.Mail;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BirthdayThread extends Thread {

    public void run() {
        this.setName("Birthday");
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss - EEE", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance(), now = Calendar.getInstance();
        calendar.setTime(new Date());
        now.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Date startDate = calendar.getTime();
        if (!now.before(calendar)){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            startDate  = calendar.getTime();
        }
        //startDate = new Date();
        Bot.bot.info("Congratulations will be launched:  " + dateFormat.format(startDate));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Bot.bot.info("Starting congratulations...");
                    String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                    Mail mail = new Mail(0L);
                    ContentType contentType = mail.getContentType();
                    File photo = null;
                    SendPhoto sendPhoto = null;
                    SendVideo sendVideo = null;
                    if (contentType.equals(ContentType.TEXT_AND_IMAGE)){
                        Bot.bot.saveDocument("mail_" + mail.getMailID() + "_image.png", Bot.bot.execute(new GetFile().setFileId(mail.getMailFileID())).getFileUrl(Bot.bot.getBotToken()));
                        photo = new File("mail_" + mail.getMailID() + "_image.png");
                        sendPhoto = new SendPhoto().setNewPhoto(photo);
                        sendPhoto.setCaption(mail.getMailMessage());
                    }
                    if (contentType.equals(ContentType.TEXT_AND_VIDEO)){
                        Bot.bot.saveDocument("mail_" + mail.getMailID() + "_video.mp4", Bot.bot.execute(new GetFile().setFileId(mail.getMailFileID())).getFileUrl(Bot.bot.getBotToken()));
                        photo = new File("mail_" + mail.getMailID() + "_video.mp4");
                        sendVideo = new SendVideo().setNewVideo(photo);
                        sendVideo.setCaption(mail.getMailMessage());
                    }
                    int br = 0;
                    for (Long userID : DataBase.getAllUserId()){
                        try{
                            User user = new User(userID,false);
                            if (nowDate.equals(user.getBirthday())){
                                Bot.bot.info("Today is " + user.getUserPhone() + " user's birthday!");
                                if (contentType.equals(ContentType.TEXT)){
                                    user.sendMessage(mail.getMailMessage());
                                    br++;
                                    continue;
                                } else if (contentType.equals(ContentType.TEXT_AND_IMAGE)){
                                    sendPhoto.setChatId(user.getTID());
                                    Bot.bot.sendPhoto(sendPhoto);
                                    br++;
                                    continue;
                                } else if (contentType.equals(ContentType.TEXT_AND_VIDEO)){
                                    sendVideo.setChatId(user.getTID());
                                    Bot.bot.sendVideo(sendVideo);
                                    br++;
                                    continue;
                                }
                            }
                        } catch (Exception e){

                        }
                    }
                    photo.delete();
                    Bot.bot.info("Congratulations end. Birthted: " + br);
                } catch (Exception ignored) {
                    Bot.bot.error("Error in Birthday THREAD");
                }
            }
        }, startDate, 86400000L);
    }



}