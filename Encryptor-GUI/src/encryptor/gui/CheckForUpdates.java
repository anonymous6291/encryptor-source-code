package encryptor.gui;

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
    private static final String HOST_URL = "https://anonymous6291.github.io/software/encryptor/gui/latest";
    private static final int MAX_LENGTH = 20_000;
    private static boolean checked;
    private static boolean failed;
    private static String updateVersionString = "";
    private static boolean updateAvailable;
    private static String updateData = "";

    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    protected static UpdateData check() {
        if (!checked) {
            try {
                HttpURLConnection http = (HttpURLConnection) new URI(HOST_URL).toURL().openConnection();
                if (http.getResponseCode() != HttpURLConnection.HTTP_OK || http.getContentLength() == -1) {
                    throw new Exception("Failed to check for updates.");
                }
                int n = Math.min(MAX_LENGTH, http.getContentLength());
                try (DataInputStream dis = new DataInputStream(http.getInputStream())) {
                    byte buffer[] = new byte[n];
                    dis.read(buffer);
                    String data = new String(buffer);
                    int p = data.indexOf('\n');
                    updateVersionString = data.substring(0, p);
                    if (VERSION_STRING.compareTo(updateVersionString) < 0) {
                        updateAvailable = true;
                        try {
                            updateData = data.substring(p + 2);
                        } catch (Exception e) {
                            updateData = "No information available.";
                        }
                    }
                }
                http.disconnect();
            } catch (Exception e) {
                failed = true;
            }
            checked = true;
        }
        return new UpdateData(checked, failed, updateVersionString, updateAvailable, updateData);
    }

    private CheckForUpdates() {
    }

}
