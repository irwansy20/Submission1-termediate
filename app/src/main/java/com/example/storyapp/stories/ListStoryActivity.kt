package com.example.storyapp.stories

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.view.DetailStrories
import com.example.storyapp.view.LoginActivity
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityListStoryBinding
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.view.AddStoryActivity

class ListStoryActivity: AppCompatActivity() {
    private lateinit var binding: ActivityListStoryBinding
    private lateinit var listViewmodel: ListStoryViewModel
    private lateinit var rvList: RecyclerView

    private lateinit var userPreferences: SharedPreferences
    private lateinit var name: String

    lateinit var storyAdapter: StoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvList = findViewById(R.id.rv_list)
        rvList.setHasFixedSize(true)

        listViewmodel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(ListStoryViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.rvList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this,layoutManager.orientation)
        binding.rvList.addItemDecoration(itemDecoration)

        userPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE)
        name = userPreferences.getString(LoginActivity.NAME, "").toString()
        val token = userPreferences.getString(LoginActivity.TOKEN, "").toString()

        searchListItem(token)


        listViewmodel.getListStoryItem().observe(this) {
            if(it != null){
                storyAdapter.setData(it)
                showLoading(false)
            }
        }
        binding.salam.text = "Hello, $name"

        binding.photoAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        setupRecycleView()
    }

    private fun setupRecycleView() {
        storyAdapter = StoryAdapter()
        rvList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = storyAdapter
        }
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback{
            override fun onItemClicked(item: ListStoryItem) {
                showSelectedUser(item.name, item.photoUrl, item.description)
            }
        })
    }

    private fun showSelectedUser(name: String, photoUrl: String, description: String) {
        val moveIntent = Intent(this@ListStoryActivity, DetailStrories::class.java)
        moveIntent.putExtra(DetailStrories.KEY_NAME, name)
        moveIntent.putExtra(DetailStrories.KEY_AVA, photoUrl)
        moveIntent.putExtra(DetailStrories.KEY_DESC, description)
        startActivity(moveIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit -> {
                userPreferences.edit().apply {
                    clear()
                    apply()
                }
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
                return true
            }
            R.id.bahasa -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            else -> false
        }
    }

    private fun searchListItem(token: String){
        showLoading(true)
        listViewmodel.getListStoryItem(token)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setMessage(resources.getString(R.string.dialEx))

            setPositiveButton(resources.getString(R.string.respY)){dialog,which ->
               finishAffinity()
            }
            setNegativeButton(resources.getString(R.string.respT)){dialog,which ->
                dialog.cancel()
            }
            setCancelable(false)
        }.create().show()
    }
}