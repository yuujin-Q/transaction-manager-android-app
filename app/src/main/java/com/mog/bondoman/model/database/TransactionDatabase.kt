package com.mog.bondoman.model.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import com.mog.bondoman.model.Transaction
import com.mog.bondoman.model.converters.DateConverter
import com.mog.bondoman.model.converters.LocationConverter

@TypeConverters(DateConverter::class, LocationConverter::class)
@Database(entities = [Transaction::class], version = 1)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null
        fun getInstance(context: Context): TransactionDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java, "bondoman_transaction"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE ownerId = :ownerId ORDER BY :sortBy ASC")
    fun getAll(ownerId: Long, sortBy: String = "date"): MutableList<Transaction>

    @Insert
    fun insert(vararg transaction: Transaction)

    @Update
    fun update(transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)
}