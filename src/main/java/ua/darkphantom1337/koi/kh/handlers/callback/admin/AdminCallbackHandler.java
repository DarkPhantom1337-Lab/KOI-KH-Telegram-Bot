package ua.darkphantom1337.koi.kh.handlers.callback.admin;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.buttons.InlineButtons;
import ua.darkphantom1337.koi.kh.entitys.Courier;
import ua.darkphantom1337.koi.kh.database.DataBase;
import ua.darkphantom1337.koi.kh.entitys.mails.Mail;
import ua.darkphantom1337.koi.kh.entitys.User;
import ua.darkphantom1337.koi.kh.entitys.mails.MailStatus;
import ua.darkphantom1337.koi.kh.entitys.mails.SendType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminCallbackHandler {

    private static Bot bot = Bot.bot;

    public static Boolean handleAdminSetVacation(User user, String data, String cbqID) {
        if (data.startsWith("#Admin/SetPosition")) {
            String selectedVacansion = data.split("/")[2];
            if (bot.ah.admins_added_personal.containsKey(user.getUID())) {
                Long personalID = bot.ah.admins_added_personal.get(user.getUID());
                DataBase.setPerFields(Math.toIntExact(personalID), "v_id", selectedVacansion);
                DataBase.setPerFields(Math.toIntExact(personalID), "position", selectedVacansion);
                DataBase.setPerFields(Math.toIntExact(personalID), "phone", DataBase.getUserStr("phone", personalID));
                DataBase.setPerFields(Math.toIntExact(personalID), "name", DataBase.getUserStr("name", personalID));
                DataBase.setPerFields(Math.toIntExact(personalID), "description", selectedVacansion + ".Добавил: " + user.getUID());
                if (selectedVacansion.equalsIgnoreCase("courier")) {
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

    public static Boolean handleAdminMailSetWhen(User user, String data, Integer msgID, String cbqID) {
        if (data.startsWith("#ADMIN/Mails/SetWhen/")) {
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Выполняю...."));
            bot.editMsg(user.getTID(), msgID, InlineButtons.getButText("Действие выполнено."));
            Long mailID = Long.parseLong(data.split("/")[3]);
            SendType stype = SendType.valueOf(data.split("/")[4]);
            Mail mail = new Mail(mailID);
            mail.setSendType(stype);
            if (stype.equals(SendType.NOW)) {
                user.setUserAction("main");
                mail.setSendDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                mail.setMailStatus(MailStatus.WAITING_TO_START);
                user.sendMessage("Рассылка №" + mailID + " заполнена и готова к старту. "
                        + "\n\uD83D\uDC49 Нажмите 'Запустить' чтобы сохранить и разрешить выполнение рассылки [Получатели: Все]." +
                        "\n\uD83D\uDC49 Нажмите 'Указать параметр' чтобы выбрать доп. параметр для сортировки получателей.", "ADMIN/Mail/StartOrSetParameters/" + mailID);
            } else {
                bot.ah.admins_current_mail.put(user.getUID(), mailID);
                user.setUserAction("admin_mail_wait_date");
                user.sendMessage("Укажите когда отправить рассылку №" + mailID + " в следующем формате день/месяц/год час:минуты:секунды"
                        + "\n\uD83D\uDC49 Пример: 01/06/2021 10:15:00");
            }
            return true;
        }
        return false;
    }

    public static Boolean handleAdminMailStart(User user, String data, Integer msgID, String cbqID) {
        if (data.startsWith("#ADMIN/Mails/START/")) {
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Выполняю...."));
            bot.editMsg(user.getTID(), msgID, InlineButtons.getButText("Действие выполнено."));
            Long mailID = Long.parseLong(data.split("/")[3]);
            Mail mail = new Mail(mailID);
            mail.setMailStatus(MailStatus.READY_TO_RUN);
            user.sendMessage("Рассылка №" + mailID + " запланирована и будет выполнена " + mail.getSendDate() + "! Удалить рассылку можно в меню 'Текущие рассылки'!");
            return true;
        }
        if (data.startsWith("#ADMIN/Mails/CANCEL/")) {
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Выполняю...."));
            bot.editMsg(user.getTID(), msgID, InlineButtons.getButText("Действие выполнено."));
            Long mailID = Long.parseLong(data.split("/")[3]);
            Mail mail = new Mail(mailID);
            int deleted = 0;
            for (String messageID : mail.getAllMessagesID()) {
                try {
                    Long chatID = Long.parseLong(messageID.split("-")[0]);
                    Integer msgmID = Integer.parseInt(messageID.split("-")[1]);
                    Bot.bot.deleteMsg(chatID, msgmID);
                    deleted++;
                } catch (Exception e) {

                }
            }
            mail.setMailStatus(MailStatus.CANCELED);
            user.sendMessage("Рассылка №" + mailID + " отменена! Сообщение было удалено у " + deleted + " пользователей!");
            return true;
        }
        return false;
    }

    public static Boolean handleAdminMailSetParameters(User user, String data, Integer msgID, String cbqID) {
        if (data.startsWith("#ADMIN/SetParameters/")) {
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Выполняю...."));
            bot.editMsg(user.getTID(), msgID, InlineButtons.getButText("Действие выполнено."));
            Long mailID = Long.parseLong(data.split("/")[2]);
            user.sendMessage("Выберите параметры сортировки получателей рассылки:"
                            + "\n\uD83D\uDC49 'Адрес' -> Отсортировать получателей по АДРЕСУ."
                            + "\n\uD83D\uDC49 'Модель' -> Отсортировать получателей по МОДЕЛИ КАРТРИДЖА."
                    , "ADMIN/Mail/SelectParameters/" + mailID);
            return true;
        }
        return false;
    }

    public static Boolean handleAdminMailSelectParameters(User user, String data, Integer msgID, String cbqID) {
        if (data.startsWith("#ADMIN/Mails/Parameter/")) {
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Выполняю...."));
            bot.editMsg(user.getTID(), msgID, InlineButtons.getButText("Действие выполнено."));
            String type = data.split("/")[3];
            Long mailID = Long.parseLong(data.split("/")[4]);
            if (type.equals("ChangeDate")) {
                bot.ah.admins_current_mail.put(user.getUID(), mailID);
                user.sendMessage("\uD83D\uDC49 Рассылка №" + mailID + " изменение даты! Укажите когда вы хотите отправить данное сообщение:", "ADMIN/Mail/When/" + mailID);
                return true;
            }
            if (type.equals("ChangeText")) {
                bot.ah.admins_current_mail.put(user.getUID(), mailID);
                user.setUserAction("admin_wait_mail_ch_tx");
                user.sendMessage("Пришлите мне текст для рассылки №" + mailID, "ADMIN/BackToMain");
                return true;
            }
            if (type.equals("BaskStep")) {
                bot.deleteMsg(user.getTID(), msgID);
                new Mail(mailID).setAllRecipientsID(new ArrayList<>());
                user.sendMessage("Рассылка №" + mailID + " заполнена и готова к старту. "
                        + "\n\uD83D\uDC49 Нажмите 'Запустить' чтобы сохранить и разрешить выполнение рассылки [Получатели: Все]." +
                        "\n\uD83D\uDC49 Нажмите 'Указать параметр' чтобы выбрать доп. параметр для сортировки получателей.", "ADMIN/Mail/StartOrSetParameters/" + mailID);
                return true;
            }
            if (type.equals("Address")) {
                user.setUserAction("admin_wait_mail_fil_address");
                user.sendMessage("\uD83D\uDC49 Введите адрес или часть адреса не указывая город. " +
                        "\nНапример 'пушк' выберет пользователей у которых адрес содержит текст 'пушк'"
                );
                return true;
            }
            if (type.equals("Phone")) {
                user.setUserAction("admin_wait_mail_fil_phone");
                user.sendMessage("\uD83D\uDC49 Введите номер телефона или часть номера. " +
                        "\nНапример '0666905956' выберет пользователей у которых номер телефона содержит текст '0666905956'"
                );
                return true;
            }
            if (type.equals("Cartridge")) {
                user.setUserAction("admin_wait_mail_fil_cartridge");
                user.sendMessage("\uD83D\uDC49 Введите модель/производителя картридж или часть модели/производителя картриджа " +
                        "\nНапример '3010' выберет пользователей у которых в моделях картриджа содержится текст '3010'"
                );
                return true;
            }
            return true;
        }
        return false;
    }


    public static Boolean handleAdminMailDelete(User user, String data, Integer msgID, String cbqID) {
        if (data.startsWith("#ADMIN/Mails/Delete/")) {
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Выполняю...."));
            Long mailID = Long.parseLong(data.split("/")[3]);
            new Mail(mailID).setMailStatus(MailStatus.COMPLETED);
            bot.editMsg(user.getTID(), msgID, InlineButtons.getButText("Рассылка №" + mailID + " завершена!"));
            return true;
        }
        return false;
    }
}
