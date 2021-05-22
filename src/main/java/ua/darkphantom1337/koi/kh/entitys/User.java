package ua.darkphantom1337.koi.kh.entitys;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.DataBase;
import ua.darkphantom1337.koi.kh.database.TidToUidTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class User {

    private Long user_id;
    private Long telegramID;
    private Long userID;
    private Bot bot = Bot.bot;

    public User (Long telegramID){
        this.telegramID = telegramID;
        TidToUidTable ttu = new TidToUidTable(telegramID, true);
        if (ttu.isSetRecord())
            this.user_id = ttu.getUserID();
        else {
            Long newUserID = DataBase.getNextID(22);
            ttu.setUserID(newUserID);
            this.user_id = newUserID;
            ttu.setDescription(bot.u.getDate("dd/MM/yyyy HH:mm:ss"));
        }
    }


    public User (Long userID, Boolean isUserid){
        TidToUidTable ttu = new TidToUidTable(userID, false);
        this.telegramID = ttu.getTelegramID();
        this.user_id = userID;
    }
    public String getUserPhone(){
        return DataBase.getUserStr("phone", user_id);
    }

    public String getUserName(){
        return DataBase.getUserStr("name", user_id);
    }

    public String getUserType(){
        return DataBase.getUserStr("type", user_id);
    }

    public String getUserCompanyName(){
        return DataBase.getUserStr("company_name", user_id);
    }

    public String getUserCity(){
        return DataBase.getUserStr("city", user_id);
    }

    public String getUserAdres(){
        return DataBase.getUserStr("adress", user_id);
    }

    public String getUserLastAdres(){
        return DataBase.getUserStr("last_adress", user_id);
    }

    public String getUserLastModel(){
        return DataBase.getUserStr("last_model", user_id);
    }

    public String getUserLastModelRem(){
        return DataBase.getUserStr("last_model_rem", user_id);
    }

    public String getUserAction(){
        String action = DataBase.getUserStr("action", user_id);
        if (action == null)
            return "";
        return action;
    }


    public void setBirthday(String date){
        DataBase.setUsFields(user_id, "dr", date);
    }


    public void setName(String name){
        DataBase.setUsFields(user_id, "name", name);
    }
    public void setPhone(String name){
        DataBase.setUsFields(user_id, "phone", name);
    }
    public void setType(String type){
        DataBase.setUsFields(user_id, "type", type);
    }

    public void setUserCompanyName(String company_name){
        DataBase.setUsFields(user_id, "company_name", company_name);
    }

    public void setUserCity(String city){
        DataBase.setUsFields(user_id, "city", city);
    }

    public void setUserAdress(String adress){
        DataBase.setUsFields(user_id, "adress", adress);
    }

    public void setUserLastAdress(String lastadress){
        DataBase.setUsFields(user_id, "last_adress", lastadress);
    }

    public void setUserAction(String action){
        DataBase.setUsFields(user_id, "action",action);
    }

    public Boolean isPersonal(){
        return DataBase.isPersonal(user_id);
    }

    public List<Long> getCartridgesID(){
        String data = DataBase.getUsFileds(user_id,"cartridgesID");
        if (data == null || data.equals("") || data.equals(" "))
            return new ArrayList<Long>();
        return new ArrayList<Long>(Arrays.asList(data.split(";")).stream().mapToLong(id -> Long.parseLong(id)).boxed().collect(Collectors.toList()));
    }

    public void setCartridgesID(List<Long> cartridgesID){
        DataBase.setUsFields(user_id, "cartridgesID", Bot.bot.u.longToString(cartridgesID,";"));
    }

    public void addCartridgeID(Long cartridge_id){
        List<Long> cartridges = getCartridgesID();
        if (!cartridges.contains(cartridge_id))
            cartridges.add(cartridge_id);
        setCartridgesID(cartridges);
    }
    public void remCartridgeID(Long cartridge_id){
        List<Long> cartridges = getCartridgesID();
        cartridges.remove(cartridge_id);
        setCartridgesID(cartridges);
    }

    public List<String> getModels(){
        String data = DataBase.getUsFileds(user_id,"models");
        if (data == null || data.equals("") || data.equals(" "))
            return new ArrayList<String>();
        return new ArrayList<String>(Arrays.asList(data.split(";")));
    }

    public void setModels(List<String> models){
        DataBase.setUsFields(user_id, "models", Bot.bot.u.objectToString(models,";"));
    }

    public void addModel(String model){
        List<String> models = getModels();
        model = model.replace(";",":");
        if (!models.contains(model)) {
            if (models.size() >= 6)
                models.remove(0);
            models.add(0,model);
            setModels(models);
        } else {
            models.remove(model);
            models.add(0,model);
        }
    }
    public void remModel(String model){
        List<String> models = getModels();
        model = model.replace(";",":");
        models.remove(model);
        setModels(models);
    }


    public List<String> getModelsRem(){
        String data = DataBase.getUsFileds(user_id,"models_rem");
        if (data == null || data.equals("") || data.equals(" "))
            return new ArrayList<String>();
        return new ArrayList<String>(Arrays.asList(data.split(";")));
    }

    public void setModelsRem(List<String> models){
        DataBase.setUsFields(user_id, "models_rem", Bot.bot.u.objectToString(models,";"));
    }

    public void addModelRem(String model){
        List<String> models = getModelsRem();
        model = model.replace(";",":");
        if (!models.contains(model)) {
            if (models.size() >= 6)
                models.remove(6);
            models.add(model);
            setModelsRem(models);
        }
    }
    public void remModelRem(String model){
        List<String> models = getModelsRem();
        model = model.replace(";",":");
        models.remove(model);
        setModelsRem(models);
    }

    public Long getID(){
        return user_id;
    }

    public Long getUID(){
        return user_id;
    }

    public Long getTID(){
        return telegramID;
    }

    public Integer sendMessage(String text){
        return bot.sendMsgToUser(getTID(), text);
    }

    public Integer sendMessage(String text, String menu){
        return bot.sendMsgToUser(getTID(), text,menu);
    }
}
