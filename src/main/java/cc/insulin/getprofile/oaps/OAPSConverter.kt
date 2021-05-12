package cc.insulin.getprofile.oaps

import cc.insulin.getprofile.data.GlucoseUnits
import cc.insulin.getprofile.nightscout.data.NSProfile
import cc.insulin.getprofile.nightscout.data.profile.ScheduleEntry
import cc.insulin.getprofile.oaps.data.OAPSProfile
import cc.insulin.getprofile.oaps.data.profile.*
import org.apache.logging.log4j.kotlin.Logging

object OAPSConverter : Logging {

    // autotune requires the profile to be in mg/dl, so this converts the units for usage in it
    private fun mmolConvert(input: Double, units: GlucoseUnits, shouldConvert: Boolean = true): Double {
        if (!shouldConvert || units == GlucoseUnits.MGDL) return input

        return input * 18
    }

    fun convertBasal(scheduleEntry: ScheduleEntry): BasalEntry {
        val rate = scheduleEntry.value.toDouble()
        val minutes = scheduleEntry.timeAsSeconds / 60

        val entry = BasalEntry(minutes, rate)
        logger.debug("Converted basal entry: $scheduleEntry -> $entry")
        return entry
    }

    fun convertBasals(entries: List<ScheduleEntry>): List<BasalEntry> {
        val basalEntries = mutableListOf<BasalEntry>()

        entries.forEach { entry ->
            basalEntries.add(convertBasal(entry))
        }

        logger.debug("Basal entries: $basalEntries")
        return basalEntries
    }

    // todo: support multiple sensitivity times
    fun convertSensitivity(entries: List<ScheduleEntry>, units: GlucoseUnits, convertMmol: Boolean): List<SensitivityEntry> {
        val sensitivityEntries = mutableListOf<SensitivityEntry>()
        val nsEntry = entries[0]
        val sens = mmolConvert(nsEntry.value.toDouble(), units, convertMmol)
        val newEntry = SensitivityEntry(0, sens, 0, 0, 1440)
        sensitivityEntries.add(newEntry)

        logger.debug("ISF entries: $sensitivityEntries")
        return sensitivityEntries
    }

    fun convertTargets(
            lowTargets: List<ScheduleEntry>,
            highTargets: List<ScheduleEntry>,
            units: GlucoseUnits,
            convertMmol: Boolean
    ): BgTargets {
        val targets = lowTargets.associate { entry ->
            val lowBg = mmolConvert(entry.value.toDouble(), units, convertMmol)
            val highBg = mmolConvert(highTargets.first { it.time == entry.time }.value.toDouble(), units, convertMmol)

            entry.time + ":00" to (highBg to lowBg)
        }.map {
            BgTargets.Target(
                    maxBg = it.value.first,
                    minBg = it.value.second,
                    start = it.key
            )
        }

        val displayedUnits = if (convertMmol && units == GlucoseUnits.MMOL) GlucoseUnits.MGDL else units

        return BgTargets(targets = targets, units = displayedUnits, userPreferredUnits = units)
    }

    fun convertCarbRatios(ratios: List<ScheduleEntry>): List<CarbRatios.ScheduleEntry> {
        return ratios.map {
            CarbRatios.ScheduleEntry(it.value.toDouble(), it.time + ":00")
        }
    }

    fun convertProfile(nsProfile: NSProfile, convertMmol: Boolean = false): OAPSProfile {
        val displayedUnits = if (convertMmol) GlucoseUnits.MGDL else nsProfile.units

        val basal = convertBasals(nsProfile.basal)
        val sens = convertSensitivity(nsProfile.sens, nsProfile.units, convertMmol)
        val isfProfile = ISFProfile(sens, displayedUnits, nsProfile.units)
        // todo: support custom insulin curves and peak times
        val targets = convertTargets(nsProfile.targetLow, nsProfile.targetHigh, nsProfile.units, convertMmol)
        val carbRatio = nsProfile.carbRatio[0].value.toDouble()
        val carbRatios = CarbRatios(convertCarbRatios(nsProfile.carbRatio))

        val oapsProfile = OAPSProfile(
                min5mCarbImpact = 8.0,
                dia = nsProfile.dia.toDouble(),
                basalProfile = basal,
                isfProfile = isfProfile,
                bgTargets = targets,
                carbRatios = carbRatios,
                carbRatio = carbRatio,
                outUnits = nsProfile.units,
                timezone = nsProfile.timezone
        )
        logger.debug("OpenAPS-converted profile: $oapsProfile")
        return oapsProfile
    }
}