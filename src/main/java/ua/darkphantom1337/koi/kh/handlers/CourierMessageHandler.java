package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.buttons.InlineButtons;
import ua.darkphantom1337.koi.kh.entitys.Courier;
import ua.darkphantom1337.koi.kh.entitys.Order;
import ua.darkphantom1337.koi.kh.entitys.Task;
import ua.darkphantom1337.koi.kh.entitys.User;

public class CourierMessageHandler {

    private Bot bot = Bot.bot;

    public boolean handleTextMessage(User user, String text, Integer message_id) {
        Long user_id = user.getUID();
        if (handleCommand(user, text)) return true;
        if (handleNotPreparedMessage(user, text)) return true;
        if (text.equals("Главное меню")) {
            bot.sendMsgToUser(user.getTID(), "Вы перешли в главное меню \uD83D\uDC4C", "Courier/Main");
            return true;
        }

        return false;
    }


    public boolean handleCommand(User user, String text) {
       /* if (bot.ah.handleNEWZCommand(user, text, "Manager")) return true;
        if (bot.ah.handleRECOVERYCommand(user, text, "Manager")) return true;
        if (bot.ah.handleReklamaciaCommand(user, text, "Manager")) return true;*/
        return false;
    }

    public boolean handleNotPreparedMessage(User user, String text) {
        Long user_id = user.getUID();
        return false;
    }

    public static boolean handleCallback(User user, String data, Integer msgid, String callid) {
        if (data.startsWith("#Courier/Task/")) {
            Task task = new Task(Long.parseLong(data.split("/")[2]));
            Order order = new Order(task.getOrderID());
            Courier courier = new Courier(task.getCourierID());
            String submenu = data.split("/")[3];
            if (submenu.equals("PreResult")) {
                String result = data.split("/")[4];
                if (result.equals("BACK"))
                    Bot.bot.editMsg(user.getTID(), msgid, InlineButtons.getCourierTaskConfirmBackButtons(task.getTaskID()));
                if (result.equals("READY"))
                    Bot.bot.editMsg(user.getTID(), msgid, InlineButtons.getCourierTaskConfirmReadyButtons(task.getTaskID()));
                if (result.equals("NOTTAKE"))
                    Bot.bot.editMsg(user.getTID(), msgid, InlineButtons.getCourierTaskConfirmNotTakeButtons(task.getTaskID()));
                Bot.bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Подтвердите действие!"));
                return true;
            }
            if (submenu.equals("ConfirmedResult")) {
                String result = data.split("/")[4];
                if (result.equals("BACK")) {
                    task.setTaskStatus("Возврат в офис");
                    courier.remCurrentTaskID(task.getTaskID());
                    courier.addCompletedTaskID(task.getTaskID());
                    Bot.bot.deleteMsg(user.getTID(), msgid);
                }
                if (result.equals("READY")){
                    task.setTaskStatus("Выполнено");
                    courier.remCurrentTaskID(task.getTaskID());
                    courier.addCompletedTaskID(task.getTaskID());
                    Bot.bot.deleteMsg(user.getTID(), msgid);
                }
                if (result.equals("NOTTAKE")){
                    task.setTaskStatus("Не забрал");
                    courier.remCurrentTaskID(task.getTaskID());
                    courier.addCompletedTaskID(task.getTaskID());
                    Bot.bot.deleteMsg(user.getTID(), msgid);
                }
                Bot.bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Действие выполнено!"));
                return true;
            }
            if (submenu.equals("BackToMain")) {
                if (order.getAccurateStatus().equals("Сбор"))
                    Bot.bot.editMsg(user.getTID(), msgid, InlineButtons.getCourierTaskPreCollectionButtons(task.getTaskID()));
                else if (order.getAccurateStatus().equals("Доставка"))
                    Bot.bot.editMsg(user.getTID(), msgid, InlineButtons.getCourierTaskPreDeliveryButtons(task.getTaskID()));
                else
                    Bot.bot.editMsg(user.getTID(), msgid, InlineButtons.getCourierTaskPreCollectionButtons(task.getTaskID()));
                Bot.bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Действие отменено!"));
                return true;
            }
        }
        return false;
    }

}
