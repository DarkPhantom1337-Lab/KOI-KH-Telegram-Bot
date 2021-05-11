package ua.darkphantom1337.koi.kh;

public class DeprecatedCodes {

    /*
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
                                            .setChatId((long) i).setReplyMarkup(InlineButtons.getNapomDaysButton()));
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
                    .setChatId((long) uid).setReplyMarkup(InlineButtons.getNapomDaysButton()));
        } catch (Exception e) {
            System.out.println("Error in days handler 522 str");
        }
    }
     */
}
