package io.drdroid.paypalincl.data.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.drdroid.paypalincl.data.model.tv_show.SearchItemModel
import io.drdroid.paypalincl.data.model.tv_show.ShowModel
import io.drdroid.paypalincl.data.paging.ShowPagingSource
import io.drdroid.paypalincl.data.repository.Repository
import io.drdroid.paypalincl.data.utils.DistinctUntilChangedMutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val searchLiveData: MutableLiveData<ArrayList<SearchItemModel>> by lazy {
        MutableLiveData<ArrayList<SearchItemModel>>()
    }
    val showLiveData by lazy { DistinctUntilChangedMutableLiveData<ArrayList<ShowModel>>() }
    val genreLiveData by lazy { MutableLiveData<List<String>>() }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isRefreshing: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

//    var showsFlow: Flow<PagingData<ShowModel>> = flowOf()

    fun getShows(): Flow<PagingData<ShowModel>> {
        isLoading.value = true
        val pager = Pager(
            pagingSourceFactory = {
                ShowPagingSource(
                    repository = repository
                )
            },
            config = PagingConfig(
                pageSize = 10
            )
        ).flow
            .cachedIn(viewModelScope)
        isLoading.value = false
        return pager
    }

//    fun getShows() {
//        isLoading.value = true
//        val pager = Pager(
//            pagingSourceFactory = {
//                ShowPagingSource(
//                    repository = repository
//                )
//            },
//            config = PagingConfig(
//                pageSize = 10
//            )
//        ).flow
//            .cachedIn(viewModelScope)
//        isLoading.value = false
//        showsFlow.
//    }

    fun refresh(): Flow<PagingData<ShowModel>> {
        isRefreshing.value = true
        val pager = getShows()
        isRefreshing.value = false
        return pager
    }

    fun search(q: String) {
        viewModelScope.launch {
            isLoading.value = true
            val value = repository.search(q = q)
            if (value.isSuccessful) {
                searchLiveData.postValue(value.body())
            } else {
                searchLiveData.value = arrayListOf()
            }
            isLoading.value = false
        }
    }

//    fun getGenres() {
//        viewModelScope.launch {
//            if (showLiveData.value == null) {
//                getShows()
//            } else {
//                genreLiveData.value = try {
//                    showLiveData.value?.asSequence()?.filter { it.genres.isNotEmpty() }
//                        ?.map { showModel -> showModel.genres }?.map { l -> l.first() }?.toSet()
//                        ?.toList()
//                } catch (e: Exception) {
//                    listOf()
//                }
//            }
//        }
//    }
}