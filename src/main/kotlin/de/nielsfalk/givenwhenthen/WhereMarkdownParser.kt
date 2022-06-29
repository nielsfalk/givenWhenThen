@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package de.nielsfalk.givenwhenthen

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.intellij.lang.annotations.Language
import java.lang.Boolean
import java.lang.Number

inline fun <reified T> where(@Language("Markdown") markdown: String): Array<T> =
    readMd<T>(markdown).toTypedArray()

inline fun <reified T> readMd(markdown: String): List<T> {
    val parameters = T::class.java.constructors.first().parameters
    val kParameters = T::class.constructors.first().parameters
    return markdown.split('\n')
        .drop(2)
        .map { line ->
            line
                .split('|')
                .drop(1)
                .dropLast(1)
                .map { it.trim() }
        }.map { line ->
            line.mapIndexed { index, cell ->
                """"${kParameters[index].name}":${cellToJsonValue(cell, parameters[index].type)}"""

            }.joinToString(prefix = "{", postfix = "}")
        }.map {
            mapper.readValue(it)
        }
}

fun cellToJsonValue(cell: String, type: Class<*>): String {
    val trimmed = cell.trim()
    return when {
        trimmed == "null" ||
                Number::class.java.isAssignableFrom(type)
                || Boolean::class.java.isAssignableFrom(type) -> trimmed
        else -> """"$trimmed""""
    }
}

val mapper: ObjectMapper = ObjectMapper().apply {
    enable(SerializationFeature.INDENT_OUTPUT)
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    registerModule(JavaTimeModule())
    registerModule(KotlinModule.Builder().build())
}