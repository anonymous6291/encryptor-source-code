package encryptor.codec;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.io.File;

public interface Service {

    public static File getParentFile(String child) throws Exception {
        if (child == null) {
            return null;
        }
        File parent = new File(child).getCanonicalFile().getParentFile();
        if (parent != null) {
            return parent;
        }
        return new File(child, PARENT_DIRECTORY_SYMBOL).getCanonicalFile();
    }

    public static File getParentFile(File child) throws Exception {
        if (child == null) {
            return null;
        }
        File parent = child.getCanonicalFile().getParentFile();
        if (parent != null) {
            return parent;
        }
        return new File(child, PARENT_DIRECTORY_SYMBOL).getCanonicalFile();
    }

    public static String getCurrentWorkingDirectoryPath() {
        return System.getProperty(CURRENT_WORKING_DIRECTORY_PROPERTY_NAME);
    }

    public static File getServicePathInParent(File curr, String dirname, boolean unique) throws Exception {
        if (dirname == null) {
            return null;
        }
        File result, parent;
        String child = null;
        if (curr == null) {
            File currdir = new File(getCurrentWorkingDirectoryPath()).getCanonicalFile();
            child = currdir.getName();
            parent = getParentFile(currdir);
        } else {
            File original = curr.getCanonicalFile();
            if (original.isDirectory()) {
                child = original.getName();
            }
            parent = getParentFile(original);
        }
        result = new File(parent, dirname);
        if (unique) {
            int count = 0;
            while (result.exists() && count < MAX_IGNORE) {
                result = new File(parent, dirname.concat(Integer.toString(count)));
                ++count;
            }
        }
        if (child == null) {
            return result;
        }
        return new File(result, child).getCanonicalFile();
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
    public final String CURRENT_WORKING_DIRECTORY_PROPERTY_NAME = "user.dir";

    public void init();

    public void startService() throws Exception;
}
