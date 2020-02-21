package com.example.notememodemo.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.notememodemo.R
import com.example.notememodemo.util.Caller
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.memo_photo_item.view.*

class PhotosAdapter(private val activity: Activity) : RecyclerView.Adapter<PhotosAdapter.ImageViewHolder>() {

    private val photoUris = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.memo_photo_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = photoUris.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageView = holder.ivPhoto
        val uri = photoUris[position]

        Caller.displayImage(activity, imageView, uri)
    }

    internal fun addPhotoUris(uris: List<String>) {
        photoUris.clear()
        photoUris.addAll(uris)

        notifyDataSetChanged()
    }

    inner class ImageViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        val ivPhoto: AppCompatImageView = containerView.iv_photos
    }
}