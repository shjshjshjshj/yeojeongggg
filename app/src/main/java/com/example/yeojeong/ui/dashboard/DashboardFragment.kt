package com.example.yeojeong.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yeojeong.BuildConfig
import com.example.yeojeong.MainActivity
import com.example.yeojeong.databinding.FragmentDashboardBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class DashboardFragment : Fragment() {

    val startpoint : String = "0150"
    val endpoint : String = "0200"
    lateinit var mainActivity : MainActivity

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView = binding.textView

        val btn = binding.btn

        btn.setOnClickListener{
            val thread = NetworkThread()
            thread.start()
            thread.join()
        }


        return root
    }

    inner class NetworkThread: Thread(){
        override fun run() {
            var site = "http://apis.data.go.kr/B553766/smt-path/path"
            var apiKey = "?serviceKey=" + BuildConfig.API_KEY
            val another = "&pageNo=3&numOfRows=100&week=DAY&search_type=MINTRF&dept_station_code="+startpoint+"&dest_station_code="+endpoint
            val finalsite : String = site + apiKey + another
            val url = URL(finalsite)
            val conn = url.openConnection()
            val input = conn.getInputStream()
            val isr = InputStreamReader(input)

            val br = BufferedReader(isr)

            var str: String? = null
            val buf = StringBuffer()

            do{
                str = br.readLine()

                if(str!=null){
                    buf.append(str)
                }
            }while (str!=null)

            val root = JSONObject(buf.toString())
            val data = root.getJSONObject("data")


            Log.e("subway", "hihi")

        }
    }

}