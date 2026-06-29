package encryptor.codec;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import encryptor.CustomLogger;
import encryptor.TaskManager;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

@SuppressWarnings("ClassWithoutLogger")
public class DecryptService implements Service {

    private final File input;
    private final File outputDir;
    private final boolean explicitFileListGiven;
    private final List<File> explicitFileList;
    private final FileOperationsCompletedStatus status;
    private final Semaphore lock;
    private final byte decr[];
    private final Map<File, File> files;

    public DecryptService(File input, FileOperationsCompletedStatus status, int threads, byte[] decr) {
        this.input = input;
        outputDir = getDecryptedDirectory(input);
        explicitFileListGiven = false;
        explicitFileList = null;
        this.status = status;
        lock = new Semaphore(threads, true);
        this.decr = decr;
        files = new HashMap<>(1);
    }

    public DecryptService(List<File> explicitFileList, FileOperationsCompletedStatus status, int threads, byte decr[]) {
        input = null;
        outputDir = null;
        this.explicitFileList = explicitFileList;
        explicitFileListGiven = true;
        this.status = status;
        lock = new Semaphore(threads, true);
        this.decr = decr;
        files = new HashMap<>(1);
    }

    @Override
    public void init() {
    }

    @Override
    public void startService() throws Exception {
        startService0();
    }

    private void startService0() throws Exception {
        addAllFiles();
        setStatus();
        startDecryption();
    }

    private File getDecryptedDirectory(File curr) {
        return Service.getServicePathInParent(curr, DECRYPTED_FOLDER_NAME, true);
    }

    private void addAllFiles() throws Exception {
        if (explicitFileListGiven) {
            if (explicitFileList == null) {
                return;
            }
            for (File f : explicitFileList) {
                addAllFiles0(f, getDecryptedDirectory(f));
            }
        } else {
            addAllFiles0(input, outputDir);
        }
    }

    private void addAllFiles0(File input, File output) throws Exception {
        if (input.isDirectory()) {
            File childs[] = input.listFiles();
            if (childs == null) {
                return;
            }
            for (File f : childs) {
                if (f.isDirectory()) {
                    addAllFiles0(f, new File(output, f.getName()));
                } else {
                    files.put(f, output);
                }
            }
        } else {
            files.put(input, output);
        }
    }

    private void setStatus() {
        status.setMaxOperations(files.size());
    }

    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    private void startDecryption() throws Exception {
        for (File f : files.keySet()) {
            try {
                lock.acquire();
            } catch (Exception e) {
                CustomLogger.log(e.getMessage(), CustomLogger.ERROR);
            }
            TaskManager.submit(new DecryptFiles(f, files.get(f), status, lock, decr));
        }
    }

    private class DecryptFiles implements Runnable {

        private final File input;
        private final File outputDir;
        private final FileOperationsCompletedStatus status;
        private final Semaphore lock;
        private final byte decr[];

        protected DecryptFiles(File input, File outputDir, FileOperationsCompletedStatus status, Semaphore lock, byte[] decr) {
            this.input = input;
            this.outputDir = outputDir;
            this.status = status;
            this.lock = lock;
            this.decr = decr;
        }

        @Override
        public void run() {
            try {
                if (!input.isFile()) {
                    CustomLogger.log("Directory found instead of file.", CustomLogger.ERROR);
                } else {
                    Service.makeDirectories(outputDir);
                    Decrypt.decrypt(input, outputDir, decr);
                    CustomLogger.log("Decrypted: ".concat(input.toString()).concat(" -> ").concat(outputDir.toString()), CustomLogger.MESSAGE);
                }
            } catch (Exception e) {
                CustomLogger.log("Failed to decrypt: ".concat(input.toString()).concat("\nReason: ").concat(e.getMessage()), CustomLogger.ERROR);
            }
            try {
                lock.release();
            } catch (Exception e) {
            }
            status.fileOperationCompleted(1);
        }
    }
}
