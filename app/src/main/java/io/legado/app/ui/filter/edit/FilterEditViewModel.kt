package io.legado.app.ui.filter.edit

import android.app.Application
import android.content.Intent
import io.legado.app.base.BaseViewModel
import io.legado.app.constant.AppLog
import io.legado.app.data.appDb
import io.legado.app.data.entities.FilterRule
import io.legado.app.utils.GSON
import io.legado.app.utils.fromJsonObject
import io.legado.app.utils.getClipText
import io.legado.app.utils.toastOnUi

class FilterEditViewModel(application: Application) : BaseViewModel(application) {

    var filterRule: FilterRule? = null

    fun initData(intent: Intent, finally: (filterRule: FilterRule) -> Unit) {
        execute {
            val id = intent.getLongExtra("id", -1)
            filterRule = if (id > 0) {
                appDb.filterRuleDao.findById(id)
            } else {
                val name = intent.getStringExtra("name") ?: ""
                val pattern = intent.getStringExtra("pattern") ?: ""
                val isRegex = intent.getBooleanExtra("isRegex", false)
                FilterRule(
                    name = name,
                    pattern = pattern,
                    isRegex = isRegex
                )
            }
        }.onFinally {
            filterRule?.let {
                finally(it)
            }
        }
    }


    fun update(vararg filterRule: FilterRule) {
        execute {
            appDb.filterRuleDao.update(*filterRule)
        }.onError {
            val msg = "更新过滤器规则出错\n${it.localizedMessage}"
            AppLog.put(msg, it)
            context.toastOnUi(msg)
        }
    }

    fun delete(vararg filterRule: FilterRule) {
        execute {
            appDb.filterRuleDao.delete(*filterRule)
        }.onError {
            val msg = "删除过滤器规则出错\n${it.localizedMessage}"
            AppLog.put(msg, it)
            context.toastOnUi(msg)
        }
    }

    fun enableSelection(vararg filterRule: FilterRule) {
        execute {
            val array = filterRule.map { it.copy(isEnabled = true) }.toTypedArray()
            appDb.filterRuleDao.insert(*array)
        }
    }

    fun disableSelection(vararg filterRule: FilterRule) {
        execute {
            val array = filterRule.map { it.copy(isEnabled = false) }.toTypedArray()
            appDb.filterRuleDao.insert(*array)
        }
    }

    fun save(filterRule: FilterRule, success: () -> Unit) {
        execute {
            filterRule.checkValid()
            appDb.filterRuleDao.insert(filterRule)
        }.onSuccess {
            success()
        }.onError {
            context.toastOnUi("save error, ${it.localizedMessage}")
        }
    }

    fun pasteRule(success: (FilterRule) -> Unit) {
        val text = context.getClipText()
        if (text.isNullOrBlank()) {
            context.toastOnUi("剪贴板没有内容")
            return
        }
        execute {
            GSON.fromJsonObject<FilterRule>(text).getOrThrow()
        }.onSuccess {
            success.invoke(it)
        }.onError {
            context.toastOnUi("格式不对")
        }
    }

}
