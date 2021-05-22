package ua.darkphantom1337.koi.kh.database;

public class ReferralTable extends DarkTable {

    public ReferralTable(Integer userID) {
        super("referral", "userID", userID.longValue());
    }

    public ReferralTable(Long userID) {
        super("referral", "userID", userID.longValue());
    }

    public ReferralTable(String userID) {
        super("referral", "userID", Long.parseLong(userID));
    }

    public Long getUserID() {
        return getFieldID();
    }

    public Long getInviterID(){
        Long inv = getLong("inviterID");
        if (inv != null)
            return inv;
        return 0L;
    }

    public void setInviterID(Long id){
        setLong("inviterID", id);
    }

    public Integer getInvited(){
        Integer inv = getInt("invited");
        if (inv != null)
            return inv;
        return 0;
    }

    public void setInvited(Integer invited){
        setInt("invited", invited);
    }

    public void addInvited(Integer add) {
        setInvited(getInvited()+add);
    }

    public String getString(String valuename){
        return super.getString(valuename);
    }

    public Boolean isSet(){
        return isSetRecord();
    }

}
