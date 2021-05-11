package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.darkphantom1337.koi.kh.*;
import ua.darkphantom1337.koi.kh.buttons.InlineButtons;
import ua.darkphantom1337.koi.kh.database.TidToUidTable;
import ua.darkphantom1337.koi.kh.entitys.Cartridge;
import ua.darkphantom1337.koi.kh.entitys.Corporation;
import ua.darkphantom1337.koi.kh.entitys.Order;
import ua.darkphantom1337.koi.kh.entitys.User;
import ua.darkphantom1337.koi.kh.handlers.callback.admin.AdminCallbackHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AdminHandler {

    private List<Long> adminstrators = Arrays.asList(521258581L, 556349297L, 1L);
    private HashMap<Long, String> adm_where = new HashMap<>();
    private HashMap<Long, String> adm_rtype = new HashMap<>();
    private HashMap<Long, Boolean> adm_wait_values = new HashMap<>();
    private HashMap<Long, Integer> adm_nv = new HashMap<>();
    private HashMap<Long, Long> selected_corporation = new HashMap<>();
    public HashMap<Long, Long> admins_added_personal = new HashMap<>();
    private List<Long> admins_wait_corporation_name = new ArrayList<>();
    private List<Long> admins_wait_new_corporation_name = new ArrayList<>();
    private List<Long> admins_wait_corporation_adress_name = new ArrayList<>();
    private List<Long> admins_wait_employee_phone = new ArrayList<>();
    private List<Long> admins_wait_personal_phone = new ArrayList<Long>();
    private List<Long> admins_wait_select_new_personal_position = new ArrayList<Long>();

    private Bot bot;

    public AdminHandler(Bot bot) {
        this.bot = bot;
    }

    public boolean handleTextMessage(User user, String text, Integer message_id) {
        Long user_id = user.getUID();
        if (handleNotPreparedMessage(user, text, message_id)) return true;
        if (handleAdminCommand(text, user)) return true;
        /*Главное меню*/
        if (text.equals("Перейти в меню пользователя")) {
            bot.sendMsgToUser(user.getTID(), "Вы перешли в главное меню пользователя \uD83D\uDCAA", "main");
            return true;
        }
        if (text.equals("Перейти в меню менеджера")) {
            bot.sendMsgToUser(user.getTID(), "Вы перешли в главное меню менеджера \uD83D\uDCAA", "Manager/MainMenu");
            return true;
        }
        if (text.equals("Вернуться в меню управления") || text.equals("Вернуться меню администратора")) {
            admins_wait_corporation_name.remove(user_id);
            admins_wait_employee_phone.remove(user_id);
            admins_wait_new_corporation_name.remove(user_id);
            admins_wait_corporation_adress_name.remove(user_id);
            admins_wait_personal_phone.remove(user_id);
            bot.sendMsgToUser(user.getTID(), "Вы вернулись в меню управления \uD83D\uDE0A", "ADMIN/MainMenu");
            return true;
        }
        if (text.equals("Управление корпорациями")) {
            bot.sendMsgToUser(user.getTID(), "Вы перешли в меню управления корпорациям \uD83D\uDE09", "ADMIN/Corporations/MainMenu");
            return true;
        }
        if (text.equals("Управление QR-кодами")) {
            bot.sendMsgToUser(user.getTID(), "Вы перешли в меню управления QR-кодами \uD83D\uDE0E", "ADMIN/QR/MainMenu");
            return true;
        }
        if (text.equals("Управление персоналом")) {
            bot.sendMsgToUser(user.getTID(), "Вы перешли в меню управления персоналом \uD83D\uDE09", "ADMIN/Personal/MainMenu");
            return true;
        }
        /* Меню корпораций */
        if (text.equals("Создать корпорацию")) {
            admins_wait_corporation_name.add(user_id);
            bot.sendMsgToUser(user.getTID(), "\uD83D\uDE0E Напишите в чат название для будущей корпорации.", "ADMIN/BackToMainMenu");
            return true;
        }
        if (text.equals("Удалить корпорацию")) {
            bot.sendMsgToUser(user.getTID(), "\uD83D\uDE15 Нажмите на корпорацию которую нужно удалить.", "ADMIN/Corporations/Delete");
            return true;
        }
        if (text.equals("Настройка корпорации")) {
            bot.sendMsgToUser(user.getTID(), "\uD83D\uDE32 Выберите корпорацию для которой нужно произвести настройку.", "ADMIN/Corporations/Settings");
            return true;
        }
        if (text.equals("Информация о корпорациях")) {
            return handleCorporationsInfo(user_id);
        }
        if (text.equals("Добавить сотрудника")) {
            if (!getSelectedCorporation().containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                return true;
            }
            admins_wait_employee_phone.add(user_id);
            bot.sendMsgToUser(user.getTID(), "Напишите номер телефона сотрудника и отправьте мне \uD83D\uDC47", "ADMIN/BackToMainMenu");
            return true;
        }
        if (text.equals("Удалить сотрудника")) {
            if (!getSelectedCorporation().containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                return true;
            }
            bot.sendMsgToUser(user.getTID(), "Нажмите на сотрудника которого нужно удалить.", "ADMIN/Corporations/DeleteEmployee");
            return true;
        }
        if (text.equals("Добавить адрес")) {
            if (!getSelectedCorporation().containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                return true;
            }
            admins_wait_corporation_adress_name.add(user_id);
            bot.sendMsgToUser(user.getTID(), "Напишите адрес который нужно добавить в корпорацию и отправьте мне \uD83D\uDC47", "ADMIN/BackToMainMenu");
            return true;
        }
        if (text.equals("Удалить адрес")) {
            if (!getSelectedCorporation().containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                return true;
            }
            bot.sendMsgToUser(user.getTID(), "Нажмите на адресс который нужно удалить \uD83D\uDC47\uD83D\uDC47", "ADMIN/Corporations/DeleteAdress");
            return true;
        }
        if (text.equals("Изменить имя корпорации")) {
            if (!getSelectedCorporation().containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                return true;
            }
            admins_wait_new_corporation_name.add(user_id);
            bot.sendMsgToUser(user.getTID(), "Введите новое имя корпорации ниже и отправьте мне. \uD83D\uDE3C", "ADMIN/BackToMain");
            return true;
        }
        if (text.equals("Добавить персонал")) {
            admins_wait_personal_phone.add(user_id);
            bot.sendMsgToUser(user.getTID(), "Напишите номер телефона нового члена персонала бота KOI-KH и отправьте мне \uD83D\uDC47", "ADMIN/BackToMainMenu");
            return true;
        }
        if (text.equals("Настройки персонала")) {
            bot.sendMsgToUser(user.getTID(), "Команды для настройки прав персонала \uD83D\uDC47"
                    + "\n1. Разрешить согласовывать - !SOGLASOVANIE/ALLOW/НомерТелефона"
                    + "\n2. Запретить согласовывать - !SOGLASOVANIE/DENY/НомерТелефона", "ADMIN/BackToMainMenu");
            return true;
        }
        if (text.equals("Создать QR-код")) {
            bot.sendMsgToUser(user.getTID(), "Создайте QR код используя команду: !NEWQR/МодельКартриджа/МодельПринтера/Адрес/НомерВладельца \uD83D\uDC47", "ADMIN/BackToMainMenu");
            return true;
        }
        if (text.equals("Изменить QR-код")) {
            bot.sendMsgToUser(user.getTID(), "Измените QR код используя команду: !CHANGEQR/НомерКартриджа/НоваяМодельКартриджа/НоваяМодельПринтера/НовыйАдрес \nЕсли не нужно обновлять какое-то поле, оставьте его пустым пример: !ChangeQR/НомерКартриджа/НоваяМодельКартриджа \uD83D\uDC47", "ADMIN/BackToMainMenu");
            return true;
        }
        if (text.equals("Удалить QR-код")) {
            bot.sendMsgToUser(user.getTID(), "Удалите QR код используя команду: !DELETEQR/НомерКартриджа \uD83D\uDC47", "ADMIN/BackToMainMenu");
            return true;
        }
        if (text.equals("Информация о QR-кодах")) {
            String s = "Информация о QR-кодах: ";
            for (int i = 1; i <= DataBase.getUtilsField(18L, "val"); i++) {
                Cartridge cartridge = new Cartridge((long) i);
                if (cartridge.getModel() != null)
                    s += "\nКартридж №" + i + " " + cartridge.getModel() + " - " + cartridge.getPrinterModel() + " - " + cartridge.getAddress() + " - " + (cartridge.getOwnersID().size() > 0 ? new User(cartridge.getOwnersID().get(0)).getUserPhone() : "НЕТУ ВЛАДЕЛЬЦА");
            }
            bot.sendMsgToUser(user.getTID(), s, "ADMIN/BackToMainMenu");
            return true;
        }
        if (text.equals("Удалить персонал")) {
            bot.sendMsgToUser(user.getTID(), "Нажмите на сотрудника бота KOI-KH которого нужно удалить.", "ADMIN/Personal/DeleteEmployee");
            return true;
        }
        if (text.equals("Информация о персонале")) {
            bot.sendMsgToUser(user.getTID(), "Ниже написаны все работники KOI-KH", "ADMIN/Personal/MainMenu");
            for (Long personalID : DataBase.getAllPersonalID())
                bot.sendMsgToUser(user.getTID(), "Работник: " + DataBase.getPerFields(personalID, "name")
                        + "\nДолжность: " + DataBase.getPerFields(personalID, "position")
                        + "\nТелефон: " + DataBase.getPerFields(personalID, "phone")
                        + "\nОписание: " + DataBase.getPerFields(personalID, "description"), "ADMIN/Personal/MainMenu");
            return true;
        }
        if ((text.toLowerCase().startsWith("!сменить статус")) && text.contains("/")) {
            return handleChangeStatusCommand(text, user_id);
        }
        return false;
    }

    public boolean handleAdminCommand(String text, User user) {
        Long user_id = user.getUID();
        if (text.startsWith("!")) {
            if (handleNEWZCommand(user, text, "Admin")) return true;
            if (handleRECOVERYCommand(user, text, "Admin")) return true;
            if (handleSoglasovanieCommand(user, text, "Admin")) return true;
            if (handleReklamaciaCommand(user, text, "Admin")) return true;
            if (handleNewQRCommand(user, text)) return true;
            if (handleChangeQRCommand(user, text)) return true;
            if (handleDeleteQRCommand(user, text)) return true;
        }
        return false;
    }

    public boolean handleDeleteQRCommand(User user, String text) {
        if (text.startsWith("!DELETEQR")) {
            String[] data = text.split("/");
            if (data.length >= 2) {
                try {
                    String s = "Картридж удалён из профилей: ";
                    Cartridge cartridge = new Cartridge(Long.parseLong(data[1]));
                    for (Long ownerid : cartridge.getOwnersID()) {
                        new User(ownerid).remCartridgeID(cartridge.getID());
                        s += "\n" + ownerid + "(" + new User(ownerid).getUserPhone() + ") -> удалён";
                    }
                    cartridge.delete();
                    bot.sendMsgToUser(user.getTID(), s, "ADMIN/QR/MainMenu");
                    return true;
                } catch (Exception e) {
                    bot.sendMsgToUser(user.getTID(), "Ошибка при создании картриджа! Ошибка: " + e.getMessage(), "ADMIN/QR/MainMenu");
                    return true;
                }

            } else {
                bot.sendMsgToUser(user.getTID(), "Измените QR код используя команду: !CHANGEQR/НомерКартриджа/НоваяМодельКартриджа/НоваяМодельПринтера/НовыйАдрес \nЕсли не нужно обновлять какое-то поле, оставьте его пустым пример: !ChangeQR/НомерКартриджа/НоваяМодельКартриджа \uD83D\uDC47", "ADMIN/BackToMainMenu");
                return true;
            }
        }
        return false;
    }

    public boolean handleChangeQRCommand(User user, String text) {
        if (text.startsWith("!CHANGEQR")) {
            String[] data = text.split("/");
            if (data.length >= 3) {
                try {
                    String s = "Данные по картриджу обновлены:";
                    Cartridge cartridge = new Cartridge(Long.parseLong(data[1]));
                    if (data.length >= 3)
                        if (!data[2].equals("") && !data[2].equals(" ")) {
                            s += "\n" + cartridge.getModel() + " -> " + data[2];
                            cartridge.set("cartridge_model", data[2]);
                        }
                    if (data.length >= 4)
                        if (!data[3].equals("") && !data[3].equals(" ")) {
                            s += "\n" + cartridge.getPrinterModel() + " -> " + data[3];
                            cartridge.set("printer_model", data[3]);
                        }
                    if (data.length >= 5)
                        if (!data[4].equals("") && !data[4].equals(" ")) {
                            s += "\n" + cartridge.getAddress() + " -> " + data[4];
                            cartridge.set("address", data[4]);
                        }
                    bot.sendMsgToUser(user.getTID(), s, "ADMIN/QR/MainMenu");
                    return true;
                } catch (Exception e) {
                    bot.sendMsgToUser(user.getTID(), "Ошибка при создании картриджа! Ошибка: " + e.getMessage(), "ADMIN/QR/MainMenu");
                    return true;
                }

            } else {
                bot.sendMsgToUser(user.getTID(), "Измените QR код используя команду: !CHANGEQR/НомерКартриджа/НоваяМодельКартриджа/НоваяМодельПринтера/НовыйАдрес \nЕсли не нужно обновлять какое-то поле, оставьте его пустым пример: !ChangeQR/НомерКартриджа/НоваяМодельКартриджа \uD83D\uDC47", "ADMIN/BackToMainMenu");
                return true;
            }
        }
        return false;
    }

    public boolean handleNewQRCommand(User user, String text) {
        if (text.startsWith("!NEWQR")) {
            Long user_id = user.getUID();
            String[] data = text.split("/");
            if (data.length >= 5) {
                Boolean valid_phone = false;
                Long ownerid = 0L;
                for (Long userid : DataBase.getAllUserId()) {
                    String uphone = DataBase.getUsFileds(userid, "phone");
                    if (uphone != null && uphone.equals(data[4])) {
                        valid_phone = true;
                        ownerid = userid;
                        break;
                    }
                }
                if (valid_phone) {
                    try {
                        Cartridge cartridge = new Cartridge(DataBase.getNextCartridgeID());
                        cartridge.create(data[1], data[2], data[3], new ArrayList<Long>(Arrays.asList(ownerid)));
                        new User(ownerid).addCartridgeID(cartridge.getID());
                        bot.sendDocument(new SendDocument().setNewDocument(DarkQRWriter.createQRCode(cartridge.getID()))
                                .setChatId(user_id).setReplyToMessageId(bot.sendMsgToUser(user.getTID(), "Картридж №" + cartridge.getID() + " успешно создан. ID Картриджа: " + cartridge.getID() + "\n" + "QR-код для картриджа №" + cartridge.getID() + " (" + cartridge.getModel() + ")", "ADMIN/QR/MainMenu")));
                        return true;
                    } catch (Exception e) {
                        bot.sendMsgToUser(user.getTID(), "Ошибка при создании картриджа! Ошибка: " + e.getMessage(), "ADMIN/QR/MainMenu");
                        return true;
                    }
                } else {
                    bot.sendMsgToUser(user.getTID(), "Пользователя с телефоном " + data[4] + " не сущевствует в базе KOI-KH.", "ADMIN/BackToMainMenu");
                    return true;
                }
            } else {
                bot.sendMsgToUser(user.getTID(), "Команда для создания QR-кода: !NEWQR/МодельКартриджа/МодельПринтера/Адрес/НомерВладельца \uD83D\uDC47", "ADMIN/BackToMainMenu");
                return true;
            }
        }
        return false;
    }

    public boolean handleNEWZCommand(User user, String text, String employee) {
        if (text.startsWith("!NEWZ") || text.startsWith("!NEWQRZ")) { //!NEWZ/НомерКлиент/Модель
            String[] data = text.split("/");
            boolean isqr = text.startsWith("!NEWQRZ");
            Long user_id = user.getUID();
            if (data.length >= 3) {
                Boolean valid_phone = false;
                Long ownerid = 0L;
                for (Long userid : DataBase.getAllUserId()) {
                    String uphone = DataBase.getUsFileds(userid, "phone");
                    if (uphone != null && (uphone.equals(data[1]) || uphone.equals("+" + data[1]) || uphone.equals("38" + data[1]) || uphone.equals("+38" + data[1]) || uphone.equals("+7" + data[1]))) {
                        valid_phone = true;
                        ownerid = userid;
                        break;
                    }
                }
                if (valid_phone) {
                    try {
                        if (!isqr) {
                            bot.user_tema.put(ownerid, "Заправка картриджа");
                            bot.user_model.put(ownerid, data[2]);
                            for (String s: data[2].split(";"))
                            UsersData.addSelectedOrderModel(ownerid, s);
                            bot.saveZayav(new User(new TidToUidTable(ownerid, false).getTelegramID()), null, null);
                            bot.sendMsgToUser(user.getTID(), "Заявка создана!", employee + "/MainMenu");
                            return true;
                        } else {
                            Long cartridgeID = Long.parseLong(data[2]);
                            Cartridge cartridge = new Cartridge(cartridgeID);
                            User userO = new User(ownerid);
                            if (userO.getCartridgesID().contains(cartridgeID)) {
                                DataBase.setUsFields(ownerid, "lastReadedCartridge", cartridgeID.intValue());
                                bot.user_tema.put(ownerid, "Заправка картриджа");
                                UsersData.addSelectedOrderModel(ownerid, cartridge.getModel());
                                bot.user_model.put(ownerid, cartridge.getModel());
                                user.setUserLastAdress(user.getUserAdres());
                                user.setUserAdress(cartridge.getAddress());
                                user.setUserAction("user_wait_qr");
                                bot.saveZayav(new User(new TidToUidTable(ownerid, false).getTelegramID()), null, null);
                                bot.sendMsgToUser(user.getTID(), "QR Заявка создана!", employee + "/MainMenu");
                            } else {
                                bot.sendMsgToUser(user.getTID(), "Картридж №" + cartridge.getID() + " не принадлежит этому клиенту!", employee + "/MainMenu");
                            }
                            return true;
                        }
                    } catch (Exception e) {
                        bot.sendMsgToUser(user.getTID(), "Ошибка при создании заявки! Ошибка: " + e.getMessage(), employee + "/MainMenu");
                        return true;
                    }
                } else {
                    bot.sendMsgToUser(user.getTID(), "Пользователя с телефоном " + data[2] + " не сущевствует в базе KOI-KH.", employee + "/BackToMainMenu");
                    return true;
                }
            } else {
                bot.sendMsgToUser(user.getTID(), "Команда для создания заявки: !NEWZ/НомерКлиент/Модель !NEWQRZ/НомерКлиент/НомерКартриджа\uD83D\uDC47", employee + "/BackToMainMenu");
                return true;
            }
        }
        return false;
    }

    public boolean handleSoglasovanieCommand(User user, String text, String employee) {
        if (text.startsWith("!SOGLASOVANIE")) { //!SOGLASOVANIE/Статус/НомерТелефона
            String[] data = text.split("/");
            Long user_id = user.getUID();
            if (data.length >= 3) {
                Boolean valid_phone = false;
                Long ownerid = 0L;
                for (Long userid : DataBase.getAllUserId()) {
                    String uphone = DataBase.getUsFileds(userid, "phone");
                    if (uphone != null && (uphone.equals(data[2]) || uphone.equals("+" + data[2]) || uphone.equals("38" + data[2]) || uphone.equals("+38" + data[2]))) {
                        valid_phone = true;
                        ownerid = userid;
                        break;
                    }
                }
                if (valid_phone) {
                    try {
                        String iddata = DataBase.getUFileds(20, "val1");
                        List<Long> usersID = new ArrayList<Long>();
                        if (iddata != null && !iddata.equals("") && !iddata.equals(" "))
                            usersID = Arrays.stream(iddata.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
                        if (data[1].equals("ALLOW")) {
                            usersID.add(ownerid);
                            DataBase.setUFields(20, "val1", bot.u.longToString(usersID, ";"));
                            bot.sendMsgToUser(user.getTID(), DataBase.getPerFields(ownerid, "name") + " теперь может согласовывать!", employee + "/MainMenu");
                        } else {
                            usersID.remove(ownerid);
                            DataBase.setUFields(20, "val1", bot.u.longToString(usersID, ";"));
                            bot.sendMsgToUser(user.getTID(), DataBase.getPerFields(ownerid, "name") + " теперь НЕ может согласовывать!", employee + "/MainMenu");
                        }
                        return true;
                    } catch (Exception e) {
                        bot.sendMsgToUser(user.getTID(), "Ошибка при изменении прав! Ошибка: " + e.getMessage(), employee + "/MainMenu");
                        return true;
                    }
                } else {
                    bot.sendMsgToUser(user.getTID(), "Пользователя с телефоном " + data[2] + " не сущевствует в базе KOI-KH.", employee + "/BackToMainMenu");
                    return true;
                }
            } else {
                bot.sendMsgToUser(user.getTID(), "Команда: !SOGLASOVANIE/Статус/НомерТелефона \uD83D\uDC47", employee + "/BackToMainMenu");
                return true;
            }
        }
        return false;
    }

    public boolean handleRECOVERYCommand(User user, String text, String employee) {
        if (text.startsWith("!RECOVERY")) { //!RECOVERY/CONFIRM/НомерЗаявки
            String[] data = text.split("/");
            Long user_id = user.getUID();
            if (data.length >= 3) {
                Long ownerid = new Order(bot.pi(data[2])).getUID();
                try {
                    Integer nz = bot.pi(data[2]);
                    if (data[1].equals("CONFIRM")) {
                        bot.handVosst(new Order(nz).getSubOrdersID().replaceAll(";","/"), "Telegram", "SOGLASOVANO");
                        bot.updateZStatus(nz, "Восстановление согласовано", "Вы подтвердили восстановления картриджа через телефон/месседжер.");
                        bot.sendMsgToUser(user.getTID(), "Восстановление согласовано по заявке " + nz + " сохранён!", employee + "/MainMenu");
                        return true;
                    }
                    if (data[1].equals("CANCEL")) {
                        bot.handVosst(new Order(nz).getSubOrdersID().replaceAll(";","/"), "Telegram", "CANCEL");
                        bot.updateZStatus(nz, "Отказ от восстановления", "Вы отказались от восстановления картриджа через телефон/месседжер.");
                        bot.sendMsgToUser(user.getTID(), "Отказ от восстановления по заявке " + nz + " сохранён!", employee + "/MainMenu");
                        return true;
                    }
                    bot.sendMsgToUser(user.getTID(), "Статуса " + data[1] + " нету. Есть CONFIRM и CANCEL.", employee + "/MainMenu");
                    return true;
                } catch (Exception e) {
                    bot.sendMsgToUser(user.getTID(), "Ошибка при создании заявки! Ошибка: " + e.getMessage(), employee + "/MainMenu");
                    return true;
                }

            } else {
                bot.sendMsgToUser(user.getTID(), "Команда для создания заявки: !RECOVERY/CONFIRM/НомерЗаявки или !RECOVERY/CANCEL/НомерЗаявки\uD83D\uDC47", employee + "/BackToMainMenu");
                return true;
            }
        }
        return false;
    }

    public boolean handleReklamaciaCommand(User user, String text, String employee) {
        if (text.startsWith("!REKLAM")) { //!REKLAM/НОМЕРЗАЯВКИ/Дефект
            String[] data = text.split("/");
            Long user_id = user.getUID();
            if (data.length >= 3) {
                try {
                    Integer nz = bot.pi(data[1]);
                    Order order = new Order(nz);
                    Long ownerid = order.getUID();
                    if (order.getDescriptions().contains("REKLAMACIYA_USE")) {
                        if (!(data.length >= 4 && data[3].equals("ALLOW"))) {
                            bot.sendMsgToUser(user.getTID(), "Рекламация по заявке уже была создана! Для повторного создания обратитесь к AV.", "main");
                            return true;
                        }
                    }
                    DataBase.setZFields(Math.toIntExact(nz), "status", "Поступила");
                    order.setDescriptions(new ArrayList<>());
                    order.addDescriptions("AD|Reklam|" + user_id);
                    order.addDescriptions("REKLAMACIYA_USE");
                    order.addDescriptions("REKLAMACIYA_Text-" + data[2].replaceAll(";", "%"));
                    order.setAllStatuses(new ArrayList<String>());
                    order.addStatuses("Заявка на рекламацию подана.");
                    bot.sendMsgToUser(new TidToUidTable(ownerid, false).getTelegramID(), "Рекламация по заявке №" + nz + " подана!", "main");
                    try {
                        bot.sendToChanelReklamaciya((long) nz, order.getTheme() + "\nОписание дефекта: " + data[2], order.getModel(),
                                DataBase.getUserType(Math.toIntExact(ownerid)), order.getName(),
                                order.getPhone());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    bot.sendMsgToUser(user.getTID(), "Спасибо! Заявка на рекламацию передана. ", "main");
                    return true;
                } catch (Exception e) {
                    bot.sendMsgToUser(user.getTID(), "Ошибка при создании заявки! Ошибка: " + e.getMessage(), employee + "/MainMenu");
                    return true;
                }
            } else {
                bot.sendMsgToUser(user.getTID(), "Команда для создания рекламации: !REKLAM/НОМЕРЗАЯВКИ/Дефект \uD83D\uDC47", employee + "/BackToMainMenu");
                return true;
            }
        }
        return false;
    }

    private boolean handleChangeStatusCommand(String text, Long user_id) {
        List<Integer> zayavki = Arrays.stream(text.split("/")[1].split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        String status = text.split("/")[2];
        if (status.toLowerCase().equals("заявка передана") || status.equals("1")) {
            for (Integer nz : zayavki) {
                bot.updateZStatus(nz, "Заявка передана", "Ваша заявка успешно передана нашему менеджеру! В скором времени он свяжется с Вами!");
                bot.sendMsgToUser(user_id, "Статус заявки №" + nz + " успешно обновлён!", "");
            }
            return true;
        }
        if (status.toLowerCase().equals("заявка принята") || status.equals("2")) {
            for (Integer nz : zayavki) {
                bot.updateZStatus(nz, "Заявка принята", "Заявка принята менеджером! Ожидайте звонка...");
                bot.sendMsgToUser(user_id, "Статус заявки №" + nz + " успешно обновлён!", "");
            }
            return true;
        }
        if (status.toLowerCase().equals("сбор в пути") || status.equals("3")) {
            for (Integer nz : zayavki) {
                bot.updateZStatus(nz, "Курьер выехал к Вам (Сбор)", "Курьер выехал к Вам забрать картридж/принтер, будет у Вас в течении 1-2 часов. Пожалуйста ожидайте.");
                bot.sendMsgToUser(user_id, "Статус заявки №" + nz + " успешно обновлён!", "");
            }
            return true;
        }
        if (status.toLowerCase().equals("заявка в работе") || status.equals("4")) {
            for (Integer nz : zayavki) {
                bot.updateZStatus(nz, "Заявка в работе", "Ваша заявка в работе, пожалуйста ожидайте.");
                bot.sendMsgToUser(user_id, "Статус заявки №" + nz + " успешно обновлён!", "");
            }
            return true;
        }
        if (status.toLowerCase().equals("доставка в пути") || status.equals("5")) {
            for (Integer nz : zayavki) {
                bot.updateZStatus(nz, "Курьер выехал к Вам (Доставка)", "Курьер везет Вам картридж/принтер, будет у Вас в течении 1-2 часов. Пожалуйста ожидайте.");
                bot.sendMsgToUser(user_id, "Статус заявки №" + nz + " успешно обновлён!", "");
            }
            return true;
        }
        if (status.toLowerCase().equals("заявка закрыта") || status.equals("6")) {
            for (Integer nz : zayavki) {
                bot.updateZStatus(nz, "Заявка закрыта", "Заявка закрыта менеджером.");
                bot.sendMsgToUser(user_id, "Статус заявки №" + nz + " успешно обновлён!", "");
            }
            return true;
        }
        if (status.toLowerCase().equals("заявка отменена") || status.equals("7")) {
            for (Integer nz : zayavki) {
                bot.updateZStatus(nz, "Заявка отменена", "Заявка отменена менеджером!");
                bot.sendMsgToUser(user_id, "Статус заявки №" + nz + " успешно обновлён!", "");
            }
            return true;
        }
        bot.sendMsgToUser(user_id, "❌ Такой статус не найден. Доступные статусы: "
                + "\nЗаявка передана"
                + "\nЗаявка принята"
                + "\nСбор в пути"
                + "\nЗаявка в работе"
                + "\nДоставка в пути"
                + "\nЗаявка закрыта"
                + "\nЗаявка отменена", "");
        return true;
    }

    private boolean handleCorporationsInfo(Long user_id) {
        List<Long> all_corp = bot.getAllCorporationsID();
        bot.sendMsgToUser(user_id, "\uD83D\uDE1B Корпораций в боте: " + all_corp.size() + "\nНиже представлена инормация о всех корпорациях добавленных в KOI-KH", "ADMIN/Corporations/MainMenu");
        for (Long id : all_corp) {
            Corporation coorp = new Corporation(id);
            StringBuilder workers_name = new StringBuilder();
            for (Long user_corpid : coorp.getEmployeesID()) {
                String name = DataBase.getUserStr("name", user_corpid);
                workers_name.append(";").append(name);
            }
            if (coorp.getEmployeesID().size() == 0)
                workers_name = new StringBuilder("НЕТУ");
            if (workers_name.toString().startsWith(";"))
                workers_name = new StringBuilder(workers_name.substring(1));
            Bot.sendMsgToUser(user_id, "Корпорация '" + coorp.getName() + "'"
                    + "\nСоздана: " + coorp.getRegDate() + " " + coorp.getRegTime()
                    + "\nАдреса: " + bot.u.stringToString(coorp.getAddresses(), ";")
                    + "\nРаботники: " + workers_name);
        }
        //new TidToUidTable(1L);
        return true;
    }

    private boolean handleNotPreparedMessage(User user, String text, Integer message_id) {
        Long user_id = user.getUID();
        if (admins_wait_corporation_name.contains(user_id)) {
            if (text.equals("Вернуться в меню управления")) return false;
            Long corporation_id = DataBase.getNextCorporationID();
            Corporation corporation = new Corporation(corporation_id);
            corporation.setName(text);
            corporation.setRegDate(bot.u.getDate("dd/MM/yyyy"));
            corporation.setRegTime(bot.u.getDate("hh:mm:ss"));
            admins_wait_corporation_name.remove(user_id);
            bot.sendMsgToUser(user.getTID(), "Корпорация " + text + " успешно создана \uD83D\uDC4C, перейдите в 'Настройки корпорации' для добавления/удаления/изменения любых данных корпорации \uD83D\uDE0E", "ADMIN/MainMenu");
            return true;
        }
        if (admins_wait_employee_phone.contains(user_id)) {
            if (text.equals("Вернуться в меню управления")) return false;
            Long selusid = null;
            for (Long usid : DataBase.getAllUserId()) {
                String phone = new User(usid, false).getUserPhone();
                if (phone != null && phone.contains(text)) {
                    selusid = usid;
                    break;
                }
            }
            if (selusid != null) {
                admins_wait_employee_phone.remove(user_id);
                if (!getSelectedCorporation().containsKey(user_id)) {
                    bot.sendMsgToUser(user.getTID(), "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                    return true;
                }
                Corporation corp = new Corporation(getSelectedCorporation().get(user_id));
                corp.addEmployeeID(selusid);
                DataBase.setUserStr("in_corporation_work", Math.toIntExact(selusid), 1);
                DataBase.setUserStr("сorporationID", Math.toIntExact(selusid), Math.toIntExact(getSelectedCorporation().get(user_id)));
                bot.sendMsgToUser(user.getTID(), "Работник " + DataBase.getUserStr("name", selusid) + " успешно добавлен в корпорацию " + corp.getName() + "."
                        + "\nТеперь в этой корпорации работает " + corp.getEmployeesID().size() + " работник(ов) \uD83E\uDD11", "ADMIN/Corporations/MainMenu");
                return true;
            }
            bot.sendMsgToUser(user.getTID(), "Работников с телефоном " + text + " не найдено в базе KOI-KH. Введите номер телефона ещё раз либо вернитесь в меню управления.", "ADMIN/BackToMainMenu");
            return true;
        }
        if (admins_wait_personal_phone.contains(user_id)) {
            String user_phone = text;
            if (user_phone.equals("Вернуться в меню управления")) return false;
            Long selusid = null;
            for (Long usid : DataBase.getAllUserId()) {
                String phone = new User(usid, false).getUserPhone();
                if (phone != null && (phone.equals(text) || phone.equals("+38" + text) || phone.equals("38" + text))) {
                    selusid = usid;
                    break;
                }
            }
            if (selusid != null) {
                admins_added_personal.put(user_id, selusid);
                admins_wait_personal_phone.remove(user_id);
                admins_wait_select_new_personal_position.add(user_id);
                bot.sendMsgToUser(user.getTID(), "Выберите ниже на какакую должность назначить пользователя  " + new User(selusid, true).getUserName() + "' \uD83E\uDD11", "ADMIN/Personal/Vacancies");
                return true;
            }
            bot.sendMsgToUser(user.getTID(), "Работников с телефоном " + user_phone + " не найдено в базе KOI-KH. Введите номер телефона ещё раз либо вернитесь в меню управления.", "ADMIN/BackToMainMenu");
            return true;
        }
        if (admins_wait_corporation_adress_name.contains(user_id)) {
            String adress = text;
            if (adress.equals("Вернуться в меню управления")) return false;
            admins_wait_corporation_adress_name.remove(user_id);
            if (!getSelectedCorporation().containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                return true;
            }
            Corporation corp = new Corporation(getSelectedCorporation().get(user_id));
            corp.addAdress(adress);
            bot.sendMsgToUser(user.getTID(), "Адресс '" + adress + "' добавлен в корпорацию " + corp.getName() + " \uD83E\uDD2A", "ADMIN/Corporations/MainMenu");
            return true;
        }
        if (admins_wait_new_corporation_name.contains(user_id)) {
            String name = text;
            if (name.equals("Вернуться в меню управления")) return false;
            admins_wait_new_corporation_name.remove(user_id);
            if (!getSelectedCorporation().containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                return true;
            }
            Corporation corp = new Corporation(getSelectedCorporation().get(user_id));
            corp.setName(name);
            bot.sendMsgToUser(user.getTID(), "Имя корпорации успешно изменено \uD83E\uDD2A", "ADMIN/Corporations/MainMenu");
            return true;
        }
        return false;
    }

    public boolean handleMessage(Long id, Message msg) {
        if (adminstrators.contains(id)) {
            if (msg.hasText()) {
                String txt = msg.getText();

            }
        } else return false;
        return false;
    }

    public boolean handleCallBack(User user, Long chatid, String data, Integer msgid, String callid) {
        Long fromid = user.getUID();
        if (data.startsWith("#ADMIN")) {
            String[] pdata = data.split("/");
            if (pdata[1].equals("CORPORATION") || pdata[1].equals("CORP")) {
                Long corporationID = Long.parseLong(pdata[2]);
                String svalue = pdata[3];
                if (svalue.equals("Settings")) {
                    getSelectedCorporation().put(fromid, corporationID);
                    try {
                        bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы выбрали корпорацию! Теперь выберите настройку"));
                        bot.deleteMsg(user.getTID(), msgid);
                        bot.sendMsgToUser(user.getTID(), "Вы выбрали корпорацию " + new Corporation(corporationID).getName() + " выберите какое действие хотите сделать ниже \uD83D\uDE01", "ADMIN/Corporations/SettingsMenu");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                if (svalue.equals("DeleteEmployee")) {
                    long empoyeeID = Long.parseLong(pdata[4]);
                    try {
                        new Corporation(corporationID).remEmployeeID(empoyeeID);
                        DataBase.setUserStr("in_corporation_work", Math.toIntExact(empoyeeID), 0);
                        bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Работник удалён"));
                        bot.deleteMsg(user.getTID(), msgid);
                        bot.sendMsgToUser(user.getTID(), "Работник успешно удалён с корпорации.", "ADMIN/Corporations/MainMenu");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                if (svalue.equals("DA")) {
                    String adress = pdata[4].replaceAll("_", " ");
                    try {
                        new Corporation(corporationID).remAdress(adress);
                        bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Адрес удалён"));
                        bot.deleteMsg(user.getTID(), msgid);
                        bot.sendMsgToUser(user.getTID(), "Адрес удалён с корпорации!", "ADMIN/Corporations/MainMenu");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                if (svalue.equals("Delete")) {
                    Corporation corp = new Corporation(corporationID);
                    for (Long employeeID : corp.getEmployeesID())
                        DataBase.setUserStr("in_corporation_work", Math.toIntExact(employeeID), 0);
                    try {
                        DataBase.deleteCorporation(corporationID);
                        bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Корпорация удалена"));
                        bot.deleteMsg(user.getTID(), msgid);
                        bot.sendMsgToUser(user.getTID(), "Корпорация удалена!", "ADMIN/Corporations/MainMenu");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
            if (pdata[1].equals("Personal")) {
                Long personalID = Long.parseLong(pdata[3]);
                String svalue = pdata[2];
                if (svalue.equals("DeleteEmployee")) {
                    try {
                        DataBase.deletePersonal(personalID);
                        bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Человек удален"));
                        bot.deleteMsg(user.getTID(), msgid);
                        bot.sendMsgToUser(personalID, "Вы больше не работаете в KOI-KH", "main");
                        bot.sendMsgToUser(user.getTID(), "Работник бота KOI-KH удален!", "ADMIN/Personal/MainMenu");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
            if (pdata[1].equals("SetPosition")) {
                String selectedVacansion = data.split("/")[2];
                if (bot.ah.admins_added_personal.containsKey(user.getUID())) {
                    Long personalID = bot.ah.admins_added_personal.get(user.getUID());
                    DataBase.setPerFields(Math.toIntExact(personalID), "v_id", selectedVacansion);
                    DataBase.setPerFields(Math.toIntExact(personalID), "position", selectedVacansion);
                    DataBase.setPerFields(Math.toIntExact(personalID), "phone", DataBase.getUserStr("phone", personalID));
                    DataBase.setPerFields(Math.toIntExact(personalID), "name", DataBase.getUserStr("name", personalID));
                    DataBase.setPerFields(Math.toIntExact(personalID), "description", selectedVacansion + ".Добавил: " + user.getUID());
                    bot.sendMsgToUser(user.getTID(), "Теперь человек " + DataBase.getUserStr("name", personalID) + " является персоналом бота KOI-KH \uD83E\uDD11", "ADMIN/Personal/MainMenu");
                    return true;
                } else {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Данные не актуальны, повторите процедуру повторно."));
                }
            }
            bot.info("lol21");
        }
        if (data.contains("#SEND_TO_")) {
            String where = data.split("_")[2];
            if (where.equals("VIBER")) {
                bot.editMsg(chatid, msgid, InlineButtons.getWhatMessengerV());
            } else {
                if (where.equals("TELEGRAM")) {
                    bot.editMsg(chatid, msgid, InlineButtons.getWhatMessengerT());
                } else {
                    bot.editMsg(chatid, msgid, InlineButtons.getWhatMessengerAll());
                }
            }
            adm_where.put(fromid, where);
            bot.sendMsg(chatid.toString(), "Отправка будет происходить в мессенджер: " + where + "\nВыберите по каким критериям будет происходить отправка:", InlineButtons.getAMenuButtons());
            return true;
        }
        if (data.contains("#ADM_SEND_")) {
            try {
                String rtype = data.split("_")[2];
                bot.sendMsgToUser(user.getTID(), "[" + rtype + "] -> Скиньте мне картинку и текст в подписи.");
                adm_rtype.put(fromid, rtype);
                bot.answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Скиньте фото/текст"));
                return true;
            } catch (Exception ex) {
                System.out.println(Bot.prefix() + "Error in #ADM_SEND block: ");
                ex.printStackTrace();
                return true;
            }
        }
        if (data.contains("#SEND=")) { // 0 SEND  1 WHERE 2 RTYPE  3 NV
            String where = data.split("=")[1];
            try {
                adm_nv.put(fromid, bot.pi(data.split("=")[3]));
                DataBase.setNLFields(adm_nv.get(fromid), "type", data.split("=")[2]);
                if (adm_rtype.get(fromid).equals("ALL") && where.equals("NOW")) {
                    if (adm_where.get(fromid).equals("TELEGRAM"))
                        sendNewsToTelegramForId(adm_nv.get(fromid), null);
                    if (adm_where.get(fromid).equals("VIBER"))
                        sendNewsToViberForId(adm_nv.get(fromid), null);
                    bot.sendMsgToUser(fromid, "[" + adm_rtype.get(fromid) + "] [" + where + "] -> Рассылка завершена!");
                    return true;
                } else adm_wait_values.put(fromid, true);
                bot.sendMsgToUser(fromid, "[" + adm_rtype.get(fromid) + "] [" + where + "] -> Напишите " + adm_rtype.get(fromid) + " через ',' или '/' или '!' разделитель и отправьте мне.");
                bot.answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Напишите " + adm_rtype.get(fromid)));
                return true;
            } catch (Exception ex) {
                System.out.println(Bot.prefix() + "Error in #SEND ");
                ex.printStackTrace();
                return true;
            }
        }
        if (AdminCallbackHandler.handleAdminSetVacation(user, data, callid)) return true;
        return false;
    }

    public void handleDocument(Message msg, Long fromid) {
        String ft = msg.hasDocument() ? msg.getDocument().getMimeType().split("/")[1] :
                msg.getAudio() != null ? msg.getAudio().getMimeType().split("/")[1] :
                        msg.getVideo().getMimeType().split("/")[1];
        if (ft.contains("word")) ft = "doc";
        if (ft.contains("wordprocessingml.document")) ft = "docx";
        if (ft.contains("excel")) ft = "xls";
        if (ft.contains("sheet")) ft = "xlsx";
        if (ft.contains("presentation")) ft = "pptx";
        if (ft.contains("powerpoint")) ft = "ppt";
        if (ft.contains("mpeg")) ft = "mp3";
        String finalFt = ft;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String fname = "doc_" + bot.u.getDate("dd_MM_yyyy") + "_" + bot.u.getTime("HH_mm_ss") + "." + finalFt,
                        fid = msg.hasDocument() ? msg.getDocument().getFileId() : msg.getAudio() != null ?
                                msg.getAudio().getFileId() : msg.getVideo().getFileId();
                int nv = bot.pi(DataBase.getUFileds(4, "val"));
                try {
                    if (!adm_where.get(fromid).equals("VIBER")) {
                        Bot.saveDocument(fname, bot.getFile(new GetFile().setFileId(fid)).getFileUrl(bot.getBotToken()));
                    }
                    //Host.uploadToHost("vintik17@bk.ru", "Vvlad2002", fname);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (adm_where.get(fromid).equals("VIBER")) {
                    try {
                        DataBase.inserToNewsLetter(nv, bot.getFile(new GetFile().setFileId(fid)).getFileUrl(bot.getBotToken()), msg.getCaption(), "", "", "START");
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else DataBase.inserToNewsLetter(nv, fname, msg.getCaption(), "", "", "START");
                System.out.println(bot.prefix() + "Document " + fname + " saved.");
                bot.sendMsgToUser(msg.getChatId(), "[" + adm_where.get(fromid) + "] [" + adm_rtype.get(fromid) + "] -> Документ успешно сохранён, выберите дату отправки: ", "send_napom=" + nv + "=" + adm_rtype.get(fromid));
                DataBase.setUFields(4, "val", (++nv));
                return;
            }
        }, (1000));
    }

    public Boolean sendNewsToTelegramForId(int nv, Integer i) {
        try {
            String fname = DataBase.getNL(nv, "file_name");
            String tex = DataBase.getNL(nv, "text");
            String type = DataBase.getNL(nv, "type");
            java.io.File f = new java.io.File(fname);
            String ftype = "." + bot.getFileExtension(f);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(" ГЫ ГЫ ЫГ " + i);
                    if (type.equals("ALL")) {
                        for (long ii : DataBase.getAllUserId())
                            send(ftype, f, Math.toIntExact(ii), tex);
                        return;
                    } else
                        send(ftype, f, i, tex);
                }
            }, (1000));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean sendNewsToViberForId(int nv, Integer id) {
        String fname = DataBase.getNL(nv, "file_name");
        String tex = DataBase.getNL(nv, "text");
        String type = DataBase.getNL(nv, "type");
        java.io.File f = new java.io.File(fname);
        String ftype = "." + bot.getFileExtension(f);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (type.equals("ALL")) {
                    for (int ii : DataBase.getAllViberUserId()) {
                        try {
                            URL oracle = new URL("https://ay.dn.ua/ViberBot/src/ChangeStatus.php?dark_id=" + ii + "&zn=1337&status=news&key=1337&nv=" + nv);
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(oracle.openStream()));
                            String inputLine;
                            while ((inputLine = in.readLine()) != null)
                                System.out.println(inputLine);
                            in.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else
                    try {
                        URL oracle = new URL("https://ay.dn.ua/ViberBot/src/ChangeStatus.php?dark_id=" + id + "&zn=1337&status=news&key=1337&nv=" + nv);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(oracle.openStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null)
                            System.out.println(inputLine);
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }, (1000 * 5));
        return false;
    }

    public void send(String ftype, File f, Integer i, String tex) {
        try {
            if (ftype.equals(".jpeg") || ftype.equals(".png")) {
                SendPhoto ss = new SendPhoto().setNewPhoto(f).setChatId((long) i);
                bot.sendPhoto(ss);
                bot.sendMsgToUser((long) i, tex);
                System.out.println(bot.prefix() + "Успешно отправлена фотография и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
            } else if (ftype.contains(".doc") || ftype.equals(".pdf")
                    || ftype.equals(".txt") || ftype.equals(".xls") || ftype.equals(".ppt") || ftype.contains(".docx") || ftype.contains(".xlsx") || ftype.contains(".pptx")) {
                SendDocument ss = new SendDocument().setNewDocument(f).setChatId((long) i);
                bot.sendDocument(ss);
                bot.sendMsgToUser((long) i, tex);
                System.out.println(bot.prefix() + "Успешно отправлен документ и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
            } else if (ftype.contains(".mp4") || ftype.equals(".gif")) {
                SendVideo ss = new SendVideo().setNewVideo(f).setChatId((long) i);
                bot.sendVideo(ss);
                bot.sendMsgToUser((long) i, tex);
                System.out.println(bot.prefix() + "Успешно отправлен видос и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
            } else if (ftype.contains(".mp3") || ftype.equals(".ogg") || ftype.equals(".mp2")) {
                SendAudio ss = new SendAudio().setNewAudio(f).setChatId((long) i);
                bot.sendAudio(ss);
                bot.sendMsgToUser((long) i, tex);
                System.out.println(bot.prefix() + "Успешно отправлен голос и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<Long, Long> getSelectedCorporation() {
        return selected_corporation;
    }

    public void setSelectedCorporation(HashMap<Long, Long> selected_corporation) {
        this.selected_corporation = selected_corporation;
    }




    /*
    ADMIN HANDLERS IN START
                if (bot.adm_send_dr.containsKey(id)) return false;
                if (msg.hasDocument() || msg.getAudio() != null || msg.getVideo() != null) {
                    if (adm_where.containsKey(id) && adm_rtype.containsKey(id)) {
                        bot.sendMsgToUser(id, "[" + adm_rtype.get(id) + "] -> Сохраняю Ваш документ... Ожидайте несколько минут пожалуйста.");
                        handleDocument(msg, id);
                        return true;
                    } else {
                        bot.sendMsgToUser(id, "Сначала выберите куда отправлять, потом критерий рассылки, а потом уже файл киньте :-)");
                        return true;
                    }
                }
            if (bot.admin_wait_phtx.containsKey(msg.getFrom().getId()) && bot.admin_wait_phtx.get(msg.getFrom().getId()) == true) {
                int frid = msg.getFrom().getId();
                if (msg.hasDocument()) {
                    System.out.println(bot.prefix() + "Пришёл документ на загрузку от " + DataBase.getPerFields(frid, "name") + "(" + frid + ") Начинаю загрузку...");
                    bot.sendMsgToUser((long) frid, "Начинаю загрузку вашего документа, ожидайте пожалуйста...");
                    String file_name = "doc_" + bot.u.getDate("dd_MM_yyyy") + "_" + bot.u.getTime("HH_mm_ss") + "." + msg.getDocument().getMimeType().split("/")[1];
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                bot. saveDocument(file_name, bot.getFile(new GetFile().setFileId(msg.getDocument().getFileId())).getFileUrl(bot.getBotToken()));
                                if (msg.getCaption() != null) {
                                    bot. admin_phtx.put((long) frid, file_name + "&" + msg.getCaption());
                                    System.out.println(bot.admin_phtx.get((long) frid) + " POTOM " + file_name + "&" + msg.getCaption());
                                    bot.admin_wait_phtx.remove(frid);
                                    bot.sendMsg(frid, "Ваш файл и текст успешно сохранены! Выберите что с ним сделать: ", "ICZA");
                                    this.cancel();
                                    return;
                                }
                                bot.admin_phtx.put((long) frid, file_name + "&NULL");
                                bot. admin_wait_phtx.remove(frid);
                                bot. sendMsgToUser((long) frid, "Ваш файл успешно загружен! Выберите что с ним сделать!");
                                this.cancel();
                                return;
                            } catch (Exception e) {
                                bot.admin_wait_phtx.remove(frid);
                                System.out.println(bot.prefix() + "Ошибка при загрузке файла " + file_name + " от " + DataBase.getPerFields(frid, "name") + "(" + frid + ")");
                                bot.sendMsgToUser((long) frid, "Ошибка при загрузке файла на хостинг! Пожалуйста повторите попытку");
                                this.cancel();
                                return;
                            }
                        }
                    }, 1000);
                    return true;
                }
                if (msg.hasDocument() == false && msg.hasText() == true) {
                    bot.admin_phtx.put((long) frid, "NULL&" + msg.getText());
                    bot.admin_wait_phtx.remove(frid);
                    bot. sendMsg(frid, "Ваш текст успешно сохранен! Выберите что с ним сделать: ", "ICZA");
                    return true;
                }
            }
            if (msg.getFrom().getId() == 521258581 || msg.getFrom().getId() == 556349297) {
                Integer mfid = msg.getFrom().getId();
                if (bot.adm_send_dr.containsKey(mfid) && bot.adm_send_dr.get(mfid) == true) {
                    if (msg.hasDocument()) {
                        String dtxt = msg.getCaption();
                        String ft = msg.getDocument().getMimeType().split("/")[1];
                        bot.user_ftype.put(id, "." + ft);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                String fname = "doc_" + bot.u.getDate("dd_MM_yyyy") + "_" + bot.u.getTime("HH_mm_ss") + bot.user_ftype.get(id),
                                        fid = msg.getDocument().getFileId();
                                try {
                                    bot.saveDocument(fname, bot.getFile(new GetFile().setFileId(fid)).getFileUrl(bot.getBotToken()));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                DataBase.inserToStaticLetter(1, fname, msg.getCaption(), "", "", "START");
                                DataBase.setSLFields(1, "news_name", "birthday");
                                DataBase.setSLFields(1, "type", "SEND_EVERY_DAY");
                                System.out.println(bot.prefix() + "Документ " + fname + " успешно сохранён на хостинге.");
                                bot.sendMsgToUser(Long.valueOf(msg.getFrom().getId()), "Ваш файл + текст поздравления успешно сохранён!");
                                this.cancel();
                            }
                        }, (1000));
                        bot.adm_send_dr.remove(msg.getFrom().getId());
                        return true;
                    }
                    if (msg.hasText()) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                DataBase.inserToStaticLetter(1, "FILE_NOT_ADDED", msg.getText(), "", "", "START");
                                DataBase.setSLFields(1, "news_name", "birthday");
                                DataBase.setSLFields(1, "type", "SEND_EVERY_DAY");
                                bot.sendMsgToUser(Long.valueOf(msg.getFrom().getId()), "Текст поздравления успешно сохранён!");
                                this.cancel();
                            }
                        }, (1000));
                        bot.adm_send_dr.remove(msg.getFrom().getId());
                        return true;
                    }
                }
                if (bot.adm_send_chas.containsKey(mfid) && bot.adm_send_chas.get(mfid) == true) {
                    if (msg.hasDocument()) {
                        String dtxt = msg.getCaption();
                        String ft = msg.getDocument().getMimeType().split("/")[1];
                        bot.user_ftype.put(id, "." + ft);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                String fname = "doc_" + bot.u.getDate("dd_MM_yyyy") + "_" + bot.u.getTime("HH_mm_ss") + bot.user_ftype.get(id),
                                        fid = msg.getDocument().getFileId();
                                try {
                                    bot.saveDocument(fname, bot.getFile(new GetFile().setFileId(fid)).getFileUrl(bot.getBotToken()));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                DataBase.inserToStaticLetter(2, fname, msg.getCaption(), "", "", "START");
                                DataBase.setSLFields(2, "news_name", "refueling_frequency");
                                DataBase.setSLFields(2, "type", "SEND_EVERY_DAY");
                                System.out.println(bot.prefix() + "Документ " + fname + " успешно сохранён на хостинге.");
                                bot. sendMsgToUser(Long.valueOf(msg.getFrom().getId()), "Ваш файл + текст для напоминаня о заправке сохранён!");
                                this.cancel();
                            }
                        }, (1000));
                        bot.adm_send_chas.remove(msg.getFrom().getId());
                        return true;
                    }
                    if (msg.hasText()) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                DataBase.inserToStaticLetter(2, "FILE_NOT_ADDED",msg.getText(), "", "", "START");
                                DataBase.setSLFields(2, "news_name", "refueling_frequency");
                                DataBase.setSLFields(2, "type", "SEND_EVERY_DAY");
                                bot.sendMsgToUser(Long.valueOf(msg.getFrom().getId()), "Текст для напоминания о заправке успешно сохранён!");
                                this.cancel();
                            }
                        }, (1000));
                        bot.adm_send_chas.remove(msg.getFrom().getId());
                        return true;
                    }
                }
                if (bot.adm_send_id.containsKey(id) && bot.adm_send_id.get(id)) {
                    if (msg.hasText() && msg.getText().contains("/")) {
                        int nv = bot.pi(msg.getText().split("/")[0]);
                        String fname = DataBase.getNL(nv, "file_name"), tx = DataBase.getNL(nv, "text"), type = DataBase.getNL(nv, "type");
                        for (String s : msg.getText().split("/")) {
                            if (!s.equals("" + nv)) {

                            }
                        }
                    }
                }
            }
            if (msg.hasDocument() || e.getMessage().getAudio() != null || e.getMessage().getVideo() != null) {
                if (msg.getFrom().getId() == 521258581 || msg.getFrom().getId() == 556349297 && ((adm_send_all.containsKey(msg.getFrom().getId())
                        && adm_send_all.get(msg.getFrom().getId()) == true) || (adm_send_id.containsKey(msg.getFrom().getId())
                        && adm_send_id.get(msg.getFrom().getId()) == true))) {
                    sendMsgToUser(id, DataBase.getUserName(Math.toIntExact(id)) + ", в течении нескольких минут ваш документ будет загружен на хостинг,"
                            + " а текст будет сохранён в базу данных!");
                    String ft = "";
                    if (e.getMessage().hasDocument() == true) {
                        ft = e.getMessage().getDocument().getMimeType().split("/")[1];
                    } else {
                        if (e.getMessage().getAudio() != null)
                            ft = e.getMessage().getAudio().getMimeType().split("/")[1];
                        else if (e.getMessage().getVideo() != null) {
                            ft = e.getMessage().getVideo().getMimeType().split("/")[1];
                        }
                    }
                    if (ft.contains("word"))
                        ft = "doc";
                    if (ft.contains("wordprocessingml.document"))
                        ft = "docx";
                    if (ft.contains("excel"))
                        ft = "xls";
                    if (ft.contains("sheet"))
                        ft = "xlsx";
                    if (ft.contains("presentation"))
                        ft = "pptx";
                    if (ft.contains("powerpoint"))
                        ft = "ppt";
                    if (ft.contains("mpeg"))
                        ft = "mp3";
                    System.out.println(ft + "---------------");
                    user_ftype.put(id, "." + ft);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            String fname = "doc_" + bot.u.getDate("dd_MM_yyyy") + "_" + bot.u.getTime("HH_mm_ss") + user_ftype.get(id), fid = e.getMessage().hasDocument() == true ? msg.getDocument().getFileId() : e.getMessage().getAudio() != null ? e.getMessage().getAudio().getFileId() : e.getMessage().getVideo().getFileId();
                            int nv = pi(DataBase.getUFileds(4, "val"));
                            try {
                                saveDocument(fname, bot.getFile(new GetFile().setFileId(fid)).getFileUrl(getBotToken()));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            DataBase.inserToNewsLetter(nv, fname, e.getMessage().getCaption(), "", "", "START");
                            System.out.println(prefix() + "Документ " + fname + " успешно сохранён на хостинге.");
                            sendMsg(msg, "Выберите дату отправки Вашего сообщения!", "send_napom=" + nv + "=ALL");
                            if (adm_send_id.containsKey(id) == true) {
                                sendMsg(msg, "Напишите id пользователей через / . В начало сообщения введите число: " + nv, "");
                            }
                            System.out.println(nv);
                            nv++;
                            System.out.println(nv);
                            DataBase.setUFields(4, "val", nv);
                            this.cancel();
                        }
                    }, (1000));
                    return;
                    } else {
                }
            }


            ***
            if has text to

                            if (adm_wait_values.containsKey(id) && adm_wait_values.get(id)){
                        String[] values = txt.split("/");
                        adm_wait_values.remove(id);
                        for (String s : values) {
                            if (adm_where.get(id).equals("TELEGRAM")) {
                                if (adm_rtype.get(id).equals("ID")) {
                                    adm_wait_values.remove(id);
                                    bot.sendMsgToUser(id, "Сообщение будет отправлено " + values.length + " ID: " + s);
                                    if (sendNewsToTelegramForId(adm_nv.get(id), bot.pi(s)))
                                        bot.sendMsgToUser(id, "Сообщение отправлено пользователю: " + s);
                                    else
                                        bot.sendMsgToUser(id, "Ошибка при отправке пользователю: " + txt + " | Чат не найден!");
                                }
                            } else {
                                if (adm_rtype.get(id).equals("ID")) {
                                    adm_wait_values.remove(id);
                                    bot.sendMsgToUser(id, "Сообщение будет отправлено " + values.length + " ID: " + s);
                                    if (sendNewsToViberForId(adm_nv.get(id), bot.pi(s)))
                                        bot.sendMsgToUser(id, "Сообщение отправлено пользователю: " + s);
                                    else
                                        bot.sendMsgToUser(id, "Ошибка при отправке пользователю: " + txt + " | Чат не найден!");
                                }
                            }
                        }
                    return true;
                }
                ***************
                for text

                                    if (admin_wait_values.containsKey(e.getMessage().getFrom().getId())) {
                        if (txt.contains("/")) {
                            String[] spl = txt.split("/");
                            long ffid = Long.valueOf(e.getMessage().getFrom().getId()), otpr = 0;
                            String[] phtx = admin_phtx.get(ffid).split("&");
                            for (String s : spl) {
                                if (admin_wait_type.get(ffid).equals("ID")) {
                                    try {
                                        Long i = Long.parseLong(s);
                                        for (long iii : DataBase.getAllUserId()) {
                                            if (iii == i) {
                                                if (!phtx[0].equals("NULL") && !phtx[1].equals("NULL")) {
                                                    bot.sendPhoto(new SendPhoto().setNewPhoto(new java.io.File(phtx[0])).setChatId((long) i).setCaption(phtx[1]));
                                                    otpr++;
                                                    continue;
                                                }
                                                if (!phtx[0].equals("NULL") && phtx[1].equals("NULL")) {
                                                    bot.sendPhoto(new SendPhoto().setNewPhoto(new java.io.File(phtx[0])).setChatId((long) i));
                                                    otpr++;
                                                    continue;
                                                }
                                                if (!phtx[1].equals("NULL") && phtx[0].equals("NULL")) {
                                                    bot.sendMsgToUser((long) i, phtx[1]);
                                                    otpr++;
                                                    continue;
                                                }
                                            }
                                        }
                                    } catch (Exception exx) {
                                        exx.printStackTrace();
                                    }
                                }
                                if (admin_wait_type.get(ffid).equals("ZONE")) {
                                    try {
                                        for (long iii : DataBase.getAllUserId()) {
                                            if (DataBase.getUsFileds(iii, "zone".toLowerCase()).equals(s.toLowerCase())) {
                                                if (!phtx[0].equals("NULL") && !phtx[1].equals("NULL")) {
                                                    bot.sendPhoto(new SendPhoto().setNewPhoto(new java.io.File(phtx[0])).setChatId((long) iii).setCaption(phtx[1]));
                                                    otpr++;
                                                    continue;
                                                }
                                                if (!phtx[0].equals("NULL") && phtx[1].equals("NULL")) {
                                                    bot.sendPhoto(new SendPhoto().setNewPhoto(new java.io.File(phtx[0])).setChatId((long) iii));
                                                    otpr++;
                                                    continue;
                                                }
                                                if (!phtx[1].equals("NULL") && phtx[0].equals("NULL")) {
                                                    bot.sendMsgToUser((long) iii, phtx[1]);
                                                    otpr++;
                                                    continue;
                                                }
                                            }
                                        }
                                    } catch (Exception exx) {
                                        exx.printStackTrace();
                                    }
                                }
                                if (admin_wait_type.get(ffid).equals("ADRESS")) {
                                    try {
                                        for (long iii : DataBase.getAllUserId()) {
                                            if (DataBase.getUsFileds(iii, "adress").toLowerCase().contains(s.toLowerCase())) {
                                                if (!phtx[0].equals("NULL") && !phtx[1].equals("NULL")) {
                                                    bot.sendPhoto(new SendPhoto().setNewPhoto(new java.io.File(phtx[0])).setChatId((long) iii).setCaption(phtx[1]));
                                                    otpr++;
                                                    continue;
                                                }
                                                if (!phtx[0].equals("NULL") && phtx[1].equals("NULL")) {
                                                    bot.sendPhoto(new SendPhoto().setNewPhoto(new java.io.File(phtx[0])).setChatId((long) iii));
                                                    otpr++;
                                                    continue;
                                                }
                                                if (!phtx[1].equals("NULL") && phtx[0].equals("NULL")) {
                                                    bot.sendMsgToUser((long) iii, phtx[1]);
                                                    otpr++;
                                                    continue;
                                                }
                                            }
                                        }
                                    } catch (Exception exx) {
                                        exx.printStackTrace();
                                    }
                                }
                                if (admin_wait_type.get(ffid).equals("CARTRIDGE")) {
                                    try {
                                        for (long uid : DataBase.getAllUserId()) {
                                            if (DataBase.getUsFileds(uid, "last_model").toLowerCase().contains(s.toLowerCase())
                                                    || DataBase.getUsFileds(uid, "last_model_rem").toLowerCase().contains(s.toLowerCase())) {
                                                if (!phtx[0].equals("NULL") && !phtx[1].equals("NULL")) {
                                                    bot.sendPhoto(new SendPhoto().setNewPhoto(new java.io.File(phtx[0])).setChatId((long) uid).setCaption(phtx[1]));
                                                    otpr++;
                                                    continue;
                                                }
                                                if (!phtx[0].equals("NULL") && phtx[1].equals("NULL")) {
                                                    bot.sendPhoto(new SendPhoto().setNewPhoto(new java.io.File(phtx[0])).setChatId((long) uid));
                                                    otpr++;
                                                    continue;
                                                }
                                                if (!phtx[1].equals("NULL") && phtx[0].equals("NULL")) {
                                                    bot.sendMsgToUser((long) uid, phtx[1]);
                                                    otpr++;
                                                    continue;
                                                }
                                            }
                                        }
                                    } catch (Exception exx) {
                                        exx.printStackTrace();
                                    }
                                }
                            }
                            admin_wait_type.remove(ffid);
                            admin_phtx.remove(ffid);
                            admin_wait_values.remove(e.getMessage().getFrom().getId());
                            sendMsgToUser(Long.valueOf(e.getMessage().getFrom().getId()), "Отправка успешно! Отправлено " + otpr + " пользователям!");
                            return;
                        }
                    }


                    *************** for text
                                        if (msg.getFrom().getId() == 521258581 || msg.getFrom().getId() == 556349297) {
                        if (news_wait_date.containsKey(id) && news_wait_date.get(id) == true) {
                            news_wait_date.put(id, false);
                            DataBase.setNLFields(news_id_d.get(id), "type", "ALLDATE");
                            DataBase.setNLFields(news_id_d.get(id), "date", (txt.split("/")[0] + "/" + txt.split("/")[1] + "/" + txt.split("/")[2]));
                            DataBase.setNLFields(news_id_d.get(id), "time", (txt.split("/")[3] + ":" + txt.split("/")[4] + ":" + txt.split("/")[5]));
                            sendMsgToUser(id, "Рассылка начнётся " + txt);
                            try {
                                handlNewsLetter();
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                            return;
                        }
                    }

                    ************************ for text
                                        if (DataBase.isPersonal(fid)) {
                        String vc = DataBase.getPerFields(fid, "v_id");
                        if (vc.equals("manager") || vc.equals("admin") || vc.equals("owner")) {
                            if (hl.handleManagersCommands(msg, txt))
                                return;
                        }
                        if (vc.equals("admin") || vc.equals("owner")) {
                            if (hl.handleAdminCommands(msg, txt))
                                return;
                        }
                        if (vc.equals("admin") || vc.equals("owner")) {
                            if (hl.handleOwnerCommands(msg, txt))
                                return;
                        }
                    }
     */

}