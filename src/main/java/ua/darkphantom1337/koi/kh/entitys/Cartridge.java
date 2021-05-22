package ua.darkphantom1337.koi.kh.entitys;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.DataBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Cartridge {

    private Long cartridgeID;

    public Cartridge(Long cartridgeID) {
        this.cartridgeID = cartridgeID;
    }

    public String getModel() {
        return DataBase.getCartridgeField(cartridgeID, "cartridge_model");
    }

    public String getPrinterModel() {
        return DataBase.getCartridgeField(cartridgeID, "printer_model");
    }

    public String getAddress() {
        return DataBase.getCartridgeField(cartridgeID, "address");
    }

    public List<Long> getOwnersID() {
        String owners_data = DataBase.getCartridgeField(cartridgeID, "ownersID");
        if (owners_data == null || owners_data.equals(""))
            return new ArrayList<Long>();
        return Arrays.stream(owners_data.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    }

    public String getAddDate() {
        return DataBase.getCartridgeField(cartridgeID, "add_date");
    }

    public String getAddTime() {
        return DataBase.getCartridgeField(cartridgeID, "add_time");
    }

    public void create(String cartridgeModel, String printerModel, String address, List<Long> owners) {
        set("cartridge_model", cartridgeModel);
        set("printer_model", printerModel);
        set("address", address);
        set("ownersID", Bot.bot.u.longToString(owners, ";"));
        set("add_date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        set("add_time", new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    public void set(String fieldname, String fieldvalue) {
        DataBase.setCartridgeField(cartridgeID, fieldname, fieldvalue);
    }

    public Long getID() {
        return cartridgeID;
    }

    public void delete(){
        DataBase.deleteCartridge(cartridgeID);
    }
}
