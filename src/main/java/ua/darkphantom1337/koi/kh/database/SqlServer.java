package ua.darkphantom1337.koi.kh.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ua.darkphantom1337.koi.kh.entitys.Order;
import ua.darkphantom1337.koi.kh.entitys.User;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SqlServer {


    public static Connection conn;
    public static Statement statmt;
    private static String jdbcURL = "";
    private static HikariDataSource dataSource = null;
    private static HikariConfig config = null;

    public SqlServer(String url, String dbName, String user, String pass){
        connect(url,dbName,user,pass);
    }

    public void connect(String url, String dbName, String user, String pass){
        try {
            System.out.println("§a[§eDARK-SQL-HIKARI§a] [§e" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "§a] [§e" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "§a] §f-> §a Connecting to SQL Server...");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            jdbcURL = "jdbc:sqlserver://" + url + ";databaseName=" + dbName;
            config = new HikariConfig();
            config.setJdbcUrl(jdbcURL);
            config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            config.setUsername(user);
            config.setPassword(pass);
            setHikariConfigSettings();
            dataSource = new HikariDataSource(config);
            dataSource.validate();
            conn = dataSource.getConnection();
            System.out.println("[§eDARK-SQL-HIKARI§a] [" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "] ["
                    + new SimpleDateFormat("HH:mm:ss").format(new Date())
                    + "] [INFO] -> Successful connect to DataBase!");
            statmt = conn.createStatement();
           /* System.out.println(" Order KOD -> " + getKodStatus(new Order(453), new User(21L,false)));*/
            //setKodStatus(new Order(453), new User(21L,false),57);
           /* System.out.println(" Order KOD -> " + getKodStatus(new Order(453), new User(21L,false)));
*/
            String pa,us;
            //insertToDataZakaz(9971,new Order(226), new User(1L, false));
            /* ResultSet result = statmt.executeQuery("USE kafe;INSERT INTO data_zakaz (kod_firma,kod_vid,what_need,kod_status,prim,zakaz,date_vvod,date_do,kod_oper) " +
                    "VALUES (5813,16,'картридж',13, 'DarkPhantom1337',0,'04.06.2021 15:33:46','04.06.2021 15:33:46',8)");
            while (result.next()) {
                us=result.getString(1);
                System.out.println(us+"  RESULT RESULT RESULT");
            }*/
        } catch (Exception e) {
            System.out.println("§a[§eDARK-MYSQL-HIKARI§a] [§e" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "§a] [§e" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "§a] §f-> §a Error in connect to DataBase!");
            e.printStackTrace();
        }
    }

    public static Connection getHIKARIDARKPHANTOMCONNECTION() throws SQLException {
        return conn;
    }

    private static void setHikariConfigSettings() {
        config.setConnectionTimeout(120*1000);
        config.setLeakDetectionThreshold(60000);
        config.setValidationTimeout(3000);
        config.setMaximumPoolSize(8);
        config.setMinimumIdle(2);
        config.setMaxLifetime(300000);
        config.setIdleTimeout(45000);
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("allowMultiQueries", "true");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "4096");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "false");
        config.addDataSourceProperty("useBatchMultiSend", "true");
        config.addDataSourceProperty("useBatchMultiSendNumber", "250");
        config.addDataSourceProperty("useBulkStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        //  config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
    }

    public static void insertToDataZakaz(Integer kodFirma,Order oder, User user){
        try(PreparedStatement e = conn.prepareStatement(
                "USE kafe;INSERT INTO data_zakaz (kod_firma,kod_vid,what_need,date_need,kod_status,prim,zakaz,date_vvod,kod_oper,dostup)" +
                        " VALUES (?,?,?,?,?,?,?,?,?,?)"))
        {
            e.setInt(1, /*9971*/kodFirma);
            e.setInt(2, 16);
            e.setString(3, oder.getOrderID() + " / " + user.getUserCompanyName() + " / "+oder.getModel());
            e.setString(4, getNexDayDate());
            e.setInt(5, 55);
            e.setString(6, user.getUserName() + " / " + user.getUserPhone() + " / " + user.getUserAdres());
            e.setBoolean(7, false);
            e.setString(8, oder.getDate() + " " + oder.getTime());
            e.setInt(9, 9);
            e.setBoolean(10, false);
            e.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Long getKodStatus(Order oder) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("USE kafe;" +
                "SELECT kod_status FROM data_zakaz WHERE what_need = ?;")) {
            User user = new User(oder.getUID(),false);
            preparedStatement.setString(1, oder.getOrderID() + " / " + user.getUserCompanyName() + " / "+oder.getModel());
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Long name = e.getLong("kod_status");
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static Long setKodStatus(Order oder, Integer kod_status) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("USE kafe;" +
                "UPDATE data_zakaz SET kod_status = "+kod_status+" WHERE what_need = ?;")) {
            User user = new User(oder.getUID(),false);
            preparedStatement.setString(1, oder.getOrderID() + " / " + user.getUserCompanyName() + " / "+oder.getModel());
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String getNexDayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return dateFormat.format(calendar.getTime());
    }

}
