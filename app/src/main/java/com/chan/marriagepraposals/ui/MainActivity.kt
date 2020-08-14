package com.chan.marriagepraposals.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chan.marriagepraposals.R
import com.chan.marriagepraposals.UserViewModel
import com.chan.marriagepraposals.databinding.ActivityMainBinding
import com.chan.marriagepraposals.db.AppDatabase
import com.chan.marriagepraposals.db.AppDatabase.Companion.destroyDBInstance
import com.chan.marriagepraposals.db.User
import com.chan.marriagepraposals.util.Constants.Companion.ACCEPTED
import com.chan.marriagepraposals.util.Constants.Companion.DECLINED
import com.chan.marriagepraposals.util.Constants.Companion.RESULT_COUNT


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: UserViewModel
    lateinit var appDatabase: AppDatabase
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: UserRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getDatabase(this)!!
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        performDataBinding()
        viewModel.setDBInstance(appDatabase)
        binding.pbLoading.visibility = VISIBLE
        observeLiveData()
        viewModel.getUserDataServer(RESULT_COUNT)
    }

    private fun performDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setUserViewModel(viewModel)
    }

    fun observeLiveData() {
        viewModel.usersLiveData.observe(this, Observer { users ->
            binding.pbLoading.visibility = GONE
            initView(users)
        })
    }

    fun initView(users: List<User>) {
        val onItemClickListener: UserRecyclerViewAdapter.OnItemClickListener =
            object : UserRecyclerViewAdapter.OnItemClickListener {
                override fun onAcceptedClicked(position: Int) {
                    viewModel.updateUserStatus(users.get(position).uid, ACCEPTED)
                    adapter.notifyItemChanged(position)
                }

                override fun onDeclineClicked(position: Int) {
                    viewModel.updateUserStatus(users.get(position).uid, DECLINED)
                    adapter.notifyItemChanged(position)
                }

            }
        adapter = UserRecyclerViewAdapter(users, onItemClickListener)
        binding.rvUser.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDBInstance()
    }

}