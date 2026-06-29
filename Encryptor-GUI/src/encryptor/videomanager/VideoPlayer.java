package encryptor.videomanager;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings({"ClassWithoutLogger", "unused"})
public class VideoPlayer extends JPanel {

    private static final long serialVersionUID = 6291L;
    private transient String[] framesPath;
    private transient int imagesCount;
    private transient volatile int currentImageIndex;
    private transient final boolean loop;
    private transient volatile boolean start;
    private transient volatile boolean played;
    private transient volatile BufferedImage bufferedImage;

    public VideoPlayer(boolean loop, Frames frames) {
        super();
        this.loop = loop;
        start = false;
        played = false;
        addImages(frames);
    }

    private void addImages(Frames frames) {
        framesPath = frames.getFramesPath();
        if (framesPath != null) {
            imagesCount = framesPath.length;
            Arrays.sort(framesPath, (a, b) -> a.compareTo(b));
        } else {
            imagesCount = 0;
        }
        currentImageIndex = 0;
    }

    @SuppressWarnings({"UseOfSystemOutOrSystemErr", "UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    @Override
    protected void paintComponent(Graphics g) {
        if (currentImageIndex < imagesCount) {
            try {
                bufferedImage = ImageIO.read(this.getClass().getResourceAsStream(framesPath[currentImageIndex]));
                g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), this);
            } catch (Exception ex) {
            }
        }
    }

    public void showNextFrame() {
        repaint();
        updateFrame();
    }

    public void updateFrame() {
        if (start) {
            if (currentImageIndex >= imagesCount - 1) {
                if (loop) {
                    currentImageIndex = 0;
                } else {
                    played = true;
                }
            } else {
                ++currentImageIndex;
            }
        }
    }

    public void playVideo() {
        start = true;
        played = false;
        currentImageIndex = 0;
    }

    public void stopVideo() {
        start = false;
        played = true;
    }

    public void resumeVideo() {
        start = true;
        played = false;
    }

    public boolean videoPlaying() {
        return !played;
    }

    public boolean videoPlayed() {
        return played;
    }
}
