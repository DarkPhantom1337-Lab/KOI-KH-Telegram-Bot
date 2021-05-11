package ua.darkphantom1337.koi.kh.buttons;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.DataBase;
import ua.darkphantom1337.koi.kh.UsersData;
import ua.darkphantom1337.koi.kh.entitys.Corporation;
import ua.darkphantom1337.koi.kh.entitys.Courier;
import ua.darkphantom1337.koi.kh.entitys.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ReplyButtons {

    private Bot bot = Bot.bot;

    public static ReplyKeyboardMarkup addMainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
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
        return replyKeyboardMarkup;
    }


    public static ReplyKeyboardMarkup addMyZayavMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Текущие заявки"));
        oneline.add(new KeyboardButton("Завершённые заявки"));
        twoline.add(new KeyboardButton("Главное меню"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addBackToMain() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Главное меню"));
        keyboard.add(oneline);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addMenAdrMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow();
        oneline.add(new KeyboardButton("Написать менеджеру"));
        oneline.add(new KeyboardButton("Адрес/Контакты"));
        keyboard.add(oneline);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addMainPersonalMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
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
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addMainUserManagerMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
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
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addMainAdminMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
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
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addAdminCorporationsMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
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
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addAdminQRMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
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
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addPersonalControlMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
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
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getSelectedOrderModels(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow sendOrderRow = new KeyboardRow(), backToMainRow = new KeyboardRow();
        Long chatID = user.getUID();
        List<String> models = UsersData.getOrderModels(chatID);
        List<String> selectedModels = UsersData.getSelectedOrderModels(chatID);
        sendOrderRow.add("Подать заявку");
        backToMainRow.add("Вернуться в главное меню");
        KeyboardRow row1 = new KeyboardRow(), row2 = new KeyboardRow(), row3 = new KeyboardRow();
        int added = 0;
        for (String model : models) {
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
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addAdminCorporationsSettingsMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
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
        return replyKeyboardMarkup;
    }


    public static ReplyKeyboardMarkup addMainManagerMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow();
        oneline.add(new KeyboardButton("Текущие заявки (M)"));
        oneline.add(new KeyboardButton("Создать заявку"));
        twoline.add(new KeyboardButton("Маршруты"));
        thrline.add(new KeyboardButton("Перейти в меню пользователя"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup  addManagerRoutesMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow(), thrline = new KeyboardRow(), thline = new KeyboardRow(), th1line = new KeyboardRow(), th2line = new KeyboardRow();
        oneline.add(new KeyboardButton("Все заявки"));
        oneline.add(new KeyboardButton("В пути"));
        oneline.add(new KeyboardButton("В офисе"));
        oneline.add(new KeyboardButton("В работе"));
        thrline.add(new KeyboardButton("Выезд"));
        thrline.add(new KeyboardButton("Сбор"));
        thrline.add(new KeyboardButton("Доставка"));
        int c = 0;
        for (Long userID : DataBase.getAllPersonalIDWhere("courier")) {
            if (c < 3) {
                Courier courier = new Courier(userID);
                User user = courier.getUser();
                thline.add(new KeyboardButton("|" + courier.getCourierID() + "|" + user.getUserName()));
            c++;
            }
        }
        thline.add(new KeyboardButton("Ч1"));
        thline.add(new KeyboardButton("Ч2"));
        thline.add(new KeyboardButton("Ч3"));
        th2line.add(new KeyboardButton("Вернуться в главное меню"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        keyboard.add(thrline);
        keyboard.add(thline);
        keyboard.add(th2line);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addMainManagerZMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
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
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup addNoContactMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow(), twoline = new KeyboardRow();
        oneline.add(new KeyboardButton("Отправить свой контакт").setRequestContact(true));
        twoline.add(new KeyboardButton("Написать менеджеру"));
        twoline.add(new KeyboardButton("Адрес/Контакты"));
        keyboard.add(oneline);
        keyboard.add(twoline);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup  addAdminBackToMainButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow oneline = new KeyboardRow();
        oneline.add(new KeyboardButton("Вернуться в меню управления"));
        keyboard.add(oneline);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup  addTypeMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow one = new KeyboardRow(), two = new KeyboardRow();
        one.add(new KeyboardButton("Я представитель компании"));
        one.add(new KeyboardButton("Я частное лицо"));
        two.add(new KeyboardButton("Я уже заказывал у вас"));
        keyboard.add(one);
        keyboard.add(two);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup  addQRZayavMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow one = new KeyboardRow(), two = new KeyboardRow();
        one.add(new KeyboardButton("Заполняя форму"));
        one.add(new KeyboardButton("Используя QR-код"));
        two.add(new KeyboardButton("Главное меню"));
        keyboard.add(one);
        keyboard.add(two);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup  addAdressMenu(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        Long id = user.getUID();
        KeyboardRow one = new KeyboardRow(), two = new KeyboardRow(), thr = new KeyboardRow(), thx = new KeyboardRow();
        String city = user.getUserCity();
        String adress = user.getUserAdres(),
                lastadress = user.getUserLastAdres();
        if (DataBase.isCorporationWorker(id)) {
            Corporation corp = new Corporation(DataBase.getCorporationID(id));
            if (corp.getAddresses().size() == 1) {
                DataBase.setUsFields(id, "adress", corp.getAddresses().get(0));
                Bot.bot.user_wait_adress.remove(id);
                Bot.bot.updateOrderModels(id);
                UsersData.remAllSelectedOrderModel(id);
                Bot.bot.user_wait_model.put(id, true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Bot.bot.sendMsgToUser(user.getTID(), "Автоматически установлен адрес " + corp.getAddresses().get(0), "model");
                        user.sendMessage("\uD83D\uDC49 Выберите одну или несколько моделей картриджей ниже");
                    }
                },350);
                user.setUserAction("user_wait_model");
                return replyKeyboardMarkup;
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
                return replyKeyboardMarkup;
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
                    if (lastadress != null && !lastadress.equals("Не указано") && !lastadress.equals(adress))
                        one.add(new KeyboardButton(lastadress));

                }
            } else {
                one.add(new KeyboardButton(adress.contains(city) ? adress : city + ", " + adress));
                if (lastadress != null && !lastadress.equals("Не указано") && !lastadress.equals(adress))
                    one.add(new KeyboardButton(lastadress.contains(city) ? lastadress : city + ", " + lastadress));
                two.add(new KeyboardButton("Я приеду сам"));
            }
        }
        thr.add(new KeyboardButton("Главное меню"));
        keyboard.add(one);
        keyboard.add(two);
        keyboard.add(thr);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }


    public static ReplyKeyboardMarkup addModelMenu(User user,Long id) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow one = new KeyboardRow(), two = new KeyboardRow(), thr = new KeyboardRow(), fh = new KeyboardRow();
        String add = "" + (Bot.bot.user_tema.get(user.getUID()).equals("Заправка картриджа") ? "" : "_rem");
        String[] lastmodel = DataBase.getUsFileds(user.getTID(), "last_model" + add).split("&&&");
        if (DataBase.getUsFileds(user.getTID(), "true_models" + add) != null) {
            String[] trmd = DataBase.getUsFileds(user.getTID(), "true_models" + add).split(";");
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
            return replyKeyboardMarkup;
        } else {
            if (lastmodel[0].equals("Не указано")) {
                one.add(new KeyboardButton("Главное меню"));
                keyboard.add(one);
                replyKeyboardMarkup.setKeyboard(keyboard);
                return replyKeyboardMarkup;
            }
            if (lastmodel.length == 1) {
                one.add(new KeyboardButton(lastmodel[0]));
                two.add(new KeyboardButton("Главное меню"));
                keyboard.add(one);
                keyboard.add(two);
                replyKeyboardMarkup.setKeyboard(keyboard);
                return replyKeyboardMarkup;
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
                return replyKeyboardMarkup;
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
                return replyKeyboardMarkup;
            }
        }
        return replyKeyboardMarkup;
    }


}
