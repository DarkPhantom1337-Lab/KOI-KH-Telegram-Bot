import org.telegram.telegrambots.api.objects.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

    public String getTime(String format){ // hh:mm:ss - hh часы - mm - минуты - ss - секунды
        return new SimpleDateFormat(format).format(new Date());
    }

    public String getDate(String format){ // dd/MM/YYYY - dd - дни - MM - месяц - YYYY - год
        return new SimpleDateFormat(format).format(new Date());
    }

    public String getTime(String format, Date date){ // hh:mm:ss
        return new SimpleDateFormat(format).format(date);
    }

    public String getDate(String format, Date date){ // dd/MM/YYYY
        return new SimpleDateFormat(format).format(date);
    }

    public String getUserPhone(Message msg){
        if(msg.getContact() != null)
            return msg.getContact().getPhoneNumber();
        else return null;
    }

    public String getUserName(Message msg){
        if(msg.getContact() != null)
            return msg.getContact().getFirstName();
        else return null;
    }

    public Integer getUserID(Message msg){
        if(msg.getContact() != null)
            return msg.getContact().getUserID();
        else return 0000;
    }

    public static Boolean isYesContact(Message msg){
        if(msg.getContact() != null)
            return true;
        else return false;
    }

    public String correctStr(String str){
        return str.replaceAll("[^A-Za-zА-Яа-я0-9 ]","");
    }

    public String longToString(List<Long> list, String splitter) { // s.s.s.s.s.s
        String s = "";
        int i = 1;
        for (Long sl : list) {
            s += sl + (i != list.size() ? splitter : "");
            i++;
        }
        return s;
    }

    public String stringToString(List<String> list, String splitter) { // s.s.s.s.s.s
        String s = "";
        int i = 1;
        for (String sl : list) {
            s += sl + (i != list.size() ? splitter : "");
            i++;
        }
        return s;
    }

}
