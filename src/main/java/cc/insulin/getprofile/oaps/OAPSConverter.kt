package cc.insulin.getprofile.oaps

import cc.insulin.getprofile.data.GlucoseUnits
import cc.insulin.getprofile.nightscout.data.NSProfile
import cc.insulin.getprofile.nightscout.data.profile.ScheduleEntry
import cc.insulin.getprofile.oaps.data.OAPSProfile
import cc.insulin.getprofile.oaps.data.profile.BasalEntry
import cc.insulin.getprofile.oaps.data.profile.ISFProfile
import cc.insulin.getprofile.oaps.data.profile.SensitivityEntry
import org.apache.logging.log4j.kotlin.Logging

object OAPSConverter : Logging {

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

    // todo: allow not converting units
    // todo: support multiple sensitivity times
    fun convertSensitivity(entries: List<ScheduleEntry>, units: GlucoseUnits): List<SensitivityEntry> {
        val sensitivityEntries = mutableListOf<SensitivityEntry>()
        val nsEntry = entries[0]
        var sens = nsEntry.value.toDouble()
        if (units == GlucoseUnits.MMOL) {
            // autotune requires the profile to be in mg/dl, so we must convert the units
            sens *= 18
        }
        val newEntry = SensitivityEntry(0, sens, 0, 0, 1440)
        sensitivityEntries.add(newEntry)

        logger.debug("ISF entries: $sensitivityEntries")
        return sensitivityEntries
    }

    fun convertProfile(nsProfile: NSProfile): OAPSProfile {
        val basal = convertBasals(nsProfile.basal)
        val sens = convertSensitivity(nsProfile.sens, nsProfile.units)
        val isfProfile = ISFProfile(sens)
        // todo: support carb schedules
        // todo: support custom curves
        val carbRatio = nsProfile.carbRatio[0].value.toDouble()

        val oapsProfile = OAPSProfile(8.0, nsProfile.dia.toDouble(), basal, isfProfile, carbRatio)
        logger.debug("OpenAPS-converted profile: $oapsProfile")
        return oapsProfile
    }
}