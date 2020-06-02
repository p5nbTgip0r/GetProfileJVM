package cc.insulin.getprofile

import picocli.CommandLine

class Options {
    @CommandLine.Option(required = true,
            names = ["-u", "--nightscout"],
            description = ["Nightscout URL"],
            paramLabel = "URL")
    lateinit var nsUrl: String

    @CommandLine.Option(names = ["-t", "--token", "--auth"],
            description = ["API secret or token for Nightscout authentication"],
            paramLabel = "token/secret",
            arity = "0..1",
            interactive = true)
    var auth: String? = null
}