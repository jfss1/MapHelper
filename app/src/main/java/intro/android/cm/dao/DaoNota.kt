package intro.android.cm.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import intro.android.cm.entities.Nota

@Dao
interface DaoNota {

    @Query("SELECT * FROM NOTA_TABLE ORDER BY TITULO ASC")
    fun getAlphabetizedTitulos(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Query("DELETE FROM NOTA_TABLE")
    suspend fun deleteAll()
}