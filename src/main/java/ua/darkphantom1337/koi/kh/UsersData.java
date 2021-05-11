package ua.darkphantom1337.koi.kh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersData {

    public static enum ReconcileAnswer { Reconcile, ForcedRecovery, ForcedCancelRecovery};

    private static HashMap<Long, HashMap<Long, ReconcileAnswer>> selected_orders_for_reconcile = new HashMap<Long, HashMap<Long, ReconcileAnswer>>();
    private static HashMap<Long, List<String>> order_models = new HashMap<Long, List<String>>();
    private static HashMap<Long, List<String>> selected_order_models = new HashMap<Long, List<String>>();

    public static HashMap<Long, ReconcileAnswer> getSelectedOrdersForReconcile(Long userID){
        if (selected_orders_for_reconcile.containsKey(userID))
            return selected_orders_for_reconcile.get(userID);
        else {
            selected_orders_for_reconcile.put(userID,new HashMap<Long, ReconcileAnswer>());
            return getSelectedOrdersForReconcile(userID);
        }
    }

    public static void addSelectedOrderForReconcile(Long userID, Long subOrderID, ReconcileAnswer answer){
        HashMap<Long, ReconcileAnswer> selectedSubOrders = getSelectedOrdersForReconcile(userID);
        if (selectedSubOrders.containsKey(subOrderID))
            selectedSubOrders.remove(subOrderID);
        else
            selectedSubOrders.put(subOrderID, answer);
        clearSelectedOrdersForReconcile(userID);
        selected_orders_for_reconcile.put(userID, selectedSubOrders);
    }

    public static void clearSelectedOrdersForReconcile(Long userID){
        selected_orders_for_reconcile.remove(userID);
    }

    public static Boolean hasSelectedSubOrderAnswer(Long userID, Long subOrderID,ReconcileAnswer answer){
        HashMap<Long, ReconcileAnswer> selectedSubOrders = getSelectedOrdersForReconcile(userID);
        if (selectedSubOrders.containsKey(subOrderID))
            if (selectedSubOrders.get(subOrderID).equals(answer))
                return true;
        return false;
    }

    public static List<Long> getSelectetSubOrderFilterAnswer(Long userID,ReconcileAnswer answer){
        HashMap<Long, ReconcileAnswer> selectedSubOrders = getSelectedOrdersForReconcile(userID);
        List<Long> list = new ArrayList<>();
        for (Long subOrderID : selectedSubOrders.keySet())
            if (selectedSubOrders.get(subOrderID).equals(answer))
                list.add(subOrderID);
        return list;
    }

    /**
     *
     */

    public static List<String> getOrderModels(Long userID) {
        if (order_models.containsKey(userID))
            return order_models.get(userID);
        order_models.put(userID, new ArrayList<>());
        return getOrderModels(userID);
    }

    public static void setOrderModels(Long userID, List<String> models) {
        order_models.put(userID, models);
    }

    public static void addOrderModel(Long userID, String model) {
        List<String> models = getOrderModels(userID);
        if (!models.contains(model)) {
            models.add(model);
            setOrderModels(userID, models);
        }
    }

    public static void remOrderModel(Long userID, String model) {
        List<String> models = getOrderModels(userID);
        models.remove(model);
        setOrderModels(userID, models);
    }

    /**
     *
     *
     */

    public static List<String> getSelectedOrderModels(Long userID) {
        if (selected_order_models.containsKey(userID))
            return selected_order_models.get(userID);
        selected_order_models.put(userID, new ArrayList<>());
        return getSelectedOrderModels(userID);
    }

    public static void setSelectedOrderModels(Long userID, List<String> models) {
        selected_order_models.put(userID, models);
    }

    public static void addSelectedOrderModel(Long userID, String model) {
        List<String> models = getSelectedOrderModels(userID);
        if (!models.contains(model)) {
            models.add(model);
            setSelectedOrderModels(userID, models);
        } else {
            remSelectedOrderModel(userID, model);
        }
    }

    public static void remSelectedOrderModel(Long userID, String model) {
        List<String> models = getSelectedOrderModels(userID);
        models.remove(model);
        setSelectedOrderModels(userID, models);
    }

    public static void remAllSelectedOrderModel(Long userID) {
        selected_order_models.remove(userID);
    }

}
