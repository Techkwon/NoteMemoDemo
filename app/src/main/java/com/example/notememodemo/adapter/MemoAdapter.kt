package com.example.notememodemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notememodemo.R
import com.example.notememodemo.data.Memo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.memo_list_item.view.*

class MemoAdapter : RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {

    private val memoList = ArrayList<Memo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.memo_list_item, parent, false)
        return MemoViewHolder(view)
    }

    override fun getItemCount(): Int = memoList.size

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.tvTitle.text = memoList[position].title
    }

    internal fun addMemos(memos: List<Memo>) {
        memoList.clear()
        memoList.addAll(memos)
        notifyDataSetChanged()
    }

    inner class MemoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        val tvTitle: AppCompatTextView = containerView.tv_memo_title_preview

    }
}