package net.lucypoulton.pronouns.cloud

import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.ButtonStyle
import net.lucypoulton.pronouns.api.set.PronounSet
import java.time.LocalDateTime

class Discord(private val config: Config, private val list: PronounsListHandler) {
    val instance: JDA = JDABuilder.createDefault(config.botToken).build()

    // TODO
    lateinit var channel: TextChannel

    private fun sendSetForApproval(set: PronounSet, source: String) {
        channel.sendMessage(
            EmbedBuilder()
                .setTitle("New set submitted")
                .addField("Set", set.toString(), false)
                .addField("Source", source, false)
                .setTimestamp(LocalDateTime.now())
                .build()
        ).setActionRow(
            Button.of(ButtonStyle.DANGER, "reject!$set", "Reject"),
            *PronounTier.values().map { t -> Button.of(ButtonStyle.PRIMARY, "${t.name}!$set", t.friendlyName) }
                .toTypedArray()
        ).queue()
    }

    init {
        list.addHandler(this::sendSetForApproval)

        instance.addEventListener(object : ListenerAdapter() {
            override fun onReady(event: ReadyEvent) {
                channel = instance.getTextChannelById(config.channelId)!!
                instance.presence.activity = Activity.playing("with Lucy's sanity")
            }

            override fun onButtonClick(event: ButtonClickEvent) {
                // kotlin doesnt like it if you negate a nullable
                if (event.member?.hasPermission(Permission.ADMINISTRATOR) == false &&
                    event.member?.roles!!.any { it.idLong == config.roleId }
                ) {
                    event.reply("${event.user.name}, you can't use that.").queue()
                }
                event.message?.delete()

                runBlocking {
                    // it's a button click event, there's definitely a button
                    val (tierStr, set) = event.button!!.id!!.split('!')

                    val setParsed = PronounSet.parse(set)
                    if (list.isInQueue(setParsed)) {
                        event.message?.delete()?.queue()
                    }

                    val embedBuilder = EmbedBuilder()
                        .setTimestamp(LocalDateTime.now())
                        .addField("Set", set, false)
                        .addField("Done by", event.member?.effectiveName, true)

                    if (tierStr == "reject") {
                        if (list.rejectSet(setParsed)) embedBuilder.setTitle("Set rejected")
                    } else {
                        val tier: PronounTier = PronounTier.valueOf(tierStr)
                        embedBuilder.setTitle("Set accepted")
                        embedBuilder.addField("Tier", tier.friendlyName, true)
                        list.acceptSet(setParsed, tier)
                    }
                    event.replyEmbeds(embedBuilder.build()).queue()
                }
            }
        })
    }
}