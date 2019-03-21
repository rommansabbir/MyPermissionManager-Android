# MyPermissionManager-Android (also support for Kotlin)
A simple library to get requested permission in Android

## Documentation

### Installation
---
Step 1. Add the JitPack repository to your build file 

```gradle
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency

```gradle
	dependencies {
          implementation 'com.github.rommansabbir:MyPermissionManager-Android:Tag'
	}
```

---

### Version available

| Releases        
| ------------- |
| v1.0          |


# Usages

### For Java: 

```java
public class MainActivity extends AppCompatActivity implements MyPermissionManagerInterface {
    private static final String TAG = "AnotherActivity";
    private MyPermissionManager myPermissionManager;
    private String permission = Manifest.permission.CALL_PHONE;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
        /**
         * Instantiate MyPermissionManager with context
         */
        myPermissionManager = new MyPermissionManager(this);
        /**
         * setup permission manager by pass your required permission
         */
        myPermissionManager.setupMyPermissionManager(permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /**
         * Let MyPermissionManager to handle the results
         */
        myPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMPMPermissionGranted() {
        //TODO Implement your logic here
    }

    @Override
    public void onMPMPermissionDenied() {
        //TODO Implement your logic here
        /**
         * If permission is must required
         * call setupMyPermissionManager() again
         */
        myPermissionManager.setupMyPermissionManager(permission);
    }

    @Override
    public void onMPMPermissionDeniedPermanently() {
        /**
         * Allow user to manually grant permission
         * Navigate user to Apps Settings
         */
        myPermissionManager.openAppSetting();
    }

    @Override
    public void onMPMException(@NotNull Exception e) {
        Log.d(TAG, "onMPMException: "+e.getMessage());
        //TODO Implement your logic here
    }
```




### For Kotlin: 

```kotlin
class MainActivity : AppCompatActivity(), MyPermissionManagerInterface{
    private var myPermissionManager : MyPermissionManager? = null
    private var TAG : String = "MainActivity"
    private val permission : String = android.Manifest.permission.CALL_PHONE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * Instantiate MyPermissionManager with context
         */
        myPermissionManager = MyPermissionManager(this)
        /**
         * setup permission manager by pass your required permission
         */
        myPermissionManager!!.setupMyPermissionManager(permission)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        /**
         * Let MyPermissionManager to handle the results
         */
        myPermissionManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMPMPermissionGranted() {
        //TODO Implement your logic here
    }

    override fun onMPMPermissionDenied() {
        //TODO Implement your logic here
        /**
         * If permission is must required
         * call setupMyPermissionManager() again
         */
        myPermissionManager!!.setupMyPermissionManager(permission)
    }

    override fun onMPMPermissionDeniedPermanently() {
        /**
         * Allow user to manually grant permission
         * Navigate user to Apps Settings
         */
        myPermissionManager!!.openAppSetting()
    }

    override fun onMPMException(error: Exception) {
        //TODO Implement your logic here
        Log.d(TAG, "onMPMException: " + error.message)
    }

}
```

### Contact me
[Portfolio](https://www.rommansabbir.com/) | [LinkedIn](https://www.linkedin.com/in/rommansabbir/) | [Twitter](https://www.twitter.com/itzrommansabbir/) | [Facebook](https://www.facebook.com/itzrommansabbir/)

