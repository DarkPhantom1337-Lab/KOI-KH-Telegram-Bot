package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.DataBase;
import ua.darkphantom1337.koi.kh.entitys.Order;
import ua.darkphantom1337.koi.kh.entitys.User;

public class UserCallbackHandler {

    private Bot bot = Bot.bot;

    public boolean handleUserType(User user, String data, Integer msgID, String callbackID) {
        if (bot.user_type.containsKey(user.getUID()) == false) {
            if (data.equals("#ЯКомпания")) {
                if (DataBase.getUserType(Math.toIntExact(user.getUID())).equals("Не указано")) {
                    bot.deleteMsg(user.getTID(), msgID);
                    bot.sendToLogChanel("Пользователь " + DataBase.getUserName(Math.toIntExact(user.getUID())) + "(" + user.getUID() + ") указал что он является КОМПАНИЕЙ.");
                    bot.sendToUserInfoChanel("Пользователь " + DataBase.getUserName(Math.toIntExact(user.getUID())) + "(" + user.getUID() + ") указал что он является КОМПАНИЕЙ.");
                    DataBase.setUsFields(user.getUID(), "type", "Компания");
                    bot.tryExecureMethod(new AnswerCallbackQuery().setText("Спасибо :-)").setCallbackQueryId(callbackID));
                    bot.user_wait_company_name.put(user.getUID(), true);
                    bot.sendMsgToUser(user.getTID(), "Напишите название компании которую Вы представляете!", "backtomain");
                } else
                    bot.tryExecureMethod(new AnswerCallbackQuery().setText("Вы уже выбрали ответ :-)").setCallbackQueryId(callbackID));
                return true;
            }
            if (data.equals("#ЯЧастник")) {
                if (DataBase.getUserType(Math.toIntExact(user.getUID())).equals("Не указано")) {
                    bot.deleteMsg(user.getTID(), msgID);
                    bot.sendToLogChanel("Пользователь " + DataBase.getUserName(Math.toIntExact(user.getUID())) + "(" + user.getUID() + ") указал что он является ЧАСТНЫМ ЛИЦОМ.");
                    bot.sendToUserInfoChanel("Пользователь " + DataBase.getUserName(Math.toIntExact(user.getUID())) + "(" + user.getUID() + ") указал что он является ЧАСТНЫМ ЛИЦОМ.");
                    DataBase.setUsFields(user.getUID(), "type", "Частное лицо");
                    bot.tryExecureMethod(new AnswerCallbackQuery().setText("Спасибо :-) Теперь вы можете оставить заявку").setCallbackQueryId(callbackID));
                    bot.user_wait_adress.put(user.getUID(), true);
                    bot.sendMsg(user.getTID().intValue(), DataBase.getUserName(Math.toIntExact(user.getUID())) + ", укажите пожалуйста адрес для выезда курьера за заявкой. ", "adress");
                } else
                    bot.tryExecureMethod(new AnswerCallbackQuery().setText("Вы уже выбрали ответ :-)").setCallbackQueryId(callbackID));
                return true;
            }
        }
        return false;
    }

    public Boolean handleRate(User user, String data, Integer msgID, String callbackID) {
        if (data.contains("#Rate")) {
            Integer rate = Integer.parseInt(data.split("=")[1]), zn = Integer.parseInt(data.split("=")[2]);
            bot.handRate(zn, rate, user.getUID(), msgID, "Telegram");
            return true;
        }
        return false;
    }

    public Boolean handleSaveZayav(User user, String data, Integer msgID, String callbackID) {
        if (data.equals("#SaveZayav")) {
            bot.user_tema.put(user.getUID(), "Заправка картриджа");
            bot.saveZayav(user, null, "");
            return true;
        }
        return false;
    }

    public Boolean handleReklamaciya(User user, String data, Integer msgID, String callbackID) {
        if (data.startsWith("#Reklamaciya")) {
            Order order = new Order(data.split("/")[1]);
            if (!order.getDescriptions().contains("REKLAMACIYA_USE")) {
                user.setUserAction("user_wait_reklam_comm/" + order.getOrderID());
                bot.editMsg(user.getTID(), msgID, bot.getButText("Опишите дефект/поломку"));
                user.sendMessage("Опишите дефект/поломку как можно более точнее и отправьте мне.", "backtomain");
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callbackID).setText("Опишите дефект/поломку"));
                return true;
            } else {
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callbackID).setText("Вы уже подавали рекламацию по этой заявке!"));
            }
        }
        return false;
    }

    public Boolean handleCancelZayavka(User user, String data, Integer msgID, String callbackID) {
        if (data.startsWith("#CancelZayavka")) {
            Order order = new Order(data.split("/")[1]);
            if (!order.getDescriptions().contains("CANCEL_USE")) {
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callbackID).setText("Заявка на отмену заявки подана"));
                bot.editMsg(user.getTID(), msgID, bot.getButText("Заявка на отмену заявки отправлена"));
                order.addDescriptions("CANCEL_USE");
                bot.sendCancelZayav(order.getOrderID());
            } else {
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callbackID).setText("Вы уже подавали заявку на отмену по этой заявке!"));
            }
        }
        return false;
    }
}
