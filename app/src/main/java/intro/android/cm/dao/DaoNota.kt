package intro.android.cm.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import intro.android.cm.entities.Nota

@Dao
interface DaoNota {

    @Query("SELECT * FROM note_table ORDER BY title ASC")
    fun getAllNotes(): LiveData<List<Nota>>

    @Query("SELECT * FROM note_table WHERE title ==:title")
    fun getTitleFromNotes(title:String):LiveData<Nota>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(title: Nota)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Query("DELETE FROM note_table WHERE title == :title")
    suspend fun deleteByTittle(title:String)

    @Update
    suspend fun updateNote(title:Nota)

    @Query("UPDATE note_table SET description=:description WHERE title ==:title")
    suspend fun updateDescriptionFromTitle(title: String,description: String)

    @Query("UPDATE note_table SET description=:description , title=:title WHERE id ==:id")
    suspend fun updateNoteFromId(id:Int,title: String,description: String)
}