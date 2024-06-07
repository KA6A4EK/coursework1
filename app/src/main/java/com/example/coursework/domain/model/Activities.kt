package com.example.coursework.domain.model

import com.example.coursework.R

enum class Activities(val id: Int, val title: Int, val icon: Int) {
    Skiing(1,  R.string.skiing, R.drawable.downhill_skiing_24px),
    Exercise(2, R.string.exercise, R.drawable.exercise_24px),
    Hiking(3, R.string.hiking, R.drawable.hiking_24px),
    Rowing(4, R.string.rowing, R.drawable.rowing_24px),
    SkateBoarding(5, R.string.skateboarding, R.drawable.skateboarding_24px),
    Snowboarding(6, R.string.snowboarding, R.drawable.snowboarding_24px),
    Snowshoeing(7, R.string.snowshoeing, R.drawable.snowshoeing_24px),
    Baseball(8, R.string.baseball, R.drawable.sports_baseball_24px),
    Basketball(9, R.string.basketball, R.drawable.sports_basketball_24px),
    Cricket(10, R.string.cricket, R.drawable.sports_cricket_24px),
    Golf(11, R.string.golf, R.drawable.sports_golf_24px),
    Gymnastic(12,R.string.gymnastic, R.drawable.sports_gymnastics_24px),
    Handball(13, R.string.handball, R.drawable.sports_handball_24px),
    Kabaddi(14, R.string.kabaddi, R.drawable.sports_kabaddi_24px),
    Martial(15, R.string.martial, R.drawable.sports_martial_arts_24px),
    Rugby(16, R.string.rugby, R.drawable.sports_rugby_24px),
    Soccer(17, R.string.soccer, R.drawable.sports_soccer_24px),
    Volleyball(18, R.string.volleyball, R.drawable.sports_volleyball_24px),
    Sprint(19, R.string.sprint, R.drawable.sprint_24px),
    Surfing(20, R.string.surfing, R.drawable.surfing_24px)
}

data class CustomActivity(
    val icon: Int,
    val title: String,
)