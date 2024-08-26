package de.php_perfect.intellij.ddev.version

import kotlin.math.max

class Version(version: String) : Comparable<Version?> {
    @JvmField
    val numbers: IntArray


    val string: String

    init {
        this.string = version
        val split: Array<String?> =
            version.replace("^v".toRegex(), "").split("-".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        numbers = IntArray(split.size)
        for (i in split.indices) {
            numbers[i] = split[i]!!.toInt()
        }
    }

    override fun compareTo(other: Version?): Int {
        val maxLength: Double = max(numbers.size.toDouble(), other?.numbers?.size?.toDouble() ?: 0.0)
        for (i in 0 until maxLength.toInt()) {
            val left = if (i < numbers.size) numbers[i] else 0
            val right = if (i < (other?.numbers?.size ?: 0)) other?.numbers?.get(i) ?: 0 else 0
            if (left != right) {
                return if (left < right) -1 else 1
            }
        }
        return 0
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val version = o as Version
        return numbers.contentEquals(version.numbers)
    }

    override fun hashCode(): Int {
        return numbers.contentHashCode()
    }

    override fun toString(): String {
        return this.string
    }
}
