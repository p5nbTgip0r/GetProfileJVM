package cc.insulin.getprofile

import cc.insulin.getprofile.command.ConvertCommand
import cc.insulin.getprofile.command.PrintCommand
import org.apache.logging.log4j.kotlin.Logging
import picocli.CommandLine
import kotlin.system.exitProcess

object Main : Logging {
    @JvmStatic
    fun main(args: Array<String>) {
        logger.trace("Starting up..")
        val exitCode = CommandLine(MainCommand())
            .execute(*args)
        exitProcess(exitCode)
    }
}

@CommandLine.Command(name = "getprofile", subcommands = [PrintCommand::class, ConvertCommand::class])
class MainCommand