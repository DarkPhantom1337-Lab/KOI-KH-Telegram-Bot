package ua.darkphantom1337.koi.kh.entitys;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.DataBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MasterWork {

    private Bot bot = Bot.bot;

    private Long workID;

    public enum WorkType {
        FILL,
        RECOVERY,
        FILL_AND_RECOVERY
    }

    public MasterWork(Long workID) {
        this.workID = workID;
    }

    public Long getWorkID() {
        return this.workID;
    }

    public Long getOrderID() {
        return DataBase.getMasterWorkLField(workID, "orderID");
    }

    public void setOrderID(Long orderID) {
        DataBase.setMasterWorkField(workID, "orderID", orderID);
    }

    public Long getMasterID() {
        return DataBase.getMasterWorkLField(workID, "masterID");
    }

    public void setMasterID(Long orderID) {
        DataBase.setMasterWorkField(workID, "masterID", orderID);
    }

    public boolean isUseChip() {
        return DataBase.getMasterWorkBField(workID, "chip");
    }

    public void setUseChip(boolean value) {
        DataBase.setMasterWorkField(workID, "chip", value);
    }

    public boolean isUseLD() {
        return DataBase.getMasterWorkBField(workID, "ld");
    }

    public void setUseLD(boolean value) {
        DataBase.setMasterWorkField(workID, "ld", value);
    }

    public boolean isUseCHL() {
        return DataBase.getMasterWorkBField(workID, "chl");
    }

    public void setUseCHL(boolean value) {
        DataBase.setMasterWorkField(workID, "chl", value);
    }

    public boolean isUseMR() {
        return DataBase.getMasterWorkBField(workID, "mr");
    }

    public void setUseMR(boolean value) {
        DataBase.setMasterWorkField(workID, "mr", value);
    }

    public boolean isUseVPZ() {
        return DataBase.getMasterWorkBField(workID, "vpz");
    }

    public void setUseVPZ(boolean value) {
        DataBase.setMasterWorkField(workID, "vpz", value);
    }

    public boolean isUseFB() {
        return DataBase.getMasterWorkBField(workID, "fb");
    }

    public void setUseFB(boolean value) {
        DataBase.setMasterWorkField(workID, "fb", value);
    }

    public boolean isUseMB() {
        return DataBase.getMasterWorkBField(workID, "mb");
    }

    public void setUseMB(boolean value) {
        DataBase.setMasterWorkField(workID, "mb", value);
    }

    public int getTonerAmount() {
        return DataBase.getMasterWorkIField(workID, "toner");
    }

    public void setTonerAmount(Integer amount) {
        if (amount >= 0)
            DataBase.setMasterWorkField(workID, "toner", amount);
        else DataBase.setMasterWorkField(workID, "toner", 0);
    }

    public void addTonerAmount(Integer add) {
        Integer tonerAmount = getTonerAmount();
        if (tonerAmount + add <= 1000)
            setTonerAmount(getTonerAmount() + add);
        else
            setTonerAmount(1000);
    }

    public void remTonerAmount(Integer add) {
        Integer tonerAmount = getTonerAmount();
        if (tonerAmount - add >= 0)
            setTonerAmount(getTonerAmount() - add);
        else
            setTonerAmount(0);
    }

    public WorkType getType() {
        return WorkType.valueOf(DataBase.getMasterWorkSField(workID, "type"));
    }

    public void setType(WorkType type) {
        DataBase.setMasterWorkField(workID, "type", type.name());
    }

    public List<String> getDescription() {
        String data = DataBase.getMasterWorkSField(workID, "description");
        if (data == null || data.equals("null") || data.equals("NULL") || data.equals("") || data.equals(" "))
            return new ArrayList<String>();
        return new ArrayList<String>(Arrays.asList(data.split(";")));
    }

    public void setDescription(List<String> descriptions) {
        DataBase.setMasterWorkField(workID, "description", bot.u.stringToString(descriptions, ";"));
    }

    public void addDescription(String name) {
        List<String> descr = getDescription();
        descr.add(name);
        setDescription(descr);
    }

    public boolean isUseRecovery() {
        return getDescription().contains("RECOVERY_USE");
    }

    public boolean isStart() {
        return getDescription().contains("START_WORK");
    }


    public boolean isConfirmRecovery() {
        return getDescription().contains("RECOVERY_CONFIRM");
    }

    public boolean isCompleteWork() {
        return getDescription().contains("COMPLETE_WORK");
    }

    public boolean isRecoveryAnswered() {
        return getDescription().contains("RECOVERY_ANSWER");
    }

    public boolean isCancelRecovery() {
        return getDescription().contains("RECOVERY_CANCEL");
    }

    public boolean isWaitRecovery() {
        return getDescription().contains("RECOVERY_WAIT");
    }


    public boolean isNeedRecovery() {
        if (isUseLD() || isUseFB() || isUseMB() || isUseMR() || isUseCHL() || isUseVPZ())
            return true;
        return false;
    }

    public String getNeedRecoveryComponentsName(){
        String need = "";
        if (isUseLD())
            need += "Лезвие Дозирования;";
        if (isUseFB())
            need += "Фотобарабан;";
        if (isUseMB())
            need += "Магнитный вал;";
        if (isUseMR())
            need += "Мягкий ракель;";
        if (isUseVPZ())
            need += "Вал первичного заряда;";
        if (isUseCHL())
            need += "Ракель;";
        if (isUseChip())
            need += "ЧИП;";
        return need;
    }

    public Integer getMainMessageID(){
        return bot.pi(DataBase.getMasterWorkSField(workID, "mainMsgID"));
    }

    public void setMainMessageID(Integer msgid){
        DataBase.setMasterWorkField(workID, "mainMsgID", msgid);
    }

    public String getStartDate(){
        return DataBase.getMasterWorkSField(workID, "start_date");
    }

    public String getStartTime(){
        return DataBase.getMasterWorkSField(workID, "start_time");
    }

    public String getEndDate(){
        return DataBase.getMasterWorkSField(workID, "end_date");
    }

    public String getEndTime(){
        return DataBase.getMasterWorkSField(workID, "end_time");
    }

    public void setStartDate(Date date) {
        DataBase.setMasterWorkField(workID, "start_date", new SimpleDateFormat("dd/MM/YYYY").format(date));
    }

    public void setStartTime(Date date) {
        DataBase.setMasterWorkField(workID, "start_time", new SimpleDateFormat("HH:mm:ss").format(date));
    }

    public void setEndDate(Date date) {
        DataBase.setMasterWorkField(workID, "end_date", new SimpleDateFormat("dd/MM/YYYY").format(date));
    }

    public void setEndTime(Date date) {
        DataBase.setMasterWorkField(workID, "end_time", new SimpleDateFormat("HH:mm:ss").format(date));
    }

}
