package cc.insulin.getprofile.command

import cc.insulin.getprofile.nightscout.data.Nightscout
import picocli.CommandLine
import java.io.File

class Options {
    @CommandLine.Option(
            names = ["-m", "--convert-mmol"],
            description = ["Convert glucose values from mmol/L to mg/dL in the output"]
    )
    var convertMmol: Boolean = false

    @CommandLine.Option(
            names = ["-f", "-o", "--file", "--output"],
            description = ["Output file"],
            paramLabel = "FILE"
    )
    var outputFile: File? = null

    @CommandLine.Option(
            required = true,
            names = ["-u", "--nightscout"],
            description = ["Nightscout URL"],
            paramLabel = "URL"
    )
    lateinit var nsUrl: String

    @CommandLine.Option(
            names = ["-t", "--token", "--auth"],
            description = ["API secret or token for Nightscout authentication"],
            paramLabel = "token/secret",
            arity = "0..1",
            interactive = true
    )
    var auth: String? = null

    val nightscout: Nightscout
        get() {
            return Nightscout(nsUrl, auth)
        }
}