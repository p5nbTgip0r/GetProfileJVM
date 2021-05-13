package cc.insulin.getprofile.oaps

import cc.insulin.getprofile.data.GlucoseUnits
import cc.insulin.getprofile.nightscout.data.profile.ScheduleEntry
import cc.insulin.getprofile.oaps.data.profile.BgTargets
import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalTime
import java.time.format.DateTimeFormatter

internal class OAPSConverterTest : Logging {

    @ParameterizedTest(name = "convert bg target(s) from {2}")
    @MethodSource("targetArguments")
    fun `convert bg targets`(
        converter: OAPSConverter,
        expected: BgTargets,
        vararg targets: Triple<LocalTime, Number, Number>,
    ) {
        logger.info("Converting using $converter")
        logger.info("Expecting targets to equal $expected")
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
            // mg/dL
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


            // mmol/L (convert)
            Arguments.of(
                OAPSConverter(GlucoseUnits.MMOL),
                buildTargets(
                    units = GlucoseUnits.MGDL,
                    preferredUnits = GlucoseUnits.MMOL,
                    target = arrayOf(Triple("00:00:00", 99, 99))
                ),

                arrayOf(Triple(LocalTime.of(0, 0), 5.5, 5.5))
            ),

            Arguments.of(
                OAPSConverter(GlucoseUnits.MMOL),
                buildTargets(
                    units = GlucoseUnits.MGDL,
                    preferredUnits = GlucoseUnits.MMOL,
                    target = arrayOf(
                        Triple("00:00:00", 99, 99),
                        Triple("18:00:00", 121, 121)
                    )
                ),

                arrayOf(
                    Triple(LocalTime.of(0, 0), 5.5, 5.5),
                    Triple(LocalTime.of(18, 0), 6.7, 6.7),
                )
            ),

            Arguments.of(
                OAPSConverter(GlucoseUnits.MMOL),
                buildTargets(
                    units = GlucoseUnits.MGDL,
                    preferredUnits = GlucoseUnits.MMOL,
                    target = arrayOf(
                        Triple("00:00:00", 99, 121),
                        Triple("18:00:00", 121, 140)
                    ),
                ),

                arrayOf(
                    Triple(LocalTime.of(0, 0), 5.5, 6.7),
                    Triple(LocalTime.of(18, 0), 6.7, 7.8),
                )
            ),
        )

        private fun buildTargets(
            units: GlucoseUnits,
            vararg target: Triple<String, Number, Number>,
            preferredUnits: GlucoseUnits = units,
        ): BgTargets {
            return BgTargets(
                targets = target.map {
                    BgTargets.Target(
                        start = it.first,
                        minBg = it.second,
                        maxBg = it.third,
                    )
                },
                units = units,
                userPreferredUnits = preferredUnits
            )
        }
    }
}