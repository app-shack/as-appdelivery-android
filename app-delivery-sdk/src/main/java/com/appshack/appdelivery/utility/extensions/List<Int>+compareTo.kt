package com.appshack.appdelivery.utility.extensions


/**
 * Created by joelbrostrom on 2018-08-20
 * Developed by App Shack
 *
 * Purpose: overload opperator >,<,=>,=< for List<Int> to compare as versions.
 * i.e. value the first index as mayor, the next as minor and the following as patches of decreasing
 * value.
 *
 * Requires input string to be of equal length or shorter.
 * Otherwise a outOfIndex exception will occur.
 *
 * Example: 1.2.3 > 2.1.0 = true
 *          3.0.0 > 2.999.999 = true
 *          1.2.0   > 1.2.9 = false
 */

operator fun List<Int>.compareTo(right: List<Int>): Int {
    for ((index, left) in this.withIndex()) when {
        left > right[index] -> return 1
        left < right[index] -> return -1
    }
    return 0
}