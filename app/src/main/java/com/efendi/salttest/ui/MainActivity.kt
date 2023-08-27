package com.efendi.salttest.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.efendi.salttest.R
import com.efendi.salttest.apis.RetrofitClient
import com.efendi.salttest.ui.adapter.NewsAdapter
import com.efendi.salttest.models.Article
import com.efendi.salttest.respons_api.NewsResponse
import com.efendi.salttest.utils.Utils.Constants.Companion.apiKey
import com.efendi.salttest.utils.Utils.Constants.Companion.country
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val visibleThreshold = 10
    private var page = 1

    private lateinit var adapter: NewsAdapter
    private lateinit var parentView: ConstraintLayout
    private val newsList = mutableListOf<Article>()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        parentView = findViewById(R.id.parentView)
//        adapter = NewsAdapter(newsList)
        adapter = NewsAdapter(newsList) { article ->
            val intent = Intent(this@MainActivity, DetailNewsActivity::class.java)
            intent.putExtra("URL", article.url)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.isRefreshing = true
        getNews()

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getNews()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!adapter.isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    Log.e(TAG, "Loadmore");
                    loadMoreItems()
                }
            }
        })


    }

    fun loadMoreItems() {
        adapter.isLoading = true
        adapter.notifyItemInserted(newsList.size - 1)
        page++
        val call = RetrofitClient.instance.getTopHeadlines(country, apiKey, page)
        call.enqueue(object : Callback<NewsResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                Log.e("loadMoreItems", "onResponse: "+response.body().toString())
                swipeRefreshLayout.isRefreshing = false
                adapter.isLoading = false
                if (response.isSuccessful && response.body() != null) {
                    newsList.addAll(response.body()!!.articles)
                    adapter.notifyItemRemoved(newsList.size - 1)
                }else{
                    Log.e("loadMoreItems", "onResponse: "+response.message().toString())
                }
                    adapter.notifyDataSetChanged()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                adapter. isLoading = false
                adapter.notifyDataSetChanged()
                Snackbar.make(parentView,t.message.toString(),Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun getNews() {
        val call = RetrofitClient.instance.getTopHeadlines(country, apiKey, page)
        call.enqueue(object : Callback<NewsResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                Log.e("getNews", "onResponse: "+response.body().toString())
                adapter.isLoading = false
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful && response.body() != null) {
                    newsList.clear()
                    newsList.addAll(response.body()!!.articles)
                    adapter.notifyItemRemoved(newsList.size - 1)
                }else{
                    Snackbar.make(parentView,"No data to display",Snackbar.LENGTH_LONG).show()
                }
                    adapter.notifyDataSetChanged()
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Snackbar.make(parentView,t.message.toString(),Snackbar.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false

                adapter.isLoading = false
                adapter.notifyDataSetChanged()


            }
        })
    }

}
