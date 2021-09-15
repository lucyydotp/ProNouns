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
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.command.arguments.PronounSetArgument;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.argument.OnlinePlayerArgument;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SetOtherNode extends AbstractNode<PermissionHolder> {
    private final ProNouns pl;
    private final PronounSetArgument setsArg;
    private final CommandArgument<SquirtgunPlayer> playerArg;

    public SetOtherNode(ProNouns plugin) {
        super("setother", "Sets another player's pronouns.", Condition.hasPermission("pronouns.admin"));
        pl = plugin;
        playerArg = new OnlinePlayerArgument("player", "The player to set pronouns for",
                false, pl.getPlatform());
        setsArg = new PronounSetArgument(pl.getPronounHandler());
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return List.of(playerArg, setsArg);
    }

    @Override
    public Component execute(CommandContext context) {
        final FormatProvider fmt = context.getFormat();

        Set<PronounSet> setList;
        try {
            setList = context.getArgumentValue(setsArg);
            Objects.requireNonNull(setList); // required arg - this is safe
        } catch (IllegalArgumentException e) {
            return fmt.getPrefix()
                    .append(fmt.formatMain("The pronoun '"))
                    .append(fmt.formatAccent(e.getMessage()))
                    .append(fmt.formatMain("' is unrecognised.\n"
                            + "To use it, just write it out like it's shown in /pronouns list."));
        }

        SquirtgunPlayer target = context.getArgumentValue(playerArg);
        Objects.requireNonNull(target); // required arg - this is safe

        pl.getPronounHandler().setUserPronouns(target.getUuid(), setList);
        return fmt.getPrefix()
                .append(fmt.formatMain("Set " + target.getUsername() + "'s pronouns to "))
                .append(fmt.formatAccent(PronounSet.friendlyPrintSet(setList)));
    }
}
