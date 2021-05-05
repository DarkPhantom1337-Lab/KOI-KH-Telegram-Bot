package ua.darkphantom1337.koi.kh.database;

import ua.darkphantom1337.koi.kh.entitys.TaskType;

public class TasksTable extends DarkTable {

    public TasksTable(Integer taskID) {
        super("tasks", "taskID", taskID.longValue());
    }

    public TasksTable(Long taskID) {
        super("tasks", "taskID", taskID.longValue());
    }

    public TasksTable(String taskID) {
        super("tasks", "taskID", Long.parseLong(taskID));
    }

    public Long getTaskID() {
        return getFieldID();
    }

    public Long getCourierID(){
        return getLong("courierID");
    }

    public void setCourierID(Long id){
        setLong("courierID",id);
    }

    public Long getOrderID(){
        return getLong("orderID");
    }

    public void setOrderID(Long id){
        setLong("orderID",id);
    }

    public Integer getOrderMessageID(){
        return getInt("orderMessageID");
    }

    public void setOrderMessageID(Integer id){
        setInt("orderMessageID",id);
    }
    
    public TaskType getTaskType(){
        String type = getString("taskType");
        if (type == null)
            return TaskType.OTHER;
        return TaskType.valueOf(type);
    }

    public void setTaskType(TaskType type){
        setString("taskType", type.name());
    }
    
    public String getCreatingDate() {
        return getString("creatingDate");
    }

    public void setCreatingDate(String text) {
        setString("creatingDate", text);
    }

    public String getTaskStatus() {
        String status =  getString("status");
        if (status == null)
            return "В процессе";
        return status;
    }

    public void setTaskStatus(String text) {
        setString("status", text);
    }

    public String getCreatingTime() {
        return getString("creatingTime");
    }

    public void setCreatingTime(String text) {
        setString("creatingTime", text);
    }

    public String getEndDate() {
        return getString("emdDate");
    }

    public void setEndDate(String text) {
        setString("emdDate", text);
    }

    public String getEndTime() {
        return getString("emdTime");
    }

    public void setEndTime(String text) {
        setString("emdTime", text);
    }

    public String getString(String valuename){
        return super.getString(valuename);
    }

}
