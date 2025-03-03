package io.legado.app.ui.filter

import android.app.Application
import io.legado.app.base.BaseViewModel
import io.legado.app.data.appDb
import io.legado.app.data.entities.FilterRule

/**
 * 过滤器规则数据修改
 */
class FilterRuleViewModel(application: Application) : BaseViewModel(application) {

    fun initData(onSuccess: (List<FilterRule>) -> Unit) {
        execute {
            appDb.filterRuleDao.enabled
        }.onSuccess {
            onSuccess.invoke(it)
        }
    }

    fun update(vararg rule: FilterRule) {
        execute {
            appDb.filterRuleDao.update(*rule)
        }
    }

    fun delete(rule: FilterRule) {
        execute {
            appDb.filterRuleDao.delete(rule)
        }
    }

    fun upOrder() {
        execute {
            val rules = appDb.filterRuleDao.all
            for ((index, rule) in rules.withIndex()) {
                rule.order = index + 1
            }
            appDb.filterRuleDao.update(*rules.toTypedArray())
        }
    }

    fun enableSelection(rules: List<FilterRule>) {
        execute {
            val array = Array(rules.size) {
                rules[it].copy(isEnabled = true)
            }
            appDb.filterRuleDao.update(*array)
        }
    }

    fun disableSelection(rules: List<FilterRule>) {
        execute {
            val array = Array(rules.size) {
                rules[it].copy(isEnabled = false)
            }
            appDb.filterRuleDao.update(*array)
        }
    }

    fun delSelection(rules: List<FilterRule>) {
        execute {
            appDb.filterRuleDao.delete(*rules.toTypedArray())
        }
    }

    fun toTop(rule: FilterRule) {
        execute {
            rule.order = appDb.filterRuleDao.minOrder - 1
            appDb.filterRuleDao.update(rule)
        }
    }

    fun toBottom(rule: FilterRule) {
        execute {
            rule.order = appDb.filterRuleDao.maxOrder + 1
            appDb.filterRuleDao.update(rule)
        }
    }
}
