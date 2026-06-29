package encryptor.gui;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.awt.Color;

public interface DefaultColors {

    public final Color BACKGROUND_DISPLAY_COLOR = new Color(51, 0, 51);
    public final Color WELCOME_MESSAGE_COLOR = new Color(0, 204, 255);
    public final Color LEFT_MESSAGE_BACKGROUND_COLOR = new Color(51, 51, 51, 200);
    public final Color LEFT_MESSAGE_FOREGROUND_COLOR = WELCOME_MESSAGE_COLOR.darker();
    public final Color RIGHT_MESSAGE_BACKGROUND_COLOR = new Color(0, 102, 102, 200);
    public final Color RIGHT_MESSAGE_FOREGROUND_COLOR = WELCOME_MESSAGE_COLOR.darker();
    public final Color SELECTED_OPTION_BORDER_HIGHLIGHT_COLOR = Color.GREEN;
    public final Color SELECTED_OPTION_BORDER_SHADOW_COLOR = Color.GREEN;
    public final Color NOT_SELECTED_OPTION_BORDER_HIGHLIGHT_COLOR = Color.RED;
    public final Color NOT_SELECTED_OPTION_BORDER_SHADOW_COLOR = Color.RED;
    public final Color PROCEED_BUTTON_BACKGROUND_COLOR = new Color(255, 0, 153);
    public final Color PROCEED_BUTTON_FOREGROUND_COLOR = new Color(0, 0, 0);
    public final Color START_MENU_BACKGROUND_COLOR = new Color(204, 102, 0);
    public final Color START_MENU_FOREGROUND_COLOR = Color.BLACK;
    public final Color QUERY_LABEL_FOREGROUND_COLOR = new Color(0, 204, 204);
    public final Color QUERY_FIELD_BACKGROUND_COLOR = new Color(255, 102, 51);
    public final Color QUERY_FIELD_FOREGROUND_COLOR = Color.BLACK;
    public final Color CHOOSE_FILE_BUTTON_BACKGROUND_COLOR = Color.CYAN;
    public final Color CHOOSE_FILE_BUTTON_FOREGROUND_COLOR = Color.BLACK;
    public final Color SHOW_PASSWORD_BUTTON_BACKGROUND_COLOR = Color.MAGENTA;
    public final Color SHOW_PASSWORD_BUTTON_FOREGROUND_COLOR = Color.BLACK;
    public final Color PROGRESS_STATUS_MESSAGE_FOREGROUND_COLOR = Color.GREEN;
    public final Color PROGRESS_MESSAGE_BACKGROUND_COLOR = new Color(51, 0, 51);
    public final Color PROGRESS_MESSAGE_FOREGROUND_COLOR = new Color(0, 204, 255);
    public final Color PROGRESS_BAR_BACKGROUND_COLOR = Color.CYAN;
    public final Color RUNNING_STATUS_COLOR = Color.CYAN;
    public final Color SUCCESS_STATUS_COLOR = new Color(0, 255, 0, 150);
    public final Color FAILED_STATUS_COLOR = new Color(255, 0, 0, 150);
}
