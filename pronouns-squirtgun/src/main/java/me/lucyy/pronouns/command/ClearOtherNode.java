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

package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.argument.OnlinePlayerArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.AbstractNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import me.lucyy.squirtgun.platform.audience.SquirtgunPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ClearOtherNode extends AbstractNode<PermissionHolder> {
    private final ProNouns pl;
    private final CommandArgument<SquirtgunPlayer> playerArg;

    public ClearOtherNode(ProNouns plugin) {
        super("clearother", "Clears another player's pronouns.", "pronouns.admin");
        pl = plugin;
        playerArg = new OnlinePlayerArgument("player", "The player to set pronouns for",
                false, pl.getPlatform());
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return List.of(playerArg);
    }

    @Override
    public Component execute(CommandContext<PermissionHolder> context) {
        final FormatProvider fmt = context.getFormat();

        SquirtgunPlayer target = context.getArgumentValue(playerArg);
        Objects.requireNonNull(target); // required arg - this is safe

        pl.getPronounHandler().clearUserPronouns(target.getUuid());
        return fmt.getPrefix()
                .append(fmt.formatMain("Cleared " + target.getUsername() + "'s pronouns."));
    }
}
