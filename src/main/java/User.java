import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class User {

    private Long user_id;
    private Bot bot = Bot.bot;

    public User (Long user_id){
        this.user_id = user_id;
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

    public void setUserName(String name){
        DataBase.setUsFields(user_id, "name", name);
    }

    public void setUserType(String type){
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

    public Long getID(){
        return user_id;
    }

}
