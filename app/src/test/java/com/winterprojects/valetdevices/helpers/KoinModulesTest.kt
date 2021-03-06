package com.winterprojects.valetdevices.helpers

import com.winterprojects.valetdevices.business.di.BusinessDI
import com.winterprojects.valetdevices.datasource.di.DatabaseDI
import com.winterprojects.valetdevices.datasource.di.DatasourceDI
import com.winterprojects.valetdevices.datasource.di.RemoteDI
import com.winterprojects.valetdevices.datasource.di.RetrofitTestDI

object KoinModulesTest {
    val testModules = listOf(
        RetrofitTestDI.module,
        RemoteDI.module,
        DatabaseDI.module,
        DatasourceDI.module,
        BusinessDI.module
    )
}