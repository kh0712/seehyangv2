package kr.mashup.seehyangcore.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun LocalDate.atEndOfDay() :LocalDateTime = LocalDateTime.of(this, LocalTime.MAX)