package io.drdroid.paypalincl.data.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.drdroid.paypalincl.data.model.tv_show.EpisodeModel
import io.drdroid.paypalincl.data.model.tv_show.SeasonModel
import io.drdroid.paypalincl.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val seasons: MutableLiveData<ArrayList<SeasonModel>> by lazy {
        MutableLiveData<ArrayList<SeasonModel>>()
    }

    fun getShowSeasons(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = repository.getSeasons(id)
            if (result.isSuccessful) {
                seasons.postValue(result.body())
            } else {
                seasons.value = arrayListOf()
            }
        }
    }

    val episodes: MutableLiveData<ArrayList<EpisodeModel>> by lazy {
        MutableLiveData<ArrayList<EpisodeModel>>()
    }

    fun getEpisodesSeason(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = repository.getSeasonEpisodes(id)
            if (result.isSuccessful) {
                episodes.postValue(result.body())
            } else {
                seasons.value = arrayListOf()
            }
        }
    }
}