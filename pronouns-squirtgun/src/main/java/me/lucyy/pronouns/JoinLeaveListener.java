package me.lucyy.pronouns;

import me.lucyy.pronouns.config.ConnectionType;
import me.lucyy.pronouns.storage.MysqlFileStorage;
import net.lucypoulton.squirtgun.platform.EventListener;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.scheduler.Task;

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
