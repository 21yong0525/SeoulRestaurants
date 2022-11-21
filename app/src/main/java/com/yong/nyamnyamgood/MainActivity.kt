package com.yong.nyamnyamgood

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.yong.nyamnyamgood.data.Library
import com.yong.nyamnyamgood.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        val DB_NAME = "storesDB"
        var VERSION = 3
        var lang: String? = null
    }

    private var storeList: MutableList<DataVOStore>? = mutableListOf()
    lateinit var dataVOStore: DataVOStore
    lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = DBHelper(this, MainActivity.DB_NAME, MainActivity.VERSION)

        if (lang==null){
            lang = dbHelper.languageGet()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(SeoulOpenApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(SeoulOpenService::class.java)
        
        // DB 저장되어있는 언어로 리스트 보여줌
        dbHelper.selectLang(lang!!)?.let { storeList?.addAll(it) }
        adapter = CustomAdapter(storeList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(Decoration(binding.root.context))
        adapter.notifyDataSetChanged()
        
        val tab1: TabLayout.Tab = binding.tabLayout.newTab()
        tab1.text = "홈"        
        val tab2: TabLayout.Tab = binding.tabLayout.newTab()
        tab2.text = "찜"

        binding.tabLayout.addTab(tab1)
        binding.tabLayout.addTab(tab2)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "홈" -> {
                        binding.fabLang.visibility = View.VISIBLE
                        storeList?.clear()
                        dbHelper.selectLang(lang!!)?.let { storeList?.addAll(it) }
                        adapter.notifyDataSetChanged()
                    }
                    "찜" -> {
                        binding.fabLang.visibility = View.INVISIBLE
                        storeList?.clear()
                        dbHelper.selectStar(lang!!)?.let { storeList?.addAll(it) }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // fab 버튼 누르면 언어설정
        binding.fabLang.setOnClickListener {
            val dialogLang = CustomDialogLang(binding.root.context)
            dialogLang.showDialog()
        }

        if (storeList?.size == 0) {
            service.getLibrarys(SeoulOpenApi.API_KEY, SeoulOpenApi.LIMIT).enqueue(object : Callback<Library> {
                    override fun onResponse(call: Call<Library>, response: Response<Library>) {
                        val data = response.body()
                        data?.let {
                            for (loadData in it.TbVwRestaurants.row) {
                                val language = loadData.LANG_CODE_ID.replace("'", "")
                                val storeName = loadData.POST_SJ.replace("'", "")
                                val address = loadData.ADDRESS.replace("'", "")
                                val telNo = loadData.CMMN_TELNO.replace("'", "")
                                val url = loadData.CMMN_HMPG_URL.replace("'", "")
                                val useTime = loadData.CMMN_USE_TIME.replace("'", "")
                                val subwayInfo = loadData.SUBWAY_INFO.replace("'", "")
                                val reprsntMenu = loadData.FD_REPRSNT_MENU.replace("'", "")
                                dataVOStore = DataVOStore(
                                    language,
                                    storeName,
                                    address,
                                    telNo,
                                    url,
                                    useTime,
                                    subwayInfo,
                                    reprsntMenu,
                                    0
                                )
                                dbHelper.insertStore(dataVOStore)
                                storeList?.add(dataVOStore)
                                adapter.notifyDataSetChanged()
                                dbHelper.insertlanguage("ko")
                            }
                        } ?: let {
                            Log.d("nyamnyamgood", "음식점 정보 없음")
                            Toast.makeText(this@MainActivity, "음식점 정보가 없습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    override fun onFailure(call: Call<Library>, t: Throwable) {
                        Log.d("nyamnyamgood", "음식점 로딩 오류 ${t.printStackTrace()}")
                        Toast.makeText(this@MainActivity, "음식점 로딩 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
            })
        }
    }

    // 언어 설정 후 리스트 재설정
    fun changeLang(){
        storeList?.clear()
        val dbHelper = DBHelper(this, MainActivity.DB_NAME, MainActivity.VERSION)
        dbHelper.selectLang(lang!!)?.let { storeList?.addAll(it) }
        adapter.notifyDataSetChanged()
    }
}