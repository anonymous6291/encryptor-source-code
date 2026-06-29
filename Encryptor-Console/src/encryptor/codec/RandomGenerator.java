package encryptor.codec;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ClassWithoutLogger")
public class RandomGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final int MIN_BYTES = 2_000;
    private static final int MAX_BYTES = 5_000;
    private static final String COMBINATIONS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateString(int length) {
        char c[] = new char[length];
        for (int i = 0; i < length; i++) {
            c[i] = COMBINATIONS.charAt(random.nextInt(COMBINATIONS.length()));
        }
        return new String(c);
    }

    public static byte[] generateRandomBytes(int minl, int maxl) {
        return generateRandomBytes(random.nextInt(minl, maxl));
    }

    public static byte[] generateRandomBytes() {
        return generateRandomBytes(random.nextInt(MIN_BYTES, MAX_BYTES));
    }

    public static byte[] generateRandomBytes(int length) {
        byte b[] = new byte[length];
        random.nextBytes(b);
        return b;
    }

    public static byte[] generateUniqueBytes() {
        byte randomBytes[] = new byte[256];
        List<Byte> list = new ArrayList<>(256);
        for (int i = 0; i < 256; ++i) {
            list.add((byte) i);
        }
        for (int i = 0; i < 256; i++) {
            randomBytes[i] = list.remove(random.nextInt(list.size()));
        }
        return randomBytes;
    }

    private RandomGenerator() {
    }
}
