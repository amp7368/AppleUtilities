package apple.utilities.qol;

import org.jetbrains.annotations.NotNull;

public interface HoldConfigable<Config> extends GetConfigable<Config> {
    @NotNull
    default Config verifyConfig() {
        if (getConfig() == null) {
            setConfig();
        }
        return getConfig();
    }

    void setConfig();
}
