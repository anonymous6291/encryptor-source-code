package encryptor;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import encryptor.gui.GUIManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseOfSystemOutOrSystemErr", "UseSpecificCatch", "ClassWithoutLogger"})
public class Encryptor_GUI {

    public static void main(String[] args) {
        try {
            try {
                UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
            } catch (Exception e) {
            }
            SwingUtilities.invokeLater(new GUIManager());
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
