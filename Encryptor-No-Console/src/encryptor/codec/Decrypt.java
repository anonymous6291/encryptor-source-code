package encryptor.codec;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ClassWithoutLogger")
public class Decrypt {

    private static final int BUFFER_SIZE = Encrypt.BUFFER_SIZE;
    private static final byte verifier[] = Encrypt.VERIFIER.clone();

    @SuppressWarnings({"NestedAssignment", "ConvertToTryWithResources"})
    protected static void decrypt(File input, File outputDir, byte decr[]) throws Exception {
        try (InputStream fis = new BufferedInputStream(new FileInputStream(input), BUFFER_SIZE)) {
            Map<Byte, Integer> changer = new HashMap<>(256);
            int decrlen = decr.length;
            byte ch[] = new byte[256];
            if (fis.read(ch) != 256) {
                throw new RuntimeException("Unexpected end detected!");
            }
            for (int i = 0; i < 256; i++) {
                changer.put((byte) (ch[i] ^ decr[i % decrlen]), i);
            }
            if (changer.size() != 256) {
                throw new RuntimeException("Possibly wrong password or corrupted file!");
            }
            ch = new byte[2];
            if (fis.read(ch) != 2) {
                throw new RuntimeException("Unexpected end detected!");
            }
            int ivlen = changer.get(ch[0]) * 255 + changer.get(ch[1]);
            byte iv[] = new byte[ivlen];
            if (fis.read(iv) != ivlen) {
                throw new RuntimeException("Unexpected end detected!");
            }
            for (int i = 0; i < ivlen; i++) {
                iv[i] = (byte) (changer.get(iv[i]) - 128);
            }
            ch = new byte[2];
            if (fis.read(ch) != 2) {
                throw new RuntimeException("Unexpected end detected!");
            }
            int nl = changer.get(ch[0]) * 255 + changer.get(ch[1]);
            ch = new byte[nl];
            if (fis.read(ch) != nl) {
                throw new RuntimeException("Unexpected end detected!");
            }
            int ivoff = 0, decroff = 0;
            decrypt0(ch, 0, nl, iv, 0, ivlen, decr, 0, decrlen, changer);
            File fileName = new File(outputDir, new String(ch));
            ch = new byte[verifier.length];
            if (fis.read(ch) != verifier.length) {
                throw new RuntimeException("Unexpected end detected!");
            }
            decrypt0(ch, 0, ch.length, iv, 0, ivlen, decr, 0, decrlen, changer);
            for (int i = 0; i < ch.length; i++) {
                if (ch[i] != verifier[i]) {
                    throw new RuntimeException("Possibly wrong password or corrupted file!");
                }
            }
            fileName.createNewFile();
            OutputStream fos = new BufferedOutputStream(new FileOutputStream(fileName), BUFFER_SIZE);
            ch = new byte[BUFFER_SIZE];
            while ((nl = fis.read(ch)) != -1) {
                decrypt0(ch, 0, nl, iv, ivoff, ivlen, decr, decroff, decrlen, changer);
                fos.write(ch, 0, nl);
                ivoff += 1;
                decroff += 1;
                if (ivoff >= ivlen) {
                    ivoff = 0;
                }
                if (decroff >= decrlen) {
                    decroff = 0;
                }
            }
            fos.close();
        } catch (Exception e) {
            throw e;
        }
    }

    protected static void decrypt0(byte data[], int off, int len, byte iv[], int ivoff, int ivlen, byte encr[], int encroff, int encrlen, Map<Byte, Integer> changer) {
        if (off + len > data.length || ivlen <= 0 || encrlen <= 0 || changer.size() != 256) {
            throw new IllegalArgumentException("Invalid parameter passed.");
        }
        for (int i = off, j = ivoff, k = encroff; i < len; ++i, ++j, ++k) {
            if (j >= ivlen) {
                j = 0;
            }
            if (k >= encrlen) {
                k = 0;
            }
            data[i] = (byte) (((byte) (changer.get(data[i]) - 128)) ^ iv[j] ^ encr[k]);
        }
    }

    private Decrypt() {
    }
}
