package transconnect.system;

/**
 *
 * @author Kiarie
 */
public enum DialogOption {
    /**
     * Represent OK option
     */
    OPTION_OK(1),
    /**
     * Represent Cancel option
     */
    OPTION_CANCEL(2);
    
    int value;
    private DialogOption(int val) {
        value=val;
    }
}
