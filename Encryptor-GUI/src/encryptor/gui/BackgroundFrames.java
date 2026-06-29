package encryptor.gui;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import encryptor.videomanager.Frames;

@SuppressWarnings("ClassWithoutLogger")
public class BackgroundFrames implements Frames {

    private static final String[] PATH_TO_FRAMES = new String[]{
        "/background_images/0.jpg",
        "/background_images/1.jpg",
        "/background_images/2.jpg",
        "/background_images/3.jpg",
        "/background_images/4.jpg",
        "/background_images/5.jpg",
        "/background_images/6.jpg"
    };

    @Override
    public String[] getFramesPath() {
        return PATH_TO_FRAMES.clone();
    }
}
