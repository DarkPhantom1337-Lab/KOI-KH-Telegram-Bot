package ua.darkphantom1337.koi.kh.buttons;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.DataBase;
import ua.darkphantom1337.koi.kh.entitys.Courier;
import ua.darkphantom1337.koi.kh.entitys.Order;
import ua.darkphantom1337.koi.kh.entitys.SubOrder;

import java.util.ArrayList;
import java.util.List;

public class InlineButtons {

    private Bot bot = Bot.bot;

    public static InlineKeyboardMarkup getAllVacanciesButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>(), r2 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Администратор").setCallbackData("#ADMIN/SetPosition/admin"));
        r1.add(new InlineKeyboardButton().setText("Менеджер").setCallbackData("#ADMIN/SetPosition/manager"));
        r2.add(new InlineKeyboardButton().setText("Мастер").setCallbackData("#ADMIN/SetPosition/master"));
        r2.add(new InlineKeyboardButton().setText("Курьер").setCallbackData("#ADMIN/SetPosition/courier"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        rowList.add(r2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getManagerRoutesSelectOrderButton(Long orderID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Выбрать").setCallbackData("#Manager/Routes/SelectOrder/" + orderID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getManagerRoutesUnSelectOrderButton(Long orderID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Убрать").setCallbackData("#Manager/Routes/UnselectOrder/" + orderID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCourierTaskPreCollectionButtons(Long taskID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("❌ Не забрал ❌").setCallbackData("#Courier/Task/" + taskID + "/PreResult/NOTTAKE"));
        r1.add(new InlineKeyboardButton().setText("✅ Готово ✅").setCallbackData("#Courier/Task/" + taskID + "/PreResult/READY"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCourierTaskPreDeliveryButtons(Long taskID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("❌ Возврат ❌").setCallbackData("#Courier/Task/" + taskID + "/PreResult/BACK"));
        r1.add(new InlineKeyboardButton().setText("✅ Готово ✅").setCallbackData("#Courier/Task/" + taskID + "/PreResult/READY"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCourierTaskConfirmReadyButtons(Long taskID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("✅ Подтвердить готовность ✅").setCallbackData("#Courier/Task/" + taskID + "/ConfirmedResult/READY"));
        r1.add(new InlineKeyboardButton().setText("❌ Отменить ❌").setCallbackData("#Courier/Task/" + taskID + "/BackToMain"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCourierTaskConfirmBackButtons(Long taskID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("✅ Подтвердить возврат ✅").setCallbackData("#Courier/Task/" + taskID + "/ConfirmedResult/BACK"));
        r1.add(new InlineKeyboardButton().setText("❌ Отменить ❌").setCallbackData("#Courier/Task/" + taskID + "/BackToMain"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public static InlineKeyboardMarkup getCourierTaskConfirmNotTakeButtons(Long taskID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("✅ Подтвердить незабор ✅").setCallbackData("#Courier/Task/" + taskID + "/ConfirmedResult/NOTTAKE"));
        r1.add(new InlineKeyboardButton().setText("❌ Отменить ❌").setCallbackData("#Courier/Task/" + taskID + "/BackToMain"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getManagerMultiFunctionalMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<>(), r2 = new ArrayList<>(), r3 = new ArrayList<>();
        int kk = 0;
        for (Long courierID : DataBase.getAllPersonalIDWhere("courier")) {
            if (kk == 3)
                break;
            r1.add(new InlineKeyboardButton().setText("К-р. " + new Courier(courierID).getUser().getUserName()).setCallbackData("#Manager/Routes/MultiFunctional/SendOrderToCourier/" + courierID));
            kk++;
        }
        r2.add(new InlineKeyboardButton().setText("Чер-ик №1").setCallbackData("#Manager/Routes/MultiFunctional/SendOrderToDraft/1"));
        r2.add(new InlineKeyboardButton().setText("Чер-ик №2").setCallbackData("#Manager/Routes/MultiFunctional/SendOrderToDraft/2"));
        r2.add(new InlineKeyboardButton().setText("Чер-ик №3").setCallbackData("#Manager/Routes/MultiFunctional/SendOrderToDraft/3"));
        r3.add(new InlineKeyboardButton().setText("Верн. статус").setCallbackData("#Manager/Routes/MultiFunctional/BackStatus"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        rowList.add(r2);
        rowList.add(r3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getManagerConfirmActionButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Подтвердить").setCallbackData("#Manager/Routes/MultiFunctional/ConfirmAction"));
        r1.add(new InlineKeyboardButton().setText("Отменить").setCallbackData("#Manager/Routes/MultiFunctional/CancelAction"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getAllSubOrdersButtons(Long orderID, Long userID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>(),
                r2 = new ArrayList<InlineKeyboardButton>(),
                r3 = new ArrayList<InlineKeyboardButton>(),
                r4 = new ArrayList<InlineKeyboardButton>(),
                r5 = new ArrayList<InlineKeyboardButton>();
        int added = 1;
        List<Long> selected = Bot.bot.getSelectedOrderForReconsile(userID);
        for (Long subOrderID : new Order(orderID).getAllSubOrdersID()) {
            SubOrder subOrder = new SubOrder(subOrderID);
            if (added <= 3)
                r1.add(new InlineKeyboardButton().setText((selected.contains(subOrderID) ? "✅" : "") + subOrder.getModel() + (subOrder.getCartridgeID() != 0 ? " (" + subOrder.getCartridge().getModel() + "-" + subOrder.getCartridgeID() + ")" : " ")).setCallbackData("#Manager/Reconciliation/SelectSubOrder/" + subOrderID));
            else if (added <= 6)
                r2.add(new InlineKeyboardButton().setText((selected.contains(subOrderID) ? "✅" : "") + subOrder.getModel() + (subOrder.getCartridgeID() != 0 ? " (" + subOrder.getCartridge().getModel() + "-" + subOrder.getCartridgeID() + ")" : " ")).setCallbackData("#Manager/Reconciliation/SelectSubOrder/" + subOrderID));
            else
                r3.add(new InlineKeyboardButton().setText((selected.contains(subOrderID) ? "✅" : "") + subOrder.getModel() + (subOrder.getCartridgeID() != 0 ? " (" + subOrder.getCartridge().getModel() + "-" + subOrder.getCartridgeID() + ")" : " ")).setCallbackData("#Manager/Reconciliation/SelectSubOrder/" + subOrderID));
        }
        r4.add(new InlineKeyboardButton().setText("Согласовать принудительно").setCallbackData("#Manager/Reconciliation/ForciblyReconcile/"+orderID));
        r4.add(new InlineKeyboardButton().setText("Отказ принудительно").setCallbackData("#Manager/Reconciliation/CancelForciblyReconcile/"+orderID));
        r5.add(new InlineKeyboardButton().setText("Согласовать").setCallbackData("#Manager/Reconciliation/Reconcile/"+orderID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        rowList.add(r2);
        rowList.add(r3);
        rowList.add(r4);
        rowList.add(r5);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

}
