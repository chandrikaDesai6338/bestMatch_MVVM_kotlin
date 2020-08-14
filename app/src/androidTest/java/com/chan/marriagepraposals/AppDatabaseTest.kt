package com.chan.marriagepraposals

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chan.marriagepraposals.db.AppDatabase
import com.chan.marriagepraposals.db.User
import com.chan.marriagepraposals.db.UserDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Chan on 14/08/20.
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private var userDao: UserDao? = null
    lateinit var db: AppDatabase
    val compositeDisposable = CompositeDisposable()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        userDao = db.UserDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        var nameLocal = ""
        val userLocal: User = TestUtil.createUser().apply {
            name = ("george")
        }
        db.UserDao().insertAll(userLocal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("database test", "Data Inserted")
            }, {
                Log.d("database test", "Data error e = "+it?.localizedMessage)
            }).let {
                compositeDisposable.add(it)
            }
        compositeDisposable.add(
            db.UserDao().findUserByName("george")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribeWith(object : DisposableSubscriber<User>() {
                    override fun onComplete() {
                        Log.d("database test", "Data inserted ")
                    }
                    override fun onNext(user: User?) {
                        nameLocal = user?.name.toString()
                    }
                    override fun onError(t: Throwable?) {
                        Log.d("database test", "Data error e = "+t?.localizedMessage)
                    }
                }) as Disposable
        )
        assertEquals(nameLocal, userLocal.name)
    }
}