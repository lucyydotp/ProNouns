package net.lucypoulton.pronouns;

import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.platform.Platform;

public interface ProNounsPlatform extends Platform {
    ConfigHandler getConfigHandler();

    Storage getStorage();

    void reloadConfig();
}
