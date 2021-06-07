package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.meta.api.objects.Contact;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.entitys.User;

public class UserContactHandler {

    private Bot bot = Bot.bot;

    public Boolean handleContact(User user, Contact contact) {
        if (user.getUserAction().equals("user_wait_phone")) {
            String phoneNumber = contact.getPhoneNumber();
            String name = (contact.getFirstName() == null ? "" : " " + contact.getFirstName()) +(contact.getLastName() == null ? "" : " "+contact.getLastName());
            user.setPhone(phoneNumber);
            bot.sendMsgToUser(user.getTID(), name + ", спасибо за предоставление Вашего номера телефона!");
            user.setUserAction("user_wait_name");
            bot.sendMsgToUser(user.getTID(), "Как я могу к Вам обращаться?", "men_adr");
            bot.sendToLogChanel("Пользователь TID: " + user.getTID() +" UID: " + user.getUID() +" Имя:" + name +"(" + phoneNumber + ") оставил свой номер телефона!");
            bot.sendToUserInfoChanel("Пользователь TID: " + user.getTID() +" UID: " + user.getUID() + " Имя:" + name +"(" + phoneNumber + ") оставил свой номер телефона!");
            //bot.user_phone.put(user.getTID(), contact.getPhoneNumber());
            return true;
        }
        return false;
    }

}