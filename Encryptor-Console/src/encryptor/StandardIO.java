package encryptor;

//@author anonymous
import java.io.Console;
import java.util.Scanner;

@SuppressWarnings({"UseOfSystemOutOrSystemErr", "ClassWithoutLogger"})
public class StandardIO {

    private static Console console = null;
    private static Scanner stdin = null;
    private static boolean initialized = false;

    public static synchronized void init() {
        if (initialized) {
            return;
        }
        console = System.console();
        if (console == null) {
            stdin = new Scanner(System.in);
        }
        initialized = true;
    }

    private static boolean initialized() {
        return initialized;
    }

    public static synchronized void print(Object object) {
        if (!initialized()) {
            init();
        }
        if (console == null) {
            System.out.print(object);
        } else {
            console.print(object);
        }
    }

    public static synchronized void println(Object object) {
        if (!initialized()) {
            init();
        }
        if (console == null) {
            System.out.println(object);
        } else {
            console.println(object);
        }
    }

    public static synchronized void errprintln(Object object) {
        if (!initialized()) {
            init();
        }
        if (console == null) {
            System.err.println(object);
        } else {
            console.println(object);
        }
    }

    public static synchronized String readLine() {
        if (!initialized()) {
            init();
        }
        if (console == null) {
            return stdin.nextLine();
        } else {
            return console.readLine();
        }
    }

    public static synchronized char[] readPassword() {
        if (!initialized()) {
            init();
        }
        if (console == null) {
            return readLine().toCharArray();
        } else {
            return console.readPassword();
        }
    }

    private StandardIO() {
    }

}
