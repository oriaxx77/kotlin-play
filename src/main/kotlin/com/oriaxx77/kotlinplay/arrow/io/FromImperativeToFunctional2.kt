package com.oriaxx77.kotlinplay.arrow.io

import arrow.fx.IO
import arrow.fx.extensions.fx

fun main(args: Array<String>)
{
    mainIO().unsafeRunAsync()
}

private fun mainIO(): IO<Unit> = IO.fx {
    putStrLine("What is your name?").bind()
    val name = getStrLine().bind()
    putStrLn("Hello, $name, welcome to the game!").bind()


}

private fun putStrLine(line: String): IO<Unit> = IO { println(line) }
private fun getStrLine(): IO<String> = IO { readLine() as String }


private fun gampeLoop(): IO<Unit> =  IO.fx {

}