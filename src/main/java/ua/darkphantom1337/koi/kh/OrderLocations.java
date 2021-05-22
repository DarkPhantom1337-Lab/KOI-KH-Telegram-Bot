package ua.darkphantom1337.koi.kh;

import ua.darkphantom1337.koi.kh.database.DataBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderLocations {

    public static List<Long> getAllCurrentOrdersID(){
        return getListLongFromStringForId(23);
    }

    public static void addAllCurrentOrderID(Long orderID){
        List<Long> orders = getAllCurrentOrdersID();
        if (!orders.contains(orderID))
            orders.add(orderID);
        setListLong(23, orders);
    }

    public static void remAllCurrentOrderID(Long orderID){
        List<Long> orders = getAllCurrentOrdersID();
        orders.remove(orderID);
        setListLong(23, orders);
    }

    public static List<Long> getOrdersInOffice(){
        return getListLongFromStringForId(24);
    }

    public static void addOrderInOffice(Long orderID){
        List<Long> orders = getOrdersInOffice();
        if (!orders.contains(orderID))
            orders.add(orderID);
        setListLong(24, orders);
    }

    public static void remOrderInOffice(Long orderID){
        List<Long> orders = getOrdersInOffice();
        orders.remove(orderID);
        setListLong(24, orders);
    }


    public static List<Long> getOrdersInTheWay(){
        return getListLongFromStringForId(25);
    }

    public static void addOrderInTheWay(Long orderID){
        List<Long> orders = getOrdersInTheWay();
        if (!orders.contains(orderID))
            orders.add(orderID);
        setListLong(25, orders);
    }

    public static void remOrderInTheWay(Long orderID){
        List<Long> orders = getOrdersInTheWay();
        orders.remove(orderID);
        setListLong(25, orders);
    }

    public static List<Long> getReadyToWayOrders(){
        return getListLongFromStringForId(26);
    }

    public static void addReadyToWay(Long orderID){
        List<Long> orders = getReadyToWayOrders();
        if (!orders.contains(orderID))
            orders.add(orderID);
        setListLong(26, orders);
    }

    public static void remReadyToWay(Long orderID){
        List<Long> orders = getReadyToWayOrders();
        orders.remove(orderID);
        setListLong(26, orders);
    }

    public static List<Long> getOrdersInWork(){
        return getListLongFromStringForId(27);
    }

    public static void addOrdersInWork(Long orderID){
        List<Long> orders = getOrdersInWork();
        if (!orders.contains(orderID))
            orders.add(orderID);
        setListLong(27, orders);
    }

    public static void remOrdersInWork(Long orderID){
        List<Long> orders = getOrdersInWork();
        orders.remove(orderID);
        setListLong(27, orders);
    }

    public static List<Long> getOrdersInFirstDraft(){
        return getListLongFromStringForId(28);
    }

    public static void addOrdersToFirstDraft(Long orderID){
        List<Long> orders = getOrdersInFirstDraft();
        if (!orders.contains(orderID))
            orders.add(orderID);
        setListLong(28, orders);
    }

    public static void remOrdersInFirstDraft(Long orderID){
        List<Long> orders = getOrdersInFirstDraft();
        orders.remove(orderID);
        setListLong(28, orders);
    }

    public static List<Long> getOrdersInSecondDraft(){
        return getListLongFromStringForId(29);
    }

    public static void addOrdersToSecondDraft(Long orderID){
        List<Long> orders = getOrdersInSecondDraft();
        if (!orders.contains(orderID))
            orders.add(orderID);
        setListLong(29, orders);
    }

    public static void remOrdersInSecondDraft(Long orderID){
        List<Long> orders = getOrdersInSecondDraft();
        orders.remove(orderID);
        setListLong(29, orders);
    }

    public static List<Long> getOrdersInThirdDraft(){
        return getListLongFromStringForId(30);
    }

    public static void addOrdersToThirdDraft(Long orderID){
        List<Long> orders = getOrdersInThirdDraft();
        if (!orders.contains(orderID))
            orders.add(orderID);
        setListLong(30, orders);
    }

    public static void remOrdersInThirdDraft(Long orderID){
        List<Long> orders = getOrdersInThirdDraft();
        orders.remove(orderID);
        setListLong(30, orders);
    }

    public static List<Long> getOrdersInCollection(){
        return getListLongFromStringForId(31);
    }

    public static void addOrdersInCollection(Long orderID){
        List<Long> orders = getOrdersInCollection();
        if (!orders.contains(orderID))
            orders.add(orderID);
        setListLong(31, orders);
    }

    public static void remOrdersInCollection(Long orderID){
        List<Long> orders = getOrdersInCollection();
        orders.remove(orderID);
        setListLong(31, orders);
    }

    public static List<Long> getOrdersInDelivery(){
        return getListLongFromStringForId(32);
    }

    public static void addOrdersInDelivery(Long orderID){
        List<Long> orders = getOrdersInDelivery();
        if (!orders.contains(orderID))
            orders.add(orderID);
        setListLong(32, orders);
    }

    public static void remOrdersInDelivery(Long orderID){
        List<Long> orders = getOrdersInDelivery();
        orders.remove(orderID);
        setListLong(32, orders);
    }

    public static void removeOrderFromAll(Long orderID){
        remReadyToWay(orderID);
        remOrderInOffice(orderID);
        remOrderInTheWay(orderID);
        remOrdersInCollection(orderID);
        remOrdersInDelivery(orderID);
        remOrdersInFirstDraft(orderID);
        remOrdersInSecondDraft(orderID);
        remOrdersInThirdDraft(orderID);
        remOrdersInWork(orderID);
    }

    private static List<Long> getListLongFromStringForId(Integer id){
        String data = DataBase.getUFileds(id,"val1");
        if (data == null || data.equals("") || data.equals(" "))
            return new ArrayList<Long>();
        return Arrays.stream(data.split(";")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    }

    private static void setListLong(Integer id, List<Long> list){
        DataBase.setUFields(id, "val1",Bot.bot.u.longToString(list, ";"));
    }
}
