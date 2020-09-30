package to.freebots.temperaturegauge

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import kotlin.math.abs
import kotlin.math.min


class TemperatureGauge : View {

    companion object {
        private const val MIN_ANGLE = 150F
        private const val MAX_ANGLE = 240F
    }

    var temperature = 0F
        set(value) {
            field = value

            if (value > maxTemperature || value < minTemperature) {
                throw IllegalAccessException("update temperature config")
            }

            invalidate()
        }

    var minTemperature = 0F
        set(value) {
            field = value
            invalidate()
        }
    var maxTemperature = 0F
        set(value) {
            field = value
            invalidate()
        }

    var config: ArrayList<TemperatureRangeConfig> = arrayListOf()
        set(value) {
            field = value
            invalidate()
        }

    var tintIcons = true
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.TemperatureGauge, defStyle, 0
        )

        a.recycle()
    }

    private fun getMaxSize() = min(measuredHeight, measuredWidth)

    private fun getMaxRadius() = getMaxSize() / 2

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBackground(canvas)

        drawTemperatureRangeArcs(canvas)

        drawMid(canvas)

        drawTemperatureArcBackground(canvas)
        drawTemperatureArc(canvas)

        drawForeground(canvas)

        drawTemperatureText(canvas)

        drawInfoText(canvas)

        drawIcon(canvas)
    }

    /**
     * draws the background circle of the view
     */
    private fun drawBackground(canvas: Canvas) {
        canvas.drawCircle(
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 2).toFloat(),
            getMaxRadius().toFloat(),
            createPaint(Color.BLACK)
        )
    }

    private fun drawMid(canvas: Canvas) {
        canvas.drawCircle(
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 2).toFloat(),
            getMaxRadius().toFloat() * 0.80F,
            createPaint(Color.BLACK)
        )
    }

    private fun drawForeground(canvas: Canvas) {
        canvas.drawCircle(
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 2).toFloat(),
            getMaxRadius().toFloat() * 0.35F,
            createPaint(Color.BLACK)
        )
    }

    /**
     * draws the icon corresponding to the temperature
     */
    private fun drawIcon(canvas: Canvas) {

        val radiusOffset = percentageOfSize(44F)
        val downOffset = percentageOfSize(35F)
        getDrawableOfTemperature(temperature)?.let {

            it.colorFilter = null

            if (tintIcons) {
                it.colorFilter =
                    PorterDuffColorFilter(
                        getColorOfTemperature(temperature),
                        PorterDuff.Mode.SRC_ATOP
                    )
            }


            it.setBounds(
                (((measuredWidth / 2).toFloat() - getMaxRadius() + radiusOffset)).toInt(),
                (((measuredHeight / 2).toFloat() - getMaxRadius() + radiusOffset) + downOffset).toInt(),
                (((measuredWidth / 2).toFloat() + getMaxRadius() - radiusOffset)).toInt(),
                (((measuredHeight / 2).toFloat() + getMaxRadius() - radiusOffset) + downOffset).toInt()
            )
            it.draw(canvas)
        }
    }

    /**
     * draws the temperature range
     */
    private fun drawTemperatureRangeArcs(canvas: Canvas) {

        config.forEachIndexed { index: Int, rangeConfig: TemperatureRangeConfig ->

            val radius = percentageOfSize(5F)

            val minAnge = if (index == 0) {
                MIN_ANGLE
            } else {
                temperatureToAngle(config[index - 1].maxTemperature) + MIN_ANGLE
            }


            val maxAngle = if (index == 0 && config.size == 1) {
                MAX_ANGLE
            } else if (index == 0) {
                temperatureToAngle(config[index].maxTemperature)
            } else {
                temperatureToAngle(config[index].maxTemperature) - temperatureToAngle(config[index - 1].maxTemperature)
            }

            canvas.drawArc(
                createRect(radius), minAnge, maxAngle, true, createPaint(rangeConfig.color)
            )
        }

    }

    private fun drawTemperatureArc(canvas: Canvas) {
        val radius = percentageOfSize(15F)

        val color = getColorOfTemperature(temperature)

        canvas.drawArc(
            createRect(radius),
            MIN_ANGLE,
            temperatureToAngle(temperature),
            true,
            createPaint(color)
        )
    }

    private fun drawTemperatureArcBackground(canvas: Canvas) {

        val radius = percentageOfSize(15F)

        canvas.drawArc(
            createRect(radius),
            MIN_ANGLE,
            MAX_ANGLE,
            true,
            createPaint(Color.GRAY)
        )
    }

    private fun drawTemperatureText(canvas: Canvas) {

        val textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.CENTER
            color = Color.WHITE
        }

        val text = "${temperature}°C"
        textPaint.let {
            it.textSize = getMaxSize() / 10 - it.measureText(text)
        }

        canvas.drawText(
            text,
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 2).toFloat(),
            textPaint
        )
    }

    private fun drawInfoText(canvas: Canvas) {

        val textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.CENTER
            color = getColorOfTemperature(temperature)
        }

        val text = getInfoTextOfTemperature(temperature)
        textPaint.let {
            it.textSize = getMaxSize() / 8 - it.measureText(text)
        }


        canvas.drawText(
            text,
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 2).toFloat() + getMaxRadius() / 2,
            textPaint
        )
    }

    private fun temperatureToAngle(temperature: Float) =
        ((temperature + abs(minTemperature)) / (abs(minTemperature) + abs(maxTemperature))) * (MAX_ANGLE)

    private fun createRect(radiusOffset: Float): RectF {
        return RectF(
            (measuredWidth / 2).toFloat() - getMaxRadius() + radiusOffset,
            (measuredHeight / 2).toFloat() - getMaxRadius() + radiusOffset,
            (measuredWidth / 2).toFloat() + getMaxRadius() - radiusOffset,
            (measuredHeight / 2).toFloat() + getMaxRadius() - radiusOffset
        )
    }

    private fun createPaint(paintColor: Int) = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = paintColor
    }

    private fun getConfigOfTemperature(temperature: Float): TemperatureRangeConfig? {
        var foundConfig: TemperatureRangeConfig? = null

        for (rangeConfig in config) {
            if (temperature <= rangeConfig.maxTemperature) {
                foundConfig = rangeConfig
                break
            }
        }

        return foundConfig
    }

    private fun getColorOfTemperature(temperature: Float): Int {
        return getConfigOfTemperature(temperature)?.color
            ?: run { 0 }
    }

    private fun getDrawableOfTemperature(temperature: Float): Drawable? {
        return getConfigOfTemperature(temperature)?.icon
    }

    private fun getInfoTextOfTemperature(temperature: Float): String {
        return getConfigOfTemperature(temperature)?.infoText ?: run { "" }
    }

    private fun percentageOfSize(percent: Float) = getMaxSize() * (percent / 100)
}
