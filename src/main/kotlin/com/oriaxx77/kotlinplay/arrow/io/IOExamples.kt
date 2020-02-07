package com.oriaxx77.kotlinplay.arrow.io

import arrow.core.Either
import arrow.fx.IO
import arrow.fx.extensions.io.effect.runAsync

// Note: do not use unsafe run sync ever

fun attempt() {
    val attempt = IO<Int> { throw RuntimeException() }
        .attempt()
        .unsafeRunSync()
        .fol


//        .runAsync { result ->
//            result.fold({ IO { println("Error") } }, { IO { println(it.toString()) } }) }
//


}

fun main(args: Array<String>) {
    println("Hello World!")
    attempt()
}