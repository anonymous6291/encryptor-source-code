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
import java.util.regex.Pattern;

@SuppressWarnings("ClassWithoutLogger")
public class Encryptor_Console {

    public static final String PATH_SEPARATOR_PATTERN = Pattern.quote(File.pathSeparator);
    private static final String WELCOME_MESSAGE = """
                                                    ______                                         _                  
                                                   |  ____|                                       | |                 
                                                   | |__     _ __     ___   _ __   _   _   _ __   | |_    ___    _ __ 
                                                   |  __|   | '_ \\   / __| | '__| | | | | | '_ \\  |  _|  / _ \\  | '__|
                                                   | |____  | | | | | (__  | |    | |_| | | |_) | | |_  | (_) | | |   
                                                   |______| |_| |_|  \\___| |_|     \\__, | | .__/   \\__|  \\___/  |_|   
                                                                                    __/ | | |                         
                                                                                   |___/  |_|           
                                                  Encrypt or decrypt files with encryptor.
                                                  Encryptor accepts multiple files and folders (separated by \"""" + File.pathSeparator + """
                                                  \") as input and performs operations recursively and concurrently.
                                                  It hides your file names (not folder names) for better confidentiality. It also supports command-line usage.
                                                  Use "Encryptor-Console --help" for viewing help page.
                                                  """;
    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final String ENCRYPT_MODE_COMMAND_LINE_ARGUMENT = "-(e|-encrypt)";
    private static final String DECRYPT_MODE_COMMAND_LINE_ARGUMENT = "-(d|-decrypt)";
    private static final String PATH_COMMAND_LINE_ARGUMENT = "-(p|-path)";
    private static final String NO_MODIFY_COMMAND_LINE_ARGUMENT = "-(nm|-no-modify)";
    private static final String THREAD_COUNT_COMMAND_LINE_ARGUMENT = "-(t|-threads)";
    private static final String PASSWORD_COMMAND_LINE_ARGUMENT = "--password";
    private static final String NO_COLOR_COMMAND_LINE_ARGUMENT = "-(nc|-no-color)";
    private static final String HELP_MESSAGE_COMMAND_LINE_ARGUMENT = "-(h|-help)";
    private static final String YES_PATTERN = "(y|yes)";
    private static final String NO_PATTERN = "(n|no)";
    private static final int ENCRYPT_MODE_CHOICE = 1;
    private static final int DECRYPT_MODE_CHOICE = 2;
    private static final int DEFAULT_CHOICE = ENCRYPT_MODE_CHOICE;
    private static final int MIN_THREADS_COUNT = 1;
    private static final int MAX_THREADS_COUNT = 10;
    private static final int DEFAULT_THREADS_COUNT = 5;
    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final int MAX_PASSWORD_LENGTH = 50;
    private static final String HELP_MESSAGE = """
                                               Encryptor can be invoked directly by passing command-line arguments.
                                               
                                               Usage :
                                               
                                               Encryptor-Console [options] --path path1 --password mypassword [path2] [path3] [path4""" + File.pathSeparator + """
                                               path5]
                                               
                                               Options :
                                               
                                               --encrypt , -e     Encryption mode. Default choice.
                                               --decrypt , -d     Decryption mode.
                                               --path , -p        Path to the files and folders. If path to folder is given then it performs operations recursively and concurrently.
                                               --no-modify , -nm  Don't hide encrypted file names or don't retrieve decrypted file names.
                                               --threads , -t     Threads count in the range [""" + MIN_THREADS_COUNT + "," + MAX_THREADS_COUNT + """
                                               ]. Default value = [""" + DEFAULT_THREADS_COUNT + """
                                               ].
                                               --password         Password used to encrypt or decrypt the file. Password's length must be in the range of [""" + MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + """
                                               ].
                                               --no-color , -nc   To suppress color outputs.
                                               --help , -h        To view this help page.                                                                                                                                                              
                                               
                                               Examples :
                                               
                                               Encryptor-Console -e --path path_to_the_file_or_folder -t 10 --password mypassword      -> Encrypts path_to_the_file_or_folder with password "mypassword" and [10] threads running concurrently.
                                               
                                               Encryptor-Console --password mypassword path_to_the_file_or_folder                   -> Encrypts path_to_the_file_or_folder with password "mypassword" and [""" + DEFAULT_THREADS_COUNT + """
                                               ] threads running concurrently.
                                                                                                                                                                                                                                            
                                               """;
    private static final String EXIT_COMMAND = ".exit";
    private static final String CHOICE_QUERY = """
                                               
                                               Enter the option or \"""" + EXIT_COMMAND
            + """
              " to quit:
              """ + ENCRYPT_MODE_CHOICE + ") Encrypt\n" + DECRYPT_MODE_CHOICE + ") Decrypt\n";
    private static final String INVALID_CHOICE_MESSAGE = """
                                                 Invalid choice.""";
    private static final String PATH_QUERY = """
                                             
                                             Enter the paths separated by \"""" + File.pathSeparator + """
                                                                                \" or \"""" + EXIT_COMMAND
            + """
              " to quit:""";
    private static final String HIDE_FILE_NAME_QUERY = """
                                               
                                               You want to hide your file names (y or n) ?""";
    private static final String RETRIEVE_FILE_NAME_QUERY = """
                                                           
                                                           You want to retrieve your file names (y or n) ?""";
    private static final String INVALID_MODIFY_FILE_NAME_QUERY = """
                                                                 Invalid choice. Enter y or n.""";
    private static final String INVALID_PATH_MESSAGE = """
                                               Invalid path.""";
    private static final String THREAD_COUNT_QUERY = """
                                                     
                                                     Enter the thread count in the range [""" + MIN_THREADS_COUNT + "," + MAX_THREADS_COUNT + """
                                                     ] or \"""" + EXIT_COMMAND + """
                                                     " to quit ( Default value =""" + " " + DEFAULT_THREADS_COUNT + " ):";
    private static final String INVALID_THREAD_COUNT_MESSAGE = """
                                                       Invalid thread count.""";
    private static final String PASSWORD_QUERY = """
                                                 
                                                 Enter the password. Password's length must be in the range of [""" + MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + """
                                                                                                                                                                      ]:""";
    private static final String INVALID_PASSWORD_MESSAGE = """
                                                             Password's criteria not met.""";
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
                    CustomLogger.log("File \"".concat(path).concat("\" doesnot exists."), CustomLogger.ERROR);
                    return false;
                }
            }
            return !files.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private static String fileToString(File file) {
        return (file == null) ? "" : file.toString();
    }

    private static boolean validChoice(int i) {
        return i == ENCRYPT_MODE_CHOICE || i == DECRYPT_MODE_CHOICE;
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

    @SuppressWarnings({"ValueOfIncrementOrDecrementUsed", "AssignmentToForLoopParameter"})
    public static void main(String[] args) {
        try {
            StandardIO.init();
            CustomLogger.init(true);
            if (args != null) {
                for (String s : args) {
                    if (s != null && s.matches(NO_COLOR_COMMAND_LINE_ARGUMENT)) {
                        CustomLogger.enableColor(false);
                        break;
                    }
                }
            }
            CustomLogger.log(WELCOME_MESSAGE, CustomLogger.MESSAGE);
            TaskManager.submit(() -> CheckForUpdates.check());
            boolean flag = true;
            int opnum = DEFAULT_CHOICE;
            List<File> paths = new ArrayList<>(1);
            boolean modifyFileName = true;
            int threads = DEFAULT_THREADS_COUNT;
            char pass[] = null;
            if (args != null && args.length > 0) {
                int n = args.length;
                for (int i = 0; i < n; i++) {
                    if (args[i] == null || args[i].isBlank() || args[i].matches(NO_COLOR_COMMAND_LINE_ARGUMENT)) {
                    } else if (args[i].matches(HELP_MESSAGE_COMMAND_LINE_ARGUMENT)) {
                        CustomLogger.log(HELP_MESSAGE, CustomLogger.MESSAGE);
                        return;
                    } else if (args[i].matches(ENCRYPT_MODE_COMMAND_LINE_ARGUMENT)) {
                        opnum = ENCRYPT_MODE_CHOICE;
                    } else if (args[i].matches(DECRYPT_MODE_COMMAND_LINE_ARGUMENT)) {
                        opnum = DECRYPT_MODE_CHOICE;
                    } else if (args[i].matches(NO_MODIFY_COMMAND_LINE_ARGUMENT)) {
                        modifyFileName = false;
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
                if (!validArguments(opnum, paths, threads, pass)) {
                    return;
                }
                performTask(opnum, paths, threads, modifyFileName, pass);
            } else {
                while (flag) {
                    while (true) {
                        CustomLogger.log(CHOICE_QUERY, CustomLogger.NORMAL);
                        String input = StandardIO.readLine();
                        if (input.equals(EXIT_COMMAND)) {
                            flag = false;
                            break;
                        }
                        opnum = getPositiveInt(input);
                        if (validChoice(opnum)) {
                            break;
                        }
                        CustomLogger.log(INVALID_CHOICE_MESSAGE, CustomLogger.ERROR);
                    }
                    if (!flag) {
                        break;
                    }
                    while (true) {
                        paths.clear();
                        CustomLogger.log(PATH_QUERY, CustomLogger.NORMAL);
                        String input = StandardIO.readLine();
                        if (input.equals(EXIT_COMMAND)) {
                            flag = false;
                            break;
                        }
                        if (addFiles(paths, input)) {
                            break;
                        }
                        CustomLogger.log(INVALID_PATH_MESSAGE, CustomLogger.ERROR);
                    }
                    if (!flag) {
                        break;
                    }
                    while (true) {
                        if (opnum == ENCRYPT_MODE_CHOICE) {
                            CustomLogger.log(HIDE_FILE_NAME_QUERY, CustomLogger.NORMAL);
                        } else {
                            CustomLogger.log(RETRIEVE_FILE_NAME_QUERY, CustomLogger.NORMAL);
                        }
                        String input = StandardIO.readLine();
                        if (input.equals(EXIT_COMMAND)) {
                            flag = false;
                            break;
                        }
                        input = input.toLowerCase();
                        if (input.matches(YES_PATTERN)) {
                            modifyFileName = true;
                            break;
                        } else if (input.matches(NO_PATTERN)) {
                            modifyFileName = false;
                            break;
                        }
                        CustomLogger.log(INVALID_MODIFY_FILE_NAME_QUERY, CustomLogger.ERROR);
                    }
                    if (!flag) {
                        break;
                    }
                    while (true) {
                        CustomLogger.log(THREAD_COUNT_QUERY, CustomLogger.NORMAL);
                        String input = StandardIO.readLine();
                        if (input.equals(EXIT_COMMAND)) {
                            flag = false;
                            break;
                        }
                        if (input.isBlank()) {
                            threads = DEFAULT_THREADS_COUNT;
                        } else {
                            threads = getPositiveInt(input);
                        }
                        if (validThreadCount(threads)) {
                            break;
                        }
                        CustomLogger.log(INVALID_THREAD_COUNT_MESSAGE, CustomLogger.ERROR);
                    }
                    if (!flag) {
                        break;
                    }
                    while (true) {
                        CustomLogger.log(PASSWORD_QUERY, CustomLogger.NORMAL);
                        pass = StandardIO.readPassword();
                        if (validPassword(pass)) {
                            break;
                        }
                        CustomLogger.log(INVALID_PASSWORD_MESSAGE, CustomLogger.ERROR);
                    }
                    if (!validArguments(opnum, paths, threads, pass)) {
                        CustomLogger.log("Internal error occured.", CustomLogger.ERROR);
                        return;
                    }
                    performTask(opnum, paths, threads, modifyFileName, pass);
                }
            }
            if (CheckForUpdates.checked()) {
                CustomLogger.log(CheckForUpdates.getResult(), CustomLogger.MESSAGE);
            }
            CustomLogger.log(EXIT_MESSAGE, CustomLogger.MESSAGE);
        } catch (Exception e) {
            StandardIO.errprintln(e.getMessage());
        }
    }

    private static boolean validArguments(int choice, List<File> paths, int threads, char pass[]) {
        if (!validChoice(choice)) {
            CustomLogger.log("Invalid choice!", CustomLogger.ERROR);
            return false;
        }
        if (paths == null) {
            return false;
        }
        if (paths.isEmpty()) {
            CustomLogger.log("Empty path given.", CustomLogger.ERROR);
            return false;
        }
        for (File path : paths) {
            if (!validFile(path)) {
                CustomLogger.log("Invalid path \"" + fileToString(path) + "\".", CustomLogger.ERROR);
                return false;
            }
        }
        if (!validThreadCount(threads)) {
            CustomLogger.log("Invalid thread count. Thread count should be in range of [" + MIN_THREADS_COUNT + "," + MAX_THREADS_COUNT + "].", CustomLogger.ERROR);
            return false;
        }
        if (!validPassword(pass)) {
            CustomLogger.log("Password's criteria not met. Password's length must be in the range of [" + MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + "].", CustomLogger.ERROR);
            return false;
        }
        return true;
    }

    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    private static int getPositiveInt(String s) {
        try {
            return Math.max(Integer.parseInt(s), -1);
        } catch (Exception e) {
            return -1;
        }
    }

    private static void performTask(int choice, List<File> path, int thread, boolean modifyFileName, char pass[]) {
        CustomLogger.log(TASKS_STARTING, CustomLogger.MESSAGE);
        try {
            RemoveChilds.remove(path);
            FileOperationsCompletedStatus focs = new FileOperationsCompletedStatus();
            byte passencr[] = Encrypt.encrypt(new String(pass), HASHING_ALGORITHM);
            Service service = switch (choice) {
                case ENCRYPT_MODE_CHOICE ->
                    new EncryptService(path, focs, thread, modifyFileName, passencr);
                default ->
                    new DecryptService(path, focs, thread, modifyFileName, passencr);
            };
            service.startService();
            while (!focs.allOperationsCompleted()) {
            }
        } catch (Exception e) {
            CustomLogger.log(e.getMessage(), CustomLogger.ERROR);
        }
        CustomLogger.log(TASKS_COMPLETED, CustomLogger.MESSAGE);
    }
}
