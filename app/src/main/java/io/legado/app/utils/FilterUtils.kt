package io.legado.app.utils

import io.legado.app.constant.AppLog
import io.legado.app.data.appDb

/**
 * 过滤器相关
 */
@Suppress("unused")
object FilterUtils {
    private val filterRules = appDb.filterRuleDao.enabled

    fun test(name: String): Boolean {
        filterRules.forEach { item ->
            if (item.pattern.isNotEmpty()) {
                return if (item.isRegex) {
                    AppLog.put("filter by regex. \nregex: `${item.pattern}` \nname: $name \nresult: ${item.regex.containsMatchIn(name)}")
                    item.regex.containsMatchIn(name)
                } else {
                    AppLog.put("filter by string. \nstring: `${item.pattern}` \nname: $name \nresult: ${item.pattern == name}")
                    item.pattern == name
                }
            }
        }
        return false
    }
}
