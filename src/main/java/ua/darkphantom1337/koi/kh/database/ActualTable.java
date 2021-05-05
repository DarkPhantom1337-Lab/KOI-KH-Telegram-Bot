package ua.darkphantom1337.koi.kh.database;

import ua.darkphantom1337.koi.kh.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ActualTable extends DarkTable {

    public ActualTable(Integer orderID) {
        super("actual", "z_id", orderID.longValue());
    }

    public ActualTable(Long orderID) {
        super("actual", "z_id", orderID.longValue());
    }

    public ActualTable(String orderID) {
        super("actual", "z_id", Long.parseLong(orderID));
    }

    public Long getOrderID() {
        return getFieldID();
    }

    public String getTheme() {
        return getString("theme");
    }

    public void setTheme(String text) {
        setString("theme", text);
    }

    public String getModel() {
        return getString("model");
    }

    public void setModel(String text) {
        setString("model", text);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String text) {
        setString("name", text);
    }

    public String getPhone() {
        return getString("phone");
    }

    public void setPhone(String text) {
        setString("phone", text);
    }

    public Long getUID() {
        return getLong("u_id");
    }

    public void setUID(Long uid) {
        setLong("u_id", uid);
    }

    public Integer getMainMsgID() {
        return getInt("main_msg_id");
    }

    public void setMainMsgID(Integer msgID) {
        setInt("main_msg_id", msgID);
    }

    public Integer getStatusMsgID() {
        return getInt("status_msg_id");
    }

    public void setStatusMsgID(Integer msgID) {
        setInt("status_msg_id", msgID);
    }

    public HashMap<Long, Integer> getVosstMsgID() {
        String data = getString("vosst_msg_id");
        HashMap<Long,Integer> map = new HashMap<Long,Integer>();
        if (data == null || data.equals("")|| data.equals(" "))
            return map;
        for (String dat : data.split(";"))
            if (!dat.equals("") && !dat.equals(" ")) {
                try {
                    map.put(Long.parseLong(dat.split("-")[0]), Integer.parseInt(dat.split("-")[1]));
                } catch (Exception e) {
                }
            }
        return map;
    }

    public void setVosstMsgID(Long chatID, Integer msgID) {
        HashMap<Long,Integer> map = getVosstMsgID();
        map.put(chatID,msgID);
        String dat = "";
        for (Long id : map.keySet())
            dat += id+"-" + map.get(id) + ";";
        setString("vosst_msg_id", dat);
    }

    public void clearVosstMsgID(){
        setString("vosst_msg_id", "");
    }

    public String getVosstMsgText() {
        String s = getString("vosst_msg_text");
        if (s == null)
            return "";
        return  s;
    }

    public void setVosstMsgText(String text) {
        setString("vosst_msg_text", text);
    }

    public String getStatus() {
        return getString("status");
    }

    public void setStatus(String text) {
        setString("status", text);
    }

    public String getStatuses() {
        return getString("statuses");
    }

    public void setStatuses(String text) {
        setString("statuses", text);
    }

    public List<String> getAllStatuses() {
        String data = getStatuses();
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals(""))
            return new ArrayList<String>();
        return new ArrayList<String>(Arrays.asList(data.split(";")));
    }

    public void setAllStatuses(List<String> statuses) {
        setStatuses(Bot.bot.u.stringToString(statuses, ";"));
    }

    public void addStatuses(String statuses) {
        List<String> str = getAllStatuses();
        str.add(statuses);
        setAllStatuses(str);
    }

    public String getPrinyal() {
        return getString("prinyal");
    }

    public void setPrinyal(String text) {
        setString("prinyal", text);
    }

    public String getCity() {
        return getString("city");
    }

    public void setCity(String text) {
        setString("city", text);
    }

    public String getAddress() {
        return getString("adress");
    }

    public void setAddress(String text) {
        setString("adress", text);
    }

    public String getSource() {
        return getString("source");
    }

    public void setSource(String text) {
        setString("source", text);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String text) {
        setString("description", text);
    }

    public List<String> getDescriptions() {
        String data = getDescription();
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals(""))
            return new ArrayList<String>();
        return new ArrayList<String>(Arrays.asList(data.split(";")));
    }

    public void setDescriptions(List<String> descriptions) {
        setDescription(Bot.bot.u.stringToString(descriptions, ";"));
    }

    public void addDescriptions(String description) {
        List<String> str = getDescriptions();
        if (!str.contains(description))
            str.add(description);
        setDescriptions(str);
    }

    public Boolean isCancelled() {
        return getBoolean("is_cancelled");
    }

    public void setCancelled(Boolean value) {
        setBoolean("is_cancelled", value);
    }

    public Long getCartridgeID() {
        return getLong("cartridgeID");
    }

    public void setCartridgeID(Long value) {
        setLong("cartridgeID", value);
    }

    public Long getWorkID() {
        return getLong("workID");
    }

    public void setWorkID(Long value) {
        setLong("workID", value);
    }

    public String getSheetRows() {
        return getString("sheetRows");
    }

    public void setSheetRows(String value) {
        setString("sheetRows", value);
    }

    public List<Integer> getAllSheetRows() {
        String data = getSheetRows();
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals(""))
            return new ArrayList<Integer>();
        return Arrays.stream(data.split(";")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
    }

    public void setAllSheetRows(List<Integer> allSheetRows) {
        setSheetRows(Bot.bot.u.objectToString(allSheetRows, ";"));
    }

    public void addSheetRow(Integer rowNumber) {
        List<Integer> str = getAllSheetRows();
        if (!str.contains(rowNumber))
            str.add(rowNumber);
        setAllSheetRows(str);
    }

    public String getTasksID() {
        return getString("tasksID");
    }

    public void setTasksID(String value) {
        setString("tasksID", value);
    }

    public List<Long> getAllTasksID() {
        String data = getTasksID();
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals(""))
            return new ArrayList<Long>();
        return Arrays.stream(data.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    }

    public void setAllTasksID(List<Long> tasksID) {
        setTasksID(Bot.bot.u.objectToString(tasksID, ";"));
    }

    public void addTaskID(Long rowNumber) {
        List<Long> str = getAllTasksID();
        if (!str.contains(rowNumber)) {
            str.add(rowNumber);
            setAllTasksID(str);
        }
    }

    public String getSubOrdersID() {
        return getString("subOrdersID");
    }

    public void setSubOrdersID(String value) {
        setString("subOrdersID", value);
    }

    public List<Long> getAllSubOrdersID() {
        String data = getSubOrdersID();
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals(""))
            return new ArrayList<Long>();
        return Arrays.stream(data.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    }

    public void setAllSubOrdersID(List<Long> subOrdersID) {
        setSubOrdersID(Bot.bot.u.objectToString(subOrdersID, ";"));
    }

    public void addSubOrderID(Long id) {
        List<Long> str = getAllSubOrdersID();
        if (!str.contains(id)) {
            str.add(id);
            setAllSubOrdersID(str);
        }
    }

    public Integer getRate() {
        return getInt("rate");
    }

    public void setRate(Integer value) {
        setInt("rate", value);
    }

    public Long getCourierID() {
        return getLong("courierID");
    }

    public void setCourierID(Long text) {
        setLong("courierID", text);
    }

    public String getDate() {
        return getString("date");
    }

    public void setDate(String text) {
        setString("date", text);
    }

    public String getTime() {
        return getString("time");
    }

    public void setTime(String text) {
        setString("time", text);
    }

    public String getString(String valuename){
        return super.getString(valuename);
    }
}
