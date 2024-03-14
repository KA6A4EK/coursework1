package com.example.coursework.util

import java.time.LocalTime
import java.time.format.DateTimeFormatter

//функция парсит время из строки
fun parceTime(time: String) = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))