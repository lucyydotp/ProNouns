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
import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.argument.OnlinePlayerArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.AbstractNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import me.lucyy.squirtgun.platform.audience.SquirtgunPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShowPronounsNode extends AbstractNode<PermissionHolder> {
	private final PronounHandler handler;
	private final CommandArgument<SquirtgunPlayer> playerArgument;

	public ShowPronounsNode(Platform platform, PronounHandler handler) {
		super("show", "Shows your, or another player's, pronouns.", null);
		this.handler = handler;
		playerArgument = new OnlinePlayerArgument("player", "the player to get pronouns for",
				true, platform);
	}

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return ImmutableList.of(playerArgument);
	}

	@Override
	public Component execute(CommandContext<PermissionHolder> context) {
		final FormatProvider fmt = context.getFormat();

		SquirtgunPlayer commandTarget = context.getArgumentValue(playerArgument);

		if (commandTarget == null) {
			if (!(context.getTarget() instanceof SquirtgunPlayer)) {
				return fmt.getPrefix().append(fmt.formatMain("That player could not be found."));
			}
			commandTarget = (SquirtgunPlayer) context.getTarget();
		}

		return fmt.getPrefix()
				.append(fmt.formatMain(commandTarget.getUsername() + "'s pronouns are "))
				.append(fmt.formatAccent(
						PronounSet.friendlyPrintSet(
								handler.getPronouns(commandTarget.getUuid()))
						)
				);
	}
}
