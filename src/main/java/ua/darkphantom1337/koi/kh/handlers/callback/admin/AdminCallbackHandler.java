package ua.darkphantom1337.koi.kh.handlers.callback.admin;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.entitys.Courier;
import ua.darkphantom1337.koi.kh.DataBase;
import ua.darkphantom1337.koi.kh.entitys.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminCallbackHandler {

    private static Bot bot = Bot.bot;

    public static Boolean handleAdminSetVacation(User user, String data, String cbqID){
        if (data.startsWith("#Admin/SetPosition")){
            String selectedVacansion = data.split("/")[2];
            if (bot.ah.admins_added_personal.containsKey(user.getUID())){
                Long personalID = bot.ah.admins_added_personal.get(user.getUID());
                DataBase.setPerFields(Math.toIntExact(personalID), "v_id", selectedVacansion);
                DataBase.setPerFields(Math.toIntExact(personalID), "position", selectedVacansion);
                DataBase.setPerFields(Math.toIntExact(personalID), "phone", DataBase.getUserStr("phone", personalID));
                DataBase.setPerFields(Math.toIntExact(personalID), "name", DataBase.getUserStr("name", personalID));
                DataBase.setPerFields(Math.toIntExact(personalID), "description", selectedVacansion+".Добавил: " + user.getUID());
                if (selectedVacansion.equalsIgnoreCase("courier")){
                    Courier courier = new Courier(personalID);
                    courier.setDate(new SimpleDateFormat("dd/MM/YYYY").format(new Date()));
                    courier.setDate(new SimpleDateFormat("HH/mm/ss").format(new Date()));
                }
                bot.sendMsgToUser(user.getTID(), "✅ Теперь человек " + DataBase.getUserStr("name", personalID) + " является персоналом бота KOI-KH \uD83E\uDD11", "ADMIN/Personal/MainMenu");
                return true;
            } else {
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Данные не актуальны, повторите процедуру повторно."));
            }
        }
        return false;
    }


}
