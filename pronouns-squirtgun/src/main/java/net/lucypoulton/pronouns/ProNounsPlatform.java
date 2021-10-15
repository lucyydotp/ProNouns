package net.lucypoulton.pronouns;

import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.platform.Platform;

import java.util.Set;
import java.util.UUID;

public interface ProNounsPlatform extends Platform {
    ConfigHandler getConfigHandler();

    Storage getStorage();

    void reloadConfig();
}
