package cc.insulin.getprofile.nightscout.data.profile

data class ScheduleEntry(val time: String,
                         val value: String,
                         val timeAsSeconds: Int)
