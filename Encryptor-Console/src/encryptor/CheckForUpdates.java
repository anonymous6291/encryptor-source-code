package encryptor;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URI;

@SuppressWarnings("ClassWithoutLogger")
public class CheckForUpdates {

    private static final String VERSION_STRING = "2.0.0";
    private static final String HOST_URL = "https://anonymous6291.github.io/software/encryptor/console/latest";
    private static final int MAX_LENGTH = 21;
    private static boolean checked = false;
    private static boolean failed = false;
    private static String result = "";

    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    protected static synchronized void check() {
        if (checked) {
            return;
        }
        try {
            HttpURLConnection http = (HttpURLConnection) new URI(HOST_URL).toURL().openConnection();
            if (http.getResponseCode() != HttpURLConnection.HTTP_OK || http.getContentLength() == -1) {
                throw new RuntimeException();
            }
            try (DataInputStream dis = new DataInputStream(http.getInputStream())) {
                int n = Math.min(MAX_LENGTH, http.getContentLength()) - 1;
                byte ver[] = new byte[n];
                dis.read(ver);
                String latestVersion = new String(ver);
                if (VERSION_STRING.compareTo(latestVersion) < 0) {
                    result = "Update available. Latest version: \"".concat(latestVersion).concat("\".");
                } else {
                    result = "You are already using the latest version.";
                }
            }
            http.disconnect();
        } catch (Exception e) {
            failed = true;
            result = "Failed to check for updates.";
        }
        checked = true;
    }

    protected static boolean checked() {
        return checked;
    }

    protected static boolean failed() {
        return failed;
    }

    protected static String getResult() {
        return result;
    }

    private CheckForUpdates() {
    }

}
