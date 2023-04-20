package com.example.yeojeong.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yeojeong.BuildConfig
import com.example.yeojeong.dataset.Homecarddata
import com.example.yeojeong.`class`.homelayeradapter
import com.example.yeojeong.api.*
import com.example.yeojeong.databinding.FragmentHomeBinding
import com.example.yeojeong.dataset.Homelayerdata
import com.example.yeojeong.login
import com.example.yeojeong.ui.placedetail.placedetailPage
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var baseDate = "20210510"  // 발표 일자
    private var baseTime = "1400"      // 발표 시각
    private var curPoint : Point? = null    // 현재 위치의 격자 좌표를 저장할 포인트
    var toass = 1
    var TourArray : List<ITEM>? = null
    var weatherlate = 1


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.fPlaceButtoon.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, placedetailPage::class.java)
            startActivity(intent)
        })
        val permissionList = arrayOf<String>(
            // 위치 권한
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

        getActivity()?.let { ActivityCompat.requestPermissions(it, permissionList, 1) }

        Log.e("hope", "i dont wanna die")
        //var nLoc = (activity as login).mLastLocation
        //val calnLoc = dfsXyConv(nLoc.latitude, nLoc.longitude)

        //Log.d("geo", (nLoc.longitude + nLoc.latitude).toString())

        val locationtext: TextView = binding.location
        locationtext.setText("서대문구") ;


        val cardView: CardView = binding.cardView
        curPoint = Common().dfsXyConv(login().nowLocationX, login().nowLocationY)
        Log.e("posew", "poi" + curPoint!!.x + "a" + curPoint!!.y)

        cardView.setOnClickListener{
            toass += 1
            requestLocation()
        }

        //below related recycler
        val dataSet = mutableListOf(
            Homecarddata("여정여정", "yodyodyodyod", "http://tong.visitkorea.or.kr/cms/resource/74/1985174_image2_1.jpg"),
            Homecarddata("여3여3여3","여여여여여여영", "http://tong.visitkorea.or.kr/cms/resource/74/1985174_image2_1.jpg")
        )
        /*
        val layer = arrayOf(
            Homelayerdata(
                "Tour Api 표시",
                mutableListOf(Homecarddata("여정여정", "yodyodyodyod", "http://tong.visitkorea.or.kr/cms/resource/74/1985174_image2_1.jpg"))
            )
        )
        binding.recoLayer.adapter = homelayeradapter(requireContext(), layer)
        binding.recoLayer.layoutManager = LinearLayoutManager(requireContext()) */


        /*
        val recyclerView: RecyclerView = recyclerbinding.homeFirstCardList
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        val homelayerrecyclerView : RecyclerView = binding.recoLayer
        val layerLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)


        homelayerrecyclerView.layoutManager = layerLayoutManager
        homelayerrecyclerView.adapter = homeLayerAdapter(layer)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = homeCardAdapter(dataSet) */

        requestLocation()

        return root


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 날씨 가져와서 설정하기
    private fun setWeather(nx : Int, ny : Int) {
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        val cal = Calendar.getInstance()
        baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각
        val timeM = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 분
        // API 가져오기 적당하게 변환
        baseTime = Common().getBaseTime(timeH, timeM)
        // 현재 시각이 00시이고 45분 이하여서 baseTime이 2330이면 어제 정보 받아오기
        if (timeH == "00" && baseTime == "2330") {
            cal.add(Calendar.DATE, -1).toString()
            baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }
        Log.e("time", "time"+baseTime+"a"+baseDate)

        val call = WeatherObject.getRetrofitService().getWeather(60, 1, "JSON", baseDate, baseTime, nx, ny)

        call.enqueue(object : Callback<WEATHER> {
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                Log.e("ldld", "lostaaa")
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    val it: List<wITEM> = response.body()!!.response.body.items.item

                    // 현재 시각부터 1시간 뒤의 날씨 6개를 담을 배열
                    val weatherArr = arrayOf(modelweather(), modelweather(), modelweather(), modelweather(), modelweather(), modelweather())

                    Log.e("Arr", "moArr" + weatherArr)


                    // 배열 채우기
                    var index = 0
                    val totalCount = response.body()!!.response.body.totalCount - 1
                    for (i in 0..totalCount) {
                        index %= 6
                        when(it[i].category) {
                            "PTY" -> weatherArr[index].rainType = it[i].fcstValue     // 강수 형태
                            "REH" -> weatherArr[index].humidity = it[i].fcstValue     // 습도
                            "SKY" -> weatherArr[index].sky = it[i].fcstValue          // 하늘 상태
                            "T1H" -> weatherArr[index].temp = it[i].fcstValue         // 기온
                            else -> continue
                        }
                        index++
                    }
                    Log.e("result","rere" + weatherArr[1] + "a" + weatherArr[0])
                    Log.e("nowtemp", "now is " + weatherArr[0].temp)
                    //temptext.setText(weatherArr[0].temp)

                    weatherArr[0].fcstTime = "지금"
                    // 각 날짜 배열 시간 설정
                    for (i in 1..5) weatherArr[i].fcstTime = it[i].fcstTime
                    val tempText = binding.temp
                    tempText.setText(weatherArr[0].temp)


                    Toast.makeText(activity, "날씨가 새로고침되었습니다", Toast.LENGTH_SHORT).show()


                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })

    }


    //Call Tour Api related Pos
    private fun getTourPos(nx : String, ny : String, CotID : String) {



        val call = tourObject.getTourService().getTourNowPos( "AND", "YeoJeong", "json", nx, ny, "2000", CotID, BuildConfig.API_KEY, "A")
        Log.e("checking", "check"+call)
        // 비동기적으로 실행하기
        call.enqueue(object : Callback<TOURPOS> {
            // 응답 성공 시
            override fun onResponse(call: Call<TOURPOS>, response: Response<TOURPOS>) {
                if (response.isSuccessful) {
                    Log.e("readytourss ", "ready")
                    val it: List<ITEM> = response.body()!!.response.body.items.item

                    TourArray = response.body()!!.response.body.items.item

                    val totalCount = response.body()!!.response.body.totalCount - 1

                    Log.e("bodyView", TourArray!![0].title+"and"+totalCount)

                    val dataSet = mutableListOf<Homecarddata>()


                    for (i in 0..totalCount){
                        //don't view no image card
                        if (TourArray!![i].firstimage != ""){
                            dataSet.add(Homecarddata(TourArray!![i].title, "desdes", TourArray!![i].firstimage))
                            }
                        val layer = arrayOf(
                            Homelayerdata("Tour Api 표시", dataSet),
                            Homelayerdata("2번째 표시", dataSet)
                        )

                        binding.recoLayer.adapter = homelayeradapter(requireContext(), layer)
                        binding.recoLayer.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        }

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<TOURPOS>, t: Throwable) {
                Log.d("tourapi fail", t.message.toString())
            }
        })
    }

    @SuppressLint("MissingPermission")
        private fun requestLocation() {
            val locationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }
            try {
                // 나의 현재 위치 요청
                val locationRequest = LocationRequest.create()
                locationRequest.run {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = 60 * 10000    // 요청 간격(10초)
                }
                val locationCallback = object : LocationCallback() {
                    // 요청 결과
                    override fun onLocationResult(p0: LocationResult) {
                        p0.let {
                            for (location in it.locations) {


                                // 현재 위치의 위경도를 격자 좌표로 변환
                                curPoint = Common().dfsXyConv(location.latitude, location.longitude)
                                Log.e("testtest", "test"+curPoint!!.x + "a" + curPoint!!.y)
                                // nx, ny지점의 날씨 가져와서 설정하기
                                setWeather(curPoint!!.x, curPoint!!.y)
                                Log.e("pospos", location.longitude.toString() + " bbbb" + location.latitude.toString())
                                getTourPos(location.longitude.toString(), location.latitude.toString(), "12")
                                Log.e("start", "stst")
                            }
                        }
                    }
                }

                // 내 위치 실시간으로 감지
                if (locationClient != null) {
                    Looper.myLooper()?.let {
                        locationClient.requestLocationUpdates(locationRequest, locationCallback,
                            it)
                    }
                }


            } catch (e : SecurityException) {
                e.printStackTrace()
            }
        }
}