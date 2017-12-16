package fk.com.locatememobile.app.ui

import android.graphics.Color

/**
 * Created by korpa on 03.12.2017.
 */
enum class MarkerColors(val color: Int, val markerHue: Float) {
    HUE_MAGNETA(Color.parseColor("#9f529e"), 300F),
    HUE_VIOLET(Color.parseColor("#b775f6"), 270F),
    HUE_GREEN(Color.parseColor("#7df87a"), 120F),
    HUE_AZURE(Color.parseColor("#75b6f5"), 210F),
    HUE_ORANGE(Color.parseColor("#f8b779"), 30F),
    HUE_ROSE(Color.parseColor("#f26faf"), 330F),
    HUE_RED(Color.parseColor("#f87d7e"), 0F),
    HUE_CYAN(Color.parseColor("#726ff2"), 180F),
    HUE_BLUE(Color.parseColor("#9f529e"), 240F),
    HUE_YELLOW(Color.parseColor("#f8f579"), 60F)
}

