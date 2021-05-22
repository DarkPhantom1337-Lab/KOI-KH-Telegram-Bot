package ua.darkphantom1337.koi.kh.database;

import ua.darkphantom1337.koi.kh.DataBase;

import java.util.List;

class DarkTable {

    private String tableName;
    private String idFieldName;
    private Long fieldID;

    public DarkTable(String tableName, String idFieldName, Long fieldID){
        this.tableName = tableName;
        this.idFieldName = idFieldName;
        this.fieldID = fieldID;
    }

    protected Integer getInt(String fieldName){
        return DataBase.getTableIField(tableName, fieldID, idFieldName, fieldName);
    }

    protected String getString(String fieldName){
        return DataBase.getTableSField(tableName, fieldID, idFieldName, fieldName);
    }

    protected Boolean getBoolean(String fieldName){
        return DataBase.getTableBField(tableName, fieldID, idFieldName, fieldName);
    }

    protected Double getDouble(String fieldName){
        return DataBase.getTableDField(tableName, fieldID, idFieldName, fieldName);
    }

    protected Long getLong(String fieldName){
        return DataBase.getTableLField(tableName, fieldID, idFieldName, fieldName);
    }

    protected void setString(String fieldName, String fieldValue){
        DataBase.setTableSField(tableName, fieldID, idFieldName, fieldName, fieldValue);
    }

    protected void setInt(String fieldName, Integer fieldValue){
        DataBase.setTableIField(tableName, fieldID, idFieldName, fieldName,fieldValue);
    }

    protected void setBoolean(String fieldName, Boolean fieldValue){
        DataBase.setTableBField(tableName, fieldID, idFieldName, fieldName,fieldValue);
    }

    protected void setDouble(String fieldName, Double fieldValue){
        DataBase.setTableDField(tableName, fieldID, idFieldName, fieldName,fieldValue);
    }

    protected void setLong(String fieldName, Long fieldValue){
        DataBase.setTableLField(tableName, fieldID, idFieldName, fieldName,fieldValue);
    }

    public List<Object> getValuesWhere(String whereName, String whereValue, String needFieldName){
        return DataBase.getTableFieldsWhere(tableName, whereName, whereValue,needFieldName);
    }

    public  Boolean isSetRecord(){
        return DataBase.isSetTableRecord(tableName, idFieldName, fieldID);
    }

    public String getTableName(){
        return this.tableName;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public Long getFieldID(){
        return this.fieldID;
    }
}
