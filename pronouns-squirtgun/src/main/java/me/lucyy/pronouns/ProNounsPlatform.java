package me.lucyy.pronouns;

import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;

import java.util.Set;
import java.util.UUID;

public interface ProNounsPlatform extends Platform {
    ConfigHandler getConfigHandler();

    Storage getStorage();

    void reloadConfig();

    void onPronounsSet(UUID uuid, Set<PronounSet> sets);
}
