package dev.anilbeesetti.nextplayer.core.datastore.serializer

import java.io.ByteArrayInputStream
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerPreferencesSerializerTest {
    @Test
    fun `long press speed is clamped to supported range`() = runBlocking {
        val belowRange = PlayerPreferencesSerializer.readFrom(
            ByteArrayInputStream("""{"longPressControlsSpeed":0.5}""".toByteArray()),
        )
        val aboveRange = PlayerPreferencesSerializer.readFrom(
            ByteArrayInputStream("""{"longPressControlsSpeed":9.0}""".toByteArray()),
        )

        assertEquals(1f, belowRange.longPressControlsSpeed)
        assertEquals(8f, aboveRange.longPressControlsSpeed)
    }
}
