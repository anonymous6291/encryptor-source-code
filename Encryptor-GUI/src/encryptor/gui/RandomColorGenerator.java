package encryptor.gui;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.awt.Color;
import java.util.Random;

@SuppressWarnings("ClassWithoutLogger")
public class RandomColorGenerator {

    private static final int ALPHA_RANGE_START = 100;
    private static final int ALPHA_RANGE_STOP = 150;
    private static final Random random = new Random();

    private static int generateRandomIntInRange(int start, int end) {
        return (int) (random.nextDouble() * (end - start) + start);
    }

    public static Color generate() {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), generateRandomIntInRange(ALPHA_RANGE_START, ALPHA_RANGE_STOP));
    }

    private RandomColorGenerator() {
    }

}
