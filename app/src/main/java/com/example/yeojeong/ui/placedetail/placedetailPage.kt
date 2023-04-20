package com.example.yeojeong.ui.placedetail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.yeojeong.MainActivity
import com.example.yeojeong.R
import com.example.yeojeong.databinding.ActivityPlaceDetailBinding
import com.example.yeojeong.ui.home.HomeFragment
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.ListTemplate
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class placedetailPage : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide();

            val mapView = MapView(this)//make kakao map view
            binding.KakaoView.addView(mapView)
            mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))

        // 줌 인
            mapView.zoomIn(true)

            // 줌 아웃
            mapView.zoomOut(true)

            // sample location
            val mapPoint = MapPoint.mapPointWithGeoCoord(37.554668024641515,
                126.97059786786713)

            // 지도의 중심을 서울역으로 변경 후 줌 레벨도 변경 해줌.
            mapView.setMapCenterPoint(mapPoint, true)
            mapView.setZoomLevel(2, true)

            // 마커 생성
            val marker = MapPOIItem()
            marker.itemName = "가나다라마바사하zk"
            marker.mapPoint = mapPoint
            marker.markerType = MapPOIItem.MarkerType.BluePin
            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin

            // 설정한 메소드를 marker에 적용함.
            mapView.addPOIItem(marker)

        binding.back.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        val shareButton = findViewById<LinearLayout>(R.id.shareButton)


        //below non kakao api
        val callbutton = findViewById<LinearLayout>(R.id.callButton)

        val placetel = "tel:01094425831"
        callbutton.setOnClickListener{
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(placetel)
            if(intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }
        } //connect call button

        val siteButton = findViewById<LinearLayout>(R.id.siteButton)
        val siteURL = findViewById<TextView>(R.id.siteURL)
        val placeURL = "https://www.mju.ac.kr"
        siteURL.setText(placeURL)
        siteButton.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(placeURL)
            if(intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }
        } //connect hyperLInk
        
        val placeAdressinfo = "서울특별시 서대문구 명지대학교 위치 어쩌구"
        val placeAdress = findViewById<TextView>(R.id.placeAdress)
        val placeAdressButton = findViewById<LinearLayout>(R.id.placeAdressButton)
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        placeAdress.setText(placeAdressinfo)

        placeAdressButton.setOnClickListener{
            clipboard.setPrimaryClip(ClipData(ClipData.newPlainText("label", placeAdressinfo)))
            Toast.makeText(this, "클립보드에 복사되었습니다" , Toast.LENGTH_SHORT).show()
        }

        val backBtn = findViewById<ImageButton>(R.id.back)
        backBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }//connect Top_back button

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
        val mCalloutBalloon: View = inflater.inflate(R.layout.balloon, null)
        val name: TextView = mCalloutBalloon.findViewById(R.id.ball_txt)
        val address: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_address)

        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            // 마커 클릭 시 나오는 말풍선
            name.text = poiItem?.itemName   // 해당 마커의 정보 이용 가능
            address.text = "getCalloutBalloon"
            return mCalloutBalloon
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            // 말풍선 클릭 시
            address.text = "getPressedCalloutBalloon"
            return mCalloutBalloon
        }
    }
}