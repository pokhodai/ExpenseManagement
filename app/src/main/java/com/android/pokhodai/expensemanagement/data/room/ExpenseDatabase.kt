package com.android.pokhodai.expensemanagement.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.android.pokhodai.expensemanagement.data.room.entities.ExpenseEntity
import com.android.pokhodai.expensemanagement.data.room.entities.WalletEntity
import com.android.pokhodai.expensemanagement.data.room.entities.WalletFtsEntity
import com.android.pokhodai.expensemanagement.data.service.ExpenseDao
import com.android.pokhodai.expensemanagement.data.service.WalletDao
import com.android.pokhodai.expensemanagement.data.service.WalletFtsDao
import com.android.pokhodai.expensemanagement.utils.LocalDateFormatter

@Database(entities = [WalletEntity::class, ExpenseEntity::class, WalletFtsEntity::class], version = 4)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase: RoomDatabase() {

    abstract fun walletDao(): WalletDao

    abstract fun expenseDao(): ExpenseDao

    abstract fun walletFtsDao(): WalletFtsDao

    companion object{
        const val DATABASE_NAME: String = "expense_db"
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateFormatter {
        return LocalDateFormatter.from(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateFormatter): Long {
        return date.timeInMillis()
    }
}
