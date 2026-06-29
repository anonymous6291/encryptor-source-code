package encryptor.codec;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.io.File;
import java.util.regex.Pattern;

public interface Service {

    public static File getParentFile(String child) {
        return new File(child, PARENT_DIRECTORY_SYMBOL);
    }

    public static File getParentFile(File child) {
        return new File(child, PARENT_DIRECTORY_SYMBOL);
    }

    public static boolean matchesCurrentWorkingDirectorySymbol(String path) {
        return path != null && path.matches(CURRENT_WORKING_DIRECTORY_PATTERN);
    }

    public static boolean matchesCurrentWorkingDirectoryPath(File file) {
        return file != null && matchesCurrentWorkingDirectorySymbol(file.toString());
    }

    public static String getCurrentWorkingDirectoryPath() {
        return System.getProperty(CURRENT_WORKING_DIRECTORY_PROPERTY_NAME);
    }

    public static File getServicePathInParent(File curr, String dirname, boolean unique) {
        if (dirname == null) {
            return null;
        }
        File result, parent;
        if (curr == null || matchesCurrentWorkingDirectoryPath(curr)) {
            parent = getParentFile(getCurrentWorkingDirectoryPath());
        } else {
            parent = getParentFile(curr);
        }
        result = new File(parent, dirname);
        if (unique) {
            int count = 0;
            while (result.exists() && count < MAX_IGNORE) {
                result = new File(parent, dirname.concat(Integer.toString(count)));
                ++count;
            }
        }
        return result;
    }

    public static boolean makeDirectories(File path) {
        if (path == null) {
            return false;
        }
        if (path.mkdirs()) {
            return true;
        }
        return path.isDirectory() && path.exists();
    }

    public final int MAX_IGNORE = 10_000;
    public final String PARENT_DIRECTORY_SYMBOL = "..";
    public final String ENCRYPTED_FOLDER_NAME = "encrypted";
    public final String DECRYPTED_FOLDER_NAME = "decrypted";
    public final String CURRENT_WORKING_DIRECTORY_PATTERN = Pattern.quote(".");
    public final String CURRENT_WORKING_DIRECTORY_PROPERTY_NAME = "user.dir";

    public void init();

    public void startService() throws Exception;
}
