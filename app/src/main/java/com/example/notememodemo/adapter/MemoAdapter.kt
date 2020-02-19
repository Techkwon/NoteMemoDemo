package com.example.notememodemo.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.notememodemo.R
import com.example.notememodemo.model.Memo
import com.example.notememodemo.util.Caller
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.memo_list_item.view.*

class MemoAdapter(private val activity: Activity) : RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {

    private val memoList = ArrayList<Memo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.memo_list_item, parent, false)
        return MemoViewHolder(view)
    }

    override fun getItemCount(): Int = memoList.size

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.tvTitle.text = memoList[position].title

        val id = memoList[position].id
        holder.constraintHolder.setOnClickListener { Caller.callMemoInfo(activity, id) }
    }

    internal fun addMemos(memos: List<Memo>) {
        memoList.clear()
        memoList.addAll(memos)
        notifyDataSetChanged()
    }

    inner class MemoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        val tvTitle: AppCompatTextView = containerView.tv_memo_title_preview
        val constraintHolder: ConstraintLayout = containerView.constraint_holder_memo_item_preview
    }
}