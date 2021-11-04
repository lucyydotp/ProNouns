/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of ProNouns.
 *
 * ProNouns is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProNouns is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProNouns.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.lucypoulton.pronouns.command;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.lucypoulton.pronouns.api.PronounHandler;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.argument.OnlinePlayerArgument;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ShowPronounsNode extends AbstractNode<PermissionHolder> {
    private final PronounHandler handler;
    private final CommandArgument<SquirtgunPlayer> playerArgument;

    public ShowPronounsNode(Platform platform, PronounHandler handler) {
        super("show", "Shows your, or another player's, pronouns.", Condition.alwaysTrue());
        this.handler = handler;
        playerArgument = new OnlinePlayerArgument("player", "the player to get pronouns for",
            true, platform);
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return ImmutableList.of(playerArgument);
    }

    @Override
    public Component execute(CommandContext context) {
        final FormatProvider fmt = context.getFormat();

        SquirtgunPlayer commandTarget = context.getArgumentValue(playerArgument);

        if (commandTarget == null) {
            if (!(context.getTarget() instanceof SquirtgunPlayer)) {
                return fmt.getPrefix().append(fmt.formatMain("That player could not be found."));
            }
            commandTarget = (SquirtgunPlayer) context.getTarget();
        }

        Set<PronounSet> sets = handler.getPronouns(commandTarget);

        return fmt.getPrefix()
            .append(fmt.formatMain(commandTarget.getUsername() + "'s pronouns are "))
            .append(fmt.formatAccent(
                sets.isEmpty() ? "unset" : PronounSet.format(sets)
            ));
    }
}
