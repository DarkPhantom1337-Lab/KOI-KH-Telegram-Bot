package ua.darkphantom1337.koi.kh.entitys;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.ActualTable;

public class Order extends ActualTable {

    private Bot bot = Bot.bot;

    public Order(Integer orderID) {
        super(orderID);
    }

    public Order(Long orderID) {
        super(orderID);
    }

    public Order(String orderID) {
        super(orderID);
    }

    public String getCourierName(){
        Long courierID = getCourierID();

        if (courierID == null || courierID == 0)
            return "Не назначен";
        return new User(courierID, true).getUserName();
    }

    public String getAccurateStatus(){
        return getString("accurateStatus");
    }

    public void setAccurateStatus(String status){
        setString("accurateStatus", status);
    }
}
