package encryptor;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.io.File;
import java.io.InputStream;

@SuppressWarnings({"unused", "ClassWithoutLogger", "PublicField"})
public class Encryptor_Console {

    private static final String NO_COLOR_COMMAND_LINE_ARGUMENT = "--no-color";
    private static final String ENCRYPT_MODE_COMMAND_LINE_ARGUMENT = "--encrypt";
    private static final String DECRYPT_MODE_COMMAND_LINE_ARGUMENT = "--decrypt";
    private static final String PATH_COMMAND_LINE_ARGUMENT = "--path";
    private static final String NO_MODIFY_COMMAND_LINE_ARGUMENT = "--no-modify";
    private static final String THREAD_COUNT_COMMAND_LINE_ARGUMENT = "--threads";
    private static final String PASSWORD_COMMAND_LINE_ARGUMENT = "--password";
    private static final String EXECUTABLE_NAME = "Encryptor-Console";
    private static final Runtime runtime = Runtime.getRuntime();
    private final String workingDirectory;
    private final boolean encrmode;
    private final String path;
    private final boolean modify;
    private final int threads;
    private final String password;
    private Process p;
    private InputStream inputStream;
    private InputStream errorStream;

    public Encryptor_Console(boolean encrmode, String path, boolean modify, int threads, String password) {
        workingDirectory = System.getProperty("user.dir");
        this.encrmode = encrmode;
        this.path = path;
        this.modify = modify;
        this.threads = threads;
        this.password = password;
        p = null;
    }

    public void run() throws Exception {
        if (p != null) {
            return;
        }
        String encryptorPath = new File(workingDirectory, EXECUTABLE_NAME).toString();
        Verify.verify(encryptorPath);
        String command[] = new String[]{encryptorPath, NO_COLOR_COMMAND_LINE_ARGUMENT, encrmode ? ENCRYPT_MODE_COMMAND_LINE_ARGUMENT : DECRYPT_MODE_COMMAND_LINE_ARGUMENT,
            modify ? "" : NO_MODIFY_COMMAND_LINE_ARGUMENT, THREAD_COUNT_COMMAND_LINE_ARGUMENT, Integer.toString(threads), PASSWORD_COMMAND_LINE_ARGUMENT, password, PATH_COMMAND_LINE_ARGUMENT, path};
        String properties[] = new String[]{"user.dir", workingDirectory};
        p = runtime.exec(command, properties);
        inputStream = p.getInputStream();
        errorStream = p.getErrorStream();
    }

    public boolean taskCompleted() {
        return !p.isAlive();
    }

    public int availableInputStream() throws Exception {
        return inputStream.available();
    }

    public int readInputStream() throws Exception {
        return inputStream.read();
    }

    public int readInputStream(byte b[]) throws Exception {
        return inputStream.read(b);
    }

    public int readInputStream(byte b[], int off, int len) throws Exception {
        return inputStream.read(b, off, len);
    }

    public int availableErrorStream() throws Exception {
        return errorStream.available();
    }

    public int readErrorStream() throws Exception {
        return errorStream.read();
    }

    public int readErrorStream(byte b[]) throws Exception {
        return errorStream.read(b);
    }

    public int readErrorStream(byte b[], int off, int len) throws Exception {
        return errorStream.read(b, off, len);
    }
}
