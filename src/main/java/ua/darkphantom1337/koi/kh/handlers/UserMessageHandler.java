package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.api.objects.Message;
import ua.darkphantom1337.koi.kh.*;
import ua.darkphantom1337.koi.kh.entitys.Corporation;
import ua.darkphantom1337.koi.kh.entitys.Order;
import ua.darkphantom1337.koi.kh.entitys.User;

import java.io.IOException;
import java.util.*;

public class UserMessageHandler {

    private Bot bot;

    public UserMessageHandler(Bot bot) {
        this.bot = bot;
    }

    public Boolean handleMessage(Message msg, Long user_id) {
        try {
         /*   List<Long> all_user_id = DataBase.getAllUserId();
            if (!all_user_id.contains(user_id)) {
                all_user_id.add(user_id);
                DataBase.setUFields(3, "val1", bot.u.longToString(all_user_id, ";"));
            }*/
            if (DataBase.isRegUser(user_id) == false) {
                handleStart("/start", msg, user_id);
                /*bot.info("User " + user_id + " not registered!");
                bot.sendToLogChanel("Пользователь "  +  msg.getFrom().getId() + " " + msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName() + " написал ПЕРВОЕ сообщение боту!");
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
                DataBase.setUsFields(user_id, "reg_time", bot.u.getTime("HH:mm:ss"));*/
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean checkIsRegister(User user, String text) {
        if (DataBase.isRegUser(user.getUID())) {
            if (!DataBase.isSetMainRegisterInfo(Math.toIntExact(user.getUID()))) {
                if (text.equals("Написать менеджеру")) {
                    user.sendMessage("Нажмите на кнопку ниже чтобы перейти в чат с нашим менеджером", "mened");
                    return true;
                }
                if (text.equals("Адрес/Контакты")) {
                    user.sendMessage("Телефоны: "
                                    + "\n- +380 50 03 444 03 "
                                    + "\n- +380 63 03 444 03 "
                                    + "\n- +380 68 03 444 03 "
                                    + "\n- +380 57 765 44 03 "
                                    + "\n Сайт: www.koi.in.ua"
                                    + "\n Email: mail@koi.in.ua"
                                    + "\n Адрес: ул. Пушкинская, 65/1  (вход с Пушкинского въезда, станция метро Пушкинская)"
                            , "contact");
                    return true;
                }
               /* if (user.getUserAction().equals("user_wait_name")) {
                    user.sendMessage("Пожалуйста введите Ваше имя в поле ввода и отправьте мне \uD83D\uDE00");
                    return true;
                }*/
                if (user.getUserAction().equals("user_wait_name")) {
                    user.setUserAction("main");
                    String name = text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ)(-]", "");
                    DataBase.saveUser(Math.toIntExact(user.getID()), null, "Не указано", name);
                    if (user.isPersonal()) {
                        String vc = DataBase.getPerFields(user.getID(), "v_id");
                        if (vc.equals("manager"))
                            user.sendMessage(text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "") + ", вы менеджер компании 'KOI'! У вас своё особое меню управления, для перехода в меню пользователя нажмите самую нижнюю кнопку.", "MainManagerMenu");
                        if (vc.equals("admin") || vc.equals("owner"))
                            user.sendMessage(text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "") + ", вы администратор компании 'KOI'! У вас своё особое меню управления, для перехода в меню пользователя нажмите самую нижнюю кнопку.", "MainAdminMenu");
                        bot.sendToLogChanel("Пользователь TID: " + user.getTID() + " UID: " + user.getUID() + " указал что его зовут " + name);
                        bot.sendToUserInfoChanel("Пользователь TID: " + user.getTID() + " UID: " + user.getUID() + " указал что его зовут " + name);
                        return true;
                    }
                    bot.sendToLogChanel("Пользователь TID: " + user.getTID() + " UID: " + user.getUID() + " указал что его зовут " + name);
                    bot.sendToUserInfoChanel("Пользователь TID: " + user.getTID() + " UID: " + user.getUID() + " указал что его зовут " + name);
                    user.sendMessage(name + ", теперь Вы можете воспользоваться всеми возможностями бота \uD83D\uDC4C", "main");
                    return true;
                }
                user.sendMessage("Вы ещё не завершили регистрацию. \uD83D\uDE15");
                user.setUserAction("user_wait_phone");
                bot.user_wait_phone.put(user.getTID(), true);
                user.sendMessage("Для удобства в дальнейшем общении прошу поделиться Вашим контактом.(Кнопка отправить контакт ниже ↓)", "send_contact");
                user.setUserAdress("Не указано");
                DataBase.setUsFields(user.getUID(), "last_model", "Не указано");
                DataBase.setUsFields(user.getUID(), "last_model_rem", "Не указано");
                DataBase.setUsFields(user.getUID(), "dr", "Не указано");
                DataBase.setUsFields(user.getUID(), "city", "Харьков");
                DataBase.setUsFields(user.getUID(), "privilege", "USER");
                DataBase.setUsFields(user.getUID(), "reg_date", bot.bot.u.getDate("dd/MM/yyyy"));
                DataBase.setUsFields(user.getUID(), "reg_time", bot.bot.u.getTime("HH:mm:ss"));
                return true;
            }
        } else {
            user.sendMessage("Здравствуйте, я чат-бот сервисного центра KOI."
                    + "\nЗдесь вы можете: "
                    + "\n- Оставить заявку на заправку картриджа;"
                    + "\n- Оставить заявку на ремонт принтера;"
                    + "\n- Узнать ЦЕНЫ ЗАПРАВКИ КАРТРИДЖЕЙ;"
                    + "\n- Приобрести принтер/катридж/расходные материалы;"
                    + "\n- Посмотреть где мы находимся и как к нам добраться;"
                    + "\n- Связяться с нашим менеджером;");
            user.setUserAction("user_wait_phone");
            bot.user_wait_phone.put(user.getTID(), true);
            user.sendMessage("Для удобства в дальнейшем общении прошу поделиться Вашим контактом.(Кнопка отправить контакт ниже ↓)", "send_contact");
            user.setUserAdress("Не указано");
            DataBase.setUsFields(user.getUID(), "last_model", "Не указано");
            DataBase.setUsFields(user.getUID(), "last_model_rem", "Не указано");
            DataBase.setUsFields(user.getUID(), "dr", "Не указано");
            DataBase.setUsFields(user.getUID(), "city", "Харьков");
            DataBase.setUsFields(user.getUID(), "privilege", "USER");
            DataBase.setUsFields(user.getUID(), "reg_date", bot.bot.u.getDate("dd/MM/yyyy"));
            DataBase.setUsFields(user.getUID(), "reg_time", bot.bot.u.getTime("HH:mm:ss"));
            return true;
        }
        return false;
    }

