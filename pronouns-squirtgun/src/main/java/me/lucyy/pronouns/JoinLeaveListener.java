package me.lucyy.pronouns;

import me.lucyy.pronouns.config.ConnectionType;
import me.lucyy.pronouns.storage.MysqlFileStorage;
import me.lucyy.squirtgun.platform.EventListener;
import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.scheduler.Task;

import java.util.UUID;

public class JoinLeaveListener extends EventListener {
    public JoinLeaveListener(ProNouns plugin) {
        super(plugin);
    }

    @Override
    public ProNouns getPlugin() {
        return (ProNouns) super.getPlugin();
    }

    @Override
    public void onPlayerJoin(UUID uuid) {
        if (getPlugin().getConfigHandler().getConnectionType() == ConnectionType.MYSQL) {
            Task.builder()
                    .async()
                    .action((Platform ignored) -> {
                    MysqlFileStorage storage = (MysqlFileStorage) getPlugin().getPlatform().getStorage();
                    storage.getPronouns(uuid, false);
                })
                    .build().execute(getPlugin().getPlatform());
        }
    }

    @Override
    public void onPlayerLeave(UUID uuid) {
        if (getPlugin().getConfigHandler().getConnectionType() == ConnectionType.MYSQL) {
            MysqlFileStorage storage = (MysqlFileStorage) getPlugin().getPlatform().getStorage();
            storage.onPlayerDisconnect(uuid);
        }
    }
}
