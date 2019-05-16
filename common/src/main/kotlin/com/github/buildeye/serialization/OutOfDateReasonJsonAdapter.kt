package com.github.buildeye.serialization

import com.github.buildeye.infos.Change
import com.github.buildeye.infos.InputsChange
import com.github.buildeye.infos.OutOfDateReason
import com.github.buildeye.infos.OutOfDateReason.Reason
import com.github.buildeye.infos.Unknown
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class OutOfDateReasonJsonAdapter : JsonSerializer<OutOfDateReason>, JsonDeserializer<OutOfDateReason> {
    override fun serialize(src: OutOfDateReason, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonObject().apply {
            add("reason", context.serialize(src.reason))

            if (src.reason == Reason.INPUTS_CHANGE) {
                add("changes", context.serialize((src as InputsChange).changes))
            }
        }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): OutOfDateReason {
        val reasonJson = json.asJsonObject.get("reason")
        val reason = context.deserialize<Reason>(reasonJson, Reason::class.java)

        return when (reason) {
            Reason.UNKNOWN -> Unknown()
            Reason.INPUTS_CHANGE -> {
                val changes = context.deserialize<Collection<Change>>(
                        json.asJsonObject.get("changes"),
                        object : TypeToken<Collection<Change>>() {}.type
                )
                InputsChange(changes)
            }
            else -> throw JsonParseException("Cannot deserialize OutOfDateReason")
        }
    }
}