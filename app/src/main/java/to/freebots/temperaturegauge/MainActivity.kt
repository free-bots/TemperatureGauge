package to.freebots.temperaturegauge

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val MIN_TEMP = 0F
        private const val MAX_TEMP = 150F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initGauge()

        initSeekBar()

        initSwitch()
    }

    private fun initGauge() {
        gauge.minTemperature = MIN_TEMP
        gauge.maxTemperature = MAX_TEMP

        gauge.temperature =
            5F // current temperature (!important the temperature needs to be between min and max Temperature)

        gauge.tintIcons = true // tint icons with the color of the configuration

        gauge.config = arrayListOf(
            TemperatureRangeConfig(
                30F, // max temperature of the range
                Color.CYAN, // color of range
                "to cold!", // info text (optional)
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_cold) // icon (optional)
            ),
            TemperatureRangeConfig(
                50F, Color.GREEN, "perfect :)",
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_ideal)
            ),
            TemperatureRangeConfig(
                90F, Color.YELLOW, "to warm :|",
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_warm)
            ),
            TemperatureRangeConfig(
                MAX_TEMP,
                Color.RED,
                "to hot >:(",
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_hot)
            )
        )
    }

    private fun initSwitch() {
        sw_tinitIcons.setOnCheckedChangeListener { _, b ->
            println(b)
            gauge.tintIcons = b
        }
    }

    private fun initSeekBar() {

        sb_temperature.let {
            it.progress = 5
            it.max = MAX_TEMP.toInt()
            it.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    gauge.temperature = p1.toFloat()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            })
        }
    }
}