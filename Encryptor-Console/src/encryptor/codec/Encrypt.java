package encryptor.codec;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import encryptor.CustomLogger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@SuppressWarnings("ClassWithoutLogger")
public class Encrypt {

    private static final int RADIX = Character.MAX_RADIX;
    protected static final int MAX_TRY = 100;
    protected static final int BUFFER_SIZE = 65_536;
    private static final int DEFAULT_FILE_NAME_SIZE = 30;
    private static final int MIN_IV_LENGTH = 10_000;
    private static final int MAX_IV_LENGTH = 40_000;
    protected static final byte VERIFIER[] = {-23, 53, -80, -83, -121, -50, -77, -19, -14, -28, 76, 110, -91, -54, 8, -30, 103, 115, 70, 78, -53, -27, -33, 63, -103, 79, -10, 104, -66, 74, -106, 112, 63, -34, -59, 93, -44, 94, -93, -21, -94, -8, -125, -95, -3, -59, 5, 104, 104, -118, -65, -1, -16, 80, 78, -23, 26, 31, -40, 110, -127, 102, 117, -35, -36, 79, 107, -120, 114, -76, 108, -37, -18, -69, -5, -17, 61, -63, -108, -71, -25, 26, -2, -80, -91, -89, 83, -38, -44, 21, 40, -52, 15, -90, -57, 37, -95, 124, -34, -128, -3, -114, 9, -85, 107, 47, 123, -24, 18, 93, 29, -58, -72, 63, 115, 126, -113, -124, -109, 61, -95, 115, 2, 24, 47, -27, -19, -24, 114, 5, -64, 67, -62, -54, 101, -32, 84, 68, -103, -33, -6, 7, -12, -105, -15, 46, -64, 83, 70, 54, 92, -56, 59, 79, -49, -50, -69, 86, -44, 92, -71, -106, -80, -75, 69, -61, 122, -23, 7, -51, -28, 115, -55, 100, 110, 49, 111, -34, 124, -33, -39, -55, 5, -92, -59, 14, 41, -63, 30, -45, 86, -84, -29, 84, -115, 80, -67, -123, -124, 46, -123, -3, -23, -89, 40, 1, -82, 8, 80, -72, 126, -90, 70, -27, 55, -113, 64, 112, -23, 110, -35, 118, 112, 71, -30, 88, 29, 46, -109, -45, -70, 65, -10, 123, 55, -70, 17, 92, -101, -60, 119, 95, -111, 0, 3, 114, 32, -97, 23, 34, -97, -106, 11, -111, 80, 53, -87, 60, 66, -89, -25, -12, 55, -53, 35, 1, -59, 5, 85, 60, -53, -14, 12, -124, -45, 84, 120, 23, -27, 63, -108, -21, -19, 71, -46, -31, -82, -95, -77, -73, -3, -50, 11, 91, -9, 58, -5, -120, 93, 95, 22, 87, 72, -21, -7, -57, 41, -64, -126, -123, -39, -14, 104, 101, -101, 85, -15, 40, -7, -17, 125, 75, -34, -24, 5, 19, 22, -34, 14, 19, -17, -106, 77, 70, 123, -4, 110, -77, 121, 108, -14, -114, 68, -48, 39, 66, 74, 26, 57, 4, 52, -104, 8, 0, -91, -102, 17, 8, -65, -81, 21, -13, -31, -3, 69, -49, -86, 70, 93, -71, -98, 67, 12, 20, -66, 1, -55, 38, 38, 33, 15, 30, -87, -14, 88, 48, 54, 1, 29, 22, 109, 33, 104, -79, -39, 21, 30, 121, -50, 99, -2, 75, 75, 85, 9, -67, 111, 65, 43, 19, 2, 106, -18, -85, -68, -32, -9, 102, 39, 1, 76, -20, 108, 33, -122, 123, -127, -43, 75, 115, -83, 21, 77, 1, 72, 59, -107, -124, -62, -23, -116, 90, -85, -82, -30, 60, -124, -14, 114, 74, 124, 35, 116, -65, -56, -39, -111, 36, 70, 67, 20, -52, -125, -71, -121, -57, -62, 3, 81, 18, 67, 72, 16, 44, 115, 69, 13, -40, -8, 65, -37, -103, 100, 15, -96, -52, 7, -3, -49, -75, 31, -128, 21, -9, -89, 45, -79, 41, -96, -83, 82, -27, -58, -75, 64, -85, 48, -5, 37, 107, -34, -67, 69, 57, 13, 58, -114, -85, 107, -7, 42, -113, -52, -66, -67, -93, -22, 42, 17, -92, 0, 15, -96, 78, 28, 109, -120, -104, 61, -123, -12, -120, -70, 7, 76, 78, -42, -75, 49, 113, -107, 22, -79, 23, 32, 14, -48, -29, -86, -48, -77, 8, 108, 18, -72, 59, -49, 56, -72, -119, 47, -6, -41, 119, 116, 89, -118, 108, -128, 34, 48, -94, -68, 84, 21, -2, 70, 79, -26, -95, -40, 71, -27, -13, 84, -109, 11, -47, -52, -41, -124, -78, 79, 22, 64, -112, 120, 32, 35, -46, 34, -96, 102, -11, -31, -33, 45, 9, 35, 43, -23, 103, 95, -18, -69, 80, -26, -58, 125, -44, -17, 103, 4, 61, 19, -17, 53, -21, 120, 29, 58, -76, -4, 78, 96, -26, -89, 12, 114, 120, 32, 92, -101, 117, -77, 113, -116, -19, 76, 10, -26, -25, -65, 60, 53, 50, 22, 31, -50, 96, 37, -31, 121, -101, 115, 86, -30, 23, -69, 31, -125, -12, 38, 44, 14, 39, 123, -100, 114, 32, -17, 35, -108, -109, -19, 71, 117, -14, 90, 98, -70, -42, 116, 111, -114, -48, -48, -91, 98, -122, -12, -109, -93, 15, -120, -104, 42, -85, -114, 96, -50, 123, 68, -127, -57, 32, 85, -96, 11, 62, -48, 16, -69, -34, 89, -84, -88, -40, -113, -95, -117, -118, -97, 46, 54, 86, 13, 40, 42, 10, -56, -45, -61, -110, 63, -65, 52, 68, -110, -61, -99, -51, 95, -29, 37, -91, -4, -11, -2, 1, 80, -72, -68, -85, -115, -12, 72, -17, -121, 33, 31, 124, 93, 107, -74, -51, 123, -41, -125, -104, 110, -27, -73, 122, 7, -10, 99, 47, -44, -7, 72, 0, -69, 106, -121, 66, -30, -3, 6, -51, -125, 123, 107, 15, -64, -66, -44, 78, -95, -96, -47, -24, -76, -1, 75, -8, -66, 7, 114, -22, 56, 99, 68, -103, -14, -87, 50, -21, -65, 57, 66, 109, 0, -112, 72, -97, 56, -128, -116, -26, -80, -20, 47, 75, 51, 92, 44, 27, 44, -113, 100, -119, 74, 123, 50, 97, 87, -112, 57, -56, 65, 110, -13, 41, 81, 89, 63, 26, -67, 38, 17, 24, 42, 116, 33, 11, -10, 1, -111, -78, 103, 89, -33, 51, 67, 51, 89, -121, -70, 96, -54, -108, 106, 36, -77, 25, -121, -65, -79, 24, -72, -7, -39, 45, 61, -23, 31, 100, 5, -58, -103, -16, 84, 14, -100, 4, -110, 91, -128, -78, 76, 104, 71, -121, -13, -70, 63, -128, 6, 72, 82, -61, -68, -52, 32, 94, 123, 2, 23, 12, 48, -65, -63, 98, 77, 36, -24, -41, 86, -5, 117, -19, -37, -41, -56, 3, 37, -114, -5, -50, 41, 75, -88, 9, 97, -66, 102, -1, -74, 8, -23, -115, -84, -125, 111, 122, 35, -125, 38, 106, 25, -43, -118, -93, 50, -23, -106, 30, 35, -2};

