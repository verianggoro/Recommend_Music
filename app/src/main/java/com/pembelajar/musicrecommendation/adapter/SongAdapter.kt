package com.pembelajar.musicrecommendation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pembelajar.musicrecommendation.R
import com.pembelajar.musicrecommendation.databinding.ItemRecommendSongBinding
import com.pembelajar.musicrecommendation.model.DataList
import com.pembelajar.musicrecommendation.model.Song

class SongAdapter(
    private val context: Context?,
    private val dataSong : List<DataList>?
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var onCallBack: onItemCallback? = null

    fun setOnClicked(onItemCallback: onItemCallback){
        this.onCallBack = onItemCallback
    }


    inner class ViewHolder(private val binding: ItemRecommendSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            if (dataSong!= null){
                binding.txtSongName.text = dataSong[position].songName
                binding.txtAccurate.text =String.format(binding.root.resources.getString(R.string.txt_accurate), dataSong[position].accurate)
                binding.btnPlay.setOnClickListener{onCallBack?.itemClickPlay(dataSong[position])}
            }else{
                binding.txtSongName.text = "NULL"
                binding.txtAccurate.text = "NULL"
                binding.btnPlay.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemRecommendSongBinding = DataBindingUtil.inflate(inflater, R.layout.item_recommend_song, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dataSong!!.size
    }

    interface onItemCallback{
        fun itemClickPlay(songId: DataList)
    }
}