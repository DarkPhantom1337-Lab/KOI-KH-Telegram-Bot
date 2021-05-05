package ua.darkphantom1337.koi.kh.entitys;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.CouriersTable;
import ua.darkphantom1337.koi.kh.database.TasksTable;

public class Task extends TasksTable {

    private Bot bot = Bot.bot;

    public Task(Integer taskID) {
        super(taskID);
    }

    public Task(Long taskID) {
        super(taskID);
    }

    public Task(String taskID) {
        super(taskID);
    }

    public Courier getCourier(){
        return new Courier(new User(getCourierID(), true).getUID());
    }

    public Order getOrder(){
        return new Order(getOrderID());
    }

}
