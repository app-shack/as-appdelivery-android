package com.appshack.appdelivery

import com.appshack.appdelivery.entity.VersionResult
import com.appshack.appdelivery.entity.VersionResultCode
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.logic.AppDelivery
import com.appshack.appdelivery.network.api.models.VersionStateResponse
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`

/**
 * Created by joelbrostrom on 2018-08-04
 * Developed by App Shack
 */
class AppDeliveryTest {
    private val appDeliveryInterface: AppDeliveryInterface
    private val appDelivery: AppDelivery
    private val versionStateResponse: VersionStateResponse

    init {
        val mockKey = "test" //Add valid key here.
        versionStateResponse = VersionStateResponse("",2,"","1.2.3", 3)
        appDeliveryInterface= mock()
        appDelivery = AppDelivery(appDeliveryInterface, mockKey)
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
    fun buildVersionCheckResult_updateRequired_ShouldReturnUPDATE_REQUIRED() {
        `when`(appDeliveryInterface.bundleId).thenReturn("com.test.appDelivery")
        `when`(appDeliveryInterface.versionCode).thenReturn(1)
        `when`(appDeliveryInterface.versionName).thenReturn("1.0.0")
        val versionCheckResult = appDelivery.buildVersionResult(versionStateResponse)

        assertEquals(VersionResultCode.UPDATE_REQUIRED, versionCheckResult.resultCode)
    }

    @Test
    fun buildVersionCheckResult_updateAvailable_ShouldReturnUPDATE_AVAILABLE() {
        `when`(appDeliveryInterface.bundleId).thenReturn("com.test.appDelivery")
        `when`(appDeliveryInterface.versionCode).thenReturn(2)
        `when`(appDeliveryInterface.versionName).thenReturn("1.0.1")

        val versionCheckResult = appDelivery.buildVersionResult(versionStateResponse)
        assertEquals(VersionResultCode.UPDATE_AVAILABLE, versionCheckResult.resultCode)
    }

    @Test
    fun buildVersionCheckResult_upToDate_ShouldReturnUP_TO_DATE() {
        `when`(appDeliveryInterface.bundleId).thenReturn("com.test.appDelivery")
        `when`(appDeliveryInterface.versionCode).thenReturn(3)
        `when`(appDeliveryInterface.versionName).thenReturn("1.0.2")

        val versionCheckResult = appDelivery.buildVersionResult(versionStateResponse)
        assertEquals(VersionResultCode.UP_TO_DATE, versionCheckResult.resultCode)
    }

}