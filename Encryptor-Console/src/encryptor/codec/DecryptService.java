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
    private final boolean retrieveFileName;
    private final byte decr[];
    private final Map<File, File> files;

    public DecryptService(File input, FileOperationsCompletedStatus status, int threads, boolean retrieveFileName, byte[] decr) throws Exception {
        this.input = input.getCanonicalFile();
        outputDir = getDecryptedDirectory(input.getCanonicalFile());
        explicitFileListGiven = false;
        explicitFileList = null;
        this.status = status;
        lock = new Semaphore(threads, true);
        this.retrieveFileName = retrieveFileName;
        this.decr = decr;
        files = new HashMap<>(1);
    }

    public DecryptService(List<File> explicitFileList, FileOperationsCompletedStatus status, int threads, boolean retrieveFileName, byte decr[]) {
        input = null;
        outputDir = null;
        this.explicitFileList = explicitFileList;
        explicitFileListGiven = true;
        this.status = status;
        lock = new Semaphore(threads, true);
        this.retrieveFileName = retrieveFileName;
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
        CustomLogger.log("Traversing files....", CustomLogger.MESSAGE);
        addAllFiles();
        CustomLogger.log("Files traversed and listed....", CustomLogger.MESSAGE);
        setStatus();
        startDecryption();
    }

    private File getDecryptedDirectory(File curr) throws Exception {
        return Service.getServicePathInParent(curr, DECRYPTED_FOLDER_NAME, true);
    }

    private void addAllFiles() throws Exception {
        if (explicitFileListGiven) {
            if (explicitFileList == null) {
                return;
            }
            for (File f : explicitFileList) {
                File original = f.getCanonicalFile();
                addAllFiles0(original, getDecryptedDirectory(original));
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
            TaskManager.submit(new DecryptFile(f, files.get(f), status, lock, retrieveFileName, decr));
        }
    }

    private class DecryptFile implements Runnable {

        private final File input;
        private final File outputDir;
        private final FileOperationsCompletedStatus status;
        private final Semaphore lock;
        private final boolean retrieveFileName;
        private final byte decr[];

        protected DecryptFile(File input, File outputDir, FileOperationsCompletedStatus status, Semaphore lock, boolean retrieveFileName, byte[] decr) {
            this.input = input;
            this.outputDir = outputDir;
            this.status = status;
            this.lock = lock;
            this.retrieveFileName = retrieveFileName;
            this.decr = decr;
        }

        @Override
        public void run() {
            try {
                if (!input.isFile()) {
                    CustomLogger.log("Directory found instead of file.", CustomLogger.ERROR);
                } else {
                    Service.makeDirectories(outputDir);
                    CustomLogger.log("Decrypting: ".concat(input.toString()), CustomLogger.NORMAL);
                    Decrypt.decrypt(input, outputDir, retrieveFileName, decr);
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
