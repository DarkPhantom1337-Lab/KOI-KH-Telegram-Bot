package ua.darkphantom1337.koi.kh.entitys;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.DataBase;

import java.util.*;
import java.util.stream.Collectors;

public class Corporation {

    private Long corporationID;

    public Corporation(Long corporationID){
        this.corporationID = corporationID;
    }

    public String getName(){
        return DataBase.getCorporationField(corporationID,"name");
    }

    public List<String> getAddresses(){
        String adres_data = DataBase.getCorporationField(corporationID,"addresses");
        if (adres_data == null || adres_data.equals("") || adres_data.equals(" "))
            return new ArrayList<String>();
        List<String> list = new ArrayList<String>();
        for (String s : Arrays.asList(adres_data.split(";")))
            list.add(s.replaceAll("_"," "));
        return list;
    }

    public List<Long> getEmployeesID(){
        String data = DataBase.getCorporationField(corporationID,"employeesID");
        if (data == null || data.equals("") || data.equals(" "))
            return new ArrayList<Long>();
        return new ArrayList<Long>(Arrays.asList(data.split(";")).stream().mapToLong(id -> Long.parseLong(id)).boxed().collect(Collectors.toList()));
    }

    public List<Long> getCartridgesID(){
        String data = DataBase.getCorporationField(corporationID,"cartridgesID");
        if (data == null || data.equals("") || data.equals(" "))
            return new ArrayList<Long>();
        return new ArrayList<Long>(Arrays.asList(data.split(";")).stream().mapToLong(id -> Long.parseLong(id)).boxed().collect(Collectors.toList()));
    }

    public List<Long> getOrdersID(){
        String data = DataBase.getCorporationField(corporationID,"ordersID");
        if (data == null || data.equals("") || data.equals(" "))
            return new ArrayList<Long>();
        return new ArrayList<Long>(Arrays.asList(data.split(";")).stream().mapToLong(id -> Long.parseLong(id)).boxed().collect(Collectors.toList()));
    }

    public String getRegDate(){
        return DataBase.getCorporationField(corporationID,"reg_date");
    }

    public String getRegTime(){
        return DataBase.getCorporationField(corporationID,"reg_time");
    }

    public void setName(String name){
        DataBase.setCorporationField(corporationID, "name", name);
    }

    public void setAdresses(List<String> adresses){
        DataBase.setCorporationField(corporationID, "addresses", Bot.bot.u.stringToString(adresses,";"));
    }

    public void setEmployeesID(List<Long> employeesID){
        DataBase.setCorporationField(corporationID, "employeesID", Bot.bot.u.longToString(employeesID,";"));
    }

    public void setCartridgesID(List<Long> cartridgesID){
        DataBase.setCorporationField(corporationID, "cartridgesID", Bot.bot.u.longToString(cartridgesID,";"));
    }

    public void setOrdersID(List<Long> ordersID){
        DataBase.setCorporationField(corporationID, "ordersID", Bot.bot.u.longToString(ordersID,";"));
    }

    public void addAdress(String adress){
        List<String> adresses = getAddresses();
        if (!adresses.contains(adress))
            adresses.add(adress);
        setAdresses(adresses);
    }

    public void remAdress(String adress){
        List<String> adresses = getAddresses();
        adresses.remove(adress);
        setAdresses(adresses);
    }

    public void addEmployeeID(Long employee_id){
        List<Long> employees = getEmployeesID();
        if (!employees.contains(employee_id))
            employees.add(employee_id);
        setEmployeesID(employees);
    }

    public void addCartridgeID(Long cartridge_id){
        List<Long> cartridges = getCartridgesID();
        if (!cartridges.contains(cartridge_id))
            cartridges.add(cartridge_id);
        setCartridgesID(cartridges);
    }
    public void remCartridgeID(Long cartridge_id){
        List<Long> cartridges = getCartridgesID();
        cartridges.remove(cartridge_id);
        setCartridgesID(cartridges);
    }

    public void remEmployeeID(Long employee_id){
        List<Long> employees = getEmployeesID();
        employees.remove(employee_id);
        setEmployeesID(employees);
    }

    public void addOrderID(Long order_id){
        List<Long> orders = getOrdersID();
        if (!orders.contains(order_id))
            orders.add(order_id);
        setOrdersID(orders);
    }

    public void remOrderID(Long order_id){
        List<Long> orders = getOrdersID();
        orders.remove(order_id);
        setOrdersID(orders);
    }

    public void setRegDate(String date){
        DataBase.setCorporationField(corporationID, "reg_date", date);
    }

    public void setRegTime(String time){
        DataBase.setCorporationField(corporationID, "reg_time", time);
    }
}
