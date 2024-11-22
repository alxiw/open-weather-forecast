package io.github.alxiw.openweatherforecast.data.model.util

internal fun formatString(input: String) = input.trim().lowercase().replace("\\s+".toRegex(), "_")
