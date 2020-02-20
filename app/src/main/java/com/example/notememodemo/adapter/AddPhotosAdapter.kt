package com.example.notememodemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notememodemo.R
import com.example.notememodemo.view.AddMemoActivity
import com.example.notememodemo.viewmodel.MemoViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.memo_add_photo_item.view.*

class AddPhotosAdapter(private val activity: AddMemoActivity,
                       private val viewModel: MemoViewModel) : RecyclerView.Adapter<AddPhotosAdapter.ImageViewHolder>() {

    private val photoUris = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.memo_add_photo_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = photoUris.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageView = holder.ivPhoto
        val uri = photoUris[position]
        Glide.with(activity).load(uri).into(imageView)

        holder.ibRemove.setOnClickListener { removePhoto(position) }
    }

    private fun removePhoto(position: Int) {
        viewModel.removePhotoFromList(position)
    }

    internal fun updateList(uris: List<String>) {
        photoUris.clear()
        photoUris.addAll(uris)
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        val ivPhoto: AppCompatImageView = containerView.iv_photo_picked
        val ibRemove: AppCompatImageButton = containerView.ib_remove_photo
    }
}