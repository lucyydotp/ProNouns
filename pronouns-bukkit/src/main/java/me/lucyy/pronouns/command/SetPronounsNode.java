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
import jdk.javadoc.internal.doclets.toolkit.taglets.UserTaglet;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.command.arguments.PronounSetArgument;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.Player;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SetPronounsNode implements CommandNode<CommandSender> {
    private final ProNouns pl;
    private final PronounSetArgument sets;

    public SetPronounsNode(ProNouns plugin) {
        pl = plugin;
        sets = new PronounSetArgument(pl.getPronounHandler());
    }

    @Override
    public @NotNull String getName() {
        return "set";
    }

	@Override
	public @Nullable String getPermission() {
    	return null;
    }

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return ImmutableList.of(sets);
	}

	private void warnAdmins(String player, String content) {
        final ConfigHandler cfg = pl.getConfigHandler();
        Bukkit.broadcast(cfg.getPrefix().append(cfg.formatMain("Player "))
                        .append(cfg.formatAccent(player))
                        .append(cfg.formatMain(" tried to use prohibited pronoun set "))
                        .append(cfg.formatAccent(content)),
                "pronouns.admin");
    }

    private boolean checkInput(String arg, CommandSender sender) {
        final ConfigHandler cfg = pl.getConfigHandler();
        if (!cfg.filterEnabled()) return true;
        if (!sender.hasPermission("pronouns.bypass"))
            for (String pattern : cfg.getFilterPatterns()) {
                if (arg.toLowerCase().matches(".*" + pattern + ".*")) {
                    warnAdmins(sender.getName(), arg);
                    return false;
                }
            }
        return true;
    }

    @Override
    public Component execute(CommandContext<CommandSender> context) {
        final ConfigHandler cfg = pl.getConfigHandler();
        if (!(context.getTarget() instanceof Player)) {
            return cfg.getPrefix().append(cfg.formatMain("This command can only be run by a player."));
        }

        if (!checkInput(context.getRaw(), context.getTarget()))  {
	        return cfg.getPrefix().append(cfg.formatMain("You can't use that set."));
        }
        Set<PronounSet> setList;
        try {
	        setList = context.getArgumentValue(sets);
        } catch (IllegalArgumentException e) {
            return cfg.getPrefix()
                    .append(cfg.formatMain("The pronoun '"))
                    .append(cfg.formatAccent(e.getMessage()))
                    .append(cfg.formatMain("' is unrecognised.\n"
                            + "To use it, just write it out like it's shown in /pronouns list."));
        }

        if (setList == null || setList.size() == 0) {
        	return Component.text("usage"); // TODO
        }

        pl.getPronounHandler().setUserPronouns(((Player) context.getTarget()).getUuid(), setList);
        return cfg.getPrefix()
                .append(cfg.formatMain("Set pronouns to "))
                .append(cfg.formatAccent(PronounSet.friendlyPrintSet(setList)));
    }
}
