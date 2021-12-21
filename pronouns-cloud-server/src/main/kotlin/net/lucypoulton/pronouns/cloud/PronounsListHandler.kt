package net.lucypoulton.pronouns.cloud

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.lucypoulton.pronouns.api.set.PronounSet
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.reactivestreams.KMongo

enum class PronounTier(val friendlyName: String) {
    COMMON_SETS("Common sets"),
    ALL_SETS("All sets"),
    EVERYTHING("Everything");
}

@Serializable
data class PronounEntry(val set: @Serializable(PronounSerializer::class) PronounSet, val tier: PronounTier)

@Serializable
data class ModerationQueueEntry(val set: @Serializable(PronounSerializer::class) PronounSet, val source: String)

class PronounSerializer : KSerializer<PronounSet> {
    override fun deserialize(decoder: Decoder): PronounSet = PronounSet.parse(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: PronounSet) = encoder.encodeString(value.toString())

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PronounSet", PrimitiveKind.STRING)
}

class PronounsListHandler {

    private val client = KMongo.createClient().coroutine
    private val database = client.getDatabase("pronouns")
    private val entries = database.getCollection<PronounEntry>("pronouns")
    private val modQueue = database.getCollection<ModerationQueueEntry>("queue")

    private val queueHandlers = mutableSetOf<(PronounSet, String) -> Unit>()

    fun addHandler(handler: (PronounSet, String) -> Unit) {
        queueHandlers.add(handler)
    }

    suspend fun rejectSet(set: PronounSet): Boolean =
        modQueue.deleteMany(ModerationQueueEntry::set eq set).deletedCount == 0L


    suspend fun acceptSet(set: PronounSet, tier: PronounTier) {
        entries.insertOne(PronounEntry(set, tier))
        rejectSet(set)
    }

    suspend fun isInQueue(set: PronounSet): Boolean = modQueue
        .find("{'set':'${set.toString().replace("\'", "\\\'")}'}")
        .first() != null


    suspend fun addToQueue(set: PronounSet, source: String) {
        if (isInQueue(set)) return
        modQueue.insertOne(ModerationQueueEntry(set, source))
        queueHandlers.forEach { it.invoke(set, source) }
    }

    suspend fun getSets(tier: PronounTier): Set<PronounSet> {
        val filter: Bson? = when (tier) {
            PronounTier.EVERYTHING -> null
            PronounTier.COMMON_SETS -> PronounEntry::tier eq PronounTier.COMMON_SETS
            PronounTier.ALL_SETS -> PronounEntry::tier ne PronounTier.EVERYTHING
        }

        return entries.find(filter).toList().map { it.set }.toSet()
    }
}