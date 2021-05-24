package ua.darkphantom1337.koi.kh.database;

import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.entitys.MasterWork;
import ua.darkphantom1337.koi.kh.entitys.Order;
import ua.darkphantom1337.koi.kh.entitys.SubOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataBase {

    public static Connection conn;
    public static Connection conn1;
    public static Bot bot;

    private static TidToUidTable tidToUidTable = null;

    public static void connectToBase(String url, String dbName, String user, String pass) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + url + "/" + dbName + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=UTC&sessionVariables=wait_timeout=600", user, pass);
            Statement statmt = conn.createStatement();
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `users` (`id` int PRIMARY KEY,"
                            + "`phone` varchar(20),"
                            + "`phone_dop` varchar(20),"
                            + "`name` varchar(128),"
                            + "`type` varchar(32),"
                            + "`company_name` varchar(128),"
                            + "`city`varchar(200),"
                            + "`last_adress`varchar(200),"
                            + "`adress`varchar(200),"
                            + "`last_model`varchar(200),"
                            + "`last_model_rem`varchar(200),"
                            + "`true_models`varchar(200),"
                            + "`true_models_rem`varchar(200),"
                            + "`action`varchar(499),"
                            + "`last_z_id` int,"
                            + "`days`varchar(64),"
                            + "`days_send_date`varchar(64),"
                            + "`z_in_day` varchar(32),"
                            + "`all_z`varchar(200),"
                            + "`z_kolv` int,"
                            + "`cartridgesID` longtext,"
                            + "`in_corporation_work` bool,"
                            + "`сorporationID` BIGINT UNSIGNED,"
                            + "`dr`varchar(200),"
                            + "`privilege`varchar(200),"
                            + "`reg_date`varchar(200),"
                            + "`reg_time`varchar(200),"
                            + "`sr_rate` double)");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `couriers` (`courierID` BIGINT UNSIGNED PRIMARY KEY,"
                            + "`currentTasksID` text,"
                            + "`completedTasksID` longtext,"
                            + "`currentMessagesID` text,"
                            + "`registerDate` varchar(32),"
                            + "`registerTime` varchar(32))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `referral` (`userID` BIGINT UNSIGNED PRIMARY KEY,"
                            + "`inviterID` BIGINT UNSIGNED DEFAULT 0,"
                            + "`invited` BIGINT UNSIGNED DEFAULT 0)");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `mailings` (`mailID` BIGINT UNSIGNED PRIMARY KEY,"
                            + "`message` longtext,"
                            + "`file_id` text,"
                            + "`file_type` varchar(64),"
                            + "`content_type` varchar(128),"
                            + "`send_type` varchar(64),"
                            + "`send_date` varchar(32),"
                            + "`recipientsID` varchar(32),"
                            + "`messagesID` longtext,"
                            + "`status` varchar(32),"
                            + "`creatingDate` varchar(32),"
                            + "`creatingTime` varchar(32))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `tasks` (`taskID` BIGINT UNSIGNED PRIMARY KEY,"
                            + "`courierID` BIGINT UNSIGNED,"
                            + "`orderID` BIGINT UNSIGNED,"
                            + "`orderMessageID` int,"
                            + "`taskType` varchar(32),"
                            + "`status` varchar(99),"
                            + "`creatingDate` varchar(32),"
                            + "`creatingTime` varchar(32),"
                            + "`endDate` varchar(32),"
                            + "`endTime` varchar(32));");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `corporations` (`corporationID` BIGINT UNSIGNED PRIMARY KEY,"
                            + "`name` varchar(99),"
                            + "`addresses` longtext,"
                            + "`employeesID` longtext,"
                            + "`ordersID` longtext,"
                            + "`vip` bool,"
                            + "`reg_date`varchar(200),"
                            + "`reg_time`varchar(200))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `cartridges` (`cartridgeID` BIGINT UNSIGNED PRIMARY KEY,"
                            + "`cartridge_model` longtext,"
                            + "`printer_model` longtext,"
                            + "`address` longtext,"
                            + "`ownersID` longtext,"
                            + "`add_date`varchar(200),"
                            + "`add_time`varchar(200))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `master_works` (`workID` BIGINT UNSIGNED PRIMARY KEY,"
                            + "`orderID` BIGINT UNSIGNED,"
                            + "`chip` bool,"
                            + "`ld` bool,"
                            + "`chl` bool,"
                            + "`mr` bool,"
                            + "`vpz` bool,"
                            + "`fb` bool,"
                            + "`mb` bool,"
                            + "`toner` int,"
                            + "`type` longtext,"
                            + "`mainMsgID` varchar(200),"
                            + "`description` longtext,"
                            + "`start_date`varchar(200),"
                            + "`start_time`varchar(200),"
                            + "`end_date`varchar(200),"
                            + "`end_time`varchar(200))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `controll` (`id` int PRIMARY KEY,"
                            + "`vz_handler` int,"//заявки
                            + "`vv_handler` int,"//вост
                            + "`vn_handler` int,"//напоминания
                            + "`vr_handler` int,"//опрос
                            + "`other` varchar(64),"//для всего остального
                            + "`date` varchar(64),"//дата%время сьёма показаний
                            + "`description` varchar(20))");//DarkPhantom1337
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `viber_zayavki` (`id` int PRIMARY KEY,"
                            + "`dark_id` int,"
                            + "`z_id` int,"
                            + "`theme` varchar(64),"
                            + "`adres` varchar(120),"
                            + "`model` varchar(120),"
                            + "`status` varchar(120),"
                            + "`description` varchar(64))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `viber_vosst` (`id` int PRIMARY KEY,"
                            + "`dark_id` int,"
                            + "`z_id` int,"
                            + "`theme` varchar(64),"
                            + "`answer` varchar(120),"
                            + "`status` varchar(120),"
                            + "`description` varchar(64))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `viber_napoms` (`id` int PRIMARY KEY,"
                            + "`dark_id` int,"
                            + "`theme` varchar(64),"
                            + "`answer` varchar(120),"
                            + "`status` varchar(120),"
                            + "`description` varchar(64))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `dark_user_id` (`viber_id` varchar(64) PRIMARY KEY,"
                            + "`dark_id` int,"
                            + "`description` varchar(124))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `viber_users` (`id` varchar(64) PRIMARY KEY,"
                            + "`phone` varchar(20),"
                            + "`type` varchar(32),"
                            + "`name` varchar(128),"
                            + "`company_name` varchar(128),"
                            + "`city`varchar(200),"
                            + "`last_adress`varchar(200),"
                            + "`adress`varchar(200),"
                            + "`model`varchar(200),"
                            + "`last_model`varchar(200),"
                            + "`model_rem`varchar(200),"
                            + "`last_model_rem`varchar(200),"
                            + "`true_models`varchar(200),"
                            + "`true_models_rem`varchar(200),"
                            + "`last_z_id` int,"
                            + "`days`varchar(64),"
                            + "`days_send_date`varchar(64),"
                            + "`z_in_day` varchar(32),"
                            + "`all_z`varchar(200),"
                            + "`z_kolv` int,"
                            + "`dr`varchar(200),"
                            + "`dr_date`varchar(200),"
                            + "`privilege`varchar(200),"
                            + "`reg_date`varchar(200),"
                            + "`reg_time`varchar(200),"
                            + "`sr_rate` double)");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `opros` (`n` int PRIMARY KEY,"
                            + "`z_id` bigint,"
                            + "`u_id` bigint,"
                            + "`send_date` varchar(32),"
                            + "`val` varchar(32))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `viber_rates` (`n` int PRIMARY KEY,"
                            + "`dark_id` bigint,"
                            + "`z_id` bigint,"
                            + "`rate` varchar(32),"
                            + "`status` varchar(32),"
                            + "`description` varchar(32))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `actual` (`z_id` bigint PRIMARY KEY,"
                            + "`theme` varchar(64),"
                            + "`model` varchar(200),"
                            + "`name` varchar(64),"
                            + "`phone` varchar(64),"
                            + "`phone_dop` varchar(64),"
                            + "`u_id` bigint,"
                            + "`main_msg_id` varchar(120),"
                            + "`status_msg_id` varchar(120),"
                            + "`msg_ids` varchar(200),"
                            + "`status` varchar(32),"
                            + "`statuses` longtext,"
                            + "`prinyal` varchar(32),"
                            + "`city` varchar(32),"
                            + "`adress` varchar(200),"
                            + "`source` varchar(50),"
                            + "`description` varchar(200),"
                            + "`sheetRows` text,"
                            + "`tasksID` text,"
                            + "`subOrdersID` text,"
                            + "`is_cancelled` bool,"
                            + "`rate` varchar(16),"
                            + "`date` varchar(64),"
                            + "`time` varchar(64))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `suborders` (`subOrderID` bigint PRIMARY KEY,"
                            + "`model` varchar(200),"
                            + "`status_msg_id` varchar(120),"
                            + "`status` varchar(32),"
                            + "`statuses` longtext,"
                            + "`workID` BIGINT UNSIGNED DEFAULT 0,"
                            + "`orderID` BIGINT UNSIGNED DEFAULT 0,"
                            + "`cartridgeID` BIGINT UNSIGNED DEFAULT 0,"
                            + "`description` varchar(200));");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `tid_to_uid` (`telegramID` BIGINT UNSIGNED PRIMARY KEY,"
                            + "`userID` BIGINT UNSIGNED,"
                            + "`description` varchar(20));");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `achive` (`z_id` bigint PRIMARY KEY,"
                            + "`theme` varchar(64),"
                            + "`model` varchar(200),"
                            + "`name` varchar(64),"
                            + "`phone` varchar(64),"
                            + "`phone_dop` varchar(64),"
                            + "`u_id` bigint,"
                            + "`msg_ids` varchar(200),"
                            + "`status` varchar(32),"
                            + "`prinyal` varchar(32),"
                            + "`city` varchar(32),"
                            + "`adress` varchar(200),"
                            + "`description` varchar(200),"
                            + "`rate` varchar(16),"
                            + "`date` varchar(64),"
                            + "`time` varchar(64))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `utils` (`id` int PRIMARY KEY,"
                            + "`val` int NOT NULL,"
                            + "`val1` LONGTEXT,"
                            + "`val2` varchar(32),"
                            + "`val3` varchar(32))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `personal` (`id` int PRIMARY KEY,"
                            + "`v_id` varchar(32),"
                            + "`position` varchar(32),"
                            + "`name` varchar(32),"
                            + "`phone` varchar(32),"
                            + "`description` varchar(32))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `newsletter` (`id` int PRIMARY KEY,"
                            + "`news_name` varchar(32),"
                            + "`file_name` varchar(32),"
                            + "`text` varchar(32),"
                            + "`type` varchar(32),"
                            + "`status` varchar(32),"
                            + "`date` varchar(32),"
                            + "`time` varchar(32))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `newsletter` (`id` int PRIMARY KEY,"
                            + "`news_name` varchar(32),"
                            + "`file_name` varchar(32),"
                            + "`text` varchar(32),"
                            + "`type` varchar(32),"
                            + "`status` varchar(32),"
                            + "`date` varchar(32),"
                            + "`time` varchar(32))");
            statmt.execute(
                    "CREATE TABLE IF NOT EXISTS `stletters` (`id` int PRIMARY KEY,"
                            + "`news_name` varchar(32),"
                            + "`file_name` varchar(32),"
                            + "`text` varchar(32),"
                            + "`type` varchar(32),"
                            + "`status` varchar(32),"
                            + "`date` varchar(32),"
                            + "`time` varchar(32))");
            statmt.close();
            statmt.cancel();
            System.out.println("Подключение к базе данных koi-bot успешно!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TidToUidTable getTidToUidTable(Long ID, Boolean isTelegramID) {
        return new TidToUidTable(ID, isTelegramID);
    }

    public static void setControllFields(int id, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO controll (id," + fname + ",date, description) VALUES (?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?, date = ?, description = ?;");) {
            e.setInt(1, id);
            e.setString(2, fvalue);
            e.setString(3, bot.u.getDate("dd/MM/YYYY%HH:mm:ss"));
            e.setString(4, "by DarkPhantom1337");
            e.setString(5, fvalue);
            e.setString(6, bot.u.getDate("dd/MM/YYYY%HH:mm:ss"));
            e.setString(7, "by DarkPhantom1337");
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void connectToPriceBase(String url, String dbName, String user, String pass) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn1 = DriverManager.getConnection(
                    "jdbc:mysql://" + url + "/" + dbName + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=UTC", user, pass);
            Statement statmt1 = conn1.createStatement();
            statmt1.close();
            statmt1.cancel();
            System.out.println("Подключение к базе данных koi-price успешно!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBot(Bot bot) {
        DataBase.bot = bot;
    }

    public static List<String> getAllPrices(String prntmodel, Boolean isprinter, String value) {
        try (PreparedStatement preparedStatement1 = conn1.prepareStatement("SELECT * FROM price");
        ) {
            ResultSet e = preparedStatement1.executeQuery();
            List<String> all = new ArrayList<String>();
            while (e.next()) {
                if (prntmodel.equals("Canon") || prntmodel.equals("HP")) {
                    if (e.getString("printer").toLowerCase().contains("Canon".toLowerCase())
                            || e.getString("printer").toLowerCase().contains("HP".toLowerCase())) {
                        if (isprinter) {
                            if (e.getString("printer").toLowerCase().contains(value.toLowerCase()))
                                all.add(e.getString("printer").replaceAll("/", "¦") + "/" + e.getString("cartrige").replaceAll("/", "¦") + "/" + e.getDouble("price_refill") + "/" + e.getInt("id"));
                        } else {
                            String crt = e.getString("cartrige");
                            if (crt.toLowerCase().contains(value.toLowerCase()) || crt.replaceAll("-", " ").toLowerCase().contains(value.toLowerCase()) || crt.replaceAll("-", "").toLowerCase().contains(value.toLowerCase()) || crt.replaceAll(" ", "").toLowerCase().contains(value.toLowerCase()))
                                all.add(e.getString("printer").replaceAll("/", "¦") + "/" + e.getString("cartrige").replaceAll("/", "¦") + "/" + e.getDouble("price_refill") + "/" + e.getInt("id"));
                        }
                    }
                } else {
                    if (prntmodel.equals("Samsung") || prntmodel.equals("Xerox")) {
                        if (e.getString("printer").toLowerCase().contains("Samsung".toLowerCase())
                                || e.getString("printer").toLowerCase().contains("Xerox".toLowerCase())) {
                            if (isprinter) {
                                if (e.getString("printer").toLowerCase().contains(value.toLowerCase())) {
                                    all.add(e.getString("printer").replaceAll("/", "¦") + "/" + e.getString("cartrige").replaceAll("/", "¦") + "/" + e.getDouble("price_refill") + "/" + e.getInt("id"));
                                }
                            } else {
                                String crt = e.getString("cartrige");
                                if (crt.toLowerCase().contains(value.toLowerCase()) || crt.replaceAll("-", " ").toLowerCase().contains(value.toLowerCase()) || crt.replaceAll("-", "").toLowerCase().contains(value.toLowerCase()) || crt.replaceAll(" ", "").toLowerCase().contains(value.toLowerCase())) {
                                    all.add(e.getString("printer").replaceAll("/", "¦") + "/" + e.getString("cartrige").replaceAll("/", "¦") + "/" + e.getDouble("price_refill") + "/" + e.getInt("id"));
                                }
                            }
                        }
                    } else {
                        if (e.getString("printer").toLowerCase().contains(prntmodel.toLowerCase())) {
                            if (isprinter) {
                                if (e.getString("printer").toLowerCase().contains(value.toLowerCase())) {
                                    all.add(e.getString("printer").replaceAll("/", "¦") + "/" + e.getString("cartrige").replaceAll("/", "¦") + "/" + e.getDouble("price_refill") + "/" + e.getInt("id"));
                                }
                            } else {
                                String crt = e.getString("cartrige");
                                if (crt.toLowerCase().contains(value.toLowerCase()) || crt.replaceAll("-", " ").toLowerCase().contains(value.toLowerCase()) || crt.replaceAll("-", "").toLowerCase().contains(value.toLowerCase()) || crt.replaceAll(" ", "").toLowerCase().contains(value.toLowerCase())) {
                                    all.add(e.getString("printer").replaceAll("/", "¦") + "/" + e.getString("cartrige").replaceAll("/", "¦") + "/" + e.getDouble("price_refill") + "/" + e.getInt("id"));
                                }
                            }
                        }
                    }
                }
            }
            e.close();
            return all;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, List<String>> getAllPricesForCartr(String prntmodel, String kart) {
        List<String> allall = getAllPrices(prntmodel, false, kart);
        Map<String, List<String>> gp = new HashMap<String, List<String>>();
        for (String s : allall) {
            String[] spl = s.split("/");
            if (gp.containsKey(spl[1])) {
                List<String> printers = gp.get(spl[1]);
                printers.add(spl[0]);
                gp.put(spl[1], printers);
            } else {
                List<String> printers = new ArrayList<String>();
                printers.add(spl[0]);
                gp.put(spl[1], printers);
            }
        }
        return gp;
    }

    public static Integer getIdForCart(String fname) {
        try (PreparedStatement preparedStatement1 = conn1.prepareStatement("SELECT * FROM price WHERE cartrige = ?;");
        ) {
            preparedStatement1.setString(1, fname);
            ResultSet e = preparedStatement1.executeQuery();
            while (e.next()) {
                if (e.getString("cartrige").toLowerCase().equals(fname.toLowerCase())) {
                    int id = e.getInt("id");
                    e.close();
                    return id;
                }
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getF(String fname, Integer id) {
        try (PreparedStatement preparedStatement1 = conn1.prepareStatement("SELECT " + fname + " FROM price WHERE id = ?;");
        ) {
            preparedStatement1.setInt(1, id);
            ResultSet e = preparedStatement1.executeQuery();
            if (e.next()) {
                String s = e.getString(fname);
                e.close();
                return s;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Null";
    }

    public static void inserToNewsLetter(int id, String filename, String text, String date, String time, String status) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO newsletter (id,file_name,text,status,date,time) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE file_name = ?, text = ?, status = ?, date = ?, time = ?;");
        ) {
            e.setInt(1, id);
            e.setString(2, filename);
            e.setString(3, text);
            e.setString(4, status);
            e.setString(5, date);
            e.setString(6, time);
            e.setString(7, filename);
            e.setString(8, text);
            e.setString(9, status);
            e.setString(10, date);
            e.setString(11, date);
            e.executeUpdate();
            e.clearParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void inserToStaticLetter(int id, String filename, String text, String date, String time, String status) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO stletters (id,file_name,text,status,date,time) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE file_name = ?, text = ?, status = ?, date = ?, time = ?;")
        ) {
            e.setInt(1, id);
            e.setString(2, filename);
            e.setString(3, text);
            e.setString(4, status);
            e.setString(5, date);
            e.setString(6, time);
            e.setString(7, filename);
            e.setString(8, text);
            e.setString(9, status);
            e.setString(10, date);
            e.setString(11, date);
            e.executeUpdate();
            e.clearParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addZForUser(int id, int nz) {
        String allz = "";
        if (DataBase.getUsFileds((long) id, "all_z") == null)
            allz = "" + nz;
        else allz = DataBase.getUsFileds((long) id, "all_z") + "," + nz;
        DataBase.setUsFields((long) id, "all_z", allz);
    }


    public static String getNL(int id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM newsletter WHERE id = ?;");
        ) {
            preparedStatement.setInt(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public static String getSL(int id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM stletters WHERE id = ?;");
        ) {
            preparedStatement.setInt(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NULL";
    }


    public static void savePerson(int uid, String position, String name, String phone) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO personal (id,position,name,phone) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE position = ?, name = ?, phone = ?;");
        ) {
            e.setInt(1, uid);
            e.setString(2, position);
            e.setString(3, name);
            e.setString(4, phone);
            e.setString(5, position);
            e.setString(6, name);
            e.setString(7, phone);
            e.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPerFields(Long id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM personal WHERE id = ?;");
        ) {
            preparedStatement.setLong(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public static List<Long> getAllPersonalID() {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM personal;");
        ) {
            ResultSet e = preparedStatement.executeQuery();
            List<Long> ids = new ArrayList<Long>();
            while (e.next()) {
                Long id = e.getLong("id");
                ids.add(id);
            }
            e.close();
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Long>();
    }

    public static List<Long> getAllPersonalIDWhere(String v_id) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT id FROM personal WHERE v_id = ?;");
        ) {
            preparedStatement.setString(1,v_id);
            ResultSet e = preparedStatement.executeQuery();
            List<Long> ids = new ArrayList<Long>();
            while (e.next()) {
                Long id = e.getLong("id");
                ids.add(id);
            }
            e.close();
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Long>();
    }

    public static List<Long> getAllUserId() {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT id FROM users;");
        ) {
            ResultSet e = preparedStatement.executeQuery();
            List<Long> allUserID = new ArrayList<>();
            while (e.next())
                allUserID.add(e.getLong("id"));
            e.close();
            return allUserID;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Integer> getAllViberUserId() {
        String[] ids = null;
        try {
            ids = getUFileds(15, "val1").split(",");
        } catch (Exception e) {
        }
        if (ids == null)
            return new ArrayList<Integer>();
        List<Integer> idss = new ArrayList<Integer>();
        for (String s : ids) {
            try {
                idss.add(Integer.parseInt(s));
            } catch (Exception e) {
                continue;
            }
        }
        return idss;
    }

    public static List<Integer> getAllBlackUserId() {
        String[] ids = null;
        try {
            ids = getUFileds(5, "val1").split(",");
        } catch (Exception e) {
            System.out.println("Чёрный лист пустой!");
        }
        List<Integer> idss = new ArrayList<Integer>();
        if (ids != null) {
            for (String s : ids) {
                try {
                    idss.add(Integer.parseInt(s));
                } catch (Exception e) {
                    return new ArrayList<Integer>();
                }
            }
            return idss;
        }
        return idss;
    }

    public static void addToBlackList(Long id) {
        String s = getUFileds(5, "val1");
        if (s != null) {
            s += "," + id;
        } else {
            s = "" + id;
        }
    }

    public static void remToBlackList(Long id) {
        List<Integer> users = getAllBlackUserId();
        if (users.contains(Math.toIntExact(id))) {
            users.remove(Math.toIntExact(id));
            String s = "";
            for (int i : users) {
                s += "" + i + ",";
            }
            setUFields(5, "val1", s);
        }
    }


    public static void saveUser(int uid, String phone, String type, String name) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO users (id,type,name) VALUES (?,?,?) ON DUPLICATE KEY UPDATE type = ?, name = ?;");
        ) {
            e.setInt(1, uid);
            e.setString(2, type);
            e.setString(3, name);
            e.setString(4, type);
            e.setString(5, name);
            e.executeUpdate();
            e.clearParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveZInOpr(int zid, int uid, String date, String val) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO opros (n,z_id,u_id,send_date,val) VALUES (?,?,?,?,?);");
        ) {
            int zopn = Integer.parseInt(getUFileds(2, "val"));
            e.setInt(1, zopn);
            e.setInt(2, zid);
            e.setInt(3, uid);
            e.setString(4, date);
            e.setString(5, val);
            e.executeUpdate();
            setUFields(2, "val", Bot.pi(DataBase.getUFileds(2, "val")) + 1);
            e.clearParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> getAllNotEndZ() {
        List<Integer> all_z = new ArrayList<Integer>();
        for (int i = 0; i < getUtilsField((long) 1, "val"); i++) {
            try {

                String status = new Order(i).getStatus();
                if (status != null && !status.contains("Завершена"))
                    all_z.add(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return all_z;
    }

    public static String getUFileds(int id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM utils WHERE id = ?");
        ) {
            preparedStatement.setInt(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public static String getVZFileds(int id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM viber_zayavki WHERE id = ?;");
        ) {
            preparedStatement.setInt(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public static String getVRField(int id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM viber_rates WHERE n = ?;");
        ) {
            preparedStatement.setInt(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public static String getVSFileds(int id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM viber_vosst WHERE id = ?;");
        ) {
            preparedStatement.setInt(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public static String getVZUFileds(int id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM viber_users WHERE id = ?;");
        ) {
            preparedStatement.setInt(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public static String getUsFileds(Long id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM users WHERE id = ?;");
        ) {
            preparedStatement.setLong(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public static void clearTable(String tabname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE " + tabname + ";");
        ) {
            preparedStatement.execute();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void saveOrderInSheet(Long orderID, String updateType) {
        CompletableFuture.runAsync(() -> {
            try {
                SubOrder subOrder = new SubOrder(orderID);
                Order order = subOrder.getOrder();
                Integer row = subOrder.getAllSheetRows().get(0);
                Integer nz = orderID.intValue();
                Long user_id = order.getUID();
                if (updateType.equals("MAIN") || updateType.equals("ID"))
                    bot.sheetsAPI.setSheetValue("A" + row, "" + order.getOrderID() + "/" + subOrder.getSubOrderID() + "/" + subOrder.getWorkID());
                if (updateType.equals("MAIN")) {
                    bot.sheetsAPI.setSheetValue("B" + row, order.getDate() + " " + order.getTime());
                    bot.sheetsAPI.setSheetValue("C" + row, DataBase.getUsFileds(user_id, "name"));
                    bot.sheetsAPI.setSheetValue("D" + row, DataBase.getUsFileds(user_id, "phone"));
                    String type = DataBase.getUsFileds(user_id, "type");
                    if (type.equals("Компания"))
                        bot.sheetsAPI.setSheetValue("E" + row, DataBase.getUsFileds(user_id, "company_name"));
                    else bot.sheetsAPI.setSheetValue("E" + row, type);
                    bot.sheetsAPI.setSheetValue("F" + row, subOrder.getModel());
                    bot.sheetsAPI.setSheetValue("G" + row, order.getAddress());
                    bot.sheetsAPI.setSheetValue("H" + row, subOrder.getCartridgeID().toString());
                    bot.sheetsAPI.setSheetValue("U" + row, order.getDescription());
                }
                if (updateType.equals("PRINYAL"))
                    bot.sheetsAPI.setSheetValue("I" + row, order.getPrinyal());
                if (updateType.equals("MAIN") || updateType.equals("PRINYAL") || updateType.equals("STATUS"))
                    bot.sheetsAPI.setSheetValue("T" + row, subOrder.getStatus());
                Long workID = subOrder.getWorkID();
                if (workID != 0) {
                    MasterWork work = new MasterWork(workID);
                    if (updateType.equals("WORK_TAKE"))
                    bot.sheetsAPI.setSheetValue("J" + row, DataBase.getUsFileds(work.getMasterID(), "name"));
                    if (updateType.equals("WORK_END")) {
                        bot.sheetsAPI.setSheetValue("K" + row, "" + work.getTonerAmount());
                        if (work.isUseChip())
                            bot.sheetsAPI.setSheetValue("L" + row, "✅");
                        if (work.isUseLD())
                            bot.sheetsAPI.setSheetValue("M" + row, "✅");
                        if (work.isUseCHL())
                            bot.sheetsAPI.setSheetValue("N" + row, "✅");
                        if (work.isUseMR())
                            bot.sheetsAPI.setSheetValue("O" + row, "✅");
                        if (work.isUseVPZ())
                            bot.sheetsAPI.setSheetValue("P" + row, "✅");
                        if (work.isUseFB())
                            bot.sheetsAPI.setSheetValue("Q" + row, "✅");
                        if (work.isUseMB())
                            bot.sheetsAPI.setSheetValue("R" + row, "✅");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                bot.sendToLogChanel("❌ Ошибка при записи данных по заявке #" + orderID + " в Google таблицу!");
            }
        });
    }


    public static int getOFileds(int id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM opros WHERE n = ?;");
        ) {
            preparedStatement.setInt(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                int name = e.getInt(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 444;
    }

    public static String getOFiledsS(int id, String fname) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fname + " FROM opros WHERE n = ?;");
        ) {
            preparedStatement.setInt(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fname);
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "DarkNull";
    }

    public static int ge() {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM opros;");
        ) {
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Integer i = e.getInt("count(*)");
                e.close();
                return i;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }


    public static void setUFields(int id, String fname, int fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO utils (id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;")) {
            e.setInt(1, id);
            e.setInt(2, fvalue);
            e.setInt(3, fvalue);
            e.executeUpdate();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setNLFields(int id, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO newsletter (id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, id);
            e.setString(2, fvalue);
            e.setString(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setVZFields(int id, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO viber_zayavki (id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, id);
            e.setString(2, fvalue);
            e.setString(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setControllFields(int id, String fname, Integer fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO controll (id," + fname + ",date, description) VALUES (?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?, date = ?, description = ?;");) {
            e.setInt(1, id);
            e.setInt(2, fvalue);
            e.setString(3, bot.u.getDate("dd/MM/YYYY%HH:mm:ss"));
            e.setString(4, "by DarkPhantom1337");
            e.setInt(5, fvalue);
            e.setString(6, bot.u.getDate("dd/MM/YYYY%HH:mm:ss"));
            e.setString(7, "by DarkPhantom1337");
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setVSFields(int id, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO viber_vosst (id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, id);
            e.setString(2, fvalue);
            e.setString(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setVRField(int id, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO viber_rates (n," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, id);
            e.setString(2, fvalue);
            e.setString(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setVUField(int dark_id, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO viber_users (id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, dark_id);
            e.setString(2, fvalue);
            e.setString(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSLFields(int id, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO stletters (id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, id);
            e.setString(2, fvalue);
            e.setString(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUFields(int id, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO utils (id,val," + fname + ") VALUES (?,?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, id);
            e.setInt(2, 0);
            e.setString(3, fvalue);
            e.setString(4, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUsFields(Long id, String fname, int fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO users (id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setLong(1, id);
            e.setInt(2, fvalue);
            e.setInt(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUsFields(Long id, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO users (id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setLong(1, id);
            e.setString(2, fvalue);
            e.setString(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPerFields(int id, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO personal (id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, id);
            e.setString(2, fvalue);
            e.setString(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setZFields(int zid, String fname, String fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO actual (z_id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, zid);
            e.setString(2, fvalue);
            e.setString(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setZFields(int zid, String fname, Boolean fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO actual (z_id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, zid);
            e.setBoolean(2, fvalue);
            e.setBoolean(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setZFields(int zid, String fname, Long fvalue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO actual (z_id," + fname + ") VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE " + fname + " = ?;");) {
            e.setInt(1, zid);
            e.setLong(2, fvalue);
            e.setLong(3, fvalue);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getUidForZn(int z_id) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + "u_id" + " FROM actual WHERE z_id = ?;");
        ) {
            preparedStatement.setInt(1, z_id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                int name = e.getInt("u_id");
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1337;
    }

    public static int delete(int id) {
        return 1337;
    }

    public static void saveZToArch(int z_id) {
        /*try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO achive (z_id,theme,model,name,phone,phone_dop,u_id,status,date,time) VALUES (?,?,?,?,?,?,?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE theme = ?, model = ?, name = ?, phone = ?, phone_dop = ?, u_id = ?, status = ?, date = ?, time = ?;");
        ) {
            e.setInt(1, z_id);
            e.setString(2, DataBase.getZFields(z_id, "theme"));
            e.setString(3, DataBase.getZFields(z_id, "model"));
            e.setString(4, DataBase.getZFields(z_id, "name"));
            e.setString(5, DataBase.getZFields(z_id, "phone"));
            e.setString(6, DataBase.getZFields(z_id, "phone_dop"));
            e.setInt(7, Bot.pi(DataBase.getZFields(z_id, "u_id")));
            e.setString(8, DataBase.getZFields(z_id, "status"));
            e.setString(9, DataBase.getZFields(z_id, "date"));
            e.setString(10, DataBase.getZFields(z_id, "time"));
            e.setString(11, DataBase.getZFields(z_id, "theme"));
            e.setString(12, DataBase.getZFields(z_id, "model"));
            e.setString(13, DataBase.getZFields(z_id, "name"));
            e.setString(14, DataBase.getZFields(z_id, "phone"));
            e.setString(15, DataBase.getZFields(z_id, "phone_dop"));
            e.setInt(16, Bot.pi(DataBase.getZFields(z_id, "u_id")));
            e.setString(17, DataBase.getZFields(z_id, "status"));
            e.setString(18, DataBase.getZFields(z_id, "date"));
            e.setString(19, DataBase.getZFields(z_id, "time"));
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static void saveZayavka(int z_id, String theme, String model, String name, String phone,
                                   String phone_dop, int uid, String status, String date, String time) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO actual (z_id,theme,model,name,phone,phone_dop,u_id,status,date,time) VALUES (?,?,?,?,?,?,?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE theme = ?, model = ?, name = ?, phone = ?, phone_dop = ?, u_id = ?, status = ?, date = ?, time = ?;");
        ) {
            e.setInt(1, z_id);
            e.setString(2, theme != null ? theme : "Тема не установлена!");
            e.setString(3, model != null ? model : "Модель не установлена!");
            e.setString(4, name != null ? name : "Имя не установлено!");
            e.setString(5, phone != null ? phone : "Номер не установлен!");
            e.setString(6, phone_dop);
            e.setInt(7, uid);
            e.setString(8, status);
            e.setString(9, date);
            e.setString(10, time);
            e.setString(11, theme != null ? theme : "Тема не установлена!");
            e.setString(12, model != null ? model : "Модель не установлена!");
            e.setString(13, name != null ? name : "Имя не установлено!");
            e.setString(14, phone != null ? phone : "Номер не установлен!");
            e.setString(15, phone_dop);
            e.setInt(16, uid);
            e.setString(17, status);
            e.setString(18, date);
            e.setString(19, time);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUserStr(String name, int uid, String data) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO users (id, " + name + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + name + " = ?");) {
            e.setInt(1, uid);
            e.setString(2, data);
            e.setString(3, data);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUserStr(String name, int uid, Integer data) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO users (id, " + name + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + name + " = ?");) {
            e.setInt(1, uid);
            e.setInt(2, data);
            e.setInt(3, data);
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setZNum(int znum) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO utils (id,val,val1) VALUES (?,?,?) ON DUPLICATE KEY UPDATE val = ?,val1 = ?;");
        ) {
            e.setInt(1, 1);
            e.setInt(2, znum + 1);
            e.setString(3, "Актуальный номер заявки");
            e.setInt(4, znum + 1);
            e.setString(5, "Актуальный номер заявки");
            e.executeUpdate();
            e.close();
            e.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUserStr(String select, long id) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + select + " FROM users WHERE id = ?;");
        ) {
            preparedStatement.setLong(1, id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(select);
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getZNum() {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT val FROM utils WHERE id = ?;");
        ) {
            preparedStatement.setInt(1, 1);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                int name = e.getInt("val");
                e.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setUserName(int id, String name) {
        setUserStr("name", id, name);
    }

    public static void setUserType(int id, String type) {
        setUserStr("type", id, type);
    }

    public static void setUserPhone(int id, String phone) {
        setUserStr("phone", id, phone);
    }

    public static void setUserDopPhone(int id, String phone_dop) {
        setUserStr("phone_dop", id, phone_dop);
    }

    public static void setUserFName(int id, String fname) {
        setUserStr("first_name", id, fname);
    }

    public static void setUserLName(int id, String lname) {
        setUserStr("last_name", id, lname);
    }

    public static Boolean isRegUser(Long user_id) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE id = ?");
        ) {
            preparedStatement.setLong(1, user_id);
            ResultSet e = preparedStatement.executeQuery();
            int n = 0;
            if (e.next())
                n = e.getInt(1);
            e.close();
            if (n > 0) return true;
            else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getUserName(long id) {
        return getUserStr("name", id);
    }

    public static String getUserType(long id) {
        return getUserStr("type", id);
    }

    public static String getUserPhone(long id) {
        return getUserStr("phone", id);
    }

    public static String getUserDopPhone(int id) {
        return getUserStr("phone_dop", id);
    }

    public static String getUserFName(int id) {
        return getUserStr("first_name", id);
    }

    public static String getUserLName(int id) {
        return getUserStr("last_name", id);
    }

    public static Boolean isSetMainInfo(long id) {
        if (getUserType(id) != null
                && !getUserType(Math.toIntExact(id)).equals("")
                && !getUserType(Math.toIntExact(id)).equals("NULL")
                && getUserName(Math.toIntExact(id)) != null
                && !getUserName(Math.toIntExact(id)).equals("")
                && !getUserName(Math.toIntExact(id)).equals("NULL")
                && getUserPhone(Math.toIntExact(id)) != null
                && !getUserPhone(Math.toIntExact(id)).equals("")
                && !getUserPhone(Math.toIntExact(id)).equals("NULL"))
            return true;
        return false;
    }

    public static Boolean isSetMainRegisterInfo(int id) {
        if (getUserType(Math.toIntExact(id)) != null
                && getUserName(Math.toIntExact(id)) != null
                && !getUserName(Math.toIntExact(id)).equals("")
                && !getUserName(Math.toIntExact(id)).equals("NULL")
                && getUserPhone(Math.toIntExact(id)) != null
                && !getUserPhone(Math.toIntExact(id)).equals("")
                && !getUserPhone(Math.toIntExact(id)).equals("NULL"))
            return true;
        return false;
    }

    public static Boolean isPersonal(Long id) {
        try {
            if (DataBase.getPerFields(id, "v_id") == null || DataBase.getPerFields(id, "v_id").equals("NULL"))
                return false;
            else return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean isCorporationWorker(Long user_id) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT in_corporation_work FROM users WHERE id = ?;");
        ) {
            preparedStatement.setLong(1, user_id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Boolean name = e.getBoolean("in_corporation_work");
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Long getCorporationID(Long user_id) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT сorporationID FROM users WHERE id = ?;");
        ) {
            preparedStatement.setLong(1, user_id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Long name = e.getLong("сorporationID");
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCorporationField(Long corporationID, String field_name) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + field_name + " FROM corporations WHERE corporationID = ?;");
        ) {
            preparedStatement.setLong(1, corporationID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(field_name);
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMasterWorkSField(Long workID, String field_name) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + field_name + " FROM master_works WHERE workID = ?;");
        ) {
            preparedStatement.setLong(1, workID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(field_name);
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean getMasterWorkBField(Long workID, String field_name) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + field_name + " FROM master_works WHERE workID = ?;");
        ) {
            preparedStatement.setLong(1, workID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Boolean name = e.getBoolean(field_name);
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getMasterWorkIField(Long workID, String field_name) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + field_name + " FROM master_works WHERE workID = ?;");
        ) {
            preparedStatement.setLong(1, workID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Integer name = e.getInt(field_name);
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getMasterWorkLField(Long workID, String field_name) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + field_name + " FROM master_works WHERE workID = ?;");
        ) {
            preparedStatement.setLong(1, workID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Long name = e.getLong(field_name);
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setMasterWorkField(Long workID, String name, String data) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO master_works (workID, " + name + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + name + " = ?;")) {
            e.setLong(1, workID);
            e.setString(2, data);
            e.setString(3, data);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMasterWorkField(Long workID, String name, Boolean data) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO master_works (workID, " + name + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + name + " = ?;")) {
            e.setLong(1, workID);
            e.setBoolean(2, data);
            e.setBoolean(3, data);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMasterWorkField(Long workID, String name, Integer data) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO master_works (workID, " + name + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + name + " = ?;")) {
            e.setLong(1, workID);
            e.setInt(2, data);
            e.setInt(3, data);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMasterWorkField(Long workID, String name, Long data) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO master_works (workID, " + name + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + name + " = ?;")) {
            e.setLong(1, workID);
            e.setLong(2, data);
            e.setLong(3, data);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCorporationField(Long corporationID, String name, String data) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO corporations (corporationID, " + name + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + name + " = ?;")) {
            e.setLong(1, corporationID);
            e.setString(2, data);
            e.setString(3, data);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCartridgeField(Long corporationID, String field_name) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + field_name + " FROM cartridges WHERE cartridgeID = ?;");
        ) {
            preparedStatement.setLong(1, corporationID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(field_name);
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setCartridgeField(Long corporationID, String name, String data) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO cartridges (cartridgeID, " + name + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + name + " = ?;")) {
            e.setLong(1, corporationID);
            e.setString(2, data);
            e.setString(3, data);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteCorporation(Long corporationID) {
        try (PreparedStatement e = conn.prepareStatement(
                "DELETE FROM corporations WHERE corporationID = ?")) {
            e.setLong(1, corporationID);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteCartridge(Long cartridgeID) {
        try (PreparedStatement e = conn.prepareStatement(
                "DELETE FROM cartridges WHERE cartridgeID = ?")) {
            e.setLong(1, cartridgeID);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deletePersonal(Long personalID) {
        try (PreparedStatement e = conn.prepareStatement(
                "DELETE FROM personal WHERE id = ?")) {
            e.setLong(1, personalID);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Long getNextCorporationID() {
        return getNextID(17);
    }

    public static Long getNextCartridgeID() {
        return getNextID(18);
    }

    public static Long getNextMasterWorkID() {
        return getNextID(19);
    }

    public static Long getNextSheetRowID() {
        return getNextID(21);
    }

    public static Long getNextSubOrderID() {
        return getNextID(33);
    }

    public static Long getNextTaskID() {
        return getNextID(34);
    }

    public static Long getNextMailID() {
        return getNextID(35);
    }


    public static Long getNextID(Integer id_id) {
        Long id = (long) 0;
        try (PreparedStatement pstm = conn.prepareStatement("SELECT val FROM utils WHERE id = " + id_id + ";");) {
            ResultSet e = pstm.executeQuery();
            if (e.next()) {
                id = e.getLong("val");
                pstm.addBatch("INSERT INTO utils (id,val) VALUES (" + id_id + "," + (id + 1) + ") ON DUPLICATE KEY UPDATE val = " + (id + 1) + ";");
                pstm.executeBatch();
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }


    public static Long getUtilsField(Long value_id, String field_name) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + field_name + " FROM utils WHERE id = ?;");
        ) {
            preparedStatement.setLong(1, value_id);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Long name = e.getLong(field_name);
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Global GET methods
     */

    public static String getTableSField(String tableName, Long fieldID, String IDFieldName, String fieldName) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fieldName + " FROM " + tableName + " WHERE " + IDFieldName + " = ?;");
        ) {
            preparedStatement.setLong(1, fieldID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                String name = e.getString(fieldName);
                e.close();
                preparedStatement.close();
                return name;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Object> getTableFieldsWhere(String tableName, String whereFieldName, String whereFieldValue, String needFieldName) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT "+needFieldName+" FROM " + tableName + " WHERE " + whereFieldName + " = ?;");
        ) {
            preparedStatement.setString(1, whereFieldValue);
            ResultSet e = preparedStatement.executeQuery();
            List<Object> values = new ArrayList<>();
            while(e.next()){
                Object result = e.getObject(needFieldName);
                System.out.println(String.valueOf(result) + "лол");
                if (result != null)
                    values.add(result);
            }
            preparedStatement.close();
            e.close();
            return values;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getTableIField(String tableName, Long fieldID, String IDFieldName, String fieldName) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fieldName + " FROM " + tableName + " WHERE " + IDFieldName + " = ?;");
        ) {
            preparedStatement.setLong(1, fieldID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Integer result = e.getInt(fieldName);
                e.close();
                preparedStatement.close();
                return result;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Double getTableDField(String tableName, Long fieldID, String IDFieldName, String fieldName) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fieldName + " FROM " + tableName + " WHERE " + IDFieldName + " = ?;");
        ) {
            preparedStatement.setLong(1, fieldID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Double result = e.getDouble(fieldName);
                e.close();
                preparedStatement.close();
                return result;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean getTableBField(String tableName, Long fieldID, String IDFieldName, String fieldName) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fieldName + " FROM " + tableName + " WHERE " + IDFieldName + " = ?;");
        ) {
            preparedStatement.setLong(1, fieldID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Boolean result = e.getBoolean(fieldName);
                e.close();
                preparedStatement.close();
                return result;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getTableLField(String tableName, Long fieldID, String IDFieldName, String fieldName) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT " + fieldName + " FROM " + tableName + " WHERE " + IDFieldName + " = ?;");
        ) {
            preparedStatement.setLong(1, fieldID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                Long result = e.getLong(fieldName);
                e.close();
                preparedStatement.close();
                return result;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Global SET methods
     */

    public static void setTableSField(String tableName, Long fieldID, String IDFieldName, String setFieldName, String setFieldValue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO " + tableName + " (" + IDFieldName + ", " + setFieldName + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + setFieldName + " = ?;")) {
            e.setLong(1, fieldID);
            e.setString(2, setFieldValue);
            e.setString(3, setFieldValue);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTableIField(String tableName, Long fieldID, String IDFieldName, String setFieldName, Integer setFieldValue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO " + tableName + " (" + IDFieldName + ", " + setFieldName + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + setFieldName + " = ?;")) {
            e.setLong(1, fieldID);
            e.setInt(2, setFieldValue);
            e.setInt(3, setFieldValue);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTableDField(String tableName, Long fieldID, String IDFieldName, String setFieldName, Double setFieldValue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO " + tableName + " (" + IDFieldName + ", " + setFieldName + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + setFieldName + " = ?;")) {
            e.setLong(1, fieldID);
            e.setDouble(2, setFieldValue);
            e.setDouble(3, setFieldValue);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTableBField(String tableName, Long fieldID, String IDFieldName, String setFieldName, Boolean setFieldValue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO " + tableName + " (" + IDFieldName + ", " + setFieldName + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + setFieldName + " = ?;")) {
            e.setLong(1, fieldID);
            e.setBoolean(2, setFieldValue);
            e.setBoolean(3, setFieldValue);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTableLField(String tableName, Long fieldID, String IDFieldName, String setFieldName, Long setFieldValue) {
        try (PreparedStatement e = conn.prepareStatement(
                "INSERT INTO " + tableName + " (" + IDFieldName + ", " + setFieldName + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + setFieldName + " = ?;")) {
            e.setLong(1, fieldID);
            e.setLong(2, setFieldValue);
            e.setLong(3, setFieldValue);
            e.executeUpdate();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean isSetTableRecord(String tableName, String recordFieldName, Long recordID) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT 1 FROM " + tableName + " WHERE " + recordFieldName + " = ?;");
        ) {
            preparedStatement.setLong(1, recordID);
            ResultSet e = preparedStatement.executeQuery();
            if (e.next()) {
                e.close();
                return true;
            }
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
