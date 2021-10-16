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
import net.lucypoulton.pronouns.ProNouns;
import net.lucypoulton.pronouns.api.PronounHandler;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.pronouns.command.arguments.PronounSetArgument;
import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SetPronounsNode extends AbstractNode<SquirtgunPlayer> {
    private final ProNouns pl;
    private final PronounSetArgument sets;

    public SetPronounsNode(ProNouns plugin) {
        super("set", "Sets your pronouns.", Condition.isPlayer());
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

        SquirtgunPlayer player = (SquirtgunPlayer) context.getTarget();

        if (!checkInput(context.getRaw(), player)) {
            return fmt.getPrefix().append(fmt.formatMain("You can't use that set."));
        }
        Set<PronounHandler.ParseResult> setList = context.getArgumentValue(sets);
        Set<PronounSet> newSets = new LinkedHashSet<>();
        Component errorMessages = Component.empty();

        // required argument - this is safe
        for (PronounHandler.ParseResult results : Objects.requireNonNull(setList)) {
            if (results.success()) {
                newSets.addAll(results.results());
            }
            if (!results.ambiguities().isEmpty()) {
                for (Set<PronounSet> ambiguity : results.ambiguities()) {
                    errorMessages = errorMessages.append(
                        fmt.formatMain("Ambiguous set detected, assuming you meant ")
                            .append(fmt.formatAccent(ambiguity.stream().findFirst().orElseThrow().toString()))
                            .append(fmt.formatMain("."))
                            .append(Component.newline())
                    );
                }
            }
            // todo error message for failed sets
        }

        pl.getPronounHandler().setUserPronouns(player, newSets);
        return errorMessages.append(fmt.getPrefix()
            .append(fmt.formatMain("Set pronouns to "))
            .append(fmt.formatAccent(PronounSet.format(newSets)))
        );
    }
}
