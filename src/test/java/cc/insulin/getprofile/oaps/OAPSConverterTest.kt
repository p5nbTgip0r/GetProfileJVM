package cc.insulin.getprofile.oaps

import cc.insulin.getprofile.data.GlucoseUnits
import cc.insulin.getprofile.nightscout.data.profile.ScheduleEntry
import cc.insulin.getprofile.oaps.data.profile.BgTargets
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalTime
import java.time.format.DateTimeFormatter

internal class OAPSConverterTest {

    @ParameterizedTest
    @MethodSource("targetArguments")
    fun `convert bg targets`(
        converter: OAPSConverter,
        expected: BgTargets,
        vararg targets: Triple<LocalTime, Number, Number>,
    ) {
        val low = mutableListOf<ScheduleEntry>()
        val high = mutableListOf<ScheduleEntry>()

        targets.forEach {
            val start = it.first.format(NIGHTSCOUT_TIME_FORMAT)
            val startSeconds = it.first.toSecondOfDay()
            low.add(ScheduleEntry(start, it.second.toString(), startSeconds))
            high.add(ScheduleEntry(start, it.third.toString(), startSeconds))
        }

        val output = converter.convertTargets(low, high)

        assertEquals(expected, output)
    }

    private companion object {
        private val OPENAPS_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss")
        private val NIGHTSCOUT_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm")

        @JvmStatic
        fun targetArguments() = listOf(
            Arguments.of(
                OAPSConverter(GlucoseUnits.MGDL),
                buildTargets(GlucoseUnits.MGDL, Triple("00:00:00", 100, 100)),

                arrayOf(Triple(LocalTime.of(0, 0), 100, 100))
            ),

            Arguments.of(
                OAPSConverter(GlucoseUnits.MGDL),
                buildTargets(
                    GlucoseUnits.MGDL,
                    Triple("00:00:00", 100, 100),
                    Triple("18:00:00", 120, 120)
                ),

                arrayOf(
                    Triple(LocalTime.of(0, 0), 100, 100),
                    Triple(LocalTime.of(18, 0), 120, 120),
                )
            ),

            Arguments.of(
                OAPSConverter(GlucoseUnits.MGDL),
                buildTargets(
                    GlucoseUnits.MGDL,
                    Triple("00:00:00", 100, 120),
                    Triple("18:00:00", 120, 140)
                ),

                arrayOf(
                    Triple(LocalTime.of(0, 0), 100, 120),
                    Triple(LocalTime.of(18, 0), 120, 140),
                )
            ),
        )

        private fun buildTargets(units: GlucoseUnits, vararg target: Triple<String, Number, Number>): BgTargets {
            return BgTargets(
                targets = target.map {
                    BgTargets.Target(
                        start = it.first,
                        minBg = it.second,
                        maxBg = it.third,
                    )
                },
                units = units
            )
        }
    }
}