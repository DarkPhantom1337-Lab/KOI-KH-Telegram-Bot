package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.api.objects.Message;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.DataBase;

public class Handlers {

    private Bot bot;

    public Handlers(Bot bot){
        this.bot = bot;
    }

    public Boolean handleManagersCommands(Message msg, String text){
        long id = msg.getChatId();
       if (text.equals("Перейти в меню пользователя")) {
           bot.sendMsgToUser(msg.getChatId(), "Вы перешли в главное меню пользователя!", "MainPersonalMenu");
           return true;
       }
       if (text.equals("Вернуться в меню управления")) {
           String vc = DataBase.getPerFields(id, "v_id");
           bot.sendMsgToUser(msg.getChatId(), "Вы вернулись в меню управления", vc.equals("manager") ? "MainManagerMenu" :
                   vc.equals("admin") ? "MainAdminMenu" : vc.equals("owner") ? "MainAdminMenu" : "main");
           return true;
       }
       if (text.equals("Информация о заявке(ах)")) {
           bot.sendMsgToUser(msg.getChatId(), "Чтобы посмотреть информацию о заявках: "
                   + "\n -> /info <znum> - посмотреть информацию про заявку №<znum>"
                   + "\n -> Пример: /info 156 - покажет информацию про заявку №156", "MainManagerMenu");
           return true;
       }
       if (text.contains("/info")) {
           int nz = Bot.pi(text.split(" ")[1]);
       }
       return false;
    }

    public Boolean handleAdminCommands(Message msg, String text){
        long id = msg.getChatId();
        if (text.equals("Управление рассылками")) {
            bot.sendMsgToUser(msg.getChatId(), "Вы открыли меню управления рассылками", "ras");
            return true;
        }
        return false;
    }

    public Boolean handleOwnerCommands(Message msg, String text){
        long id = msg.getChatId();
        if (text.equals("Управление персоналом")) {
            bot.sendMsgToUser(msg.getChatId(), "Команды управления персоналом: "
                    + "\n -> /add <phone> <name> <v_id> - добавление персонала"
                    + "\n -> /rem <phone> <name> <v_id> - удаление персонала"
                    + "\n -> Пример: /add 380666905956 DarkPhantom1337 develop"
                    + "\n -> Доступные вакансии: admin, manager", "MainAdminMenu");
            return true;
        }
        if (text.equals("Статистики")) {
            bot.sendMsgToUser(msg.getChatId(), "Статистики: "
                    + "\n -> Пока что статистики недоступны!", "MainAdminMenu");
            return true;
        }
        if (text.contains("/add")) {
            String phone = text.split(" ")[1];
            String name = text.split(" ")[2];
            String v_id = text.split(" ")[3];
            if (v_id.equals("manager") || v_id.equals("admin")) {
                for(long i: DataBase.getAllUserId()){
                    if (DataBase.getUserPhone(Math.toIntExact(i)).equals(phone)){
                        DataBase.setPerFields(Math.toIntExact(i),"v_id", phone);
                        DataBase.setPerFields(Math.toIntExact(i),"position", (v_id.equals("manager") ? "Менеджер" : "Администратор"));
                        DataBase.setPerFields(Math.toIntExact(i),"name", DataBase.getUserName(Math.toIntExact(i)));
                        DataBase.setPerFields(Math.toIntExact(i),"phone", phone);
                        DataBase.setPerFields(Math.toIntExact(i),"description", ((v_id.equals("manager") ? "Менеджер" : "Администратор"))+ " чат-бота KOI");
                        Bot.sendMsgToUser(id, "Вы успешно назначили " + DataBase.getUserName(Math.toIntExact(i)) + (v_id.equals("manager") ? "менеджером" : "администратором") + "!");
                        Bot.sendMsgToUser((long) i, "Вас успешно назначили " + (v_id.equals("manager") ? "менеджером" : "администратором") + "!");
                        return true;
                    }
                }
            } else {
                Bot.sendMsgToUser(id, "Ошибка! Вакансия '" + v_id + "' недоступна!\nДоступные вакансии: manager,admin");
                return true;
            }
            return false;
        }
        return false;
    }

}
