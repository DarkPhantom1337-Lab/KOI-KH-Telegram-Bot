import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;

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


    public static Timer vh = new Timer(), vz, vr, vv, vn;


    public static List<String> menus = Arrays.asList(new String[]{"Оставить заявку на заправку", "Оставить заявку на ремонт", "Написать менеджеру", "Адрес/Контакты"});

    int nomer = 1;

    public static DataBase db = null;
    public static Bot bot = null;
    public static Handlers hl = null;
    public Utils u = null;
    public static AdminHandler ah = null;

    public Boolean enabled = false;

    public static void main(String[] args) throws IOException {
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
            startVzHandler();
            startAutoRestartVzHandler();
            startVvHandler();
            startAutoRestartVvHandler();
            startVrHandler();
            startAutoRestartVrHandler();
            viberHandlers();
        }

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
                            bot.handVosst(zn, "Viber", DataBase.getVSFileds(i, "answer"));
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
            System.out.println(prefix() + "Error in deleting msg."
                    + "\n-> ChatID: " + chatid
                    + "\n-> MsgID: " + msgid
                    + "\n-> Exception: " + e.getLocalizedMessage()
                    + "\n-> Error line: " + e.getStackTrace()[0].getLineNumber());
            e.printStackTrace();
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

    static String getFileExtension(java.io.File file) {
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
                                Date ldate = new SimpleDateFormat("dd/MM/yyyy").parse(DataBase.getZFields(last_z, "date"));
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
                                Date ldate = new SimpleDateFormat("dd/MM/yyyy").parse(DataBase.getZFields(last_z, "date"));
                                Date tdate = new Date();
                                int days = bot.daysBetween(ldate, tdate);
                                if (days == d) {
                                    URL oracle = new URL("https://ay.dn.ua/ViberBot/src/ChangeStatus.php?dark_id=" + DataBase.getZFields(last_z, "u_id") + "&zn=" + last_z + "&status=napom_zapr&key=1337");
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
        System.out.println("[KH] [KOI] [" + bot.u.getDate("dd/MM/YYYY") + "] [" + bot.u.getTime("HH:mm:ss") + "] [INFO] -> " + s);
    }

    public void error(String s) {
        System.out.println("[KH] [KOI] [" + bot.u.getDate("dd/MM/YYYY") + "] [" + bot.u.getTime("HH:mm:ss") + "] [ERROR] -> " + s);
    }

    public static String decode(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new java.io.File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override //Получение обновлений(Сообщения/Данные и т.д.)
    public void onUpdateReceived(final Update e) {
        if (bot.enabled == true) {
            if (e.hasMessage()) {
                Message msg = e.getMessage();
                Long fid = msg.getFrom().getId().longValue(), id = msg.getChatId();
                User user = new User(fid);
                if (umh.handleMessage(msg, fid)) return;
                if (msg.getContact() != null) {
                    try {
                        sendToLogChanel("Пользователь " + msg.getFrom().getFirstName() + " прислал свой контакт. Номер: " + msg.getContact().getPhoneNumber());
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                    info("[CONTACT] [" + fid + "] Status: Received Information: { ID:" + fid + " Phone:" + msg.getContact().getPhoneNumber());
                    if (umh.handContact(id, msg)) return;
                    info("[CONTACT] [" + fid + "] Status: NOT HANDLED Information: { ID:" + fid + " Phone:" + msg.getContact().getPhoneNumber() + "}");
                    return;
                }
                if (msg.hasDocument()) {
                    try {
                        bot.saveDocument("EAN_13.jpg", bot.getFile(new GetFile().setFileId(msg.getDocument().getFileId())).getFileUrl(bot.getBotToken()));
                    } catch (TelegramApiException telegramApiException) {
                        telegramApiException.printStackTrace();
                    }
                    try {
                        sendMsgToUser(fid, "Отсканированный текст: " + decode("EAN_13.jpg"));
                    } catch (Exception e1) {
                        sendMsgToUser(fid, "Не вышло отксканировать штрихкод.");
                    }
                    return;
                }
                if (e.getMessage().hasText()) {
                    String txt = msg.getText();
                    Integer message_id = msg.getMessageId();
                    info("[TEXT] [" + fid + "] Status: Received Information: { ID:" + fid + " Text:" + txt);
                    try {
                        sendToLogChanel("Пользователь " + msg.getFrom().getFirstName() + " написал текстовое сообщение. Текст: " + txt);
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                    if (umh.handleStart(txt, msg, fid)) return;
                    if (DataBase.getUserStr("action", fid) == null || (DataBase.getUserStr("action", fid) != null && !DataBase.getUserStr("action", fid).equals("user_wait_name")))
                        if (umh.checkIsRegister(txt, msg, fid)) return;
                    if (DataBase.isPersonal(fid)) {
                        String vacantion = DataBase.getPerFields(fid, "v_id");
                        if (vacantion.equals("admin") || vacantion.equals("owner")) {
                            if (ah.handleTextMessage(fid, txt, message_id)) return;
                        } else {
                            // TODO For Managers
                        }
                    }
                    if (umh.handleUserReklamComm(fid, txt)) return;
                    if (umh.handleUserName(user, txt, msg)) return;
                    if (umh.handlePersonalMsgs(fid, txt, msg)) return;
                    if (umh.handleUserRateComm(fid, txt, msg)) return;
                    if (handZayav(id, msg, txt)) return;
                    if (umh.handleUserAdress(fid, txt, msg)) return;
                    if (umh.handleUserCompanyName(fid, txt, msg)) return;

                    if (user_wait_semodel.containsKey(fid)) {
                        if (user_stype.get(fid).equals("Print")) {
                            List<String> all = DataBase.getAllPrices(prnt_model.get(fid), true, txt);
                            if (all.size() == 0) {
                                sendMsg(msg, "Ничего не найдено!", "main");
                            } else {
                                int i = 1;
                                for (String s : all) {
                                    if (i <= 20) {
                                        String[] spl = s.split("/");
                                        sendMsg(e.getMessage().getFrom().getId(), "Модель принтера: " + spl[0] + "\nМодель картриджа: " + spl[1] + "\nЦЕНА ЗАПРАВКИ: " + spl[2], "ost_zmodel/" + spl[3]);
                                    } else {
                                        sendMsg(e.getMessage().getFrom().getId(), "Пожалуйста повторите попытку, введя более точное значение.", "main");
                                        return;
                                    }
                                    i++;
                                }
                            }
                        } else {
                            Map<String, List<String>> grp = DataBase.getAllPricesForCartr(prnt_model.get(fid), txt);
                            for (String key : grp.keySet()) {
                                String printers = "\n";
                                int i = 1;
                                for (String s : grp.get(key)) {
                                    printers += "- " + s + (i <= grp.get(key).size() - 1 ? "\n" : "");
                                }
                                sendMsg(e.getMessage().getFrom().getId(), "Модель картриджа: " + key.replaceAll("¦", "/") + "\nПодходит для следующих моделей принтеров: " + printers.replaceAll("¦", "/") + "\nЦЕНА ЗАПРАВКИ: " + (DataBase.getF("price_refill", DataBase.getIdForCart(key.replaceAll("¦", "/"))) == null ? "Уточняйте у менеджера" : DataBase.getF("price_refill", DataBase.getIdForCart(key.replaceAll("¦", "/")))), "ost_kart%" + DataBase.getIdForCart(key.replaceAll("¦", "/")));
                            }
                        }
                        sendMsg(e.getMessage().getFrom().getId(), "Если в списке нет Вашей модели, повторите поиск, и введите более подробное название либо свяжитесь с нашим менеджером.", "research_price");
                        user_wait_semodel.remove(fid);
                        user_stype.remove(fid);
                        prnt_model.remove(fid);
                        return;
                    }
                    switch (txt) {
                        case "/start":
                            if (DataBase.getAllBlackUserId().contains(Math.toIntExact(id))) {
                                sendMsg(msg, "Извините, но Вам запрещено производить какие-либо действия! Для уточнения деталей напишите менеджеру...", "mened");
                                return;
                            }
                            handStart(id, msg);
                            return;
                        case "Адрес/Контакты":
                            contacts(msg);
                            return;
                        case "Мои заявки":
                            sendMsgToUser(fid, "Вы перешли в меню заявок.", "my_zayavki");
                            return;
                        case "Текущие заявки":
                            if (DataBase.isCorporationWorker(id)) {
                                Corporation corp = new Corporation(DataBase.getCorporationID(id));
                                if (corp.getOrdersID().size() <= 0) {
                                    sendMsg(msg, "Вы ещё не оставляли заявки!", "no_z");
                                    return;
                                } else {
                                    for (Long nzz : corp.getOrdersID()) {
                                        int nz = Math.toIntExact(nzz);
                                        String status = DataBase.getZFields(nz, "status");
                                        if (!status.contains("Завершена")) {
                                            String current_statuses = "\nℹ️ Заявка №" + nz;
                                            for (String st : DataBase.getZStatuses((long) nz))
                                                current_statuses += "\n" + st;
                                            sendMsg(msg, "Заявка №" + nz + " от " + DataBase.getZFields(nz, "date") +
                                                    "\nТема: " + DataBase.getZFields(nz, "theme") + "\nМодель: " + DataBase.getZFields(nz, "model") +
                                                    "\nАдрес: " + DataBase.getZFields(nz, "adress")
                                                    + current_statuses, "reklamaciya/" + nz + "/" + status);
                                        }
                                    }
                                    return;
                                }
                            }
                            String allz = "";
                            if (DataBase.getUsFileds(id, "all_z") == null) {
                                sendMsg(msg, "Вы ещё не оставляли заявки!", "no_z");
                                return;
                            } else {
                                allz = DataBase.getUsFileds(id, "all_z");
                            }
                            List<String> zayavs = new ArrayList<String>(Arrays.asList(allz.split(",")));
                            for (String s : zayavs) {
                                try {
                                    int nz = pi(s);
                                    String status = DataBase.getZFields(nz, "status");
                                    if (!status.contains("Завершена")) {
                                        String current_statuses = "\nℹ️ Заявка №" + nz;
                                        for (String st : DataBase.getZStatuses((long) nz))
                                            current_statuses += "\n" + st;
                                        sendMsg(msg, "Заявка №" + nz + " от " + DataBase.getZFields(nz, "date") +
                                                "\nТема: " + DataBase.getZFields(nz, "theme") + "\nМодель: " + DataBase.getZFields(nz, "model") +
                                                "\nАдрес: " + DataBase.getZFields(nz, "adress")
                                                + current_statuses, "reklamaciya/" + nz + "/" + status);
                                    }
                                } catch (Exception eee) {
                                    continue;
                                }
                            }
                            return;
                        case "Завершённые заявки":
                            if (DataBase.isCorporationWorker(id)) {
                                Corporation corp = new Corporation(DataBase.getCorporationID(id));
                                if (corp.getOrdersID().size() <= 0) {
                                    sendMsg(msg, "Вы ещё не оставляли заявки!", "no_z");
                                    return;
                                } else {
                                    for (Long nzz : corp.getOrdersID()) {
                                        int nz = Math.toIntExact(nzz);
                                        String status = DataBase.getZFields(nz, "status");
                                        if (status.contains("Завершена")) {
                                            String current_statuses = "\nℹ️ Заявка №" + nz;
                                            for (String st : DataBase.getZStatuses((long) nz))
                                                current_statuses += "\n" + st;
                                            sendMsg(msg, "Заявка №" + nz + " от " + DataBase.getZFields(nz, "date") +
                                                    "\nТема: " + DataBase.getZFields(nz, "theme") + "\nМодель: " + DataBase.getZFields(nz, "model") +
                                                    "\nАдрес: " + DataBase.getZFields(nz, "adress")
                                                    + current_statuses, "reklamaciya/" + nz + "/" + status);
                                        }
                                    }
                                    return;
                                }
                            }
                            String allzz = "";
                            if (DataBase.getUsFileds(id, "all_z") == null) {
                                sendMsg(msg, "Вы ещё не оставляли заявки!", "no_z");
                                return;
                            } else {
                                allzz = DataBase.getUsFileds(id, "all_z");
                            }
                            List<String> zayavvs = new ArrayList<String>(Arrays.asList(allzz.split(",")));
                            for (String s : zayavvs) {
                                try {
                                    int nz = pi(s);
                                    String status = DataBase.getZFields(nz, "status");
                                    if (status.contains("Завершена")) {
                                        String current_statuses = "\nℹ️ Заявка №" + nz;
                                        for (String st : DataBase.getZStatuses((long) nz))
                                            current_statuses += "\n" + st;
                                        sendMsg(msg, "Заявка №" + nz + " от " + DataBase.getZFields(nz, "date") +
                                                "\nТема: " + DataBase.getZFields(nz, "theme") + "\nМодель: " + DataBase.getZFields(nz, "model") +
                                                "\nАдрес: " + DataBase.getZFields(nz, "adress")
                                                + current_statuses, "reklamaciya/" + nz + "/" + status);
                                    }
                                } catch (Exception eee) {
                                    continue;
                                }
                            }
                            return;
                        case "Написать менеджеру":
                            sendMsg(msg, "Нажмите на кнопку ниже чтобы перейти в чат с нашим менеджером", "mened");
                            return;
                        case "Главное меню":
                            sendMsg(msg, "Главное меню", "main");
                            user_wait_adress.remove(id);
                            user_wait_model.remove(id);
                            return;
                        case "Прайс лист":
                            sendMsg(Math.toIntExact(id), "Пожалуйста выберите производителя Вашего принтера ниже:", "print_models");
                            return;
                        case "Используя QR-код":
                            user.setUserAction("user_wait_qr");
                            sendMsgToUser(user.getID(), user.getUserName() + ", сфотографируйте QR-код на вашем картридже/принтере и отправьте мне, либо отправьте мне цифры написанные под QR кодом и я автоматически сформирую заявку :-) ", "backtomain");
                            break;
                        case "Заполняя форму":
                            if (user_tema.containsKey(user.getID())) {
                                if (user_tema.get(user.getID()).equals("Заправка картриджа")) {
                                    if (!handlOstUsZaprZayav(msg, id))
                                        sendMsgToUser(user.getID(), user.getUserName() + ", заявка не может быть подана. Повторите попытку пожалуйста.", "main");
                                } else
                                    if (!handlOstUsRemZayav(msg, id))
                                        sendMsgToUser(user.getID(), user.getUserName() + ", заявка не может быть подана. Повторите попытку пожалуйста.", "main");
                                return;
                            }
                            sendMsgToUser(user.getID(), user.getUserName() + ", заявка не может быть подана. Повторите попытку пожалуйста.", "main");
                            break;
                    }
                    if (DataBase.isSetMainInfo(Math.toIntExact(id)) == true) {
                        if (txt.equals("Оставить заявку на заправку")) {
                            if (user.getCartridgesID().isEmpty()) {
                                user_tema.put(id, "Заправка картриджа");
                                if (handlOstUsZaprZayav(msg, id) == true)
                                    return;
                            } else {
                                sendMsgToUser(user.getID(), user.getUserName() + ", выберите как бы Вы хотели оформить" + txt.replaceAll("Оставить", "") + ".Если Вы выберите вариант 'Используя QR-код', Вам будет нужно сфотографировать QR-код на Вашем картридже и отправить мне, и я автоматически сформирую заявку по этому картриджу.", "user_qr_zayav");
                                return;
                            }
                        }
                        if (txt.equals("Оставить заявку на ремонт")) {
                            if (user.getCartridgesID().isEmpty()) {
                                user_tema.put(id, "Ремонт принтера");
                                if (handlOstUsRemZayav(msg, id) == true)
                                    return;
                            } else {
                                sendMsgToUser(user.getID(), user.getUserName() + ", выберите как бы Вы хотели оформить" + txt.replaceAll("Оставить", "") + ".Если Вы выберите вариант 'QR', Вам будет нужно сфотографировать QR-код на Вашем картридже и отправить мне, и я автоматически сформирую заявку по этому картриджу/принтеру.", "user_qr_zayav");
                                return;
                            }
                        }
                    }
                    sendMsg(msg, "Вы хотите нам что-то сообщить или узнать? Напишите нашему менеджеру ", "mened");
                }
            } else if (e.hasCallbackQuery()) {
                try {

                    final Long id = e.getCallbackQuery().getMessage().getChatId();
                    Integer msgid = e.getCallbackQuery().getMessage().getMessageId(), fromid = e.getCallbackQuery().getFrom().getId();
                    String data = e.getCallbackQuery().getData(), text = e.getCallbackQuery().getMessage().getText();
                    System.out.println(prefix() + "User -> " + DataBase.getUserName(fromid) + "(" + fromid + ") press button: `" + data + "`");
                    try {
                        sendToLogChanel("Пользователь " + e.getCallbackQuery().getFrom().getFirstName() + " нажал на кнопку: UID: " + fromid + " CID: " + id + " DATA: " + data);
                    } catch (Exception e1) {

                    }
                    if (ah.handleCallBack(Long.parseLong("" + fromid), id, data, msgid, e.getCallbackQuery().getId()))
                        return;
                    if (user_type.containsKey(id) == false) {
                        if (data.equals("#ЯКомпания")) {
                            if (DataBase.getUserType(Math.toIntExact(id)).equals("Не указано")) {
                                deleteMsg(id, msgid);
                                sendToLogChanel("Пользователь " + DataBase.getUserName(fromid) + "(" + fromid + ") указал что он является КОМПАНИЕЙ.");
                                sendToUserInfoChanel("Пользователь " + DataBase.getUserName(fromid) + "(" + fromid + ") указал что он является КОМПАНИЕЙ.");
                                DataBase.setUsFields(id, "type", "Компания");
                                answerCallbackQuery(new AnswerCallbackQuery().setText("Спасибо :-)").setCallbackQueryId(e.getCallbackQuery().getId()));
                                sendMsgToUser(id, "Напишите название компании которую Вы представляете!", "backtomain");
                                user_wait_company_name.put(id, true);
                            } else {
                                answerCallbackQuery(new AnswerCallbackQuery().setText("Вы уже выбрали ответ :-)").setCallbackQueryId(e.getCallbackQuery().getId()));
                            }
                            return;
                        }
                        if (data.equals("#ЯЧастник")) {
                            if (DataBase.getUserType(Math.toIntExact(id)).equals("Не указано")) {
                                deleteMsg(id, msgid);
                                sendToLogChanel("Пользователь " + DataBase.getUserName(fromid) + "(" + fromid + ") указал что он является ЧАСТНЫМ ЛИЦОМ.");
                                sendToUserInfoChanel("Пользователь " + DataBase.getUserName(fromid) + "(" + fromid + ") указал что он является ЧАСТНЫМ ЛИЦОМ.");
                                DataBase.setUsFields(id, "type", "Частное лицо");
                                answerCallbackQuery(new AnswerCallbackQuery().setText("Спасибо :-) Теперь вы можете оставить заявку").setCallbackQueryId(e.getCallbackQuery().getId()));
                                user_wait_adress.put(id, true);
                                sendMsg(e.getCallbackQuery().getMessage(), DataBase.getUserName(Math.toIntExact(id)) + ", укажите пожалуйста адрес для выезда курьера за заявкой. ", "adress");
                            } else {
                                answerCallbackQuery(new AnswerCallbackQuery().setText("Вы уже выбрали ответ :-)").setCallbackQueryId(e.getCallbackQuery().getId()));
                            }
                            return;
                        }
                    }
                    if (data.contains("#PRINYAT-Z=")) {
                        if (DataBase.isPersonal((long) fromid) == false) {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("У вас недостаточно прав!"));
                            return;
                        }
                        String nzayav = data.split("=")[1];
                        DataBase.setZFields(pi(nzayav), "status", "ПринятаМенеджером");
                        editMsg(id, msgid, e.getCallbackQuery().getMessage().getText().replaceAll("Поступила",
                                "Принята - " + DataBase.getPerFields((long) fromid, "position")
                                        + " " + DataBase.getPerFields((long) fromid, "name")));
                        DataBase.setZFields(pi(nzayav), "prinyal", DataBase.getPerFields((long) fromid, "position")
                                + " " + DataBase.getPerFields((long) fromid, "name"));
                        System.out.println(prefix() + "Z №" + nzayav + " prinyata! Prinyal: " + DataBase.getPerFields((long) fromid, "position")
                                + " " + DataBase.getPerFields((long) fromid, "name"));
                        sendToLogChanel("Пользователь " + DataBase.getPerFields((long) fromid, "name") + "(" + fromid + ") принял заявку №" + nzayav);
                        Boolean isss = false;
                        if (DataBase.getZFields(Integer.parseInt(nzayav), "source").equals("Viber")) {
                            URL oracle = new URL("https://ay.dn.ua/ViberBot/src/ChangeStatus.php?dark_id=" + DataBase.getZFields(pi(nzayav), "u_id") + "&zn=" + nzayav + "&status=prinyata&key=1337");
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(oracle.openStream()));
                            String inputLine;
                            while ((inputLine = in.readLine()) != null)
                                System.out.println(inputLine);
                            in.close();
                        } else {
                            isss = true;
                        }
                       /* if (DataBase.isPersonal(Long.parseLong(DataBase.getZFields(Integer.parseInt(nzayav), "u_id")))) {
                            DataBase.setZFields(Integer.parseInt(nzayav), "status", "Завершена|Опрошена");
                            new Timer().schedule(new TimerTask() { // Таймер для опроса
                                @Override
                                public void run() {
                                    try {
                                        sendToArchiveTask(Integer.parseInt(nzayav), 5, (long) 1337, true);
                                    } catch (Exception ex) {
                                        System.out.println(prefix() + "Error in send to archive ADMIN Zayav"
                                                + "\n-> NZayzav: " + nzayav
                                                + "\n-> Exception: " + ex.getLocalizedMessage()
                                                + "\n-> Error line: " + ex.getStackTrace()[0].getLineNumber());
                                    }
                                    this.cancel();
                                }
                            }, 60000); //
                        } else {*/
                        editMsg(id, msgid, getObrobotkaTrueButton(null));
                        bot.updateZStatus(pi(nzayav), "Заявка принята.", "Заявка № " + nzayav + " принята менеджером! Ожидайте звонка...");
                        new Timer().schedule(new TimerTask() { // Таймер для опроса
                            @Override
                            public void run() {
                                try {
                                    int zn = Integer.parseInt(nzayav), uid = Integer.parseInt(DataBase.getZFields(zn, "u_id"));
                                    bot.deleteMsg(Long.parseLong(getZayavChannelID()), pi(DataBase.getZFields(zn, "main_msg_id")));
                                    if (DataBase.getZFields(Math.toIntExact(zn), "source").equals("Viber")) {
                                        sendToChanelOpros((long) zn,
                                                DataBase.getZFields(zn, "theme"), DataBase.getZFields(zn, "model"),
                                                DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(zn), "u_id")), "type"),
                                                DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(zn), "u_id")), "name"),
                                                DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(zn), "u_id")), "phone"),
                                                DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(zn), "u_id")), "company_name"),
                                                DataBase.getZFields(Math.toIntExact(zn), "source"));
                                    } else {
                                        sendToChanelOpros((long) zn,
                                                DataBase.getZFields(zn, "theme"), DataBase.getZFields(zn, "model"),
                                                DataBase.getUserType(uid), DataBase.getUserName(uid), DataBase.getUserPhone(uid),
                                                DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(zn), "u_id")), "company_name"),
                                                DataBase.getZFields(Math.toIntExact(zn), "source"));
                                    }
                                    String s = DataBase.getZFields(zn, "msg_ids");
                                    if (s != null) {
                                        s += "," + id + "!" + msgid;
                                    } else {
                                        s = "" + id + "!" + msgid;
                                    }
                                    DataBase.setZFields(zn, "msg_ids", s);
                                    System.out.println(prefix() + "Z №" + nzayav + " end! Prinimal: " + DataBase.getPerFields((long) fromid, "position")
                                            + " " + DataBase.getPerFields((long) fromid, "name") + " Oprosit?");
                                    sendToLogChanel("Заявка №" + nzayav + " завершена по истечению 2х дней. Принимал: " + DataBase.getZFields(pi(nzayav), "prinyal"));
                                } catch (Exception ex) {
                                    sendToLogChanel("Заявка №" + nzayav + " ОШИБКА при завершении заявки. Принимал: " + DataBase.getZFields(pi(nzayav), "prinyal") + "\n" + ex.getMessage());
                                    System.out.println(prefix() + "Error in send zayav"
                                            + "\n-> NZayzav: " + nzayav
                                            + "\n-> Exception: " + ex.getLocalizedMessage()
                                            + "\n-> Error line: " + ex.getStackTrace()[0].getLineNumber());
                                }
                                this.cancel();
                            }
                        }, (((60000 * 60) * 24) * 2)); // Отправлять через 3 дня после принятия заявки
                    }
                    if (data.contains("#OPROS")) {
                        if (DataBase.isPersonal((long) fromid) == false) {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("У вас недостаточно прав!"));
                            return;
                        }
                        final Integer nz = Integer.parseInt(data.split("=")[1]), idfz = DataBase.getUidForZn(nz);
                        if (data.contains("#OPROS-M-Z=")) {
                            editMsg(id, msgid, getOprButton("УТРОМ", nz));
                            DataBase.setZFields(nz, "status", "Завершена");
                            int h = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
                            if (h >= 9 && h <= 12) {
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (DataBase.getZFields(nz, "status").equals("ПринятаМенеджером")
                                                    || DataBase.getZFields(nz, "status").equals("Завершена")) {
                                                sendOpros((long) idfz, nz);
                                                System.out.println(prefix() + "Отправлен опрос по заявке №" + nz
                                                        + " клиенту " + DataBase.getUserName(idfz) + "(" + idfz + ")");
                                            }
                                        } catch (Exception ex) {
                                            System.out.println(prefix() + "Ошибка при отправке отпроса заявки"
                                                    + "\n-> Exception: " + ex.getLocalizedMessage()
                                                    + "\n-> Error line: " + ex.getStackTrace()[0].getLineNumber());
                                        }
                                        this.cancel();
                                    }
                                }, (100000)); //Отправить через некоторое время (1м)
                            } else {
                                String date1 = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                                DataBase.saveZInOpr(nz, Integer.parseInt(DataBase.getZFields(nz, "u_id")), addonedaytoday(date1), "");
                                System.out.println(prefix() + "Опрос по заявке №" + nz
                                        + " клиенту " + DataBase.getUserName(idfz) + "(" + idfz + ") ПЕРЕНЕСЁН НА ЗАВТРА НА УТРО");
                            }
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    deleteMsg(id, msgid);
                                    this.cancel();
                                }
                            }, 60000);
                        }
                        if (data.contains("#OPROS-T-Z=")) {
                            editMsg(id, msgid, getOprButton("НА2ДНЯ", nz));
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    deleteMsg(id, msgid);
                                    this.cancel();
                                }
                            }, 30000);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        int uid = Integer.valueOf(DataBase.getZFields(nz, "u_id"));
                                        if (DataBase.getZFields(Math.toIntExact(nz), "source").equals("Viber")) {
                                            sendToChanelOpros((long) nz,
                                                    DataBase.getZFields(nz, "theme"), DataBase.getZFields(nz, "model"),
                                                    DataBase.getVZUFileds(uid, "type"),
                                                    DataBase.getVZUFileds(uid, "name"),
                                                    DataBase.getVZUFileds(uid, "phone"),
                                                    DataBase.getVZUFileds(uid, "company_name"),
                                                    DataBase.getZFields(Math.toIntExact(nz), "source"));
                                        } else {
                                            sendToChanelOpros((long) nz,
                                                    DataBase.getZFields(nz, "theme"), DataBase.getZFields(nz, "model"),
                                                    DataBase.getUserType(uid), DataBase.getUserName(uid), DataBase.getUserPhone(uid),
                                                    DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(nz), "u_id")), "company_name"),
                                                    DataBase.getZFields(Math.toIntExact(nz), "source"));
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    this.cancel();
                                }
                            }, 60000); // Отправить через два дня
                            System.out.println(prefix() + "Opros z №" + nz
                                    + " klient " + DataBase.getUserName(idfz) + "(" + idfz + ") perenos in two day");
                        }
                        if (data.contains("#OPROS-C-Z=")) {
                            editMsg(id, msgid, getOprButton("cancel ❌❌❌", nz));
                            DataBase.setZFields(nz, "status", "Завершена");
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    deleteMsg(id, msgid);
                                    this.cancel();
                                }
                            }, 60000);
                        }
                    }
                    if (data.contains("#Rate")) {
                        Integer rate = Integer.parseInt(data.split("=")[1]), zn = Integer.parseInt(data.split("=")[2]);
                        handRate(zn, rate, id, msgid, "Telegram");
                        return;
                    }
                    if (data.equals("#SaveZayav")) {
                        user_tema.put(id, "Заправка картриджа");
                        saveZayav(id, null, "");
                    }
                    if (data.startsWith("#Reklamaciya")) {
                        Integer nz = pi(data.split("/")[1]);
                        if (!DataBase.getZDescriptions((long) nz).contains("REKLAMACIYA_USE")) {
                            DataBase.setUserStr("action", fromid, "user_wait_reklam_comm/" + nz);
                            sendMsgToUser(fromid.longValue(), "Опишите дефект/поломку как можно более точнее и отправьте мне.", "backtomain");
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Опишите дефект/поломку"));
                            editMsg(id, e.getCallbackQuery().getMessage().getMessageId(), getButText("Опишите дефект/поломку"));
                            return;
                        } else {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Вы уже подавали рекламацию по этой заявке!"));
                        }
                    }
                    if (data.startsWith("#CancelZayavka")) {
                        Integer nz = pi(data.split("/")[1]);
                        if (!DataBase.getZDescriptions((long) nz).contains("CANCEL_USE")) {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Заявка на отмену заявки подана"));
                            editMsg(id, e.getCallbackQuery().getMessage().getMessageId(), getButText("Заявка на отмену заявки отправлена"));
                            DataBase.addZDescriptions((long) nz, "CANCEL_USE");
                            sendCancelZayav((long) nz);
                        } else {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Вы уже подавали заявку на отмену по этой заявке!"));
                        }
                    }
                    if (data.startsWith("#REKLAM_HAND")) {
                        Integer nz = pi(data.split("/")[1]);
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Заявка на  рекламацию закрыта"));
                        // DataBase.setZFields(nz, "description", "REKLAMACIYA_USE/END/" + DataBase.getUserPhone(e.getCallbackQuery().getFrom().getId()));
                        DataBase.addZDescriptions((long) nz, "REKLAMACIYA-END-" + DataBase.getUserPhone(e.getCallbackQuery().getFrom().getId()));
                        updateZStatus(nz, "Заявка на рекламацию принята.", "Заявка на рекламацию принята менеджером! Ожидайте звонка...");
                        editMsg(id, e.getCallbackQuery().getMessage().getMessageId(), getObrobotkaTrueButton(null));

                    }
                    if (data.startsWith("#CONFIRM_CANCEL_ZAYAV")) {
                        Integer nz = pi(data.split("/")[1]);
                        if (!DataBase.getZDescriptions((long) nz).contains("CANCEL_CONFIRM")) {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Заявка отменена."));
                            DataBase.addZDescriptions((long) nz, "CANCEL_CONFIRM");
                            DataBase.addZDescriptions((long) nz, "CANCEL_CONFIRM-" + DataBase.getUserPhone(e.getCallbackQuery().getFrom().getId()));
                            DataBase.setZFields(nz, "status", "Завершена");
                            updateZStatus(nz, "Заявка отменена.", "❎Отмена заявки №" + nz + " успешно подтверждена менеджером.");
                            editMsg(id, e.getCallbackQuery().getMessage().getMessageId(), getButText("Заявка отменена. Отменил: " + e.getCallbackQuery().getFrom().getUserName()));
                            bot.deleteMsg(Long.parseLong(getZayavChannelID()), pi(DataBase.getZFields(nz, "main_msg_id")));
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    deleteMsg(id, e.getCallbackQuery().getMessage().getMessageId());
                                }
                            }, 30000);
                        } else {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Отмена заявки уже подтверждена."));
                        }
                    }
                    if (data.startsWith("#OTKLON_CANCEL_ZAYAV")) {
                        Integer nz = pi(data.split("/")[1]);
                        if (!DataBase.getZDescriptions((long) nz).contains("OTKLON_CANCEL")) {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Вы отклонили отмену заявки.."));
                            DataBase.addZDescriptions((long) nz, "OTKLON_CANCEL");
                            DataBase.addZDescriptions((long) nz, "OTKLON_CANCEL-" + DataBase.getUserPhone(e.getCallbackQuery().getFrom().getId()));
                            updateZStatus(nz, "Отмена заявки отклонена.", "❌Отмена заявки №" + nz + " не была подтверждена менеджером.");
                            editMsg(id, e.getCallbackQuery().getMessage().getMessageId(), getButText("Отмена заявки отклонена. Отклонил: " + e.getCallbackQuery().getFrom().getUserName()));
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    deleteMsg(id, e.getCallbackQuery().getMessage().getMessageId());
                                }
                            }, 30000);
                        } else {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Отклонение отмены уже сделано."));
                        }
                    }
                    if (data.contains("#SEND=NOW=ALL=")) {
                        sendMsgToUser(id, "Запрос на рассылку ВСЕМ ПОЛЬЗОВАТЕЛЯМ вашего сообщения ПРИНЯТ! Ожидайте...");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("ПРИНЯТО"));
                        Integer nv = pi(data.split("=")[3]);
                        DataBase.setNLFields(nv, "type", "ALL");
                        startAllNewsLetter(nv);
                        adm_send_all.remove(id);
                        return;
                    }
                    if (data.contains("#SEND=DATE=ALL=")) {
                        sendMsgToUser(id, "Запрос на отправку ВСЕМ по ДАТЕ");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("ПРИНЯТО"));
                        Integer nv = pi(data.split("=")[3]);
                        news_id_d.put(id, nv);
                        news_wait_date.put(id, true);
                        adm_send_all.remove(id);
                        return;
                    }
                    if (data.contains("#SEND=NOW=ALL=")) {
                        sendMsgToUser(id, "Запрос на рассылку ВСЕМ ПОЛЬЗОВАТЕЛЯМ вашего сообщения ПРИНЯТ! Ожидайте...");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("ПРИНЯТО"));
                        Integer nv = pi(data.split("=")[3]);
                        DataBase.setNLFields(nv, "type", "ALLZ");
                        startAllNewsLetter(nv);
                        adm_send_all.remove(id);
                        return;
                    }
                    if (data.contains("#SendMsgForNz=")) {
                        int nz = pi(data.split("=")[1]);
                        if (DataBase.getZFields(nz, "source").equals("Viber")) {
                            URL oracle = new URL("https://ay.dn.ua/ViberBot/src/ChangeStatus.php?dark_id=" + DataBase.getZFields(nz, "u_id") + "&zn=" + nz + "&status=vosst&key=1337&text="
                                    + e.getCallbackQuery().getMessage().getText().replaceAll(" ", "%20").replaceAll("\\s", "%5Cn"));
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(oracle.openStream()));
                            String inputLine;
                            while ((inputLine = in.readLine()) != null)
                                System.out.println(inputLine);
                            in.close();
                        } else {
                            last_vost_msg.put((long) fromid, sendMsgToUser(Long.parseLong(DataBase.getZFields(nz, "u_id")), e.getCallbackQuery().getMessage().getText(), "yesno=" + nz));
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (DataBase.getZDescriptions((long) nz).contains("VOSST_USE")) {
                                        this.cancel();
                                        return;
                                    }
                                    deleteMsg(Long.parseLong(DataBase.getZFields(nz, "u_id")), last_vost_msg.get((long) fromid));
                                    last_vost_msg.put((long) fromid, sendMsgToUser(Long.parseLong(DataBase.getZFields(nz, "u_id")), e.getCallbackQuery().getMessage().getText(), "yesno=" + nz));
                                }
                            }, 60000 * 5, 60000 * 13);
                        }
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId())
                                .setText("Ваш текст успешно отправлен " + DataBase.getUserName(pi(DataBase.getZFields(nz, "u_id")))));
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), "Отправлен текст на согласование по заявке #" + nz + "\nТекст: " + e.getCallbackQuery().getMessage().getText());
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getM1Button(nz));
                        return;
                    }
                    if (data.contains("#SoglasVoss")) {
                        if (DataBase.isPersonal((long) fromid) == false) {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("У вас недостаточно прав!"));
                            return;
                        }
                        int nz = pi(e.getCallbackQuery().getMessage().getText().split("/")[1].replaceAll("#Z", ""));
                        sendMsgToUser((long) e.getCallbackQuery().getFrom().getId(), "---------------------------------");
                        sendMsgToUser((long) e.getCallbackQuery().getFrom().getId(), "Заявка №/" + nz + "/"
                                + "\nТема: " + DataBase.getZFields(nz, "theme")
                                + "\nМодель: " + DataBase.getZFields(nz, "model")
                                + "\nНазвание: " + DataBase.getZFields(nz, "name")
                                + "\nНомер телефона: " + DataBase.getZFields(nz, "phone")
                                + "\nСтатус: Принята менеджером" + "\nФормат отправки сообщения: /НОМЕР ЗАЯВКИ/ВАШ ТЕКСТ");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getObrobotkaTrueTrueButton());
                        return;
                    }
                    if (data.startsWith("#ChangeZStatus")) {
                        Integer nz = pi(e.getCallbackQuery().getMessage().getText().split("/")[1].replaceAll("#Z", ""));
                        String status = data.split("/")[1].replace("_", " ").toLowerCase();
                        if (status.equals("сбор в пути") || status.equals("3")) {
                            bot.updateZStatus(nz, "Курьер выехал к Вам (Сбор)", "Курьер выехал к Вам забрать картридж/принтер, будет у Вас в течении 1-2 часов. Пожалуйста ожидайте.");
                        }
                        if (status.equals("заявка в работе") || status.equals("4")) {
                            bot.updateZStatus(nz, "Заявка в работе", "Ваша заявка в работе, пожалуйста ожидайте.");
                        }
                        if (status.equals("доставка в пути") || status.equals("5")) {
                            bot.updateZStatus(nz, "Курьер выехал к Вам (Доставка)", "Курьер везет Вам картридж/принтер, будет у Вас в течении 1-2 часов. Пожалуйста ожидайте.");
                        }
                        try {
                            editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getObrobotkaTrueButton(nz));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Статус заявки изменён."));
                        return;
                    }
                    if (data.equals("#ENDZAYAV") || data.contains("#ENDZFORVOSST")) {
                        if (DataBase.isPersonal((long) fromid) == false) {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("У вас недостаточно прав!"));
                            return;
                        }
                        int nz = 0;
                        if (data.equals("#ENDZAYAV"))
                            nz = pi(e.getCallbackQuery().getMessage().getText().split("/")[1].replaceAll("#Z", ""));
                        else nz = pi(data.split("/")[1]);
                        int finalNz = nz;
                        DataBase.setZFields(nz, "status", "Завершена");
                        new Timer().schedule(new TimerTask() { // Таймер для опроса
                            @Override
                            public void run() {
                                try {
                                    int zn = finalNz, uid = Integer.parseInt(DataBase.getZFields(zn, "u_id"));
                                    bot.deleteMsg(Long.parseLong(getZayavChannelID()), pi(DataBase.getZFields(zn, "main_msg_id")));
                                    if (DataBase.getZFields(Math.toIntExact(zn), "source").equals("Viber")) {
                                        sendToChanelOpros((long) zn,
                                                DataBase.getZFields(zn, "theme"), DataBase.getZFields(zn, "model"),
                                                DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(zn), "u_id")), "type"),
                                                DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(zn), "u_id")), "name"),
                                                DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(zn), "u_id")), "phone"),
                                                DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(zn), "u_id")), "company_name"),
                                                DataBase.getZFields(Math.toIntExact(zn), "source"));
                                    } else {
                                        sendToChanelOpros((long) zn,
                                                DataBase.getZFields(zn, "theme"), DataBase.getZFields(zn, "model"),
                                                DataBase.getUserType(uid), DataBase.getUserName(uid), DataBase.getUserPhone(uid),
                                                DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(zn), "u_id")), "company_name"),
                                                DataBase.getZFields(Math.toIntExact(zn), "source"));
                                    }
                                    String s = DataBase.getZFields(zn, "msg_ids");
                                    if (s != null) {
                                        s += "," + id + "!" + msgid;
                                    } else {
                                        s = "" + id + "!" + msgid;
                                    }
                                    DataBase.setZFields(zn, "msg_ids", s);
                                    System.out.println(prefix() + "Z №" + finalNz + " end! Prinimal: " + DataBase.getPerFields((long) fromid, "position")
                                            + " " + DataBase.getPerFields((long) fromid, "name") + " Oprosit?");
                                } catch (Exception ex) {
                                    System.out.println(prefix() + "Error in send zayav"
                                            + "\n-> NZayzav: " + finalNz
                                            + "\n-> Exception: " + ex.getLocalizedMessage()
                                            + "\n-> Error line: " + ex.getStackTrace()[0].getLineNumber());
                                }
                                this.cancel();
                            }
                        }, 5000);
                        updateZStatus(nz, "Заявка завершена", "Заявка №" + nz + " завершена.");
                        editMsg(id, msgid, "Заявка №" + nz + " завершена принудительно! Завершил: " + DataBase.getPerFields((long) e.getCallbackQuery().getFrom().getId(), "name"));
                        sendToLogChanel("Пользователь " + DataBase.getPerFields((long) e.getCallbackQuery().getFrom().getId(), "name") + " принудительно завершил заявку №" + nz + "!");
                        return;
                    }
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
                        handlOstUsZaprZayav(null, (long) e.getCallbackQuery().getFrom().getId());
                        return;
                    }
                    if (data.contains("#SOGLASOVANO")) {
                        handVosst(pi(data.split("=")[1]), "Telegram", "SOGLASOVANO");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getTTTButton());
                        return;
                    }
                    if (data.contains("#ByNewKart")) {
                        handVosst(pi(data.split("=")[1]), "Telegram", "ByNewCart");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getTTTTButton());
                        return;
                    }
                    if (data.contains("#MENED")) {
                        handVosst(pi(data.split("=")[1]), "Telegram", "MENED");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getTTTTButton());
                        return;
                    }
                    if (data.equals("#SEND_DR")) {
                        adm_send_dr.put(e.getCallbackQuery().getFrom().getId(), true);
                        sendMsgToUser(Long.valueOf(e.getCallbackQuery().getFrom().getId()), "Пришлите мне текст/картинку для обновления рассылки *ДР*");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Пришлите мне текс/картинку!"));
                        return;
                    }
                    if (data.equals("#SEND_CHASTOT")) {
                        adm_send_chas.put(e.getCallbackQuery().getFrom().getId(), true);
                        sendMsgToUser(Long.valueOf(e.getCallbackQuery().getFrom().getId()), "Пришлите мне текст/картинку для обновления рассылки *ЧАСТОТА ЗАПРАВКИ*");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Пришлите мне текс/картинку!"));
                        return;
                    }
                    if (data.equals("#SEND_ALL")) {
                        sendMsg("" + e.getCallbackQuery().getFrom().getId(), "Выберите в какой мессенджер делать рассылку:", getWhatMessenger());
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Выберите мессенджер"));
                        return;
                    }
                    if (data.equals("#SEND_TO_VIBER")) {
                        adm_send_all.put(e.getCallbackQuery().getFrom().getId(), true);
                        admin_sall_type.put(Long.valueOf(e.getCallbackQuery().getFrom().getId()), "Viber");
                        sendMsg(e.getCallbackQuery().getFrom().getId(), "Пришлите мне текст/фотографию для отправки его всем подписчикам нашего Viber - бота.", "NONE");
                        return;
                    }
                    if (data.equals("#SEND_TO_TELEGRAM")) {
                        adm_send_all.put(e.getCallbackQuery().getFrom().getId(), true);
                        admin_sall_type.put(Long.valueOf(e.getCallbackQuery().getFrom().getId()), "Telegram");
                        sendMsg(e.getCallbackQuery().getFrom().getId(), "Пришлите мне текст/фотографию для отправки его всем подписчикам нашего Telegram - бота.", "NONE");
                        return;
                    }
                    if (data.equals("#SEND_TO_ALL")) {
                        adm_send_all.put(e.getCallbackQuery().getFrom().getId(), true);
                        admin_sall_type.put(Long.valueOf(e.getCallbackQuery().getFrom().getId()), "ALL");
                        sendMsg(e.getCallbackQuery().getFrom().getId(), "Пришлите мне текст/фотографию для отправки его всем подписчикам нашего Telegram и Viber ботов.", "NONE");
                        return;
                    }
                    if (data.equals("#SEND_ID")) {
                        adm_send_id.put(e.getCallbackQuery().getFrom().getId(), true);
                        sendMsgToUser(Long.valueOf(e.getCallbackQuery().getFrom().getId()), "Пришлите мне текст/картинку/файл!");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Пришлите мне текс/картинку/файл!"));
                    }
               /* if (data.equals("#SEND_KARTR")) {
                }
                if (data.equals("#SEND_ZONA")) {
                }
                if (data.equals("#SEND_ADRES")) {
                }*/
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
                        handlOstUsZaprZayav(e.getCallbackQuery().getMessage(), (long) fromid);
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
                        sendMsgToUser((long) e.getCallbackQuery().getFrom().getId(), "Продолжайте :-)");
                        if (user_tema.get((long) e.getCallbackQuery().getFrom().getId()).equals("Заправка картриджа"))
                            handlOstUsZaprZayav(e.getCallbackQuery().getMessage(), (long) e.getCallbackQuery().getFrom().getId());
                        else
                            handlOstUsRemZayav(e.getCallbackQuery().getMessage(), (long) e.getCallbackQuery().getFrom().getId());
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
                        saveZayav((long) frid, null, "-_-");
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
                        saveZayav((long) Math.toIntExact(fromid), null, null);
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), "Вы подали заявку!");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getButText("Вы подали заявку!"));
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Заявка принята!"));
                        return;
                    }
                    if (data.equals("#ИзменитьАдрес")) {
                        user_wait_adress.put(id, true);
                        user_edit_adress.put(id, true);
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), "Вы изменили адрес!");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getButText("Вы изменили адрес"));
                        sendMsg(fromid, DataBase.getUserName(Math.toIntExact(id)) + ", укажите пожалуйста адрес в поле ввода и отправьте мне, либо выберите подходящий вариант ниже.Адрес нужен для выезда курьера за заявкой. ", "adress");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Укажите новый адрес :-)"));
                        return;
                    }
                    if (data.equals("#ИзменитьМодель")) {
                        user_wait_model.put(id, true);
                        user_edit_model.put(id, true);
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), "Вы изменили модель!");
                        editMsg(e.getCallbackQuery().getMessage().getChatId(), e.getCallbackQuery().getMessage().getMessageId(), getButText("Вы изменили адрес"));
                        sendZOstZ(id, null);
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Введите новую модель!"));
                        return;
                    }
                    if (data.equals("#ICZA")) {
                        if (DataBase.isPersonal((long) fromid) == false) {
                            answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("У вас недостаточно прав!"));
                            return;
                        }
                        admin_wait_phtx.put(fromid, true);
                        sendMsg(fromid, prefix() + "Пришлите мне картинку/текст либо картинку с текстом в подписи в виде файла!", "null");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Пришлите картинку/текст!"));
                        return;
                    }
                    if (data.equals("#ADM_SEND_ID")) {
                        sendMsgToUser((long) fromid, "Напишите ID пользователей через разделитель '/'.Если ID один не используйте разделитель.\nПример: 556349297/521258581/917045001");
                        admin_wait_values.put(fromid, true);
                        admin_wait_type.put((long) fromid, "ID");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Напишите ID"));
                        return;
                    }
                    if (data.equals("#ADM_SEND_ZONE")) {
                        sendMsgToUser((long) fromid, "Напишите номера зон через разделитель '/'.Если зона одна не используйте разделитель.\nПример: 5.1/5.2/8/3");
                        admin_wait_values.put(fromid, true);
                        admin_wait_type.put((long) fromid, "ZONE");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Напишите ZONE"));
                        return;
                    }
                    if (data.equals("#ADM_SEND_ADRESS")) {
                        sendMsgToUser((long) fromid, "Напишите адреса пользователей через разделитель '/'.Если адресс один не используйте разделитель.\nПример: Пушкинская/Рымарская 6/Научная");
                        admin_wait_values.put(fromid, true);
                        admin_wait_type.put((long) fromid, "ADRESS");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Напишите ADRESS"));
                        return;
                    }
                    if (data.equals("#ADM_SEND_CARTRIDGE")) {
                        sendMsgToUser((long) fromid, "Напишите модели картриджей пользователей через разделитель '/'.Если картридж один не используйте разделитель.\nПример: Canon/3010/HP 4180");
                        admin_wait_values.put(fromid, true);
                        admin_wait_type.put((long) fromid, "CARTRIDGE");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Напишите CARTRIDGE"));
                        return;
                    }
                    if (data.equals("#ADM_SEND_СANCEL")) {
                        sendMsgToUser((long) fromid, "Рассылка ICZA отменена.");
                        answerCallbackQuery(new AnswerCallbackQuery().setCallbackQueryId(e.getCallbackQuery().getId()).setText("Рассылка ICZA отменена."));
                        admin_wait_phtx.remove(fromid);
                        admin_phtx.remove(fromid);
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
        try {
            deleteMsg(Long.parseLong(DataBase.getZFields(zid, "u_id")), DataBase.getZStatusMsgID((long) zid));
        } catch (Exception ignore) {
        }
        String current_statuses = "ℹ️ Заявка №" + zid;
        for (String status : DataBase.getZStatuses((long) zid))
            current_statuses += "\n" + status;
        current_statuses += "\n\uD83D\uDC49 Текущий статус: " + longstatus;
        DataBase.addZStatuses((long) zid, bot.u.getDate("dd/MM/YY HH:mm:ss") + " " + smallstatus);
        DataBase.setZStatusMsgID((long) zid, sendMsgToUser(Long.parseLong(DataBase.getZFields(zid, "u_id")), current_statuses));
    }

    public static Boolean handZayav(Long id, Message msg, String txt) {
        if (user_wait_model.containsKey(id) && user_wait_model.get(id) == true) {
            if (menus.contains(txt)) {
                sendMsgToUser(id, "Вы уже оставляете заявку! Пожалуйста укажите модель принтера!");
                return true;
            }
            if (txt.equals("Главное меню")) {
                user_wait_model.remove(id);
                user_wait_adress.remove(id);
                bot.sendMsg(msg, "Главное меню", "main");
                return true;
            }
            user_wait_model.put(id, false);
            user_model.put(id, txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
            if (DataBase.isCorporationWorker(id)) {
                String true_modelcorp = DataBase.getUserStr("true_models" + (!user_tema.get(id).equals("Заправка картриджа") ? "_rem" : ""), id);
                List<String> true_mod = new ArrayList<String>();
                if (true_modelcorp != null)
                    true_mod = new ArrayList<String>(Arrays.asList(true_modelcorp.split(";")));
                String model = txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "").replaceAll(";", ":");
                if (!true_mod.contains(model))
                    true_mod.add(model);
                DataBase.setUserStr("true_models" + (!user_tema.get(id).equals("Заправка картриджа") ? "_rem" : ""), Math.toIntExact(id), bot.u.stringToString(true_mod, ";"));
                bot.sendMsgToUser(id, "Данные заявки успешно заполнены!", "backtomain");
                bot.sendMsg(msg, "Проверьте правильность ввода: "
                        + "\nВаш адрес: " + DataBase.getUsFileds(id, "adress")
                        + "\nВаша модель: " + model, "prov_info");
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
                bot.sendMsgToUser(id, "Данные заявки успешно заполнены!", "backtomain");
                String[] sp = DataBase.getUsFileds(id, "last_model").split("&&&");
                bot.sendMsg(msg, "Проверьте правильность ввода: "
                        + "\nВаш адрес: " + DataBase.getUsFileds(id, "adress")
                        + "\nВаша модель: " + user_model.get(id), "prov_info");
            } else {
                if (DataBase.getUsFileds(id, "last_model_rem") != null && !DataBase.getUsFileds(id, "last_model_rem").equals("Не указано")) {
                    if (!DataBase.getUsFileds(id, "last_model_rem").split("&&&")[0].equals(txt))
                        DataBase.setUsFields(id, "last_model_rem", txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "") + "&&&" + DataBase.getUsFileds(id, "last_model_rem").split("&&&")[0]);
                } else if (!DataBase.getUsFileds(id, "last_model_rem").split("&&&")[0].equals(txt)) {
                    DataBase.setUsFields(id, "last_model_rem", txt.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", ""));
                }
                String[] sp = DataBase.getUsFileds(id, "last_model_rem").split("&&&");
                bot.sendMsg(msg, "Проверьте правильность ввода: "
                        + "\nВаш адрес: " + DataBase.getUsFileds(id, "adress")
                        + "\nВаша модель/дефект: " + user_model.get(id), "prov_info");
            }
            return true;
        }
        return false;
    }

    public static void saveZayav(Long id, Message msg, String txt) {
        try {
            int znum = DataBase.getZNum();
            if (DataBase.isCorporationWorker(id)) {
                Corporation corp = new Corporation(DataBase.getCorporationID(id));
                corp.addOrderID((long) znum);
            }
            DataBase.addZForUser(Math.toIntExact(id), znum);
            DataBase.saveZayavka(znum, user_tema.get(id), bot.u.correctStr(user_model.get(id)), DataBase.getUserName(Math.toIntExact(id)),
                    DataBase.getUserPhone(Math.toIntExact(id)), DataBase.getUserDopPhone(Math.toIntExact(id)),
                    Math.toIntExact(id), "Поступила", bot.u.getDate("dd/MM/yyyy"), bot.u.getTime("HH:mm:ss"));
            DataBase.setZFields(znum, "adress", DataBase.getUsFileds(id, "adress"));
            DataBase.setZFields(znum, "source", "Telegram");
            bot.sendToChanel((long) znum, user_tema.get(id), user_model.get(id).replaceAll("[^A-Za-zА-Яа-я0-9 ]", ""),
                    DataBase.getUserType(Math.toIntExact(id)), DataBase.isCorporationWorker(id) ? DataBase.getUserName(Math.toIntExact(id)) + "\nКомпания: #" + new Corporation(DataBase.getCorporationID(id)).getName() : DataBase.getUserName(Math.toIntExact(id)),
                    DataBase.getUserPhone(Math.toIntExact(id)));
            System.out.println(prefix() + "Z №" + znum + "! Тheme: " + DataBase.getZFields(znum, "theme")
                    + " Klient: " + DataBase.getUserType(Math.toIntExact(id)) + " " + DataBase.getUserName(Math.toIntExact(id))
                    + " Phone: " + DataBase.getUserPhone(Math.toIntExact(id)));
            bot.updateZStatus(znum, "Заявка подана.", "Заявка " + znum + " успешно передана нашему менеджеру! В скором времени он свяжется с Вами!");
            DataBase.setZNum(znum);
            DataBase.setUsFields(id, "last_z_id", znum);
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
            if (admin_data.containsKey(id)) {
                String[] str = admin_data.get(id).split("//");
                DataBase.setUsFields(id, "company_name", str[0]);
                DataBase.setUsFields(id, "name", str[1]);
                DataBase.setUsFields(id, "phone", str[2]);
                admin_data.remove(id);
            }
        } catch (Exception e) {
            System.out.println(prefix() + "Error in send zayav."
                    + "\n-> ID: " + id
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
        sendZOstZ(id, msg);
        return;
    }

    public static void sendZOstZ(Long id, Message msg) {
        if (bot.user_tema.get(id).equals("Заправка картриджа")) {
            if (msg != null)
                bot.sendMsg(msg, "Укажите модель картриджа(ей)/принтера(ов) и количество штук.", "model");
            else
                bot.sendMsg(Math.toIntExact(id), "Укажите модель картриджа(ей)/принтера(ов) и количество штук.", "model");
        } else if (msg != null)
            bot.sendMsg(msg, "Укажите модель принтера и кратко опишите дефект/поломку если она присутсвует.", "model");
        else
            bot.sendMsg(Math.toIntExact(id), "Укажите модель принтера и кратко опишите дефект/поломку если она присутсвует.", "model");
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

    public void handleMenu(String menu, SendMessage s, Long user_id) {
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
            if (menu.contains("yesno"))
                s.setReplyMarkup(getSoglasButton(pi(menu.split("=")[1])));
            if (menu.equals("main")) {
                addMainMenu(s);
                if (DataBase.isPersonal(user_id)) {
                    String vc = DataBase.getPerFields(user_id, "v_id");
                    if (vc.equals("manager") || vc.equals("admin") || vc.equals("owner"))
                        addMainPersonalMenu(s);
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
                addModelMenu(s, user_id);
            }
            if (menu.equals("adress")) {
                addAdressMenu(s);
            }
            if (menu.contains("send_napom")) {
                s.setReplyMarkup(getNapomButtons(pi(menu.split("=")[1]), menu.split("=")[2]));
            }
            if (menu.contains("vosst")) {
                s.setReplyMarkup(getMButton(pi(menu.split("=")[1])));
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
        }
    }

    public void handVosst(int zn, String source, String type) {
        try {
            deleteMsg(Long.parseLong(getZayavChannelID()), pi(DataBase.getZFields(zn, "main_msg_id")));
            int uid = pi(DataBase.getZFields(zn, "u_id"));
            DataBase.addZDescriptions((long) zn, "VOSST_USE");
            String username = source.equals("Viber") ? DataBase.getVZUFileds(uid, "name") : DataBase.getUserName(uid);
            String userphone = source.equals("Viber") ? DataBase.getVZUFileds(uid, "phone") : DataBase.getUserName(uid);
            String text = "";
            if (type.equals("SOGLASOVANO")) {
                text = "Заявка #Z/" + zn + "/ на #" + DataBase.getZFields(zn, "theme").split(" ")[0]
                        + "\nМодель: #" + DataBase.getZFields(zn, "model").replaceAll(" ", "_")
                        + "\nАдрес: #" + DataBase.getZFields(zn, "adress").replaceAll(" ", "_")
                        + "\nСтатус: #" + DataBase.getZFields(zn, "status")
                        + "\nПринимал: #" + DataBase.getZFields(zn, "prinyal")
                        + "\n --->> ВОССТАНОВЛЕНИЕ - СОГЛАСОВАНО \uD83E\uDD11 \uD83E\uDD11 \uD83E\uDD11  "
                        + "\nКлиент: " + username
                        + "\nНомер телефона: " + userphone
                        + "\n--> Клиент согласился на восстановление картриджа! Свяжитесь с ним!";
            }
            if (type.equals("MENED")) {
                text = "Заявка #Z/" + zn + "/ на #" + DataBase.getZFields(zn, "theme").split(" ")[0]
                        + "\nМодель: #" + DataBase.getZFields(zn, "model").replaceAll(" ", "_")
                        + "\nАдрес: #" + DataBase.getZFields(zn, "adress").replaceAll(" ", "_")
                        + "\nСтатус: #" + DataBase.getZFields(zn, "status")
                        + "\nПринимал: #" + DataBase.getZFields(zn, "prinyal")
                        + "\n --->> ВОССТАНОВЛЕНИЕ - ПЕРЕЗВОНИТЬ ☎️ ☎️ ☎️"
                        + "\nКлиент: " + username
                        + "\nНомер телефона: " + userphone
                        + "\n--> Клиент жаждит поговорить с менеджером! Свяжитесь с ним!";
            }
            if (type.equals("ByNewCart")) {
                text = "Заявка #Z/" + zn + "/ на #" + DataBase.getZFields(zn, "theme").split(" ")[0]
                        + "\nМодель: #" + DataBase.getZFields(zn, "model").replaceAll(" ", "_")
                        + "\nАдрес: #" + DataBase.getZFields(zn, "adress").replaceAll(" ", "_")
                        + "\nСтатус: #" + DataBase.getZFields(zn, "status")
                        + "\nПринимал: #" + DataBase.getZFields(zn, "prinyal")
                        + "\n --->> ВОССТАНОВЛЕНИЕ - ПОКУПКА \uD83D\uDCB0 \uD83D\uDCB0 \uD83D\uDCB0 "
                        + "\nКлиент: " + username
                        + "\nНомер телефона: " + userphone
                        + "\n--> Клиентх хочет приобрести картридж! Свяжитесь с ним!";
            }
            SendMessage s = new SendMessage().setChatId(getZayavChannelID()).setText(text);
            s.setReplyMarkup(getEndZayavButton(zn));
            try {
                int msgid1 = 5;
                msgid1 = sendMessage(s).getMessageId();
                DataBase.setZFields(zn, "main_msg_id", "" + msgid1);
                String ss = DataBase.getZFields(Math.toIntExact(zn), "msg_ids");
                if (ss != null) {
                    ss += "," + getZayavChannelID() + "!" + 6;
                } else {
                    ss = "" + getZayavChannelID() + "!" + msgid1;
                }
                DataBase.setZFields(Math.toIntExact(zn), "msg_ids", ss);
                //editMsg(Long.parseLong(getZayavChannelID()), msgid1, getTTTButton());
            } catch (Exception E) {

            }
        } catch (Exception e) {
        }
    }

    public void handRate(int zn, int rate, long id, int msgid, String source) {
        DataBase.setZFields(zn, "rate", rate + "");
        DataBase.setZFields(zn, "status", "Завершена|Опрошена");
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
            String s = DataBase.getZFields(zn, "msg_ids");
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
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (rate <= 4) {
                    sendToChanel((long) zn);
                }
                if (DataBase.getZFields(zn, "status").equals("Завершена|Опрошена")) {
                    DataBase.setZFields(zn, "status", "Завершена|Опрошена|Архив");
                    String msggg = DataBase.getZFields(zn, "msg_ids");

                    sendToArch(zn);
                    deleteMsg(Long.parseLong(getZayavChannelID()), pi(DataBase.getZFields(zn, "main_msg_id")));
                    System.out.println(prefix() + "Заявка успешно перенесена в архив");
                    if (msggg != null && !msggg.equals("NULL")) {
                        String[] msgs = DataBase.getZFields(zn, "msg_ids").split(",");
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

    public boolean handlOstUsZaprZayav(Message msg, Long id) {
        if (DataBase.isPersonal(id) && pers_is_z_saved.containsKey(id) == false) {
            sendMsg(Math.toIntExact(id), "Введите название компании/контактное лицо/контактный номер телефона и отправьте мне, после чего продолжите подавать заявку.", "cancelvvod");
            admin_data.put(id, DataBase.getUserStr("company_name", Math.toIntExact(id)) + "//" + DataBase.getUserStr("name", Math.toIntExact(id)) + "//" + DataBase.getUserStr("phone", Math.toIntExact(id)));
            pers_is_z_saved.put(id, true);
            return true;
        } else {
            pers_is_z_saved.remove(id);
        }
        if (DataBase.getAllBlackUserId().contains(Math.toIntExact(id))) {
            sendMsg(Math.toIntExact(id), "Извините, но Вам запрещено производить какие-либо действия! Для уточнения деталей напишите менеджеру...", "mened");
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
            user_tema.put(id, "Заправка картриджа");
            if (DataBase.getUserType(Math.toIntExact(id)).equals("Не указано")) {
                bot.user_enter_type(msg);
                return true;
            }
            user_wait_adress.put(id, true);
            sendMsg(Math.toIntExact(id), DataBase.getUserName(Math.toIntExact(id)) + ", укажите пожалуйста адрес в поле ввода и отправьте мне, либо выберите подходящий вариант ниже.Адрес нужен для выезда курьера за заявкой. ", "adress");
        }
        return true;
    }

    public boolean handlOstUsRemZayav(Message msg, Long id) {
        if (DataBase.isPersonal(id) && pers_is_z_saved.containsKey(id) == false) {
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
            s.setReplyMarkup(getSoglasButton(pi(menu.split("=")[1])));
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
            s.setReplyMarkup(getMButton(pi(menu.split("=")[1])));
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

    public void addMainAdminMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Управление рассылками"));
        oneline.add(new KeyboardButton("Управление QR-кодами"));
        twoline.add(new KeyboardButton("Управление корпорациями"));
        twoline.add(new KeyboardButton("Управление персоналом"));
        thrline.add(new KeyboardButton("Перейти в меню пользователя"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
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
        thrline.add(new KeyboardButton("Вернуться меню администратора"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
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
        oneline.add(new KeyboardButton("Информация о заявке(ах)"));
        oneline.add(new KeyboardButton("Команда manager №2"));
        twoline.add(new KeyboardButton("Команда manager №3"));
        twoline.add(new KeyboardButton("Команда manager №4"));
        thrline.add(new KeyboardButton("Перейти в меню пользователя"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
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
        two.add(new KeyboardButton("Вернуться в главное меню"));
        keyboard.add(one);
        keyboard.add(two);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void addAdressMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow one = new KeyboardRow(), two = new KeyboardRow(), thr = new KeyboardRow(), thx = new KeyboardRow();
        String city = DataBase.getUsFileds((Long.parseLong(sendMessage.getChatId())), "city");
        String adress = DataBase.getUsFileds(Long.parseLong(sendMessage.getChatId()), "adress"),
                lastadress = DataBase.getUsFileds((Long.parseLong(sendMessage.getChatId())), "last_adress");
        Long id = Long.parseLong(sendMessage.getChatId());
        if (DataBase.isCorporationWorker(id)) {
            Corporation corp = new Corporation(DataBase.getCorporationID(id));
            {
                if (corp.getAddresses().size() == 1) {
                    DataBase.setUsFields(id, "adress", corp.getAddresses().get(0));
                    user_wait_adress.remove(id);
                    sendMessage.setText("Укажите пожалуйста модель");
                    sendMsgToUser(id, "Автоматически установлен адрес " + corp.getAddresses().get(0), "model");
                    user_wait_model.put(id, true);
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
            inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
            String dateee = DataBase.getZFields(nz, "date");
            List<String> descriptions = DataBase.getZDescriptions((long) nz);
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

    public InlineKeyboardMarkup getSoglasButton(Integer msgid) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Да! Согласовываю!").setCallbackData("#SOGLASOVANO=" + msgid));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Хочу уточнить у менеджера.").setCallbackData("#MENED=" + msgid));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Купить новый картридж").setCallbackData("#ByNewKart=" + msgid));
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

    public InlineKeyboardMarkup getMButton(Integer nz) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Отправить").setCallbackData("#SendMsgForNz=" + nz));
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
        if (nz != null)
            statuses = DataBase.getZStatuses((long) nz);
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("\uD83D\uDFE0 \uD83D\uDFE0 \uD83D\uDFE0 - ЗАЯВКА ПРИНЯТА - \uD83D\uDFE0 \uD83D\uDFE0 \uD83D\uDFE0").setCallbackData("#Пофиг"));

        if (nz == null || !containsStatus(statuses, "Курьер выехал к Вам (Сбор)") && (!containsStatus(statuses, "Заявка в работе") && !containsStatus(statuses, "Курьер выехал к Вам (Доставка)")))
            keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Сбор").setCallbackData("#ChangeZStatus/Сбор_в_пути"));
        if (nz == null || !containsStatus(statuses, "Заявка в работе") && !containsStatus(statuses, "Курьер выехал к Вам (Доставка)"))
            keyboardButtonsRow2.add(new InlineKeyboardButton().setText("В работе").setCallbackData("#ChangeZStatus/Заявка_в_работе"));
        if (nz == null || !containsStatus(statuses, "Курьер выехал к Вам (Доставка)"))
            keyboardButtonsRow2.add(new InlineKeyboardButton().setText("СОГЛАСОВАТЬ").setCallbackData("#SoglasVoss"));
        if (nz == null || !containsStatus(statuses, "Курьер выехал к Вам (Доставка)"))
            keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Доставка").setCallbackData("#ChangeZStatus/Доставка_в_пути"));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText("ЗАВЕРШИТЬ").setCallbackData("#ENDZAYAV"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
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

    public InlineKeyboardMarkup getObrobotkaTrueTrueButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("\uD83D\uDFE0 \uD83D\uDFE0 \uD83D\uDFE0 - ЗАЯВКА ПРИНЯТА - \uD83D\uDFE0 \uD83D\uDFE0 \uD83D\uDFE0").setCallbackData("#Пофиг"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("ПЕРЕЙТИ В БОТА").setUrl("https://t.me/koi_servis_bot"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
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
                + "\nАдрес: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "adress").replaceAll(" ", "_").replaceAll(",", "_")
                + "\nТип: #" + type
                + (type.equals("Компания") ? "\nНазвание: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "company_name").replaceAll(" ", "_") : "")
                + "\nИмя: #" + name.replaceAll(" ", "_")
                + "\nНомер телефона: #Tel" + phone
                + (DataBase.isPersonal(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id"))) ? "\nПодал: " + DataBase.getPerFields(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "name") : "")
                + "\nСтатус: /Поступила/" + " #Поступила";
        SendMessage s = new SendMessage().setChatId(getZayavChannelID()).setText(text);
        s.setReplyMarkup(getObrobotkaButton(nza));
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
        SendMessage s = new SendMessage().setChatId("-1001143163268").setText(bot.u.getDate("[dd/MM/yyyy hh:mm:ss] -> ") + text);
        Integer msgid = 0;
        try {
            msgid = sendMessage(s).getMessageId();
            info("Отправлено");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return msgid;
    }

    public void sendToChanelReklamaciya(Long nza, String tema, String model, String type, String name, String phone) throws IOException {
        String text = "РЕКЛАМАЦИЯ /" + nza + "/" + "#Z" + nza
                + "\nТема: #" + tema
                + "\nМодель: #" + (model != null ? model.replaceAll(" ", "_") : "Не указано")
                + "\nАдрес: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "adress").replaceAll(" ", "_").replaceAll(",", "_")
                + "\nТип: #" + type
                + (type.equals("Компания") ? "\nНазвание: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "company_name").replaceAll(" ", "_") : "")
                + "\nИмя: #" + name.replaceAll(" ", "_")
                + "\nНомер телефона: #Tel" + phone
                + (DataBase.isPersonal(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id"))) ? "\nПодал: " + DataBase.getPerFields(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "name") : "")
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
        String text = "Заявка /#Z" + z_id + "/"
                + "\nТема: #" + (DataBase.getVZFileds(id, "theme").equals("zapravka") ? "Заправка картриджа" : "Ремонт принтера")
                + "\nМодель: #" + (DataBase.getVZFileds(id, "model"))
                + "\nАдрес: #" + (DataBase.getVZFileds(id, "adres").replaceAll(" ", "_"))
                + "\nИмя/Тип: #" + nametype
                + "\nНомер телефона: #Tel" + (DataBase.getVZUFileds(dark_id, "phone"))
                + "\nГород: #" + (DataBase.getZFields(Math.toIntExact(z_id), "city"))
                + "\nИсточник: #" + (DataBase.getZFields(Math.toIntExact(z_id), "source")) + " ✓"
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
        if (DataBase.getZFields(Math.toIntExact(nza), "source").equals("Viber")) {
            int uid = pi(DataBase.getZFields(Math.toIntExact(nza), "u_id"));
            text = "Заявка #/" + nza + "/ #Z" + nza
                    + "\nТема: #" + DataBase.getZFields(Math.toIntExact(nza), "theme")
                    + "\nДата и время подачи: #" + DataBase.getZFields(Math.toIntExact(nza), "date") + "  " + DataBase.getZFields(Math.toIntExact(nza), "time")
                    + "\nМодель: #" + DataBase.getZFields(Math.toIntExact(nza), "model").replaceAll(" ", "_")
                    + "\nАдрес: #" + DataBase.getVZUFileds(uid, "adress").replaceAll(" ", "_").replaceAll(",", "_")
                    + "\nТип: #" + DataBase.getVZUFileds(uid, "type")
                    + "\nКонтактное лицо: #" + DataBase.getVZUFileds(uid, "name").replaceAll(" ", "_")
                    + (DataBase.getVZUFileds(uid, "type").equals("Компания") ? "\nНазвание компании: #" + DataBase.getVZUFileds(uid, "company_name").replaceAll(" ", "_") : "")
                    + "\nНомер телефона: #Tel" + DataBase.getZFields(Math.toIntExact(nza), "phone")
                    + "\nОценка: #" + DataBase.getZFields(Math.toIntExact(nza), "rate")
                    + "\nКомментарий: #" + DataBase.getZFields(Math.toIntExact(nza), "description")
                    + "\nСтатус: #" + DataBase.getZFields(Math.toIntExact(nza), "status").replaceAll(" ", "_")
                    + "\nПринимал: #" + DataBase.getZFields(Math.toIntExact(nza), "city");
        } else {
            text = "Заявка #/" + nza + "/ #Z" + nza
                    + "\nТема: #" + DataBase.getZFields(Math.toIntExact(nza), "theme")
                    + "\nДата и время подачи: #" + DataBase.getZFields(Math.toIntExact(nza), "date") + "  " + DataBase.getZFields(Math.toIntExact(nza), "time")
                    + "\nМодель: #" + DataBase.getZFields(Math.toIntExact(nza), "model").replaceAll(" ", "_")
                    + "\nАдрес: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "adress").replaceAll(" ", "_").replaceAll(",", "_")
                    + "\nТип: #" + DataBase.getUserType(pi(DataBase.getZFields(Math.toIntExact(nza), "u_id")))
                    + "\nКонтактное лицо: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "name").replaceAll(" ", "_")
                    + (DataBase.getUserType(pi(DataBase.getZFields(Math.toIntExact(nza), "u_id"))).equals("Компания") ? "\nНазвание компании: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "name").replaceAll(" ", "_") : "")
                    + "\nНомер телефона: #Tel" + DataBase.getZFields(Math.toIntExact(nza), "phone")
                    + "\nОценка: #" + DataBase.getZFields(Math.toIntExact(nza), "rate")
                    + "\nКомментарий: #" + DataBase.getZFields(Math.toIntExact(nza), "description")
                    + "\nСтатус: #" + DataBase.getZFields(Math.toIntExact(nza), "status").replaceAll(" ", "_")
                    + "\nПринимал: #" + DataBase.getZFields(Math.toIntExact(nza), "city");
        }
        sendMsg(getCellChannelID(), text, getCellButton(nza));
    }

    public void sendCancelZayav(Long nza) {
        Integer nz = Math.toIntExact(nza);
        String text = "Заявка #/" + nza + "/ #Z" + nza
                + "\nТема: #" + DataBase.getZFields(nz, "theme")
                + "\nДата и время подачи: #" + DataBase.getZFields(nz, "date") + " " + DataBase.getZFields(nz, "time")
                + "\nМодель: #" + DataBase.getZFields(nz, "model").replaceAll(" ", "_")
                + "\nАдрес: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(nz, "u_id")), "adress").replaceAll(" ", "_").replaceAll(",", "_")
                + "\nТип: #" + DataBase.getUserType(pi(DataBase.getZFields(nz, "u_id")))
                + "\nКонтактное лицо: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(nz, "u_id")), "name").replaceAll(" ", "_")
                + (DataBase.getUserType(pi(DataBase.getZFields(nz, "u_id"))).equals("Компания") ? "\nНазвание компании: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(nz, "u_id")), "name").replaceAll(" ", "_") : "")
                + "\nНомер телефона: #Tel" + DataBase.getZFields(nz, "phone")
                + "\nСтатус: #" + DataBase.getZFields(nz, "status").replaceAll(" ", "_")
                + "\nПринимал: #" + DataBase.getZFields(nz, "prinyal")
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
        String type = DataBase.getZFields(nza, "source").equals("Viber") ? DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "type") : DataBase.getUserType(pi(DataBase.getZFields(Math.toIntExact(nza), "u_id")));
        String name = DataBase.getZFields(nza, "source").equals("Viber") ? DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "name") : DataBase.getUserName(pi(DataBase.getZFields(Math.toIntExact(nza), "u_id")));
        String сname = DataBase.getZFields(nza, "source").equals("Viber") ? DataBase.getVZUFileds(pi(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "company_name") : DataBase.getUserStr("company_name", pi(DataBase.getZFields(Math.toIntExact(nza), "u_id")));
        String text = "Заявка #/" + nza + "/ #Z" + nza
                + "\nТема: #" + DataBase.getZFields(Math.toIntExact(nza), "theme")
                + "\nДата подачи: #" + DataBase.getZFields(Math.toIntExact(nza), "date")
                + "\nВремя подачи: #" + DataBase.getZFields(Math.toIntExact(nza), "time")
                + "\nМодель: #" + DataBase.getZFields(Math.toIntExact(nza), "model").replaceAll(" ", "_")
                + "\nАдрес: #" + DataBase.getUsFileds(Long.parseLong(DataBase.getZFields(Math.toIntExact(nza), "u_id")), "adress").replaceAll(" ", "_")
                + "\nТип: #" + type
                + "\nКонтактное лицо: #" + name
                + (type.equals("Компания") ? "\nНазвание компании: #" + сname : "")
                + "\nНомер телефона: #Tel" + DataBase.getZFields(Math.toIntExact(nza), "phone")
                + "\nОценка: #" + DataBase.getZFields(Math.toIntExact(nza), "rate")
                + "\nКомментарий: #" + DataBase.getZFields(Math.toIntExact(nza), "description")
                + "\nСтатус: #" + DataBase.getZFields(Math.toIntExact(nza), "status").replaceAll(" ", "_")
                + "\nПринимал: #" + DataBase.getZFields(Math.toIntExact(nza), "city");
        sendMsg(getArchChannelID(), text, null);
    }

    public static String getArchChannelID() {
        return "-1001319666445";
    }

    public static String getContrChannelID() {
        return "-1001455850449";
    }

    public void sendOpros(Long id, Integer nz) throws TelegramApiException {
        String text = "Доброе утро." + DataBase.getZFields(nz, "date") + " Вы оставляли заявку №"
                + nz + " на " + (DataBase.getZFields(nz, "theme").contains("Заправка") ? "заправку картриджа" : "ремонт принтера") + "\n" +
                "Пожалуйста оцените работу нашего сервиса по шкале от 0 до 7.\n";
        if (DataBase.getZFields(nz, "source").equals("Viber")) {
            try {
                URL oracle = new URL("https://ay.dn.ua/ViberBot/src/ChangeStatus.php?dark_id=" + DataBase.getZFields(nz, "u_id") + "&zn=" + nz + "&status=opros&key=1337&text="
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
        String ss = DataBase.getZFields(Math.toIntExact(nza), "msg_ids");
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


}