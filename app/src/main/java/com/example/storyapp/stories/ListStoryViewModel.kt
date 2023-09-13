package com.example.storyapp.stories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.response.AllStoryResponse
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoryViewModel: ViewModel() {
    private val _StoryItem = MutableLiveData<List<ListStoryItem>>()
    private val _isLoading = MutableLiveData<Boolean>()


    fun getListStoryItem(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStories("Bearer "+ token)
        client.enqueue(object : Callback<AllStoryResponse> {
            override fun onResponse(
                call: Call<AllStoryResponse>,
                response: Response<AllStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _StoryItem.postValue(response.body()?.listStory)
                }
            }

            override fun onFailure(call: Call<AllStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("FETCHINGSTORIES", t.message.toString())
            }

        })
    }

    fun getListStoryItem(): LiveData<List<ListStoryItem>> {
        return _StoryItem
    }
}