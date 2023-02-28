package com.example.yeojeong

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.yeojeong.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        val mapView = MapView(this)
        binding.KakaoView.addView(mapView)

        // 줌 인
        mapView.zoomIn(true)

        // 줌 아웃
        mapView.zoomOut(true)

        // 서울역의 위도 경도
        val mapPoint = MapPoint.mapPointWithGeoCoord(37.554668024641515,
            126.97059786786713)

        // 지도의 중심을 서울역으로 변경 후 줌 레벨도 변경 해줌.
        mapView.setMapCenterPoint(mapPoint, true)
        mapView.setZoomLevel(2, true)


        // 마커 생성
        val marker = MapPOIItem()
        marker.itemName = "이곳은 서울역 입니다."
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.BluePin
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin

        // 설정한 메소드를 marker에 적용함.
        mapView.addPOIItem(marker)




        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}