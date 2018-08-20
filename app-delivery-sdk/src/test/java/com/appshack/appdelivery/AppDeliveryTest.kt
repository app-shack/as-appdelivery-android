package com.appshack.appdelivery

import com.appshack.appdelivery.entity.VersionResultCode
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.logic.AppDelivery
import com.appshack.appdelivery.network.api.models.VersionDataModel
import com.appshack.appdelivery.utility.extensions.compareTo
import com.appshack.appdelivery.utility.extensions.toVersionList
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test


/**
 * Created by joelbrostrom on 2018-08-04
 * Developed by App Shack
 */
class AppDeliveryTest {
    private lateinit var appDelivery: AppDelivery

    @Before
    fun setup() {
        val appDeliveryInterface: AppDeliveryInterface = mock()
        appDelivery = AppDelivery(appDeliveryInterface)
    }

    @Test
    fun getMaxVersionLength_equal_shouldReturnThree() {
        val left = listOf(1, 2, 3)
        val right = listOf(1, 2, 3)
        val candidates = listOf(left, right)
        assertEquals(3, appDelivery.getMaxLength(candidates))
    }

    @Test
    fun toVersionList_stringToList_shouldReturnList() {
        val arg = "1.3.2"
        val expected = mutableListOf(1, 3, 2)
        assertEquals(expected, arg.toVersionList())
    }

    @Test
    fun getMaxVersionLength_longer_shouldReturnFour() {
        val left = listOf(1, 2, 3, 4)
        val right = listOf(1, 2, 3)
        val candidates = listOf(left, right)
        assertEquals(4, appDelivery.getMaxLength(candidates))
    }

    @Test
    fun getMaxVersionLength_shorter_shouldReturnTree() {
        val left = listOf(1)
        val right = listOf(1, 2, 3)
        val candidates = listOf(left, right)
        assertEquals(3, appDelivery.getMaxLength(candidates))
    }

    @Test
    fun isVersionGraterThen_equal_shouldReturnFalse() {
        val left = listOf(1, 2, 3)
        val right = listOf(1, 2, 3)
        assertFalse(left > right)
    }

    @Test
    fun isVersionGraterThen_major_shouldReturnTrue() {
        val left = listOf(3, 1, 0)
        val right = listOf(1, 0, 2)
        assertTrue(left > right)
    }

    @Test
    fun isVersionGraterThen_major_shouldReturnFalse() {
        val left = listOf(1, 1, 5)
        val right = listOf(2, 0, 2)
        assertFalse(left > right)
    }

    @Test
    fun isVersionGraterThen_minor_shouldReturnTrue() {
        val left = listOf(2, 1, 5)
        val right = listOf(2, 0, 2)
        assertTrue(left > right)
    }

    @Test
    fun isVersionGraterThen_minor_shouldReturnFalse() {
        val left = listOf(2, 1, 0)
        val right = listOf(2, 3, 2)
        assertFalse(left > right)
    }

    @Test
    fun isVersionGraterThen_patch_shouldReturnTrue() {
        val left = listOf(3, 3, 5)
        val right = listOf(3, 3, 2)
        assertTrue(left > right)
    }

    @Test
    fun isVersionGraterThen_patch_shouldReturnFalse() {
        val left = listOf(1, 1, 1)
        val right = listOf(1, 1, 5)
        assertFalse(left > right)
    }

    @Test
    fun isVersionGraterThen_negative_shouldReturnTrue() {
        val left = listOf(1, 3, 5)
        val right = listOf(-2, 5, 2)
        assertTrue(left > right)
    }

    @Test
    fun isVersionGraterThen_doubleDigits_shouldReturnTrue() {
        val left = listOf(1, 15, 1)
        val right = listOf(1, 6, 1)
        assertTrue(left > right)
    }

    @Test
    fun isVersionGraterThen_differentLengthShort_shouldReturnTrue() {
        val left = listOf(1, 5)
        val right = listOf(0, 1, 5)
        assertTrue(left > right)
    }

