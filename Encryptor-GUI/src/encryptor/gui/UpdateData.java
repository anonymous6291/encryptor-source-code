package encryptor.gui;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
@SuppressWarnings("ClassWithoutLogger")
public class UpdateData {

    private final boolean checked;
    private final boolean failed;
    private final String versionString;
    private final boolean updateAvailable;
    private final String updateData;

    public UpdateData(boolean checked, boolean failed, String versionString, boolean updateAvailable, String updateData) {
        this.checked = checked;
        this.failed = failed;
        this.versionString = versionString;
        this.updateAvailable = updateAvailable;
        this.updateData = updateData;
    }

    public boolean checked() {
        return checked;
    }

    public boolean failed() {
        return failed;
    }

    public String getVersionString() {
        return versionString;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getUpdateData() {
        return updateData;
    }

}
