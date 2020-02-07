package com.oriaxx77.kotlinplay.arrow.io

import arrow.fx.typeclasses.Bracket


// https://arrow-kt.io/docs/arrow/effects/typeclasses/bracket/
// acquire - use - release resource
// can be use synch/asynch mode


import arrow.fx.IO
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files


class Connection(url: String) {
    fun open(): Connection = this
    fun close(): Unit {}
    override fun toString(): String = "Some interesting content"
}

fun openConnection(uri: String): IO<Connection> = IO { Connection(uri).open() }
fun closeConnection(connection: Connection): IO<Unit> = IO { connection.close() }
fun readConnection(connection: Connection): IO<String> = IO { connection.toString() }

typealias ConnectionUsage = (Connection) -> IO<String>


fun main(args: Array<String>) {
    val safeComputation = openConnection("http://x.y.z").bracket(
            release = { connection -> closeConnection(connection) },
            use = { connection -> readConnection(connection) })

    safeComputation.unsafeRunAsync { result ->
        result.fold({println("Error") }, { println(it.toString())})
    }


    IO { Connection("http://x.y.z").open() }
            .bracket(
                release = { connection -> IO { connection.close() } },
                use = {  connection -> IO { connection.toString() } } )
            .unsafeRunAsync { result ->
                result.fold({println("Error") }, { println(it.toString())})
            }

}