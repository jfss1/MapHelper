package intro.android.cm.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import intro.android.cm.entities.Nota

@Dao
interface DaoNota {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Query("SELECT * FROM NOTA_TABLE ORDER BY TITULO ASC")
    fun getAllNotasOrder(): LiveData<List<Nota>>

    @Query("SELECT * FROM NOTA_TABLE WHERE TITULO == :titulo" )
    suspend fun getFromTitulo(titulo: String) : LiveData<Nota>

    @Update
    suspend fun updateNota(nota: Nota)

    @Query("UPDATE NOTA_TABLE SET TITULO = :titulo AND DESCRICAO = :descricao WHERE ID == :id")
    suspend fun updateFromID(id: Int?, titulo: String, descricao: String)

    @Query("UPDATE NOTA_TABLE SET DESCRICAO = :descricao WHERE TITULO == :titulo")
    suspend fun updateFromTitle(titulo: String, descricao: String)

    @Query("DELETE FROM NOTA_TABLE ")
    suspend fun deleteAll()

    @Query("DELETE FROM NOTA_TABLE WHERE TITULO == :titulo")
    suspend fun deleteFromTitulo(titulo: String)




}