    public static byte[] encrypt(String text, String algorithm) throws Exception {
        byte b[] = text.getBytes(StandardCharsets.UTF_16);
        MessageDigest md = MessageDigest.getInstance(algorithm);
        BigInteger bi = new BigInteger(1, md.digest(b));
        return bi.toString(RADIX).getBytes(StandardCharsets.UTF_16);
    }

    @SuppressWarnings("NestedAssignment")
    protected static void encrypt(File input, File outputDir, boolean hideFileName, final byte encr[]) throws Exception {
        String originalFileName = input.getName();
        byte name[] = originalFileName.getBytes();
        if (name.length > 65536) {
            CustomLogger.log("Length of filename is too long :".concat(input.toString()), CustomLogger.ERROR);
            throw new CodecException("File name too long.");
        }
        File encryptedFile;
        if (hideFileName) {
            int i = 0;
            encryptedFile = new File(outputDir, RandomGenerator.generateString(DEFAULT_FILE_NAME_SIZE));
            while (!encryptedFile.createNewFile()) {
                if (i >= MAX_TRY) {
                    throw new CodecException("File creation failed.");
                }
                i += 1;
                encryptedFile = new File(outputDir, RandomGenerator.generateString(DEFAULT_FILE_NAME_SIZE));
            }
        } else {
            encryptedFile = new File(outputDir, input.getName());
            if (!encryptedFile.createNewFile()) {
                throw new CodecException("File creation failed.");
            }
        }
        try (InputStream fis = new BufferedInputStream(new FileInputStream(input), BUFFER_SIZE); OutputStream fos = new BufferedOutputStream(new FileOutputStream(encryptedFile), BUFFER_SIZE)) {
            byte iv[] = RandomGenerator.generateRandomBytes(MIN_IV_LENGTH, MAX_IV_LENGTH);
            byte changer[] = RandomGenerator.generateUniqueBytes();
            int encroff = 0, ivoff = 0, encrlen = encr.length, ivlen = iv.length;
            int nn = name.length;
            for (int i = 0; i < 256; i++) {
                fos.write(changer[i] ^ encr[i % encrlen]);
            }
            fos.write(changer[ivlen & 0xff]);
            fos.write(changer[(ivlen >> 8) & 0xff]);
            fos.write(changer[(ivlen >> 16) & 0xff]);
            for (int i = 0; i < ivlen; i++) {
                fos.write(changer[iv[i] + 128]);
            }
            fos.write(changer[nn & 0xff]);
            fos.write(changer[(nn >> 8) & 0xff]);
            fos.write(changer[(nn >> 16) & 0xff]);
            encrypt0(name, 0, nn, iv, ivoff, ivlen, encr, encroff, encrlen, changer);
            fos.write(name);
            byte verifiercopy[] = new byte[VERIFIER.length];
            System.arraycopy(VERIFIER, 0, verifiercopy, 0, VERIFIER.length);
            encrypt0(verifiercopy, 0, verifiercopy.length, iv, 0, ivlen, encr, 0, encrlen, changer);
            fos.write(verifiercopy);
            byte chunks[] = new byte[BUFFER_SIZE];
            int len;
            while ((len = fis.read(chunks)) != -1) {
                encrypt0(chunks, 0, len, iv, ivoff, ivlen, encr, encroff, encrlen, changer);
                fos.write(chunks, 0, len);
                ivoff += 1;
                encroff += 1;
                if (ivoff >= ivlen) {
                    ivoff = 0;
                }
                if (encroff >= encrlen) {
                    encroff = 0;
                }
            }
        } catch (Exception e) {
            throw new CodecException(e.getMessage());
        }
    }

    public static void encrypt0(byte data[], int off, int len, byte iv[], int ivoff, int ivlen, byte encr[], int encroff, int encrlen, byte changer[]) {
        if (off + len > data.length || ivlen <= 0 || encrlen <= 0 || changer.length != 256) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        for (int i = 0, j = ivoff, k = encroff; i < len; i++, j++, k++) {
            if (j >= ivlen) {
                j = 0;
            }
            if (k >= encrlen) {
                k = 0;
            }
            data[i] = changer[((byte) (data[i] ^ iv[j] ^ encr[k])) + 128];
        }
    }

    private Encrypt() {
    }
}
