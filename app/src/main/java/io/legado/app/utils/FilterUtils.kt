package io.legado.app.utils

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
                    item.regex.containsMatchIn(name)
                } else {
                    item.pattern == name
                }
            }
        }
        return false
    }
}
