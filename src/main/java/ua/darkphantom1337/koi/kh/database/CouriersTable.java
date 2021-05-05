package ua.darkphantom1337.koi.kh.database;

import ua.darkphantom1337.koi.kh.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CouriersTable extends DarkTable {

    public CouriersTable(Integer courierID) {
        super("couriers", "courierID", courierID.longValue());
    }

    public CouriersTable(Long courierID) {
        super("couriers", "courierID", courierID.longValue());
    }

    public CouriersTable(String courierID) {
        super("couriers", "courierID", Long.parseLong(courierID));
    }

    public Long getCourierID() {
        return getFieldID();
    }

    public String getCurrentTasksIDData(){
        return getString("currentTasksID");
    }

    public List<Long> getAllCurrentTasksID() {
        String data = getCurrentTasksIDData();
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals(""))
            return new ArrayList<Long>();
        return Arrays.stream(data.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    }

    public void setAllCurrentTaskID(List<Long> tasksID) {
        setString("currentTasksID",Bot.bot.u.objectToString(tasksID, ";"));
    }

    public void addCurrentTaskID(Long taskID) {
        List<Long> str = getAllCurrentTasksID();
        if (!str.contains(taskID))
            str.add(taskID);
        setAllCurrentTaskID(str);
    }

    public void remCurrentTaskID(Long taskID) {
        List<Long> str = getAllCurrentTasksID();
        str.remove(taskID);
        setAllCurrentTaskID(str);
    }

    public String getCompletedTasksIDData(){
        return getString("completedTasksID");
    }

    public List<Long> getAllCompletedTasksID() {
        String data = getCompletedTasksIDData();
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals(""))
            return new ArrayList<Long>();
        return Arrays.stream(data.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    }

    public void setAllCompletedTaskID(List<Long> tasksID) {
        setString("completedTasksID",Bot.bot.u.objectToString(tasksID, ";"));
    }

    public void addCompletedTaskID(Long taskID) {
        List<Long> str = getAllCompletedTasksID();
        if (!str.contains(taskID))
            str.add(taskID);
        setAllCompletedTaskID(str);
    }

    public void remCompletedTaskID(Long taskID) {
        List<Long> str = getAllCompletedTasksID();
        str.remove(taskID);
        setAllCompletedTaskID(str);
    }

    public String getMessagesIDData(){
        return getString("currentMessagesID");
    }

    public List<Long> getAllMessagesID() {
        String data = getMessagesIDData();
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals(""))
            return new ArrayList<Long>();
        return Arrays.stream(data.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    }

    public void setAllMessagesID(List<Long> messageID) {
        setString("currentMessagesID",Bot.bot.u.objectToString(messageID, ";"));
    }

    public void addMessagesTaskID(Long messageID) {
        List<Long> str = getAllMessagesID();
        if (!str.contains(messageID))
            str.add(messageID);
        setAllMessagesID(str);
    }

    public void remMessagesTaskID(Long messageID) {
        List<Long> str = getAllMessagesID();
        str.remove(messageID);
        setAllMessagesID(str);
    }


    public String getDate() {
        return getString("registerDate");
    }

    public void setDate(String text) {
        setString("registerDate", text);
    }

    public String getTime() {
        return getString("registerTime");
    }

    public void setTime(String text) {
        setString("registerTime", text);
    }

    public String getString(String valuename){
        return super.getString(valuename);
    }

}
