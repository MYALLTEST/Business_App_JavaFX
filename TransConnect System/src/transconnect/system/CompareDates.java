package transconnect.system;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Compare Dates
 * @author Kiarie
 */
public class CompareDates {
    
    private SimpleDateFormat dateFormat;
    
    public CompareDates() {
        dateFormat= new SimpleDateFormat("d/M/yyyy");
    }
    /**
     * Determines if the dates are equal
     * @param date1
     * @param date2
     * @return Boolean
     */
    private boolean equals(Date date1,Date date2){
        String str1=dateFormat.format(date1);// eg 23/5/2013
        String str2=dateFormat.format(date2);// eg 23/5/2013 then both dates are equal
        System.out.println("equals checking");
        return str1.equals(str2);
    }
    
    /**
     * Compares the first date to determine its after the second date
     * @param date1 Date
     * @param date2 Date
     * @return Boolean True if less than else False
     */
    public boolean greater(Date date1,Date date2){
        System.out.println("checking greater");
        if(equals(date1, date2)){
            return false;
        }
        if(date1.before(date2)){
            return false;
        }
        if(date1.after(date2)){
            return true;
        }
        return true;
    }
    /**
     * Compares the first date to determine its before the second date
     * @param date1 Date
     * @param date2 Date
     * @return Boolean True if less than else False
     */
    public boolean less(Date date1,Date date2){
        System.out.println("checking less");
        if(equals(date1, date2)){
            return false;
        }
        if(date1.before(date2)){
            return true;
        }
        if(date1.after(date2)){
            return false;
        }
        return true;
    }
}
