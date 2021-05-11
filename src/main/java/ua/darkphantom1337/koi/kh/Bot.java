package ua.darkphantom1337.koi.kh;

import com.google.gdata.util.ServiceException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.ActionType;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.File;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.darkphantom1337.koi.kh.buttons.InlineButtons;
import ua.darkphantom1337.koi.kh.buttons.ReplyButtons;
import ua.darkphantom1337.koi.kh.database.TidToUidTable;
import ua.darkphantom1337.koi.kh.entitys.*;
import ua.darkphantom1337.koi.kh.handlers.*;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Bot extends TelegramLongPollingBot {

    public static HashMap<Long, Integer> user_cmd_count = new HashMap<Long, Integer>();

    public static HashMap<Long, String> user_type = new HashMap<Long, String>(),
            user_name = new HashMap<Long, String>(),
            user_ftype = new HashMap<Long, String>(),
            user_phone = new HashMap<Long, String>(),
            user_tema = new HashMap<Long, String>(),
            user_model = new HashMap<Long, String>();
    public static UserMessageHandler umh;
    public static HashMap<Long, Integer> user_wait_rate_zn = new HashMap<Long, Integer>(), user_wait_rate_zn_тsg = new HashMap<Long, Integer>(), news_id_d = new HashMap<Long, Integer>(), z_kolvo = new HashMap<Long, Integer>();
    public static HashMap<Long, List<String>> user_zayavki = new HashMap<Long, List<String>>();
    public static HashMap<Long, File> user_file = new HashMap<Long, File>();

    public static HashMap<String, File> saved_files = new HashMap<String, File>();

    public static HashMap<Integer, List<String>> act_zayavki = new HashMap<Integer, List<String>>();
    public static HashMap<Long, Boolean> user_wait_name = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> user_wait_company_name = new HashMap<Long, Boolean>();

    public static HashMap<Long, Boolean> user_wait_adress = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> pers_is_z_saved = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> user_wait_phone = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> news_wait_date = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> user_is_ost_price = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> user_wait_rate_comm = new HashMap<Long, Boolean>();

    public static HashMap<Long, Boolean> user_wait_semodel = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> user_wait_podmenka = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> user_wait_model = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> user_edit_adress = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> user_edit_model = new HashMap<Long, Boolean>();
    public static HashMap<Long, Boolean> user_podmenka = new HashMap<Long, Boolean>();
    public static HashMap<Integer, Boolean> adm_send_dr = new HashMap<Integer, Boolean>(),
            adm_send_chas = new HashMap<Integer, Boolean>(), adm_send_all = new HashMap<Integer, Boolean>(), adm_send_id = new HashMap<Integer, Boolean>(), adm_wait_ids = new HashMap<Integer, Boolean>();

    public static HashMap<Integer, Boolean> admin_wait_phtx = new HashMap<Integer, Boolean>();
    public static HashMap<Integer, Boolean> admin_wait_qr = new HashMap<Integer, Boolean>();
    public static HashMap<Long, String> admin_phtx = new HashMap<Long, String>();
    public static HashMap<Long, String> user_is_ost_model = new HashMap<Long, String>();
    public static HashMap<Long, String> prnt_model = new HashMap<Long, String>();
    public static HashMap<Integer, Boolean> admin_wait_values = new HashMap<Integer, Boolean>();
    public static HashMap<Long, String> admin_wait_type = new HashMap<Long, String>();
    public static HashMap<Long, String> admin_data = new HashMap<Long, String>();
    public static HashMap<Long, String> user_stype = new HashMap<Long, String>();
    public static HashMap<Long, String> admin_sall_type = new HashMap<Long, String>();
    public static HashMap<Long, Integer> last_vost_msg = new HashMap<Long, Integer>();


    public static HashMap<Long, List<String>> order_models = new HashMap<Long, List<String>>();
    public static HashMap<Long, List<String>> selected_order_models = new HashMap<Long, List<String>>();
    public static HashMap<Long, List<Long>> selected_sub_orders_for_reconsile = new HashMap<Long, List<Long>>();

    public static List<String> menus = Arrays.asList(new String[]{"Оставить заявку на заправку", "Оставить заявку на ремонт", "Написать менеджеру", "Адрес/Контакты", "Мои заявки", "Прайс лист", "Текущие заявки", "Завершённые заявки", "Главное меню", "Вернуться в главное меню", "Подать заявку", "Используя QR-код", "Заполняя форму"});

    public static DataBase db = null;
    public static Bot bot = null;
    public static Handlers hl = null;
    public Utils u = null;
    public static AdminHandler ah = null;
    public static ManagerHandler mh = null;
    public static MasterChannelHandler mch = null;
    public static MasterChannelButtons mcb = null;
    public static UserCallbackHandler uch = null;
    public static UserPhotoHandler uphh = null;
    public static UserContactHandler ucth = null;
    public static PersonalCallbackHandler pch = null;
    public static CourierMessageHandler ch = null;
    public static UserGlobalHandler ugh = null;

    public static GoogleSheetsAPI sheetsAPI;

    public Boolean enabled = false;

    public static void main(String[] args) {
        System.out.println("Starting KOI-BOT...");
        DataBase.connectToBase("koi.mysql.ukraine.com.ua", "koi_bot", "koi_bot", "n5C-N6*8ii");
        DataBase.connectToPriceBase("koi.mysql.ukraine.com.ua", "koi_v2inua", "koi_v2inua", "rxn74rh5");
        registerBot();
        bot.u = new Utils();
        DataBase.setBot(bot);
        bot.umh = new UserMessageHandler(bot);
        if (bot.enabled == true) {
            db = new DataBase();
            DataBase.setControllFields(1, "other", bot.bot.u.getDate("dd/MM/YYYY%HH:mm:ss"));
            menus = Arrays.asList(new String[]{"Оставить заявку на заправку", "Оставить заявку на ремонт", "Написать менеджеру", "Адрес/Контакты"});
            handQuiz();
            bot.startQuiz();
            hl = new Handlers(bot);
            ah = new AdminHandler(bot);
            mh = new ManagerHandler(bot);
            mch = new MasterChannelHandler();
            mcb = new MasterChannelButtons(bot);
            uch = new UserCallbackHandler();
            ugh = new UserGlobalHandler();
            uphh = new UserPhotoHandler();
            ucth = new UserContactHandler();
            pch = new PersonalCallbackHandler();
            sheetsAPI = new GoogleSheetsAPI();
        }

    }

    public static void registerBot() {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new Bot());
            System.out.println("Successful enabled KOI-BOT!");
        } catch (TelegramApiException e) {
            System.out.println("Error in enabled KOI-BOT!");
        }
        bot = new Bot();
        bot.enabled = true;
    }

    public static String prefix() {
        return "[" + bot.u.getDate("dd/MM/YYYY") + "] [" + bot.u.getTime("HH:mm:ss") + "] [DarkSystems] -> ";
    }

    public static void handQuiz() {
        Date date = new Date(), toz = date;
        String date1 = new SimpleDateFormat("dd/MM/yyyy").format(date);
        Integer h = Integer.parseInt(new SimpleDateFormat("HH").format(date));
        if (h >= 9 && h <= 12) {
            System.out.println(bot.prefix() + "Time to OPROS. Starting opros...");
            bot.startQuiz();
        } else {
            toz.setDate(date.getDate() + 1);
            toz.setHours(9);
            toz.setMinutes(15);
            System.out.println(prefix() + "Now is not the time for opros!" +
                    "Next time for opros: " + bot.u.getDate("dd/MM/YYYY", toz) + " " + bot.u.getTime("HH:mm:ss", toz));
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(bot.prefix() + "Time to opros! Start opros!");
                    bot.startQuiz();
                    System.gc();
                }
            }, toz, ((600000 * 6) * 24));
        }
    }

    public static int sendMsgToUser(Long id, String text) {//Отправка сообщения пользователю
        Integer i = 0;
        try {
            i = bot.execute(new SendMessage().setText(text).setChatId(id)).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static int sendMsgToUser(Long id, String text, Integer msgid) throws TelegramApiException {//Отправка сообщения пользователю
        return bot.execute(new SendMessage().setText(text).setChatId(id)).getMessageId();
    }

    public void editMsg(Long chatid, Integer msgid, String text) {//Редактирование сообщения
        try {
            this.editMessageText(new EditMessageText().setChatId(chatid)
                    .setMessageId(msgid)
                    .setText(text));
        } catch (Exception e) {
            System.out.println(prefix() + "Error in editing msg"
                    + "\n-> ChatID: " + chatid
                    + "\n-> MsgID: " + msgid
                    + "\n-> Exception: " + e.getLocalizedMessage()
                    + "\n-> Error line: " + e.getStackTrace()[0].getLineNumber());
        }
    }

    public void editMsg(Long chatid, Integer msgid, InlineKeyboardMarkup kb) {//Редактирование сообщения
        try {
            this.editMessageReplyMarkup(new EditMessageReplyMarkup().setChatId(chatid)
                    .setMessageId(msgid)
                    .setReplyMarkup(kb));
        } catch (Exception e) {
            System.out.println(prefix() + "Error in editing msg"
                    + "\n-> ChatID: " + chatid
                    + "\n-> MsgID: " + msgid
                    + "\n-> Exception: " + e.getLocalizedMessage()
                    + "\n-> Error line: " + e.getStackTrace()[0].getLineNumber());
            e.printStackTrace();
        }
    }

    public void deleteMsg(Long chatid, Integer msgid) {//Удаление сообщения
        try {
            this.deleteMessage(new DeleteMessage().setChatId(String.valueOf(chatid)).setMessageId(msgid));
        } catch (Exception e) {
            System.out.println(prefix() + "Message not delete. CID: " + chatid + " MsgID: " + msgid + " E: " + e.getLocalizedMessage());
            //e.printStackTrace();
        }
    }

    public static String getFileExtension(java.io.File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    public void startQuiz() {//Начать обработку опросов
        int n = DataBase.ge(), s = 0;
        if (n != 0) {
            Map<Integer, String> vals = new HashMap<Integer, String>();
            int nvls = 1;
            for (int i = 1; i <= n; i++) {
                if (DataBase.getOFiledsS(i, "send_date").equals(bot.u.getDate("dd/MM/yyyy"))) {
                    try {
                        sendOpros((long) DataBase.getOFileds(i, "u_id"), DataBase.getOFileds(i, "z_id"));
                        s++;
                    } catch (Exception e) {
                        System.out.println(prefix() + "Error in opros"
                                + "\n-> n: " + n
                                + "\n-> Exception: " + e.getLocalizedMessage()
                                + "\n-> Error line: " + e.getStackTrace()[0].getLineNumber());
                    }
                } else {
                    vals.put(nvls, DataBase.getOFiledsS(i, "z_id") + "!"
                            + DataBase.getOFiledsS(i, "u_id") + "!"
                            + DataBase.getOFiledsS(i, "send_date") + "!"
                            + DataBase.getOFiledsS(i, "val"));
                    nvls++;
                }
            }
            DataBase.clearTable("opros");
            DataBase.setUFields(2, "val", 1);
            try {
                if (!vals.isEmpty()) {
                    for (int i : vals.keySet()) {
                        if (vals.get(i).contains("!")) {
                            String[] spl = vals.get(i).split("!");
                            DataBase.saveZInOpr(pi(spl[0]), pi(spl[1]), spl[2], spl.length >= 3 ? spl[3] : "");
                        }
                    }
                }
            } catch (Exception e) {

            }
            System.out.println(prefix() + "Opros end! - >ALL opros: " + n
                    + "! Sended opros: " + s);
        } else {
            System.out.println(prefix() + "Opros empty");
        }
    }


    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    @Override //Имя бота
    public String getBotUsername() {
        return "KOI";
    }

    public static void saveDocument(String file_name, String url_to_file) {
        java.io.File f = new java.io.File(file_name);
        try (BufferedInputStream in = new BufferedInputStream(new URL(url_to_file).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file_name)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println(prefix() + "File " + file_name + " in url: " + url_to_file + " dowload!");
        } catch (Exception e) {
            System.out.println(prefix() + "Error in load file: " + file_name + " in url: " + url_to_file + "\nException: " + e.getLocalizedMessage());
        }
    }

    public static int pi(String i) {
        return Integer.parseInt(i);
    }

    public void info(String s) {
        System.out.println("[KOI-KH] [" + bot.u.getDate("dd/MM/YYYY") + "] [" + bot.u.getTime("HH:mm:ss") + "] [INFO] -> " + s);
    }

    public void error(String s) {
        System.out.println("[KOI-KH] [" + bot.u.getDate("dd/MM/YYYY") + "] [" + bot.u.getTime("HH:mm:ss") + "] [ERROR] -> " + s);
    }

    @Override //Получение обновлений(Сообщения/Данные и т.д.)
    public void onUpdateReceived(Update e) {
        if (bot.enabled) {
            if (e.hasMessage()) {
                Message msg = e.getMessage();
                Long fromID = msg.getFrom().getId().longValue(), fid = fromID, chatID = msg.getChatId();
                User user = new User(fromID);
                bot.u.changeBotAction(chatID, ActionType.TYPING);
                if (ugh.handleUserMessage(user, msg)) return;
            } else if (e.hasCallbackQuery()) {
                try {
                    Message msg = e.getCallbackQuery().getMessage();
                    final Long id = msg.getChatId();
                    Long chatID = msg.getChatId(), fromID = e.getCallbackQuery().getFrom().getId().longValue();
                    Integer msgid = msg.getMessageId(), fromid = e.getCallbackQuery().getFrom().getId();
                    String data = e.getCallbackQuery().getData(), text = msg.getText(), cbqid = e.getCallbackQuery().getId();
                    User user = new User(fromID);
                    bot.u.logAsyncCallback(user, msg, data);
                    sendToLogChanel("Пользователь " + e.getCallbackQuery().getFrom().getFirstName() + " нажал на кнопку: UID: " + fromid + " CID: " + id + " DATA: " + data);
                    if (ugh.handleUserCallback(e.getCallbackQuery())) return;
                } catch (Exception x) {
                    System.out.println(prefix() + "Ошибка при обработке callback"
                            + "\n-> Exception: " + x.getLocalizedMessage()
                            + "\n-> Error line: " + x.getStackTrace()[0].getLineNumber());
                    x.printStackTrace();
                }
            }
        }
    }

    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public void updateZStatus(Integer zid, String smallstatus, String longstatus) {
        Order order = new Order(zid);
        User user = new User(new TidToUidTable(order.getUID(), false).getTelegramID());
        try {
            int smi = order.getStatusMsgID();
            if (smi > 0)
                deleteMsg(user.getTID(), smi);
        } catch (Exception ignore) {
        }
        String current_statuses = "ℹ️ Заявка №" + zid;
        for (String status : order.getAllStatuses())
            current_statuses += "\n" + status;
        current_statuses += "\n\uD83D\uDC49 Текущий статус: " + longstatus;
        order.addStatuses(bot.u.getDate("dd/MM/YY HH:mm:ss") + " " + smallstatus);
        order.setStatusMsgID(sendMsgToUser(user.getTID(), current_statuses));
    }

    public void updateVosstMsg(Long orderID, String text) {
        Order order = new Order(orderID);
        HashMap<Long, Integer> msgids = order.getVosstMsgID();
        if (msgids.isEmpty()) {
            String iddata = DataBase.getUFileds(20, "val1");
            List<Long> usersID = new ArrayList<Long>();
            if (iddata != null && !iddata.equals("") && !iddata.equals(" "))
                usersID = Arrays.stream(iddata.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
            for (Long id : usersID) {
                msgids.put(id, 0);
            }
        }
        try {
            for (Long chatID : msgids.keySet()) {
                try {
                    int smi = msgids.get(chatID);
                    if (smi > 0)
                        deleteMsg(chatID, smi);
                } catch (Exception e) {

                }
            }
        } catch (Exception ignore) {
        }
        String current_statuses = "ℹ️ Заявка №" + order.getOrderID() + "\n";
        String message = order.getVosstMsgText() + "\n\n" + text;
        order.setVosstMsgText(message);
        for (Long chatID : msgids.keySet()) {
            try {
                order.setVosstMsgID(chatID, sendMsgToUser(chatID, current_statuses + message, "Manager/Reconciliation/AllSubOrders/" + order.getOrderID()));
            } catch (Exception e) {

            }
        }
    }

    public void updateVosstMsg(User user, Long orderID, String text) {
        Order order = new Order(orderID);
        HashMap<Long, Integer> msgids = order.getVosstMsgID();
        try {
            for (Long chatID : msgids.keySet()) {
                int smi = msgids.get(chatID);
                if (smi > 0)
                    deleteMsg(chatID, smi);
            }
        } catch (Exception ignore) {
        }
        String current_statuses = "ℹ️ Заявка №" + order.getOrderID() + "\n";
        String message = order.getVosstMsgText() + "\n\n" + text;
        order.setVosstMsgText(message);
        deleteAllLastVosstMsg(orderID);
        order.clearVosstMsgID();
        order.setVosstMsgID(user.getTID(), sendMsgToUser(user.getTID(), current_statuses + message, "Manager/Reconciliation/AllSubOrders/" + order.getOrderID()));
    }

    public void deleteAllLastVosstMsg(Long orderID) {
        Order order = new Order(orderID);
        HashMap<Long, Integer> msgids = order.getVosstMsgID();
        for (Long chatId : msgids.keySet()) {
            try {
                //bot.clearSelectedOrderForReconsile(new User(chatId).getUID());
                bot.deleteMsg(chatId, msgids.get(chatId));
            } catch (Exception e) {

            }
        }
    }

    public static Boolean handZayav(User user, Message msg, String txt) {
        Long id = user.getUID();
        if (user_wait_model.containsKey(id) && user_wait_model.get(id) == true) {
            if (menus.contains(txt)) {
                sendMsgToUser(user.getTID(), "Вы уже оставляете заявку! Пожалуйста укажите модель принтера!");
                return true;
            }
            if (txt.equals("Главное меню")) {
                user_wait_model.remove(id);
                user_wait_adress.remove(id);
                user.sendMessage("Главное меню", "main");
                return true;
            }
            if (txt.equals("Подать заявку"))
                return false;
            String smodel = txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "");
            user_model.put(id, smodel);
            UsersData.addOrderModel(id, smodel);
            UsersData.addSelectedOrderModel(id, smodel);
            if (DataBase.isCorporationWorker(id)) {
                String true_modelcorp = DataBase.getUserStr("true_models" + (!user_tema.get(id).equals("Заправка картриджа") ? "_rem" : ""), id);
                List<String> true_mod = new ArrayList<String>();
                if (true_modelcorp != null)
                    true_mod = new ArrayList<String>(Arrays.asList(true_modelcorp.split(";")));
                String model = txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "").replaceAll(";", ":");
                if (!true_mod.contains(model))
                    true_mod.add(model);
                DataBase.setUserStr("true_models" + (!user_tema.get(id).equals("Заправка картриджа") ? "_rem" : ""), Math.toIntExact(id), bot.u.stringToString(true_mod, ";"));
                bot.sendMsgToUser(user.getTID(), "Модель '" + smodel + "' добавлена в заявку! Если Вы хотите добавить ещё выберите модель снизу или напишите её название мне.", "model");
                return true;
            }
            if (user_tema.containsKey(id) && user_tema.get(id).equals("Заправка картриджа")) {
                if (DataBase.getUsFileds(id, "last_model") != null && !DataBase.getUsFileds(id, "last_model").equals("Не указано")) {
                    if (!DataBase.getUsFileds(id, "last_model").split("&&&")[0].equals(txt))
                        DataBase.setUsFields(id, "last_model", txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "") + "&&&" + DataBase.getUsFileds(id, "last_model").split("&&&")[0]);
                } else {
                    if (!DataBase.getUsFileds(id, "last_model").split("&&&")[0].equals(txt))
                        DataBase.setUsFields(id, "last_model", txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
                }
                bot.sendMsgToUser(user.getTID(), "Модель '" + smodel + "' добавлена в заявку! Если Вы хотите добавить ещё выберите модель снизу или напишите её название мне.", "model");
            } else {
                if (DataBase.getUsFileds(id, "last_model_rem") != null && !DataBase.getUsFileds(id, "last_model_rem").equals("Не указано")) {
                    if (!DataBase.getUsFileds(id, "last_model_rem").split("&&&")[0].equals(txt))
                        DataBase.setUsFields(id, "last_model_rem", txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "") + "&&&" + DataBase.getUsFileds(id, "last_model_rem").split("&&&")[0]);
                } else if (!DataBase.getUsFileds(id, "last_model_rem").split("&&&")[0].equals(txt)) {
                    DataBase.setUsFields(id, "last_model_rem", txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
                }
                bot.sendMsgToUser(user.getTID(), "Модель '" + smodel + "' добавлена в заявку! Если Вы хотите добавить ещё выберите модель снизу или напишите её название мне.", "model");

            }
            return true;
        }
        return false;
    }

    public void updateOrderModels(Long id) {
        List<String> default_models = new ArrayList<String>();
        if (DataBase.isCorporationWorker(id)) {
            String true_modelcorp = DataBase.getUserStr("true_models" + (!user_tema.get(id).equals("Заправка картриджа") ? "_rem" : ""), id);
            List<String> true_mod = new ArrayList<String>();
            if (true_modelcorp != null)
                true_mod = new ArrayList<String>(Arrays.asList(true_modelcorp.split(";")));
            for (String md : true_mod)
                if (!default_models.contains(md))
                    default_models.add(md);
            UsersData.setOrderModels(id, default_models);
            return;
        }
        if (user_tema.containsKey(id) && user_tema.get(id).equals("Заправка картриджа")) {
            List<String> last_models = new ArrayList<String>();
            String model_data = DataBase.getUsFileds(id, "last_model");
            if (model_data != null && !model_data.equals("") && !model_data.equals(" "))
                for (String model : model_data.split("&&&"))
                    if (!last_models.contains(model))
                        last_models.add(model);
            UsersData.setOrderModels(id, last_models);
            return;
        } else {
            List<String> last_models = new ArrayList<String>();
            String model_data = DataBase.getUsFileds(id, "last_model_rem");
            if (model_data != null && !model_data.equals("") && !model_data.equals(" "))
                for (String model : model_data.split("&&&"))
                    if (!last_models.contains(model))
                        last_models.add(model);
            UsersData.setOrderModels(id, last_models);
            return;
        }
    }

    public static void saveZayav(User user, Message msg, String txt) {
        try {
            Long id = user.getUID();
            if (!user_tema.containsKey(id) || !user_model.containsKey(id)) {
                user.setUserAction("main");
                user.sendMessage("Ваша заявка не была отправлена! Повторите попытку либо свяжитесь с менеджером!", "main");
                return;
            }
            int znum = DataBase.getZNum();
            DataBase.saveZayavka(znum, user_tema.get(id), bot.u.stringToString(UsersData.getSelectedOrderModels(id), ";"), DataBase.getUserName(Math.toIntExact(id)),
                    DataBase.getUserPhone(Math.toIntExact(id)), DataBase.getUserDopPhone(Math.toIntExact(id)),
                    Math.toIntExact(id), "Поступила", bot.u.getDate("dd/MM/yyyy"), bot.u.getTime("HH:mm:ss"));
            Order order = new Order(znum);
            for (String model : UsersData.getSelectedOrderModels(id)) {
                Long nextSubOrderId = DataBase.getNextSubOrderID();
                SubOrder subOrder = new SubOrder(nextSubOrderId);
                subOrder.setModel(model);
                subOrder.setStatus("Поступила");
                subOrder.setOrderID(order.getOrderID());
                subOrder.addSheetRow(DataBase.getNextSheetRowID().intValue());
                order.addSubOrderID(nextSubOrderId);
                DataBase.saveOrderInSheet(subOrder.getSubOrderID(), "MAIN");
            }
            order.setAddress(user.getUserAdres());
            Integer cartridgeID = pi(DataBase.getUserStr("lastReadedCartridge", id));
            if (cartridgeID != 0)
                order.setCartridgeID((long) cartridgeID);
            order.setSource("Telegram");
            order.setAccurateStatus("Поступила");
            bot.sendToChanel((long) znum, user_tema.get(id) + (user.getUserAction().equals("user_wait_qr") ? " \uD83C\uDD40\uD83C\uDD41" : ""), bot.u.stringToString(UsersData.getSelectedOrderModels(id), ";"),
                    user.getUserType(), DataBase.isCorporationWorker(id) ? DataBase.getUserName(Math.toIntExact(id)) + "\nКомпания: #" + new Corporation(DataBase.getCorporationID(id)).getName() : user.getUserName(),
                    user.getUserPhone());
            bot.updateMainOrderMessage((long) znum);
            bot.updateZStatus(znum, "Заявка подана.", "Заявка " + znum + " успешно передана нашему менеджеру! В скором времени он свяжется с Вами!");
            user.setUserAction("main");
            DataBase.setZNum(znum);
            DataBase.setUsFields(id, "last_z_id", znum);
            //order.addSheetRow(DataBase.getNextSheetRowID().intValue());
            //DataBase.saveOrderInSheet((long) znum);
            if (DataBase.isCorporationWorker(id))
                new Corporation(DataBase.getCorporationID(id)).addOrderID((long) znum);
            OrderLocations.addAllCurrentOrderID((long) znum);
            DataBase.addZForUser(Math.toIntExact(id), znum);
            if (z_kolvo.containsKey(id) && z_kolvo.get(id) <= 3) {
                z_kolvo.put(id, (z_kolvo.get(id) + 1));
            } else {
                z_kolvo.put(id, 1);
                Date da = new Date();
                da.setHours(23);
                da.setMinutes(59);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (z_kolvo.containsKey(id))
                            z_kolvo.remove(id);
                        this.cancel();
                    }
                }, da);
            }
            user_wait_model.remove(id);
            user_model.remove(id);
            user_tema.remove(id);
            UsersData.remAllSelectedOrderModel(id);
            if (admin_data.containsKey(id)) {
                String[] str = admin_data.get(id).split("//");
                DataBase.setUsFields(id, "company_name", str[0]);
                DataBase.setUsFields(id, "name", str[1]);
                DataBase.setUsFields(id, "phone", str[2]);
                admin_data.remove(id);
            }
        } catch (Exception e) {
            System.out.println(prefix() + "Error in send zayav."
                    + "\n-> ID: "
                    + "\n-> MsgID: "
                    + "\n-> Text: " + txt != null ? " нулл" : txt
                    + "\n-> Exception: " + e.getLocalizedMessage()
                    + "\n-> Error line: " + e.getStackTrace()[0].getLineNumber());
            if (msg != null)
                user.sendMessage("Ваша заявка не была отправлена! Повторите попытку либо свяжитесь с менеджером!", "");
        }
    }

    public static void handOstZayav(Long id, Message msg) {
        if (DataBase.getUserType(Math.toIntExact(id)).equals("Не указано")) {
            bot.user_enter_type(msg);
            return;
        }
        user_wait_model.put(id, true);
        new User(id).setUserAction("user_wait_model");
        sendZOstZ(new User(id), msg);
        return;
    }

    public static void sendZOstZ(User user, Message msg) {
        Long user_id = user.getUID();
        if (bot.user_tema.get(user_id).equals("Заправка картриджа")) {
            if (msg != null)
                user.sendMessage("Укажите модель картриджа(ей)/принтера(ов) и количество штук.", "model");
            else
                user.sendMessage("Укажите модель картриджа(ей)/принтера(ов) и количество штук.", "model");
        } else if (msg != null)
            user.sendMessage("Укажите модель принтера и кратко опишите дефект/поломку если она присутсвует.", "model");
        else
            user.sendMessage("Укажите модель принтера и кратко опишите дефект/поломку если она присутсвует.", "model");
        return;
    }

    public static String addonedaytoday(String date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date()); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, 1);// прибавляем 1 дня к установленной дате
        Date newDate = instance.getTime();
        return bot.u.getDate("dd/MM/yyyy", newDate);
    }

    @Override
    public String getBotToken() {
        return "1160732246:AAFhmLYo8-Ot5-LFoIkrHq5q91_SMSjpHk8";
    }

    /*by DarkPhantom1337*/
    public int sendMsgToUser(Long user_id, String text, String menu) {
        SendMessage s = new SendMessage().setChatId(user_id).setText(text);
        handleMenu(menu, s, user_id);
        Integer message_id = 0;
        try {
            message_id = sendMessage(s).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return message_id;
    }

    public void updateMainOrderMessage(Long orderID) {
        Order order = new Order(orderID);
        User user = new User(new TidToUidTable(order.getUID(), false).getTelegramID());
        String text = getMainOrderText(order, user);
        try {
            editMsg(Long.parseLong(getZayavChannelID()), order.getMainMsgID(), text);
            editMsg(Long.parseLong(getZayavChannelID()), order.getMainMsgID(), getMainOrderButtons(order));
        } catch (Exception e) {
        }
    }

    public @NotNull
    InlineKeyboardMarkup getMainOrderButtons(@NotNull Order order) {
        String status = order.getStatus();
        if (status.equalsIgnoreCase("Поступила"))
            return InlineButtons.getObrobotkaButton(order.getOrderID());
        return InlineButtons.getObrobotkaTrueButton(order.getOrderID().intValue());
    }

    public String getMainOrderText(@NotNull Order order, @NotNull User user) {
        String text = "Order #" + order.getOrderID() + " от " + order.getDate() + " " + order.getTime();
        try {
            text += "\nТема: #" + order.getTheme();
            text += "\nКартриджи: ";
            for (Long subOrderID : order.getAllSubOrdersID()) {
                SubOrder subOrder = new SubOrder(subOrderID);
                text += "\n\uD83D\uDC49 #" + subOrder.getModel().replaceAll(" ", "_") + " " + (subOrder.getStatus() == null ? "" : subOrder.getStatus());
            }
            Long courierID = order.getCourierID();
            if (courierID != 0) {
                List<Long> tasksID = order.getAllTasksID();
                text += "\nКурьер: #" + order.getCourierName().replaceAll(" ", "_") + " " + (tasksID.isEmpty() ? "" : new Task(tasksID.get(tasksID.size() - 1)).getTaskStatus());
            }
            text += "\nКлиент: " + (user.getUserType().equals("Компания") ? "#" + user.getUserName().replaceAll(" ", "_") + " из компании '#" + user.getUserCompanyName().replaceAll(" ", "_") + "'" : "Частное лицо #" + user.getUserName().replaceAll(" ", "_"));
            text += "\nНомер клиента: " + user.getUserPhone();
            text += "\nАдрес клиента: #" + user.getUserAdres().replaceAll(" ", "_");
            text += "\nСтатус: #" + order.getAccurateStatus() + (order.getAccurateStatus().equals("Принята") && order.getPrinyal() != null ? " | " + order.getPrinyal() : "");
        } catch (Exception e) {
            text += "\n❌ Ошибка " + e.getLocalizedMessage();
            text += "\nОбратитесь к DarkPhantom1337 для устранения этой проблемы.";
            e.printStackTrace();
        }
        return text;
    }

    public void handleMenu(String menu, SendMessage s, Long user_id) {
        User user = new User(user_id);
        user_id = user.getUID();
        if (menu.startsWith("ADMIN")) {
            String asubmenu = menu.split("/")[1];
            String vacantion = DataBase.getPerFields(user_id, "v_id");
            if (vacantion == null || (!vacantion.equals("admin") && !vacantion.equals("owner")))
                return;
            if (asubmenu.startsWith("Corporations")) {
                String subcorpmenu = menu.split("/")[2];
                if (subcorpmenu.equals("MainMenu")) {
                    s.setReplyMarkup(ReplyButtons.addAdminCorporationsMenu());
                    return;
                }
                if (subcorpmenu.equals("Settings")) {
                    s.setReplyMarkup(InlineButtons.getCorporationButtons(getAllCorporationsID(), "Settings"));
                    return;
                }
                if (subcorpmenu.equals("SettingsMenu")) {
                    s.setReplyMarkup(ReplyButtons.addAdminCorporationsSettingsMenu());
                    return;
                }
                if (subcorpmenu.equals("Delete")) {
                    s.setReplyMarkup(InlineButtons.getAdminCorporationsDeleteMenu());
                    return;
                }
                if (subcorpmenu.equals("DeleteEmployee")) {
                    s.setReplyMarkup(InlineButtons.getCorporationEmployeesListButtons(ah.getSelectedCorporation().get(user_id), subcorpmenu));
                    return;
                }
                if (subcorpmenu.equals("DeleteAdress")) {
                    s.setReplyMarkup(InlineButtons.getCorporationAdressListButtons(ah.getSelectedCorporation().get(user_id), "DA"));
                    return;
                }
            }
            if (asubmenu.startsWith("Personal")) {
                String subpersmenu = menu.split("/")[2];
                if (subpersmenu.equals("MainMenu")) {
                    s.setReplyMarkup(ReplyButtons.addPersonalControlMenu());
                    return;
                }
                if (subpersmenu.equals("DeleteEmployee")) {
                    s.setReplyMarkup(InlineButtons.getPersonalDeleteButtons(DataBase.getAllPersonalID()));
                    return;
                }
                if (subpersmenu.equals("Vacancies")) {
                    s.setReplyMarkup(InlineButtons.getAllVacanciesButtons());
                    return;
                }
            }
            if (asubmenu.startsWith("QR")) {
                String subpersmenu = menu.split("/")[2];
                if (subpersmenu.equals("MainMenu")) {
                    s.setReplyMarkup(ReplyButtons.addAdminQRMenu());
                    return;
                }
            }
            if (asubmenu.equals("MainMenu")) {
                s.setReplyMarkup(ReplyButtons.addMainAdminMenu());
                return;
            }
            if (asubmenu.equals("BackToMainMenu")) {
                s.setReplyMarkup(ReplyButtons.addAdminBackToMainButton());
                return;
            }
        } else {
            if (menu.startsWith("Manager")) {
                String msubmenu = menu.split("/")[1];
                String vacantion = DataBase.getPerFields(user_id, "v_id");
                if (vacantion == null || (!vacantion.equals("manager") && (!vacantion.equals("admin")) && (!vacantion.equals("owner"))))
                    return;
                if (msubmenu.equals("MainMenu")) {
                    s.setReplyMarkup(ReplyButtons.addMainManagerMenu());
                    return;
                }
                if (msubmenu.equals("Reconciliation")) {
                    String recmenu = menu.split("/")[2];
                    if (recmenu.equals("AllSubOrders")) {
                        s.setReplyMarkup(InlineButtons.getAllSubOrdersButtons(Long.parseLong(menu.split("/")[3]), user_id));
                        return;
                    }
                }
                if (msubmenu.equals("CurrentOrders")) {
                    s.setReplyMarkup(InlineButtons.getCurrentZButtons());
                    return;
                }
                if (msubmenu.startsWith("ZMenu")) {
                    s.setReplyMarkup(ReplyButtons.addMainManagerZMenu());
                    return;
                }
                if (msubmenu.startsWith("Routes")) {
                    String routessubmenu = menu.split("/")[2];
                    if (routessubmenu.equals("Main")) {
                        s.setReplyMarkup(ReplyButtons.addManagerRoutesMenu());
                        return;
                    }
                    if (routessubmenu.equals("Order")) {
                        s.setReplyMarkup(InlineButtons.getManagerRoutesSelectOrderButton(Long.parseLong(menu.split("/")[3])));
                        return;
                    }
                    if (routessubmenu.equals("MultiFunctionMenu")) {
                        s.setReplyMarkup(InlineButtons.getManagerMultiFunctionalMenu());
                        return;
                    }
                    if (routessubmenu.equals("ConfirmAction")) {
                        s.setReplyMarkup(InlineButtons.getManagerConfirmActionButtons());
                        return;
                    }
                }
            } else if (menu.startsWith("Courier")) {
                String msubmenu = menu.split("/")[1];
                String vacantion = DataBase.getPerFields(user_id, "v_id");
                if (vacantion == null || (!vacantion.equals("manager") && (!vacantion.equals("admin")) && (!vacantion.equals("owner") && (!vacantion.equals("courier")))))
                    return;
                if (msubmenu.equals("Task")) {
                    String tasksubmenu = menu.split("/")[1];
                    Long taskID = Long.parseLong(menu.split("/")[2]);
                    Order order = new Order(new Task(taskID).getOrderID());
                    if (order.getAccurateStatus().equals("Сбор"))
                        s.setReplyMarkup(InlineButtons.getCourierTaskPreCollectionButtons(taskID));
                    else if (order.getAccurateStatus().equals("Доставка"))
                        s.setReplyMarkup(InlineButtons.getCourierTaskPreDeliveryButtons(taskID));
                    else
                        s.setReplyMarkup(InlineButtons.getCourierTaskPreCollectionButtons(taskID));
                    return;
                }
            }
            if (menu.contains("yesno"))
                s.setReplyMarkup(InlineButtons.getSoglasButton(menu.split("=")[1]));
            if (menu.equals("main")) {
                s.setReplyMarkup(ReplyButtons.addMainMenu());
                if (DataBase.isPersonal(user_id)) {
                    String vc = DataBase.getPerFields(user_id, "v_id");
                    if (vc.equals("admin") || vc.equals("owner"))
                        s.setReplyMarkup(ReplyButtons.addMainPersonalMenu());
                    if (vc.equals("manager"))
                        s.setReplyMarkup(ReplyButtons.addMainUserManagerMenu());
                }
            }
            if (menu.equals("MainPersonalMenu") || menu.equals("MainAdminMenu")) {
                if (DataBase.isPersonal(user_id)) {
                    String vc = DataBase.getPerFields(user_id, "v_id");
                    if (vc.equals("manager") || vc.equals("admin") || vc.equals("owner"))
                        s.setReplyMarkup(ReplyButtons.addMainPersonalMenu());
                }
            }
            if (menu.equals("MainManagerMenu")) {
                if (DataBase.isPersonal(user_id)) {
                    String vc = DataBase.getPerFields(user_id, "v_id");
                    if (vc.equals("manager"))
                        s.setReplyMarkup(ReplyButtons.addMainManagerMenu());
                }
            }
            if (menu.equals("user_qr_zayav")) {
                s.setReplyMarkup(ReplyButtons.addQRZayavMenu());
                return;
            }
            if (menu.equals("type")) {
                s.setReplyMarkup(InlineButtons.getTypeButtons());
                //  addTypeMenu(s); //Тут менюшка делаетс
            }
            if (menu.equals("men_adr")) {
                s.setReplyMarkup(ReplyButtons.addMenAdrMenu());
            }
            if (menu.equals("send_contact")) {
                s.setReplyMarkup(ReplyButtons.addNoContactMenu());
                // s.setReplyMarkup(getSendContactButton());
            }
            if (menu.equals("mened")) {
                s.setReplyMarkup(InlineButtons.getMenedButton());
            }
            if (menu.equals("ras")) {
                s.setReplyMarkup(InlineButtons.getAMenuButtons());
            }
            if (menu.equals("print_search_type")) {
                s.setReplyMarkup(InlineButtons.getPrintTypeButtons());
            }
            if (menu.equals("prov_info")) {
                s.setReplyMarkup(InlineButtons.getProvInfoButtons());
            }
            if (menu.equals("amenu")) {
                s.setReplyMarkup(ReplyButtons.addMainAdminMenu());
            }
            if (menu.equals("model")) {
                //addModelMenu(s, user_id);
                s.setReplyMarkup(ReplyButtons.getSelectedOrderModels(user));
            }
            if (menu.equals("adress")) {
                s.setReplyMarkup(ReplyButtons.addAdressMenu(user));
            }
            if (menu.equals("print_models")) {
                s.setReplyMarkup(InlineButtons.getPrintModelsButtons());
            }
            if (menu.equals("cancelvvod")) {
                s.setReplyMarkup(InlineButtons.getCancelVvodAdm());
            }
            if (menu.contains("send_napom")) {
                s.setReplyMarkup(InlineButtons.getNapomButtons(pi(menu.split("=")[1]), menu.split("=")[2]));
            }
            if (menu.contains("vosst")) {
                s.setReplyMarkup(InlineButtons.getMButton(menu.split("=")[1]));
            }
            if (menu.equals("contact")) {
                s.setReplyMarkup(InlineButtons.getGoogleMapButton());
                s.enableWebPagePreview();
            }
            if (menu.equals("backtomain")) {
                s.setReplyMarkup(ReplyButtons.addBackToMain());
            }
            if (menu.equals("my_zayavki")) {
                s.setReplyMarkup(ReplyButtons.addMyZayavMenu());
            }
            if (menu.contains("ost_zmodel")) {
                s.setReplyMarkup(InlineButtons.getOstZPrinterForPriceListButtons(menu.split("/")[1]));
            }
            if (menu.startsWith("reklamaciya")) {
                Integer nz = Integer.parseInt(menu.split("/")[1]);
                String status = menu.split("/")[2];
                s.setReplyMarkup(InlineButtons.getReklamaciaButtons(nz, user_id, status));
            }
            if (menu.contains("research_price")) {
                s.setReplyMarkup(InlineButtons.getReSearchPrice());
            }
            if (menu.startsWith("reklamaciya")) {
                Integer nz = Integer.parseInt(menu.split("/")[1]);
                String status = menu.split("/")[2];
                s.setReplyMarkup(InlineButtons.getReklamaciaButtons(nz, user_id, status));
            }
        }
    }

    public void handVosst(String subOrdersID, String source, String type) {
        try {
            Order order = new Order(new SubOrder(subOrdersID.split("/")[0]).getOrderID());
            int uid = order.getUID().intValue();
            Boolean mend = false;
            for (String subOrderID : subOrdersID.split("/")) {
                try {
                    SubOrder subOrder = new SubOrder(subOrderID);
                    MasterWork work = subOrder.getWork();
                    if (!work.isUseRecovery())
                        work.addDescription("RECOVERY_USE");
                    order.addDescriptions("VOSST_USE");
                    work.addDescription("RECOVERY_ANSWER");
                    if (type.equals("SOGLASOVANO")) {
                        work.addDescription("RECOVERY_CONFIRM");
                        subOrder.setStatus("Клиент согласовал восстановление");
                    }
                    if (type.equals("MENED")) {
                        work.addDescription("RECOVERY_WAIT");
                        subOrder.setStatus("Клиент хочет уточнить детали");
                        mend = true;
                    }
                    if (type.equals("ByNewCart")) {
                        work.addDescription("RECOVERY_CANCEL");
                        subOrder.setStatus("Клиент хочет купить новый картридж");
                    }
                    if (type.equals("CANCEL")) {
                        work.addDescription("RECOVERY_CANCEL");
                        subOrder.setStatus("Отказ по согласованию");
                    }
                    bot.editMsg(Long.parseLong(getMasterChannelID()), work.getMainMessageID(), mch.getMainZText(work));
                    if (work.isStart())
                        bot.editMsg(Long.parseLong(getMasterChannelID()), work.getMainMessageID(), mcb.getMainZButtons(work));
                    else
                        bot.editMsg(Long.parseLong(getMasterChannelID()), work.getMainMessageID(), mcb.getStartWorkButton(work));
                } catch (Exception e) {
                }
            }
            if (mend) {
                new User(917045001L).sendMessage("\uD83D\uDC49 Клиент " + new User((long) uid, false).getUserName() + " хочет УТОЧНИТЬ ДЕТАЛИ по заявке №" + order.getOrderID(), "Manager/Reconciliation/AllSubOrders/" + order.getOrderID());
                UsersData.clearSelectedOrdersForReconcile(new User(917045001L).getUID());
            }
            bot.updateMainOrderMessage(order.getOrderID());
            bot.deleteAllLastVosstMsg(order.getOrderID());
            order.setVosstMsgText("");
            order.clearVosstMsgID();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void handRate(int zn, int rate, long id, int msgid, String source) {
        DataBase.setZFields(zn, "rate", rate + "");
        DataBase.setZFields(zn, "status", "Завершена|Опрошена");
        Order order = new Order(zn);

        if (source.equals("Viber") == false) {
            editMsg(id, msgid, InlineButtons.getSpasiboZaRateButton());
            user_wait_rate_comm.put(id, true);
            user_wait_rate_zn.put(id, zn);
            int msgi = sendMsgToUser(id, "Пожалуйста напишите комментарий о наших услугах :-)");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (user_wait_rate_zn.containsKey(id) && user_wait_rate_zn.get(id) == zn) ;
                    {
                        deleteMsg(id, msgid);
                        user_wait_rate_comm.remove(id);
                        user_wait_rate_zn.remove(id);
                    }
                    this.cancel();
                }
            }, 60000 * 5);
            String s = order.getString("msg_ids");
            if (s != null)
                s += "," + id + "!" + msgi + "," + id + "!" + msgid;
            else
                s = id + "!" + msgi + "," + id + "!" + msgid;
            DataBase.setZFields(zn, "msg_ids", s);
            user_wait_rate_zn_тsg.put(id, msgi);
        }
        sendToArchiveTask(zn, rate, id, false);
    }


    public void sendToArchiveTask(Integer zn, Integer rate, Long id, Boolean type) {
        Order order = new Order(zn);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (rate <= 4) {
                    sendToChanel((long) zn);
                }
                if (order.getStatus().equals("Завершена|Опрошена")) {
                    DataBase.setZFields(zn, "status", "Завершена|Опрошена|Архив");
                    String msggg = order.getString("msg_ids");

                    sendToArch(zn);
                    deleteMsg(Long.parseLong(getZayavChannelID()), order.getMainMsgID());
                    System.out.println(prefix() + "Заявка успешно перенесена в архив");
                    if (msggg != null && !msggg.equals("NULL")) {
                        String[] msgs = msggg.split(",");
                        try {
                            for (String mid : msgs) {
                                try {
                                    String[] s = mid.split("!");
                                    int i = 0;
                                    if (s[0] != "") {
                                        long si = s[0].contains("-") ? Long.parseLong(s[0].replaceAll("-", "")) * -1 : Long.parseLong(s[0]);
                                        long mi = s[1].contains("-") ? Long.parseLong(s[1].replaceAll("-", "")) * -1 : Long.parseLong(s[1]);
                                        deleteMsg((long) si, (int) mi);
                                    } else {
                                        long si = s[1].contains("-") ? Long.parseLong(s[1].replaceAll("-", "")) * -1 : Long.parseLong(s[1]);
                                        long mi = s[2].contains("-") ? Long.parseLong(s[2].replaceAll("-", "")) * -1 : Long.parseLong(s[2]);
                                        deleteMsg((long) si, (int) mi);
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                    if (type == false) {
                        user_wait_rate_zn_тsg.remove(id);
                        user_wait_rate_zn.remove(id);
                        user_wait_rate_comm.remove(id);
                    }
                    DataBase.saveZToArch(zn);
                    DataBase.delete(zn);
                    this.cancel();
                }
                this.cancel();
            }
        }, 60000 * 2);
    }

    public List<Long> getAllCorporationsID() {
        List<Long> all_corporations_id = new ArrayList<Long>();
        for (int i = 1; i <= DataBase.getUtilsField((long) 17, "val"); i++)
            if (new Corporation((long) i).getName() != null)
                all_corporations_id.add((long) i);
        return all_corporations_id;
    }


    public ReplyKeyboardMarkup getSendContactButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        //sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow one = new KeyboardRow(), two = new KeyboardRow();
        one.add(new KeyboardButton("Поделиться контактом").setRequestContact(true));
        keyboard.add(one);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public void sendToChanel(Long nza, String tema, String model, String type, String name, String phone) throws IOException {
        String text = "Заявка /" + nza + "/" + "#Z" + nza
                + "\nТема: #" + tema
                + "\nМодель: #" + (model != null ? model.replaceAll(" ", "_") : "Не указано")
                + "\nАдрес: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "adress").replaceAll(" ", "_").replaceAll(",", "_")
                + "\nТип: #" + type
                + (type.equals("Компания") ? "\nНазвание: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "company_name").replaceAll(" ", "_") : "")
                + "\nИмя: #" + name.replaceAll(" ", "_")
                + "\nНомер телефона: #Tel" + phone
                + (DataBase.isPersonal(Long.parseLong(new Order(nza).getString("u_id"))) ? "\nПодал: " + DataBase.getPerFields(Long.parseLong(new Order(nza).getString("u_id")), "name") : "")
                + "\nСтатус: /Поступила/" + "";
        SendMessage s = new SendMessage().setChatId(getZayavChannelID()).setText(text);
        s.setReplyMarkup(InlineButtons.getObrobotkaButton(nza));
        try {
            DataBase.setZFields(Math.toIntExact(nza), "main_msg_id", "" + sendMessage(s).getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendCustomMessageToZChanel(Integer nza, String text, InlineKeyboardMarkup buttons) throws IOException {
        SendMessage s = new SendMessage().setChatId(getZayavChannelID()).setText(text);
        s.setReplyMarkup(buttons);
        try {
            DataBase.setZFields(Math.toIntExact(nza), "main_msg_id", "" + sendMessage(s).getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public Integer sendToUserInfoChanel(String text) {
        SendMessage s = new SendMessage().setChatId("-1001283507373").setText(text);
        Integer msgid = 0;
        try {
            msgid = sendMessage(s).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return msgid;
    }

    public Integer sendToLogChanel(String text) {
        try {
            SendMessage s = new SendMessage().setChatId("-1001143163268").setText(bot.u.getDate("[dd/MM/yyyy hh:mm:ss] -> ") + text);
            Integer msgid = 0;
            try {
                msgid = sendMessage(s).getMessageId();
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return msgid;
        } catch (Exception e) {
            error("Message to log channel not send.Text: " + text);
        }
        return 0;
    }

    public void sendToChanelReklamaciya(Long nza, String tema, String model, String type, String name, String phone) throws IOException {
        Order order = new Order(nza);
        String text = "РЕКЛАМАЦИЯ /" + nza + "/" + "#Z" + nza
                + "\nТема: #" + tema
                + "\nМодель: #" + (model != null ? model.replaceAll(" ", "_") : "Не указано")
                + "\nАдрес: #" + DataBase.getUsFileds(order.getUID(), "adress").replaceAll(" ", "_").replaceAll(",", "_")
                + "\nТип: #" + type
                + (type.equals("Компания") ? "\nНазвание: #" + DataBase.getUsFileds(order.getUID(), "company_name").replaceAll(" ", "_") : "")
                + "\nИмя: #" + name.replaceAll(" ", "_")
                + "\nНомер телефона: #Tel" + phone
                + (DataBase.isPersonal(order.getUID()) ? "\nПодал: " + DataBase.getPerFields(order.getUID(), "name") : "")
                + "\nСтатус: /Поступила/" + " #Поступила";
        SendMessage s = new SendMessage().setChatId(getZayavChannelID()).setText(text);
        s.setReplyMarkup(InlineButtons.getObrobotkaReklamButton(nza));
        try {
            DataBase.setZFields(Math.toIntExact(nza), "main_msg_id", "" + sendMessage(s).getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendViberZToTelegramChanel(Long z_id, Integer dark_id, Integer id) throws IOException {
        String nametype = "Не определено.";
        try {
            nametype = (DataBase.getVZUFileds(dark_id, "name")) + (DataBase.getVZUFileds(dark_id, "type").equals("Компания") ? (" из компании #" + (DataBase.getVZUFileds(dark_id, "company_name")).replaceAll(" ", "_")) : "");
        } catch (Exception e) {
        }
        Order order = new Order(z_id);
        String text = "Заявка /#Z" + z_id + "/"
                + "\nТема: #" + (DataBase.getVZFileds(id, "theme").equals("zapravka") ? "Заправка картриджа" : "Ремонт принтера")
                + "\nМодель: #" + (DataBase.getVZFileds(id, "model"))
                + "\nАдрес: #" + (DataBase.getVZFileds(id, "adres").replaceAll(" ", "_"))
                + "\nИмя/Тип: #" + nametype
                + "\nНомер телефона: #Tel" + (DataBase.getVZUFileds(dark_id, "phone"))
                + "\nГород: #" + (order.getCity())
                + "\nИсточник: #" + (order.getSource()) + " ✓"
                + "\nСтатус: -> #Поступила";
        SendMessage s = new SendMessage().setChatId(getZayavChannelID()).setText(text);
        s.setReplyMarkup(InlineButtons.getObrobotkaButton(z_id));
        try {
            DataBase.setZFields(Math.toIntExact(z_id), "main_msg_id", "" + sendMessage(s).getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendToChanel(Long nza) {
        String text = "";
        if (new Order(nza).getString("source").equals("Viber")) {
            int uid = pi(new Order(nza).getString("u_id"));
            text = "Заявка #/" + nza + "/ #Z" + nza
                    + "\nТема: #" + new Order(nza).getString("theme")
                    + "\nДата и время подачи: #" + new Order(nza).getString("date") + "  " + new Order(nza).getString("time")
                    + "\nМодель: #" + new Order(nza).getString("model").replaceAll(" ", "_")
                    + "\nАдрес: #" + DataBase.getVZUFileds(uid, "adress").replaceAll(" ", "_").replaceAll(",", "_")
                    + "\nТип: #" + DataBase.getVZUFileds(uid, "type")
                    + "\nКонтактное лицо: #" + DataBase.getVZUFileds(uid, "name").replaceAll(" ", "_")
                    + (DataBase.getVZUFileds(uid, "type").equals("Компания") ? "\nНазвание компании: #" + DataBase.getVZUFileds(uid, "company_name").replaceAll(" ", "_") : "")
                    + "\nНомер телефона: #Tel" + new Order(nza).getString("phone")
                    + "\nОценка: #" + new Order(nza).getString("rate")
                    + "\nКомментарий: #" + new Order(nza).getString("description")
                    + "\nСтатус: #" + new Order(nza).getString("status").replaceAll(" ", "_")
                    + "\nПринимал: #" + new Order(nza).getString("city");
        } else {
            text = "Заявка #/" + nza + "/ #Z" + nza
                    + "\nТема: #" + new Order(nza).getString("theme")
                    + "\nДата и время подачи: #" + new Order(nza).getString("date") + "  " + new Order(nza).getString("time")
                    + "\nМодель: #" + new Order(nza).getString("model").replaceAll(" ", "_")
                    + "\nАдрес: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "adress").replaceAll(" ", "_").replaceAll(",", "_")
                    + "\nТип: #" + DataBase.getUserType(pi(new Order(nza).getString("u_id")))
                    + "\nКонтактное лицо: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "name").replaceAll(" ", "_")
                    + (DataBase.getUserType(pi(new Order(nza).getString("u_id"))).equals("Компания") ? "\nНазвание компании: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "name").replaceAll(" ", "_") : "")
                    + "\nНомер телефона: #Tel" + new Order(nza).getString("phone")
                    + "\nОценка: #" + new Order(nza).getString("rate")
                    + "\nКомментарий: #" + new Order(nza).getString("description")
                    + "\nСтатус: #" + new Order(nza).getString("status").replaceAll(" ", "_")
                    + "\nПринимал: #" + new Order(nza).getString("city");
        }
        sendMsg(getCellChannelID(), text, InlineButtons.getCellButton(nza));
    }

    public void sendCancelZayav(Long nza) {
        Integer nz = Math.toIntExact(nza);
        String text = "Заявка #/" + nza + "/ #Z" + nza
                + "\nТема: #" + new Order(nza).getString("theme")
                + "\nДата и время подачи: #" + new Order(nza).getString("date") + " " + new Order(nza).getString("time")
                + "\nМодель: #" + new Order(nza).getString("model").replaceAll(" ", "_")
                + "\nАдрес: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "adress").replaceAll(" ", "_").replaceAll(",", "_")
                + "\nТип: #" + DataBase.getUserType(pi(new Order(nza).getString("u_id")))
                + "\nКонтактное лицо: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "name").replaceAll(" ", "_")
                + (DataBase.getUserType(pi(new Order(nza).getString("u_id"))).equals("Компания") ? "\nНазвание компании: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "name").replaceAll(" ", "_") : "")
                + "\nНомер телефона: #Tel" + new Order(nza).getString("phone")
                + "\nСтатус: #" + new Order(nza).getString("status").replaceAll(" ", "_")
                + "\nПринимал: #" + new Order(nza).getString("prinyal")
                + "\n❌❌❌❌❌❌❌❌❌❌❌❌❌"
                + "\nКлиент хочет отменить заявку!";
        sendMsg(getZayavChannelID(), text, InlineButtons.getCancelZayavButtons(nza));
    }

    public static String getCellChannelID() {
        return "-1001415877009";
    }

    public int sendMsg(String id, String text, InlineKeyboardMarkup bt) {
        SendMessage s = new SendMessage().setChatId(id).setText(text);
        s.setText(text);
        if (bt != null)
            s.setReplyMarkup(bt);
        int msgid = 0;
        try {
            msgid = sendMessage(s).getMessageId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgid;
    }

    public void sendToArch(Integer nza) {
        Order order = new Order(nza);
        String source = order.getSource();
        String type = source.equals("Viber") ? DataBase.getVZUFileds(pi(new Order(nza).getString("u_id")), "type") : DataBase.getUserType(pi(new Order(nza).getString("u_id")));
        String name = source.equals("Viber") ? DataBase.getVZUFileds(pi(new Order(nza).getString("u_id")), "name") : DataBase.getUserName(pi(new Order(nza).getString("u_id")));
        String сname = source.equals("Viber") ? DataBase.getVZUFileds(pi(new Order(nza).getString("u_id")), "company_name") : DataBase.getUserStr("company_name", pi(new Order(nza).getString("u_id")));
        String text = getMainOrderText(order, new User(new TidToUidTable(order.getUID(), false).getTelegramID()))
                + "\nОценка: #" + new Order(nza).getString("rate")
                + "\nКомментарий: #" + new Order(nza).getString("description")
                + "\nСтатус: #" + new Order(nza).getString("status").replaceAll(" ", "_")
                + "\nПринимал: #" + new Order(nza).getString("city");
        sendMsg(getArchChannelID(), text, null);
    }

    public static String getArchChannelID() {
        return "-1001319666445";
    }

    public static String getContrChannelID() {
        return "-1001455850449";
    }

    public static String getMasterChannelID() {
        return "-1001233518211";
    }


    public void sendOpros(Long id, Integer nz) throws TelegramApiException {
        Order order = new Order(nz);
        String text = "Доброе утро." + order.getDate() + " Вы оставляли заявку №"
                + nz + " на " + (order.getTheme().contains("Заправка") ? "заправку картриджа" : "ремонт принтера") + "\n" +
                "Пожалуйста оцените работу нашего сервиса по шкале от 0 до 7.\n";
        if (order.getSource().equals("Viber")) {
            try {
                URL oracle = new URL("https://ay.dn.ua/ViberBot/src/ChangeStatus.php?dark_id=" + order.getUID() + "&zn=" + nz + "&status=opros&key=1337&text="
                        + text.replaceAll(" ", "%20").replaceAll("\\s", "%5Cn"));
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    System.out.println(inputLine);
                in.close();
            } catch (Exception e) {
            }
        } else {
            SendMessage s = new SendMessage().setChatId(id).setText(text);
            s.setReplyMarkup(InlineButtons.getOcenkiButtons(nz));
            try {
                sendMessage(s);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getZayavChannelID() {
        return "-1001438904019";
    }

    public void sendToChanelOpros(Long nza, String tema, String model, String type, String name, String phone, String company_name, String source) throws IOException {
        String text = "";
        text = "Заявка #Z" + nza + " #завершена ! "
                + "\nТема: #" + tema
                + "\nКлиент: #" + (type.equals("Компания") ? name + " из компании #" + company_name : name)
                + "\nНомер телефона: #Tel" + phone
                + "\nИсточник: #" + source
                + "\n--> Выберете когда опросить клиента.";
        int msgid = sendMsg(getZayavChannelID(), text, InlineButtons.getOprosButton(nza));
        DataBase.setZFields(Math.toIntExact(nza), "main_msg_id", "" + msgid);
        String ss = new Order(nza).getString("msg_ids");
        if (ss != null) {
            ss += "," + "-1001438904019" + "!" + 6;
        } else {
            ss = "" + "-1001438904019" + "!" + msgid;
        }
        DataBase.setZFields(Math.toIntExact(nza), "msg_ids", ss);
    }

    public void user_welcome(Message msg) {
        sendMsgToUser(msg.getChatId(), "Здравствуйте, я чат-бот сервисного центра KOI."
                + "\nЗдесь вы можете: "
                + "\n- Оставить заявку на заправку картриджа;"
                + "\n- Оставить заявку на ремонт принтера;"
                + "\n- Узнать ЦЕНЫ ЗАПРАВКИ КАРТРИДЖЕЙ;"
                + "\n- Приобрести принтер/катридж/расходные материалы;"
                + "\n- Посмотреть где мы находимся и как к нам добраться;"
                + "\n- Связяться с нашим менеджером;", "");
    }

    public void user_enter_type(Message msg) {
        sendMsgToUser(msg.getChatId(), "Вы представитель компании или частное лицо? Для компаний у нас действует специальная система скидок и бонусов!", "type");
    }

    public void user_add_contact(Message msg) {
        sendMsgToUser(msg.getChatId(), "Для удобства в дальнейшем общении прошу поделиться Вашим контактом.(Кнопка отправить контакт ниже ↓)", "send_contact");
    }

    public void user_spasibo_contact(Message msg) {
        sendMsgToUser(msg.getChatId(), msg.getContact().getFirstName() + ", спасибо за предоставление Вашего номера телефона! ", "");
    }

    public void user_cancel_contact(Message msg) {
        sendMsgToUser(msg.getChatId(), "Что бы  принять заявку мне нужен Ваш номер телефона. Данная информация останется конфиденциальной.", "send_contact");
    }

    public void user_reg_complete(Message msg) {
        sendMsgToUser(msg.getChatId(), "Теперь Вам доступна возможность оставить зявку на заправку/ремонт картриджа/принтера.", "work_true");
    }

    public void contacts(Message msg) {
        sendMsgToUser(msg.getChatId(), "Телефоны: "
                        + "\n- +380 50 03 444 03 "
                        + "\n- +380 63 03 444 03 "
                        + "\n- +380 68 03 444 03 "
                        + "\n- +380 57 765 44 03 "
                        + "\n Сайт: www.koi.in.ua"
                        + "\n Email: mail@koi.in.ua"
                        + "\n Адрес: ул. Пушкинская, 65/1  (вход с Пушкинского въезда, станция метро Пушкинская)"
                , "contact");
    }

    public void tryExecureMethod(org.telegram.telegrambots.api.methods.BotApiMethod method) {
        try {
            bot.execute(method);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}