package transconnect.system;

/**
 * Enum types to represent icons values to be used in Dialog
 * @author Kiarie
 */
public enum DialogIcon {
    /**
     * For information message
     */
    INFORMATION(1),
    /**
     * For question message
     */
    QUESTION(2),
    /**
     * For error message
     */
    ERROR(3),
    /**
     * For warning message
     */
    WARNING(4);
    
    private int value;

    /**
     * Get icon numeric value
     */
    private DialogIcon(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
