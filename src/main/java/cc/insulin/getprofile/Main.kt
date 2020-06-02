package cc.insulin.getprofile

import cc.insulin.getprofile.command.ConvertCommand
import cc.insulin.getprofile.command.PrintCommand
import picocli.CommandLine
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val exitCode = CommandLine(MainCommand())
            .execute(*args)
    exitProcess(exitCode)
}

@CommandLine.Command(name = "getprofile", subcommands = [PrintCommand::class, ConvertCommand::class])
class MainCommand