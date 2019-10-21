package com.deedee.citysearch.util

import android.content.res.Resources
import com.deedee.citysearch.R

/**
 * @author diederick.
 */
class Utils {

    companion object {

        fun isPortrait(resources: Resources): Boolean {
            return resources.getBoolean(R.bool.is_portrait)
        }

        fun isLandscape(resources: Resources): Boolean {
            return isPortrait(resources).not()
        }
    }

}