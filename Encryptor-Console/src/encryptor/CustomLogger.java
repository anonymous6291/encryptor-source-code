package encryptor;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
@SuppressWarnings("ClassWithoutLogger")
public class CustomLogger {

    public static final int MESSAGE = 0;
    public static final int NORMAL = 5;
    public static final int ERROR = 10;
    private static final String NORMAL_COLOR_ANSI_ESCAPE_CODE = "\u001B[94m";
    private static final String MESSAGE_COLOR_ANSI_ESCAPE_CODE = "\u001B[92m";
    private static final String ERROR_COLOR_ANSI_ESCAPE_CODE = "\u001B[91m";
    private static final String RESET_COLOR_ANSI_ESCAPE_CODE = "\u001B[0m";
    private static volatile boolean coloredOutput;
    private static volatile boolean lock;

    protected static void init(boolean enableColor) {
        coloredOutput = enableColor;
    }

    protected static void enableColor(boolean colored) {
        coloredOutput = colored;
    }

    public static void log(String message, int type) {
        while (lock) {
        }
        lock = true;
        try {
            String msg;
            if (coloredOutput) {
                msg = switch (type) {
                    case NORMAL ->
                        NORMAL_COLOR_ANSI_ESCAPE_CODE;
                    case MESSAGE ->
                        MESSAGE_COLOR_ANSI_ESCAPE_CODE;
                    default ->
                        ERROR_COLOR_ANSI_ESCAPE_CODE;
                };
                msg = msg.concat(message).concat(RESET_COLOR_ANSI_ESCAPE_CODE);
            } else {
                msg = message;
            }
            switch (type) {
                case ERROR ->
                    StandardIO.errprintln(msg);
                default ->
                    StandardIO.println(msg);
            }
        } catch (Exception e) {
        }
        lock = false;
    }

    private CustomLogger() {
    }
}
