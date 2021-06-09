package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.DataBase;
import ua.darkphantom1337.koi.kh.OrderLocations;
import ua.darkphantom1337.koi.kh.UsersData;
import ua.darkphantom1337.koi.kh.database.SqlServer;
import ua.darkphantom1337.koi.kh.entitys.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MasterChannelHandler {

    Bot bot = Bot.bot;

    public Integer sendTextToChannel(String text, InlineKeyboardMarkup buttons) {
        return bot.sendMsg(bot.getMasterChannelID(), text, buttons);
    }

    public String getMainZText(MasterWork work) {
        SubOrder order = new SubOrder(work.getOrderID());
        Order main_order = new Order(order.getOrderID());
        Long cartridgeID = order.getCartridgeID();
        String sendModel = order.getModel(), cartridgeModel = sendModel, printerModel = cartridgeModel;
        if (cartridgeID != 0L) {
            Cartridge cartridge = new Cartridge(cartridgeID);
            cartridgeModel = cartridge.getModel();
            printerModel = cartridge.getPrinterModel();
        }
        String s = "Work №" + order.getOrderID() +"/" + order.getSubOrderID() + "/" + work.getWorkID()
                + "\nКлиент: " + main_order.getName()
                + "\nТема: " + main_order.getTheme()
                + "\nМодель: " + sendModel + (!sendModel.equals(cartridgeModel) ? "(" + cartridgeModel + ")" : "")
                + (!cartridgeModel.equals(printerModel) ? "\nМодель принтера: " + printerModel : "")
                + (cartridgeID != 0L ? "\nID картриджа: " + cartridgeID : "")
                + (work.isConfirmRecovery() ? "\n\uD83D\uDC49 Статус согласования: СОГЛАСОВАНО ✅" : (work.isCancelRecovery() ? "\n\uD83D\uDC49 Статус согласования: Клиент отказался ❌" : (work.isWaitRecovery() ? "\n\uD83D\uDC49 Статус согласования: Уточнение деталей \uD83D\uDD38" : "")));
        return s;
    }

    public boolean handleStartWorkButtonClick(User user, String data, Integer msgid, String callid) {
        if (data.startsWith("#Master/TakeToWork")) {
            Long workID = Long.parseLong(data.split("/")[2]);
            MasterWork work = new MasterWork(workID);
            work.addDescription("START_WORK");
            work.addDescription("Took|" + user.getUID());
            work.setMasterID(user.getUID());
            MasterWork lastWork = new MasterWork(workID--);
            work.setTonerAmount(lastWork.getTonerAmount());
            bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
            new SubOrder(work.getOrderID()).setStatus("В работе");
            bot.updateMainOrderMessage(new SubOrder(work.getOrderID()).getOrderID());
            DataBase.saveOrderInSheet(work.getOrderID(),"WORK_TAKE");
        }
        return false;
    }

    public boolean handleRemontButtonClick(User user, String data, Integer msgid, String callid) {
        if (data.startsWith("#Master/Remont/")) {
            Long workID = Long.parseLong(data.split("/")[2]);
            String[] spldata = data.split("/");
            String remtype = spldata[3];
            Long user_id = user.getUID();
            MasterWork work = new MasterWork(workID);
            if (remtype.equals("Component")) {
                String updatetype = spldata[4];
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Компонент выбран!"));
                if (updatetype.equals("LD")) {
                    work.setUseLD(!work.isUseLD());
                    bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                    return true;
                }
                if (updatetype.equals("CHL")) {
                    work.setUseCHL(!work.isUseCHL());
                    bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                    return true;
                }
                if (updatetype.equals("MR")) {
                    work.setUseMR(!work.isUseMR());
                    bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                    return true;
                }
                if (updatetype.equals("VPZ")) {
                    work.setUseVPZ(!work.isUseVPZ());
                    bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                    return true;
                }
                if (updatetype.equals("FB")) {
                    work.setUseFB(!work.isUseFB());
                    bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                    return true;
                }
                if (updatetype.equals("MB")) {
                    work.setUseMB(!work.isUseMB());
                    bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                    return true;
                }
                if (updatetype.equals("CHIP")) {
                    work.setUseChip(!work.isUseChip());
                    bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                    return true;
                }
            }
            if (remtype.equals("UpdateToner")) {
                String updatetype = spldata[4];
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Тонер обновлён!"));
                if (updatetype.equals("Pour")) {
                    Integer pour = bot.pi(spldata[5]);
                    work.remTonerAmount(pour);
                    bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                    return true;
                }
                if (updatetype.equals("FillUp")) {
                    Integer add = bot.pi(spldata[5]);
                    work.addTonerAmount(add);
                    bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                    return true;
                }
            }
            if (remtype.equals("RECOVERY")) {
                if (work.isNeedRecovery()) {
                    if (work.isUseRecovery()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Зявка на восстановление уже была передана менеджеру.").setShowAlert(true));
                    } else {
                        work.addDescription("REQUESTED_RECOVERY|" + user_id);
                        work.addDescription("RECOVERY_USE");
                        SubOrder subOrder = new SubOrder(work.getOrderID());
                           /* bot.sendMsgToUser(userId, "Мастер просит согласовать восстановление по заявке №" + subOrder.getOrderID()
                                    + " и катриджу " + subOrder.getModel() + "(№" + subOrder.getSubOrderID() + ") и работе №" + work.getWorkID() + ".\nКомпоненты на замену: " + work.getNeedRecoveryComponentsName(), "Manager/Reconciliation/AllSubOrders/" + subOrder.getOrderID());
                            */
                        bot.deleteAllLastVosstMsg(new SubOrder(work.getOrderID()).getOrderID());
                        String iddata = DataBase.getUFileds(20, "val1");
                        List<Long> usersID = new ArrayList<Long>();
                        if (iddata != null && !iddata.equals("") && !iddata.equals(" "))
                            usersID = Arrays.stream(iddata.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
                        for (Long id : usersID) {
                            UsersData.addSelectedOrderForReconcile(id, work.getOrderID(), UsersData.ReconcileAnswer.Reconcile);
                            System.out.println("AddSubOrderIDForUser=" + id + "ss+" + work.getOrderID());
                         //   bot.addSelectedOrderForReconsile(id, work.getOrderID());
                        }
                        bot.updateVosstMsg(new SubOrder(work.getOrderID()).getOrderID(), "Мастер просит согласовать восстановление по заявке №" + subOrder.getOrderID()
                                    + " и катриджу " + subOrder.getModel() + "(№" + subOrder.getSubOrderID() + ") и работе №" + work.getWorkID() + ".\nКомпоненты на замену: " + work.getNeedRecoveryComponentsName());
                        bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                        subOrder.setStatus("Требует согласования");
                        bot.updateMainOrderMessage(new SubOrder(work.getOrderID()).getOrderID());
                    }
                } else {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Сначала выберите какие компоненты нужно заменить, потом нажмите кнопку 'Согласовать'").setShowAlert(true));
                }
                return true;
            }
            if (remtype.equals("Complete")) {
                if (work.isNeedRecovery() && !work.isUseRecovery()) {
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Работа не может быть завершена\nбез согласования ремонта.").setShowAlert(true));
                    return true;
                }
                if (!work.isNeedRecovery() || (work.isUseRecovery() && work.isConfirmRecovery())) {
                    work.addDescription("COMPLETED|" + user_id);
                    work.addDescription("COMPLETE_WORK");
                    work.setEndDate(new Date());
                    work.setEndTime(new Date());
                    new SubOrder(work.getOrderID()).setStatus("Готова к выезду");
                    Boolean is_all = true;
                    for (Long subOrdersID : new Order(new SubOrder(work.getOrderID()).getOrderID()).getAllSubOrdersID()) {
                        SubOrder sor = new SubOrder(subOrdersID);
                        bot.info(sor.getStatus());
                        if (sor.getStatus() == null || !sor.getStatus().equals("Готова к выезду")) {
                            is_all = false;
                            break;
                        }
                    }
                    if (is_all) {
                        new Order(new SubOrder(work.getOrderID()).getOrderID()).setAccurateStatus("Готова к выезду");
                        OrderLocations.addReadyToWay(new SubOrder(work.getOrderID()).getOrderID());
                        SqlServer.setKodStatus( new Order(new SubOrder(work.getOrderID()).getOrderID()),58);
                    }
                    DataBase.saveOrderInSheet(work.getOrderID(), "WORK_END");
                    bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                    bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Работа #" + workID + " #Z" + work.getOrderID() + " завершена.").setShowAlert(true));
                    bot.updateMainOrderMessage(new SubOrder(work.getOrderID()).getOrderID());
                    return true;
                } else {
                    if (work.isNeedRecovery() && work.isUseRecovery() && !work.isConfirmRecovery()) {
                        work.addDescription("COMPLETED|" + user_id);
                        work.addDescription("COMPLETE_WORK");
                        work.setEndDate(new Date());
                        work.setEndTime(new Date());
                        new SubOrder(work.getOrderID()).setStatus("Готова к выезду");
                        Boolean is_all = true;
                        for (Long subOrdersID : new Order(new SubOrder(work.getOrderID()).getOrderID()).getAllSubOrdersID()) {
                            SubOrder sor = new SubOrder(subOrdersID);
                            bot.info(sor.getStatus());
                            if (sor.getStatus() == null || !sor.getStatus().equals("Готова к выезду")) {
                                is_all = false;
                                break;
                            }
                        }
                        if (is_all) {
                            new Order(new SubOrder(work.getOrderID()).getOrderID()).setAccurateStatus("Готова к выезду");
                            OrderLocations.addReadyToWay(new SubOrder(work.getOrderID()).getOrderID());
                            SqlServer.setKodStatus( new Order(new SubOrder(work.getOrderID()).getOrderID()),58);
                        }
                        DataBase.saveOrderInSheet(work.getOrderID(),"WORK_END");
                        bot.editMsg(Long.parseLong(bot.getMasterChannelID()), msgid, bot.mcb.getMainZButtons(work));
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Работа #" + workID + " #Z" + work.getOrderID() + " завершена.").setShowAlert(true));
                        bot.updateMainOrderMessage(new SubOrder(work.getOrderID()).getOrderID());
                        return true;
                    }
                }
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Нельзя завершить работу с данными параметрами").setShowAlert(true));
                return true;
            }
            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Инструкция не найдена."));
            return true;
        }
        return false;
    }
}
