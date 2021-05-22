package ua.darkphantom1337.koi.kh;

import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.darkphantom1337.koi.kh.entitys.Mail;
import ua.darkphantom1337.koi.kh.entitys.User;
import ua.darkphantom1337.koi.kh.entitys.mails.ContentType;
import ua.darkphantom1337.koi.kh.entitys.mails.FileType;
import ua.darkphantom1337.koi.kh.entitys.mails.MailStatus;
import ua.darkphantom1337.koi.kh.entitys.mails.SendType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MailingsThread extends Thread {

    public void run() {
//        Bot.bot.info("Starting mailings thread...");
        this.setName("Mailings");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (Long mailID : new Mail(0).getMailingsIDWhere(MailStatus.READY_TO_RUN)) {
                        Mail mail = new Mail(mailID);
                        if (mail.getSendType().equals(SendType.NOW)) {
                            if (sendMail(mail)) continue;
                        } else {
                            Date now = new Date(), sendDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(mail.getSendDate());
                            int seconds = (int) (now.getTime() - sendDate.getTime() / 1000);
                            if (seconds >= 0) {
                                if (sendMail(mail)) continue;
                            } else continue;
                        }
                    }
                } catch (Exception ignored) {
                    Bot.bot.error("Error in MAILING THREAD");
                }
            }
        }, 1000, 60000);
    }


    private Boolean sendMail(Mail mail) throws TelegramApiException {
        ContentType contentType = mail.getContentType();
        List<Long> recipients = mail.getAllRecipientsID();
        if (recipients.isEmpty())
            recipients = DataBase.getAllUserId();
        if (contentType.equals(ContentType.TEXT)) {
            for (Long uid : recipients) {
                try {
                    User user = new User(uid, false);
                    mail.addMessageID((long) user.sendMessage(mail.getMailMessage()));
                } catch (Exception ignored) {
                }
            }
            mail.setMailStatus(MailStatus.COMPLETED);
            return true;
        }
        if (contentType.equals(ContentType.IMAGE) || contentType.equals(ContentType.TEXT_AND_IMAGE)) {
            Bot.bot.saveDocument("mail_" + mail.getMailID() + "_image.png", Bot.bot.execute(new GetFile().setFileId(mail.getMailFileID())).getFileUrl(Bot.bot.getBotToken()));
            File photo = new File("mail_" + mail.getMailID() + "_image.png");
            SendPhoto sendPhoto = new SendPhoto().setNewPhoto(photo);
            if (contentType.equals(ContentType.TEXT_AND_IMAGE))
                sendPhoto.setCaption(mail.getMailMessage());
            for (Long uid : recipients) {
                try {
                    User user = new User(uid, false);
                    sendPhoto.setChatId(user.getTID());
                    mail.addMessageID((long) Bot.bot.sendPhoto(sendPhoto).getMessageId());
                } catch (Exception ignored) {
                }
            }
            photo.delete();
            mail.setMailStatus(MailStatus.COMPLETED);
            return true;
        }
        if (contentType.equals(ContentType.FILE) || contentType.equals(ContentType.TEXT_AND_FILE)) {
            String filename = "mail_" + mail.getMailID() + "_file" + FileType.getMimeType(mail.getFileType());
            Bot.bot.saveDocument(filename, Bot.bot.execute(new GetFile().setFileId(mail.getMailFileID())).getFileUrl(Bot.bot.getBotToken()));
            File file = new File(filename);
            SendDocument sendFile = new SendDocument().setNewDocument(file);
            if (contentType.equals(ContentType.TEXT_AND_FILE))
                sendFile.setCaption(mail.getMailMessage());
            for (Long uid : recipients) {
                try {
                    User user = new User(uid, false);
                    sendFile.setChatId(user.getTID());
                    mail.addMessageID((long) Bot.bot.sendDocument(sendFile).getMessageId());
                } catch (Exception ignored) {
                }
            }
            file.delete();
            mail.setMailStatus(MailStatus.COMPLETED);
            return true;
        }
        return false;
    }

}