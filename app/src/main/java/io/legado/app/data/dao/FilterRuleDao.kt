package io.legado.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.legado.app.data.entities.FilterRule
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterRuleDao {

    @get:Query("select * from filter_rules order by sortOrder")
    val all: List<FilterRule>

    @get:Query("select * from filter_rules where isEnabled = 1 order by sortOrder")
    val enabled: List<FilterRule>

    @Query("select * from filter_rules order by sortOrder")
    fun flowAll(): Flow<List<FilterRule>>

    @Query("select * from filter_rules where name = :name")
    fun getByName(name: String): FilterRule?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg filterRule: FilterRule)

    @Update
    fun update(vararg filterRule: FilterRule)

    @Delete
    fun delete(vararg filterRule: FilterRule)

    @Query("SELECT * FROM filter_rules where name like :key ORDER BY sortOrder ASC")
    fun flowSearch(key: String): Flow<List<FilterRule>>

    @Query("SELECT * FROM filter_rules WHERE id = :id")
    fun findById(id: Long): FilterRule?

    @get:Query("SELECT MIN(sortOrder) FROM filter_rules")
    val minOrder: Int

    @get:Query("SELECT MAX(sortOrder) FROM filter_rules")
    val maxOrder: Int

}