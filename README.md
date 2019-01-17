# as-appdelivery-android
SDK for distribution version control

# Get Started
1.  Add SDK to project
    - Import SDK
      In Android Studio right click a module and select new -> module -> import .JAR/.AAR Package and navigate to where the .aar file is stored.
    - Add ':ModuleName' to include in settings.gradle
    - In app build.gradle add "implementation project(':app-delivery-sdk')" as a dependency

2.  In the class you want to run the version check from (most likely the first activity) implement AppDeliveryInterface and override its members.

3.  Create an Instance of AppDelivery(appDeliveryInterface, apiKey) and call its startVersionCheck().
    Once a result have been computed AppDelivery will call onVersionCheckResult() from the interface passed to the constructor.

4.  onVersionCheckResult takes a VersionResult data class as its only object and contains all information needed to implement a solution suited for your apps needs.
    VersionResult is the only return value from the SDK and has the following structure:
    ```
    /**
     * Data model returned to the AppDeliveryInterface passed at initiation of AppDelivery.
     * This is the only data passed between the implementor and the SDK and contains all
     * results calculated.
     *
     * @property resultCode enum containing the result of the version check.
     * @property downloadUrl holds path to apk
     * @property deviceVersion holds the current version of the app on the local device.
     * @property minimumVersion holds the minimum required (forced) version of the app.
     * @property recommendedVersion holds the recommended version of the app.
     */
    data class VersionResult(
            val resultCode: VersionResultCode,
            val downloadUrl: String? = null,
            val deviceVersion: List<Int>? = null,
            val minimumVersion: List<Int>? = null,
            val recommendedVersion: List<Int>? = null
    ) : Serializable

    ```

    The most important property is the resultCode which is an Enum:
    ```
    /**
     * Enum containing possible result outcomes.
     * This is what will be used by the implementor to determine cause of action.
     */
    enum class VersionResultCode(var message: String) {
        UPDATE_REQUIRED("Update Required"),
        UPDATE_AVAILABLE("Update Available"),
        UP_TO_DATE("Up To Date"),
        ERROR("ERROR")
    }
    ```

    Use this information to implement the correct response for your app.
    DONE!

    ### Example

    ```
    //implement Interface
    class LaunchActivity : Activity(), AppDeliveryInterface {

        //override context
    	override val context: Context = this

    	override fun onCreate(savedInstanceState: Bundle?) {
    		super.onCreate(savedInstanceState)

    		appDelivery = AppDelivery(this)
            Log.d("@dev onCreate", "layout completed")

            setupListeners()
            responseText.text = getString(R.string.FETCHING)
            appDelivery.startVersionCheck()
    	}

    	//onVersionCheckedResult is called asynchronously when a result is ready.
    	//Decide what to do with the result and implement it here.
    	override fun onVersionCheckResult(versionResult: VersionResult) {

    	    //Check result code
    	    when (versionResult.resultCode) {
    	        UP_TO_DATE ->       //All good
    	        UPDATE_AVAILABLE -> //Implement custom prompt to upgrade
    	        UPDATE_REQUIRED ->  //Implement lock down here
    	        ERROR ->            //Handle error here
    	     }
    	}
    }
    ```

5.  Optional!
    If you don't wish to implement your own logic to handle version conflicts there is a default class called AlertDialog.
    Just call the static function showDialog(Context, VersionResult) with the obtained VersionResult and a dialog will show prompting the user to update the app if needed.
    If the app does not meet the required version the AlertDialog will lock the app until the required minimum version is met.

    ### Example

    ```
    import com.appshack.appdelivery.utility.dialog.VersionAlert

    //in onVersionCheckResult, simply call VersionAlerts static method showDialog.
    override fun onVersionCheckResult(versionResult: VersionResult) {

        //Check result code
  	    when (versionResult.resultCode) {
            UP_TO_DATE ->       //All good
            UPDATE_AVAILABLE -> VersionAlert.showDialog(this, versionResult)
            UPDATE_REQUIRED ->  VersionAlert.showDialog(this, versionResult)
            ERROR ->            //Handle error here
        }
    }
    ```

# FlowChart
![Flowchart](docs/AppDeliveryFlowchart.png)
