package ua.darkphantom1337.koi.kh.entitys;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.MailingsTable;
import ua.darkphantom1337.koi.kh.database.SubOrdersTable;

public class Mail extends MailingsTable {

    private Bot bot = Bot.bot;

    public Mail(Integer mailID) {
        super(mailID);
    }

    public Mail(Long mailID) {
        super(mailID);
    }

    public Mail(String mailID) {
        super(mailID);
    }

}
