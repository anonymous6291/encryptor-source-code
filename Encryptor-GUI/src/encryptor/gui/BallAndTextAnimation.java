package encryptor.gui;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings({"ClassWithoutLogger", "unused"})
public class BallAndTextAnimation implements Animator {

    private final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 60);
    private final int FRAMES_INTERVAL_MILLISECONDS = 20;
    private final int BALL_GAPS = 0;
    private final int width;
    private final int height;
    private String runningMessage;
    private String finishMessage;
    private final Color background;
    private Color foreground;
    private final Color finishColor;
    private final BufferedImage scene;
    private final Graphics2D g2d;
    private final JPanel display;
    private final Timer timer;
    private final VerticalBouncingBall vbbs[];
    private volatile boolean play;
    private int ballSize;
    private int fallHeight;
    private int mX;
    private int mY;

    public BallAndTextAnimation(int width, int height, Color background, Color foreground, String runningMessage, String finishMessage, Color finishColor) {
        this.width = width;
        this.height = height;
        this.background = background;
        this.foreground = foreground;
        this.runningMessage = runningMessage;
        this.finishMessage = finishMessage;
        this.finishColor = finishColor;
        scene = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = scene.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));
        g2d.setFont(FONT);
        calculateValues();
        display = new JPanel(new BorderLayout()) {
            @Override
            public void paint(Graphics g) {
                g.drawImage(scene, 0, 0, this);
            }
        };
        display.setPreferredSize(new Dimension(width, height));
        display.setDoubleBuffered(true);
        timer = new Timer(FRAMES_INTERVAL_MILLISECONDS, (ae) -> {
            animate();
        });
        vbbs = makeVerticalBouncingBalls(Math.max(runningMessage.length(), finishMessage.length()));
        play = false;
    }

    private void calculateValues() {
        int n = runningMessage.length();
        Rectangle2D r = FONT.getStringBounds(runningMessage, g2d.getFontRenderContext());
        ballSize = (int) (r.getWidth() / n);
        fallHeight = height - 6 * ballSize;
        fallHeight -= (fallHeight % n);
        if (fallHeight <= 0) {
            fallHeight = height + n - (height % runningMessage.length());
        }
        mX = (int) ((width - r.getWidth()) / 2);
        mY = height / 2;
    }

    private VerticalBouncingBall[] makeVerticalBouncingBalls(int n) {
        VerticalBouncingBall bbs[] = new VerticalBouncingBall[n];
        for (int i = 0; i < n; i++) {
            bbs[i] = new VerticalBouncingBall(ballSize, fallHeight, (fallHeight * i) / n, width, height, i, n, BALL_GAPS, RandomColorGenerator.generate(), finishColor, g2d);
        }
        return bbs;
    }

    public void setRunningMessage(String runningMessage) {
        this.runningMessage = runningMessage;
    }

    public void setFinishMessage(String finishMessage) {
        this.finishMessage = finishMessage;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public void setErrorStatus(String text, Color error) {
        setFinishMessage(text);
        setForeground(error);
        for (VerticalBouncingBall vbb : vbbs) {
            vbb.setMovingColor(error);
            vbb.setStopColor(error);
        }
    }

    @Override
    public Container getDisplay() {
        return display;
    }

    @Override
    public void animate() {
        try {
            g2d.setColor(background);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(foreground);
            if (play) {
                g2d.drawString(runningMessage, mX, mY);
            } else {
                g2d.drawString(finishMessage, mX, mY);
            }
            boolean stopped = true;
            for (VerticalBouncingBall vbb : vbbs) {
                vbb.move();
                if (!vbb.stopped()) {
                    stopped = false;
                }
            }
            display.repaint();
            if (!play && stopped && timer.isRunning()) {
                timer.stop();
            }
        } catch (Exception e) {
        }
    }

    private void startAnimation() {
        play = true;
        int i = 0, n = runningMessage.length();
        for (VerticalBouncingBall vbb : vbbs) {
            vbb.setStopColor(finishColor);
            vbb.setMovingColor(RandomColorGenerator.generate());
            vbb.setY((height * i) / n);
            vbb.resume();
            ++i;
        }
    }

    @Override
    public void start() {
        startAnimation();
        timer.start();
    }

    @Override
    public void stop() {
        play = false;
        for (VerticalBouncingBall vbb : vbbs) {
            vbb.stop();
        }
    }

    @Override
    public void stopImmediately() {
        play = false;
        timer.stop();
        for (VerticalBouncingBall vbb : vbbs) {
            vbb.stopImmediately();
        }
        animate();
    }

    @Override
    public void pause() {
        stop();
    }

    @Override
    public void resume() {
        startAnimation();
        timer.restart();
    }

    @Override
    public boolean playing() {
        return play;
    }

    @Override
    public boolean finished() {
        return !timer.isRunning();
    }
}
