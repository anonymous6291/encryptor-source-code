package encryptor.gui;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.awt.Container;

public interface Animator {

    public Container getDisplay();

    public void animate();

    public void start();

    public void stop();

    public void stopImmediately();

    public void pause();

    public void resume();

    public boolean playing();

    public boolean finished();
}
