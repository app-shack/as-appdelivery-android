package com.appshack.appdelivery

import android.content.Context
import com.appshack.appdelivery.entity.VersionCheckResult
import com.appshack.appdelivery.mvp.main.AppDeliveryInterface
import com.appshack.appdelivery.mvp.main.MainPresenter
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test


/**
 * Created by joelbrostrom on 2018-08-04
 * Developed by App Shack
 */
class MainTest {
    private lateinit var viewActivity: AppDeliveryInterface
    private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        viewActivity = object : AppDeliveryInterface {
            override val context: Context? = null
            override fun setTextViewText(text: String) {}
            override fun onVersionCheckResult(versionCheckResult: VersionCheckResult) {}
        }
        presenter = MainPresenter(viewActivity)
    }

    @Test
    fun getMaxVersionLength_equal_shouldReturnThree() {
        val left = listOf(1, 2, 3)
        val right = listOf(1, 2, 3)
        val candidates = listOf(left, right)
        assertEquals(3, presenter.getMaxLength(candidates))
    }

    @Test
    fun getMaxVersionLength_longer_shouldReturnFour() {
        val left = listOf(1, 2, 3, 4)
        val right = listOf(1, 2, 3)
        val candidates = listOf(left, right)
        assertEquals(4, presenter.getMaxLength(candidates))
    }

    @Test
    fun getMaxVersionLength_shorter_shouldReturnTree() {
        val left = listOf(1)
        val right = listOf(1, 2, 3)
        val candidates = listOf(left, right)
        assertEquals(3, presenter.getMaxLength(candidates))
    }

    @Test
    fun isVersionGraterThen_equal_shouldReturnFalse() {
        val left = listOf(1, 2, 3)
        val right = listOf(1, 2, 3)
        assertFalse(presenter.isVersionGraterThen(left, right))
    }

    @Test
    fun isVersionGraterThen_major_shouldReturnTrue() {
        val left = listOf(3, 1, 0)
        val right = listOf(1, 0, 2)
        assertTrue(presenter.isVersionGraterThen(left, right))
    }

    @Test
    fun isVersionGraterThen_major_shouldReturnFalse() {
        val left = listOf(1, 1, 5)
        val right = listOf(2, 0, 2)
        assertFalse(presenter.isVersionGraterThen(left, right))
    }

    @Test
    fun isVersionGraterThen_minor_shouldReturnTrue() {
        val left = listOf(2, 1, 5)
        val right = listOf(2, 0, 2)
        assertTrue(presenter.isVersionGraterThen(left, right))
    }

    @Test
    fun isVersionGraterThen_minor_shouldReturnFalse() {
        val left = listOf(2, 1, 0)
        val right = listOf(2, 3, 2)
        assertFalse(presenter.isVersionGraterThen(left, right))
    }

    @Test
    fun isVersionGraterThen_patch_shouldReturnTrue() {
        val left = listOf(3, 3, 5)
        val right = listOf(3, 3, 2)
        assertTrue(presenter.isVersionGraterThen(left, right))
    }

    @Test
    fun isVersionGraterThen_patch_shouldReturnFalse() {
        val left = listOf(1, 1, 1)
        val right = listOf(1, 1, 5)
        assertFalse(presenter.isVersionGraterThen(left, right))
    }

    @Test
    fun isVersionGraterThen_negative_shouldReturnTrue() {
        val left = listOf(1, 3, 5)
        val right = listOf(-2, 5, 2)
        assertTrue(presenter.isVersionGraterThen(left, right))
    }

    @Test
    fun isVersionGraterThen_doubleDigits_shouldReturnTrue() {
        val left = listOf(1, 15, 1)
        val right = listOf(1, 6, 1)
        assertTrue(presenter.isVersionGraterThen(left, right))
    }

    @Test
    fun isVersionGraterThen_differentLengthShort_shouldReturnTrue() {
        val left = listOf(1, 5)
        val right = listOf(0, 1, 5)
        assertTrue(presenter.isVersionGraterThen(left, right))
    }

    @Test
    fun adjustVersionLength_shorter_shouldReturnTrue() {
        val left = mutableListOf(1)
        val right = mutableListOf(1, 2, 3)
        val candidates = mutableListOf(left, right)
        val maxLength = presenter.getMaxLength(candidates)

        val expected = listOf(listOf(1, 0, 0), listOf(1, 2, 3))
        assertEquals(expected, presenter.adjustVersionLength(candidates, maxLength))
    }

    @Test
    fun adjustVersionLength_equal_shouldReturnTrue() {
        val left = mutableListOf(1, 2, 3)
        val right = mutableListOf(1, 2, 3)
        val candidates = listOf(left, right)
        val maxLength = presenter.getMaxLength(candidates)

        val expected = listOf(listOf(1, 2, 3), listOf(1, 2, 3))
        assertEquals(expected, presenter.adjustVersionLength(candidates, maxLength))
    }

    @Test
    fun adjustVersionLength_longer_shouldReturnTrue() {
        val left = mutableListOf(1, 2, 3)
        val right = mutableListOf(1, 2)
        val candidates = listOf(left, right)
        val maxLength = presenter.getMaxLength(candidates)

        val expected = listOf(listOf(1, 2, 3), listOf(1, 2, 0))
        assertEquals(expected, presenter.adjustVersionLength(candidates, maxLength))
    }

}