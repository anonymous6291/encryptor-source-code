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
    private static volatile boolean lock;

    protected static void init() {
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void log(String message, int type) {
        while (lock) {
        }
        lock = true;
        try {
            if (type == MESSAGE || type == NORMAL) {
                System.out.println(message);
            } else {
                System.err.println(message);
            }
        } catch (Exception e) {
        }
        lock = false;
    }

    private CustomLogger() {
    }
}
