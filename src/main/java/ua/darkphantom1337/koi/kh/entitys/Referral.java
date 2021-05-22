package ua.darkphantom1337.koi.kh.entitys;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.ReferralTable;

public class Referral extends ReferralTable {

    private Bot bot = Bot.bot;

    public Referral(Integer userID) {
        super(userID);
    }

    public Referral(Long userID) {
        super(userID);
    }

    public Referral(String userID) {
        super(userID);
    }

}
