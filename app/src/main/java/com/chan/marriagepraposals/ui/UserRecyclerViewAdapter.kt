package com.chan.marriagepraposals.ui

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chan.marriagepraposals.R
import com.chan.marriagepraposals.databinding.UserItemBinding
import com.chan.marriagepraposals.db.User
import com.chan.marriagepraposals.util.Constants.Companion.ACCEPTED
import com.chan.marriagepraposals.util.Constants.Companion.DECLINED
import com.squareup.picasso.Picasso

/**
 * Created by Chan on 12/08/20.
 */
class UserRecyclerViewAdapter(
    val userList: List<User>,
    private var onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayout(), parent, false)
    )

    fun getLayout(): Int {
        return R.layout.user_item
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    interface OnItemClickListener {
        fun onAcceptedClicked(position: Int)
        fun onDeclineClicked(position: Int)
    }

    class UserViewHolder(userItemBinding: UserItemBinding) :
        RecyclerView.ViewHolder(userItemBinding.root) {
        val binding: UserItemBinding = userItemBinding
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        var user = userList.get(position)
        holder.binding.user = user
        if (user.status.equals(ACCEPTED)) {
            holder.binding.llActionButtons.visibility = GONE
            holder.binding.llStatus.visibility = VISIBLE
            holder.binding.tvStatus.text = "Member "+ ACCEPTED
        } else if (user.status.equals(DECLINED)) {
            holder.binding.llActionButtons.visibility = GONE
            holder.binding.llStatus.visibility = VISIBLE
            holder.binding.tvStatus.text = "Member "+ DECLINED
        } else {
            holder.binding.llActionButtons.visibility = VISIBLE
            holder.binding.llStatus.visibility = GONE
        }
        Picasso.get().load(userList.get(position).picture)
            .placeholder(R.drawable.ic_launcher_background).fit().centerInside()
            .into(holder.binding.IvProfilePic)
        holder.binding.btnAccept.setOnClickListener {
            onItemClickListener.onAcceptedClicked(position)
        }
        holder.binding.btnDecline.setOnClickListener {
            onItemClickListener.onDeclineClicked(position)
        }
    }
}
