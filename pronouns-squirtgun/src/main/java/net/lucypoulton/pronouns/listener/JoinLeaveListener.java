package net.lucypoulton.pronouns.listener;

import net.lucypoulton.pronouns.ProNouns;
import net.lucypoulton.pronouns.config.ConnectionType;
import net.lucypoulton.pronouns.storage.MysqlFileStorage;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.event.EventHandler;
import net.lucypoulton.squirtgun.platform.event.EventListener;
import net.lucypoulton.squirtgun.platform.event.player.PlayerJoinEvent;
import net.lucypoulton.squirtgun.platform.event.player.PlayerLeaveEvent;
import net.lucypoulton.squirtgun.platform.scheduler.Task;

import java.util.List;

public class JoinLeaveListener implements EventListener {

    private final ProNouns plugin;

    public JoinLeaveListener(ProNouns plugin) {
        this.plugin = plugin;
    }

    private void onJoin(PlayerJoinEvent event) {
        if (plugin.getConfigHandler().getConnectionType() == ConnectionType.MYSQL) {
            Task.builder()
                .async()
                .action((Platform ignored) -> {
                    MysqlFileStorage storage = (MysqlFileStorage) plugin.getPlatform().getStorage();
                    storage.getPronouns(event.player().getUuid(), false);
                })
                .build().execute(plugin.getPlatform());
        }
    }

    private void onLeave(PlayerLeaveEvent event) {
        if (plugin.getConfigHandler().getConnectionType() == ConnectionType.MYSQL) {
            MysqlFileStorage storage = (MysqlFileStorage) plugin.getPlatform().getStorage();
            storage.onPlayerDisconnect(event.player().getUuid());
        }
    }

    private final List<EventHandler<?>> handlers = List.of(
        EventHandler.executes(PlayerJoinEvent.class, this::onJoin),
        EventHandler.executes(PlayerLeaveEvent.class, this::onLeave)
    );

    @Override
    public List<EventHandler<?>> handlers() {
        return handlers;
    }
}
