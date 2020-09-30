package to.freebots.temperaturegauge

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gauge.minTemperature = -100F
        gauge.maxTemperature = 100F

        gauge.temperature = 25F

        gauge.tintIcons = true

        gauge.config = arrayListOf(
            TemperatureRangeConfig(
                -10F,
                Color.CYAN,
                "to cold!",
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_cold)
            ),
            TemperatureRangeConfig(
                30F, Color.GREEN, "perfect :)",
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_ideal)
            ),
            TemperatureRangeConfig(
                60F, Color.YELLOW, "its getting hot here",
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_warm)
            ),
            TemperatureRangeConfig(
                100F,
                Color.RED,
                "to hot >:(",
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_hot)
            )
        )
    }
}