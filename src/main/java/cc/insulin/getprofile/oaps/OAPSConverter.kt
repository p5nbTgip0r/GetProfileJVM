package cc.insulin.getprofile.oaps

import cc.insulin.getprofile.nightscout.data.profile.ScheduleEntry
import cc.insulin.getprofile.oaps.data.profile.BasalEntry

object OAPSConverter {
    fun convertBasal(scheduleEntry: ScheduleEntry): BasalEntry {
        val rate = scheduleEntry.value.toDouble()
        val minutes = scheduleEntry.timeAsSeconds / 60

        val entry = BasalEntry(minutes, rate)
        println(entry)
        return entry
    }

    fun convertBasals(entries: List<ScheduleEntry>): List<BasalEntry> {
        val basalEntries = mutableListOf<BasalEntry>()

        entries.forEach { entry ->
            basalEntries.add(convertBasal(entry))
        }

        println(basalEntries)
        return basalEntries
    }
}