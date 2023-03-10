package com.android.pokhodai.expensemanagement.repositories

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.android.pokhodai.expensemanagement.data.room.entities.WalletEntity
import com.android.pokhodai.expensemanagement.data.service.WalletDao
import com.android.pokhodai.expensemanagement.ui.home.source.WalletPagingSource
import com.android.pokhodai.expensemanagement.utils.LocalDateFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val walletDao: WalletDao
) {
    suspend fun getAllWallets() = walletDao.getAll()

    suspend fun insertAllWallet(vararg wallets: WalletEntity) = walletDao.insertAll(*wallets)

    suspend fun sumByType(type: String, date: String) = walletDao.sumAmountByType(type, date)

    suspend fun sumByPublicatedAt(dateFormat: String) = walletDao.sumAmountByPublicateAt(dateFormat)

    suspend fun deleteWalletById(id: Int) = walletDao.deleteWalletById(id)

    suspend fun getStatisticsByTypeAndDate(type: String, date: String) =
        walletDao.getStatisticsByTypeAndDate(type, date)

    suspend fun getWalletsCountByMonthAndYear(type: String, date: String) =
        walletDao.getWalletsCountByMonthAndYear(type, date)

    suspend fun updateWallet(walletEntity: WalletEntity) {
        walletDao.update(walletEntity)
    }

    suspend fun deleteAll() = walletDao.deleteAll()

    fun getPaginationWalletsByDate(date: String) = Pager(
        PagingConfig(
            pageSize = 30,
            initialLoadSize = 30,
            prefetchDistance = 5,
            maxSize = 60,
            enablePlaceholders = false
        )
    ) {
        WalletPagingSource(date = date, walletDao = walletDao)
    }
}