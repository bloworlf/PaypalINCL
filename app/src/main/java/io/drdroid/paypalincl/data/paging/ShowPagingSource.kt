package io.drdroid.paypalincl.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.drdroid.paypalincl.data.model.tv_show.ShowModel
import io.drdroid.paypalincl.data.repository.Repository

class ShowPagingSource(
    private val repository: Repository
) : PagingSource<Int, ShowModel>() {

    override fun getRefreshKey(state: PagingState<Int, ShowModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShowModel> {
        return try {
            val page = params.key ?: 1
            val response = repository.getShows(page = page)
            if(response.isSuccessful) {
                LoadResult.Page(
                    data = response.body()!!,
                    prevKey = if (page == 1) null else page.minus(1),
                    nextKey = if (response.body()!!.isEmpty()) null else page.plus(1)
                )
            } else{
                LoadResult.Page(
                    data = listOf(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}