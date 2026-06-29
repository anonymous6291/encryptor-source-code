package encryptor.gui;

import java.io.File;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
public interface OtherConstants {

    public final int WINDOW_WIDTH = 700;
    public final int WINDOW_HEIGHT = 650;
    public final int FRAME_CHANGE_DELAY = 5_000;
    public final String ENCRYPTION_NAME = "SHA-256";
    public final int MIN_PASSWORD_LENGTH = 5;
    public final int MAX_PASSWORD_LENGTH = 50;
    public final int MIN_THREAD_COUNT = 1;
    public final int MAX_THREAD_COUNT = 10;
    public final int DEFAULT_THREAD_COUNT = 5;
    public final String PATH_SEPARATOR = File.pathSeparator;
    public final String WELCOME_MESSAGE = "Welcome to encryptor!";
    public final String LEFT_MESSAGE = "<html><center><h2><u>Encrypt</u></h2><br>Encrypt your files with your password. Enter file or folder path to encrypt. When folder is given, it encrypts all the files inside it recursively and concurrently.</center></html>";
    public final String RIGHT_MESSAGE = "<html><center><h2><u>Decrypt</u></h2><br>Decrypt your files with your password. Enter file or folder path to decrypt. When folder is given, it decrypts all the files inside it recursively and concurrently. (Only files encrypted by this program can be decrypted.)</center></html>";
    public final String ENTER_FILE_PATHS_MESSAGE = "<html><center>Enter file or folder paths seperated by '" + PATH_SEPARATOR + "' :</center></html>";
    public final String WRONG_FILE_PATHS_MESSAGE = "<html><center>Enter file or folder paths seperated by '" + PATH_SEPARATOR + "' :<br><label>(Invalid path!)</label></center></html>";
    public final String ENTER_PASSWORD_MESSAGE = "<html><center>Enter the password (Length must be in range of [" + MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + "]):</center></html>";
    public final String WRONG_PASSWORD_MESSAGE = "<html><center>Enter the password (Length must be in range of [" + MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + "]):<br><label>(Invalid password length!)</label></center></html>";
    public final String ENTER_THREAD_COUNT_MESSAGE = "<html><center>Enter number of threads [" + MIN_THREAD_COUNT + "," + MAX_THREAD_COUNT + "]:</center></html>";
    public final String WRONG_THREAD_COUNT_MESSAGE = "<html><center>Enter number of threads [" + MIN_THREAD_COUNT + "," + MAX_THREAD_COUNT + "]:<br><label>(Thread count should be in range[" + MIN_THREAD_COUNT + "," + MAX_THREAD_COUNT + "]!)</label></center></html>";

}
