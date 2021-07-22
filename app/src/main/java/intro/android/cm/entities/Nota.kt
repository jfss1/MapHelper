package intro.android.cm.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")

class Nota (
        @PrimaryKey(autoGenerate = true) val id: Int?= null,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "description") val description: String


)