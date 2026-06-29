package encryptor;


/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("ClassWithoutLogger")
public class TaskManager {

    private static final ExecutorService service = Executors.newFixedThreadPool(15);

    public static void submit(Runnable runnable) {
        service.submit(runnable);
    }

    private TaskManager() {
    }
}
