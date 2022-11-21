package com.yong.nyamnyamgood

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context, dbName: String, version: Int) :
    SQLiteOpenHelper(context, dbName, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val queryStoreTBL = """
            create table storeTBL(
                language text,
                storeName text,
                address text,
                telNo text,
                url text,
                useTime text,
                subwayInfo text,
                reprsntMenu text,
                star integer
            )
        """.trimIndent()
        val queryLangSetting = """
            create table langSetting(
                language text
            )
        """.trimIndent()
        db?.apply {
            execSQL(queryStoreTBL)
            execSQL(queryLangSetting)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, newVersion: Int, oldVersion: Int) {
        val query = """
            drop table storeTBL
        """.trimIndent()
        val query2 = """
            drop table langSetting
        """.trimIndent()
        db?.apply {
            execSQL(query)
            execSQL(query2)
        }
        this.onCreate(db)
    }

    //언어 변경
    fun languageSet(language: String): Boolean {
        var flag = false
        val query = "update langSetting set language = '$language'"
        val db = this.writableDatabase
        try {
            db.execSQL(query)
            flag = true
        } catch (e: Exception) {
            Log.d("nyamnyamgood", "${e.printStackTrace()}")
            flag = false
        } finally {
            db.close()
        }
        return flag
    }
    
    //어플 시작시 언어 설정
    fun languageGet(): String{
        val query = "select * from langSetting"
        var cursor: Cursor? = null
        var language = "ko"
        val db = this.readableDatabase

        try {
            cursor = db.rawQuery(query, null)
            if (cursor.count > 0) {
                if (cursor.moveToFirst()) {
                    language = cursor.getString(0)
                }
            }
        } catch (e: Exception) {
            Log.d("nyamnyamgood", "${e.printStackTrace()}")
        } finally {
            db.close()
        }
        return language
    }

    //초기 언어 설정
    fun insertlanguage(language: String): Boolean {
        var flag = false
        val query = "insert into langSetting values('$language')"
        val db = this.writableDatabase
        try {
            db.execSQL(query)
            flag = true
        } catch (e: Exception) {
            Log.d("nyamnyamgood", "${e.printStackTrace()}")
            flag = false
        } finally {
            db.close()
        }
        return flag
    }

    fun selectLang(language: String): MutableList<DataVOStore>? {
        var storeList: MutableList<DataVOStore>? = mutableListOf<DataVOStore>()
        var cursor: Cursor? = null
        val query = """
            select * from storeTBL where language = '$language'
        """.trimIndent()
        val db = this.readableDatabase
        try {
            cursor = db.rawQuery(query, null)
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val storeName = cursor.getString(1)
                    val address = cursor.getString(2)
                    val telNo = cursor.getString(3)
                    val url = cursor.getString(4)
                    val useTime = cursor.getString(5)
                    val subwayInfo = cursor.getString(6)
                    val reprsntMenu = cursor.getString(7)
                    val star = cursor.getInt(8)
                    val dataVOStore = DataVOStore(
                        language,
                        storeName,
                        address,
                        telNo,
                        url,
                        useTime,
                        subwayInfo,
                        reprsntMenu,
                        star
                    )
                    storeList?.add(dataVOStore)
                }
            } else {
                storeList = null
            }
        } catch (e: Exception) {
            Log.d("nyamnyamgood", "${e.printStackTrace()}")
            storeList = null
        } finally {
            cursor?.close()
            db.close()
        }
        return storeList
    }

    fun insertStore(dataVOStore: DataVOStore): Boolean {
        var flag = false
        val query = """
            insert into storeTBL(language, storeName, address, telNo, url, useTime, subwayInfo, reprsntMenu, star)
            values('${dataVOStore.language}','${dataVOStore.storeName}','${dataVOStore.address}','${dataVOStore.telNo}',
                    '${dataVOStore.url}','${dataVOStore.useTime}', '${dataVOStore.subwayInfo}', '${dataVOStore.reprsntMenu}', ${dataVOStore.star})
        """.trimIndent()
        val db = this.writableDatabase

        try {
            db.execSQL(query)
            flag = true
        } catch (e: Exception) {
            Log.d("nyamnyamgood", "${e.printStackTrace()}")
            flag = false
        } finally {
            db.close()
        }
        return flag
    }

    // 음식점에 별표주기
    fun updateStar(dataVOStore: DataVOStore): Boolean {
        var flag = false
        val query = "update storeTBL set star = '${dataVOStore.star}' where storeName = '${dataVOStore.storeName}'"
        val db = this.writableDatabase

        try{
            db.execSQL(query)
            flag = true
        }catch (e: Exception){
            Log.d("nyamnyamgood", "${e.printStackTrace()}")
            flag = false
        }finally {
            db.close()
        }
        return flag
    }

    // 별표준 음식점만 보이기
    fun selectStar(language: String): MutableList<DataVOStore>? {
        var storeList: MutableList<DataVOStore>? = mutableListOf<DataVOStore>()
        var cursor: Cursor? = null
        val query = """
            select * from storeTBL where language = '$language' and star = '1'
        """.trimIndent()
        val db = this.readableDatabase
        try {
            cursor = db.rawQuery(query, null)
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val storeName = cursor.getString(1)
                    val address = cursor.getString(2)
                    val telNo = cursor.getString(3)
                    val url = cursor.getString(4)
                    val useTime = cursor.getString(5)
                    val subwayInfo = cursor.getString(6)
                    val reprsntMenu = cursor.getString(7)
                    val star = cursor.getInt(8)
                    val dataVOStore = DataVOStore(
                        language,
                        storeName,
                        address,
                        telNo,
                        url,
                        useTime,
                        subwayInfo,
                        reprsntMenu,
                        star
                    )
                    storeList?.add(dataVOStore)
                }
            } else {
                storeList = null
            }
        } catch (e: Exception) {
            Log.d("nyamnyamgood", "${e.printStackTrace()}")
            storeList = null
        } finally {
            cursor?.close()
            db.close()
        }
        return storeList
    }
}