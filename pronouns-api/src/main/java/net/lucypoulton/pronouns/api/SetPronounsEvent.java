package net.lucypoulton.pronouns.api;

import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.platform.event.player.AbstractCancellablePlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SetPronounsEvent extends AbstractCancellablePlayerEvent {
    private final SquirtgunPlayer player;
    private final Set<PronounSet> set;

    public SetPronounsEvent(SquirtgunPlayer player, Set<PronounSet> set) {
        super(player);
        this.player = player;
        this.set = set;
    }

    @Override
    public @NotNull SquirtgunPlayer player() {
        return player;
    }

    public Set<PronounSet> getSet() {
        return set;
    }
}
