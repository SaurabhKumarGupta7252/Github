package gupta.saurabh.github.util

import android.graphics.Color

object Constants {

    const val SPLASH_DURATION = 3000L

    const val BASE_URL = "https://api.github.com/"

    val languageColors = mapOf(

        "Kotlin" to "#A97BFF",

        "Java" to "#B07219",

        "Python" to "#3572A5",

        "JavaScript" to "#F1E05A",

        "C++" to "#F34B7D",

        "Swift" to "#FFAC45",

        "Go" to "#00ADD8",

        "Ruby" to "#701516",

        "Dart" to "#00B4AB"
    )

    const val defaultColor = "#000000"

    fun lightenColor(color: Int, factor: Float): Int {

        val r = (Color.red(color) + (255 - Color.red(color)) * factor).toInt()

        val g = (Color.green(color) + (255 - Color.green(color)) * factor).toInt()

        val b = (Color.blue(color) + (255 - Color.blue(color)) * factor).toInt()

        return Color.rgb(r, g, b)
    }
}