package writenow.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "author") val author: Int,
    @ColumnInfo(name = "date_retrieved") val dateRetrieved: Int
)