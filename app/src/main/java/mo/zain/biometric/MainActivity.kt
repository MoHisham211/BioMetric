package mo.zain.biometric

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                displayMessage("Biometric authentication is available")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                displayMessage("This device doesn't support biometric authentication")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                displayMessage("Biometric authentication is currently unavailable")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                displayMessage("No biometric credentials are enrolled")
        }

        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                displayMessage("Authentication error: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                displayMessage("Authentication succeeded!")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                displayMessage("Authentication failed")
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        findViewById<View>(R.id.authenticateButton).setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
/*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException

fun updateUserInfo(token: String, files: List<Pair<String, String>>) {
    val url = "http://127.0.0.1:8000/api/update_user_infoo/"

    val client = OkHttpClient()

    val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
    requestBodyBuilder.addFormDataPart("email", "omn@example.com")
    requestBodyBuilder.addFormDataPart("phone", "01002284852")
    requestBodyBuilder.addFormDataPart("fullname", "zzzxx")

    files.forEach { (name, filePath) ->
        val file = File(filePath)
        if (file.exists()) {
            requestBodyBuilder.addFormDataPart(
                "attachments", // Ensure the parameter name is correct
                file.name,
                RequestBody.create("image/png".toMediaTypeOrNull(), file)
            )
        } else {
            println("File not found: $filePath")
        }
    }

    val requestBody = requestBodyBuilder.build()

    val request = Request.Builder()
        .url(url)
        .header("Authorization", "Token $token")
        .put(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            println("An error occurred: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            println(responseBody)
        }
    })
}

fun main() {
    val token = "28a9a6b87c1d39913d528a70635256ddd27be5dc"
    val files = listOf(
        Pair("photo", "D:\\testyy\\img.PNG"),
        Pair("passport", "D:\\testyy\\img2.PNG"),
        // Add more files as needed
    )
    updateUserInfo(token, files)
}
 */