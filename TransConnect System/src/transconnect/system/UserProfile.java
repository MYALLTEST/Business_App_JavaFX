package transconnect.system;

/**
 * Store User details 
 * @author Kiarie
 */
public class UserProfile {
   private static String name;
   private static String note;
   private static String accountType;
   private static int userID;
   /**
    * Get User logged in name
    * @return String
    */ 
    public static String getName() {
        return name;
    }
    /**
     * Get User profile note
     * @return String
     */
    public static String getNote() {
        return note;
    }
    /**
     * Get User profile type either System User or System Administrator
     * @return String
     */
    public static String getAccountType() {
        return accountType;
    }
    /**
     * Get user UserID
     * @return Integer
     */
    public static int getUserID(){
        return userID;
    }
    /**
     * Set System Administrator logged in Name
     * @param name String
     */
    public static void setName(String name) {
        UserProfile.name = name;
    }
    /**
     * Set User profile note
     * @param note String
     */
    public static void setNote(String note) {
        UserProfile.note = note;
    }
    /**
     * Set User profile Type either System User or System Administrator
     * @param accountType 
     */
    public static void setAccountType(String accountType) {
        UserProfile.accountType = accountType;
    }
    /**
     * Set user UserID
     * @param userID Integer
     */
    public static void setUserID(int userID) {
        UserProfile.userID = userID;
    }
    
}
