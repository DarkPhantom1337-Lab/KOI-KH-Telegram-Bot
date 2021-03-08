import com.google.inject.internal.cglib.reflect.$FastClass;
import org.telegram.telegrambots.api.objects.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserMessageHandler {

    private Bot bot;

    public UserMessageHandler(Bot bot) {
        this.bot = bot;
    }

    public Boolean handleMessage(Message msg, Long user_id) {
        try {
            List<Long> all_user_id = DataBase.getAllUserId();
            if (!all_user_id.contains(user_id)) {
                all_user_id.add(user_id);
                DataBase.setUFields(3, "val1", bot.u.longToString(all_user_id, ";"));
            }
            if (DataBase.isRegUser(user_id) == false) {
                bot.info("NOT REG " + user_id);
                bot.sendToLogChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + " написал ПЕРВОЕ сообщение боту!");
                bot.user_add_contact(msg);
                DataBase.setUserStr("action", Math.toIntExact(user_id), "user_wait_phone");
                bot.user_wait_phone.put(user_id, true);
                DataBase.setUsFields(user_id, "adress", "Не указано");
                DataBase.setUsFields(user_id, "last_model", "Не указано");
                DataBase.setUsFields(user_id, "last_model_rem", "Не указано");
                DataBase.setUsFields(user_id, "dr", "Не указано");
                DataBase.setUsFields(user_id, "city", "Харьков");
                DataBase.setUsFields(user_id, "privilege", "USER");
                DataBase.setUsFields(user_id, "reg_date", bot.u.getDate("dd/MM/yyyy"));
                DataBase.setUsFields(user_id, "reg_time", bot.u.getTime("HH:mm:ss"));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean checkIsRegister(String text, Message msg, Long user_id) {
        if (DataBase.isRegUser(user_id))
            if (!DataBase.isSetMainRegisterInfo(Math.toIntExact(user_id))) {
                bot.info("MAIN INFO - NOT SET 1");
                bot.sendMsg(msg, "Вы ещё не завершили регистрацию.", "");
                if (DataBase.getUserStr("action", user_id).equals("user_wait_name")) {
                    DataBase.setUserStr("action", Math.toIntExact(user_id), "user_wait_name");
                    bot.sendMsg(msg, "Введите Ваше имя пожалуйста!", "");
                    return true;
                }
                if (text.equals("Написать менеджеру")) {
                    bot.sendMsg(msg, "Нажмите на кнопку ниже чтобы перейти в чат с нашим менеджером", "mened");
                    return true;
                }

                if (text.equals("Адрес/Контакты")) {
                    bot.contacts(msg);
                    return true;
                }
                DataBase.setUserStr("action", Math.toIntExact(user_id), "user_wait_phone");
                bot.user_add_contact(msg);
                DataBase.setUserStr("action", Math.toIntExact(user_id), "user_wait_phone");
                bot.user_wait_phone.put(msg.getChatId(), true);
                DataBase.setUsFields(user_id, "adress", "Не указано");
                DataBase.setUsFields(user_id, "last_model", "Не указано");
                DataBase.setUsFields(user_id, "last_model_rem", "Не указано");
                DataBase.setUsFields(user_id, "dr", "Не указано");
                DataBase.setUsFields(user_id, "city", "Харьков");
                DataBase.setUsFields(user_id, "privilege", "USER");
                DataBase.setUsFields(user_id, "reg_date", bot.bot.u.getDate("dd/MM/yyyy"));
                DataBase.setUsFields(user_id, "reg_time", bot.bot.u.getTime("HH:mm:ss"));
                return true;
            }
        return false;
    }

    public Boolean handleStart(String text, Message msg, Long user_id) {
        if (text.equals("/start")) {
            if (DataBase.isRegUser(user_id))
                if (DataBase.isSetMainInfo(Math.toIntExact(user_id))) {
                    bot.sendMsg(msg, "Здравствуйте " + DataBase.getUsFileds(user_id, "name") + ", мы с Вами уже знакомы :-)\nЯ готов выполнить Ваш запрос.", "main");
                    return true;
                } else {
                    bot.info("MAIN INFO - NOT SET 2");
                    bot.sendMsg(msg, "Вы ещё не завершили регистрацию.", "");
                    if (DataBase.getUserStr("action", user_id).equals("user_wait_name")) {
                        DataBase.setUserStr("action", Math.toIntExact(user_id), "user_wait_name");
                        bot.sendMsg(msg, "Введите Ваше имя пожалуйста!", "");
                        return true;
                    }
                    if (text.equals("Написать менеджеру")) {
                        bot.sendMsg(msg, "Нажмите на кнопку ниже чтобы перейти в чат с нашим менеджером", "mened");
                        return true;
                    }

                    if (text.equals("Адрес/Контакты")) {
                        bot.contacts(msg);
                        return true;
                    }
                    DataBase.setUserStr("action", Math.toIntExact(user_id), "user_wait_phone");
                    bot.user_add_contact(msg);
                    DataBase.setUserStr("action", Math.toIntExact(user_id), "user_wait_phone");
                    bot.user_wait_phone.put(msg.getChatId(), true);
                    DataBase.setUsFields(user_id, "adress", "Не указано");
                    DataBase.setUsFields(user_id, "last_model", "Не указано");
                    DataBase.setUsFields(user_id, "last_model_rem", "Не указано");
                    DataBase.setUsFields(user_id, "dr", "Не указано");
                    DataBase.setUsFields(user_id, "city", "Харьков");
                    DataBase.setUsFields(user_id, "privilege", "USER");
                    DataBase.setUsFields(user_id, "reg_date", bot.bot.u.getDate("dd/MM/yyyy"));
                    DataBase.setUsFields(user_id, "reg_time", bot.bot.u.getTime("HH:mm:ss"));
                    return true;
                }
            else {
                if (DataBase.getUserStr("action", user_id).equals("user_wait_name")) {
                    DataBase.setUserStr("action", Math.toIntExact(user_id), "user_wait_name");
                    bot.sendMsg(msg, "Введите Ваше имя пожалуйста!", "");
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean handContact(Long id, Message msg) {
        if (DataBase.getUserStr("action", id) != null && DataBase.getUserStr("action", id).equals("user_wait_phone")) {
            if (bot.u.isYesContact(msg)) {
                bot.sendToLogChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + "(" + msg.getContact().getPhoneNumber() + ") оставил свой номер телефона!");
                bot.sendToUserInfoChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + "(" + msg.getContact().getPhoneNumber() + ") оставил свой номер телефона!");
                bot.user_phone.put(id, msg.getContact().getPhoneNumber());
                DataBase.setUserStr("phone", Math.toIntExact(id), msg.getContact().getPhoneNumber());
                bot.user_spasibo_contact(msg);
                DataBase.setUserStr("action", Math.toIntExact(id), "user_wait_name");
                bot.sendMsg(msg, "Как я могу к Вам обращаться?", "men_adr");
                System.out.println(bot.prefix() + "User " + bot.u.getUserPhone(msg) + " succes register! ID: " + bot.u.getUserID(msg) + " Name: " + bot.u.getUserName(msg));
                return true;
            } else {
                if (msg.hasText()) {
                    String text = msg.getText();
                    if (!text.equals("Написать менеджеру") && !text.equals("Адрес/Контакты")) {
                        bot.user_cancel_contact(msg);
                        return true;
                    } else {
                        if (text.equals("Написать менеджеру")) {
                            bot.sendMsg(msg, "Нажмите на кнопку ниже чтобы перейти в чат с нашим менеджером", "mened");
                            return true;
                        }

                        if (text.equals("Адрес/Контакты")) {
                            bot.contacts(msg);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Boolean handleUserReklamComm(Long userID, String text) {
        if ((DataBase.getUserStr("action", userID) != null && DataBase.getUserStr("action", userID).startsWith("user_wait_reklam_comm"))) {
            Long nz = Long.parseLong(DataBase.getUserStr("action", userID).split("/")[1]);
            if (!userID.equals("Главное меню")) {
                DataBase.setUserStr("action", Math.toIntExact(userID), "main");
                DataBase.setZFields(Math.toIntExact(nz), "status", "Поступила");
                DataBase.addZDescriptions((long) nz, "REKLAMACIYA_Text-" + text.replaceAll(";", "%"));
                DataBase.setZStatuses(nz, new ArrayList<String>());
                DataBase.addZStatuses(nz, "Заявка на рекламацию подана.");
                try {
                    bot.sendToChanelReklamaciya((long) nz, DataBase.getZFields(Math.toIntExact(nz), "theme") + "\nОписание дефекта: " + text, DataBase.getZFields(Math.toIntExact(nz), "model"),
                            DataBase.getUserType(Math.toIntExact(userID)), DataBase.getZFields(Math.toIntExact(nz), "name"),
                            DataBase.getZFields(Math.toIntExact(nz), "phone"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                bot.sendMsgToUser(userID, "Спасибо! Заявка на рекламацию передана. ", "main");
                return true;
            } else {
                try {
                    DataBase.setZStatuses(nz, new ArrayList<String>());
                    DataBase.addZStatuses(nz, "Заявка на рекламацию подана.");
                    bot.sendToChanelReklamaciya((long) nz, DataBase.getZFields(Math.toIntExact(nz), "theme") + "\nОписание дефекта: " + text, DataBase.getZFields(Math.toIntExact(nz), "model"),
                            DataBase.getUserType(Math.toIntExact(userID)), DataBase.getZFields(Math.toIntExact(nz), "name"),
                            DataBase.getZFields(Math.toIntExact(nz), "phone"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                DataBase.setUserStr("action", Math.toIntExact(userID), "main");
                return false;
            }
        }
        return false;
    }

    public Boolean handleUserName(User user, String text, Message msg){
        if (user.getUserAction().equals("user_wait_name")) {
            if (text.equals("Написать менеджеру")) {
                bot.sendMsg(msg, "Нажмите на кнопку ниже чтобы перейти в чат с нашим менеджером", "mened");
                return true;
            }
            if (text.equals("Адрес/Контакты")) {
                bot.contacts(msg);
                return true;
            }
            user.setUserAction("main");
            DataBase.saveUser(Math.toIntExact(user.getID()), null, "Не указано", text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
            if (user.isPersonal()) {
                String vc = DataBase.getPerFields(user.getID(), "v_id");
                if (vc.equals("manager")) {
                    bot.sendMsg(msg, text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "") + ", вы менеджер компании 'KOI'! У вас своё особое меню управления, для перехода в меню пользователя нажмите самую нижнюю кнопку.", "MainManagerMenu");
                    bot.sendToLogChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + " указал что его зовут " + text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
                    bot.sendToUserInfoChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + " указал что его зовут " + text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
                    return true;
                }
                if (vc.equals("admin") || vc.equals("owner")) {
                    bot.sendMsg(msg, text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "") + ", вы администратор компании 'KOI'! У вас своё особое меню управления, для перехода в меню пользователя нажмите самую нижнюю кнопку.", "MainAdminMenu");
                    bot.sendToLogChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + " указал что его зовут " + text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
                    bot.sendToUserInfoChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + " указал что его зовут " + text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
                    return true;
                }
            }
            bot.sendToLogChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + " указал что его зовут " + text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
            bot.sendToUserInfoChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + " указал что его зовут " + text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
            bot.sendMsg(msg, text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "") + ", теперь Вы можете оставлять заявки :-)", "main");
            return true;
        }
        return false;
    }

    public Boolean handleUserRateComm(Long userID, String text, Message msg){
        if (bot.user_wait_rate_comm.containsKey(userID) && bot.user_wait_rate_comm.get(userID) == true && bot.user_wait_rate_zn.containsKey(userID) && !bot.menus.contains(text)) {
            DataBase.addZDescriptions((long)bot.user_wait_rate_zn.get(userID), text);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.deleteMsg(userID, msg.getMessageId());
                    this.cancel();
                }
            }, 200 * 60);
            bot.user_wait_rate_comm.remove(userID);
            int msgi = bot.sendMsgToUser(userID, "Спасибо за комментарий!");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.deleteMsg(userID, msgi);
                    bot.deleteMsg(userID, bot.user_wait_rate_zn_тsg.get(userID));
                    bot.user_wait_rate_zn_тsg.remove(userID);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (DataBase.getZFields(bot.user_wait_rate_zn.get(userID), "status").equals("Завершена|Опрошена")) {
                                DataBase.setZFields(bot.user_wait_rate_zn.get(userID), "status", "Завершена|Опрошена|Архив");
                                if (bot.pi(DataBase.getZFields(bot.user_wait_rate_zn.get(userID), "rate")) <= 4) {
                                    bot.sendToChanel((long) bot.user_wait_rate_zn.get(userID));
                                }
                                String[] msgs = DataBase.getZFields(bot.user_wait_rate_zn.get(userID), "msg_ids").split(",");
                                for (String mid : msgs) {
                                    try {
                                        String[] s = mid.split("!");
                                        bot.deleteMsg((long) bot.pi(s[0]), bot.pi(s[1]));
                                    } catch (Exception e) {

                                    }
                                }
                                bot.user_wait_rate_zn_тsg.remove(userID);
                                bot.user_wait_rate_comm.remove(userID);
                                DataBase.saveZToArch(bot.user_wait_rate_zn.get(userID));
                                DataBase.delete(bot.user_wait_rate_zn.get(userID));
                                bot.user_wait_rate_zn.remove(userID);
                            }
                            this.cancel();
                        }
                    }, 60000 * 2);
                    this.cancel();
                }
            }, 200 * 60);
            return true;
        }
        return false;
    }

    public Boolean handleUserAdress(Long userID, String txt, Message msg){
        if (bot.user_wait_adress.containsKey(userID) && bot.user_wait_adress.get(userID) == true) {
            if (!bot.menus.contains(txt)) {
                if (txt.equals("Главное меню")) {
                    bot.user_wait_model.remove(userID);
                    bot.user_wait_adress.remove(userID);
                    bot.sendMsg(msg, "Главное меню", "main");
                    return true;
                }
                String adr = DataBase.getUsFileds(userID, "adress").replaceAll(DataBase.getUsFileds(userID, "city") + ", ", "");
                adr = adr.startsWith(" ") ? DataBase.getUsFileds(userID, "adress").replaceAll(DataBase.getUsFileds(userID, "city") + ", ", "").substring(1) : DataBase.getUsFileds(userID, "adress").replaceAll(DataBase.getUsFileds(userID, "city") + ", ", "");
                if (adr.startsWith(" "))
                    adr = adr.substring(1);
                String ls = DataBase.getUsFileds(userID, "last_adress");
                if (ls != null && ls.startsWith(" "))
                    ls = ls.substring(1);
                DataBase.setUsFields(userID, "last_adress", ls);
                txt = txt.replaceAll(DataBase.getUsFileds(userID, "city") + ", ", "");
                if (txt.equals("Я приеду сам")) {
                    if (adr.equals("Не указано")) {
                        DataBase.setUsFields(userID, "last_adress", "Не указано");
                        if (DataBase.getUsFileds(userID, "last_adress").equals(txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""))) {
                            DataBase.setUsFields(userID, "adress", DataBase.getUsFileds(userID, "last_adress"));
                            DataBase.setUsFields(userID, "last_adress", adr);
                        }
                        if (!adr.equals(txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "")))
                            DataBase.setUsFields(userID, "adress", txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));

                    } else {
                        DataBase.setUsFields(userID, "last_adress", adr);
                    }
                } else {

                    if (!adr.equals(txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""))) {
                        DataBase.setUsFields(userID, "last_adress", adr);
                        DataBase.setUsFields(userID, "adress", txt.replaceAll(DataBase.getUsFileds(userID, "city") + ",", "").replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
                    }
                    if (DataBase.getUsFileds(userID, "last_adress").equals(txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""))) {
                        DataBase.setUsFields(userID, "adress", DataBase.getUsFileds(userID, "last_adress"));
                        DataBase.setUsFields(userID, "last_adress", adr);
                    }
                }
                if (bot.user_edit_adress.containsKey(userID) && bot.user_edit_adress.get(userID)) {
                    bot.user_wait_adress.remove(userID);
                    bot.user_edit_adress.remove(userID);
                    bot.sendMsg(msg, "Проверьте правильность ввода: "
                            + "\nВаш адрес: " + DataBase.getUsFileds(userID, "adress")
                            + "\nВаша модель: " + bot.user_model.get(userID), "prov_info");
                    return true;
                }
                bot.user_wait_adress.remove(userID);
                if (txt.equals("Я приеду сам"))
                    bot.sendMsg(msg, "Спасибо, ожидаем Вас.", bot.user_is_ost_price.containsKey(userID) ? "" : "model");
                else
                    bot.sendMsg(msg, "Спасибо за предоставление адреса!", bot.user_is_ost_price.containsKey(userID) ? "" : "model");
                bot.user_wait_model.put(userID, true);
                if (bot.user_is_ost_price.containsKey(userID)) {
                    bot.handZayav(userID, msg, bot.user_is_ost_model.get(userID));
                } else bot.sendZOstZ(userID, msg);
                bot.user_is_ost_price.remove(userID);
                return true;
            } else {
                bot.sendMsg(msg, "Сначала завершите заполнение текущей заявки! Введите адрес", "adress");
                return true;
            }
        }
        return false;
    }


    public Boolean handleUserCompanyName(Long userID, String text, Message msg){
        if (bot.user_wait_company_name.containsKey(userID) && bot.user_wait_company_name.get(userID) == true) {
            DataBase.setUsFields(userID, "company_name", text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
            bot.user_wait_company_name.remove(userID);
            bot.user_wait_adress.put(userID, true);
            bot.sendMsg(msg, DataBase.getUserName(Math.toIntExact(userID)) + ", укажите пожалуйста адрес в поле ввода и отправьте мне, либо выберите подходящий вариант ниже.Адрес нужендля выезда курьера за заявкой. ", "adress");
            bot.sendToLogChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + " указал наименование своей компании. Он из компании: " + text);
            bot.sendToUserInfoChanel("Пользователь " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + " указал наименование своей компании. Он из компании: " + text);
            return true;
        }
        return false;
    }

    public Boolean handlePersonalMsgs(Long userID, String txt, Message msg){
        if (DataBase.isPersonal(userID) && bot.pers_is_z_saved.containsKey(userID)) {
            if (txt.contains("/")) {
                String[] spl = txt.split("/");
                DataBase.setUsFields(userID, "company_name", spl[0]);
                DataBase.setUsFields(userID, "name", spl[1]);
                DataBase.setUsFields(userID, "phone", spl[2]);
                bot.sendMsgToUser(userID, "Продолжайте :-)");
                if (bot.user_tema.get(userID).equals("Заправка картриджа"))
                    bot.handlOstUsZaprZayav(msg, userID);
                else bot.handlOstUsRemZayav(msg, userID);
            } else {
                bot.sendMsg(Math.toIntExact(userID), "Пишите через разделитель / !!!", "cancelvvod");
            }
            return true;
        }
        if (DataBase.isPersonal(userID) && txt.startsWith("/") && !txt.contains("/start")) {
            int nz = bot.pi(txt.split("/")[1]);
            String texx = txt.split("/")[2];
            if (DataBase.getZFields(nz, "theme").contains("Заправка"))
                texx += "\nСтоимость восстановления включает гарантию на данное восстановление с заправкой и две последующие заправки (детальнее по гарантии можно узнать у менеджера)";
            bot.sendMsg(msg, texx, "vosst=" + nz);
            return true;
        }
        return false;
    }

}
