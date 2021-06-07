package ua.darkphantom1337.koi.kh.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.DataBase;
import ua.darkphantom1337.koi.kh.OrderLocations;
import ua.darkphantom1337.koi.kh.UsersData;
import ua.darkphantom1337.koi.kh.database.TidToUidTable;
import ua.darkphantom1337.koi.kh.entitys.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
        List<InlineKeyboardButton> r1 = new ArrayList<>(), r2 = new ArrayList<>(), r3 = new ArrayList<>(),
                r4 = new ArrayList<>();
        int kk = 0;
        for (Long courierID : DataBase.getAllPersonalIDWhere("courier")) {
            if (kk == 3)
                break;
            r1.add(new InlineKeyboardButton().setText(new Courier(courierID).getUser().getUserName()).setCallbackData("#Manager/Routes/MultiFunctional/SendOrderToCourier/" + courierID));
            kk++;
        }
        r1.add(new InlineKeyboardButton().setText("Ч1").setCallbackData("#Manager/Routes/MultiFunctional/SendOrderToDraft/1"));
        r1.add(new InlineKeyboardButton().setText("Ч2").setCallbackData("#Manager/Routes/MultiFunctional/SendOrderToDraft/2"));
        r1.add(new InlineKeyboardButton().setText("Ч3").setCallbackData("#Manager/Routes/MultiFunctional/SendOrderToDraft/3"));
        r2.add(new InlineKeyboardButton().setText("Выезд").setCallbackData("#Manager/Routes/MultiFunctional/Departure"));
        r2.add(new InlineKeyboardButton().setText("Вернуть в офис").setCallbackData("#Manager/Routes/MultiFunctional/SendOrdersToOffice"));
        r2.add(new InlineKeyboardButton().setText("Завершить").setCallbackData("#Manager/Routes/MultiFunctional/EndOrders"));
        r3.add(new InlineKeyboardButton().setText("Сбор").setCallbackData("#Manager/Routes/MultiFunctional/CollectOrders"));
        r3.add(new InlineKeyboardButton().setText("Доставка").setCallbackData("#Manager/Routes/MultiFunctional/DeliveryOrders"));
        r3.add(new InlineKeyboardButton().setText("В работе").setCallbackData("#Manager/Routes/MultiFunctional/ToWorkOrders"));
        r4.add(new InlineKeyboardButton().setText("Ожидает сбор").setCallbackData("#Manager/Routes/MultiFunctional/WaitCollectOrders"));
        r4.add(new InlineKeyboardButton().setText("Ожидает доставку").setCallbackData("#Manager/Routes/MultiFunctional/WaitDeliveryOrders"));
        r4.add(new InlineKeyboardButton().setText("Оповестить").setCallbackData("#Manager/Routes/MultiFunctional/Notify"));
        //r4.add(new InlineKeyboardButton().setText("Верн. статус").setCallbackData("#Manager/Routes/MultiFunctional/BackStatus"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        rowList.add(r2);
        rowList.add(r3);
        rowList.add(r4);
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

    public static InlineKeyboardMarkup getAdminMailWhenButtons(Long mailID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Сейчас").setCallbackData("#ADMIN/Mails/SetWhen/"+mailID+"/NOW"));
        r1.add(new InlineKeyboardButton().setText("Указать дату").setCallbackData("#ADMIN/Mails/SetWhen/"+mailID+"/PLANNED"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    public static InlineKeyboardMarkup getAdminMailStartOrSetParamaetersButtons(Long mailID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Запустить").setCallbackData("#ADMIN/Mails/START/"+mailID));
        r1.add(new InlineKeyboardButton().setText("Указать параметры").setCallbackData("#ADMIN/SetParameters/"+mailID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getAdminMailSelectParametersButtons(Long mailID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>(),r2 = new ArrayList<InlineKeyboardButton>(),r3 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Адрес").setCallbackData("#ADMIN/Mails/Parameter/Address/"+mailID));
        r1.add(new InlineKeyboardButton().setText("Картридж").setCallbackData("#ADMIN/Mails/Parameter/Cartridge/"+mailID));
        r1.add(new InlineKeyboardButton().setText("Телефон").setCallbackData("#ADMIN/Mails/Parameter/Phone/"+mailID));
        r2.add(new InlineKeyboardButton().setText("Изменить текст").setCallbackData("#ADMIN/Mails/Parameter/ChangeText/"+mailID));
        r2.add(new InlineKeyboardButton().setText("Изменить дату").setCallbackData("#ADMIN/Mails/Parameter/ChangeDate/"+mailID));
        r3.add(new InlineKeyboardButton().setText("Вернуться назад").setCallbackData("#ADMIN/Mails/Parameter/BaskStep/"+mailID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        rowList.add(r2);
        rowList.add(r3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getAdminMailCurrentButtons(Long mailID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Удалить рассылку").setCallbackData("#ADMIN/Mails/Delete/"+mailID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getAdminMailWaitStartsButtons(Long mailID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Запустить").setCallbackData("#ADMIN/Mails/START/"+mailID));
        r1.add(new InlineKeyboardButton().setText("Удалить рассылку").setCallbackData("#ADMIN/Mails/Delete/"+mailID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getAdminMailCompletedButtons(Long mailID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Отменить рассылку").setCallbackData("#ADMIN/Mails/CANCEL/"+mailID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(r1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getPreReconcileConfirmButton(String orderID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> r1 = new ArrayList<InlineKeyboardButton>();
        r1.add(new InlineKeyboardButton().setText("Скрыть").setCallbackData("#Manager/Reconciliation/HideReconcile/" + orderID));
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
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        HashMap<Long, UsersData.ReconcileAnswer> selectedSubOrders = UsersData.getSelectedOrdersForReconcile(userID);
        for (Long subOrderID : new Order(orderID).getAllSubOrdersID()) {
            SubOrder subOrder = new SubOrder(subOrderID);
            List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
            row.add(new InlineKeyboardButton().setText(subOrder.getModel() + (subOrder.getCartridgeID() != 0 ? " (" + subOrder.getCartridge().getModel() + "-" + subOrder.getCartridgeID() + ")" : " "))
                    .setCallbackData("#Manager/Reconciliation/SubOrder/" + subOrderID));
            row.add(new InlineKeyboardButton()
                    .setText(
                            (UsersData.hasSelectedSubOrderAnswer(userID, subOrderID, UsersData.ReconcileAnswer.Reconcile) ? "✅" : "")
                                    + "Сог.")
                    .setCallbackData("#Manager/Reconciliation/Select/Reconcile/" + subOrderID));
            row.add(new InlineKeyboardButton()
                    .setText(
                            (UsersData.hasSelectedSubOrderAnswer(userID, subOrderID, UsersData.ReconcileAnswer.ForcedRecovery) ? "✅" : "")
                                    + "Сог.Пр.")
                    .setCallbackData("#Manager/Reconciliation/Select/ForciblyReconcile/" + subOrderID));
            row.add(new InlineKeyboardButton()
                    .setText(
                            (UsersData.hasSelectedSubOrderAnswer(userID, subOrderID, UsersData.ReconcileAnswer.ForcedCancelRecovery) ? "✅" : "")
                                    + "Отк.Пр.")
                    .setCallbackData("#Manager/Reconciliation/Select/CancelForciblyReconcile/" + subOrderID));
            rowList.add(row);
        }
        List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
        row.add(new InlineKeyboardButton().setText("Провести").setCallbackData("#Manager/Reconciliation/ConfirmReconcile/" + orderID));
        row.add(new InlineKeyboardButton().setText("Скрыть").setCallbackData("#Manager/Reconciliation/HideReconcile/" + orderID));
        rowList.add(row);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public void getYesNoButt() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> ryad = new ArrayList<InlineKeyboardButton>();
        ryad.add(new InlineKeyboardButton().setText("Да").setCallbackData("Да"));
        ryad.add(new InlineKeyboardButton().setText("Нет").setCallbackData("Нет"));
        //inlineKeyboardMarkup.setKeyboard(ryad);
    }

    public static InlineKeyboardMarkup getOstZPrinterForPriceListButtons(String id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Заправить " + DataBase.getF("printer", Bot.bot.pi(id))).setCallbackData("#ZaprPrinter/" + DataBase.getF("printer", Bot.bot.pi(id)) + "/" + DataBase.getF("cartrige", Bot.bot.pi(id))));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getReklamaciaButtons(Integer nz, Long id, String status) {
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

    public static InlineKeyboardMarkup getReSearchPrice() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Повторить поиск").setCallbackData("#ReSearchPrice"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getOstZPrinterForPriceKListButtons(String id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Заправить " + DataBase.getF("cartrige", Bot.bot.pi(id))).setCallbackData("#ZaprPrinter/" + DataBase.getF("printer", Bot.bot.pi(id)) + "/" + DataBase.getF("cartrige", Bot.bot.pi(id))));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCancelVvodAdm() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Не вводить данные, компании").setCallbackData("#CancelVvodAdm"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getProvInfoButtons() {
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


    public static InlineKeyboardMarkup getCorporationButtons(List<Long> all_corporations, String data) {
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

    public static InlineKeyboardMarkup getPersonalDeleteButtons(List<Long> personal) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        for (Long personID : personal) {
            List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
            row.add(new InlineKeyboardButton().setText(DataBase.getPerFields(personID, "name") + " " + DataBase.getPerFields(personID, "position")).setCallbackData("#ADMIN/Personal/DeleteEmployee/" + personID));
            rowList.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCorporationEmployeesListButtons(Long corporationID, String data) {
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

    public static InlineKeyboardMarkup getAdminCorporationsDeleteMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        for (Long corporationID : Bot.bot.getAllCorporationsID()) {
            List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
            row.add(new InlineKeyboardButton().setText(new Corporation(corporationID).getName()).setCallbackData("#ADMIN/CORP/" + corporationID + "/Delete"));
            rowList.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCorporationAdressListButtons(Long corporationID, String data) {
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

    public static InlineKeyboardMarkup getPrintModelsButtons() {
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

    public static InlineKeyboardMarkup getPrintTypeButtons() {
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

    public static InlineKeyboardMarkup getTypeButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Я представитель компании").setCallbackData("#ЯКомпания"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Я частное лицо").setCallbackData("#ЯЧастник"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getICZAButtons() {
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

    public static InlineKeyboardMarkup getWhatMessengerV() {
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

    public static InlineKeyboardMarkup getWhatMessengerT() {
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

    public static InlineKeyboardMarkup getWhatMessengerAll() {
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

    public static InlineKeyboardMarkup getWhatMessenger() {
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

    public static InlineKeyboardMarkup getZaprKt() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Оставить заявку на заправку").setCallbackData("#SaveZayav"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Спасибо, пока что не нужно").setCallbackData("#ThankYouNo"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getNapomButtons(Integer msgid, String type) {
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

    public static InlineKeyboardMarkup getUpdateStatusButton(Long nz) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Обновить статус").setCallbackData("#UPDATEORDERSTATUS/" + nz));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getAMenuButtons() {
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

    public static InlineKeyboardMarkup getSoglasButton(String subOrdersID) {
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

    public static InlineKeyboardMarkup getMButton(String subOrdersID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Отправить").setCallbackData("#SendMsgForNz=" + subOrdersID));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getM1Button(Integer nz) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("☢ ☢ ☢ ОТПРАВЛЕНО ☢ ☢ ☢").setCallbackData("#ПофигGetM1Button"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getNapomDaysButton() {
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

    public static InlineKeyboardMarkup getNapomDaysTwoButton() {
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


    public static InlineKeyboardMarkup getMenedButton() {
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

    public static InlineKeyboardMarkup getGoogleMapButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(), keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Показать на карте Google Maps").setUrl("https://goo.gl/maps/5BqcDzCb5VPyzj8V6"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Позвонить нам (ЭТО БЕСПЛАТНО)").setUrl("http://ay.dn.ua/FreeCallToKoi.html"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getObrobotkaButton(Long nomer) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(" \uD83D\uDD34  \uD83D\uDD34  \uD83D\uDD34 - ПРИНЯТЬ ЗАЯВКУ-  \uD83D\uDD34  \uD83D\uDD34  \uD83D\uDD34").setCallbackData("#PRINYAT-Z=" + nomer));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getObrobotkaReklamButton(Long nomer) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(" \uD83D\uDD34  \uD83D\uDC49 - Заявка принята -  \uD83D\uDC48 \uD83D\uDD34").setCallbackData("#REKLAM_HAND/" + nomer));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCellButton(Long nomer) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(" \uD83D\uDD34  \uD83D\uDD34  \uD83D\uDD34 - ОБЗВОНИЛ -  \uD83D\uDD34  \uD83D\uDD34  \uD83D\uDD34").setCallbackData("#OBZVON-Z=" + nomer));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCancelZayavButtons(Long nomer) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ПОДТВЕРДИТЬ").setCallbackData("#CONFIRM_CANCEL_ZAYAV/" + nomer));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ОТКЛОНИТЬ").setCallbackData("#OTKLON_CANCEL_ZAYAV/" + nomer));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public static InlineKeyboardMarkup getCellTrueButton(int id) {
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

    public static InlineKeyboardMarkup getObrobotkaTrueButton(Integer nz) {
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

    public static boolean containsStatus(List<String> statuses, String status) {
        for (String s : statuses)
            if (s.contains(status))
                return true;
        return false;
    }

    public static InlineKeyboardMarkup getEndZayavButton(int nz) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Доставка").setCallbackData("#ChangeZStatus/Доставка_в_пути"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ЗАВЕРШИТЬ ЗАЯВКУ").setCallbackData("#ENDZFORVOSST/" + nz));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCurrentZButtons() {
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

    public static InlineKeyboardMarkup getObrobotkaTrueTrueButton() {
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

    public static InlineKeyboardMarkup getOprosButton(Long nz) {
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

    public static InlineKeyboardMarkup getOprButton(String type, Integer nomer) {
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

    public static InlineKeyboardMarkup getSpasiboZaRateButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Спасибо! Ваша оценка поможет нам стать лучше.").setCallbackData("#СпасибоЗаОценку"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getButText(String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(text).setCallbackData("#IGNORED"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getOcenkiButtons(int zn) {
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

    public static InlineKeyboardMarkup getNPBt() {
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

    public static InlineKeyboardMarkup getTr(String s) {
        return new InlineKeyboardMarkup().setKeyboard(Arrays.asList(Arrays.asList(
                new InlineKeyboardButton[]{new InlineKeyboardButton().setText(s).setCallbackData("#NP").setSwitchInlineQueryCurrentChat("Готово!")
                })));
    }

}
