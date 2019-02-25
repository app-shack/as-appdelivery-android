package com.appshack.appdelivery

import com.appshack.appdelivery.entity.VersionResultCode
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.logic.AppDelivery
import com.appshack.appdelivery.network.api.models.ProjectDataModel
import com.appshack.appdelivery.network.api.models.VersionDataModel
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`

/**
 * Created by joelbrostrom on 2018-08-04
 * Developed by App Shack
 */
class AppDeliveryTest {
    private val mockKey = "test" //Add valid key here.
    private val appDeliveryInterface: AppDeliveryInterface = mock()
    private val appDelivery: AppDelivery = AppDelivery(appDeliveryInterface, mockKey)

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
        val projectDataModel = ProjectDataModel(2, "1.0.1", VersionDataModel(3))
        val versionCheckResult = appDelivery.buildVersionResult(projectDataModel)
        assertEquals(VersionResultCode.UPDATE_REQUIRED, versionCheckResult.resultCode)
    }

    @Test
    fun buildVersionCheckResult_updateAvailable_ShouldReturnUPDATE_AVAILABLE() {
        `when`(appDeliveryInterface.bundleId).thenReturn("com.test.appDelivery")
        `when`(appDeliveryInterface.versionCode).thenReturn(2)
        `when`(appDeliveryInterface.versionName).thenReturn("1.0.1")
        val projectDataModel = ProjectDataModel(2, "1.0.1", VersionDataModel(3))
        val versionCheckResult = appDelivery.buildVersionResult(projectDataModel)
        assertEquals(VersionResultCode.UPDATE_AVAILABLE, versionCheckResult.resultCode)
    }

    @Test
    fun buildVersionCheckResult_upToDate_ShouldReturnUP_TO_DATE() {
        `when`(appDeliveryInterface.bundleId).thenReturn("com.test.appDelivery")
        `when`(appDeliveryInterface.versionCode).thenReturn(3)
        `when`(appDeliveryInterface.versionName).thenReturn("1.0.2")
        val projectDataModel = ProjectDataModel(2, "1.0.1", VersionDataModel(3))
        val versionCheckResult = appDelivery.buildVersionResult(projectDataModel)
        assertEquals(VersionResultCode.UP_TO_DATE, versionCheckResult.resultCode)
    }

}