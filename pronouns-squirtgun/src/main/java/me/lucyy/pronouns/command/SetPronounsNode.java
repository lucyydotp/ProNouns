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

import com.google.common.collect.ImmutableList;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.command.arguments.PronounSetArgument;
import me.lucyy.pronouns.config.ConfigHandler;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SetPronounsNode extends AbstractNode<PermissionHolder> {
    private final ProNouns pl;
    private final PronounSetArgument sets;

    public SetPronounsNode(ProNouns plugin) {
        super("set", "Sets your pronouns.", Condition.alwaysTrue());
        pl = plugin;
        sets = new PronounSetArgument(pl.getPronounHandler());
    }

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return ImmutableList.of(sets);
	}

	private void warnAdmins(String player, String content) {
        final ConfigHandler cfg = pl.getConfigHandler();
        final Component message = cfg.getPrefix().append(cfg.formatMain("Player "))
                        .append(cfg.formatAccent(player))
                        .append(cfg.formatMain(" tried to use prohibited pronoun set "))
                        .append(cfg.formatAccent(content));

        pl.getPlatform().getConsole().sendMessage(message);
        for (SquirtgunPlayer sgPlayer : pl.getPlatform().getOnlinePlayers()) {
            if (sgPlayer.hasPermission("pronouns.admin")) {
                sgPlayer.sendMessage(message);
            }
        }
    }

    private boolean checkInput(String arg, SquirtgunPlayer sender) {
        final ConfigHandler cfg = pl.getConfigHandler();
        if (!cfg.filterEnabled()) return true;
        if (!sender.hasPermission("pronouns.bypass"))
            for (String pattern : cfg.getFilterPatterns()) {
                if (arg.toLowerCase().matches(".*" + pattern + ".*")) {
                    warnAdmins(sender.getUsername(), arg);
                    return false;
                }
            }
        return true;
    }

    @Override
    public Component execute(CommandContext context) {
        final FormatProvider fmt = context.getFormat();
        if (!(context.getTarget() instanceof SquirtgunPlayer)) {
            return fmt.getPrefix().append(fmt.formatMain("This command can only be run by a player."));
        }

        SquirtgunPlayer player = (SquirtgunPlayer) context.getTarget();

        if (!checkInput(context.getRaw(), player))  {
	        return fmt.getPrefix().append(fmt.formatMain("You can't use that set."));
        }
        Set<PronounSet> setList;
        try {
	        setList = context.getArgumentValue(sets);
	        Objects.requireNonNull(setList); // required arg - this is safe
        } catch (IllegalArgumentException e) {
            return fmt.getPrefix()
                    .append(fmt.formatMain("The pronoun '"))
                    .append(fmt.formatAccent(e.getMessage()))
                    .append(fmt.formatMain("' is unrecognised.\n"
                            + "To use it, just write it out like it's shown in /pronouns list."));
        }

        pl.getPronounHandler().setUserPronouns(player.getUuid(), setList);
        return fmt.getPrefix()
                .append(fmt.formatMain("Set pronouns to "))
                .append(fmt.formatAccent(PronounSet.friendlyPrintSet(setList)));
    }
}
