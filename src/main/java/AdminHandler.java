import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AdminHandler {

    private List<Long> adminstrators = Arrays.asList(521258581L, 556349297L);
    private HashMap<Long, String> adm_where = new HashMap<>();
    private HashMap<Long, String> adm_rtype = new HashMap<>();
    private HashMap<Long, Boolean> adm_wait_values = new HashMap<>();
    private HashMap<Long, Integer> adm_nv = new HashMap<>();
    private HashMap<Long, Long> selected_corporation = new HashMap<>();
    private List<Long> admins_wait_corporation_name = new ArrayList<>();
    private List<Long> admins_wait_new_corporation_name = new ArrayList<>();
    private List<Long> admins_wait_corporation_adress_name = new ArrayList<>();
    private List<Long> admins_wait_employee_phone = new ArrayList<>();
    private List<Long> admins_wait_personal_phone = new ArrayList<Long>();
    private Bot bot;

    public AdminHandler(Bot bot) {
        this.bot = bot;
    }

    public boolean handleTextMessage(Long user_id, String text, Integer message_id) {
        if (adminstrators.contains(user_id)) {
            if (handleNotPreparedMessage(user_id, text, message_id)) return true;
            /*Главное меню*/
            if (text.equals("Перейти в меню пользователя")) {
                bot.sendMsgToUser(user_id, "Вы перешли в главное меню пользователя \uD83D\uDCAA", "main");
                return true;
            }
            if (text.equals("Вернуться в меню управления") || text.equals("Вернуться меню администратора")) {
                admins_wait_corporation_name.remove(user_id);
                admins_wait_employee_phone.remove(user_id);
                admins_wait_new_corporation_name.remove(user_id);
                admins_wait_corporation_adress_name.remove(user_id);
                admins_wait_personal_phone.remove(user_id);
                bot.sendMsgToUser(user_id, "Вы вернулись в меню управления \uD83D\uDE0A", "ADMIN/MainMenu");
                return true;
            }
            if (text.equals("Управление корпорациями")) {
                bot.sendMsgToUser(user_id, "Вы перешли в меню управления корпорациям \uD83D\uDE09", "ADMIN/Corporations/MainMenu");
                return true;
            }
            if (text.equals("Управление QR-кодами")) {
                bot.sendMsgToUser(user_id, "Вы перешли в меню управления QR-кодами \uD83D\uDE0E", "ADMIN/QR/MainMenu");
                return true;
            }
            if (text.equals("Управление персоналом")) {
                bot.sendMsgToUser(user_id, "Вы перешли в меню управления персоналом \uD83D\uDE09", "ADMIN/Personal/MainMenu");
                return true;
            }
            /* Меню корпораций */
            if (text.equals("Создать корпорацию")) {
                admins_wait_corporation_name.add(user_id);
                bot.sendMsgToUser(user_id, "\uD83D\uDE0E Напишите в чат название для будущей корпорации.", "ADMIN/BackToMainMenu");
                return true;
            }
            if (text.equals("Удалить корпорацию")) {
                bot.sendMsgToUser(user_id, "\uD83D\uDE15 Нажмите на корпорацию которую нужно удалить.", "ADMIN/Corporations/Delete");
                return true;
            }
            if (text.equals("Настройка корпорации")) {
                bot.sendMsgToUser(user_id, "\uD83D\uDE32 Выберите корпорацию для которой нужно произвести настройку.", "ADMIN/Corporations/Settings");
                return true;
            }
            if (text.equals("Информация о корпорациях")) {
                return handleCorporationsInfo(user_id);
            }
            if (text.equals("Добавить сотрудника")) {
                if (!getSelectedCorporation().containsKey(user_id)) {
                    bot.sendMsgToUser(user_id, "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                    return true;
                }
                admins_wait_employee_phone.add(user_id);
                bot.sendMsgToUser(user_id, "Напишите номер телефона сотрудника и отправьте мне \uD83D\uDC47", "ADMIN/BackToMainMenu");
                return true;
            }
            if (text.equals("Удалить сотрудника")) {
                if (!getSelectedCorporation().containsKey(user_id)) {
                    bot.sendMsgToUser(user_id, "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                    return true;
                }
                bot.sendMsgToUser(user_id, "Нажмите на сотрудника которого нужно удалить.", "ADMIN/Corporations/DeleteEmployee");
                return true;
            }
            if (text.equals("Добавить адрес")) {
                if (!getSelectedCorporation().containsKey(user_id)) {
                    bot.sendMsgToUser(user_id, "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                    return true;
                }
                admins_wait_corporation_adress_name.add(user_id);
                bot.sendMsgToUser(user_id, "Напишите адрес который нужно добавить в корпорацию и отправьте мне \uD83D\uDC47", "ADMIN/BackToMainMenu");
                return true;
            }
            if (text.equals("Удалить адрес")) {
                if (!getSelectedCorporation().containsKey(user_id)) {
                    bot.sendMsgToUser(user_id, "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                    return true;
                }
                bot.sendMsgToUser(user_id, "Нажмите на адресс который нужно удалить \uD83D\uDC47\uD83D\uDC47", "ADMIN/Corporations/DeleteAdress");
                return true;
            }
            if (text.equals("Изменить имя корпорации")) {
                if (!getSelectedCorporation().containsKey(user_id)) {
                    bot.sendMsgToUser(user_id, "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                    return true;
                }
                admins_wait_new_corporation_name.add(user_id);
                bot.sendMsgToUser(user_id, "Введите новое имя корпорации ниже и отправьте мне. \uD83D\uDE3C", "ADMIN/BackToMain");
                return true;
            }
            if (text.equals("Добавить персонал")) {
                admins_wait_personal_phone.add(user_id);
                bot.sendMsgToUser(user_id, "Напишите номер телефона нового члена персонала бота KOI-KH и отправьте мне \uD83D\uDC47", "ADMIN/BackToMainMenu");
                return true;
            }
            if (text.equals("Удалить персонал")) {
                bot.sendMsgToUser(user_id, "Нажмите на сотрудника бота KOI-KH которого нужно удалить.", "ADMIN/Personal/DeleteEmployee");
                return true;
            }
            if (text.equals("Информация о персонале")) {
                bot.sendMsgToUser(user_id, "Ниже написаны все работники KOI-KH", "ADMIN/Personal/MainMenu");
                for (Long personalID : DataBase.getAllPersonalID())
                    bot.sendMsgToUser(user_id, "Работник: " + DataBase.getPerFields(personalID, "name")
                            + "\nДолжность: " + DataBase.getPerFields(personalID, "position")
                            + "\nТелефон: " + DataBase.getPerFields(personalID, "phone")
                            + "\nОписание: " + DataBase.getPerFields(personalID, "description"), "ADMIN/Personal/MainMenu");
                return true;
            }
            if ((text.toLowerCase().startsWith("!сменить статус")) && text.contains("/")) {
                return handleChangeStatusCommand(text, user_id);
            }
        }
        return false;
    }

    private boolean handleChangeStatusCommand(String text, Long user_id){
        List<Integer> zayavki = Arrays.stream(text.split("/")[1].split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        String status = text.split("/")[2];
        if (status.toLowerCase().equals("заявка передена") || status.equals("1")) {
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

    private boolean handleCorporationsInfo(Long user_id){
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
        return true;
    }

    private boolean handleNotPreparedMessage(Long user_id, String text, Integer message_id) {
        if (admins_wait_corporation_name.contains(user_id)) {
            if (text.equals("Вернуться в меню управления")) return false;
            Long corporation_id = DataBase.getNextCorporationID();
            Corporation corporation = new Corporation(corporation_id);
            corporation.setName(text);
            corporation.setRegDate(bot.u.getDate("dd/MM/yyyy"));
            corporation.setRegTime(bot.u.getDate("hh:mm:ss"));
            admins_wait_corporation_name.remove(user_id);
            bot.sendMsgToUser(user_id, "Корпорация " + text + " успешно создана \uD83D\uDC4C, перейдите в 'Настройки корпорации' для добавления/удаления/изменения любых данных корпорации \uD83D\uDE0E", "ADMIN/MainMenu");
            return true;
        }
        if (admins_wait_employee_phone.contains(user_id)) {
            if (text.equals("Вернуться в меню управления")) return false;
            Long selusid = null;
            for (Long usid : DataBase.getAllUserId())
                if (DataBase.getUserStr("phone", usid).contains(text))
                    selusid = usid;
            if (selusid != null) {
                admins_wait_employee_phone.remove(user_id);
                if (!getSelectedCorporation().containsKey(user_id)) {
                    bot.sendMsgToUser(user_id, "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                    return true;
                }
                Corporation corp = new Corporation(getSelectedCorporation().get(user_id));
                corp.addEmployeeID(selusid);
                DataBase.setUserStr("in_corporation_work", Math.toIntExact(selusid), 1);
                DataBase.setUserStr("сorporationID", Math.toIntExact(selusid), Math.toIntExact(getSelectedCorporation().get(user_id)));
                bot.sendMsgToUser(user_id, "Работник " + DataBase.getUserStr("name", selusid) + " успешно добавлен в корпорацию " + corp.getName() + "."
                        + "\nТеперь в этой корпорации работает " + corp.getEmployeesID().size() + " работник(ов) \uD83E\uDD11", "ADMIN/Corporations/MainMenu");
                return true;
            }
            bot.sendMsgToUser(user_id, "Работников с телефоном " + text + " не найдено в базе KOI-KH. Введите номер телефона ещё раз либо вернитесь в меню управления.", "ADMIN/BackToMainMenu");
            return true;
        }
        if (admins_wait_personal_phone.contains(user_id)) {
            String user_phone = text;
            if (user_phone.equals("Вернуться в меню управления")) return false;
            Long selusid = null;
            for (Long usid : DataBase.getAllUserId())
                if (DataBase.getUserStr("phone", usid) != null)
                    if (DataBase.getUserStr("phone", usid).equals(user_phone))
                        selusid = usid;
            if (selusid != null) {
                admins_wait_personal_phone.remove(user_id);
                DataBase.setPerFields(Math.toIntExact(selusid), "v_id", "manager");
                DataBase.setPerFields(Math.toIntExact(selusid), "position", "Менеджер");
                DataBase.setPerFields(Math.toIntExact(selusid), "phone", DataBase.getUserStr("phone", selusid));
                DataBase.setPerFields(Math.toIntExact(selusid), "name", DataBase.getUserStr("name", selusid));
                DataBase.setPerFields(Math.toIntExact(selusid), "description", "Менеджер.Добавил: " + user_id);
                bot.sendMsgToUser(user_id, "Теперь человек " + DataBase.getUserStr("name", selusid) + " является персоналом бота KOI-KH \uD83E\uDD11", "ADMIN/Personal/MainMenu");
                return true;
            }
            bot.sendMsgToUser(user_id, "Работников с телефоном " + user_phone + " не найдено в базе KOI-KH. Введите номер телефона ещё раз либо вернитесь в меню управления.", "ADMIN/BackToMainMenu");
            return true;
        }
        if (admins_wait_corporation_adress_name.contains(user_id)) {
            String adress = text;
            if (adress.equals("Вернуться в меню управления")) return false;
            admins_wait_corporation_adress_name.remove(user_id);
            if (!getSelectedCorporation().containsKey(user_id)) {
                bot.sendMsgToUser(user_id, "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                return true;
            }
            Corporation corp = new Corporation(getSelectedCorporation().get(user_id));
            corp.addAdress(adress);
            bot.sendMsgToUser(user_id, "Адресс '" + adress + "' добавлен в корпорацию " + corp.getName() + " \uD83E\uDD2A", "ADMIN/Corporations/MainMenu");
            return true;
        }
        if (admins_wait_new_corporation_name.contains(user_id)) {
            String name = text;
            if (name.equals("Вернуться в меню управления")) return false;
            admins_wait_new_corporation_name.remove(user_id);
            if (!getSelectedCorporation().containsKey(user_id)) {
                bot.sendMsgToUser(user_id, "Данные о выбранной корпорации больше не актуальны. Повторите процедуру ещё раз.", "ADMIN/Corporations/MainMenu");
                return true;
            }
            Corporation corp = new Corporation(getSelectedCorporation().get(user_id));
            corp.setName(name);
            bot.sendMsgToUser(user_id, "Имя корпорации успешно изменено \uD83E\uDD2A", "ADMIN/Corporations/MainMenu");
            return true;
        }
        return false;
    }

    public boolean handleMessage(Long id, Message msg) {
        if (adminstrators.contains(id)) {
            if (msg.hasText()) {
                String txt = msg.getText();
               /* if (txt.equals("Управление рассылками")) {
                    bot.sendMsg(id.toString(), "Выберите в какой мессенджер будет происходить отправка: ", bot.getWhatMessenger());
                    return true;
                }
                if (txt.equals("Перейти в меню пользователя")) {
                    bot.sendMsg(msg, "Главное меню - >", "main");
                    return true;
                }*/
            }
        } else return false;
        return false;
    }

    public boolean handleCallBack(Long fromid, Long chatid, String data, Integer msgid, String callid) {
        if (adminstrators.contains(fromid)) {
            if (data.startsWith("#ADMIN")) {
                String[] pdata = data.split("/");
                if (pdata[1].equals("CORPORATION") || pdata[1].equals("CORP")) {
                    Long corporationID = Long.parseLong(pdata[2]);
                    String svalue = pdata[3];
                    if (svalue.equals("Settings")) {
                        getSelectedCorporation().put(fromid, corporationID);
                        try {
                            bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы выбрали корпорацию! Теперь выберите настройку"));
                            bot.deleteMsg(fromid, msgid);
                            bot.sendMsgToUser(fromid, "Вы выбрали корпорацию " + new Corporation(corporationID).getName() + " выберите какое действие хотите сделать ниже \uD83D\uDE01", "ADMIN/Corporations/SettingsMenu");
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
                            bot.deleteMsg(fromid, msgid);
                            bot.sendMsgToUser(fromid, "Работник успешно удалён с корпорации.", "ADMIN/Corporations/MainMenu");
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
                            bot.deleteMsg(fromid, msgid);
                            bot.sendMsgToUser(fromid, "Адрес удалён с корпорации!", "ADMIN/Corporations/MainMenu");
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
                            bot.deleteMsg(fromid, msgid);
                            bot.sendMsgToUser(fromid, "Корпорация удалена!", "ADMIN/Corporations/MainMenu");
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
                            bot.deleteMsg(fromid, msgid);
                            bot.sendMsgToUser(personalID, "Вы больше не работаете в KOI-KH", "main");
                            bot.sendMsgToUser(fromid, "Работник бота KOI-KH удален!", "ADMIN/Personal/MainMenu");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
            }
            if (data.contains("#SEND_TO_")) {
                String where = data.split("_")[2];
                if (where.equals("VIBER")) {
                    bot.editMsg(chatid, msgid, bot.getWhatMessengerV());
                } else {
                    if (where.equals("TELEGRAM")) {
                        bot.editMsg(chatid, msgid, bot.getWhatMessengerT());
                    } else {
                        bot.editMsg(chatid, msgid, bot.getWhatMessengerAll());
                    }
                }
                adm_where.put(fromid, where);
                bot.sendMsg(chatid.toString(), "Отправка будет происходить в мессенджер: " + where + "\nВыберите по каким критериям будет происходить отправка:", bot.getAMenuButtons());
                return true;
            }
            if (data.contains("#ADM_SEND_")) {
                try {
                    String rtype = data.split("_")[2];
                    bot.sendMsgToUser(fromid, "[" + rtype + "] -> Скиньте мне картинку и текст в подписи.");
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
        } else return false;
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
                bot.sendMsg(msg, "[" + adm_where.get(fromid) + "] [" + adm_rtype.get(fromid) + "] -> Документ успешно сохранён, выберите дату отправки: ", "send_napom=" + nv + "=" + adm_rtype.get(fromid));
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
