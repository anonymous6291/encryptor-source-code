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

@SuppressWarnings("ClassWithoutLogger")
public class Decrypt {

    private static final int BUFFER_SIZE = Encrypt.BUFFER_SIZE;
    private static final byte verifier[] = Encrypt.VERIFIER.clone();

    @SuppressWarnings({"NestedAssignment", "ConvertToTryWithResources"})
    protected static void decrypt(File input, File outputDir, boolean retrieveFileName, byte decr[]) throws Exception {
        try (InputStream fis = new BufferedInputStream(new FileInputStream(input), BUFFER_SIZE)) {
            int changer[] = new int[256];
            int decrlen = decr.length;
            byte ch[] = new byte[256];
            if (fis.read(ch) != 256) {
                throw new CodecException("Unexpected end detected! Corrupted file.");
            }
            for (int i = 0; i < 256; i++) {
                changer[((byte) (ch[i] ^ decr[i % decrlen])) + 128] = i;
            }
            ch = new byte[3];
            if (fis.read(ch) != 3) {
                throw new CodecException("Unexpected end detected! Corrupted file.");
            }
            int ivlen = changer[ch[0] + 128] | (changer[ch[1] + 128] << 8) | (changer[ch[2] + 128] << 16);
            byte iv[] = new byte[ivlen];
            if (fis.read(iv) != ivlen) {
                throw new CodecException("Unexpected end detected! Corrupted file.");
            }
            for (int i = 0; i < ivlen; i++) {
                iv[i] = (byte) (changer[iv[i] + 128] - 128);
            }
            ch = new byte[3];
            if (fis.read(ch) != 3) {
                throw new CodecException("Unexpected end detected! Corrupted file.");
            }
            int nl = changer[ch[0] + 128] | (changer[ch[1] + 128] << 8) | (changer[ch[2] + 128] << 16);
            ch = new byte[nl];
            if (fis.read(ch) != nl) {
                throw new CodecException("Unexpected end detected! Corrupted file.");
            }
            int ivoff = 0, decroff = 0;
            decrypt0(ch, 0, nl, iv, 0, ivlen, decr, 0, decrlen, changer);
            File result;
            if (retrieveFileName) {
                result = new File(outputDir, new String(ch));
            } else {
                result = new File(outputDir, input.getName());
            }
            ch = new byte[verifier.length];
            if (fis.read(ch) != verifier.length) {
                throw new CodecException("Unexpected end detected! Corrupted file.");
            }
            decrypt0(ch, 0, ch.length, iv, 0, ivlen, decr, 0, decrlen, changer);
            for (int i = 0; i < ch.length; i++) {
                if (ch[i] != verifier[i]) {
                    throw new CodecException("Possibly wrong password or corrupted file!");
                }
            }
            result.createNewFile();
            OutputStream fos = new BufferedOutputStream(new FileOutputStream(result), BUFFER_SIZE);
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
            throw new CodecException(e.getMessage());
        }
    }

    protected static void decrypt0(byte data[], int off, int len, byte iv[], int ivoff, int ivlen, byte encr[], int encroff, int encrlen, int changer[]) {
        if (off + len > data.length || ivlen <= 0 || encrlen <= 0 || changer.length != 256) {
            throw new IllegalArgumentException("Invalid parameter passed.");
        }
        for (int i = off, j = ivoff, k = encroff; i < len; ++i, ++j, ++k) {
            if (j >= ivlen) {
                j = 0;
            }
            if (k >= encrlen) {
                k = 0;
            }
            data[i] = (byte) (((byte) (changer[data[i] + 128] - 128)) ^ iv[j] ^ encr[k]);
        }
    }

    private Decrypt() {
    }
}
