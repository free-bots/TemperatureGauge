package to.freebots.temperaturegauge

import android.graphics.drawable.Drawable

/**
 * config class for the temperature ranges of the view
 */
data class TemperatureRangeConfig(
    val maxTemperature: Float,
    val color: Int,
    val infoText: String? = null,
    val icon: Drawable? = null
)