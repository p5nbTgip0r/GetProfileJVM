package cc.insulin.getprofile

import picocli.CommandLine
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val exitCode = CommandLine(MainCommand())
            .execute(*args)
    exitProcess(exitCode)
}

@CommandLine.Command(name = "getprofile", subcommands = [PrintCommand::class])
class MainCommand