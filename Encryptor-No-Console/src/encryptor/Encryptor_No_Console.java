package encryptor;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import encryptor.codec.DecryptService;
import encryptor.codec.Encrypt;
import encryptor.codec.EncryptService;
import encryptor.codec.FileOperationsCompletedStatus;
import encryptor.codec.Service;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class Encryptor_No_Console {

    public static final String PATH_SEPARATOR_PATTERN = Pattern.quote(File.pathSeparator);
    private static final String WELCOME_MESSAGE = """
                                                    ______                                         _                  
                                                   |  ____|                                       | |                 
                                                   | |__     _ __     ___   _ __   _   _   _ __   | |_    ___    _ __ 
                                                   |  __|   | '_ \\   / __| | '__| | | | | | '_ \\  | __|  / _ \\  | '__|
                                                   | |____  | | | | | (__  | |    | |_| | | |_) | | |_  | (_) | | |   
                                                   |______| |_| |_|  \\___| |_|     \\__, | | .__/   \\__|  \\___/  |_|   
                                                                                    __/ | | |                         
                                                                                   |___/  |_|           
                                                  Encrypt or decrypt files with encryptor.
                                                  Encryptor accepts multiple files and folders (separated by \"""" + File.pathSeparator + """
                                                  \") as input and performs operations recursively and concurrently.
                                                  It hides your file's name (not folder's name) for better confidentiality.
                                                  Use "Encryptor-No-Console --help" for viewing help page.
                                                  """;
    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final String ENCRYPT_MODE_COMMAND_LINE_ARGUMENT = "-(e|-encrypt)";
    private static final String DECRYPT_MODE_COMMAND_LINE_ARGUMENT = "-(d|-decrypt)";
    private static final String PATH_COMMAND_LINE_ARGUMENT = "-(p|-path)";
    private static final String THREAD_COUNT_COMMAND_LINE_ARGUMENT = "-(t|-threads)";
    private static final String PASSWORD_COMMAND_LINE_ARGUMENT = "--password";
    private static final String HELP_MESSAGE_COMMAND_LINE_ARGUMENT = "-(h|-help)";
    private static final int ENCRYPT_MODE_CHOICE = 1;
    private static final int DECRYPT_MODE_CHOICE = 2;
    private static final int DEFAULT_CHOICE = ENCRYPT_MODE_CHOICE;
    private static final int MIN_THREADS_COUNT = 1;
    private static final int MAX_THREADS_COUNT = 10;
    private static final int DEFAULT_THREADS_COUNT = 5;
    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final int MAX_PASSWORD_LENGTH = 50;
    private static final String HELP_MESSAGE = """
                                               
                                               Usage :
                                               
                                               Encryptor-No-Console [options] --path path1 --password mypassword [path2] [path3] [path4""" + File.pathSeparator + """
                                               path5]
                                               
                                               Options :
                                               
                                               --encrypt , -e   Encryption mode. Default choice.
                                               --decrypt , -d   Decryption mode.
                                               --path , -p      Path to the files and folders. If path to folder is given then it performs operations recursively and concurrently.
                                               --threads , -t   Threads count in the range [""" + MIN_THREADS_COUNT + "," + MAX_THREADS_COUNT + """
                                               ]. Default value = [""" + DEFAULT_THREADS_COUNT + """
                                               ].
                                               --password       Password used to encrypt or decrypt the file. Password's length must be in the range of [""" + MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + """
                                               ].
                                               --help , -h      To view this help page.                                                                                                                                                              
                                               
                                               Examples :
                                               
                                               Encryptor-No-Console -e --path path_to_the_file_or_folder -t 10 --password mypassword      -> Encrypts path_to_the_file_or_folder with password "mypassword" and [10] threads running concurrently.
                                               
                                               Encryptor-No-Console --password mypassword path_to_the_file_or_folder                   -> Encrypts path_to_the_file_or_folder with password "mypassword" and [""" + DEFAULT_THREADS_COUNT + """
                                               ] threads running concurrently.
                                                                                                                                                                                                                                            
                                               """;
    private static final String TASKS_STARTING = """
                                                 Tasks starting....""";
    private static final String TASKS_COMPLETED = """
                                                  Tasks completed.""";
    private static final String EXIT_MESSAGE = """
                                                 ____   __     __  ______ 
                                                |  _ \\  \\ \\   / / |  ____|
                                                | |_) |  \\ \\_/ /  | |__   
                                                |  _ <    \\   /   |  __|  
                                                | |_) |    | |    | |____ 
                                                |____/     |_|    |______|
                                                                          
                                                                          
                                               """;
    private static final Logger LOG = Logger.getLogger(Encryptor_No_Console.class.getName());

    private static boolean addFiles(List<File> files, String text) {
        try {
            if (text == null) {
                return false;
            }
            for (String path : text.split(PATH_SEPARATOR_PATTERN)) {
                File f = new File(path);
                if (f.exists()) {
                    files.add(f);
                } else {
                    System.out.println("File \"".concat(path).concat("\" doesnot exists."));
                    return false;
                }
            }
            return !files.isEmpty();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @SuppressWarnings({"ValueOfIncrementOrDecrementUsed", "AssignmentToForLoopParameter", "UseOfSystemOutOrSystemErr"})
    public static void main(String[] args) {
        try {
            CustomLogger.init();
            System.out.println(WELCOME_MESSAGE);
            int opnum = DEFAULT_CHOICE;
            List<File> paths = new ArrayList<>(1);
            int threads = DEFAULT_THREADS_COUNT;
            char pass[] = null;
            if (args.length > 0) {
                int n = args.length;
                for (int i = 0; i < n; i++) {
                    if (args[i].matches(HELP_MESSAGE_COMMAND_LINE_ARGUMENT)) {
                        System.out.println(HELP_MESSAGE);
                        return;
                    } else if (args[i].matches(ENCRYPT_MODE_COMMAND_LINE_ARGUMENT)) {
                        opnum = ENCRYPT_MODE_CHOICE;
                    } else if (args[i].matches(DECRYPT_MODE_COMMAND_LINE_ARGUMENT)) {
                        opnum = DECRYPT_MODE_CHOICE;
                    } else if (i + 1 < n) {
                        if (args[i].matches(PATH_COMMAND_LINE_ARGUMENT)) {
                            if (!addFiles(paths, args[++i])) {
                                return;
                            }
                        } else if (args[i].matches(THREAD_COUNT_COMMAND_LINE_ARGUMENT)) {
                            threads = getPositiveInt(args[++i]);
                        } else if (args[i].matches(PASSWORD_COMMAND_LINE_ARGUMENT)) {
                            pass = args[++i].toCharArray();
                        } else {
                            if (!addFiles(paths, args[i])) {
                                return;
                            }
                        }
                    } else {
                        if (!addFiles(paths, args[i])) {
                            return;
                        }
                    }
                }
                if (!verifyArguments(opnum, paths, threads, pass)) {
                    return;
                }
                performTask(opnum, paths, threads, pass);
            } else {
                System.out.println(HELP_MESSAGE);
            }
            System.out.println(EXIT_MESSAGE);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.toString());
        }
    }

    private static boolean verifyArguments(int choice, List<File> paths, int threads, char pass[]) {
        if (choice != ENCRYPT_MODE_CHOICE && choice != DECRYPT_MODE_CHOICE) {
            System.out.println("Invalid choice!");
            return false;
        }
        if (paths == null) {
            return false;
        }
        for (File path : paths) {
            if (!validFile(path)) {
                System.out.println("Invalid path \"" + fileToString(path) + "\".");
                return false;
            }
        }
        if (!validThreadCount(threads)) {
            System.out.println("Invalid thread count. Thread count should be in range of [" + MIN_THREADS_COUNT + "," + MAX_THREADS_COUNT + "].");
            return false;
        }
        if (!validPassword(pass)) {
            System.out.println("Password's criteria not met. Password's length must be in the range of [" + MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + "].");
            return false;
        }
        return true;
    }

    private static String fileToString(File file) {
        return (file == null) ? "" : file.toString();
    }

    private static boolean validFile(File file) {
        return file != null && file.exists();
    }

    private static boolean validThreadCount(int count) {
        return count >= MIN_THREADS_COUNT && count <= MAX_THREADS_COUNT;
    }

    private static boolean validPassword(char pass[]) {
        return pass != null && pass.length >= MIN_PASSWORD_LENGTH && pass.length <= MAX_PASSWORD_LENGTH;
    }

    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    private static int getPositiveInt(String s) {
        try {
            return Math.max(Integer.parseInt(s), -1);
        } catch (Exception e) {
            return -1;
        }
    }

    private static void performTask(int choice, List<File> path, int thread, char pass[]) {
        System.out.println(TASKS_STARTING);
        try {
            RemoveChilds.remove(path);
            FileOperationsCompletedStatus focs = new FileOperationsCompletedStatus();
            byte passencr[] = Encrypt.encrypt(new String(pass), HASHING_ALGORITHM);
            Service service = switch (choice) {
                case ENCRYPT_MODE_CHOICE ->
                    new EncryptService(path, focs, thread, passencr);
                default ->
                    new DecryptService(path, focs, thread, passencr);
            };
            service.startService();
            while (!focs.allOperationsCompleted()) {
            }
        } catch (Exception e) {
            CustomLogger.log(e.toString(), CustomLogger.ERROR);
        }
        System.out.println(TASKS_COMPLETED);
    }

}
