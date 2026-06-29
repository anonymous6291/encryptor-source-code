package encryptor.codec;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
@SuppressWarnings("ClassWithoutLogger")
public class FileOperationsCompletedStatus {

    private volatile double maxCount;
    private volatile double completed;

    public FileOperationsCompletedStatus() {
        this(0);
    }

    public FileOperationsCompletedStatus(double maxCount) {
        this.maxCount = maxCount;
        completed = 0;
    }

    public void setMaxOperations(double max) {
        maxCount = max;
        completed = 0;
    }

    public boolean allOperationsCompleted() {
        return maxCount <= completed;
    }

    public double getProgress() {
        if (maxCount == 0) {
            return 100.0;
        }
        return Math.min((completed * 100.0) / maxCount, 100.0);
    }

    public synchronized void fileOperationCompleted(double size) {
        completed += size;
    }
}
