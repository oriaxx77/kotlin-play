package com.oriaxx77.kotlinplay.arrow.io

import arrow.core.Either
import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.extensions.io.monad.flatten
import java.security.SecureRandom
import kotlin.random.Random.Default.nextInt


private val random = SecureRandom()

fun main(args: Array<String>) {
    mainIO(args).unsafeRunSync()
}

private fun mainIO(args: Array<String>): IO<Unit> = IO.fx {
    putStrLn("What is your name?").bind()
    val name = getStrLn().bind()
    putStrLn("Hello, $name, welcome to the game!").bind()
    gameLoop(name).bind()
}

private fun gameLoop(name: String?): IO<Unit> = IO.fx {
    putStrLn("Dear $name, please guess a number from 1 to 5:").bind()
    getStrLn().safeToInt2()
            .map {
                val (number) = nextIOInt().map { it +1 }.bind()
            }
            .handleError { putStrLn("You did not enter a number!") }

    getStrLn().safeToInt().fold(
            { putStrLn("You did not enter a number!").bind() },
            {
                val number = nextIOInt(5).map { it + 1 }.bind()
                if (it.bind() == number) putStrLn("You guessed right, $name!").bind()
                else putStrLn("You guessed wrong, $name! The number was $number").bind()
            }
    )
    checkContinue(name).map {
        (if (it) gameLoop(name)
        else IO.just(Unit))
    }.flatten()
            .bind()
}



private fun checkContinue(name: String?): IO<Boolean> = IO.fx {
    putStrLn("Do you want to continue(y/n), $name?").bind()
    (getStrLn()).map { it.toLowerCase() }.map {
        when (it) {
            "y" -> IO.just(true)
            "n" -> IO.just(false)
            else -> checkContinue(name)
        }
    }.flatten()
            .bind()
}

private fun putStrLn(line: String): IO<Unit> = IO { println(line) }
private fun getStrLn(): IO<String> = IO { readLine() as String }

private fun nextIOInt(upper: Int): IO<Int> = IO { random.nextInt(upper) }

private fun IO<String>.safeToInt2() = this.map { it.toInt() }

private fun IO<String>.safeToInt() = Try { map { it.toInt() }}