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
            adm_send_chas = new HashMap<Integer, Boolean>(), adm_send_all = new HashMap<>(), adm_send_id = new HashMap<Integer, Boolean>(), adm_wait_ids = new HashMap<Integer, Boolean>();

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

    public static Timer vh = new Timer(), vz, vr, vv, vn;

    public static List<String> menus = Arrays.asList(new String[]{"Оставить заявку на заправку", "Оставить заявку на ремонт", "Написать менеджеру", "Адрес/Контакты", "Мои заявки", "Прайс лист", "Текущие заявки", "Завершённые заявки", "Главное меню", "Вернуться в главное меню", "Подать заявку", "Используя QR-код", "Заполняя форму"});

    int nomer = 1;

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


    public static GoogleSheetsAPI sheetsAPI;

    public Boolean enabled = false;

    public static void main(String[] args) throws IOException, ServiceException {
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
            startDrHandler();
            startDays();
            hl = new Handlers(bot);
            ah = new AdminHandler(bot);
            mh = new ManagerHandler(bot);
            mch = new MasterChannelHandler();
            mcb = new MasterChannelButtons(bot);
            uch = new UserCallbackHandler();
            uphh = new UserPhotoHandler();
            ucth = new UserContactHandler();
            pch = new PersonalCallbackHandler();
            sheetsAPI = new GoogleSheetsAPI();
            startVzHandler();
            startAutoRestartVzHandler();
            startVvHandler();
            startAutoRestartVvHandler();
            startVrHandler();
            startAutoRestartVrHandler();
            viberHandlers();
          /*  bot.sendMsgToUser(396407333L,"Вы были возвращены в главное меню!","main");
            bot.sendMsgToUser(556349297L,"Вы были возвращены в главное меню!2","main");
            */
        }
    }

    public List<Long> getSelectedOrderForReconsile(Long userID) {
        if (selected_sub_orders_for_reconsile.containsKey(userID))
            return selected_sub_orders_for_reconsile.get(userID);
        else
            selected_sub_orders_for_reconsile.put(userID, new ArrayList<Long>());
        return new ArrayList<Long>();
    }

    public void addSelectedOrderForReconsile(Long userID, Long subOrderID) {
        List<Long> selected = getSelectedOrderForReconsile(userID);
        if (selected.contains(subOrderID))
            selected.remove(subOrderID);
        else selected.add(subOrderID);
    }

    public void clearSelectedOrderForReconsile(Long userID) {
        selected_sub_orders_for_reconsile.remove(userID);
    }

    public List<String> getOrderModels(Long userID) {
        if (order_models.containsKey(userID))
            return order_models.get(userID);
        order_models.put(userID, new ArrayList<>());
        return getOrderModels(userID);
    }

    public void setOrderModels(Long userID, List<String> models) {
        order_models.put(userID, models);
    }

    public void addOrderModel(Long userID, String model) {
        List<String> models = getOrderModels(userID);
        if (!models.contains(model)) {
            models.add(model);
            setOrderModels(userID, models);
        }
    }

    public void remOrderModel(Long userID, String model) {
        List<String> models = getOrderModels(userID);
        models.remove(model);
        setOrderModels(userID, models);
    }

    public List<String> getSelectedOrderModels(Long userID) {
        if (selected_order_models.containsKey(userID))
            return selected_order_models.get(userID);
        selected_order_models.put(userID, new ArrayList<>());
        return getOrderModels(userID);
    }

    public void setSelectedOrderModels(Long userID, List<String> models) {
        selected_order_models.put(userID, models);
    }

    public void addSelectedOrderModel(Long userID, String model) {
        List<String> models = getSelectedOrderModels(userID);
        if (!models.contains(model)) {
            models.add(model);
            setSelectedOrderModels(userID, models);
        } else {
            remSelectedOrderModel(userID, model);
        }
    }

    public void remSelectedOrderModel(Long userID, String model) {
        List<String> models = getSelectedOrderModels(userID);
        models.remove(model);
        setSelectedOrderModels(userID, models);
    }

    public void remAllSelectedOrderModel(Long userID) {
        selected_order_models.remove(userID);
    }

    public static void startAutoRestartVzHandler() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int k = 0;
                String sn = DataBase.getUFileds(12, "val");
                int n = ((sn != null && !sn.equals("NULL")) ? (Integer.parseInt(sn)) : 0);
                for (int i = 1; i <= n; i++)
                    if (DataBase.getVZFileds(i, "status").equals("HANDLING"))
                        k++;
                if (k == 0) {
                    // Bot.bot.sendMsg(getContrChannelID(), "[KOI-KH] [" + bot.u.getDate("dd/MM/YYYY") + "] [" + bot.u.getTime("HH:mm:ss") + "] -> Viber Zayavki handler - restarting.", null);
                    vz.cancel();
                    vz.purge();
                    DataBase.setControllFields(1, "vz_handler", 0);
                    startVzHandler();
                } else {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // Bot.bot.sendMsg(getContrChannelID(), "[KOI-KH] [" + bot.u.getDate("dd/MM/YYYY") + "] [" + bot.u.getTime("HH:mm:ss") + "] -> Viber Zayavki handler - ПРИНУДИТЕЛЬНАЯ ПЕРЕЗАГРУЗКА.", null);
                            vz.cancel();
                            vz.purge();
                            DataBase.setControllFields(1, "vz_handler", 0);
                            for (int i = 1; i <= n; i++)
                                if (DataBase.getVZFileds(i, "status").equals("HANDLING"))
                                    DataBase.setVZFields(i, "status", "ENTERED");
                            startVzHandler();
                            System.gc();
                            this.cancel();
                        }
                    }, 60000 * 1);
                }
            }
        }, 60000 * 1, 60000 * 30);
    }

    public static void startAutoRestartVvHandler() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int kk = 0;
                String sk = DataBase.getUFileds(13, "val");
                int k = ((sk != null && !sk.equals("NULL")) ? (Integer.parseInt(sk)) : 0);
                for (int i = 1; i <= k; i++)
                    if (DataBase.getVSFileds(i, "status").equals("HANDLING"))
                        kk++;
                if (kk == 0) {
                    //Bot.bot.sendMsg(getContrChannelID(), "[KOI-KH] " + bot.u.getDate("[dd/MM/YYYY] [HH:mm:ss]") + " -> Viber Vosstanovleniya handler - restarting.", null);
                    vv.cancel();
                    vv.purge();
                    DataBase.setControllFields(1, "vv_handler", 0);
                    startVvHandler();
                } else {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // Bot.bot.sendMsg(getContrChannelID(), "[KOI-KH] " + bot.u.getDate("[dd/MM/YYYY] [HH:mm:ss]") + " -> Viber Vosstanovleniya handler - ПРИНУДИТЕЛЬНАЯ ПЕРЕЗАГРУЗКА.", null);
                            vv.cancel();
                            vv.purge();
                            DataBase.setControllFields(1, "vv_handler", 0);
                            for (int i = 1; i <= k; i++)
                                if (DataBase.getVSFileds(i, "status").equals("HANDLING"))
                                    DataBase.setVSFields(i, "status", "ENTERED");
                            startVvHandler();
                            System.gc();
                            this.cancel();
                        }
                    }, 60000 * 1);
                }
            }
        }, 60000 * 1, 60000 * 30);
    }

    public static void startAutoRestartVrHandler() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int kk = 0;
                String sz = DataBase.getUFileds(14, "val");
                int z = ((sz != null && !sz.equals("NULL")) ? (Integer.parseInt(sz)) : 0);
                for (int i = 1; i <= z; i++)
                    if (DataBase.getVRField(i, "status").equals("HANDLING"))
                        kk++;
                if (kk == 0) {
                    vr.cancel();
                    vr.purge();
                    DataBase.setControllFields(1, "vr_handler", 0);
                    startVrHandler();
                } else {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //Bot.bot.sendMsg(getContrChannelID(), "[KOI-KH] " + bot.u.getDate("[dd/MM/YYYY] [HH:mm:ss]") + " -> Viber Rates handler - ПРИНУДИТЕЛЬНАЯ ПЕРЕЗАГРУЗКА.", null);
                            vr.cancel();
                            vr.purge();
                            DataBase.setControllFields(1, "vr_handler", 0);
                            for (int i = 1; i <= z; i++)
                                if (DataBase.getVRField(i, "status").equals("HANDLING"))
                                    DataBase.setVRField(i, "status", "ENTERED");
                            startVrHandler();
                            this.cancel();
                        }
                    }, 60000 * 1);
                }
            }
        }, 60000 * 1, 60000 * 30);
    }

    public static void startVzHandler() {
        vz = new Timer();
        vz.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Bot.bot.sendMsg(getContrChannelID(), "[KOI-KH] [" + bot.u.getDate("dd/MM/YYYY") + "] [" + bot.u.getTime("mm:HH:ss") + "] -> Viber Zayavki handler - UPDATING.", null);
                DataBase.setControllFields(1, "vz_handler", 1);
                String sn = DataBase.getUFileds(12, "val");
                int n = ((sn != null && !sn.equals("NULL")) ? (Integer.parseInt(sn)) : 0);
                if (n != 0)
                    for (int i = 1; i <= n; i++) {
                        try {
                            if (DataBase.getVZFileds(i, "status").equals("ENTERED")) {
                                DataBase.setVZFields(i, "status", "HANDLING");
                                int zn = Integer.parseInt(DataBase.getVZFileds(i, "z_id")),
                                        dark_id = Integer.parseInt(DataBase.getVZFileds(i, "dark_id"));
                                String theme = DataBase.getVZFileds(i, "theme").equals("zapravka") ? "Заправка картриджа" : "Ремонт принтера", model = DataBase.getVZFileds(i, "model");
                                DataBase.saveZayavka(zn, theme, bot.u.correctStr(model), DataBase.getVZUFileds(dark_id, "name"),
                                        DataBase.getVZUFileds(dark_id, "phone"), "нету",
                                        dark_id, "Поступила", bot.u.getDate("dd/MM/yyyy"), bot.u.getTime("HH:mm:ss"));
                                DataBase.setZFields(zn, "adress", DataBase.getVZFileds(i, "adres"));
                                DataBase.setZFields(zn, "city", "Харьков");
                                DataBase.setZFields(zn, "source", "Viber");
                                DataBase.setVUField(dark_id, "last_z_id", "" + zn);
                                DataBase.setVUField(dark_id, "all_z", DataBase.getVZUFileds(dark_id, "all_z") + "," + zn);
                                DataBase.setVUField(dark_id, "z_in_day", "" + (pi(DataBase.getVZUFileds(dark_id, "z_in_day")) + 1));
                                try {
                                    bot.sendViberZToTelegramChanel((long) zn, dark_id, i);
                                    DataBase.setVZFields(i, "status", "HANDLED");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            }
        }, 1000, 30000);
    }

    public static void startVvHandler() {
        vv = new Timer();
        vv.schedule(new TimerTask() {
            @Override
            public void run() {
                //Bot.bot.sendMsg(getContrChannelID(), "[KOI-KH] [" + bot.u.getDate("dd/MM/YYYY") + "] [" + bot.u.getTime("mm:HH:ss") + "] -> Viber Vossstanovleniya handler - UPDATING.", null);
                DataBase.setControllFields(1, "vv_handler", 1);
                String sk = DataBase.getUFileds(13, "val");
                int k = ((sk != null && !sk.equals("NULL")) ? (Integer.parseInt(sk)) : 0);
                for (int i = 1; i <= k; i++) {
                    try {
                        if (DataBase.getVSFileds(i, "status").equals("ENTERED")) {
                            DataBase.setVSFields(i, "status", "HANDLING");
                            int zn = Integer.parseInt(DataBase.getVSFileds(i, "z_id")),
                                    dark_id = Integer.parseInt(DataBase.getVSFileds(i, "dark_id"));
                            bot.handVosst(new Order(zn).getSubOrdersID().replaceAll(";", "/"), "Viber", DataBase.getVSFileds(i, "answer"));
                            DataBase.setVSFields(i, "status", "HANDLED");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1000, 30000);
    }

    public static void startVrHandler() {
        vr = new Timer();
        vr.schedule(new TimerTask() {
            @Override
            public void run() {
                // Bot.bot.sendMsg(getContrChannelID(), "[KOI-KH] [" + bot.u.getDate("dd/MM/YYYY") + "] [" + bot.u.getTime("mm:HH:ss") + "] -> Viber Rates handler - UPDATING.", null);
                DataBase.setControllFields(1, "vr_handler", 1);
                String sz = DataBase.getUFileds(14, "val");
                int z = ((sz != null && !sz.equals("NULL")) ? (Integer.parseInt(sz)) : 0);
                for (int i = 1; i <= z; i++) {
                    try {
                        if (DataBase.getVRField(i, "status").equals("ENTERED")) {
                            DataBase.setVRField(i, "status", "HANDLING");
                            int zn = Integer.parseInt(DataBase.getVRField(i, "z_id")),
                                    dark_id = Integer.parseInt(DataBase.getVRField(i, "dark_id"));
                            if (pi(DataBase.getVRField(i, "rate")) != 1337)
                                bot.handRate(zn, pi(DataBase.getVRField(i, "rate")), dark_id, 1337, "Viber");
                            else
                                DataBase.setZFields(zn, "description", DataBase.getVRField(i, "description"));
                            DataBase.setVRField(i, "status", "HANDLED");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1000, 30000);
    }


    public static void viberHandlers() {
        Boolean iscons = true;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int id : DataBase.getAllViberUserId()) {
                    DataBase.setVUField(id, "z_in_day", "" + 0);
                }
                System.out.println("Cleared z in day all viber userss");
                System.gc();
            }
        }, 60000 * 1, ((60000 * 60) * 23));
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

    public static void handDR() {
        Date date = new Date(), toz = date;
        String date1 = new SimpleDateFormat("dd/MM/yyyy").format(date);
        Integer h = Integer.parseInt(new SimpleDateFormat("HH").format(date));
        if (h >= 9 && h <= 12) {
            System.out.println(Bot.prefix() + "Time to dr");
            startDrHandler();
        } else {
            toz.setDate(date.getDate() + 1);
            toz.setHours(6);
            toz.setMinutes(55);
            System.out.println(bot.prefix() + "Now is not the time for DR" +
                    "Next time for dr: " + bot.u.getDate("dd/MM/YYYY", toz) + " " + bot.u.getTime("HH:mm:ss", toz));
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(bot.prefix() + "Time for DR");
                    startDrHandler();
                    System.gc();
                }
            }, toz, ((600000 * 6) * 24));
        }
    }

    public static void handNewsLetter() {
        Date date = new Date(), toz = date;
        String date1 = new SimpleDateFormat("dd/MM/yyyy").format(date);
        Integer h = pi(new SimpleDateFormat("HH").format(date));
        if (h >= 9 && h <= 12) {
            System.out.println(prefix() + "Time to news! Starting news handler");
            //bot.startQuiz();
        } else {
            toz.setDate(date.getDate() + 1);
            toz.setHours(8);
            toz.setMinutes(55);
            System.out.println(Bot.prefix() + "Now is not time for news" +
                    "Next time for news: " + bot.u.getDate("dd/MM/YYYY", toz) + " " + bot.u.getTime("HH:mm:ss", toz));
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(Bot.prefix() + "Now is time for news! Start news handler.");
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

    public void handlNewsLetter() throws ParseException {//Начать обработку
        int n = pi(DataBase.getUFileds(4, "val"));
        if (n != 0) {
            for (int i = 1; i <= n; i++) {
                String type = DataBase.getNL(i, "type"), status = DataBase.getNL(i, "status");
                if (type.equals("ALL") && status.equals("START")) {
                    int h = pi(bot.u.getTime("HH"));
                    if (h >= 11 && h <= 13) {
                        startAllNewsLetter(i);
                    } else {
                        Date date = new Date();
                        date.setDate(date.getDate() + 1);
                        date.setHours(10);
                        date.setMinutes(55);
                        int finalI = i;
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                startAllNewsLetter(finalI);
                                DataBase.setNLFields(finalI, "status", "STOP");
                            }
                        }, date, (60000 * 6) * 24);
                        return;
                    }
                } else if (type.equals("ALLDATE") && status.equals("START")) {
                    String date = DataBase.getNL(i, "date"), time = DataBase.getNL(i, "time");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date sdate = formatter.parse(date + " " + time);
                    System.out.println(bot.u.getDate("dd/MM/yyyy", sdate) + " " + bot.u.getTime("HH/mm/ss", sdate));
                    int finalI1 = i;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            startAllNewsLetter(finalI1);
                            DataBase.setNLFields(finalI1, "status", "STOP");
                            this.cancel();
                        }
                    }, sdate);
                }
            }
        } else {
            System.out.println(prefix() + "Actual news for letters empty");
        }
    }

    public static String getFileExtension(java.io.File file) {
        String fileName = file.getName();
        // если в имени файла есть точка и она не является первым символом в названии файла
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
            return fileName.substring(fileName.lastIndexOf(".") + 1);
            // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return "";
    }

    public static void startDrHandler() {
        final String fname = DataBase.getSL(1, "file_name"), txt = DataBase.getSL(1, "text"), status = DataBase.getSL(1, "status");
        if (status.equals("START")) {
            System.out.println(prefix() + "Start dr");
            if (fname.equals("FILE_NOT_ADDED") && txt.equals("TEXT_NOT_ADDED")) {
                System.out.println(prefix() + "File and text not added");
                return;
            }
            if (fname.equals("FILE_NOT_ADDED") && !txt.equals("TEXT_NOT_ADDED")) {
                System.out.println(prefix() + "File not added");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int n = 0, all = 0;
                        for (long i : DataBase.getAllUserId()) {
                            String dr_date = DataBase.getUsFileds(i, "dr");
                            if (dr_date == "Не указано") {
                                all++;
                                continue;
                            }
                            if (dr_date.equals(bot.u.getDate("dd/MM/yyyy"))) {
                                sendMsgToUser((long) i, txt);
                                n++;
                            }
                            all++;
                        }
                        for (int i : DataBase.getAllViberUserId()) {
                            String dr_date = DataBase.getVZUFileds(i, "dr_date");
                            if (dr_date != null) {
                                if (dr_date == "Не указано") {
                                    all++;
                                    continue;
                                }
                                if (dr_date.equals(bot.u.getDate("dd/MM/yyyy"))) {
                                    sendMsgToUser((long) i, txt);
                                    n++;
                                }
                            }
                            all++;
                        }
                        System.out.println(prefix() + "Handled people: " + all + " Birthded people: " + n);
                        this.cancel();
                    }
                }, (1000 * 5));
                return;
            }
            if (!fname.equals("FILE_NOT_ADDED")) {
                java.io.File f = new java.io.File(fname);
                String[] ffff = fname.split(".");
                String ftype = "." + getFileExtension(f);
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
                keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Ответить").setUrl("t.me/koi_service"));
                List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
                rowList.add(keyboardButtonsRow1);
                inlineKeyboardMarkup.setKeyboard(rowList);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int n = 0, all = 0;
                        for (long i : DataBase.getAllUserId()) {
                            try {
                                String dr_date = DataBase.getUsFileds(i, "dr");
                                if (dr_date == "Не указано") {
                                    all++;
                                    continue;
                                }
                                if (dr_date.equals(bot.u.getDate("dd/MM/yyyy"))) {
                                    if (!txt.equals("TEXT_NOT_ADDED"))
                                        sendMsgToUser(i, txt);
                                    if (ftype.equals(".jpeg") || ftype.equals(".png")) {
                                        SendPhoto ss = new SendPhoto().setNewPhoto(f).setChatId(i);
                                        bot.sendPhoto(ss);
                                        sendMsgToUser(i, txt);
                                        System.out.println(prefix() + "Успешно отправлена фотография и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
                                    } else if (ftype.contains(".doc") || ftype.equals(".pdf")
                                            || ftype.equals(".txt") || ftype.equals(".xls") || ftype.equals(".ppt") || ftype.contains(".docx") || ftype.contains(".xlsx") || ftype.contains(".pptx")) {
                                        SendDocument ss = new SendDocument().setNewDocument(f).setChatId(i);
                                        bot.sendDocument(ss);
                                        sendMsgToUser(i, txt);
                                        System.out.println(prefix() + "Успешно отправлен документ и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
                                    } else if (ftype.contains(".mp4") || ftype.equals(".gif")) {
                                        SendVideo ss = new SendVideo().setNewVideo(f).setChatId(i);
                                        bot.sendVideo(ss);
                                        sendMsgToUser(i, txt);
                                        System.out.println(prefix() + "Успешно отправлен видос и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
                                    } else if (ftype.contains(".mp3") || ftype.equals(".ogg") || ftype.equals(".mp2")) {
                                        SendAudio ss = new SendAudio().setNewAudio(f).setChatId(i);
                                        bot.sendAudio(ss);
                                        sendMsgToUser(i, txt);
                                        System.out.println(prefix() + "Успешно отправлен голос и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
                                    }
                                    n++;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        this.cancel();
                    }
                }, (1000 * 5));
            }
        } else {
            System.out.println(prefix() + "DR stopped. Table stletters column status (STOP) Insert START to start!");
        }
    }

    public static void startAllNewsLetter(int nv) {
        String fname = DataBase.getNL(nv, "file_name");
        String tex = DataBase.getNL(nv, "text");
        java.io.File f = new java.io.File(fname);
        String[] ffff = fname.split(".");
        String ftype = "." + getFileExtension(f);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (long i : DataBase.getAllUserId()) {
                    try {
                        if (ftype.equals(".jpeg") || ftype.equals(".png")) {
                            SendPhoto ss = new SendPhoto().setNewPhoto(f).setChatId((long) i);
                            bot.sendPhoto(ss);
                            sendMsgToUser((long) i, tex);
                            System.out.println(prefix() + "Успешно отправлена фотография и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
                        } else if (ftype.contains(".doc") || ftype.equals(".pdf")
                                || ftype.equals(".txt") || ftype.equals(".xls") || ftype.equals(".ppt") || ftype.contains(".docx") || ftype.contains(".xlsx") || ftype.contains(".pptx")) {
                            SendDocument ss = new SendDocument().setNewDocument(f).setChatId((long) i);
                            bot.sendDocument(ss);
                            sendMsgToUser((long) i, tex);
                            System.out.println(prefix() + "Успешно отправлен документ и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
                        } else if (ftype.contains(".mp4") || ftype.equals(".gif")) {
                            SendVideo ss = new SendVideo().setNewVideo(f).setChatId((long) i);
                            bot.sendVideo(ss);
                            sendMsgToUser((long) i, tex);
                            System.out.println(prefix() + "Успешно отправлен видос и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
                        } else if (ftype.contains(".mp3") || ftype.equals(".ogg") || ftype.equals(".mp2")) {
                            SendAudio ss = new SendAudio().setNewAudio(f).setChatId((long) i);
                            bot.sendAudio(ss);
                            sendMsgToUser((long) i, tex);
                            System.out.println(prefix() + "Успешно отправлен голос и текст пользователю: " + i + "(" + DataBase.getUserName(Math.toIntExact(i)) + ")");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.cancel();
            }
        }, (1000 * 5));
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

    public static void startDays() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (long i : DataBase.getAllUserId()) {
                    int d = 0;
                    try {
                        if (DataBase.getUsFileds(i, "days") != null)
                            d = pi(DataBase.getUsFileds(i, "days"));
                        else continue;
                    } catch (Exception e) {
                        System.out.println("Param days not addded for user " + i);
                        d = 0;
                    }
                    if (d != 0) {
                        int last_z = 0;
                        try {
                            last_z = pi(DataBase.getUsFileds(i, "last_z_id"));
                        } catch (Exception e) {
                            System.out.println("Param days not added for user " + i);
                            last_z = 0;
                        }
                        if (last_z != 0) {
                            try {
                                Date ldate = new SimpleDateFormat("dd/MM/yyyy").parse(new Order(last_z).getDate());
                                Date tdate = new Date();
                                int days = bot.daysBetween(ldate, tdate);
                                if (days == d) {
                                    bot.execute(new SendMessage().setText("Добрый день, хотел узнать всё ли в порядке? Возможно Вам уже нужно заправить картридж?")
                                            .setChatId((long) i).setReplyMarkup(bot.getNapomDaysButton()));
                                    DataBase.setUsFields(i, "days_send_date", bot.u.getDate("dd_MM_yyyy"));
                                    return;
                                }
                            } catch (Exception e) {
                                System.out.println("Ошибка при парсе даты");
                            }
                        }
                    }
                }
                for (int a : DataBase.getAllViberUserId()) {
                    Long i = (long) a;
                    int d = 0;
                    try {
                        d = pi(DataBase.getUsFileds(i, "days"));
                    } catch (Exception e) {
                        System.out.println("Param days not addded for user " + i);
                        d = 0;
                    }
                    if (d != 0) {
                        int last_z;
                        try {
                            last_z = pi(DataBase.getUsFileds(i, "last_z_id"));
                        } catch (Exception e) {
                            System.out.println("Param days not added for user " + i);
                            last_z = 0;
                        }
                        if (last_z != 0) {
                            try {
                                Order order = new Order(last_z);
                                Date ldate = new SimpleDateFormat("dd/MM/yyyy").parse(order.getDate());
                                Date tdate = new Date();
                                int days = bot.daysBetween(ldate, tdate);
                                if (days == d) {
                                    URL oracle = new URL("https://ay.dn.ua/ViberBot/src/ChangeStatus.php?dark_id=" + order.getUID() + "&zn=" + last_z + "&status=napom_zapr&key=1337");
                                    BufferedReader in = new BufferedReader(
                                            new InputStreamReader(oracle.openStream()));
                                    String inputLine;
                                    while ((inputLine = in.readLine()) != null)
                                        System.out.println(inputLine);
                                    in.close();
                                    DataBase.setUsFields(i, "days_send_date", bot.u.getDate("dd_MM_yyyy"));
                                    return;
                                }
                            } catch (Exception e) {
                                System.out.println("Ошибка при парсе даты");
                            }
                        }
                    }
                }
            }
        }, 30000, ((60000 * 60) * 24));
    }

    public static void getNamomDaysMsg(int uid) {
        try {
            bot.execute(new SendMessage().setText("Добрый день, хотел узнать всё ли в порядке? Возможно Вам уже нужно заправить картридж?")
                    .setChatId((long) uid).setReplyMarkup(bot.getNapomDaysButton()));
        } catch (Exception e) {
            System.out.println("Error in days handler 522 str");
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
    public void onUpdateReceived(final Update e) {
        if (bot.enabled == true) {
            if (e.hasMessage()) {
                Message msg = e.getMessage();
                Long fromID = msg.getFrom().getId().longValue(), fid = fromID, chatID = msg.getChatId();
                User user = new User(fromID);
                bot.u.logAsyncInfo(user, msg);
                bot.u.changeBotAction(chatID, ActionType.TYPING);
                if (msg.getContact() != null) {
                    if (ucth.handleContact(user, msg.getContact())) return;
                    user.sendMessage("К сожалению я не знаю что делать с этим контактом \uD83D\uDE22");
                    return;
                }
                if (e.getMessage().hasText()) {
                    String txt = msg.getText(), text = txt;
                    Integer message_id = msg.getMessageId();
                    sendToLogChanel("Пользователь " + msg.getFrom().getFirstName() + " написал текстовое сообщение. Текст: " + txt);
                    if (umh.checkIsRegister(user, text)) return;
                    if (text.startsWith("/")) {
                        if (umh.handleStart(txt, msg, fromID)) return;
                        user.sendMessage("К сожалению я не знаю что делать с данной командой \uD83D\uDE22");
                        return;
                    } else {
                        if (DataBase.isPersonal(user.getUID())) {
                            String vacantion = DataBase.getPerFields(user.getUID(), "v_id");
                            if (vacantion.equals("admin") || vacantion.equals("owner")) {
                                if (ah.handleTextMessage(user, txt, message_id)) return;
                            }
                            if (vacantion.equals("manager") || vacantion.equals("admin") || vacantion.equals("owner")) {
                                if (mh.handleTextMessage(user, txt, message_id)) return;
                            }
                            if (vacantion.equals("courier")) {
                                //if (ch.handleTextMessage(user, txt, message_id)) return;
                            }
                        }
                        switch (text) {
                            case "Оставить заявку на заправку":
                                if (!DataBase.isSetMainInfo(user.getUID())) {
                                    user.sendMessage("Для подачи заявки сначала укажите данные о себе!");
                                    return;
                                }
                                DataBase.setUsFields(user.getUID(), "lastReadedCartridge", 0);
                                if (user.getCartridgesID().isEmpty()) {
                                    if (umh.handleUserNewOrder(user, true)) return;
                                } else {
                                    user_tema.put(user.getUID(), "Заправка картриджа");
                                    user.sendMessage(user.getUserName() + ", выберите как бы Вы хотели оформить" + txt.replaceAll("Оставить", "") + ".Если Вы выберите вариант 'Используя QR-код', Вам будет нужно сфотографировать QR-код на Вашем картридже и отправить мне, и я автоматически сформирую заявку по этому картриджу.", "user_qr_zayav");
                                }
                                return;
                            case "Оставить заявку на ремонт":
                                if (!DataBase.isSetMainInfo(user.getUID())) {
                                    user.sendMessage("Для подачи заявки сначала укажите данные о себе!");
                                    return;
                                }
                                DataBase.setUsFields(user.getUID(), "lastReadedCartridge", 0);
                                if (user.getCartridgesID().isEmpty()) {
                                    if (umh.handleUserNewOrder(user, false)) return;
                                } else {
                                    user_tema.put(user.getUID(), "Ремонт принтера");
                                    user.sendMessage(user.getUserName() + ", выберите как бы Вы хотели оформить" + txt.replaceAll("Оставить", "") + ".Если Вы выберите вариант 'QR', Вам будет нужно сфотографировать QR-код на Вашем картридже и отправить мне, и я автоматически сформирую заявку по этому картриджу/принтеру.", "user_qr_zayav");
                                }
                                return;
                            case "Адрес/Контакты":
                                contacts(msg);
                                return;
                            case "Мои заявки":
                                sendMsgToUser(user.getTID(), "Вы перешли в меню заявок.", "my_zayavki");
                                return;
                            case "Текущие заявки":
                                if (umh.handleCurrentOrdersHystory(user.getID())) return;
                            case "Завершённые заявки":
                                if (umh.handleEndOrdersHystory(user.getID())) return;
                            case "Написать менеджеру":
                                sendMsg(msg, "Нажмите на кнопку ниже чтобы перейти в чат с нашим менеджером", "mened");
                                return;
                            case "Главное меню":
                            case "Вернуться в главное меню":
                                sendMsg(msg, "Вы перешли в главное меню", "main");
                                if (DataBase.isPersonal(user.getUID()))
                                    for (Long messageID : mh.getAllSendedMsgsID(user.getUID()))
                                        try {
                                            bot.deleteMsg(user.getTID(), messageID.intValue());
                                        } catch (Exception ignored) {
                                        }
                                user_wait_adress.remove(chatID);
                                user_wait_model.remove(chatID);
                                return;
                            case "Прайс лист":
                                sendMsgToUser(user.getTID(), "Пожалуйста выберите производителя Вашего принтера ниже:", "print_models");
                                return;
                            case "Подать заявку":
                                bot.sendMsgToUser(user.getTID(), "Данные заявки успешно заполнены!", "backtomain");
                                user_wait_adress.remove(chatID);
                                user_wait_model.remove(chatID);
                                bot.sendMsg(msg, "Проверьте правильность ввода: "
                                        + "\nВаш адрес: " + DataBase.getUsFileds(user.getUID(), "adress")
                                        + "\nВаша модель: " + bot.u.stringToString(bot.getSelectedOrderModels(user.getUID()), ";"), "prov_info");
                                return;
                            case "Используя QR-код":
                                user.setUserAction("user_wait_qr");
                                sendMsgToUser(user.getTID(), user.getUserName() + ", сфотографируйте QR-код на вашем картридже/принтере и отправьте мне, либо отправьте мне цифры написанные под QR кодом и я автоматически сформирую заявку :-) ", "backtomain");
                                return;
                            case "Заполняя форму":
                                if (user_tema.containsKey(user.getUID())) {
                                    if (user_tema.get(user.getUID()).equals("Заправка картриджа")) {
                                        if (!handlOstUsZaprZayav(msg, user))
                                            sendMsgToUser(user.getTID(), user.getUserName() + ", заявка не может быть подана. Повторите попытку пожалуйста.", "main");
                                    } else if (!handlOstUsRemZayav(msg, user))
                                        sendMsgToUser(user.getTID(), user.getUserName() + ", заявка не может быть подана. Повторите попытку пожалуйста.", "main");
                                    return;
                                }
                                sendMsgToUser(user.getTID(), user.getUserName() + ", заявка не может быть подана. Повторите попытку пожалуйста.", "main");
                                return;
                        }
                        if (umh.handleUserReklamComm(user, txt)) return;
                        if (umh.handleUserName(user, txt, msg)) return;
                        if (umh.handleUserCompanyName(user, txt, msg)) return;
                        if (umh.handlePersonalMsgs(user, txt, msg)) return;
                        if (umh.handleUserRateComm(user, txt, msg)) return;
                        if (handZayav(user, msg, txt)) return;
                        if (umh.handleUserAdress(user, txt, msg)) return;
                        if (umh.handleUserSeeModel(user, txt)) return;
                    }
                }
                if (msg.hasDocument()) {
                    if (msg.getDocument().getMimeType().contains("image")) {
                        try {
                            bot.saveDocument("lastqr" + fromID + ".png", bot.getFile(new GetFile().setFileId(msg.getDocument().getFileId())).getFileUrl(bot.getBotToken()));
                        } catch (TelegramApiException telegramApiException) {
                            telegramApiException.printStackTrace();
                        }
                        if (uphh.handleUserPhoto(user, new java.io.File("lastqr" + fromID + ".png"))) return;
                    }
                }
                if (msg.hasPhoto()) {
                    try {
                        bot.saveDocument("lastqr" + fromID + ".png", bot.getFile(new GetFile().setFileId(msg.getPhoto().get(1).getFileId())).getFileUrl(bot.getBotToken()));
                    } catch (TelegramApiException telegramApiException) {
                        telegramApiException.printStackTrace();
                    }
                    if (uphh.handleUserPhoto(user, new java.io.File("lastqr" + fromID + ".png"))) return;
                }
                sendMsg(msg, "Вы хотите нам что-то сообщить или узнать? Напишите нашему менеджеру ", "mened");
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
                    if (DataBase.isPersonal(user.getUID())) {
                        String vacantion = DataBase.getPerFields(user.getUID(), "v_id");
                        if (vacantion.equals("admin") || vacantion.equals("owner")) {
                            if (ah.handleCallBack(user, id, data, msgid, cbqid))
                                return;
                        }
                        if (vacantion.equals("manager") || vacantion.equals("admin") || vacantion.equals("owner")) {
                            if (mh.handleCallback(user, data, msgid, cbqid)) return;
                        }
                        if (vacantion.equals("manager") || vacantion.equals("admin") || vacantion.equals("owner") || vacantion.equals("courier")) {
                            if (CourierMessageHandler.handleCallback(user, data, msgid, cbqid)) return;
                        }
                        if (data.startsWith("#Master")) {
                            if (mch.handleStartWorkButtonClick(user, data, msgid, cbqid))
                                return;
                            if (mch.handleRemontButtonClick(user, data, msgid, cbqid))
                                return;
                        }
                        if (pch.handleCallback(user, data, msgid, cbqid, text)) return;
                    }
                    if (uch.handleUserType(user, data, msgid, cbqid)) return;
                    if (uch.handleRate(user, data, msgid, cbqid)) return;
                    if (uch.handleSaveZayav(user, data, msgid, cbqid)) return;
                    if (uch.handleReklamaciya(user, data, msgid, cbqid)) return;
                    if (uch.handleCancelZayavka(user, data, msgid, cbqid)) return;

                    if (data.contains("#PrintModel")) {
                        String przv = data.split("/")[1];
                        prnt_model.put((long) e.getCallbackQuery().getFrom().getId(), przv);
                        editMsg((long) e.getCallbackQuery().getFrom().getId(), e.getCallbackQuery().getMessage().getMessageId(), "Вы выбрали производителя " + przv + "!");
                        sendMsg(e.getCallbackQuery().getFrom().getId(), "Выберите по какому параметру производить поиск:", "print_search_type");
                        return;
                    }

                    if (data.contains("#SearchType")) {
                        String przv = data.split("/")[1];
                        user_wait_semodel.put((long) e.getCallbackQuery().getFrom().getId(), true);
                        user_stype.put((long) e.getCallbackQuery().getFrom().getId(), przv);
                        editMsg((long) e.getCallbackQuery().getFrom().getId(), e.getCallbackQuery().getMessage().getMessageId(), "Вы выбрали поиск по модели " + (przv.equals("Print") ? "принтера" : "картриджа") + "!");
                        if (przv.equals("Print")) {
                            sendMsg(e.getCallbackQuery().getFrom().getId(), "Укажите цифры модели принтера. \nНапример: MF3010 либо просто 3010", "back_to_main");
                        } else {
                            sendMsg(e.getCallbackQuery().getFrom().getId(), "Укажите цифры модели картриджа. \nНапример: CА4554 либо просто 4554", "back_to_main");
                        }
                        return;
                    }
                    if (data.contains("#ZaprPrinter")) {
                        editMsg((long) e.getCallbackQuery().getFrom().getId(), e.getCallbackQuery().getMessage().getMessageId(), "Вы начали оставлять заявку на заправку " + data.split("/")[1] + " " + data.split("/")[2]);
                        user_is_ost_price.put((long) e.getCallbackQuery().getFrom().getId(), true);
                        user_is_ost_model.put((long) e.getCallbackQuery().getFrom().getId(), data.split("/")[1] + " " + data.split("/")[2]);
                        handlOstUsZaprZayav(null, user);
                        return;
                    }
                    if (data.contains("#SOGLASOVANO")) {
                        handVosst(data.split("=")[1], "Telegram", "SOGLASOVANO");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getTTTButton());
                        return;
                    }
                    if (data.contains("#ByNewKart")) {
                        handVosst(data.split("=")[1], "Telegram", "ByNewCart");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getTTTTButton());
                        return;
                    }
                    if (data.contains("#MENED")) {
                        handVosst(data.split("=")[1], "Telegram", "MENED");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getTTTTButton());
                        return;
                    }
                    if (data.contains("#OBZVON-Z=")) {
                        int nz = pi(data.split("=")[1]);
                        bot.editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getCellTrueButton(e.getCallbackQuery().getFrom().getId()));
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Обзвон завершён/Заявка будет перемещена в архив"));
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendToArch(nz);
                                deleteMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId());
                                this.cancel();
                            }
                        }, 60000 * 5);
                        return;
                    }
                    if (data.equals("#OST_Z_ZAPR")) {
                        handlOstUsZaprZayav(e.getCallbackQuery().getMessage(), user);
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Заполните заявку..."));
                        return;
                    }
                    if (data.equals("#VSE_V_PORYDKE")) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendMsg(getCellChannelID(), "#date" + bot.u.getDate("dd_MM_yyyy") + " #Напоминание_по_частоте"
                                        + "\nБыло выслано клиенту #date" + DataBase.getUsFileds((long) fromid, "days_send_date")
                                        + "\nКлиент: #" + DataBase.getUsFileds((long) fromid, "company_name").replaceAll(" ", "_")
                                        + "\nКонтактное лицо: #" + DataBase.getUsFileds((long) fromid, "name")
                                        + "\nТелефон: #tel" + DataBase.getUserPhone(fromid)
                                        + "\nАдрес: #" + DataBase.getUsFileds((long) fromid, "adress").replaceAll(" ", "_")
                                        + "\nМодель: #" + DataBase.getUsFileds((long) fromid, "last_model").replaceAll(" ", "_")
                                        + "\n---------------------------\n" + (long) fromid, getNPBt());
                                this.cancel();
                            }
                        }, 60000 * 1/*((60000*60)*24)*2*/);
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Спасибо!"));
                    }
                    if (data.equals("#NAPOM_TWO_DAY")) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    bot.execute(new SendMessage().setText("Добрый день, хотел узнать всё ли в порядке? Возможно Вам уже нужно заправить картридж?")
                                            .setChatId(String.valueOf(e.getCallbackQuery().getFrom().getId())).setReplyMarkup(bot.getNapomDaysTwoButton()));
                                } catch (Exception e111) {
                                    System.out.println("Ошибка при отправке напом.");
                                }
                                this.cancel();
                            }
                        }, 60000 * 1/*((60000*60)*24)*2*/);
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Спасибо :-)"));
                        return;
                    }
                    if (data.equals("#ReSearchPrice")) {
                        sendMsg(Math.toIntExact(id), "Пожалуйста выберите модель Вашего принтера ниже:", "print_models");
                        return;
                    }


                    if (data.equals("#CancelVvodAdm")) {
                        deleteMsg(id, msgid);
                        sendMsgToUser(user.getTID(), "Продолжайте :-)");
                        // if (user_tema.get(user.getUID()).equals("Заправка картриджа"))
                        handlOstUsZaprZayav(e.getCallbackQuery().getMessage(), user);
                        /*else
                            handlOstUsRemZayav(e.getCallbackQuery().getMessage(), user);*/
                        return;
                    }
                    if (data.equals("#PerenosNaZavtra")) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                deleteMsg(id, msgid);
                                this.cancel();
                            }
                        }, (60000 * 1));
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                bot.sendMsg(getCellChannelID(), text, getNPBt());
                                this.cancel();
                            }
                        }, (60000 * 2));
                        editMsg(id, msgid, getTr("Выполнено: `Отмена`"));
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Ваш ответ принят!"));
                        return;
                    }
                    if (data.equals("#PerenosNa3Days")) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                deleteMsg(id, msgid);
                                this.cancel();
                            }
                        }, (60000 * 1));
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                bot.sendMsg(getCellChannelID(), text, getNPBt());
                                this.cancel();
                            }
                        }, (60000 * 3));
                        editMsg(id, msgid, getTr("Выполнено: `Отмена`"));
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Ваш ответ принят!"));
                        return;
                    }
                    if (data.equals("#PerenosNaPn")) {
                        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                deleteMsg((long) id, msgid);
                                this.cancel();
                            }
                        }, (60000 * 1));
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                bot.sendMsg(getCellChannelID(), text, getNPBt());
                                this.cancel();
                            }
                        }, convertToDateViaSqlDate(nextMonday));
                        editMsg(id, msgid, getTr("Выполнено: `В понедельник`"));
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Ваш ответ принят!"));
                        return;
                    }
                    if (data.equals("#SaveZayav")) {
                        int frid = pi(e.getCallbackQuery().getMessage().getText().split("---------------------------")[1]);
                        saveZayav(user, null, "-_-");
                        return;
                    }
                    if (data.equals("#CancelNP")) {
                        String text1 = e.getCallbackQuery().getMessage().getText();
                        editMsg(id, msgid, getTr("Выполнено: `Отмена`"));
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Ваш ответ принят!"));
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                deleteMsg(id, msgid);
                                this.cancel();
                            }
                        }, (60000 * 5));
                        return;
                    }
                    if (data.equals("#ПодатьЗаявку")) {
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), "Вы подали заявку!");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getButText("Вы подали заявку!"));
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Заявка принята!"));
                        saveZayav(user, null, null);
                        return;
                    }
                    if (data.equals("#ИзменитьАдрес")) {
                        user_wait_model.remove(user.getUID());
                        user_wait_adress.put(user.getUID(), true);
                        user_edit_adress.put(user.getUID(), true);
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), "Вы изменили адрес!");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getButText("Вы изменили адрес"));
                        sendMsg(user.getTID().intValue(), DataBase.getUserName(Math.toIntExact(user.getUID())) + ", укажите пожалуйста адрес в поле ввода и отправьте мне, либо выберите подходящий вариант ниже.Адрес нужен для выезда курьера за заявкой. ", "adress");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Укажите новый адрес :-)"));
                        return;
                    }
                    if (data.equals("#ИзменитьМодель")) {
                        user_wait_adress.remove(user.getUID());
                        user_wait_model.put(user.getUID(), true);
                        user_edit_model.put(user.getUID(), true);
                        new User(id).setUserAction("user_wait_edit_model");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), "Вы изменили модель!");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getButText("Вы изменили модель"));
                        sendZOstZ(user, null);
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Введите новую модель!"));
                        return;
                    }
                    answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Ваш ответ уже принят!"));
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
                } catch (Exception e){

                }
                }
        } catch (Exception ignore) {
        }
        String current_statuses = "ℹ️ Заявка №" + order.getOrderID() + "\n";
        String message = order.getVosstMsgText() + "\n\n" + text;
        order.setVosstMsgText(message);
        for (Long chatID : msgids.keySet()) {
            order.setVosstMsgID(chatID, sendMsgToUser(chatID, current_statuses + message, "Manager/Reconciliation/AllSubOrders/" + order.getOrderID()));
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
        String current_statuses = "ℹ️ Заявка №" + order + "\n";
        String message = order.getVosstMsgText() + "\n" + text;
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
                bot.clearSelectedOrderForReconsile(new User(chatId).getUID());
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
                bot.sendMsg(msg, "Главное меню", "main");
                return true;
            }
            if (txt.equals("Подать заявку"))
                return false;
            //user_wait_model.put(id, false);
            String smodel = txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "");
            //bot.addOrderModel(id, smodel);
            user_model.put(id, smodel);
            bot.addOrderModel(id, smodel);
            bot.addSelectedOrderModel(id, smodel);
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
                /*bot.sendMsgToUser(id, "Данные заявки успешно заполнены!", "backtomain");
                bot.sendMsg(msg, "Проверьте правильность ввода: "
                        + "\nВаш адрес: " + DataBase.getUsFileds(id, "adress")
                        + "\nВаша модель: " + model, "prov_info");*/
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
                /*bot.sendMsgToUser(id, "Данные заявки успешно заполнены!", "backtomain");
                String[] sp = DataBase.getUsFileds(id, "last_model").split("&&&");
                bot.sendMsg(msg, "Проверьте правильность ввода: "
                        + "\nВаш адрес: " + DataBase.getUsFileds(id, "adress")
                        + "\nВаша модель: " + user_model.get(id), "prov_info");*/
                bot.sendMsgToUser(user.getTID(), "Модель '" + smodel + "' добавлена в заявку! Если Вы хотите добавить ещё выберите модель снизу или напишите её название мне.", "model");
            } else {
                if (DataBase.getUsFileds(id, "last_model_rem") != null && !DataBase.getUsFileds(id, "last_model_rem").equals("Не указано")) {
                    if (!DataBase.getUsFileds(id, "last_model_rem").split("&&&")[0].equals(txt))
                        DataBase.setUsFields(id, "last_model_rem", txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "") + "&&&" + DataBase.getUsFileds(id, "last_model_rem").split("&&&")[0]);
                } else if (!DataBase.getUsFileds(id, "last_model_rem").split("&&&")[0].equals(txt)) {
                    DataBase.setUsFields(id, "last_model_rem", txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
                }
               /* String[] sp = DataBase.getUsFileds(id, "last_model_rem").split("&&&");
                bot.sendMsg(msg, "Проверьте правильность ввода: "
                        + "\nВаш адрес: " + DataBase.getUsFileds(id, "adress")
                        + "\nВаша модель/дефект: " + user_model.get(id), "prov_info");*/
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
            bot.setOrderModels(id, default_models);
            return;
        }
        if (user_tema.containsKey(id) && user_tema.get(id).equals("Заправка картриджа")) {
            List<String> last_models = new ArrayList<String>();
            String model_data = DataBase.getUsFileds(id, "last_model");
            if (model_data != null && !model_data.equals("") && !model_data.equals(" "))
                for (String model : model_data.split("&&&"))
                    if (!last_models.contains(model))
                        last_models.add(model);
            bot.setOrderModels(id, last_models);
            return;
        } else {
            List<String> last_models = new ArrayList<String>();
            String model_data = DataBase.getUsFileds(id, "last_model_rem");
            if (model_data != null && !model_data.equals("") && !model_data.equals(" "))
                for (String model : model_data.split("&&&"))
                    if (!last_models.contains(model))
                        last_models.add(model);
            bot.setOrderModels(id, last_models);
            return;
        }
    }

    public static void saveZayav(User user, Message msg, String txt) {
        try {
            Long id = user.getUID();
            if (!user_tema.containsKey(id) || !user_model.containsKey(id)) {
                user.setUserAction("main");
                bot.sendMsg(msg, "Ваша заявка не была отправлена! Повторите попытку либо свяжитесь с менеджером!", "main");
                return;
            }
            int znum = DataBase.getZNum();
            DataBase.saveZayavka(znum, user_tema.get(id), bot.u.stringToString(bot.getSelectedOrderModels(id), ";"), DataBase.getUserName(Math.toIntExact(id)),
                    DataBase.getUserPhone(Math.toIntExact(id)), DataBase.getUserDopPhone(Math.toIntExact(id)),
                    Math.toIntExact(id), "Поступила", bot.u.getDate("dd/MM/yyyy"), bot.u.getTime("HH:mm:ss"));
            Order order = new Order(znum);
            for (String model : bot.getSelectedOrderModels(id)) {
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
            bot.sendToChanel((long) znum, user_tema.get(id) + (user.getUserAction().equals("user_wait_qr") ? " \uD83C\uDD40\uD83C\uDD41" : ""), bot.u.stringToString(bot.getSelectedOrderModels(id), ";"),
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
            bot.remAllSelectedOrderModel(id);
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
                bot.sendMsg(msg, "Ваша заявка не была отправлена! Повторите попытку либо свяжитесь с менеджером!", "");
        }
    }

    public static void handStart(Long id, Message msg) {
        if (!DataBase.getAllUserId().contains(Math.toIntExact(id))) {
            String allus = "";
            if (DataBase.getUFileds(3, "val1") == null)
                allus = "" + id;
            else allus = DataBase.getUFileds(3, "val1") + "," + id;
            DataBase.setUFields(3, "val1", allus);
        }
        if (DataBase.isRegUser(id) == null) {
            bot.user_add_contact(msg);
            DataBase.setUserStr("action", Math.toIntExact(id), "user_wait_phone");
            user_wait_phone.put(msg.getChatId(), true);
            DataBase.setUsFields(id, "adress", "Не указано");
            DataBase.setUsFields(id, "last_model", "Не указано");
            DataBase.setUsFields(id, "last_model_rem", "Не указано");
            DataBase.setUsFields(id, "dr", "Не указано");
            DataBase.setUsFields(id, "city", "Харьков");
            DataBase.setUsFields(id, "privilege", "USER");
            DataBase.setUsFields(id, "reg_date", bot.bot.u.getDate("dd/MM/yyyy"));
            DataBase.setUsFields(id, "reg_time", bot.bot.u.getTime("HH:mm:ss"));
            return;
        } else {
            if (DataBase.isSetMainInfo(Math.toIntExact(id))) {
                bot.sendMsg(msg, "Здравствйте " + DataBase.getUsFileds(id, "name") + ", мы с Вами уже знакомы :-)\nЯ готов выполнить Ваш запрос.", "main");
                return;
            } else {
                bot.info("MAIN INFO - NOT SET 3");
                bot.sendMsg(msg, "Вы ещё не завершили регистрацию.", "");
                if (DataBase.getUserStr("action", id).equals("user_wait_name")) {
                    DataBase.setUserStr("action", Math.toIntExact(id), "user_wait_name");
                    bot.sendMsg(msg, "Введите Ваше имя пожалуйста!", "");
                    return;
                }
                if (msg.getText().equals("Написать менеджеру")) {
                    bot.sendMsg(msg, "Нажмите на кнопку ниже чтобы перейти в чат с нашим менеджером", "mened");
                    return;
                }

                if (msg.getText().equals("Адрес/Контакты")) {
                    bot.contacts(msg);
                    return;
                }
                bot.user_add_contact(msg);
                DataBase.setUserStr("action", Math.toIntExact(id), "user_wait_phone");
                user_wait_phone.put(msg.getChatId(), true);
                DataBase.setUsFields(id, "adress", "Не указано");
                DataBase.setUsFields(id, "last_model", "Не указано");
                DataBase.setUsFields(id, "last_model_rem", "Не указано");
                DataBase.setUsFields(id, "dr", "Не указано");
                DataBase.setUsFields(id, "city", "Харьков");
                DataBase.setUsFields(id, "privilege", "USER");
                DataBase.setUsFields(id, "reg_date", bot.bot.u.getDate("dd/MM/yyyy"));
                DataBase.setUsFields(id, "reg_time", bot.bot.u.getTime("HH:mm:ss"));
            }
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
                bot.sendMsg(msg, "Укажите модель картриджа(ей)/принтера(ов) и количество штук.", "model");
            else
                bot.sendMsgToUser(user.getTID(), "Укажите модель картриджа(ей)/принтера(ов) и количество штук.", "model");
        } else if (msg != null)
            bot.sendMsg(msg, "Укажите модель принтера и кратко опишите дефект/поломку если она присутсвует.", "model");
        else
            bot.sendMsgToUser(user.getTID(), "Укажите модель принтера и кратко опишите дефект/поломку если она присутсвует.", "model");
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

    @SuppressWarnings("deprecation")
    public void sendMsg(Message msg, String text, String menu) {
        SendMessage s = new SendMessage().setChatId(msg.getChatId()).setText(text);
        long chatid = Math.toIntExact(msg.getChatId());
        handleMenu(menu, s, chatid);
        try {
            sendMessage(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
            return getObrobotkaButton(order.getOrderID());
        return getObrobotkaTrueButton(order.getOrderID().intValue());
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
        user_id = new User(user_id).getUID();
        if (menu.startsWith("ADMIN")) {
            String asubmenu = menu.split("/")[1];
            String vacantion = DataBase.getPerFields(user_id, "v_id");
            if (vacantion == null || (!vacantion.equals("admin") && !vacantion.equals("owner")))
                return;
            if (asubmenu.startsWith("Corporations")) {
                String subcorpmenu = menu.split("/")[2];
                if (subcorpmenu.equals("MainMenu")) {
                    addAdminCorporationsMenu(s);
                    return;
                }
                if (subcorpmenu.equals("Settings")) {
                    s.setReplyMarkup(getCorporationButtons(getAllCorporationsID(), "Settings"));
                    return;
                }
                if (subcorpmenu.equals("SettingsMenu")) {
                    addAdminCorporationsSettingsMenu(s);
                    return;
                }
                if (subcorpmenu.equals("Delete")) {
                    s.setReplyMarkup(getAdminCorporationsDeleteMenu());
                    return;
                }
                if (subcorpmenu.equals("DeleteEmployee")) {
                    s.setReplyMarkup(getCorporationEmployeesListButtons(ah.getSelectedCorporation().get(user_id), subcorpmenu));
                    return;
                }
                if (subcorpmenu.equals("DeleteAdress")) {
                    s.setReplyMarkup(getCorporationAdressListButtons(ah.getSelectedCorporation().get(user_id), "DA"));
                    return;
                }
            }
            if (asubmenu.startsWith("Personal")) {
                String subpersmenu = menu.split("/")[2];
                if (subpersmenu.equals("MainMenu")) {
                    addPersonalControlMenu(s);
                    return;
                }
                if (subpersmenu.equals("DeleteEmployee")) {
                    s.setReplyMarkup(getPersonalDeleteButtons(DataBase.getAllPersonalID()));
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
                    addAdminQRMenu(s);
                    return;
                }
            }
            if (asubmenu.equals("MainMenu")) {
                addMainAdminMenu(s);
                return;
            }
            if (asubmenu.equals("BackToMainMenu")) {
                addAdminBackToMainButton(s);
                return;
            }
        } else {
            if (menu.startsWith("Manager")) {
                String msubmenu = menu.split("/")[1];
                String vacantion = DataBase.getPerFields(user_id, "v_id");
                if (vacantion == null || (!vacantion.equals("manager") && (!vacantion.equals("admin")) && (!vacantion.equals("owner"))))
                    return;
                if (msubmenu.equals("MainMenu")) {
                    addMainManagerMenu(s);
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
                    s.setReplyMarkup(getCurrentZButtons());
                    return;
                }
                if (msubmenu.startsWith("ZMenu")) {
                    addMainManagerZMenu(s);
                    return;
                }
                if (msubmenu.startsWith("Routes")) {
                    String routessubmenu = menu.split("/")[2];
                    if (routessubmenu.equals("Main")) {
                        addManagerRoutesMenu(s);
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
                s.setReplyMarkup(getSoglasButton(menu.split("=")[1]));
            if (menu.equals("main")) {
                addMainMenu(s);
                if (DataBase.isPersonal(user_id)) {
                    String vc = DataBase.getPerFields(user_id, "v_id");
                    if (vc.equals("admin") || vc.equals("owner"))
                        addMainPersonalMenu(s);
                    if (vc.equals("manager"))
                        addMainUserManagerMenu(s);
                }
            }
            if (menu.equals("MainPersonalMenu") || menu.equals("MainAdminMenu")) {
                if (DataBase.isPersonal(user_id)) {
                    String vc = DataBase.getPerFields(user_id, "v_id");
                    if (vc.equals("manager") || vc.equals("admin") || vc.equals("owner"))
                        addMainPersonalMenu(s);
                }
            }
            if (menu.equals("MainManagerMenu")) {
                if (DataBase.isPersonal(user_id)) {
                    String vc = DataBase.getPerFields(user_id, "v_id");
                    if (vc.equals("manager"))
                        addMainManagerMenu(s);
                }
            }
            if (menu.equals("user_qr_zayav")) {
                addQRZayavMenu(s);
                return;
            }
            if (menu.equals("type")) {
                s.setReplyMarkup(getTypeButtons());
                //  addTypeMenu(s); //Тут менюшка делаетс
            }
            if (menu.equals("men_adr")) {
                addMenAdrMenu(s);
            }
            if (menu.equals("send_contact")) {
                addNoContactMenu(s);
                // s.setReplyMarkup(getSendContactButton());
            }
            if (menu.equals("mened")) {
                s.setReplyMarkup(getMenedButton());
            }
            if (menu.equals("ras")) {
                s.setReplyMarkup(getAMenuButtons());
            }
            if (menu.equals("prov_info")) {
                s.setReplyMarkup(getProvInfoButtons());
            }
            if (menu.equals("amenu")) {
                addMainAdminMenu(s);
            }
            if (menu.equals("model")) {
                //addModelMenu(s, user_id);
                getSelectedOrderModels(s);
            }
            if (menu.equals("adress")) {
                addAdressMenu(s);
            }
            if (menu.equals("cancelvvod")) {
                s.setReplyMarkup(getCancelVvodAdm());
            }
            if (menu.contains("send_napom")) {
                s.setReplyMarkup(getNapomButtons(pi(menu.split("=")[1]), menu.split("=")[2]));
            }
            if (menu.contains("vosst")) {
                s.setReplyMarkup(getMButton(menu.split("=")[1]));
            }
            if (menu.equals("contact")) {
                s.setReplyMarkup(getGoogleMapButton());
                s.enableWebPagePreview();
            }
            if (menu.equals("backtomain")) {
                addBackToMain(s);
            }
            if (menu.equals("my_zayavki")) {
                addMyZayavMenu(s);
            }
            if (menu.startsWith("reklamaciya")) {
                Integer nz = Integer.parseInt(menu.split("/")[1]);
                String status = menu.split("/")[2];
                s.setReplyMarkup(getReklamaciaButtons(nz, user_id, status));
            }
            if (menu.startsWith("reklamaciya")) {
                Integer nz = Integer.parseInt(menu.split("/")[1]);
                String status = menu.split("/")[2];
                s.setReplyMarkup(getReklamaciaButtons(nz, user_id, status));
            }
        }
    }

    public void handVosst(String subOrdersID, String source, String type) {
        try {
            Order order = new Order(new SubOrder(subOrdersID.split("/")[0]).getOrderID());
            int uid = order.getUID().intValue();
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
                        subOrder.setStatus("Согласовано");
                    }
                    if (type.equals("MENED")) {
                        work.addDescription("RECOVERY_WAIT");
                        subOrder.setStatus("Уточняется...");
                    }
                    if (type.equals("ByNewCart")) {
                        work.addDescription("RECOVERY_CANCEL");
                        subOrder.setStatus("Покупка нового картриджа");
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
            bot.updateMainOrderMessage(order.getOrderID());
            bot.deleteAllLastVosstMsg(order.getOrderID());
            order.setVosstMsgText("");
            order.clearVosstMsgID();
            // SendMessage s = new SendMessage().setChatId(getZayavChannelID()).setText(text);
            //s.setReplyMarkup(getEndZayavButton(zn));
            /*try {
                int msgid1 = 5;
                msgid1 = sendMessage(s).getMessageId();
                DataBase.setZFields(zn, "main_msg_id", "" + msgid1);
                String ss = order.getString("msg_ids");
                if (ss != null) {
                    ss += "," + getZayavChannelID() + "!" + 6;
                } else {
                    ss = "" + getZayavChannelID() + "!" + msgid1;
                }
                DataBase.setZFields(Math.toIntExact(zn), "msg_ids", ss);
                //editMsg(Long.parseLong(getZayavChannelID()), msgid1, getTTTButton());
            } catch (Exception ignored) {

            }*/
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void handRate(int zn, int rate, long id, int msgid, String source) {
        DataBase.setZFields(zn, "rate", rate + "");
        DataBase.setZFields(zn, "status", "Завершена|Опрошена");
        Order order = new Order(zn);

        if (source.equals("Viber") == false) {
            editMsg(id, msgid, getSpasiboZaRateButton());
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

    public boolean handlOstUsZaprZayav(Message msg, User user) {
        Long id = user.getUID();
        if (DataBase.isPersonal(id) && pers_is_z_saved.containsKey(id) == false) {
            user_tema.put(user.getUID(), "Заправка картриджа");
            sendMsgToUser(user.getTID(), "Введите название компании/контактное лицо/контактный номер телефона и отправьте мне, после чего продолжите подавать заявку.", "cancelvvod");
            admin_data.put(id, DataBase.getUserStr("company_name", Math.toIntExact(id)) + "//" + DataBase.getUserStr("name", Math.toIntExact(id)) + "//" + DataBase.getUserStr("phone", Math.toIntExact(id)));
            pers_is_z_saved.put(id, true);
            return true;
        } else {
            pers_is_z_saved.remove(id);
        }
        if (DataBase.getAllBlackUserId().contains(Math.toIntExact(id))) {
            sendMsg(Math.toIntExact(user.getTID()), "Извините, но Вам запрещено производить какие-либо действия! Для уточнения деталей напишите менеджеру...", "mened");
            return true;
        }
        if (!z_kolvo.containsKey(id)) {
            z_kolvo.put(id, 0);
        }
        if (DataBase.getUsFileds(id, "privilege").equals("VIP") == false &&
                z_kolvo.containsKey(id) &&
                DataBase.getUserType(Math.toIntExact(id)).equals("Частное лицо") ? z_kolvo.get(id) >= 3 :
                DataBase.getUserType(Math.toIntExact(id)).equals("Компания") ? z_kolvo.get(id) >= 10 : false) {
            sendMsg(Math.toIntExact(user.getTID()), "Вы исчерпали лимит заявок на день! Если нужно подать заявку свяжитесь с нашим менеджером!", "mened");
            return true;
        } else {
            user_tema.put(id, "Заправка картриджа");
            if (DataBase.getUserType(Math.toIntExact(id)).equals("Не указано")) {
                bot.user_enter_type(msg);
                return true;
            }
            user_wait_adress.put(id, true);
            sendMsg(Math.toIntExact(user.getTID()), DataBase.getUserName(Math.toIntExact(id)) + ", укажите пожалуйста адрес в поле ввода и отправьте мне, либо выберите подходящий вариант ниже.Адрес нужен для выезда курьера за заявкой. ", "adress");
        }
        return true;
    }

    public boolean handlOstUsRemZayav(Message msg, User user) {
        Long id = user.getUID();

        if (DataBase.isPersonal(id) && pers_is_z_saved.containsKey(id) == false) {
            user_tema.put(user.getUID(), "Ремонт принтера");
            sendMsg(msg, "Введите название компании/контактное лицо/контактный номер телефона и отправьте мне, после чего продолжите подавать заявку.", "т");
            pers_is_z_saved.put(id, true);
            return true;
        } else {
            pers_is_z_saved.remove(id);
        }
        if (DataBase.getAllBlackUserId().contains(Math.toIntExact(id))) {
            sendMsg(msg, "Извините, но Вам запрещено производить какие-либо действия! Для уточнения деталей напишите менеджеру...", "mened");
            return true;
        }
        if (!z_kolvo.containsKey(id)) {
            z_kolvo.put(id, 0);
        }
        if (DataBase.getUsFileds(id, "privilege").equals("VIP") == false &&
                z_kolvo.containsKey(id) &&
                DataBase.getUserType(Math.toIntExact(id)).equals("Частное лицо") ? z_kolvo.get(id) >= 3 :
                DataBase.getUserType(Math.toIntExact(id)).equals("Компания") ? z_kolvo.get(id) >= 10 : false) {
            sendMsg(msg, "Вы исчерпали лимит заявок на день! Если нужно подать заявку свяжитесь с нашим менеджером!", "mened");
            return true;
        } else {
            user_tema.put(id, "Ремонт принтера");
            if (DataBase.getUserType(Math.toIntExact(id)).equals("Не указано")) {
                bot.user_enter_type(msg);
                return true;
            }
            user_wait_adress.put(id, true);
            sendMsg(msg, DataBase.getUserName(Math.toIntExact(id)) + ", укажите пожалуйста адрес в поле ввода и отправьте мне, либо выберите подходящий вариант ниже.Адрес нужендля выезда курьера за заявкой. ", "adress");
            return true;
        }
    }

    @SuppressWarnings("deprecation")
    public void sendMsg(Integer id, String text, String menu) {
        SendMessage s = new SendMessage().setChatId((long) id).setText(text);
        if (menu.contains("yesno"))
            s.setReplyMarkup(getSoglasButton(menu.split("=")[1]));
        if (menu.equals("main")) {
            addMainMenu(s);
            if (DataBase.isPersonal((long) id)) {
                String vc = DataBase.getPerFields((long) id, "v_id");
                if (vc.equals("manager") || vc.equals("admin") || vc.equals("owner"))
                    addMainPersonalMenu(s);
            }
        }
        if (menu.equals("MainPersonalMenu")) {
            if (DataBase.isPersonal((long) id)) {
                String vc = DataBase.getPerFields((long) id, "v_id");
                if (vc.equals("manager") || vc.equals("admin") || vc.equals("owner"))
                    addMainPersonalMenu(s);
            }
        }
        if (menu.equals("MainManagerMenu")) {
            if (DataBase.isPersonal((long) id)) {
                String vc = DataBase.getPerFields((long) id, "v_id");
                if (vc.equals("manager"))
                    addMainManagerMenu(s);
            }
        }
        if (menu.equals("MainAdminMenu")) {
            if (DataBase.isPersonal((long) id)) {
                String vc = DataBase.getPerFields((long) id, "v_id");
                if (vc.equals("admin") || vc.equals("owner"))
                    addMainAdminMenu(s);
            }
        }
        if (menu.equals("ICZA")) {
            s.setReplyMarkup(getICZAButtons());
        }
        if (menu.equals("type")) {
            s.setReplyMarkup(getTypeButtons());
            //  addTypeMenu(s); //Тут менюшка делаетс
        }
        if (menu.equals("men_adr")) {
            addMenAdrMenu(s);
        }
        if (menu.equals("send_contact")) {
            addNoContactMenu(s);
            // s.setReplyMarkup(getSendContactButton());
        }
        if (menu.equals("mened")) {
            s.setReplyMarkup(getMenedButton());
        }
        if (menu.equals("ras")) {
            s.setReplyMarkup(getAMenuButtons());
        }
        if (menu.equals("prov_info")) {
            s.setReplyMarkup(getProvInfoButtons());
        }
        if (menu.equals("amenu")) {
            addMainAdminMenu(s);
        }
        if (menu.equals("model")) {
            addModelMenu(s, (long) id);
        }
        if (menu.equals("adress")) {
            addAdressMenu(s);
        }
        if (menu.contains("send_napom")) {
            s.setReplyMarkup(getNapomButtons(pi(menu.split("=")[1]), menu.split("=")[2]));
        }
        if (menu.contains("vosst")) {
            s.setReplyMarkup(getMButton(menu.split("=")[1]));
        }
        if (menu.equals("contact")) {
            s.setReplyMarkup(getGoogleMapButton());
            s.enableWebPagePreview();
        }
        if (menu.equals("print_models")) {
            s.setReplyMarkup(getPrintModelsButtons());
        }
        if (menu.equals("print_search_type")) {
            s.setReplyMarkup(getPrintTypeButtons());
        }
        if (menu.contains("ost_zmodel")) {
            s.setReplyMarkup(getOstZPrinterForPriceListButtons(menu.split("/")[1]));
        }
        if (menu.contains("research_price")) {
            s.setReplyMarkup(getReSearchPrice());
        }
        if (menu.contains("ost_kart")) {
            s.setReplyMarkup(getOstZPrinterForPriceKListButtons(menu.split("%")[1]));
        }
        if (menu.equals("cancelvvod")) {
            s.setReplyMarkup(getCancelVvodAdm());
        }
        if (menu.startsWith("reklamaciya")) {
            Integer nz = Integer.parseInt(menu.split("/")[1]);
            String status = menu.split("/")[2];
            s.setReplyMarkup(getReklamaciaButtons(nz, (long) id, status));
        }
        if (menu.equals("my_zayavki")) {
            addMyZayavMenu(s);
        }
        try {
            sendMessage(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public List<Long> getAllCorporationsID() {
        List<Long> all_corporations_id = new ArrayList<Long>();
        for (int i = 1; i <= DataBase.getUtilsField((long) 17, "val"); i++)
            if (new Corporation((long) i).getName() != null)
                all_corporations_id.add((long) i);
        return all_corporations_id;
    }

    public void addMainMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Оставить заявку на заправку"));
        oneline.add(new KeyboardButton("Оставить заявку на ремонт"));
        twoline.add(new KeyboardButton("Написать менеджеру"));
        twoline.add(new KeyboardButton("Адрес/Контакты"));
        thrline.add(new KeyboardButton("Мои заявки"));
        thrline.add(new KeyboardButton("Прайс лист"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }


    public void addMyZayavMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Текущие заявки"));
        oneline.add(new KeyboardButton("Завершённые заявки"));
        twoline.add(new KeyboardButton("Главное меню"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addBackToMain(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Главное меню"));
        keyboard.add(oneline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addMenAdrMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow();
        oneline.add(new KeyboardButton("Написать менеджеру"));
        oneline.add(new KeyboardButton("Адрес/Контакты"));
        keyboard.add(oneline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addMainPersonalMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow(), phline = new KeyboardRow();
        oneline.add(new KeyboardButton("Оставить заявку на заправку"));
        oneline.add(new KeyboardButton("Оставить заявку на ремонт"));
        twoline.add(new KeyboardButton("Написать менеджеру"));
        twoline.add(new KeyboardButton("Адрес/Контакты"));
        thrline.add(new KeyboardButton("Мои заявки"));
        thrline.add(new KeyboardButton("Прайс лист"));
        phline.add(new KeyboardButton("Вернуться в меню управления"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        keyboard.add(phline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addMainUserManagerMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow(), phline = new KeyboardRow();
        oneline.add(new KeyboardButton("Оставить заявку на заправку"));
        oneline.add(new KeyboardButton("Оставить заявку на ремонт"));
        twoline.add(new KeyboardButton("Написать менеджеру"));
        twoline.add(new KeyboardButton("Адрес/Контакты"));
        thrline.add(new KeyboardButton("Мои заявки"));
        thrline.add(new KeyboardButton("Прайс лист"));
        phline.add(new KeyboardButton("Перейти в меню менеджера"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        keyboard.add(phline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addMainAdminMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow(), fline = new KeyboardRow();
        oneline.add(new KeyboardButton("Управление рассылками"));
        oneline.add(new KeyboardButton("Управление QR-кодами"));
        twoline.add(new KeyboardButton("Управление корпорациями"));
        twoline.add(new KeyboardButton("Управление персоналом"));
        thrline.add(new KeyboardButton("Перейти в меню пользователя"));
        fline.add(new KeyboardButton("Перейти в меню менеджера"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        keyboard.add(fline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addAdminCorporationsMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Создать корпорацию"));
        oneline.add(new KeyboardButton("Удалить корпорацию"));
        twoline.add(new KeyboardButton("Настройка корпорации"));
        twoline.add(new KeyboardButton("Информация о корпорациях"));
        thrline.add(new KeyboardButton("Вернуться меню администратора"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addAdminQRMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Создать QR-код"));
        oneline.add(new KeyboardButton("Удалить QR-код"));
        twoline.add(new KeyboardButton("Изменить QR-код"));
        twoline.add(new KeyboardButton("Информация о QR-кодах"));
        thrline.add(new KeyboardButton("Вернуться меню администратора"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addPersonalControlMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Добавить персонал"));
        oneline.add(new KeyboardButton("Удалить персонал"));
        twoline.add(new KeyboardButton("Информация о персонале"));
        twoline.add(new KeyboardButton("Настройки персонала"));

        thrline.add(new KeyboardButton("Вернуться меню администратора"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }


    public void getSelectedOrderModels(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow sendOrderRow = new KeyboardRow(), backToMainRow = new KeyboardRow();
        Long chatID = new User(Long.parseLong(sendMessage.getChatId())).getUID();
        List<String> models = bot.getOrderModels(chatID);
        List<String> selectedModels = bot.getSelectedOrderModels(chatID);
        sendOrderRow.add("Подать заявку");
        backToMainRow.add("Вернуться в главное меню");
        KeyboardRow row1 = new KeyboardRow(), row2 = new KeyboardRow(), row3 = new KeyboardRow();
        int added = 0;
        for (String model : models) {
            //System.out.println(model);
            if (model == null || model.equals("Не указано"))
                continue;
            if (added < 3)
                row1.add(new KeyboardButton(model + (selectedModels.contains(model) ? "✅" : "")));
            if (added >= 3 && added < 6)
                row2.add(new KeyboardButton(model + (selectedModels.contains(model) ? "✅" : "")));
            if (added >= 6 && added < 9)
                row3.add(new KeyboardButton(model + (selectedModels.contains(model) ? "✅" : "")));
            added++;
        }
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        if (selectedModels.size() > 0)
            keyboard.add(sendOrderRow);
        keyboard.add(backToMainRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addAdminCorporationsSettingsMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow(), line_4 = new KeyboardRow();
        oneline.add(new KeyboardButton("Добавить сотрудника"));
        oneline.add(new KeyboardButton("Удалить сотрудника"));
        twoline.add(new KeyboardButton("Добавить адрес"));
        twoline.add(new KeyboardButton("Удалить адрес"));
        thrline.add(new KeyboardButton("Изменить имя корпорации"));
        line_4.add(new KeyboardButton("Вернуться меню администратора"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        keyboard.add(line_4);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }


    public void addMainManagerMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Текущие заявки"));
        oneline.add(new KeyboardButton("Создать заявку"));
        twoline.add(new KeyboardButton("Маршруты"));
        thrline.add(new KeyboardButton("Перейти в меню пользователя"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addManagerRoutesMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow(), thline = new KeyboardRow(), th1line = new KeyboardRow(), th2line = new KeyboardRow();
        oneline.add(new KeyboardButton("Все заявки"));
        twoline.add(new KeyboardButton("В пути"));
        twoline.add(new KeyboardButton("В офисе"));
        twoline.add(new KeyboardButton("В работе"));
        thrline.add(new KeyboardButton("Выезд"));
        thrline.add(new KeyboardButton("Сбор"));
        thrline.add(new KeyboardButton("Доставка"));
        for (Long userID : DataBase.getAllPersonalIDWhere("courier")) {
            Courier courier = new Courier(userID);
            User user = courier.getUser();
            thline.add(new KeyboardButton("|" + courier.getCourierID() + "|" + user.getUserName()));
        }
        th1line.add(new KeyboardButton("Чер-ик №1"));
        th1line.add(new KeyboardButton("Чер-ик №2"));
        th1line.add(new KeyboardButton("Чер-ик №3"));
        th2line.add(new KeyboardButton("Вернуться в главное меню"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        keyboard.add(thline);
        keyboard.add(th1line);
        keyboard.add(th2line);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addMainManagerZMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow(), fline = new KeyboardRow();
        oneline.add(new KeyboardButton("Изменить модель"));
        oneline.add(new KeyboardButton("Изменить адрес"));
        twoline.add(new KeyboardButton("Согласиться на восстановление"));
        thrline.add(new KeyboardButton("Отказ от восстановления"));
        fline.add(new KeyboardButton("Вернуться в меню менеджера"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        keyboard.add(fline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addNoContactMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow();
        oneline.add(new KeyboardButton("Отправить свой контакт").setRequestContact(true));
        twoline.add(new KeyboardButton("Написать менеджеру"));
        twoline.add(new KeyboardButton("Адрес/Контакты"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addAdminBackToMainButton(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow();
        oneline.add(new KeyboardButton("Вернуться в меню управления"));
        keyboard.add(oneline);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addTypeMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow one = new KeyboardRow(), two = new KeyboardRow();
        one.add(new KeyboardButton("Я представитель компании"));
        one.add(new KeyboardButton("Я частное лицо"));
        two.add(new KeyboardButton("Я уже заказывал у вас"));
        keyboard.add(one);
        keyboard.add(two);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addQRZayavMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow one = new KeyboardRow(), two = new KeyboardRow();
        one.add(new KeyboardButton("Заполняя форму"));
        one.add(new KeyboardButton("Используя QR-код"));
        two.add(new KeyboardButton("Главное меню"));
        keyboard.add(one);
        keyboard.add(two);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addAdressMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        User user = new User(Long.parseLong(sendMessage.getChatId()));
        Long id = user.getUID();

        KeyboardRow one = new KeyboardRow(), two = new KeyboardRow(), thr = new KeyboardRow(), thx = new KeyboardRow();
        String city = user.getUserCity();
        String adress = user.getUserAdres(),
                lastadress = user.getUserLastAdres();

        if (DataBase.isCorporationWorker(id)) {
            Corporation corp = new Corporation(DataBase.getCorporationID(id));
            if (corp.getAddresses().size() == 1) {
                DataBase.setUsFields(id, "adress", corp.getAddresses().get(0));
                user_wait_adress.remove(id);
                updateOrderModels(id);
                user_wait_model.put(id, true);
                sendMsgToUser(id, "Автоматически установлен адрес " + corp.getAddresses().get(0), "model");
                sendMessage.setText("Укажите пожалуйста модель");
                new User(id).setUserAction("user_wait_model");
                return;
            } else if (corp.getAddresses().size() >= 2) {
                Integer k = 1;
                for (String cadr : corp.getAddresses()) {
                    if (k <= 3)
                        one.add(new KeyboardButton(cadr));
                    if (k > 4 && k <= 6)
                        two.add(new KeyboardButton(cadr));
                    if (k >= 7 && k <= 9)
                        thr.add(new KeyboardButton(cadr));
                }
                if (k <= 3) {
                    two.add(new KeyboardButton("Главное меню"));
                }
                if (k > 4 && k <= 6)
                    thr.add(new KeyboardButton("Главное меню"));
                if (k >= 7 && k <= 9)
                    thx.add(new KeyboardButton("Главное меню"));
                keyboard.add(one);
                keyboard.add(two);
                keyboard.add(thr);
                keyboard.add(thx);
                replyKeyboardMarkup.setKeyboard(keyboard);
                return;
            }

        }
        if (adress.startsWith(" "))
            adress = adress.substring(1);
        if (lastadress != null && lastadress.startsWith(" "))
            lastadress = lastadress.substring(1);
        if (adress.equals("Не указано")) {
            one.add(new KeyboardButton("Я приеду сам"));
        } else {
            if (adress.equals("Я приеду сам")) {
                if (lastadress == null || lastadress.equals("Не указано")) {
                    two.add(new KeyboardButton("Я приеду сам"));
                } else {
                    if (lastadress != null && !lastadress.equals("Не указано") && !lastadress.equals(adress)) {
                        one.add(new KeyboardButton(lastadress));
                    }
                }
            } else {
                one.add(new KeyboardButton(adress.contains(city) ? adress : city + ", " + adress));
                if (lastadress != null && !lastadress.equals("Не указано") && !lastadress.equals(adress)) {
                    one.add(new KeyboardButton(lastadress.contains(city) ? lastadress : city + ", " + lastadress));
                }
                two.add(new KeyboardButton("Я приеду сам"));
            }
        }
        thr.add(new KeyboardButton("Главное меню"));
        keyboard.add(one);
        keyboard.add(two);
        keyboard.add(thr);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }


    public void addModelMenu(SendMessage sendMessage, Long id) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow one = new KeyboardRow(), two = new KeyboardRow(), thr = new KeyboardRow(), fh = new KeyboardRow();
        String add = "" + (user_tema.get(id).equals("Заправка картриджа") ? "" : "_rem");
        String[] lastmodel = DataBase.getUsFileds(Long.parseLong(sendMessage.getChatId()), "last_model" + add).split("&&&");
        if (DataBase.getUsFileds(Long.parseLong(sendMessage.getChatId()), "true_models" + add) != null) {
            String[] trmd = DataBase.getUsFileds(Long.parseLong(sendMessage.getChatId()), "true_models" + add).split(";");
            if (trmd.length <= 3) {
                for (int i = 0; i < 3; i++)
                    if (trmd.length >= i + 1)
                        one.add(new KeyboardButton(trmd[i]));
                two.add(new KeyboardButton("Главное меню"));
                keyboard.add(one);
                keyboard.add(two);
            }
            if (trmd.length > 3 && trmd.length <= 6) {
                for (int i = 0; i <= 3; i++)
                    if (trmd.length >= i + 1)
                        one.add(new KeyboardButton(trmd[i]));
                for (int i = 4; i < 6; i++)
                    if (trmd.length >= i + 1)
                        two.add(new KeyboardButton(trmd[i]));
                thr.add(new KeyboardButton("Главное меню"));
                keyboard.add(one);
                keyboard.add(two);
                keyboard.add(thr);
            }
            if (trmd.length > 6 && trmd.length <= 9) {
                for (int i = 0; i < 3; i++)
                    if (trmd.length >= i + 1)
                        one.add(new KeyboardButton(trmd[i]));
                for (int i = 3; i < 6; i++)
                    if (trmd.length >= i + 1)
                        two.add(new KeyboardButton(trmd[i]));
                for (int i = 6; i < 9; i++)
                    if (trmd.length >= i + 1)
                        thr.add(new KeyboardButton(trmd[i]));
                fh.add(new KeyboardButton("Главное меню"));
                keyboard.add(one);
                keyboard.add(two);
                keyboard.add(thr);
                keyboard.add(fh);
            }
            replyKeyboardMarkup.setKeyboard(keyboard);
            return;
        } else {
            if (lastmodel[0].equals("Не указано")) {
                one.add(new KeyboardButton("Главное меню"));
                keyboard.add(one);
                replyKeyboardMarkup.setKeyboard(keyboard);
                return;
            }
            if (lastmodel.length == 1) {
                one.add(new KeyboardButton(lastmodel[0]));
                two.add(new KeyboardButton("Главное меню"));
                keyboard.add(one);
                keyboard.add(two);
                replyKeyboardMarkup.setKeyboard(keyboard);
                return;
            }
            if (lastmodel.length == 2) {
                if (lastmodel[0].equals(lastmodel[1]))
                    one.add(new KeyboardButton(lastmodel[0]));
                else {
                    one.add(new KeyboardButton(lastmodel[0]));
                    one.add(new KeyboardButton(lastmodel[1]));
                }
                two.add(new KeyboardButton("Главное меню"));
                keyboard.add(one);
                keyboard.add(two);
                replyKeyboardMarkup.setKeyboard(keyboard);
                return;
            }
            if (lastmodel.length >= 3) {
                if (lastmodel[lastmodel.length - 1].equals(lastmodel[lastmodel.length - 2]))
                    one.add(new KeyboardButton(lastmodel[lastmodel.length - 1]));
                else {
                    one.add(new KeyboardButton(lastmodel[lastmodel.length - 1]));
                    one.add(new KeyboardButton(lastmodel[lastmodel.length - 2]));
                }
                two.add(new KeyboardButton("Главное меню"));
                keyboard.add(one);
                keyboard.add(two);
                replyKeyboardMarkup.setKeyboard(keyboard);
                return;
            }
        }
    }

    public void getYesNoButt() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> ryad = new ArrayList<InlineKeyboardButton>();
        ryad.add(new InlineKeyboardButton().setText("Да").setCallbackData("Да"));
        ryad.add(new InlineKeyboardButton().setText("Нет").setCallbackData("Нет"));
        //inlineKeyboardMarkup.setKeyboard(ryad);
    }

    public InlineKeyboardMarkup getOstZPrinterForPriceListButtons(String id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Заправить " + DataBase.getF("printer", pi(id))).setCallbackData("#ZaprPrinter/" + DataBase.getF("printer", pi(id)) + "/" + DataBase.getF("cartrige", pi(id))));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getReklamaciaButtons(Integer nz, Long id, String status) {
        InlineKeyboardMarkup inlineKeyboardMarkup = null;
        try {
            Order order = new Order(nz);
            inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
            String dateee = order.getDate();
            List<String> descriptions = order.getDescriptions();
            Boolean reklam_use = descriptions.contains("REKLAMACIYA_USE") ? true : false;
            Boolean cancel_use = descriptions.contains("CANCEL_USE") ? true : false;
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateee);
            Integer time_allow = 0;
            if (DataBase.getUserType(Math.toIntExact(id)).equals("Частное лицо"))
                time_allow = 3;
            else time_allow = 15;
            Integer days = Math.toIntExact(TimeUnit.MILLISECONDS.toDays(new Date().getTime() - date.getTime()));
            if (status.contains("Завершена"))
                if (days <= time_allow && reklam_use == false)
                    keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Рекламация").setCallbackData("#Reklamaciya/" + nz));
            if (!status.contains("Завершена"))
                if (cancel_use == false)
                    keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Отменить заявку").setCallbackData("#CancelZayavka/" + nz));
            List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
            rowList.add(keyboardButtonsRow1);
            inlineKeyboardMarkup.setKeyboard(rowList);
            return inlineKeyboardMarkup;
        } catch (Exception e) {
            return inlineKeyboardMarkup;
        }
    }

    public InlineKeyboardMarkup getReSearchPrice() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Повторить поиск").setCallbackData("#ReSearchPrice"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getOstZPrinterForPriceKListButtons(String id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Заправить " + DataBase.getF("cartrige", pi(id))).setCallbackData("#ZaprPrinter/" + DataBase.getF("printer", pi(id)) + "/" + DataBase.getF("cartrige", pi(id))));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getCancelVvodAdm() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Не вводить данные, компании").setCallbackData("#CancelVvodAdm"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getProvInfoButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Подать заявку").setCallbackData("#ПодатьЗаявку"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Изменить адрес").setCallbackData("#ИзменитьАдрес"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Изменить модель").setCallbackData("#ИзменитьМодель"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public InlineKeyboardMarkup getCorporationButtons(List<Long> all_corporations, String data) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        for (Long corporationID : all_corporations) {
            Corporation corp = new Corporation(corporationID);
            List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
            row.add(new InlineKeyboardButton().setText(corp.getName()).setCallbackData("#ADMIN/CORPORATION/" + corporationID + "/" + data));
            rowList.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getPersonalDeleteButtons(List<Long> personal) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        for (Long personID : personal) {
            List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
            row.add(new InlineKeyboardButton().setText(DataBase.getPerFields(personID, "name")).setCallbackData("#ADMIN/Personal/DeleteEmployee/" + personID));
            rowList.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getCorporationEmployeesListButtons(Long corporationID, String data) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        for (Long employeeID : new Corporation(corporationID).getEmployeesID()) {
            List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
            row.add(new InlineKeyboardButton().setText(DataBase.getUserStr("name", employeeID)).setCallbackData("#ADMIN/CORPORATION/" + corporationID + "/" + data + "/" + employeeID));
            rowList.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getAdminCorporationsDeleteMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        for (Long corporationID : getAllCorporationsID()) {
            List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
            row.add(new InlineKeyboardButton().setText(new Corporation(corporationID).getName()).setCallbackData("#ADMIN/CORP/" + corporationID + "/Delete"));
            rowList.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getCorporationAdressListButtons(Long corporationID, String data) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        for (String adress : new Corporation(corporationID).getAddresses()) {
            List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
            row.add(new InlineKeyboardButton().setText(adress).setCallbackData("#ADMIN/CORP/" + corporationID + "/" + data + "/" + adress.replaceAll(" ", "_")));
            rowList.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getPrintModelsButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Canon").setCallbackData("#PrintModel/Canon"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("HP").setCallbackData("#PrintModel/HP"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Samsung").setCallbackData("#PrintModel/Samsung"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Xerox").setCallbackData("#PrintModel/Xerox"));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Brother").setCallbackData("#PrintModel/Brother"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getPrintTypeButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Модель Принтера").setCallbackData("#SearchType/Print"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Модель Картриджа").setCallbackData("#SearchType/Kartr"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Показать весь список моделей").setCallbackData("#SearchType/AllModels"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getTypeButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Я представитель компании").setCallbackData("#ЯКомпания"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Я частное лицо").setCallbackData("#ЯЧастник"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getICZAButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ID").setCallbackData("#ADM_SEND_ID"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ZONE").setCallbackData("#ADM_SEND_ZONE"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("ADRESS").setCallbackData("#ADM_SEND_ADRESS"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("CATRTRIDGE").setCallbackData("#ADM_SEND_CARTRIDGE"));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText("ОТМЕНА").setCallbackData("#ADM_SEND_СANCEL"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getWhatMessengerV() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("\uD83D\uDFE0 \uD83D\uDFE0 Viber \uD83D\uDFE0 \uD83D\uDFE0").setCallbackData("#Пофиг"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Telegram").setCallbackData("#Пофиг"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Viber/Telegram").setCallbackData("#Пофиг"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getWhatMessengerT() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Viber").setCallbackData("#Пофиг"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("\uD83D\uDFE0 \uD83D\uDFE0 Telegram \uD83D\uDFE0 \uD83D\uDFE0").setCallbackData("#Пофиг"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Viber/Telegram").setCallbackData("#Пофиг"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getWhatMessengerAll() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Viber").setCallbackData("#Пофиг"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Telegram").setCallbackData("#Пофиг"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("\uD83D\uDFE0 \uD83D\uDFE0 Viber/Telegram \uD83D\uDFE0 \uD83D\uDFE0").setCallbackData("#Пофиг"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getWhatMessenger() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Viber").setCallbackData("#SEND_TO_VIBER"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Telegram").setCallbackData("#SEND_TO_TELEGRAM"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Viber/Telegram").setCallbackData("#SEND_TO_ALL"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getZaprKt() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Оставить заявку на заправку").setCallbackData("#SaveZayav"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Спасибо, пока что не нужно").setCallbackData("#ThankYouNo"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getNapomButtons(Integer msgid, String type) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Отправить сейчас").setCallbackData("#SEND=NOW=" + type + "=" + msgid));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Отправить завтра").setCallbackData("#SEND=TOMORROW=" + type + "=" + msgid));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Назначить дату отправки").setCallbackData("#SEND=DATE=" + type + "=" + msgid));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getAMenuButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(),
                keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow4 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow5 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Обновить поздравление").setCallbackData("#SEND_DR"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Обновить напоминание").setCallbackData("#SEND_CHASTOT"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Отправить всем").setCallbackData("#ADM_SEND_ALL"));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText("ID").setCallbackData("#ADM_SEND_ID"));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Картридж").setCallbackData("#ADM_SEND_CARTRIDGE"));
        keyboardButtonsRow4.add(new InlineKeyboardButton().setText("Зона").setCallbackData("#ADM_SEND_ZONE"));
        keyboardButtonsRow4.add(new InlineKeyboardButton().setText("Адрес").setCallbackData("#ADM_SEND_ADRESS"));
        keyboardButtonsRow5.add(new InlineKeyboardButton().setText("Отменить рассылку").setCallbackData("#ADM_SEND_СANCEL"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);
        rowList.add(keyboardButtonsRow5);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getSoglasButton(String subOrdersID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Да! Согласовываю!").setCallbackData("#SOGLASOVANO=" + subOrdersID));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Хочу уточнить у менеджера.").setCallbackData("#MENED=" + subOrdersID));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Купить новый картридж").setCallbackData("#ByNewKart=" + subOrdersID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getTTTButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("СОГЛАСОВАНО").setCallbackData("#S"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getTTTTButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Менеджер Вам перезвонит!Спасибо!").setCallbackData("#S"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getMButton(String subOrdersID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Отправить").setCallbackData("#SendMsgForNz=" + subOrdersID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getM1Button(Integer nz) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("☢ ☢ ☢ ОТПРАВЛЕНО ☢ ☢ ☢").setCallbackData("#ПофигGetM1Button"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getNapomDaysButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Оставить заявку на заправку").setCallbackData("#OST_Z_ZAPR"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Пока всё в порядке :-)").setCallbackData("#VSE_V_PORYDKE"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Напомнить через пару дней").setCallbackData("#NAPOM_TWO_DAY"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getNapomDaysTwoButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Оставить заявку на заправку").setCallbackData("#OST_Z_ZAPR"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Пока всё в порядке :-)").setCallbackData("#VSE_V_PORYDKE"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public InlineKeyboardMarkup getMenedButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Перейти в чат с менеджером").setUrl("t.me/koi_service"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getOtvetitBut() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Ответить").setUrl("t.me/koi_service"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getGoogleMapButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Показать на карте Google Maps").setUrl("https://goo.gl/maps/5BqcDzCb5VPyzj8V6"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Позвонить нам (ЭТО БЕСПЛАТНО)").setUrl("http://ay.dn.ua/FreeCallToKoi.html"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getObrobotkaButton(Long nomer) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(" \uD83D\uDD34  \uD83D\uDD34  \uD83D\uDD34 - ПРИНЯТЬ ЗАЯВКУ-  \uD83D\uDD34  \uD83D\uDD34  \uD83D\uDD34").setCallbackData("#PRINYAT-Z=" + nomer));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getObrobotkaReklamButton(Long nomer) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(" \uD83D\uDD34  \uD83D\uDC49 - Заявка принята -  \uD83D\uDC48 \uD83D\uDD34").setCallbackData("#REKLAM_HAND/" + nomer));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getCellButton(Long nomer) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(" \uD83D\uDD34  \uD83D\uDD34  \uD83D\uDD34 - ОБЗВОНИЛ -  \uD83D\uDD34  \uD83D\uDD34  \uD83D\uDD34").setCallbackData("#OBZVON-Z=" + nomer));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getCancelZayavButtons(Long nomer) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ПОДТВЕРДИТЬ").setCallbackData("#CONFIRM_CANCEL_ZAYAV/" + nomer));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ОТКЛОНИТЬ").setCallbackData("#OTKLON_CANCEL_ZAYAV/" + nomer));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public InlineKeyboardMarkup getCellTrueButton(int id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("\uD83D\uDFE0 \uD83D\uDFE0 \uD83D\uDFE0 - ЗАЯВКА ОБЗВОНЕНА - " + DataBase.getPerFields((long) id, "position") + " " + DataBase.getPerFields((long) id, "name") + "\uD83D\uDFE0 \uD83D\uDFE0 \uD83D\uDFE0").setCallbackData("#Пох"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getObrobotkaTrueButton(Integer nz) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        List<String> statuses = new ArrayList<String>();
        Order order = new Order(nz);
        if (nz != null)
            statuses = order.getAllStatuses();
        if (nz == null || !containsStatus(statuses, "Курьер выехал к Вам (Сбор)") && (!containsStatus(statuses, "Заявка в работе") && !containsStatus(statuses, "Курьер выехал к Вам (Доставка)")))
            keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Сбор").setCallbackData("#ChangeZStatus/Сбор_в_пути/" + nz));
        if (nz == null || !containsStatus(statuses, "Заявка в работе") && !containsStatus(statuses, "Курьер выехал к Вам (Доставка)"))
            keyboardButtonsRow2.add(new InlineKeyboardButton().setText("В работе").setCallbackData("#ChangeZStatus/Заявка_в_работе/" + nz));
        if (nz == null || !containsStatus(statuses, "Курьер выехал к Вам (Доставка)"))
            keyboardButtonsRow2.add(new InlineKeyboardButton().setText("СОГЛАСОВАТЬ").setCallbackData("#SoglasVoss/" + nz));
        if (nz == null || !containsStatus(statuses, "Курьер выехал к Вам (Доставка)"))
            keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Доставка").setCallbackData("#ChangeZStatus/Доставка_в_пути/" + nz));
        if (containsStatus(statuses, "Курьер выехал к Вам (Сбор)") || containsStatus(statuses, "Заявка в работе") || containsStatus(statuses, "Курьер выехал к Вам (Доставка)"))
            keyboardButtonsRow3.add(new InlineKeyboardButton().setText("ЗАВЕРШИТЬ").setCallbackData("#ENDZAYAV/" + nz));
        else
            keyboardButtonsRow3.add(new InlineKeyboardButton().setText("ОТМЕНИТЬ").setCallbackData("#CANCELORDER/" + nz));
        if (nz != null && !order.getDescriptions().contains("QR-CREATE"))
            keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Создать QR").setCallbackData("#CreateQR/" + nz));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public boolean containsStatus(List<String> statuses, String status) {
        for (String s : statuses)
            if (s.contains(status))
                return true;
        return false;
    }

    public InlineKeyboardMarkup getEndZayavButton(int nz) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Доставка").setCallbackData("#ChangeZStatus/Доставка_в_пути"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ЗАВЕРШИТЬ ЗАЯВКУ").setCallbackData("#ENDZFORVOSST/" + nz));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getCurrentZButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        for (Long zn : OrderLocations.getAllCurrentOrdersID()) {
            Order order = new Order(zn);
            List<InlineKeyboardButton> temprow = new ArrayList<InlineKeyboardButton>();
            String u_id = order.getUID().toString();
            if (u_id == null || u_id.equals("NULL"))
                continue;
            User user = new User(new TidToUidTable(order.getUID(), false).getTelegramID());
            temprow.add(new InlineKeyboardButton().setText("#" + zn + " от " + user.getUserName() + "(" + user.getUserPhone() + ") " + order.getModel()).setCallbackData("#ManagerEditZ/" + zn));
            rowList.add(temprow);
        }
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getObrobotkaTrueTrueButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("ПЕРЕЙТИ В ЧАТ С БОТОМ").setUrl("https://t.me/koi_servis_bot"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getOprosButton(Long nz) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Отправить утром ").setCallbackData("#OPROS-M-Z=" + nz));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Перенести на 2 дня").setCallbackData("#OPROS-T-Z=" + nz));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Отменить опрос").setCallbackData("#OPROS-C-Z=" + nz));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getOprButton(String type, Integer nomer) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        if (type.contains("cancel"))
            keyboardButtonsRow1.add(new InlineKeyboardButton().setText("❌ ❌ ❌ -> ОПРОС ОТМЕНЁН <- ❌ ❌ ❌").setCallbackData("#ПофигCancel"));
        if (type.contains("НА2ДНЯ"))
            keyboardButtonsRow1.add(new InlineKeyboardButton().setText("\uD83D\uDFE2 \uD83D\uDFE2 \uD83D\uDFE2 -> БУДЕТ ОПРОШЕНА ПОСЛЕЗАВТРА <- \uD83D\uDFE2 \uD83D\uDFE2 \uD83D\uDFE2").setCallbackData("#Пофиг2"));
        if (type.contains("УТРОМ"))
            keyboardButtonsRow1.add(new InlineKeyboardButton().setText("\uD83D\uDFE2 \uD83D\uDFE2 \uD83D\uDFE2 -> БУДЕТ ОПРОШЕНА УТРОМ <- \uD83D\uDFE2 \uD83D\uDFE2 \uD83D\uDFE2").setCallbackData("#ПофигMorn"));
        //keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Изменить время опроса").setCallbackData("#ChangeOprosTime-Z="+nomer));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getSpasiboZaRateButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Спасибо! Ваша оценка поможет нам стать лучше.").setCallbackData("#СпасибоЗаОценку"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getButText(String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(text).setCallbackData("#IGNORED"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getOcenkiButtons(int zn) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("0").setCallbackData("#Rate=0=" + zn));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("1").setCallbackData("#Rate=1=" + zn));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("2").setCallbackData("#Rate=2=" + zn));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("3").setCallbackData("#Rate=3=" + zn));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("4").setCallbackData("#Rate=4=" + zn));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("5").setCallbackData("#Rate=5=" + zn));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("6").setCallbackData("#Rate=6=" + zn));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("7").setCallbackData("#Rate=7=" + zn));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getNPBt() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow4 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Завтра").setCallbackData("#PerenosNaZavtra"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Через 3 дня").setCallbackData("#PerenosNa3Days"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("В понедельник").setCallbackData("#PerenosNaPn"));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Оставить заявку").setCallbackData("#SaveZayav"));
        keyboardButtonsRow4.add(new InlineKeyboardButton().setText("Отменить").setCallbackData("#CancelNP"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getTr(String s) {
        return new InlineKeyboardMarkup().setKeyboard(Arrays.asList(Arrays.asList(
                new InlineKeyboardButton[]{new InlineKeyboardButton().setText(s).setCallbackData("#NP").setSwitchInlineQueryCurrentChat("Готово!")
                })));
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
        s.setReplyMarkup(getObrobotkaButton(nza));
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
        s.setReplyMarkup(getObrobotkaReklamButton(nza));
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
        s.setReplyMarkup(getObrobotkaButton(z_id));
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
        sendMsg(getCellChannelID(), text, getCellButton(nza));
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
        sendMsg(getZayavChannelID(), text, getCancelZayavButtons(nza));
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
            s.setReplyMarkup(getOcenkiButtons(nz));
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
        int msgid = sendMsg(getZayavChannelID(), text, getOprosButton(nza));
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
        sendMsg(msg, "Здравствуйте, я чат-бот сервисного центра KOI."
                + "\nЗдесь вы можете: "
                + "\n- Оставить заявку на заправку картриджа;"
                + "\n- Оставить заявку на ремонт принтера;"
                + "\n- Узнать ЦЕНЫ ЗАПРАВКИ КАРТРИДЖЕЙ;"
                + "\n- Приобрести принтер/катридж/расходные материалы;"
                + "\n- Посмотреть где мы находимся и как к нам добраться;"
                + "\n- Связяться с нашим менеджером;", "");
    }

    public void user_enter_type(Message msg) {
        sendMsg(msg, "Вы представитель компании или частное лицо? Для компаний у нас действует специальная система скидок и бонусов!", "type");
    }

    public void user_add_contact(Message msg) {
        sendMsg(msg, "Для удобства в дальнейшем общении прошу поделиться Вашим контактом.(Кнопка отправить контакт ниже ↓)", "send_contact");
    }

    public void user_spasibo_contact(Message msg) {
        sendMsg(msg, msg.getContact().getFirstName() + ", спасибо за предоставление Вашего номера телефона! ", "");
    }

    public void user_cancel_contact(Message msg) {
        sendMsg(msg, "Что бы  принять заявку мне нужен Ваш номер телефона. Данная информация останется конфиденциальной.", "send_contact");
    }

    public void user_reg_complete(Message msg) {
        sendMsg(msg, "Теперь Вам доступна возможность оставить зявку на заправку/ремонт картриджа/принтера.", "work_true");
    }

    public void contacts(Message msg) {
        sendMsg(msg, "Телефоны: "
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