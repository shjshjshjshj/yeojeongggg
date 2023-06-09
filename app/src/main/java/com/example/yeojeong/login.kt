package com.example.yeojeong

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.yeojeong.call_api.Companion.TAG
import com.example.yeojeong.databinding.ActivityLoginBinding
import com.example.yeojeong.ui.placedetail.placedetailPage
import com.google.android.gms.location.*
import java.util.*
import kotlin.math.*
class login : AppCompatActivity() {

    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) } //binding biew


    //ablove about location
    val LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val LOCATION_PERMISSION_RESULT = 100

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10
    lateinit var button: Button
    lateinit var text1: TextView
    lateinit var text2: TextView

    var nowLocationX: Double = 0.0
    var nowLocationY: Double = 0.0

    object testMJL{
        var latitude = 37.57995662342976
        var longitude = 126.92189838327057
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val intent = Intent(this, MainActivity::class.java)//login to mainactivity인텐트 설정

        val subIntent = Intent(this, checkLocationIN::class.java)//jump to anotherPage

        val buttonView = findViewById<Button>(R.id.turnButton)//this is login xml button
        val turnButton = findViewById<Button>(R.id.button)


        buttonView.setOnClickListener {
            var targetDistance = DistanceManager.getDistance(mLastLocation.latitude, mLastLocation.longitude, testMJL.latitude, testMJL.longitude)
            if (targetDistance > 5000){
                startActivity(intent)
            }
            else{
                startActivity(subIntent)
            }
        }//Do click button, exchange activity
        turnButton.setOnClickListener{
            startActivity(intent)
        }

        supportActionBar?.hide();

        button = findViewById(R.id.button)
        text1 = findViewById(R.id.text1)
        text2 = findViewById(R.id.text2)


        mLocationRequest =  LocationRequest.create().apply {

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }

        // 버튼 이벤트를 통해 현재 위치 찾기
            if (checkPermissionForLocation(this)) {
                startLocationUpdates()
            }

    }


    private fun startLocationUpdates() {

        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        Looper.myLooper()?.let {
            mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback,
                it
            )
        }
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mLastLocation = location
        text2.text = "위도 : " + mLastLocation.latitude // 갱신 된 위도
        text1.text = "경도 : " + mLastLocation.longitude // 갱신 된 경도

        nowLocationX = mLastLocation.latitude
        nowLocationY = mLastLocation.longitude
        Log.e("pos", "pospospos"+nowLocationX+"a"+nowLocationY)

    }
    // 위치 권한이 있는지 확인하는 메서드
    private fun checkPermissionForLocation(context: Context): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()

            } else {
                Log.d("ttt", "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(this, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    object DistanceManager {

        private const val R = 6372.8 * 1000

        fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
            val c = 2 * asin(sqrt(a))
            return (R * c).toInt()
        }
    }


}