    @Test
    fun getVersionResultCode_isRequired_shouldReturn_UPDATE_REQUIRED() {
        val isUpdateRequired = true
        val isUpdateAvailable = true
        assertEquals(VersionResultCode.UPDATE_REQUIRED,
                appDelivery.getVersionResultCode(isUpdateRequired, isUpdateAvailable)
        )
    }

    @Test
    fun getVersionResultCode_isAvailable_shouldReturn_UPDATE_AVAILABLE() {
        val isUpdateRequired = false
        val isUpdateAvailable = true
        assertEquals(VersionResultCode.UPDATE_AVAILABLE,
                appDelivery.getVersionResultCode(isUpdateRequired, isUpdateAvailable)
        )
    }

    @Test
    fun getVersionResultCode_upToDate_shouldReturn_UP_TO_DATE() {
        val isUpdateRequired = false
        val isUpdateAvailable = false
        assertEquals(VersionResultCode.UP_TO_DATE,
                appDelivery.getVersionResultCode(isUpdateRequired, isUpdateAvailable)
        )
    }

    @Test
    fun adjustVersionLength_shorter_shouldReturnTrue() {
        val left = mutableListOf(1)
        val right = mutableListOf(1, 2, 3)
        val candidates = mutableListOf(left, right)
        val maxLength = appDelivery.getMaxLength(candidates)

        val expected = listOf(listOf(1, 0, 0), listOf(1, 2, 3))
        assertEquals(expected, appDelivery.adjustVersionLength(candidates, maxLength))
    }

    @Test
    fun adjustVersionLength_equal_shouldReturnTrue() {
        val left = mutableListOf(1, 2, 3)
        val right = mutableListOf(1, 2, 3)
        val candidates = listOf(left, right)
        val maxLength = appDelivery.getMaxLength(candidates)

        val expected = listOf(listOf(1, 2, 3), listOf(1, 2, 3))
        assertEquals(expected, appDelivery.adjustVersionLength(candidates, maxLength))
    }

    @Test
    fun adjustVersionLength_longer_shouldReturnTrue() {
        val left = mutableListOf(1, 2, 3)
        val right = mutableListOf(1, 2)
        val candidates = listOf(left, right)
        val maxLength = appDelivery.getMaxLength(candidates)

        val expected = listOf(listOf(1, 2, 3), listOf(1, 2, 0))
        assertEquals(expected, appDelivery.adjustVersionLength(candidates, maxLength))
    }

    @Test
    fun buildVersionCheckResult_updateRequired_ShouldReturnUPDATE_REQUIRED() {
        val versionDataModel = VersionDataModel(requiredVersion = "99.99.99", currentVersion = "1.2.3")
        val versionCheckResult = appDelivery.buildVersionResult(versionDataModel)
        assertEquals(VersionResultCode.UPDATE_REQUIRED, versionCheckResult.resultCode)
    }

    @Test
    fun buildVersionCheckResult_updateAvailable_ShouldReturnUPDATE_AVAILABLE() {
        val versionDataModel = VersionDataModel(latestVersion = "99.99.99", currentVersion = "1.2.3")
        val versionCheckResult = appDelivery.buildVersionResult(versionDataModel)
        assertEquals(VersionResultCode.UPDATE_AVAILABLE, versionCheckResult.resultCode)
    }

    @Test
    fun buildVersionCheckResult_upToDate_ShouldReturnUP_TO_DATE() {
        val versionDataModel = VersionDataModel(requiredVersion = "0.0.0", latestVersion = "0.0.0", currentVersion = "1.2.3")
        val versionCheckResult = appDelivery.buildVersionResult(versionDataModel)
        assertEquals(VersionResultCode.UP_TO_DATE, versionCheckResult.resultCode)
    }

    @Test
    fun buildVersionCheckResult_upToDateEqual_ShouldReturnUP_TO_DATE() {
        val versionDataModel = VersionDataModel(requiredVersion = "1.2.3", latestVersion = "1.2.3", currentVersion = "1.2.3")
        val versionCheckResult = appDelivery.buildVersionResult(versionDataModel)
        assertEquals(VersionResultCode.UP_TO_DATE, versionCheckResult.resultCode)
    }

}