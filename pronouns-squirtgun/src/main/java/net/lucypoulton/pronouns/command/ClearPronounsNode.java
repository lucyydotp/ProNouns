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

import net.lucypoulton.pronouns.api.PronounHandler;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.kyori.adventure.text.Component;

public class ClearPronounsNode extends AbstractNode<SquirtgunPlayer> {
	private final PronounHandler pronounHandler;

	public ClearPronounsNode(PronounHandler pronounHandler) {
		super("clear", "Clears your pronouns.", Condition.isPlayer());
		this.pronounHandler = pronounHandler;
	}

	@Override
	public Component execute(CommandContext context) {
		final FormatProvider fmt = context.getFormat();


		pronounHandler.clearUserPronouns((SquirtgunPlayer) context.getTarget());
		return fmt.getPrefix().append(fmt.formatMain("Cleared pronouns"));
	}
}