    public Boolean handleStart(String text, Message msg, Long user_id) {
        if (text.equals("/start")) {
            if (DataBase.isRegUser(user_id)) {
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
            } else {

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

    public Boolean handleUserReklamComm(User user, String text) {
        Long userID = user.getUID();
        if ((DataBase.getUserStr("action", userID) != null && DataBase.getUserStr("action", userID).startsWith("user_wait_reklam_comm"))) {
            Long nz = Long.parseLong(DataBase.getUserStr("action", userID).split("/")[1]);
            Order order = new Order(nz);
            if (!text.equals("Главное меню")) {
                DataBase.setUserStr("action", Math.toIntExact(userID), "main");
                DataBase.setZFields(Math.toIntExact(nz), "status", "Поступила");
                order.addDescriptions("REKLAMACIYA_Text-" + text.replaceAll(";", "%"));
                order.setAllStatuses(new ArrayList<String>());
                order.addStatuses("Заявка на рекламацию подана.");
                try {
                    bot.sendToChanelReklamaciya((long) nz, order.getTheme() + "\nОписание дефекта: " + text, order.getModel(),
                            DataBase.getUserType(Math.toIntExact(userID)), order.getName(),
                            order.getPhone());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                bot.sendMsgToUser(userID, "Спасибо! Заявка на рекламацию передана. ", "main");
                return true;
            } else {
                try {
                    order.setAllStatuses(new ArrayList<String>());
                  order.addStatuses("Заявка на рекламацию подана.");
                    bot.sendToChanelReklamaciya((long) nz, order.getTheme() + "\nОписание дефекта: " + text, order.getModel(),
                            DataBase.getUserType(Math.toIntExact(userID)), order.getName(),
                            order.getModel());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                DataBase.setUserStr("action", Math.toIntExact(userID), "main");
                return false;
            }
        }
        return false;
    }

    public Boolean handleUserName(User user, String text, Message msg) {
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

    public Boolean handleUserRateComm(User user, String text, Message msg) {
        Long userID = user.getUID();
        if (bot.user_wait_rate_comm.containsKey(userID) && bot.user_wait_rate_comm.get(userID) == true && bot.user_wait_rate_zn.containsKey(userID) && !bot.menus.contains(text)) {
            Order order = new Order(bot.user_wait_rate_zn.get(userID));
            order.addDescriptions("Comment|"+text);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.deleteMsg(user.getTID(), msg.getMessageId());
                    this.cancel();
                }
            }, 200 * 60);
            bot.user_wait_rate_comm.remove(userID);
            int msgi = bot.sendMsgToUser(user.getTID(), "Спасибо за комментарий!");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.deleteMsg(userID, msgi);
                    bot.deleteMsg(userID, bot.user_wait_rate_zn_тsg.get(userID));
                    bot.user_wait_rate_zn_тsg.remove(userID);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (order.getStatus().equals("Завершена|Опрошена")) {
                                DataBase.setZFields(bot.user_wait_rate_zn.get(userID), "status", "Завершена|Опрошена|Архив");
                                if (order.getRate() <= 4) {
                                    bot.sendToChanel((long) bot.user_wait_rate_zn.get(userID));
                                }
                                String[] msgs = order.getString("msg_ids").split(",");
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

    public Boolean handleUserAdress(User user, String txt, Message msg) {
        if (bot.user_wait_adress.containsKey(user.getUID()) && bot.user_wait_adress.get(user.getUID()) == true) {
            if (!bot.menus.contains(txt)) {
                if (txt.equals("Главное меню")) {
                    bot.user_wait_model.remove(user.getUID());
                    bot.user_wait_adress.remove(user.getUID());
                    bot.sendMsg(msg, "Главное меню", "main");
                    return true;
                }
                String adr = user.getUserAdres().replaceAll(user.getUserCity() + ", ", "");
                adr = adr.startsWith(" ") ? adr.substring(1) : adr;
                String lastAddress = user.getUserLastAdres();
                if (lastAddress != null && lastAddress.startsWith(" "))
                    lastAddress = lastAddress.substring(1);
                user.setUserLastAdress(lastAddress);
                txt = txt.replaceAll(user.getUserCity() + ", ", "");
                if (txt.equals("Я приеду сам")) {
                    if (adr.equals("Не указано")) {
                        user.setUserLastAdress("Не указано");
                        lastAddress = "Не указано";
                        if (lastAddress.equals(txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""))) {
                            user.setUserAdress(lastAddress);
                            user.setUserLastAdress(adr);
                        }
                        if (!adr.equals(txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "")))
                            user.setUserAdress( txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));

                    } else {
                        user.setUserLastAdress(adr);
                    }
                } else {

                    if (!adr.equals(txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""))) {
                        user.setUserLastAdress(adr);
                       user.setUserAdress(txt.replaceAll(user.getUserCity() + ",", "").replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
                    }
                    if (user.getUserLastAdres().equals(txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""))) {
                      user.setUserAdress(user.getUserLastAdres());
                        user.setUserLastAdress(adr);
                    }
                }
                if (bot.user_edit_adress.containsKey(user.getUID()) && bot.user_edit_adress.get(user.getUID())) {
                    bot.user_wait_adress.remove(user.getUID());
                    bot.sendMsg(msg, "Проверьте правильность ввода: "
                            + "\nВаш адрес: " + user.getUserAdres()
                            + "\nВаша модель: " + bot.getSelectedOrderModels(user.getUID()), "prov_info");
                    return true;
                }
                bot.user_wait_adress.remove(user.getUID());
                if (txt.equals("Я приеду сам"))
                    bot.sendMsg(msg, "Спасибо, ожидаем Вас.", bot.user_is_ost_price.containsKey(user.getUID()) ? "" : "model");
                else
                    bot.sendMsg(msg, "Спасибо за предоставление адреса!", bot.user_is_ost_price.containsKey(user.getUID()) ? "" : "model");
                bot.user_wait_model.put(user.getUID(), true);
                bot.updateOrderModels(user.getUID());
                if (bot.user_is_ost_price.containsKey(user.getUID())) {
                    bot.handZayav(user, msg, bot.user_is_ost_model.get(user.getUID()));
                } else bot.sendZOstZ(user, msg);
                bot.user_is_ost_price.remove(user.getUID());
                return true;
            } else {
                bot.sendMsg(msg, "Сначала завершите заполнение текущей заявки! Введите адрес", "adress");
                return true;
            }
        }
        return false;
    }


    public Boolean handleUserCompanyName(User user, String text, Message msg) {
        Long userID = user.getUID();;
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

    public Boolean handlePersonalMsgs(User user, String txt, Message msg) {
        Long userID = user.getUID();
        if (!DataBase.isPersonal(userID))
            return false;
        if (bot.pers_is_z_saved.containsKey(userID)) {
            if (txt.contains("/")) {
                String[] spl = txt.split("/");
                DataBase.setUsFields(userID, "company_name", spl[0]);
                DataBase.setUsFields(userID, "name", spl[1]);
                DataBase.setUsFields(userID, "phone", spl[2]);
                bot.sendMsgToUser(userID, "Продолжайте :-)");
                if (bot.user_tema.get(userID).equals("Заправка картриджа"))
                    bot.handlOstUsZaprZayav(msg, user);
                else bot.handlOstUsRemZayav(msg, user);
            } else {
                bot.sendMsg(Math.toIntExact(user.getTID()), "Пишите через разделитель / !!!", "cancelvvod");
            }
            return true;
        }
        if (user.getUserAction().equals("wait_reconcile_text")) {
            String texx = txt;
           /* if (new Order(nz).getTheme().contains("Заправка"))
                texx += "\nСтоимость восстановления включает гарантию на данное восстановление с заправкой и две последующие заправки (детальнее по гарантии можно узнать у менеджера)";
            */
            user.setUserAction("main");
            bot.sendMsg(msg, texx, "vosst=" + bot.u.objectToString(bot.getSelectedOrderForReconsile(userID),"/"));

            return true;
        }
        return false;
    }

    public boolean handleCurrentOrdersHystory(Long id) {
        if (DataBase.isCorporationWorker(id)) {
            Corporation corp = new Corporation(DataBase.getCorporationID(id));
            if (corp.getOrdersID().size() <= 0) {
                bot.sendMsgToUser(id, "Вы ещё не оставляли заявки!", "no_z");
            } else {
                for (Long nzz : corp.getOrdersID()) {
                    int nz = Math.toIntExact(nzz);
                    Order order = new Order(nz);
                    String status = order.getStatus();
                    if (!status.contains("Завершена")) {
                        String current_statuses = "\nℹ️ Заявка №" + nz;
                        for (String st : order.getAllStatuses())
                            current_statuses += "\n" + st;
                        bot.sendMsgToUser(id, "Заявка №" + nz + " от " + order.getDate() +
                                "\nТема: " + order.getTheme() + "\nМодель: " + order.getModel() +
                                "\nАдрес: " + order.getAddress()
                                + current_statuses, "reklamaciya/" + nz + "/" + status);
                    }
                }
            }
            return true;
        }
        String allz = DataBase.getUsFileds(id, "all_z");
        if (allz == null || allz.equals("NULL") || allz.equals("") || allz.equals(" ")) {
            bot.sendMsgToUser(id, "Вы ещё не оставляли заявки!", "no_z");
            return true;
        }
        List<String> zayavs = new ArrayList<String>(Arrays.asList(allz.split(",")));
        for (String s : zayavs) {
            try {
                int nz = bot.pi(s);
                Order order = new Order(nz);

                String status = order.getStatus();
                if (!status.contains("Завершена")) {
                    String current_statuses = "\nℹ️ Заявка №" + nz;
                    for (String st : order.getAllStatuses())
                        current_statuses += "\n" + st;
                    bot.sendMsgToUser(id, "Заявка №" + nz + " от " + order.getDate() +
                            "\nТема: " + order.getTheme() + "\nМодель: " + order.getModel() +
                            "\nАдрес: " + order.getAddress()
                            + current_statuses, "reklamaciya/" + nz + "/" + status);
                }
            } catch (Exception ignored) {
                continue;
            }
        }
        return true;
    }

    public boolean handleEndOrdersHystory(Long id) {
        if (DataBase.isCorporationWorker(id)) {
            Corporation corp = new Corporation(DataBase.getCorporationID(id));
            if (corp.getOrdersID().size() <= 0) {
                bot.sendMsgToUser(id, "Вы ещё не оставляли заявки!", "no_z");
            } else {
                for (Long nzz : corp.getOrdersID()) {
                    int nz = Math.toIntExact(nzz);
                    Order order = new Order(nz);

                    String status = order.getStatus();
                    if (status.contains("Завершена")) {
                        String current_statuses = "\nℹ️ Заявка №" + nz;
                        for (String st : order.getAllStatuses())
                            current_statuses += "\n" + st;
                        bot.sendMsgToUser(id, "Заявка №" + nz + " от " + order.getDate() +
                                "\nТема: " + order.getTheme() + "\nМодель: " + order.getModel() +
                                "\nАдрес: " + order.getAddress()
                                + current_statuses, "reklamaciya/" + nz + "/" + status);
                    }
                }
            }
            return true;
        }
        String allzz = DataBase.getUsFileds(id, "all_z");
        if (allzz == null || allzz.equals("NULL") || allzz.equals("") || allzz.equals(" ")) {
            bot.sendMsgToUser(id, "Вы ещё не оставляли заявки!", "no_z");
            return true;
        }
        List<String> zayavvs = new ArrayList<String>(Arrays.asList(allzz.split(",")));
        for (String s : zayavvs) {
            try {
                int nz = bot.pi(s);
                Order order = new Order(nz);

                String status = order.getStatus();
                if (status.contains("Завершена")) {
                    String current_statuses = "\nℹ️ Заявка №" + nz;
                    for (String st : order.getAllStatuses())
                        current_statuses += "\n" + st;
                    bot.sendMsgToUser(id, "Заявка №" + nz + " от " + order.getDate() +
                            "\nТема: " + order.getTheme() + "\nМодель: " + order.getModel() +
                            "\nАдрес: " + order.getAddress()
                            + current_statuses, "reklamaciya/" + nz + "/" + status);
                }
            } catch (Exception ignored) {
                continue;
            }
        }
        return true;
    }

    public boolean handleUserSeeModel(User user, String text) {
        Long user_id = user.getUID();;
        if (bot.user_wait_semodel.containsKey(user_id)) {
            if (bot.user_stype.get(user_id).equals("Print")) {
                List<String> all = DataBase.getAllPrices(bot.prnt_model.get(user_id), true, text);
                if (all.size() == 0) {
                    bot.sendMsg(Math.toIntExact(user.getTID()), "Ничего не найдено!", "main");
                } else {
                    int i = 1;
                    for (String s : all) {
                        if (i <= 20) {
                            String[] spl = s.split("/");
                            bot.sendMsg(Math.toIntExact(user.getTID()), "Модель принтера: " + spl[0] + "\nМодель картриджа: " + spl[1] + "\nЦЕНА ЗАПРАВКИ: " + spl[2], "ost_zmodel/" + spl[3]);
                        } else {
                            bot.sendMsg(Math.toIntExact(user.getTID()), "Пожалуйста повторите попытку, введя более точное значение.", "main");
                            return true;
                        }
                        i++;
                    }
                }
            } else {
                Map<String, List<String>> grp = DataBase.getAllPricesForCartr(bot.prnt_model.get(user_id), text);
                for (String key : grp.keySet()) {
                    String printers = "\n";
                    int i = 1;
                    for (String s : grp.get(key)) {
                        printers += "- " + s + (i <= grp.get(key).size() - 1 ? "\n" : "");
                    }
                    bot.sendMsg(user.getTID().intValue(), "Модель картриджа: " + key.replaceAll("¦", "/") + "\nПодходит для следующих моделей принтеров: " + printers.replaceAll("¦", "/") + "\nЦЕНА ЗАПРАВКИ: " + (DataBase.getF("price_refill", DataBase.getIdForCart(key.replaceAll("¦", "/"))) == null ? "Уточняйте у менеджера" : DataBase.getF("price_refill", DataBase.getIdForCart(key.replaceAll("¦", "/")))), "ost_kart%" + DataBase.getIdForCart(key.replaceAll("¦", "/")));
                }
            }
            bot.sendMsg(user.getTID().intValue(), "Если в списке нет Вашей модели, повторите поиск, и введите более подробное название либо свяжитесь с нашим менеджером.", "research_price");
            bot.user_wait_semodel.remove(user_id);
            bot.user_stype.remove(user_id);
            bot.prnt_model.remove(user_id);
            return true;
        }
        return false;
    }

    public boolean handleUserNewOrder(User user, Boolean isFill) {
        if (DataBase.isPersonal(user.getUID()) && bot.pers_is_z_saved.containsKey(user.getUID()) == false) {
            user.sendMessage("Введите название компании/контактное лицо/контактный номер телефона и отправьте мне, после чего продолжите подавать заявку.", "cancelvvod");
            // admin_data.put(id, DataBase.getUserStr("company_name", Math.toIntExact(id)) + "//" + DataBase.getUserStr("name", Math.toIntExact(id)) + "//" + DataBase.getUserStr("phone", Math.toIntExact(id)));
            bot.pers_is_z_saved.put(user.getUID(), true);
            return true;
        } else
            bot.pers_is_z_saved.remove(user.getUID());
        /*if (DataBase.getAllBlackUserId().contains(user.getUID())) {
            user.sendMessage("Извините, но Вам запрещено производить какие-либо действия! Для уточнения деталей напишите менеджеру...", "mened");
            return true;
        }*/
        if (!bot.z_kolvo.containsKey(user.getUID()))
            bot.z_kolvo.put(user.getUID(), 0);
        String userType = user.getUserType();
        Integer zKolv = bot.z_kolvo.get(user.getUID());
        if (!DataBase.getUsFileds(user.getUID(), "privilege").equals("VIP") &&
                userType.equals("Частное лицо") ? zKolv >= 3 :
                userType.equals("Компания") ? zKolv >= 10 : false) {
            user.sendMessage("Вы исчерпали лимит заявок на день! Если нужно подать заявку свяжитесь с нашим менеджером!", "mened");
            return true;
        } else {
            if (isFill)
                bot.user_tema.put(user.getUID(), "Заправка картриджа");
            else bot.user_tema.put(user.getUID(), "Ремонт принтера");
            if (userType.equals("Не указано")) {
                user.sendMessage("Вы представитель компании или частное лицо? Для компаний у нас действует специальная система скидок и бонусов!", "type");
                return true;
            }
            bot.user_wait_adress.put(user.getUID(), true);
            user.sendMessage(user.getUserName() + ", укажите пожалуйста адрес в поле ввода и отправьте мне, либо выберите подходящий вариант ниже.Адрес нужен для выезда курьера за заявкой. ", "adress");
        }
        return true;
    }

}
