package net.lucypoulton.pronouns.cloud

import com.google.common.collect.MultimapBuilder
import com.google.common.collect.SetMultimap
import net.lucypoulton.pronouns.api.set.PronounSet

enum class PronounTier(val friendlyName: String) {
    COMMON_SETS("Common sets"),
    ALL_SETS("All sets"),
    EVERYTHING("Everything");

}

class PronounsListHandler {

    // TODO - make this persistent.
    @Suppress("UnstableApiUsage")
    val sets: SetMultimap<PronounTier, PronounSet> = MultimapBuilder.hashKeys().hashSetValues().build()

    private val modQueue = mutableListOf<Pair<PronounSet, String>>()
    private val queueHandlers = mutableSetOf<(PronounSet, String) -> Unit>()

    fun addHandler(handler: (PronounSet, String) -> Unit) {
        queueHandlers.add(handler)
    }

    fun acceptSet(set: PronounSet, tier: PronounTier) {
        modQueue.removeAll { it.first == set }
        sets.put(tier, set)
    }

    fun isInQueue(set: PronounSet): Boolean {
        return modQueue.any { it.first == set }
    }

    fun addToQueue(set: PronounSet, source: String) {
        if (modQueue.any { it.first == set } || sets.values().any { it == set }) return
        queueHandlers.forEach { it.invoke(set, source) }
        modQueue.add(set to source)
    }

    fun getSets(tier: PronounTier): Set<PronounSet> = sets.get(tier)
}