package io.legado.app.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import io.legado.app.R
import io.legado.app.exception.NoStackTraceException
import io.legado.app.utils.isValid
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import splitties.init.appCtx


@Parcelize
@Entity(
    tableName = "filter_rules",
    indices = [(Index(value = ["id"]))]
)
data class FilterRule(
    @PrimaryKey(autoGenerate = true)
    var id: Long = System.currentTimeMillis(),
    //名称
    @ColumnInfo(defaultValue = "")
    var name: String = "",
    //替换内容
    @ColumnInfo(defaultValue = "")
    var pattern: String = "",
    //是否启用
    @ColumnInfo(defaultValue = "1")
    var isEnabled: Boolean = true,
    //是否正则
    @ColumnInfo(defaultValue = "1")
    var isRegex: Boolean = true,
    //超时时间
    @ColumnInfo(defaultValue = "3000")
    var timeoutMillisecond: Long = 3000L,
    //排序
    @ColumnInfo(name = "sortOrder", defaultValue = "0")
    var order: Int = Int.MIN_VALUE
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other is FilterRule) {
            return other.id == id
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    @delegate:Transient
    @delegate:Ignore
    @IgnoredOnParcel
    val regex: Regex by lazy {
        pattern.toRegex()
    }

    @Throws(NoStackTraceException::class)
    fun checkValid() {
        if (!pattern.isValid(isRegex)) {
            throw NoStackTraceException(appCtx.getString(R.string.rule_invalid))
        }
    }

    fun getValidTimeoutMillisecond(): Long {
        if (timeoutMillisecond <= 0) {
            return 3000L
        }
        return timeoutMillisecond
    }
}