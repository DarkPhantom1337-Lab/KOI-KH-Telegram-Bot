package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.DataBase;
import ua.darkphantom1337.koi.kh.OrderLocations;
import ua.darkphantom1337.koi.kh.buttons.InlineButtons;
import ua.darkphantom1337.koi.kh.entitys.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ManagerHandler {

    private Bot bot;
    private HashMap<Long, Integer> selected_z = new HashMap<>();
    private List<Long> managers_wait_new_z_model = new ArrayList<>();
    private List<Long> managers_wait_new_z_address = new ArrayList<Long>();

    private HashMap<Long, List<Long>> selected_orders = new HashMap<>();
    private HashMap<Long, List<Long>> catMsgsId = new HashMap<>();
    private HashMap<Long, String> selected_category = new HashMap<>();
    private HashMap<Long, String> selected_action = new HashMap<>();


    public void addSelectedOrder(Long managerID, Long selectedOrder) {
        List<Long> selectedOrders = new ArrayList<>();
        if (selected_orders.containsKey(managerID))
            selectedOrders = selected_orders.get(managerID);
        if (!selectedOrders.contains(selectedOrder))
            selectedOrders.add(selectedOrder);
        selected_orders.remove(managerID);
        selected_orders.put(managerID, selectedOrders);
    }

    public void remSelectedOrder(Long managerID, Long selectedOrder) {
        List<Long> selectedOrders = new ArrayList<>();
        if (selected_orders.containsKey(managerID))
            selectedOrders = selected_orders.get(managerID);
        selectedOrders.remove(selectedOrder);
        selected_orders.remove(managerID);
        selected_orders.put(managerID, selectedOrders);
    }

    public void removeAllSelectedOrders(Long managerID) {
        selected_orders.remove(managerID);
    }

    public List<Long> getAllSelectedOrders(Long managerID) {
        List<Long> selectedOrders = new ArrayList<>();
        if (selected_orders.containsKey(managerID))
            selectedOrders = selected_orders.get(managerID);
        return selectedOrders;
    }

    public void addSendedMsgID(Long managerID, Long selectedOrder) {
        List<Long> selectedOrders = new ArrayList<>();
        if (catMsgsId.containsKey(managerID))
            selectedOrders = catMsgsId.get(managerID);
        if (!selectedOrders.contains(selectedOrder))
            selectedOrders.add(selectedOrder);
        catMsgsId.remove(managerID);
        catMsgsId.put(managerID, selectedOrders);
    }

    public void remSendedMsgID(Long managerID, Long selectedOrder) {
        List<Long> selectedOrders = new ArrayList<>();
        if (catMsgsId.containsKey(managerID))
            selectedOrders = catMsgsId.get(managerID);
        selectedOrders.remove(selectedOrder);
        catMsgsId.remove(managerID);
        catMsgsId.put(managerID, selectedOrders);
    }

    public void removeAllSendedMsgsID(Long managerID) {
        catMsgsId.remove(managerID);
    }

    public List<Long> getAllSendedMsgsID(Long managerID) {
        List<Long> selectedOrders = new ArrayList<>();
        if (catMsgsId.containsKey(managerID))
            selectedOrders = catMsgsId.get(managerID);
        return selectedOrders;
    }

    public ManagerHandler(Bot bot) {
        this.bot = bot;
    }

    public boolean handleTextMessage(User user, String text, Integer message_id) {
        Long user_id = user.getUID();
        if (text.startsWith("!")) {
            if (handleManagerCommand(user, text)) return true;
        }
        if (handleNotPreparedMessage(user, text)) return true;
        if (text.equals("Перейти в меню пользователя")) {
            deleteAllSendedMsg(user);
            bot.sendMsgToUser(user.getTID(), "Вы перешли в главное меню пользователя \uD83D\uDCAA", "main");
            return true;
        }
        if (text.equals("Перейти в меню менеджера") || text.equals("Вернуться в меню менеджера")) {
            managers_wait_new_z_model.remove(user_id);
            managers_wait_new_z_address.remove(user_id);
            deleteAllSendedMsg(user);
            bot.sendMsgToUser(user.getTID(), "Вы перешли в меню менеджера \uD83D\uDE0E", "Manager/MainMenu");
            return true;
        }
        if (text.equals("Текущие заявки (M)")) {
            bot.sendMsgToUser(user.getTID(), "Текущие заявки (не завершённые) \uD83D\uDC47", "Manager/CurrentOrders");
            return true;
        }
        if (text.equals("Создать заявку")) {
            bot.sendMsgToUser(user.getTID(), "Для создания заявки используйте !NEWZ/НомерТелефонаКлиента/МодельКартриджа\uD83D\uDC47", "Manager/MainMenu");
            return true;
        }
        if (text.equals("Изменить модель")) {
            if (!selected_z.containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные по выбранной заявки не актуальны, повторите попытку.", "Manager/MainMenu");
                return true;
            }
            managers_wait_new_z_model.add(user_id);
            bot.sendMsgToUser(user.getTID(), "Напишите новую модель для заявки №" + selected_z.get(user_id) + " \uD83D\uDC47", "");
            return true;
        }
        if (text.equals("Согласиться на восстановление")) {
            if (!selected_z.containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные по выбранной заявки не актуальны, повторите попытку.", "Manager/MainMenu");
                return true;
            }
            bot.handVosst(new Order(selected_z.get(user_id)).getSubOrdersID().replaceAll(";", "/"), "Telegram", "SOGLASOVANO");
            bot.updateZStatus(selected_z.get(user_id), "Восстановление согласовано", "Вы подтвердили восстановления картриджа через телефон/месседжер.");
            bot.sendMsgToUser(user.getTID(), "Согласование по заявке " + selected_z.get(user_id) + " подтверждено принудительно \uD83D\uDC47", "");
            return true;
        }
        if (text.equals("Отказ от восстановления")) {
            if (!selected_z.containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные по выбранной заявки не актуальны, повторите попытку.", "Manager/MainMenu");
                return true;
            }
            bot.handVosst(new Order(selected_z.get(user_id)).getSubOrdersID().replaceAll(";", "/"), "Telegram", "CANCEL");
            bot.updateZStatus(selected_z.get(user_id), "Отказ от восстановления", "Вы отказались от восстановления картриджа через телефон/месседжер.");
            bot.sendMsgToUser(user.getTID(), "Согласование по заявке " + selected_z.get(user_id) + " ОТКАЗАНО принудительно \uD83D\uDC47", "");
            return true;
        }
        if (text.equals("Изменить адрес")) {
            if (!selected_z.containsKey(user_id)) {
                bot.sendMsgToUser(user.getTID(), "Данные по выбранной заявки не актуальны, повторите попытку.", "Manager/MainMenu");
                return true;
            }
            managers_wait_new_z_address.add(user_id);
            bot.sendMsgToUser(user.getTID(), "Напишите новый адрес для заявки №" + selected_z.get(user_id) + " \uD83D\uDC47", "");
            return true;
        }
        if (text.equals("Маршруты")) {
            deleteAllSendedMsg(user);
            bot.sendMsgToUser(user.getTID(), "Вы перешли в меню маршрутов, выберите категорию заявок ниже \uD83D\uDC47", "Manager/Routes/Main");
            return true;
        }
        if (text.equals("Все заявки")) {
            deleteAllSendedMsg(user);
            if (sendOrderMessages(user, OrderLocations.getAllCurrentOrdersID(), false)) {
                bot.sendMsgToUser(user.getTID(), "На данный момент актуальных заявок ещё нету \uD83E\uDD2A", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "AllOrders");
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены все текущие заявки которые не завершены. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        if (text.equals("В пути")) {
            deleteAllSendedMsg(user);
            if (sendOrderMessages(user, OrderLocations.getOrdersInTheWay(), false)) {
                bot.sendMsgToUser(user.getTID(), "Заявок которые либо едут в офис либо уже выехали с него ещё нету \uD83E\uDD2A", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "InTheWay");
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены заявки которые либо едут в офис либо уже выехали с него. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        if (text.equals("В офисе")) {
            deleteAllSendedMsg(user);
            if (sendOrderMessages(user, OrderLocations.getOrdersInOffice(), false)) {
                bot.sendMsgToUser(user.getTID(), "Заявок которые находяться в офисе ещё нету \uD83E\uDD2A", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "InTheOffice");
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены заявки которые находяться в офисе. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        if (text.equals("В работе")) {
            deleteAllSendedMsg(user);
            if (sendOrderMessages(user, OrderLocations.getOrdersInWork(), false)) {
                bot.sendMsgToUser(user.getTID(), "Заявок которые находяться в работе ещё нету \uD83E\uDD2A", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "InTheWork");
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены заявки которые находяться в офисе и над которыми уже начали работать. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        if (text.equals("Выезд")) {
            deleteAllSendedMsg(user);
            if (sendOrderMessages(user, OrderLocations.getReadyToWayOrders(), false)) {
                bot.sendMsgToUser(user.getTID(), "Заявок которые готовы к доставке клиенту ещё нету \uD83E\uDD2A", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "ReadyToWay");
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены заявки которые готовы к доставке клиенту. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        if (text.equals("Сбор")) {
            deleteAllSendedMsg(user);
            if (sendOrderMessages(user, OrderLocations.getOrdersInCollection(), false)) {
                bot.sendMsgToUser(user.getTID(), "Заявок которые едут в офис ещё нету \uD83E\uDD2A", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "Collection");
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены заявки которые едут в офис. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        if (text.equals("Доставка")) {
            deleteAllSendedMsg(user);
            if (sendOrderMessages(user, OrderLocations.getOrdersInDelivery(), false)) {
                bot.sendMsgToUser(user.getTID(), "Заявок которые доставляються клиенту ещё нету \uD83E\uDD2A", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "Delivery");
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены заявки которые доставляються клиенту. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        if (text.startsWith("|")) {
            deleteAllSendedMsg(user);
            String courierData = text.substring(1);
            Courier courier = new Courier(courierData.split("\\|")[0]);
            if (sendOrderMessages(user, courier.getAllCurrentTasksID(), true)) {
                bot.sendMsgToUser(user.getTID(), "Курьер '" + courier.getUser().getUserName() + "' в данный момент не выполняет заданий!", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "InTheCourier|" + courier.getCourierID());
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены заявки которые находяться у курьера '" + courier.getUser().getUserName() + "'. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        if (text.equals("Ч1")) {
            deleteAllSendedMsg(user);
            if (sendOrderMessages(user, OrderLocations.getOrdersInFirstDraft(), false)) {
                bot.sendMsgToUser(user.getTID(), "Заявок в первом черновике ещё нету \uD83E\uDD2A", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "InTheFirstDraft");
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены заявки которые находяться в первом черновике. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        if (text.equals("Ч2")) {
            deleteAllSendedMsg(user);
            if (sendOrderMessages(user, OrderLocations.getOrdersInSecondDraft(), false)) {
                bot.sendMsgToUser(user.getTID(), "Заявок во втором черновике ещё нету \uD83E\uDD2A", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "InTheSecondDraft");
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены заявки которые находяться во втором черновике. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        if (text.equals("Ч3")) {
            deleteAllSendedMsg(user);
            if (sendOrderMessages(user, OrderLocations.getOrdersInThirdDraft(), false)) {
                bot.sendMsgToUser(user.getTID(), "Заявок в третьем черновике ещё нету \uD83E\uDD2A", "Manager/Routes/Main");
                return true;
            }
            removeAllSelectedOrders(user.getUID());
            selected_category.put(user.getUID(), "InTheThirdDraft");
            addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), "Выше представлены заявки которые находяться в третьем черновике. Выберите их и выполните любое действие ниже \uD83D\uDC47", "Manager/Routes/MultiFunctionMenu"));
            return true;
        }
        return false;
    }

    public Boolean sendOrderMessages(User user, List<Long> orders, Boolean isTask) {
        if (orders.isEmpty())
            return true;
        for (Long orderID : orders) {
            Order order = null;
            if (!isTask) order = new Order(orderID);
            else order = new Order(new Task(orderID).getOrderID());
            if (order != null)
                addSendedMsgID(user.getUID(), (long) bot.sendMsgToUser(user.getTID(), getOrderText(order), "Manager/Routes/Order/" + orderID));
        }
        return false;
    }

    public String getOrderText(Order order) {
        String text = "Заявка №" + order.getOrderID() + " | К-р: " + order.getCourierName()
                + "\nСтатус: " + order.getAccurateStatus();
        List<Long> tasksID = order.getAllTasksID();
        if (!tasksID.isEmpty()) {
            text += "\nОтметки курьеров: ";
            for (Long taskID : tasksID) {
                Task task = new Task(taskID);
                text += "\n\uD83D\uDC49 " + getTextTaskType(task.getTaskType()) + " " + task.getCourier().getUser().getUserName() + " - " + task.getTaskStatus();
            }
        }
        User orderOwner = new User(order.getUID(), true);
        text += "\n" + orderOwner.getUserType() + ": "
                + (orderOwner.getUserType().equals("Компания") ? orderOwner.getUserCompanyName() + " (" + orderOwner.getUserName() + ")"
                : orderOwner.getUserName());
        text += "\nАдрес: " + order.getAddress();
        text += "\nМодель(и): " + order.getModel();
        return text;
    }

    public String getTextTaskType(TaskType type) {
        if (type == null)
            return "Не определено";
        if (type.equals(TaskType.COLLECT))
            return "Сбор";
        if (type.equals(TaskType.DELIVERY))
            return "Доставка";
        return "Другое";
    }

    public boolean handleManagerCommand(User user, String text) {
        if (bot.ah.handleNEWZCommand(user, text, "Manager")) return true;
        if (bot.ah.handleRECOVERYCommand(user, text, "Manager")) return true;
        if (bot.ah.handleReklamaciaCommand(user, text, "Manager")) return true;
        return false;
    }

    public boolean handleNotPreparedMessage(User user, String text) {
        Long user_id = user.getUID();
        if (managers_wait_new_z_model.contains(user_id)) {
            try {
                managers_wait_new_z_model.remove(user_id);
                String lastadr = new Order(selected_z.get(user_id)).getModel();
                DataBase.setZFields(selected_z.get(user_id), "model", text);
                Integer nza = selected_z.get(user_id);
                String type = DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "type");
                String status = new Order(nza).getString("status");
                String z_text = "Заявка /" + nza + "/" + "#Z" + nza
                        + "\nТема: #" + new Order(nza).getString("theme")
                        + "\nМодель: #" + new Order(nza).getString("model").replaceAll(" ", "_")
                        + "\nАдрес: #" + new Order(nza).getString("adress").replaceAll(" ", "_").replaceAll(",", "_")
                        + "\nТип: #" + type
                        + (type.equals("Компания") ? "\nНазвание: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "company_name").replaceAll(" ", "_") : "")
                        + "\nИмя: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "name").replaceAll(" ", "_")
                        + "\nНомер телефона: #Tel" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "phone")
                        + (DataBase.isPersonal(Long.parseLong(new Order(nza).getString("u_id"))) ? "\nПодал: " + DataBase.getPerFields(Long.parseLong(new Order(nza).getString("u_id")), "name") : "")
                        + "\nСтатус: /" + (status.equals("Поступила") ? status + "/" + " #Поступила" : "Принята - " + new Order(nza).getString("prinyal") + "/ #Принята - " + new Order(nza).getString("prinyal"));
                bot.editMsg(Long.parseLong(bot.getZayavChannelID()), bot.pi(new Order(nza).getString("main_msg_id")), z_text);
                bot.editMsg(Long.parseLong(bot.getZayavChannelID()), bot.pi(new Order(nza).getString("main_msg_id")), status.equals("Поступила") ? InlineButtons.getObrobotkaButton((long) nza) : InlineButtons.getObrobotkaTrueButton(nza));
                bot.sendMsgToUser(user.getTID(), "✅ Редактирование модели по заявке №" + selected_z.get(user_id) + " успешно завершено!"
                        + "\n" + lastadr + " -> " + text, "Manager/ZMenu/" + selected_z.get(user_id));
            } catch (Exception e) {
                bot.sendMsgToUser(user.getTID(), "❌ Редактирование модели по заявке №" + selected_z.get(user_id) + " НЕ БЫЛО ВЫПОЛНЕНО.ПОВТОРИТЕ ПОПЫТКУ.", "Manager/ZMenu/" + selected_z.get(user_id));
            }
            return true;
        }
        if (managers_wait_new_z_address.contains(user_id)) {
            try {
                managers_wait_new_z_address.remove(user_id);
                String lastadr = new Order(selected_z.get(user_id)).getAddress();
                DataBase.setZFields(selected_z.get(user_id), "adress", text);
                Integer nza = selected_z.get(user_id);
                String type = DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "type");
                String status = new Order(nza).getString("status");
                String z_text = "Заявка /" + nza + "/" + "#Z" + nza
                        + "\nТема: #" + new Order(nza).getString("theme")
                        + "\nМодель: #" + new Order(nza).getString("model").replaceAll(" ", "_")
                        + "\nАдрес: #" + text.replaceAll(" ", "_").replaceAll(",", "_")
                        + "\nТип: #" + type
                        + (type.equals("Компания") ? "\nНазвание: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "company_name").replaceAll(" ", "_") : "")
                        + "\nИмя: #" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "name").replaceAll(" ", "_")
                        + "\nНомер телефона: #Tel" + DataBase.getUsFileds(Long.parseLong(new Order(nza).getString("u_id")), "phone")
                        + (DataBase.isPersonal(Long.parseLong(new Order(nza).getString("u_id"))) ? "\nПодал: " + DataBase.getPerFields(Long.parseLong(new Order(nza).getString("u_id")), "name") : "")
                        + "\nСтатус: /" + (status.equals("Поступила") ? status + "/" + " #Поступила" : "Принята - " + new Order(nza).getString("prinyal") + "/ #Принята - " + new Order(nza).getString("prinyal"));
                bot.editMsg(Long.parseLong(bot.getZayavChannelID()), bot.pi(new Order(nza).getString("main_msg_id")), z_text);
                bot.editMsg(Long.parseLong(bot.getZayavChannelID()), bot.pi(new Order(nza).getString("main_msg_id")), status.equals("Поступила") ? InlineButtons.getObrobotkaButton((long) nza) : InlineButtons.getObrobotkaTrueButton(nza));

                bot.sendMsgToUser(user.getTID(), "✅ Редактирование адреса по заявке №" + selected_z.get(user_id) + " успешно завершено!"
                        + "\n" + lastadr + " -> " + text, "Manager/ZMenu/" + selected_z.get(user_id));
            } catch (Exception e) {
                bot.sendMsgToUser(user.getTID(), "❌ Редактирование адреса по заявке №" + selected_z.get(user_id) + " НЕ БЫЛО ВЫПОЛНЕНО.ПОВТОРИТЕ ПОПЫТКУ.", "Manager/ZMenu/" + selected_z.get(user_id));
            }
            return true;
        }
        return false;
    }

    public boolean handleCallback(User user, String data, Integer msgid, String callid) {
        if (data.startsWith("#ManagerEditZ")) {
            String nz = data.split("/")[1];
            selected_z.put(user.getUID(), bot.pi(nz));
            User user1 = new User(new Order(nz).getUID());
            bot.deleteMsg(user.getTID(), msgid);
            bot.sendMsgToUser(user.getTID(), "Вы в меню управления заявкой №" + nz
                    + " от " + user1.getUserName() + "(" + user1.getUserPhone() + ") Модель картриджа: " + new Order(nz).getModel(), "Manager/ZMenu");
            return true;
        }
        if (data.startsWith("#Manager/Routes/")) {
            String submenu = data.split("/")[2];
            if (submenu.equals("SelectOrder")) {
                Long orderID = Long.parseLong(data.split("/")[3]);
                addSelectedOrder(user.getUID(), orderID);
                bot.editMsg(user.getTID(), msgid, InlineButtons.getManagerRoutesUnSelectOrderButton(orderID));
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Заявка №" + orderID + " выбрана!"));
                return true;
            }
            if (submenu.equals("UnselectOrder")) {
                Long orderID = Long.parseLong(data.split("/")[3]);
                remSelectedOrder(user.getUID(), orderID);
                bot.editMsg(user.getTID(), msgid, InlineButtons.getManagerRoutesSelectOrderButton(orderID));
                bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Заявка №" + orderID + " убрана!"));
                return true;
            }
            if (submenu.equals("MultiFunctional")) {
                String ssubmenu = data.split("/")[3];
                if (ssubmenu.equals("ConfirmAction")) {
                    bot.deleteMsg(user.getTID(), msgid);
                    if (!selected_action.containsKey(user.getUID())) {
                        user.sendMessage("Данные о выбранном действии больше недоступны. Повторите процедуру заново.", "Manager/Routes/Main");
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Данные больше не актуальны!"));
                        return true;
                    }
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        user.sendMessage("Данные о выбранных заявках больше недоступны. Повторите процедуру заново.", "Manager/Routes/Main");
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    String action = selected_action.get(user.getUID());
                    if (action.equals("EndOrders")) {
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            Order order = new Order(selectedOrder);
                            OrderLocations.removeOrderFromAll(selectedOrder);
                            Bot.bot.pch.handleEndZayavAndVosst(user, "#ENDZAYAV/" + order.getOrderID(), 0, "", "");
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявки " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " были завершены.", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                    if (action.equals("Collect")) {
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            Order order = new Order(selectedOrder);
                            OrderLocations.removeOrderFromAll(selectedOrder);
                            Bot.bot.pch.handleChangeZStatus(user, "#ChangeZStatus/Сбор_в_пути/" + order.getOrderID(), 0, "", "");
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявкам " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " был назначем статус 'СБОР'.", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                    if (action.equals("Delivery")) {
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            Order order = new Order(selectedOrder);
                            OrderLocations.removeOrderFromAll(selectedOrder);
                            Bot.bot.pch.handleChangeZStatus(user, "#ChangeZStatus/Доставка_в_пути/" + order.getOrderID(), 0, "", "");
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявкам " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " был назначем статус 'ДОСТАВКА'.", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                    if (action.equals("ToWork")) {
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            Order order = new Order(selectedOrder);
                            OrderLocations.removeOrderFromAll(selectedOrder);
                            Bot.bot.pch.handleChangeZStatus(user, "#ChangeZStatus/Заявка_в_работе/" + order.getOrderID(), 0, "", "");
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявкам " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " был назначем статус 'В РАБОТЕ'.", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                    if (action.equals("Departure")) {
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            Order order = new Order(selectedOrder);
                            String accStatus = order.getAccurateStatus();
                            if (accStatus.equals("Принята") || accStatus.equals("Ожидает сбор")) {
                                OrderLocations.removeOrderFromAll(selectedOrder);
                                Bot.bot.pch.handleChangeZStatus(user, "#ChangeZStatus/Сбор_в_пути/" + order.getOrderID(), 0, "", "");
                            } else {
                                if (accStatus.equals("В работе") || accStatus.equals("Ожидает доставку") || accStatus.equals("Готова к выезду")) {
                                    OrderLocations.removeOrderFromAll(selectedOrder);
                                    Bot.bot.pch.handleChangeZStatus(user, "#ChangeZStatus/Доставка_в_пути/" + order.getOrderID(), 0, "", "");
                                }
                            }
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявкам " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " было выполнено действие 'ВЫЕЗД'.", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                    if (action.equals("ToOffice")) {
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            Order order = new Order(selectedOrder);
                            String accStatus = order.getAccurateStatus();
                            if (accStatus.equals("Сбор")) {
                                OrderLocations.removeOrderFromAll(selectedOrder);
                                OrderLocations.addOrderInOffice(selectedOrder);
                                order.setAccurateStatus("Ожидает сбор");
                            } else {
                                if (accStatus.equals("Доставка")) {
                                    OrderLocations.removeOrderFromAll(selectedOrder);
                                    OrderLocations.addOrderInOffice(selectedOrder);
                                    order.setAccurateStatus("Ожидает доставку");
                                }
                            }
                            bot.updateMainOrderMessage(selectedOrder);
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявкам " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " было выполнено действие 'ВЕРНУТЬ В ОФИС'.", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                    if (action.equals("WaitCollect")) {
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            Order order = new Order(selectedOrder);
                            order.setAccurateStatus("Ожидает сбор");
                            OrderLocations.removeOrderFromAll(selectedOrder);
                            OrderLocations.addOrderInOffice(selectedOrder);
                            bot.updateMainOrderMessage(selectedOrder);
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявкам " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " был назначен статус 'ОЖИДАЕТ СБОР'.", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                    if (action.equals("WaitDelivery")) {
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            Order order = new Order(selectedOrder);
                            order.setAccurateStatus("Ожидает доставку");
                            OrderLocations.removeOrderFromAll(selectedOrder);
                            OrderLocations.addOrderInOffice(selectedOrder);
                            bot.updateMainOrderMessage(selectedOrder);
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявкам " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " был назначен статус 'ОЖИДАЕТ ДОСТАВКУ'.", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                    if (action.startsWith("SendOrderToCourier")) {
                        Long courierID = Long.parseLong(action.split("%")[1]);
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            Task task = new Task(DataBase.getNextTaskID());
                            task.setCourierID(courierID);
                            task.setOrderID(selectedOrder);
                            task.setCreatingDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                            task.setCreatingDate(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                            new Order(selectedOrder).setCourierID(courierID);
                            new Order(selectedOrder).addTaskID(task.getTaskID());
                            new Courier(courierID).addCurrentTaskID(task.getTaskID());
                            OrderLocations.removeOrderFromAll(selectedOrder);
                            OrderLocations.addOrderInTheWay(selectedOrder);
                            if (isDelivery(new Order(selectedOrder).getAllStatuses())) {
                                OrderLocations.addOrdersInDelivery(selectedOrder);
                                task.setTaskType(TaskType.DELIVERY);
                            } else if (isCollect(new Order(selectedOrder).getAllStatuses())) {
                                OrderLocations.addOrdersInCollection(selectedOrder);
                                task.setTaskType(TaskType.COLLECT);
                            }
                            task.setOrderMessageID(bot.sendMsgToUser(new Courier(courierID).getUser().getTID(), getOrderText(new Order(selectedOrder)), "Courier/Task/" + task.getTaskID()));
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявки " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " были назначены курьеру '" + new Courier(courierID).getUser().getUserName() + "'.", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                    if (action.startsWith("SendOrderToDraft")) {
                        Integer draftNumber = Integer.parseInt(action.split("%")[1]);
                        if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                            return true;
                        }
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            OrderLocations.removeOrderFromAll(selectedOrder);
                            if (draftNumber == 1)
                                OrderLocations.addOrdersToFirstDraft(selectedOrder);
                            if (draftNumber == 2)
                                OrderLocations.addOrdersToSecondDraft(selectedOrder);
                            if (draftNumber == 3)
                                OrderLocations.addOrdersToThirdDraft(selectedOrder);
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявки " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " были перемещены в черновик № " + draftNumber + ".", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                    if (action.equals("BackStatus")) {
                        if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                            bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                            return true;
                        }
                        for (Long selectedOrder : getAllSelectedOrders(user.getUID())) {
                            Order order = new Order(selectedOrder);
                            OrderLocations.removeOrderFromAll(selectedOrder);
                            if (order.getAccurateStatus().equals("Принята"))
                                order.setAccurateStatus("Подана");
                            if (order.getAccurateStatus().equals("Сбор"))
                                order.setAccurateStatus("Принята");
                            if (order.getAccurateStatus().equals("Доставка")) {
                                OrderLocations.addOrderInOffice(selectedOrder);
                                OrderLocations.addReadyToWay(selectedOrder);
                                order.setAccurateStatus("Готова к выезду");
                            }
                            if (order.getAccurateStatus().equals("В работе")) {
                                OrderLocations.addOrderInOffice(selectedOrder);
                                order.setAccurateStatus("В офисе");
                            }
                            if (order.getAccurateStatus().equals("Готова к выезду")) {
                                OrderLocations.addOrderInOffice(selectedOrder);
                                order.setAccurateStatus("В офисе");
                            }
                            List<String> all_statuses = order.getAllStatuses();
                            all_statuses.remove(all_statuses.size() - 1);
                            order.setAllStatuses(all_statuses);
                            bot.editMsg(Long.parseLong(bot.getZayavChannelID()), order.getMainMsgID(), InlineButtons.getObrobotkaTrueButton(order.getOrderID().intValue()));
                            bot.updateZStatus(order.getOrderID().intValue(), all_statuses.get(all_statuses.size() - 1).replaceAll("[0-9/:]", ""), "");
                        }
                        deleteAllSendedMsg(user);
                        user.sendMessage("Заявки " + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + " были обновлены. Возвращён прошлый статус..", "Manager/Routes/Main");
                        removeAllSelectedOrders(user.getUID());
                        return true;
                    }
                }
                if (ssubmenu.equals("CancelAction")) {
                    bot.deleteMsg(user.getTID(), msgid);
                    if (!selected_action.containsKey(user.getUID())) {
                        user.sendMessage("Данные о выбранном действии больше недоступны. Повторите процедуру заново.", "Manager/Routes/Main");
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Данные больше не актуальны!"));
                        return true;
                    }
                    if (!selected_category.containsKey(user.getUID())) {
                        user.sendMessage("Данные о выбранной категории больше недоступны. Повторите процедуру заново.", "Manager/Routes/Main");
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Данные больше не актуальны!"));
                        return true;
                    }
                    String category = selected_category.get(user.getUID());
                    removeAllSelectedOrders(user.getUID());
                    if (category.equals("AllOrders")) {
                        handleTextMessage(user, "Все заявки", 0);
                        return true;
                    }
                    if (category.equals("InTheWay")) {
                        handleTextMessage(user, "В пути", 0);
                        return true;
                    }
                    if (category.equals("InTheOffice")) {
                        handleTextMessage(user, "В офисе", 0);
                        return true;
                    }
                    if (category.equals("InTheWork")) {
                        handleTextMessage(user, "В работе", 0);
                        return true;
                    }
                    if (category.equals("ReadyToWay")) {
                        handleTextMessage(user, "Выезд", 0);
                        return true;
                    }
                    if (category.equals("Collection")) {
                        handleTextMessage(user, "Сбор", 0);
                        return true;
                    }
                    if (category.equals("Delivery")) {
                        handleTextMessage(user, "Доставка", 0);
                        return true;
                    }
                    if (category.startsWith("InTheCourier")) {
                        handleTextMessage(user, "|" + category.split("\\|")[1] + "|КрName", 0);
                        return true;
                    }
                    if (category.equals("InTheFirstDraft")) {
                        handleTextMessage(user, "Чер-ик №1", 0);
                        return true;
                    }
                    if (category.equals("InTheSecondDraft")) {
                        handleTextMessage(user, "Чер-ик №2", 0);
                        return true;
                    }
                    if (category.equals("InTheThirdDraft")) {
                        handleTextMessage(user, "Чер-ик №3", 0);
                        return true;
                    }
                }
                if (ssubmenu.equals("SendOrderToCourier")) {
                    Long courierID = Long.parseLong(data.split("/")[4]);
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "SendOrderToCourier%" + courierID);
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите назначить заявки '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "' курьеру " + new Courier(courierID).getUser().getUserName() + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
                if (ssubmenu.equals("SendOrderToDraft")) {
                    Integer draftNumber = Integer.parseInt(data.split("/")[4]);
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "SendOrderToDraft%" + draftNumber);
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите переместить заявки '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "  в черновик № " + draftNumber + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
                if (ssubmenu.equals("BackStatus")) {
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "BackStatus");
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите вернуть статус заявкам '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
                if (ssubmenu.equals("EndOrders")) {
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "EndOrders");
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите завершить заявки '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
                if (ssubmenu.equals("CollectOrders")) {
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "Collect");
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите назначить статус 'СБОР' заявкам '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
                if (ssubmenu.equals("DeliveryOrders")) {
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "Delivery");
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите назначить статус 'ДОСТАВКА' заявкам '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
                if (ssubmenu.equals("ToWorkOrders")) {
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "ToWork");
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите назначить статус 'В РАБОТЕ' заявкам '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
                if (ssubmenu.equals("Departure")) {
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "Departure");
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите произвести 'ВЫЕЗД' заявкам '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
                if (ssubmenu.equals("SendOrdersToOffice")) {
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "ToOffice");
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите 'ВЕРНУТЬ В ОФИС' заявки '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
                if (ssubmenu.equals("WaitCollectOrders")) {
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "WaitCollect");
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите назначить статус 'ОЖИДАЕТ СБОР' заявки '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
                if (ssubmenu.equals("WaitDeliveryOrders")) {
                    if (getAllSelectedOrders(user.getUID()).isEmpty()) {
                        bot.tryExecureMethod(new AnswerCallbackQuery().setCallbackQueryId(callid).setText("Вы не выбрали заявки!"));
                        return true;
                    }
                    selected_action.put(user.getUID(), "WaitDelivery");
                    deleteAllSendedMsg(user);
                    user.sendMessage("Вы хотите назначить статус 'ОЖИДАЕТ ДОСТАВКУ' заявки '" + bot.u.objectToString(getAllSelectedOrders(user.getUID()), ";") + "? Подтвердите или отмените действие.", "Manager/Routes/ConfirmAction");
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    public String getActionConfirmText(String action) {
        return "";
    }


    public Boolean isCollect(List<String> statuses) {
        for (String s : statuses)
            if (s.contains("Курьер выехал к Вам (Сбор)"))
                return true;
        return false;
    }

    public Boolean isDelivery(List<String> statuses) {
        for (String s : statuses)
            if (s.contains("Курьер выехал к Вам (Доставка)"))
                return true;
        return false;
    }

    public void deleteAllSendedMsg(User user) {
        for (Long messageID : getAllSendedMsgsID(user.getUID()))
            try {
                bot.deleteMsg(user.getTID(), messageID.intValue());
            } catch (Exception ignored) {
            }
        removeAllSendedMsgsID(user.getUID());
    }
}
