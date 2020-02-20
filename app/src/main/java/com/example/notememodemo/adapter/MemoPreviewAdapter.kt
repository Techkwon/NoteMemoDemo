package com.example.notememodemo.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notememodemo.R
import com.example.notememodemo.model.Memo
import com.example.notememodemo.util.Caller
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.memo_preview_item.view.*

class MemoPreviewAdapter(private val activity: Activity) : RecyclerView.Adapter<MemoPreviewAdapter.MemoViewHolder>() {

    private val memoList = ArrayList<Memo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.memo_preview_item, parent, false)
        return MemoViewHolder(view)
    }

    override fun getItemCount(): Int = memoList.size

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val id = memoList[position].id

        setViews(holder, position)

        holder.constraintHolder.setOnClickListener { Caller.callMemoInfo(activity, id) }
    }

    private fun setViews(holder: MemoViewHolder, position: Int) {
        memoList[position].run {
            holder.tvTitle.text = this.title
            holder.tvMemo.text = this.content

            if (this.photos.items.isEmpty()) {
                holder.photoHolder.visibility = View.GONE
            } else {
                holder.photoHolder.visibility = View.VISIBLE
                val photoUri = this.photos.items[0]
                Glide.with(activity).load(photoUri).into(holder.ivPhoto)
            }
        }
    }

    internal fun addMemos(memos: List<Memo>) {
        memoList.clear()
        memoList.addAll(memos)
        notifyDataSetChanged()
    }

    inner class MemoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        val constraintHolder: ConstraintLayout = containerView.constraint_holder_memo_item_preview
        val photoHolder: ConstraintLayout = containerView.constraint_holder_item_right
        val tvTitle: AppCompatTextView = containerView.tv_memo_title_preview
        val tvMemo: AppCompatTextView = containerView.tv_memo_content_preview
        val ivPhoto: AppCompatImageView = containerView.iv_photo_preview
    }
}