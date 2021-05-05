package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.DarkQRWriter;
import ua.darkphantom1337.koi.kh.DataBase;
import ua.darkphantom1337.koi.kh.OrderLocations;
import ua.darkphantom1337.koi.kh.buttons.InlineButtons;
import ua.darkphantom1337.koi.kh.database.TidToUidTable;
import ua.darkphantom1337.koi.kh.entitys.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PersonalCallbackHandler {

    private Bot bot = Bot.bot;
    private Long zChannelID = Long.parseLong(bot.getZayavChannelID());

    public boolean handleCallback(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (!DataBase.isPersonal(user.getUID())) {
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("У вас нет доступа к данной функции!"));
            return true;
        }
        if (handlePrinyatZCallback(user, data, msgid, cbqID, msgText)) return true;
        if (handleOprosCallback(user, data, msgid, cbqID, msgText)) return true;
        if (handleReklamHand(user, data, msgid, cbqID, msgText)) return true;
        if (handleConfirmCancelZayav(user, data, msgid, cbqID, msgText)) return true;
        if (handleOtklonCancelZayav(user, data, msgid, cbqID, msgText)) return true;
        if (handleSendMsgForNz(user, data, msgid, cbqID, msgText)) return true;
        if (handleSoglasVoss(user, data, msgid, cbqID, msgText)) return true;
        if (handleCancelOrder(user, data, msgid, cbqID, msgText)) return true;
        if (handleEndZayavAndVosst(user, data, msgid, cbqID, msgText)) return true;
        if (handleChangeZStatus(user, data, msgid, cbqID, msgText)) return true;
        if (handleOrderRecovery(user, data, msgid, cbqID, msgText)) return true;
        if (handleCreateQR(user, data, msgid, cbqID, msgText)) return true;
        if (handleSelectSubOrderForReconcile(user, data, msgid, cbqID, msgText)) return true;
        return false;
    }

    public Boolean handlePrinyatZCallback(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#PRINYAT-Z=")) {
            Order order = new Order(data.split("=")[1]);
            if (order.getStatus().equals("ПринятаМенеджером")) {
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Заявка уже принята!"));
                return true;
            }
            if (order.getStatus().contains("Завершена")) {
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Заявка уже завершена!"));
                return true;
            }
            order.setStatus("ПринятаМенеджером");
            order.setAccurateStatus("Принята");
            String prinyal = DataBase.getPerFields(user.getUID(), "position") + " " + user.getUserName();
            order.setPrinyal(prinyal);
            for (Long subOrderID : order.getAllSubOrdersID()) {
                SubOrder subOrder = new SubOrder(subOrderID);
                subOrder.setStatus("Принят");
                DataBase.saveOrderInSheet(subOrderID, "PRINYAL");
            }
            bot.updateMainOrderMessage(order.getOrderID());
            /*bot.editMsg(zChannelID, msgid, msgText.replaceAll("Поступила",
                    "\uD83D\uDC49 Заявку принял: " + prinyal));
             */
            //bot.editMsg(zChannelID, msgid, bot.getObrobotkaTrueButton(order.getOrderID().intValue()));
            bot.updateZStatus(order.getOrderID().intValue(), "Заявка принята.", "Заявка № " + order.getOrderID() + " принята менеджером! Ожидайте звонка...");

            CompletableFuture.runAsync(() -> {
                if (order.getSource().equals("Viber")) {
                    try {
                        URL changeStatus = new URL("https://ay.dn.ua/ViberBot/src/ChangeStatus.php?dark_id=" + order.getUID() + "&zn=" + order.getOrderID() + "&status=prinyata&key=1337");
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(changeStatus.openStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null)
                            System.out.println(inputLine);
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        bot.sendToLogChanel("❌ Ошибка при попытке изменении статуса в Viber по заявке #" + order.getOrderID());
                    }
                }
            });
            bot.info("Заявка #" + order.getOrderID() + " принята! Принял: " + prinyal);
            bot.sendToLogChanel("Заявка #" + order.getOrderID() + " принята! Принял: " + prinyal);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        int zn = order.getOrderID().intValue();
                        bot.deleteMsg(zChannelID, order.getMainMsgID());
                        bot.sendCustomMessageToZChanel(zn, msgText + "« ооо  - ЗАЯВКА ПРИНЯТА БОЛЕЕ 48 ЧАС  – ооо »", bot.getObrobotkaTrueButton(zn));
                        bot.info("Заявка #" + order.getOrderID() + " принята более чем 48 часов назад! Принимал: " + prinyal);
                        bot.sendToLogChanel("Заявка #" + order.getOrderID() + " принята более чем 48 часов назад! Принимал: " + prinyal);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        bot.info("Заявка #" + order.getOrderID() + " принята более чем 48 часов назад! Принимал: " + prinyal + "\nОшибка при продлении заявки");
                        bot.sendToLogChanel("Заявка #" + order.getOrderID() + " принята более чем 48 часов назад! Принимал: " + prinyal + "\nОшибка при продлении заявки");
                    }
                    this.cancel();
                }
            }, (((60000 * 60) * 24) * 2)); // Отправлять через 3 дня после принятия заявки
            return true;
        }
        return false;
    }

    public Boolean handleOprosCallback(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.contains("#OPROS")) {
            final Integer nz = Integer.parseInt(data.split("=")[1]), idfz = DataBase.getUidForZn(nz);
            Order order = new Order(nz);
            if (handleOprosMZ(user, order, data, msgid, cbqID, msgText)) return true;
            if (handleOprosTZ(user, order, data, msgid, cbqID, msgText)) return true;
            if (data.contains("#OPROS-C-Z=")) {
                bot.editMsg(zChannelID, msgid, bot.getOprButton("cancel ❌❌❌", nz));
                order.setStatus("Завершена");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.deleteMsg(zChannelID, msgid);
                        this.cancel();
                    }
                }, 60000);
                return true;
            }
        }
        return false;
    }

    private Boolean handleOprosMZ(User user, Order order, String data, Integer msgid, String cbqID, String msgText) {
        if (data.contains("#OPROS-M-Z=")) {
            order.setStatus("Завершена");
            bot.editMsg(zChannelID, msgid, bot.getOprButton("УТРОМ", order.getOrderID().intValue()));
            int h = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
            if (h >= 9 && h <= 12) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            bot.sendOpros(order.getUID(), order.getOrderID().intValue());
                            bot.info("Отправлен опрос по заявке №" + order.getOrderID());
                        } catch (Exception ex) {
                            bot.error("Ошибка при отправке отпроса заявки"
                                    + "\n-> Exception: " + ex.getLocalizedMessage()
                                    + "\n-> Error line: " + ex.getStackTrace()[0].getLineNumber());
                        }
                        this.cancel();
                    }
                }, (100000)); //Отправить через некоторое время (1м)
            } else {
                String date1 = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                DataBase.saveZInOpr(order.getOrderID().intValue(), order.getUID().intValue(), bot.addonedaytoday(date1), "");
                bot.info("Опрос по заявке №" + order.getOrderID()
                        + " клиенту  ПЕРЕНЕСЁН НА ЗАВТРА НА УТРО");
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.deleteMsg(zChannelID, msgid);
                    this.cancel();
                }
            }, 60000);
            return true;
        }
        return false;
    }

    private Boolean handleOprosTZ(User user, Order order, String data, Integer msgid, String cbqID, String msgText) {
        if (data.contains("#OPROS-T-Z=")) {
            bot.editMsg(zChannelID, msgid, bot.getOprButton("НА2ДНЯ", order.getOrderID().intValue()));
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.deleteMsg(zChannelID, msgid);
                    this.cancel();
                }
            }, 30000);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        int uid = order.getUID().intValue();
                        if (order.getSource().equals("Viber")) {
                            bot.sendToChanelOpros(order.getOrderID(),
                                    order.getTheme(), order.getModel(),
                                    DataBase.getVZUFileds(uid, "type"),
                                    DataBase.getVZUFileds(uid, "name"),
                                    DataBase.getVZUFileds(uid, "phone"),
                                    DataBase.getVZUFileds(uid, "company_name"),
                                    order.getSource());
                        } else {
                            bot.sendToChanelOpros(order.getOrderID(),
                                    order.getTheme(), order.getModel(),
                                    DataBase.getUserType(uid), DataBase.getUserName(uid), DataBase.getUserPhone(uid),
                                    DataBase.getUsFileds((long) uid, "company_name"),
                                    order.getSource());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    this.cancel();
                }
            }, 60000); // Отправить через два дня
            bot.info("Opros z №" + order.getOrderID() + " perenos in two day");
            return true;
        }
        return false;
    }

    public Boolean handleReklamHand(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#REKLAM_HAND")) {
            Order order = new Order(data.split("/")[1]);
            order.addDescriptions("REKLAMACIYA-END-" + user.getUserPhone());
            bot.updateZStatus(order.getOrderID().intValue(), "Заявка на рекламацию принята.", "Заявка на рекламацию принята менеджером! Ожидайте звонка...");
            bot.editMsg(zChannelID, msgid, bot.getObrobotkaTrueButton(null));
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Заявка на  рекламацию закрыта"));
            return true;
        }
        return false;
    }

    public Boolean handleConfirmCancelZayav(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#CONFIRM_CANCEL_ZAYAV")) {
            Order order = new Order(data.split("/")[1]);
            if (!order.getDescriptions().contains("CANCEL_CONFIRM")) {
                order.addDescriptions("CANCEL_CONFIRM");
                order.addDescriptions("CANCEL_CONFIRM-" + user.getUserPhone());
                order.setStatus("Завершена");
                bot.updateZStatus(order.getOrderID().intValue(), "Заявка отменена.", "❎Отмена заявки №" + order.getOrderID() + " успешно подтверждена менеджером.");
                bot.editMsg(zChannelID, msgid, bot.getButText("Заявка отменена. Отменил: " + user.getUserName()));
                bot.deleteMsg(zChannelID, order.getMainMsgID());
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.deleteMsg(zChannelID, msgid);
                    }
                }, 30000);
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Заявка отменена."));
            } else {
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Отмена заявки уже подтверждена."));
            }
        }
        return false;
    }

    public Boolean handleOtklonCancelZayav(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#OTKLON_CANCEL_ZAYAV")) {
            Order order = new Order(data.split("/")[1]);

            if (!order.getDescriptions().contains("OTKLON_CANCEL")) {
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Вы отклонили отмену заявки."));
                order.addDescriptions("OTKLON_CANCEL");
                order.addDescriptions("OTKLON_CANCEL-" + user.getUserName());
                bot.updateZStatus(order.getOrderID().intValue(), "Отмена заявки отклонена.", "❌Отмена заявки №" + order.getOrderID() + " не была подтверждена менеджером.");
                bot.editMsg(zChannelID, msgid, bot.getButText("Отмена заявки отклонена. Отклонил: " + user.getUserName()));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.deleteMsg(zChannelID, msgid);
                    }
                }, 30000);
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Вы отклонили отмену заявки."));
            } else {
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Отклонение отмены уже сделано."));
            }
            return true;
        }
        return false;
    }

    public Boolean handleSendMsgForNz(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.contains("#SendMsgForNz=")) {
            Order order = new Order(new SubOrder(data.split("=")[1].split("/")[0]).getOrderID());
            if (order.getSource().equals("Viber")) {
                try {
                    URL oracle = new URL("https://ay.dn.ua/ViberBot/src/ChangeStatus.php?dark_id=" + order.getUID() + "&zn=" + order.getOrderID() + "&status=vosst&key=1337&text="
                            + msgText.replaceAll(" ", "%20").replaceAll("\\s", "%5Cn"));
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(oracle.openStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        System.out.println(inputLine);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    bot.sendToLogChanel("❌ Ошибка при попытке отправки сообщения в Viber по заявке #" + order.getOrderID());
                }
            } else {
                bot.last_vost_msg.put(order.getUID(), new User(new TidToUidTable(order.getUID(), false).getTelegramID()).sendMessage(msgText, "yesno=" + data.split("=")[1]));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (order.getDescriptions().contains("VOSST_USE")) {
                            this.cancel();
                            return;
                        }
                        bot.deleteMsg(order.getUID(), bot.last_vost_msg.get(order.getUID()));
                        bot.last_vost_msg.put(order.getUID(), user.sendMessage(msgText, "yesno=" + order.getOrderID()));
                    }
                }, 60000 * 5, 60000 * 13);
            }
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID)
                    .setText("Ваш текст успешно отправлен пользователю."));
            bot.editMsg(user.getTID(), msgid, "Отправлен текст на согласование по заявке #" + order.getOrderID() + "\nТекст: " + msgText);
            bot.editMsg(user.getTID(), msgid, bot.getM1Button(order.getOrderID().intValue()));
            for (String subOrderID : data.split("=")[1].split("/"))
                new SubOrder(subOrderID).setStatus("Ожидает согласования...");
            bot.updateMainOrderMessage(order.getOrderID());
            bot.clearSelectedOrderForReconsile(user.getUID());
            return true;
        }
        return false;
    }

    public Boolean handleSoglasVoss(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#SoglasVoss")) {
            Order order = new Order(data.split("/")[1]);
            String iddata = DataBase.getUFileds(20, "val1");
            List<Long> usersID = new ArrayList<Long>();
            if (iddata != null && !iddata.equals("") && !iddata.equals(" "))
                usersID = Arrays.stream(iddata.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
            if (!usersID.contains(user.getUID())) {
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Вам не разрешено согласовывать заявки.").setShowAlert(true));
                return true;
            }
            bot.updateVosstMsg(user,order.getOrderID(), user.getUserName() + ", выберите какой(ие) картридж(и) Вам нужно согласовать по заявке №" + order.getOrderID() + " от " + new User(new TidToUidTable(order.getUID(), false).getTelegramID()).getUserName());
            bot.editMsg(Long.parseLong(bot.getZayavChannelID()), msgid, bot.getObrobotkaTrueTrueButton());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.editMsg(Long.parseLong(bot.getZayavChannelID()), msgid, bot.getObrobotkaTrueButton(order.getOrderID().intValue()));
                }
            }, 60000);
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Перейдите пожалуйста в личный чат с ботом..."));
            return true;
        }
        return false;
    }

    public Boolean handleCancelOrder(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#CANCELORDER")) {
            Order order = new Order(data.split("/")[1]);
            order.addDescriptions("CANCEL|" + user.getUID());
            bot.updateZStatus(order.getOrderID().intValue(), "Заявка отменена", "Менеджер отменил Вашу заявку, для уточнения деталей свяжитесь с менеджером.");
            order.setStatus("Завершена|Отменена");
            try {
                for (Long taskID : order.getAllTasksID()) {
                    Task task = new Task(taskID);
                    task.getCourier().remCurrentTaskID(taskID);
                    bot.deleteMsg(task.getCourier().getUser().getTID(), task.getOrderMessageID());
                }
            } catch (Exception ignore) {
            }
            bot.editMsg(zChannelID, msgid, "❌ Заявка №" + order.getOrderID() + " отменена! Отменил: " + DataBase.getPerFields(user.getUID(), "position") + " " + DataBase.getPerFields(user.getUID(), "name"));
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Заявка #" + order.getOrderID() + " отменена!"));
            bot.sendToLogChanel("❌ Пользователь " + DataBase.getPerFields(user.getUID(), "position") + " " + DataBase.getPerFields(user.getUID(), "name") + " отменил заявку №" + order.getOrderID() + "!");
            OrderLocations.remAllCurrentOrderID(order.getOrderID());
            OrderLocations.removeOrderFromAll(order.getOrderID());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.deleteMsg(zChannelID, order.getMainMsgID());
                }
            }, 30000);
            return true;
        }
        return false;
    }

    public Boolean handleEndZayavAndVosst(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#ENDZAYAV") || data.contains("#ENDZFORVOSST")) {
            Order order = new Order(data.split("/")[1]);
            order.setStatus("Завершена");
            order.setAccurateStatus("Завершена");
            bot.editMsg(zChannelID, msgid, "Заявка №" + order.getOrderID() + " завершена принудительно! Завершил: " + DataBase.getPerFields(user.getUID(), "name"));
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Заявка №" + order.getOrderID() + " завершена!"));
            try {
                for (Long taskID : order.getAllTasksID()) {
                    Task task = new Task(taskID);
                    task.getCourier().remCurrentTaskID(taskID);
                    task.setTaskStatus("Завершен");
                    bot.deleteMsg(task.getCourier().getUser().getTID(), task.getOrderMessageID());
                }
            } catch (Exception ignore) {
            }
            for (Long subOrderID : order.getAllSubOrdersID()) {
                new SubOrder(subOrderID).setStatus("Завершена");
                DataBase.saveOrderInSheet(subOrderID, "STATUS");
            }
            new Timer().schedule(new TimerTask() { // Таймер для опроса
                @Override
                public void run() {
                    try {
                        Integer uid = order.getUID().intValue();
                        bot.deleteMsg(zChannelID, order.getMainMsgID());
                        if (order.getSource().equals("Viber")) {
                            bot.sendToChanelOpros(order.getOrderID(),
                                    order.getTheme(), order.getModel(),
                                    DataBase.getVZUFileds(uid, "type"),
                                    DataBase.getVZUFileds(uid, "name"),
                                    DataBase.getVZUFileds(uid, "phone"),
                                    DataBase.getVZUFileds(uid, "company_name"),
                                    order.getSource());
                        } else {
                            bot.sendToChanelOpros(order.getOrderID(),
                                    order.getTheme(), order.getModel(),
                                    DataBase.getUserType(uid), DataBase.getUserName(uid), DataBase.getUserPhone(uid),
                                    DataBase.getUsFileds(uid.longValue(), "company_name"),
                                    order.getSource());
                        }
                   /* String s = DataBase.getZFields(zn, "msg_ids");
                    if (s != null) {
                        s += "," + id + "!" + msgid;
                    } else {
                        s = "" + id + "!" + msgid;
                    }
                    DataBase.setZFields(zn, "msg_ids", s);*/
                        bot.info("Z №" + order.getOrderID() + " end! Prinimal: " + DataBase.getPerFields(user.getUID(), "position")
                                + " " + DataBase.getPerFields(user.getUID(), "name") + " Oprosit?");
                    } catch (Exception ex) {
                        bot.error("Error in send zayav"
                                + "\n-> NZayzav: " + order.getOrderID()
                                + "\n-> Exception: " + ex.getLocalizedMessage()
                                + "\n-> Error line: " + ex.getStackTrace()[0].getLineNumber());
                    }
                    this.cancel();
                }
            }, 5000);
            bot.updateZStatus(order.getOrderID().intValue(), "Заявка завершена", "Заявка №" + order.getOrderID() + " завершена.");
            bot.sendToLogChanel("Пользователь " + DataBase.getPerFields(order.getUID(), "name") + " принудительно завершил заявку №" + order.getOrderID() + "!");
            OrderLocations.remAllCurrentOrderID(order.getOrderID());
            OrderLocations.removeOrderFromAll(order.getOrderID());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.sendToArch(order.getOrderID().intValue());
                }
            }, 1);
            return true;
        }
        return false;
    }

    public Boolean handleChangeZStatus(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#ChangeZStatus")) {
            Order order = new Order(data.split("/")[2]);
            String status = data.split("/")[1].replace("_", " ").toLowerCase();
            if (status.equals("сбор в пути") || status.equals("3")) {
                bot.updateZStatus(order.getOrderID().intValue(), "Курьер выехал к Вам (Сбор)", "Курьер выехал к Вам забрать картридж/принтер, будет у Вас в течении 1-2 часов. Пожалуйста ожидайте.");
                order.setAccurateStatus("Сбор");
                for (Long subOrderID : order.getAllSubOrdersID()) {
                    SubOrder subOrder = new SubOrder(subOrderID);
                    subOrder.setStatus("Сбор");
                }
                OrderLocations.addOrderInTheWay(order.getOrderID());
                OrderLocations.addOrdersInCollection(order.getOrderID());
            }
            if (status.equals("заявка в работе") || status.equals("4")) {
                bot.updateZStatus(order.getOrderID().intValue(), "Заявка в работе", "Ваша заявка в работе, пожалуйста ожидайте.");
                try {
                    bot.editMsg(zChannelID, msgid, bot.getObrobotkaTrueButton(order.getOrderID().intValue()));
                    bot.updateMainOrderMessage(order.getOrderID());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Статус заявки изменён."));

                for (Long subOrderID : order.getAllSubOrdersID()) {
                    MasterWork work = new MasterWork(DataBase.getNextMasterWorkID());
                    work.setOrderID(subOrderID);
                    work.setStartDate(new Date());
                    work.setStartTime(new Date());
                    work.setMainMessageID(bot.mch.sendTextToChannel(bot.mch.getMainZText(work), bot.mcb.getStartWorkButton(work)));
                    new SubOrder(subOrderID).setWorkID(work.getWorkID());
                    new SubOrder(subOrderID).setStatus("Ожидает мастера");
                    order.setWorkID(work.getWorkID());
                    DataBase.saveOrderInSheet(subOrderID, "ID");
                }
                order.setAccurateStatus("В работе");
                try {
                    for (Long taskID : order.getAllTasksID()) {
                        Task task = new Task(taskID);
                        if (task.getTaskType().equals(TaskType.COLLECT)) {
                            bot.deleteMsg(task.getCourier().getUser().getTID(), task.getOrderMessageID());
                            task.getCourier().remCurrentTaskID(taskID);
                        }
                    }
                } catch (Exception ignore) {
                }
                OrderLocations.remOrderInTheWay(order.getOrderID());
                OrderLocations.remOrdersInCollection(order.getOrderID());
                OrderLocations.addOrderInOffice(order.getOrderID());
                OrderLocations.addOrdersInWork(order.getOrderID());
                for (Long subOrderID : order.getAllSubOrdersID())
                    DataBase.saveOrderInSheet(subOrderID, "STATUS");
                return true;
            }
            if (status.equals("доставка в пути") || status.equals("5")) {
                Long workID = order.getWorkID();
                if (workID != 0) {
                    OrderLocations.remReadyToWay(order.getOrderID());
                    OrderLocations.addOrderInTheWay(order.getOrderID());
                    OrderLocations.addOrdersInDelivery(order.getOrderID());
                    try {
                        for (Long taskID : order.getAllTasksID()) {
                            Task task = new Task(taskID);
                            if (task.getTaskType().equals(TaskType.COLLECT)) {
                                task.getCourier().remCurrentTaskID(taskID);
                                bot.deleteMsg(task.getCourier().getUser().getTID(), task.getOrderMessageID());
                            }
                        }
                    } catch (Exception ignore) {
                    }
                    order.setAccurateStatus("Доставка");
                    bot.updateZStatus(order.getOrderID().intValue(), "Курьер выехал к Вам (Доставка)", "Курьер везет Вам картридж/принтер, будет у Вас в течении 1-2 часов. Пожалуйста ожидайте.");
                    for (Long subOrderID : order.getAllSubOrdersID()) {
                        SubOrder subOrder = new SubOrder(subOrderID);
                        subOrder.setStatus("Доставка");
                    }
                } else {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Нельзя согласовать заявку не изменив статус на 'В работе'\nИзмените статус заявки на 'В работе'").setShowAlert(true));
                    return true;
                }
            }
            try {
                bot.editMsg(zChannelID, msgid, bot.getObrobotkaTrueButton(order.getOrderID().intValue()));
                bot.updateMainOrderMessage(order.getOrderID());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Статус заявки изменён."));

            return true;
        }
        return false;
    }

    public Boolean handleOrderRecovery(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#Manager/Reconciliation/ForciblyReconcile/")) {
            try {
                Order order = new Order(data.split("/")[3]);
                if (Bot.bot.getSelectedOrderForReconsile(user.getUID()).isEmpty()) {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Сначала выберите картридж!"));
                    return true;
                }
                bot.handVosst(bot.u.objectToString(bot.getSelectedOrderForReconsile(user.getUID()), "/"), "Telegram", "SOGLASOVANO");
                bot.updateZStatus(order.getOrderID().intValue(), "Восстановление согласовано", "Вы подтвердили восстановления картриджа через телефон/месседжер.");
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Согласование успешно завершено!\nКлиент уведомлён.").setShowAlert(true));
                bot.deleteMsg(user.getTID(), msgid);
                bot.clearSelectedOrderForReconsile(user.getUID());
            } catch (Exception exp) {
                exp.printStackTrace();
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Ошибка! Data: " + data + "\nОбратитесь к DarkPhantom1337").setShowAlert(true));
            }
            return true;
        }
        if (data.equals("#Manager/Reconciliation/CancelForciblyReconcile/")) {
            try {
                Order order = new Order(data.split("/")[3]);
                if (Bot.bot.getSelectedOrderForReconsile(user.getUID()).isEmpty()) {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Сначала выберите картридж!"));
                    return true;
                }
                bot.handVosst(bot.u.objectToString(bot.getSelectedOrderForReconsile(user.getUID()), "/"), "Telegram", "CANCEL");
                bot.updateZStatus(order.getOrderID().intValue(), "Отказ от восстановления", "Вы отказались от восстановления картриджа через телефон/месседжер.");
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Согласование успешно завершено!\nКлиент уведомлён.").setShowAlert(true));
                bot.deleteMsg(user.getTID(), msgid);
                bot.clearSelectedOrderForReconsile(user.getUID());
            } catch (Exception exp) {
                exp.printStackTrace();
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Ошибка! Data: " + data + "\nОбратитесь к DarkPhantom1337").setShowAlert(true));
            }
            return true;
        }
        return false;
    }

    public Boolean handleCreateQR(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#CreateQR")) {
            Order order = new Order(data.split("/")[1]);
            Cartridge cartridge = new Cartridge(DataBase.getNextCartridgeID());
            cartridge.create(order.getModel(), order.getModel(), order.getAddress(), new ArrayList<Long>(Arrays.asList(order.getUID())));
            new User(cartridge.getOwnersID().get(0)).addCartridgeID(cartridge.getID());
            order.addDescriptions("QR-CREATE");
            bot.editMsg(zChannelID, msgid, bot.getObrobotkaTrueButton(order.getOrderID().intValue()));
            try {
                bot.sendDocument(new SendDocument().setNewDocument(DarkQRWriter.createQRCode(cartridge.getID()))
                        .setChatId(user.getTID()).setCaption("QR для картриджа №" + cartridge.getID() + " успешно создан."
                                + "\nКлиент: " + new User(cartridge.getOwnersID().get(0)).getUserPhone()
                                + "\nМодель картриджа: " + cartridge.getModel()
                                + "\nМестонахождение: " + cartridge.getAddress()
                                + "\nQR-код для картриджа №" + cartridge.getID()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public Boolean handleSelectSubOrderForReconcile(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#Manager/Reconciliation/SelectSubOrder/")) {
            try {
                Long subOrderID = Long.parseLong(data.split("/")[3]);
                bot.addSelectedOrderForReconsile(user.getUID(), subOrderID);
                bot.editMsg(user.getTID(), msgid, InlineButtons.getAllSubOrdersButtons(new SubOrder(subOrderID).getOrderID(), user.getUID()));
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Действие выполнено!").setShowAlert(true));
            } catch (Exception exp) {
                exp.printStackTrace();
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Ошибка! Data: " + data + "\nОбратитесь к DarkPhantom1337").setShowAlert(true));
            }
            return true;
        }
        if (data.startsWith("#Manager/Reconciliation/Reconcile/")) {
            try {
                Order order = new Order(data.split("/")[3]);
                if (Bot.bot.getSelectedOrderForReconsile(user.getUID()).isEmpty()) {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Сначала выберите картридж!"));
                } else {
                    bot.deleteMsg(user.getTID(), msgid);
                    user.setUserAction("wait_reconcile_text");
                    user.sendMessage(user.getUserName() + ", напишите текст согласования и отправьте мне :-)");
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Напишите текст согласования мне"));
                }
                return true;
            } catch (Exception exp) {
                exp.printStackTrace();
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Ошибка! Data: " + data + "\nОбратитесь к DarkPhantom1337").setShowAlert(true));
            }
            return true;
        }
        return false;
    }
}
