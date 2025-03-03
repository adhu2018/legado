package io.legado.app.utils

import io.legado.app.data.appDb

/**
 * 过滤器相关
 */
@Suppress("unused")
object FilterUtils {
    private val filterRules = appDb.filterRuleDao.all

    fun test(name: String): Boolean {
        filterRules.forEach { item ->
            if (item.pattern.isNotEmpty()) {
                if (item.isRegex) {
                    item.regex.matchEntire(name) ?: return true
                } else {
                    return item.pattern == name
                }
            }
        }
        return false
    }
}
