package com.example.bp_frontend

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.MediaRecorder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.ObservationDataItem
import com.example.bp_frontend.loginLogic.SessionManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NewSimpleObservationActivity : AppCompatActivity() {

    val items: MutableList<String> = ArrayList()
    lateinit var file_path: Uri
    var code: Int = 0

    private var latitude:Double = 0.0
    private var longitude:Double = 0.0

    lateinit var location_client: FusedLocationProviderClient
    lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient

    lateinit var cityName: String

    lateinit var media_file_path: Uri
    private var permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false

    private lateinit var recorder: MediaRecorder

    private var dirPath = ""
    private var mediaFilename = ""

    private var isRecordingActive = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_simple_observation)

        back_button()

        val audio_button = findViewById<View>(R.id.audio_button)

        permissionGranted = ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED

        if(!permissionGranted)
            ActivityCompat.requestPermissions(this, permissions, REQ_CODE)


        audio_button.setOnClickListener {
            when{
                isRecordingActive ->stopRecorder()
                else -> startRecording()
            }
        }

        val size = findViewById<TextInputEditText>(R.id.size)
        val color = findViewById<TextInputEditText>(R.id.color)
        val beak = findViewById<TextInputEditText>(R.id.beak)
        val number = findViewById<TextInputEditText>(R.id.decimal)
        val loc_text = findViewById<TextInputEditText>(R.id.loc_text)

        val photo_button = findViewById<View>(R.id.photo_button)
        val location_button = findViewById<View>(R.id.get_location)

        val location_text = findViewById<TextInputEditText>(R.id.loc_text)
        val submit_button = findViewById<RelativeLayout>(R.id.button_send)


        location_client = LocationServices.getFusedLocationProviderClient(this)
        photo_button.isClickable=true

        photo_button.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        location_text.isFocusable =false

        getGPSLocation(latitude, longitude)

        location_button.setOnClickListener {

            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<android.location.Address> = geocoder.getFromLocation(latitude, longitude, 1)
            cityName = addresses[0].subAdminArea
            location_text.setText(cityName)
            Log.d("my_debug", "$addresses")
        }

        submit_button.setOnClickListener{

            if (checkEmptyFields(size, color, beak, number)) return@setOnClickListener

            setClickedProperty(submit_button, findViewById(R.id.send_2))

            if(!this::file_path.isInitialized && !this::media_file_path.isInitialized){

                apiClient = BackendApiClient()
                sessionManager = SessionManager(this)

                val intent = Intent(applicationContext, HomeActivity::class.java)

                apiClient.getApiService(this)
                    .newSimpleObservationNoPhotoNoVoice(
                        token = "Token ${sessionManager.getToken()}",
                        bird_count = number.text.toString().toInt(),
                        obs_x_coords = longitude.toFloat(),
                        obs_y_coords = latitude.toFloat(),
                        obs_place = cityName,
                        bird_beak = beak.text.toString(),
                        bird_color = color.text.toString(),
                        bird_size = size.text.toString()

                    ).enqueue(object : Callback<ObservationDataItem?> {

                        override fun onResponse(
                            call: Call<ObservationDataItem?>,
                            response: Response<ObservationDataItem?>
                        )
                        {
                            if(response.code() == 200) {
                                Toast.makeText(applicationContext,"Pozorovanie bolo pridané.", Toast.LENGTH_LONG).show()
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                                Log.d("my_debug", "${response.code()} <|> ${response.body()}")
                                Log.d("my_debug", "token ${sessionManager.getToken()} \n " +
                                        "bird_count = ${number.text.toString()},\n" +
                                        "obs_x_coords = $longitude,\n" +
                                        "obs_y_coords = $latitude,\n")

                            }

                            else {

                                Toast.makeText(applicationContext," HALF SUCCESS ${response.code()}", Toast.LENGTH_SHORT).show()
                                Log.d("my_debug", "${response.code()} <|> ${response.body()}")
                                Log.d("my_debug", "token ${sessionManager.getToken()} \n " +
                                        "bird_count = ${number.text.toString()},\n" +
                                        "obs_x_coords = $longitude,\n" +
                                        "obs_y_coords = $latitude,\n")
                            }
                        }

                        override fun onFailure(call: Call<ObservationDataItem?>, t: Throwable) {

                            Toast.makeText(applicationContext, "FAIL", Toast.LENGTH_SHORT).show()
                            Log.d("my_debug", "${t.message}")
                        }
                    })
            }

            if(this::file_path.isInitialized && !this::media_file_path.isInitialized){
                val file = File(file_path.path)
                val requestbody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("bird_photo", file.name, requestbody)


                apiClient = BackendApiClient()
                sessionManager = SessionManager(this)
                val intent = Intent(applicationContext, HomeActivity::class.java)
                apiClient.getApiService(this).newSimpleObservationWithPhotoNoVoice(
                    token = "Token ${sessionManager.getToken()}",
                    bird_count = number.text.toString().toInt(),
                    obs_x_coords = longitude.toFloat(),
                    obs_y_coords = latitude.toFloat(),
                    bird_photo = part,
                    obs_place = cityName,
                    bird_beak = beak.text.toString(),
                    bird_color = color.text.toString(),
                    bird_size = size.text.toString()

                ).enqueue(object : Callback<ObservationDataItem?> {
                    override fun onResponse(
                        call: Call<ObservationDataItem?>,
                        response: Response<ObservationDataItem?>
                    ) {
                        if(response.code() == 200) {
                            Toast.makeText(applicationContext,"Pozorovanie bolo pridané.", Toast.LENGTH_LONG).show()
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()

                        }

                        else {
                            Toast.makeText(applicationContext," HALF SUCCESS ${response.code()}", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<ObservationDataItem?>, t: Throwable) {

                        Toast.makeText(applicationContext, "FAIL", Toast.LENGTH_SHORT).show()
                        Log.d("my_debug", "${t.message}")
                    }
                })
            }

            if(!this::file_path.isInitialized && this::media_file_path.isInitialized){
                Log.d("my_debug", "it is null")

                apiClient = BackendApiClient()
                sessionManager = SessionManager(this)

                val intent = Intent(applicationContext, HomeActivity::class.java)

                val recording_file = File(media_file_path.path)
                val recoridng_requestbody = recording_file.asRequestBody("recording/*".toMediaTypeOrNull())
                val record_part = MultipartBody.Part.createFormData(
                    "bird_recording",
                    recording_file.name,
                    recoridng_requestbody
                )

                apiClient.getApiService(this)
                    .newSimpleObservationNoPhotoWithVoice(
                        token = "Token ${sessionManager.getToken()}",
                        bird_count = number.text.toString().toInt(),
                        obs_x_coords = longitude.toFloat(),
                        obs_y_coords = latitude.toFloat(),
                        obs_place = cityName,
                        bird_beak = beak.text.toString(),
                        bird_color = color.text.toString(),
                        bird_size = size.text.toString(),
                        bird_recording = record_part

                    ).enqueue(object : Callback<ObservationDataItem?> {

                        override fun onResponse(
                            call: Call<ObservationDataItem?>,
                            response: Response<ObservationDataItem?>
                        )
                        {
                            if(response.code() == 200) {
                                Toast.makeText(applicationContext,"Pozorovanie bolo pridané.", Toast.LENGTH_LONG).show()
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(applicationContext," HALF SUCCESS ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ObservationDataItem?>, t: Throwable) {

                            Toast.makeText(applicationContext, "FAIL", Toast.LENGTH_SHORT).show()
                        }
                    })
            }

            if(this::file_path.isInitialized && this::media_file_path.isInitialized){
                val file = File(file_path.path)
                val requestbody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("bird_photo", file.name, requestbody)


                val recording_file = File(media_file_path.path)
                val recoridng_requestbody = recording_file.asRequestBody("recording/*".toMediaTypeOrNull())
                val record_part = MultipartBody.Part.createFormData(
                    "bird_recording",
                    recording_file.name,
                    recoridng_requestbody
                )

                apiClient = BackendApiClient()
                sessionManager = SessionManager(this)
                val intent = Intent(applicationContext, HomeActivity::class.java)
                apiClient.getApiService(this).newSimpleObservationWithPhotoWithVoice(
                    token = "Token ${sessionManager.getToken()}",
                    bird_count = number.text.toString().toInt(),
                    obs_x_coords = longitude.toFloat(),
                    obs_y_coords = latitude.toFloat(),
                    bird_photo = part,
                    obs_place = cityName,
                    bird_beak = beak.text.toString(),
                    bird_color = color.text.toString(),
                    bird_size = size.text.toString(),
                    bird_recording = record_part


                ).enqueue(object : Callback<ObservationDataItem?> {
                    override fun onResponse(
                        call: Call<ObservationDataItem?>,
                        response: Response<ObservationDataItem?>
                    ) {
                        if(response.code() == 200) {
                            Toast.makeText(applicationContext,"Pozorovanie bolo pridané.", Toast.LENGTH_LONG).show()
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()

                        }

                        else {
                            Toast.makeText(applicationContext," HALF SUCCESS ${response.code()}", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<ObservationDataItem?>, t: Throwable) {

                        Toast.makeText(applicationContext, "FAIL", Toast.LENGTH_SHORT).show()
                        Log.d("my_debug", "${t.message}")
                    }
                })
            }


        }


    }

    private fun checkEmptyFields(
        size: TextInputEditText,
        color: TextInputEditText,
        beak: TextInputEditText,
        number: TextInputEditText
    ): Boolean {
        if (size.text!!.isEmpty()) {
            size.error = "Toto pole je povinné"
            size.requestFocus()
            return true
        }

        if (color.text!!.isEmpty()) {
            color.error = "Toto pole je povinné"
            color.requestFocus()
            return true
        }

        if (beak.text!!.isEmpty()) {
            beak.error = "Toto pole je povinné"
            beak.requestFocus()
            return true
        }

        if (number.text!!.isEmpty()) {
            number.error = "Toto pole je povinné"
            number.requestFocus()
            return true
        }
        return false
    }

    private fun setClickedProperty(button_submit: RelativeLayout, login_text: TextView) {
        button_submit.setBackgroundResource(R.drawable.button_prim_dark) // set dark color
        button_submit.isEnabled = false
        button_submit.isClickable = false
        login_text.text = "Počkajte.."
    }

    private fun setOriginalProperty(button_submit: RelativeLayout, login_text: TextView) {
        button_submit.setBackgroundResource(R.drawable.button_prim) // set original color
        button_submit.isEnabled = true
        button_submit.isClickable = true
        login_text.text = "Nahrať"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            file_path = data?.data!!

            val photo_button = findViewById<View>(R.id.photo_button)
            val photo_text = findViewById<TextView>(R.id.add_photo_text)

            photo_button.setBackgroundResource(R.drawable.add_photo_button_dark)
            photo_text.text = "Fotka Pridaná"


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getGPSLocation(latitude: Double, longitude: Double) {
        if(checkPermissions()) {
            if(isLocationEnabled()) {
                // final location here

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                location_client.lastLocation.addOnCompleteListener(this){ task->
                    val location: Location?=task.result
                    if(location == null) {
                        Toast.makeText(this,"Null Received", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        // success
//                        Toast.makeText(this,"latitude=" + location.latitude + "\nlongitude" + location.longitude, Toast.LENGTH_SHORT).show()
                        this@NewSimpleObservationActivity.latitude = location.latitude
                        this@NewSimpleObservationActivity.longitude = location.longitude
                    }
                }
            }
            else {
                // open settings
                Toast.makeText(this,"Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

        }
        else {
            // request permission
            requestPermission()
        }
    }

    private fun isLocationEnabled() : Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION)
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                getGPSLocation(latitude, longitude)
            }
            else{
                Toast.makeText(applicationContext,"Denied", Toast.LENGTH_SHORT).show()
            }
    }

    private fun stopRecorder(){
        recorder.stop()

        isRecordingActive = false

        val mic_logo = findViewById<ImageView>(R.id.mic_logo)
        mic_logo.setImageResource(R.drawable.ic_baseline_mic_24)

        media_file_path = "$dirPath$mediaFilename.mp3".toUri()
    }

    private fun startRecording(){
        if(!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQ_CODE)
            return
        }
        recorder = MediaRecorder()
        dirPath = "${externalCacheDir?.absolutePath}/"
        var simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
        var date = simpleDateFormat.format(Date())
        mediaFilename = "rec_$date"

        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("$dirPath$mediaFilename.mp3")
            try {
                prepare()
            }catch (e: IOException){}

            start()
            isRecordingActive = true

            val mic_logo = findViewById<ImageView>(R.id.mic_logo)
            mic_logo.setImageResource(R.drawable.ic_baseline_stop_24)


        }
    }

    private fun checkPermissions() : Boolean {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun back_button() {
        val left_top_text = findViewById(R.id.left_top_text) as TextView

        left_top_text.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
            finish()
        }
    }
}