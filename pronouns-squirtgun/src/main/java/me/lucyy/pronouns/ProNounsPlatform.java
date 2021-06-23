package me.lucyy.pronouns;

import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.storage.Storage;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;

import java.util.Set;
import java.util.UUID;

public interface ProNounsPlatform extends Platform {
    ConfigHandler getConfigHandler();

    Storage getStorage();

    void reloadConfig();

    void onPronounsSet(UUID uuid, Set<PronounSet> sets);

    void registerCommand(CommandNode<PermissionHolder> node);
}
