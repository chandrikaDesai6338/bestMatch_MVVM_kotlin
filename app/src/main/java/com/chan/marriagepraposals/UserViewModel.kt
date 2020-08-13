package com.chan.marriagepraposals

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chan.marriagepraposals.db.AppDatabase
import com.chan.marriagepraposals.db.User
import com.chan.marriagepraposals.models.BaseResponse
import com.chan.marriagepraposals.models.Results
import com.chan.marriagepraposals.webservice.getApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

/**
 * Created by Chan on 11/08/20.
 */
class UserViewModel() : ViewModel() {

    var usersLiveData = MutableLiveData<List<User>>()
    private var compositeDisposable = CompositeDisposable()
    private lateinit var appDatabase: AppDatabase

    fun setDBInstance(appDatabase: AppDatabase) {
        this.appDatabase = appDatabase
    }

    fun getUserDataServer(resultCount: Int) {
        compositeDisposable.add(
            getApiService()
                .getUserList(resultCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribeWith(object : DisposableSubscriber<BaseResponse?>() {

                    override fun onNext(response: BaseResponse?) {
                        if (response?.results != null) {
                            saveUserDataDB(response.results)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("UserViewModel","Network Error!! e = "+e.localizedMessage)
                    }

                    override fun onComplete() {
                    }
                }) as Disposable
        )
    }

    fun saveUserDataDB(results: List<Results>) {
        for (result: Results in results) {
            val user = User(
                result.login.uuid,
                result.name.first + " " + result.name.last,
                result.dob.age,
                result.dob.date,
                result.location.city + " " + result.location.state + " " + result.location.country,
                result.picture.medium,
                result.login.username,
                result.login.password,
                result.email,
                ""
            )
            appDatabase.UserDao().insertAll(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("UserViewModel", "Data Inserted")
                }, {
                }).let {
                    compositeDisposable.add(it)
                }
        }
        getUserDataDB()
    }

    fun getUserDataDB() {
        compositeDisposable.add(
            appDatabase.UserDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribeWith(object : DisposableSubscriber<List<User>>() {
                    override fun onComplete() {
                        TODO("Not yet implemented")
                    }

                    override fun onNext(users: List<User>?) {
                        usersLiveData.value = users
                        Log.d("UserViewModel", "Data received from DB")
                    }

                    override fun onError(t: Throwable?) {
                        TODO("Not yet implemented")
                    }

                }) as Disposable
        )
    }

    fun updateUserStatus(userId : String, status : String){
        appDatabase.UserDao().updateStatus(status, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("UserViewModel", "Data Updated")
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }
}