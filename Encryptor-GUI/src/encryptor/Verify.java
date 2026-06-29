package encryptor;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@SuppressWarnings("ClassWithoutLogger")
public class Verify {

    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    private static byte[] calculate(InputStream input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte buffer[] = new byte[65536];
        for (int l = input.read(buffer); l > 0; l = input.read(buffer)) {
            md.update(buffer, 0, l);
        }
        return new BigInteger(1, md.digest()).toString(36).getBytes(StandardCharsets.UTF_16);
    }

    protected static void verify(String path) throws Exception {
        FileInputStream fis = new FileInputStream(path);
        if (!matches(calculate(fis))) {
            throw new RuntimeException("Unknown error occured.");
        }
    }

    private static boolean matches(byte res[]) {
        if (res == null) {
            return false;
        }
        final byte tar[] = {-2, -1, 0, 54, 0, 53, 0, 113, 0, 49, 0, 48, 0, 109, 0, 108, 0, 104, 0, 111, 0, 121, 0, 121, 0, 52, 0, 112, 0, 105, 0, 121, 0, 55, 0, 57, 0, 53, 0, 56, 0, 51, 0, 119, 0, 121, 0, 103, 0, 116, 0, 111, 0, 48, 0, 110, 0, 98, 0, 106, 0, 112, 0, 121, 0, 97, 0, 98, 0, 101, 0, 52, 0, 117, 0, 50, 0, 108, 0, 110, 0, 119, 0, 104, 0, 106, 0, 52, 0, 116, 0, 109, 0, 116, 0, 122, 0, 56, 0, 107, 0, 55};
        if (tar.length != res.length) {
            return false;
        }
        for (int i = 0; i < tar.length; i++) {
            if (res[i] != tar[i]) {
                return false;
            }
        }
        return true;
    }

    private Verify() {
    }

}
