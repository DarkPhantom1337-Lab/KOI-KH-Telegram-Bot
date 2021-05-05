package ua.darkphantom1337.koi.kh.database;

import ua.darkphantom1337.koi.kh.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SubOrdersTable extends DarkTable {

    public SubOrdersTable(Integer subOrderID) {
        super("suborders", "subOrderID", subOrderID.longValue());
    }

    public SubOrdersTable(Long subOrderID) {
        super("suborders", "subOrderID", subOrderID.longValue());
    }

    public SubOrdersTable(String subOrderID) {
        super("suborders", "subOrderID", Long.parseLong(subOrderID));
    }

    public Long getSubOrderID() {
        return getFieldID();
    }

    public String getModel() {
        return getString("model");
    }

    public void setModel(String text) {
        setString("model", text);
    }

    public Integer getStatusMsgID() {
        return getInt("status_msg_id");
    }

    public void setStatusMsgID(Integer msgID) {
        setInt("status_msg_id", msgID);
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

    public Long getOrderID() {
        return getLong("orderID");
    }

    public void setOrderID(Long uid) {
        setLong("orderID", uid);
    }

    public Long getWorkID() {
        return getLong("workID");
    }

    public void setWorkID(Long uid) {
        setLong("workID", uid);
    }

    public Long getCartridgeID() {
        return getLong("cartridgeID");
    }

    public void setCartridgeID(Long value) {
        setLong("cartridgeID", value);
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

    public String getString(String valuename){
        return super.getString(valuename);
    }
}
