package encryptor.gui;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.awt.Color;
import java.awt.Graphics2D;

@SuppressWarnings("ClassWithoutLogger")
public class VerticalBouncingBall {

    private static final int FALLING_SPEED = -4;
    private static final int RISING_SPEED = 3;
    private final int bS;
    private final int fH;
    private final int gX;
    private final int gY;
    private final Graphics2D g2d;
    private Color mC;
    private Color sC;
    private volatile float speed;
    private volatile int cY;
    private volatile boolean stop;
    private volatile boolean stopImmediately;

    public VerticalBouncingBall(int bS, int fH, int sH, int dW, int dH, int nthHB, int tHB, int gS, Color movingColor, Color stopColor, Graphics2D g2d) {
        this.bS = bS;
        this.fH = fH;
        cY = Math.min(sH, fH);
        gX = (dW - (bS + gS) * tHB + gS) / 2 + (bS + gS) * nthHB;
        gY = (dH - fH - 2 * bS) / 2;
        mC = movingColor;
        sC = stopColor;
        this.g2d = g2d;
        stop = false;
        stopImmediately = false;
        speed = FALLING_SPEED;
    }

    public void setY(int cY) {
        this.cY = Math.max(0, cY);
    }

    public void setMovingColor(Color mC) {
        this.mC = mC;
    }

    public void setStopColor(Color sC) {
        this.sC = sC;
    }

    public void move() {
        if (stop) {
            g2d.setColor(sC);
        } else {
            g2d.setColor(mC);
        }
        g2d.fillOval(gX, gY + cY, bS, bS);
        if (stopImmediately) {
            return;
        }
        cY -= speed;
        if (stop) {
            speed = RISING_SPEED;
        }
        if (cY <= 0) {
            if (stop) {
                stopImmediately = true;
            }
            cY = 0;
            speed = FALLING_SPEED;
        } else if (cY >= fH) {
            cY = fH;
            speed = RISING_SPEED;
        }
    }

    public void stop() {
        stop = true;
    }

    public void resume() {
        stop = false;
        stopImmediately = false;
    }

    public void stopImmediately() {
        stop = true;
        stopImmediately = true;
    }

    public boolean stopped() {
        return stopImmediately;
    }
}
