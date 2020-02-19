package com.example.notememodemo.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notememodemo.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.memo_photo_item.view.*

class PhotosAdapter(private val activity: Activity) : RecyclerView.Adapter<PhotosAdapter.ImageViewHolder>() {

    private val photosUri = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.memo_photo_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = photosUri.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageView = holder.iv_photo
        val uri = photosUri[position]
        Glide.with(activity).load(uri).into(imageView)
    }

    internal fun addPhotoUris(uris: List<String>) {
        photosUri.clear()
        photosUri.addAll(uris)

        notifyDataSetChanged()
    }

    inner class ImageViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        val iv_photo: AppCompatImageView = containerView.iv_photo_picked
    }
}