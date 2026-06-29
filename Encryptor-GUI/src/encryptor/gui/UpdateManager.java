package encryptor.gui;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import encryptor.TaskManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("ClassWithoutLogger")
public class UpdateManager {

    private static Frame window;
    private static JDialog dialog;
    private static JLabel versionName;
    private static JTextArea information;
    private static boolean initialized;

    public static void setWindow(Frame window) {
        UpdateManager.window = window;
    }

    private static void init() {
        if (initialized) {
            return;
        }
        dialog = new JDialog(window, "Update available", JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        dialog.setResizable(false);
        Dimension size = window.getSize();
        dialog.setSize((3 * size.width) / 4, (3 * size.height) / 4);
        Point location = window.getLocation();
        dialog.setLocation(location.x + size.width / 8, location.y + size.height / 8);
        versionName = new JLabel();
        versionName.setVerticalTextPosition(JLabel.CENTER);
        versionName.setHorizontalTextPosition(JLabel.CENTER);
        versionName.setVerticalAlignment(JLabel.CENTER);
        versionName.setHorizontalAlignment(JLabel.CENTER);
        versionName.setForeground(new Color(51, 255, 51));
        versionName.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        information = new JTextArea();
        information.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        information.setForeground(new Color(137, 206, 148));
        information.setBackground(new Color(100, 49, 115));
        information.setEditable(false);
        JScrollPane jsp = new JScrollPane(information);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(new Color(51, 51, 51, 150));
        holder.add(versionName, BorderLayout.NORTH);
        holder.add(jsp, BorderLayout.CENTER);
        dialog.setContentPane(holder);
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void display(UpdateData update) {
        if (update.failed()) {
            System.err.println("Failed to check for updates.");
            return;
        }
        if (!update.isUpdateAvailable()) {
            System.out.println("You are using the latest version.");
            return;
        }
        versionName.setText("Latest version: ".concat(update.getVersionString()));
        information.setText(update.getUpdateData());
        TaskManager.submit(() -> dialog.setVisible(true));
    }

    private static boolean disableCheck() {
        try {
            return new File(System.getProperty("user.dir"), "nc.data").exists();
        } catch (Exception e) {
            return false;
        }
    }

    protected static void check() {
        if (!initialized) {
            init();
        }
        if (disableCheck()) {
            return;
        }
        display(CheckForUpdates.check());
    }

    private UpdateManager() {
    }
}
