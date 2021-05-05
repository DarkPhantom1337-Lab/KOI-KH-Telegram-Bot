package ua.darkphantom1337.koi.kh.entitys;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.MasterChannelButtons;
import ua.darkphantom1337.koi.kh.database.ActualTable;
import ua.darkphantom1337.koi.kh.database.SubOrdersTable;

public class SubOrder extends SubOrdersTable {

    private Bot bot = Bot.bot;

    public SubOrder(Integer subOrderID) {
        super(subOrderID);
    }

    public SubOrder(Long subOrderID) {
        super(subOrderID);
    }

    public SubOrder(String subOrderID) {
        super(subOrderID);
    }

    public Order getOrder(){
        return new Order(getOrderID());
    }

    public MasterWork getWork(){
        return new MasterWork(getWorkID());
    }

    public Cartridge getCartridge(){
        return new Cartridge(getCartridgeID());
    }

}
