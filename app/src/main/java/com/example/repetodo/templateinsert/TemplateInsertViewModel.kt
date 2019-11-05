package com.example.repetodo.templateinsert

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.repetodo.database.Template
import com.example.repetodo.database.TemplateDao
import com.example.repetodo.database.TemplateItem
import com.example.repetodo.database.TemplateListDao
import kotlinx.coroutines.*

class TemplateInsertViewModel(val templateListDao: TemplateListDao, val templateDao: TemplateDao, application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

//    private var _templateList = MutableLiveData<List<Template>> ()
//    val templateList: LiveData<List<Template>>
//        get() = _templateList

    // lateinit var _templateBriefInfo : List<String>
    private var templatesList : List<Template> = listOf ()

    private var _templateInsertList = MutableLiveData<List<TemplateInsert>> ()
    val templateInsertList: LiveData<List<TemplateInsert>>
        get() = _templateInsertList


    init {
        _templateInsertList.value = listOf<TemplateInsert>()
        getTemplateList()

    }

    private fun getTemplateList() {
        uiScope.launch {
            templatesList = getAllTemplates()
            Log.i("TemplateInsertViewModel", "xin chao ${templatesList.joinToString()}")

            for (template in templatesList) {
                var templateId = template.templateId
                var latestItems = getTemplatesLastestItems(templateId, 3)
                var templateBrief = latestItems.joinToString(", ")
                var aTemplate = TemplateInsert(templateId, template.templateTitle, templateBrief)
                Log.i("TemplateInsertViewModel", "this is a template brief: $templateBrief")
                _templateInsertList.value = _templateInsertList.value!!.plus(aTemplate)
            }
            Log.i("TemplateInsertViewModel", "hello ${_templateInsertList.value!!.joinToString()}")
        }
    }

    private suspend fun getAllTemplates(): List<Template> {
        return withContext(Dispatchers.IO) {
            lateinit var template: List<Template>
            template = templateListDao.getAllItems()
            template
        }
    }

    private suspend fun getTemplatesLastestItems(templateId: Long, number: Int): List<String> {
        return withContext(Dispatchers.IO) {
            var result = templateDao.getLastestTitles(templateId, number)
            result
        }
    }

}