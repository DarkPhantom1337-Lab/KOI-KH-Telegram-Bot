package ua.darkphantom1337.koi.kh.database;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.entitys.mails.ContentType;
import ua.darkphantom1337.koi.kh.entitys.mails.FileType;
import ua.darkphantom1337.koi.kh.entitys.mails.MailStatus;
import ua.darkphantom1337.koi.kh.entitys.mails.SendType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MailingsTable extends DarkTable {

    public MailingsTable(Integer courierID) {
        super("mailings", "mailID", courierID.longValue());
    }

    public MailingsTable(Long courierID) {
        super("mailings", "mailID", courierID.longValue());
    }

    public MailingsTable(String courierID) {
        super("mailings", "mailID", Long.parseLong(courierID));
    }

    public Long getMailID() {
        return getFieldID();
    }

    public String getMailMessage() {
        return getString("message");
    }

    public void setMailMessage(String message) {
        setString("message", message);
    }

    public String getMailFileID() {
        return getString("file_id");
    }

    public void setMailFileID(String file_id) {
        setString("file_id", file_id);
    }

    public FileType getFileType() {
        return FileType.valueOf(getString("file_type"));
    }

    public void setFileType(FileType file_type) {
        setString("file_type", file_type.name());
    }

    public ContentType getContentType() {
        return ContentType.valueOf(getString("content_type"));
    }

    public void setContentType(ContentType content_type) {
        setString("content_type", content_type.name());
    }

    public SendType getSendType() {
        return SendType.valueOf(getString("send_type"));
    }

    public void setSendType(SendType send_type) {
        setString("send_type", send_type.name());
    }

    public String getSendDate() {
        return getString("send_date");
    }

    public void setSendDate(String date) {
        setString("send_date", date);
    }

    public String getRecipientsID() {
        return getString("recipientsID");
    }

    public List<Long> getAllRecipientsID() {
        String data = getRecipientsID();
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals(""))
            return new ArrayList<Long>();
        return Arrays.stream(data.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    }

    public void setAllRecipientsID(List<Long> recipientsID) {
        setString("recipientsID", Bot.bot.u.objectToString(recipientsID, ";"));
    }

    public void remRecipientID(Long recipientID) {
        List<Long> str = getAllRecipientsID();
        str.remove(recipientID);
        setAllRecipientsID(str);
    }

    public String getMessagesID() {
        return getString("messagesID");
    }

    public List<String> getAllMessagesID() {
        String data = getMessagesID();
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals(""))
            return new ArrayList<String>();
        return Arrays.stream(data.split(";")).collect(Collectors.toList());
    }

    public void setAllMessagesID(List<String> messagesID) {
        setString("messagesID", Bot.bot.u.objectToString(messagesID, ";"));
    }

    public void remMessageID(String messageID) {
        List<String> str = getAllMessagesID();
        str.remove(messageID);
        setAllMessagesID(str);
    }

    public void addMessageID(String messageID) {
        List<String> str = getAllMessagesID();
        if (!str.contains(messageID)) {
            str.add(messageID);
            setAllMessagesID(str);
        }
    }

    public MailStatus getMailStatus() {
        return MailStatus.valueOf(getString("status"));
    }

    public void setMailStatus(MailStatus status) {
        setString("status", status.name());
    }

    public String getCreatingDate() {
        return getString("creatingDate");
    }

    public void setCreatingDate(String text) {
        setString("creatingDate", text);
    }

    public String getCreatingTime() {
        return getString("creatingTime");
    }

    public void setCreatingTime(String text) {
        setString("creatingTime", text);
    }

    public String getString(String valuename) {
        return super.getString(valuename);
    }

    public List<Long> getMailingsIDWhere(MailStatus status) {
        return super.getValuesWhere("status", status.name(), "mailID").stream()
                .mapToLong(obj -> Long.parseLong(String.valueOf(obj))).filter(id -> id != 0L ? true : false)
                .boxed().collect(Collectors.toList());
    }

}
