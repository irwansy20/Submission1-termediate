package com.example.storyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityDetailStroriesBinding

class DetailStrories : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailStroriesBinding

    companion object {
        const val KEY_NAME = "name"
        const val KEY_DESC = "description"
        const val KEY_AVA = "photoUrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailStroriesBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val name = intent.getStringExtra(KEY_NAME).toString()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_back)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        supportActionBar?.title = name

        setData()
    }

    private fun setData() {
        val name = intent.getStringExtra(KEY_NAME).toString()
        val photoUrl = intent.getStringExtra(KEY_AVA).toString()
        val description = intent.getStringExtra(KEY_DESC).toString()

        detailBinding.apply {
            Glide.with(this@DetailStrories)
                .load(photoUrl)
                .into(detailBinding.imgPhoto)
            tvName.text = name
            tvDescContent.text = description
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}