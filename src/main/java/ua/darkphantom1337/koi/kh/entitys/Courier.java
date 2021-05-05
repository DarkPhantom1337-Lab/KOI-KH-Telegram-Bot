package ua.darkphantom1337.koi.kh.entitys;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.CouriersTable;

public class Courier extends CouriersTable {

    private Bot bot = Bot.bot;

    public Courier(Integer courierID) {
        super(courierID);
    }

    public Courier(Long courierID) {
        super(courierID);
    }

    public Courier(String courierID) {
        super(courierID);
    }

    public User getUser(){
        return new User(getCourierID(), true);
    }

}
