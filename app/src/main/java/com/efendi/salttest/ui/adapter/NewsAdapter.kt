package com.efendi.salttest.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efendi.salttest.R
import com.efendi.salttest.databinding.ItemNewsBinding
import com.efendi.salttest.models.Article
import com.efendi.salttest.utils.Utils

//class NewsAdapter(private val articles: MutableList<Article>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
class NewsAdapter(
    private val articles: MutableList<Article>,
    private val clickListener: (Article) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isLoading = false

//class NewsAdapter(private val articles: List<Article>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
//        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return NewsViewHolder(binding)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NewsViewHolder(binding)
            }
            VIEW_TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(view)
            }
            else -> throw IllegalArgumentException("Tipe view tidak dikenal")
        }
    }

//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is NewsViewHolder) {
//            holder.bind(articles[position]!!)
//        }
//        // Jika holder adalah instance dari LoadingViewHolder, Anda tidak perlu melakukan apa-apa karena ini hanya tampilan loading
//    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsViewHolder) {
            holder.bind(articles[position], clickListener)
        }
        // Handle for other ViewHolder types if necessary...
    }
    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


//    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
//        holder.bind(articles[position])
//    }

//    override fun getItemCount(): Int = articles.size

    override fun getItemCount(): Int {
        Log.e("getItemCount", ""+articles.size)
//        return articles.size
        return if (isLoading) articles.size + 1 else articles.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == articles.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, clickListener: (Article) -> Unit) {
            binding.titleTextView.text = article.title
            binding.descriptionTextView.text = article.description
            binding.dateTextView.text = Utils.formatIndonesianTime(article.publishedAt)
            binding.authorTextView.text = article.author
            Glide.with(itemView.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.ivArticleImage)
            itemView.setOnClickListener {
                clickListener(article)
            }
        }
    }
}
