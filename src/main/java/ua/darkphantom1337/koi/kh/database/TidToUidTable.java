package ua.darkphantom1337.koi.kh.database;

public class TidToUidTable extends DarkTable {

    private Boolean isTelegramID;

    public TidToUidTable(Long ID, Boolean isTelegramID) {
        super("tid_to_uid", isTelegramID ? "telegramID" : "userID", ID);
        this.isTelegramID = isTelegramID;
    }

    public Long getUserID() {
        return super.getLong("userID");
    }

    public Long getTelegramID() {
        if (isTelegramID) return super.getFieldID();
        else return super.getLong("telegramID");
    }

    public void setUserID(Long userID) {
        super.setLong("userID", userID);
    }

    public void setTelegramID(Long telegramID){
        super.setLong("telegramID", telegramID);
    }

    public void setDescription(String text){
        super.setString("description", text);
    }

    public String getDescription(){
        return super.getString("description");
    }

}
