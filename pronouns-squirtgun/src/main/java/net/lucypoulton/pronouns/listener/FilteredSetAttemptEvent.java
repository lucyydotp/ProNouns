package net.lucypoulton.pronouns.listener;

import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.platform.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class FilteredSetAttemptEvent implements PlayerEvent {

    private final SquirtgunPlayer player;
    private final String blockedInput;

    public FilteredSetAttemptEvent(SquirtgunPlayer player, String blockedInput) {
        this.player = player;
        this.blockedInput = blockedInput;
    }

    @Override
    public @NotNull SquirtgunPlayer player() {
        return player;
    }

    public String blockedInput() {
        return blockedInput;
    }
}
