package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import ua.darkphantom1337.koi.kh.*;
import ua.darkphantom1337.koi.kh.buttons.InlineButtons;
import ua.darkphantom1337.koi.kh.database.DataBase;
import ua.darkphantom1337.koi.kh.database.SqlServer;
import ua.darkphantom1337.koi.kh.database.TidToUidTable;
import ua.darkphantom1337.koi.kh.entitys.*;
import ua.darkphantom1337.koi.kh.qr.DarkQRWriter;

import java.io.BufferedReader;
import java.io.File;
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
            bot.updateZStatus(order.getOrderID().intValue(), "Заявка принята.", "Заявка № " + order.getOrderID() + " принята менеджером! Ожидайте звонка...", true);
            SqlServer.setKodStatus(order,55);
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
                        if (!new Order(zn).getStatus().contains("Завершена")) {
                            bot.deleteMsg(zChannelID, order.getMainMsgID());
                            bot.sendCustomMessageToZChanel(zn, Bot.bot.getMainOrderText(order, user) + "\n« ооо  - ЗАЯВКА ПРИНЯТА БОЛЕЕ 48 ЧАС  – ооо »", InlineButtons.getObrobotkaTrueButton(zn));
                            bot.info("Заявка #" + order.getOrderID() + " принята более чем 48 часов назад! Принимал: " + prinyal);
                            bot.sendToLogChanel("Заявка #" + order.getOrderID() + " принята более чем 48 часов назад! Принимал: " + prinyal);
                        } else this.cancel();
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
                bot.editMsg(zChannelID, msgid, InlineButtons.getOprButton("cancel ❌❌❌", nz));
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
            bot.editMsg(zChannelID, msgid, InlineButtons.getOprButton("УТРОМ", order.getOrderID().intValue()));
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
            bot.editMsg(zChannelID, msgid, InlineButtons.getOprButton("НА2ДНЯ", order.getOrderID().intValue()));
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
            bot.updateZStatus(order.getOrderID().intValue(), "Заявка на рекламацию принята.", "Заявка на рекламацию принята менеджером! Ожидайте звонка...", true);
            bot.editMsg(zChannelID, msgid, InlineButtons.getObrobotkaTrueButton(null));
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
                order.setAccurateStatus("Завершена");
                OrderLocations.removeOrderFromAll(order.getOrderID());
                OrderLocations.remAllCurrentOrderID(order.getOrderID());
                bot.updateZStatus(order.getOrderID().intValue(), "Заявка отменена.", "❎Отмена заявки №" + order.getOrderID() + " успешно подтверждена менеджером.", true);
                bot.editMsg(zChannelID, msgid, InlineButtons.getButText("Заявка отменена. Отменил: " + user.getUserName()));
                bot.deleteMsg(zChannelID, order.getMainMsgID());
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.deleteMsg(zChannelID, msgid);
                    }
                }, 30000);
                SqlServer.setKodStatus(order,19);
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
                bot.updateZStatus(order.getOrderID().intValue(), "Отмена заявки отклонена.", "❌Отмена заявки №" + order.getOrderID() + " не была подтверждена менеджером.", true);
                bot.editMsg(zChannelID, msgid, InlineButtons.getButText("Отмена заявки отклонена. Отклонил: " + user.getUserName()));
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
                        bot.deleteMsg(new TidToUidTable(order.getUID(), false).getTelegramID(), bot.last_vost_msg.get(order.getUID()));
                        bot.last_vost_msg.put(order.getUID(), new User(new TidToUidTable(order.getUID(), false).getTelegramID()).sendMessage(msgText, "yesno=" + order.getOrderID()));
                    }
                }, 60000 * 5, 60000 * 13);
            }
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID)
                    .setText("Ваш текст успешно отправлен пользователю."));
            bot.editMsg(user.getTID(), msgid, "Отправлен текст на согласование по заявке #" + order.getOrderID() + "\nТекст: " + msgText);
            bot.editMsg(user.getTID(), msgid, InlineButtons.getM1Button(order.getOrderID().intValue()));
            for (String subOrderID : data.split("=")[1].split("/"))
                new SubOrder(subOrderID).setStatus("Ожидает согласования...");
            bot.updateMainOrderMessage(order.getOrderID());
            //  UsersData.clearSelectedOrdersForReconcile(user.getUID());
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
            UsersData.clearSelectedOrdersForReconcile(user.getUID());
            bot.updateVosstMsg(user, order.getOrderID(), user.getUserName() + ", выберите какой(ие) картридж(и) Вам нужно согласовать по заявке №" + order.getOrderID() + " от " + new User(new TidToUidTable(order.getUID(), false).getTelegramID()).getUserName());
            bot.editMsg(Long.parseLong(bot.getZayavChannelID()), msgid, InlineButtons.getObrobotkaTrueTrueButton());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.editMsg(Long.parseLong(bot.getZayavChannelID()), msgid, InlineButtons.getObrobotkaTrueButton(order.getOrderID().intValue()));
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
            bot.updateZStatus(order.getOrderID().intValue(), "Заявка отменена", "Менеджер отменил Вашу заявку, для уточнения деталей свяжитесь с менеджером.", true);
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
            SqlServer.setKodStatus(order,18);
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
            bot.updateZStatus(order.getOrderID().intValue(), "Заявка завершена", "Заявка №" + order.getOrderID() + " завершена.", true);
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
                bot.updateZStatus(order.getOrderID().intValue(), "Курьер выехал к Вам (Сбор)", "Курьер выехал к Вам забрать картридж/принтер, будет у Вас в течении 1-2 часов. Пожалуйста ожидайте.", false);
                order.setAccurateStatus("Сбор");
                for (Long subOrderID : order.getAllSubOrdersID()) {
                    SubOrder subOrder = new SubOrder(subOrderID);
                    subOrder.setStatus("Сбор");
                }
                OrderLocations.removeOrderFromAll(order.getOrderID());
                OrderLocations.addOrderInTheWay(order.getOrderID());
                OrderLocations.addOrdersInCollection(order.getOrderID());
                SqlServer.setKodStatus(order,56);
            }
            if (status.equals("заявка в работе") || status.equals("4")) {
                bot.updateZStatus(order.getOrderID().intValue(), "Заявка в работе", "Ваша заявка в работе, пожалуйста ожидайте.", false);
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Статус заявки изменён."));
                try {
                    bot.editMsg(zChannelID, msgid, InlineButtons.getObrobotkaTrueButton(order.getOrderID().intValue()));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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
                    bot.updateMainOrderMessage(order.getOrderID());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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
                OrderLocations.removeOrderFromAll(order.getOrderID());
                OrderLocations.addOrderInOffice(order.getOrderID());
                OrderLocations.addOrdersInWork(order.getOrderID());
                for (Long subOrderID : order.getAllSubOrdersID())
                    DataBase.saveOrderInSheet(subOrderID, "STATUS");
                SqlServer.setKodStatus(order,57);
                return true;
            }
            if (status.equals("доставка в пути") || status.equals("5")) {
                Long workID = order.getWorkID();
                if (workID != 0) {
                    OrderLocations.removeOrderFromAll(order.getOrderID());
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
                    bot.updateZStatus(order.getOrderID().intValue(), "Курьер выехал к Вам (Доставка)", "Курьер везет Вам картридж/принтер, будет у Вас в течении 1-2 часов. Пожалуйста ожидайте.", false);
                    for (Long subOrderID : order.getAllSubOrdersID()) {
                        SubOrder subOrder = new SubOrder(subOrderID);
                        subOrder.setStatus("Доставка");
                    }
                    SqlServer.setKodStatus(order,59);
                } else {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Нельзя согласовать заявку не изменив статус на 'В работе'\nИзмените статус заявки на 'В работе'").setShowAlert(true));
                    return true;
                }
            }
            try {
                bot.editMsg(zChannelID, msgid, InlineButtons.getObrobotkaTrueButton(order.getOrderID().intValue()));
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
        if (data.startsWith("#Manager/Reconciliation/Select/ForciblyReconcile/")) {
            try {
                UsersData.addSelectedOrderForReconcile(user.getUID(), Long.parseLong(data.split("/")[4]), UsersData.ReconcileAnswer.ForcedRecovery);
                bot.editMsg(user.getTID(), msgid, InlineButtons.getAllSubOrdersButtons(new SubOrder(data.split("/")[4]).getOrderID(), user.getUID()));
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Действие выполнено!"));
               /* Order order = new Order(data.split("/")[3]);
                if (Bot.bot.getSelectedOrderForReconsile(user.getUID()).isEmpty()) {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Сначала выберите картридж!"));
                    return true;
                }
                bot.handVosst(bot.u.objectToString(bot.getSelectedOrderForReconsile(user.getUID()), "/"), "Telegram", "SOGLASOVANO");
                bot.updateZStatus(order.getOrderID().intValue(), "Восстановление согласовано", "Вы подтвердили восстановления картриджа через телефон/месседжер.");
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Согласование успешно завершено!\nКлиент уведомлён.").setShowAlert(true));
                bot.deleteMsg(user.getTID(), msgid);
                bot.clearSelectedOrderForReconsile(user.getUID());
            */
            } catch (Exception exp) {
                exp.printStackTrace();
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Ошибка! Data: " + data + "\nОбратитесь к DarkPhantom1337").setShowAlert(true));
            }
            return true;
        }
        if (data.startsWith("#Manager/Reconciliation/Select/CancelForciblyReconcile/")) {
            try {
                UsersData.addSelectedOrderForReconcile(user.getUID(), Long.parseLong(data.split("/")[4]), UsersData.ReconcileAnswer.ForcedCancelRecovery);
                bot.editMsg(user.getTID(), msgid, InlineButtons.getAllSubOrdersButtons(new SubOrder(data.split("/")[4]).getOrderID(), user.getUID()));
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Действие выполнено!"));
               /* Order order = new Order(data.split("/")[3]);
                if (Bot.bot.getSelectedOrderForReconsile(user.getUID()).isEmpty()) {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Сначала выберите картридж!"));
                    return true;
                }
                bot.handVosst(bot.u.objectToString(bot.getSelectedOrderForReconsile(user.getUID()), "/"), "Telegram", "CANCEL");
                bot.updateZStatus(order.getOrderID().intValue(), "Отказ от восстановления", "Вы отказались от восстановления картриджа через телефон/месседжер.");
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Согласование успешно завершено!\nКлиент уведомлён.").setShowAlert(true));
                bot.deleteMsg(user.getTID(), msgid);
                bot.clearSelectedOrderForReconsile(user.getUID());
            */
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
            order.addDescriptions("QR-CREATE");
            bot.editMsg(zChannelID, msgid, InlineButtons.getObrobotkaTrueButton(order.getOrderID().intValue()));
            for (Long subOrderID : order.getAllSubOrdersID()) {
                try {
                    SubOrder subOrder = new SubOrder(subOrderID);
                    Cartridge cartridge = new Cartridge(DataBase.getNextCartridgeID());
                    cartridge.create(subOrder.getModel(), subOrder.getModel(), order.getAddress(), new ArrayList<Long>(Arrays.asList(order.getUID())));
                    File file = DarkQRWriter.createQRCode(cartridge.getID());
                    new User(order.getUID(), true).addCartridgeID(cartridge.getID());
                    bot.execute(new SendDocument().setDocument(file)
                            .setChatId(user.getTID()).setCaption("QR код для картриджа №" + cartridge.getID() + " успешно создан."
                                    + "\nКлиент: " + new User(order.getUID()).getUserPhone()
                                    + "\nМодель картриджа: " + cartridge.getModel()
                                    + "\nМестонахождение: " + cartridge.getAddress()
                                    + "\n№ Картриджа: " + cartridge.getID()));
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    public Boolean handleSelectSubOrderForReconcile(User user, String data, Integer msgid, String cbqID, String msgText) {
        if (data.startsWith("#Manager/Reconciliation/ConfirmReconcile/")) {
            try {
                Long orderID = Long.parseLong(data.split("/")[3]);
                HashMap<Long, UsersData.ReconcileAnswer> selectedSubOrders = UsersData.getSelectedOrdersForReconcile(user.getUID());
                if (selectedSubOrders.isEmpty()) {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Сначала выберите картридж!").setShowAlert(true));
                    return true;
                }
               /* user.setUserAction("wait_reconcile_text");
                user.sendMessage("\uD83D\uDC49 " +user.getUserName() + ", напишите ТЕКСТ согласования и отправьте мне :-)");
                return true;*/
                bot.editMsg(user.getTID(), msgid, InlineButtons.getPreReconcileConfirmButton(orderID.toString()));
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Выполняю..."));
                String text = "ℹ️Комплексное согласование выполнено: ";
                Order order = new Order(orderID);
                List<Long> allSubOrdersID = order.getAllSubOrdersID();

                List<Long> subOrdersForcedReconcile = UsersData.getSelectetSubOrderFilterAnswer(user.getUID(), UsersData.ReconcileAnswer.ForcedRecovery);
                List<Long> subOrdersForcedCancelReconcile = UsersData.getSelectetSubOrderFilterAnswer(user.getUID(), UsersData.ReconcileAnswer.ForcedCancelRecovery);
                List<Long> subOrdersReconcile = UsersData.getSelectetSubOrderFilterAnswer(user.getUID(), UsersData.ReconcileAnswer.Reconcile);

                String subOrdersModelsForcedCancelReconcile = "", subOrdersModelsForcedReconcile = "", subOrdersModelsReconcile = "";

                /**
                 * ----------------- Reconcile CANCEL FORCED
                 */

                for (Long subOrderID : subOrdersForcedCancelReconcile) {
                    try {
                        SubOrder subOrder = new SubOrder(subOrderID);
                        if (allSubOrdersID.contains(subOrderID)) {
                            subOrdersModelsForcedCancelReconcile += subOrder.getModel() + ";";
                            text += "\n\uD83D\uDC49 Order №" + orderID + "/" + subOrderID + " - Отказ от восстановления";
                        } else
                            subOrdersForcedCancelReconcile.remove(subOrderID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!subOrdersForcedCancelReconcile.isEmpty()) {
                    try {
                        bot.handVosst(bot.u.objectToString(subOrdersForcedCancelReconcile, "/"), "Telegram", "CANCEL");
                        bot.updateZStatus(orderID.intValue(), "Отказ от восстановления", "Вы отказались от восстановления картриджа(ей) " + subOrdersModelsForcedCancelReconcile + " через телефон/месседжер.", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /**
                 * ----------------- Reconcile FORCED
                 */

                for (Long subOrderID : subOrdersForcedReconcile) {
                    try {
                        SubOrder subOrder = new SubOrder(subOrderID);
                        if (allSubOrdersID.contains(subOrderID)) {
                            subOrdersModelsForcedReconcile += subOrder.getModel() + ";";
                            text += "\n\uD83D\uDC49 Order №" + orderID + "/" + subOrderID + " - Восстановление согласовано";
                        } else
                            subOrdersForcedReconcile.remove(subOrderID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!subOrdersForcedReconcile.isEmpty()) {
                    try {
                        bot.handVosst(bot.u.objectToString(subOrdersForcedReconcile, "/"), "Telegram", "SOGLASOVANO");
                        bot.updateZStatus(orderID.intValue(), "Восстановление согласовано", "Вы подтвердили восстановления картриджа(ей) " + subOrdersModelsForcedReconcile + " через телефон/месседжер.", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /**
                 * ----------------- Reconcile
                 */

                if (!subOrdersReconcile.isEmpty()) {
                    UsersData.clearSelectedOrdersForReconcile(user.getUID());
                    for (Long subOrderID : subOrdersReconcile) {
                        try {
                            SubOrder subOrder = new SubOrder(subOrderID);
                            UsersData.addSelectedOrderForReconcile(user.getUID(), subOrderID, UsersData.ReconcileAnswer.Reconcile);
                            if (allSubOrdersID.contains(subOrderID)) {
                                subOrdersModelsReconcile += subOrder.getModel() + ";";
                                text += "\n\uD83D\uDC49 Order №" + orderID + "/" + subOrderID + " - Ожидаю текст согласования";
                            } else
                                subOrdersReconcile.remove(subOrderID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else UsersData.clearSelectedOrdersForReconcile(user.getUID());

                user.sendMessage(text);
                if (!subOrdersReconcile.isEmpty()) {
                    user.setUserAction("wait_reconcile_text");
                    user.sendMessage("\uD83D\uDC49 " + user.getUserName() + ", напишите ТЕКСТ согласования и отправьте мне :-)");
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Напишите текст согласования").setShowAlert(true));
                }
                //UsersData.clearSelectedOrdersForReconcile(user.getUID());
                return true;
            } catch (Exception exp) {
                exp.printStackTrace();
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Ошибка! Data: " + data + "\nОбратитесь к DarkPhantom1337").setShowAlert(true));
            }
            return true;
        }
        if (data.startsWith("#Manager/Reconciliation/Select/Reconcile/")) {
            try {
                //Order order = new Order(data.split("/")[3]);
                UsersData.addSelectedOrderForReconcile(user.getUID(), Long.parseLong(data.split("/")[4]), UsersData.ReconcileAnswer.Reconcile);
                bot.editMsg(user.getTID(), msgid, InlineButtons.getAllSubOrdersButtons(new SubOrder(data.split("/")[4]).getOrderID(), user.getUID()));
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Действие выполнено!"));
                /*if (Bot.bot.getSelectedOrderForReconsile(user.getUID()).isEmpty()) {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Сначала выберите картридж!"));
                } else {
                    bot.deleteMsg(user.getTID(), msgid);
                    user.setUserAction("wait_reconcile_text");
                    user.sendMessage(user.getUserName() + ", напишите текст согласования и отправьте мне :-)");
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Напишите текст согласования мне"));
                }*/
                return true;
            } catch (Exception exp) {
                exp.printStackTrace();
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Ошибка! Data: " + data + "\nОбратитесь к DarkPhantom1337").setShowAlert(true));
            }
            return true;
        }
        if (data.startsWith("#Manager/Reconciliation/HideReconcile")) {
            try {
                new Order(data.split("/")[3]).setVosstMsgText("");
                UsersData.clearSelectedOrdersForReconcile(user.getUID());
                bot.deleteMsg(user.getTID(), msgid);
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(cbqID).setText("Действие выполнено!"));
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
