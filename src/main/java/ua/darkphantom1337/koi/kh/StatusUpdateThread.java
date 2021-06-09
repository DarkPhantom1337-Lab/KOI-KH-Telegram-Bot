package ua.darkphantom1337.koi.kh;


import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.darkphantom1337.koi.kh.database.DataBase;
import ua.darkphantom1337.koi.kh.database.SqlServer;
import ua.darkphantom1337.koi.kh.entitys.Order;
import ua.darkphantom1337.koi.kh.entitys.SubOrder;
import ua.darkphantom1337.koi.kh.entitys.User;
import ua.darkphantom1337.koi.kh.entitys.mails.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StatusUpdateThread extends Thread {

    public void run() {
//        Bot.bot.info("Starting mailings thread...");
        this.setName("StatusUpdate");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (Long orderID : OrderLocations.getAllCurrentOrdersID()) {
                        Integer kodStatus = SqlServer.getKodStatus(new Order(orderID)).intValue();
                        if (!isEqualsStatuses(kodStatus, new Order(orderID).getAccurateStatus())) {
                            Bot.bot.error("||||| Do not change status for SQL -> Order -> " + orderID + " KodStatus -> " + kodStatus + " OrderStatus -> " + new Order(orderID).getAccurateStatus());
                            try {
                               /* if (kodStatus == 55)
                                 Bot.bot.pch.handlePrinyatZCallback(new User(1L, false), "#PRINYAT-Z=" + orderID, new Order(orderID).getMainMsgID(), "", "");
                                if (kodStatus == 56)
                                    Bot.bot.pch.handleChangeZStatus(new User(9L, false), "#ChangeZStatus/сбор_в_пути/" + orderID, new Order(orderID).getMainMsgID(), "", "");
                                if (kodStatus == 57)
                                    Bot.bot.pch.handleChangeZStatus(new User(9L, false), "#ChangeZStatus/заявка_в_работе/" + orderID, new Order(orderID).getMainMsgID(), "", "");
                                if (kodStatus == 58) {
                                    new Order(orderID).setAccurateStatus("Готова к выезду");
                                    OrderLocations.addReadyToWay(orderID);
                                }
                                if (kodStatus == 59)
                                    Bot.bot.pch.handleChangeZStatus(new User(9L, false), "#ChangeZStatus/доставка_в_пути/" + orderID, new Order(orderID).getMainMsgID(), "", "");
                                if (kodStatus == 18 || kodStatus == 19)
                                    Bot.bot.pch.handleEndZayavAndVosst(new User(9L, false), "#ENDZAYAV/" + orderID, new Order(orderID).getMainMsgID(), "", "");
                            */} catch (Exception e) {
                                Bot.bot.error("Do not change status for SQL -> Order -> " + orderID + " KodStatus -> " + kodStatus + " OrderStatus -> " + new Order(orderID).getAccurateStatus());
                            continue;
                            }
                        }
                    }
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    Bot.bot.error("Error in StatusUpdate THREAD");
                }
            }
        }, 1000, 60000);
    }

    public boolean isEqualsStatuses(Integer kodStatus, String orderStatus) {
        if (kodStatus == 55 && (orderStatus.equalsIgnoreCase("принята") || orderStatus.equalsIgnoreCase("поступила")))
            return true;
        if (kodStatus == 56 && orderStatus.equalsIgnoreCase("сбор"))
            return true;
        if (kodStatus == 57 && orderStatus.equalsIgnoreCase("в работе"))
            return true;
        if (kodStatus == 58 && orderStatus.equalsIgnoreCase("готова к выезду"))
            return true;
        if (kodStatus == 59 && orderStatus.equalsIgnoreCase("доставка"))
            return true;
        if ((kodStatus == 18 || kodStatus == 19) && orderStatus.equalsIgnoreCase("завершена"))
            return true;
        return false;
    }
}