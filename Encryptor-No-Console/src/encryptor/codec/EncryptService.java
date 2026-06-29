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
public class EncryptService implements Service {

    private final File input;
    private final File outputPath;
    private final boolean explicitFileListGiven;
    private final List<File> explicitFileList;
    private final FileOperationsCompletedStatus status;
    private final Semaphore lock;
    private final byte encr[];
    private final Map<File, File> files;

    public EncryptService(File input, FileOperationsCompletedStatus status, int threads, byte encr[]) {
        this.input = input;
        outputPath = getEncryptedDirectory(input);
        explicitFileListGiven = false;
        explicitFileList = null;
        this.status = status;
        lock = new Semaphore(threads, true);
        this.encr = encr;
        files = new HashMap<>(1);
    }

    public EncryptService(List<File> explicitFileList, FileOperationsCompletedStatus status, int threads, byte encr[]) {
        input = null;
        outputPath = null;
        explicitFileListGiven = true;
        this.explicitFileList = explicitFileList;
        this.status = status;
        lock = new Semaphore(threads, true);
        this.encr = encr;
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
        startEncryption();
    }

    private File getEncryptedDirectory(File curr) {
        return Service.getServicePathInParent(curr, ENCRYPTED_FOLDER_NAME, true);
    }

    private void addAllFiles() throws Exception {
        if (explicitFileListGiven) {
            if (explicitFileList == null) {
                return;
            }
            for (File f : explicitFileList) {
                addAllFiles0(f, getEncryptedDirectory(f));
            }
        } else {
            addAllFiles0(input, outputPath);
        }
    }

    private void addAllFiles0(File inputFile, File output) throws Exception {
        if (inputFile.isDirectory()) {
            File childs[] = inputFile.listFiles();
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
            files.put(inputFile, output);
        }
    }

    private void setStatus() {
        status.setMaxOperations(files.size());
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    private void startEncryption() throws Exception {
        for (File f : files.keySet()) {
            try {
                lock.acquire();
            } catch (Exception e) {
                CustomLogger.log(e.getMessage(), CustomLogger.ERROR);
            }
            TaskManager.submit(new EncryptFiles(f, files.get(f), status, lock, encr));
        }
    }

    private class EncryptFiles implements Runnable {

        private final File input;
        private final File output;
        private final FileOperationsCompletedStatus status;
        private final Semaphore lock;
        private final byte encr[];

        protected EncryptFiles(File input, File output, FileOperationsCompletedStatus status, Semaphore lock, byte encr[]) {
            this.input = input;
            this.output = output;
            this.status = status;
            this.lock = lock;
            this.encr = encr;
        }

        @Override
        public void run() {
            try {
                if (!input.isFile()) {
                    CustomLogger.log("Directory found instead of file.", CustomLogger.ERROR);
                } else {
                    Service.makeDirectories(output);
                    Encrypt.encrypt(input, output, encr);
                    CustomLogger.log("Encrypted: ".concat(input.toString()).concat(" -> ").concat(output.toString()), CustomLogger.MESSAGE);
                }
            } catch (Exception e) {
                CustomLogger.log("Failed to encrypt: ".concat(input.toString()).concat("\nReason: ").concat(e.getMessage()), CustomLogger.ERROR);
            }
            try {
                lock.release();
            } catch (Exception e) {
            }
            status.fileOperationCompleted(1);
        }
    }

}
