package com.example.restfulusers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.restfulusers.API.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UserModifyFragment(private val sourceUUID: UUID?) : Fragment() {

    private val LOG_TAG = this.javaClass.simpleName

    private var listener: OnModificationDoneListener? = null
    private var firstName: EditText? = null
    private var lastName: EditText? = null
    private var phoneNumber: EditText? = null
    private var uuid: EditText? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnModificationDoneListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_user_modify, container, false)

        firstName = view.findViewById(R.id.user_modify_user_first_name)
        lastName = view.findViewById(R.id.user_modify_user_last_name)
        phoneNumber = view.findViewById(R.id.user_modify_user_phone)
        uuid = view.findViewById(R.id.user_modify_user_uuid)

        if (sourceUUID != null) {
            getUserByID()
        }

        view.findViewById<FloatingActionButton>(R.id.modify_done_fab).setOnClickListener { v ->
            kotlin.run {

                val user = User(User.getUUIDIfValid(uuid?.text.toString()), firstName?.text.toString(), lastName?.text.toString(), phoneNumber?.text.toString())

                if (user.isBlank()) return@run

                if (user.uuid == null) insertUser(user)
                else if (user.uuid == sourceUUID) modifyUserAtID(user.uuid, user)
                else insertUserAtID(user.uuid, user)

            }
            onModificationDone()
        }

        return view
    }

    fun User.isBlank(): Boolean {
        return this.firstName.isBlank() && this.lastName.isBlank() && this.phoneNumber.isBlank()
    }

    private fun setupUserInfo(user: User) {

        firstName?.setText(user.firstName)
        lastName?.setText(user.lastName)
        phoneNumber?.setText(user.phoneNumber)
        uuid?.setText(user.uuid.toString())

    }

    private fun getUserByID() {
        val call = RetrofitClient.getInstance()
                .apiClient
                .getUserByID(sourceUUID)
        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val body = response.body()
                if (body != null) {
                    setupUserInfo(body)
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                call.cancel()
                Log.d(LOG_TAG, "Call failed :$call")
            }
        }
        )
    }

    fun insertUser(user: User?) {
        val call = RetrofitClient.getInstance()
                .apiClient
                .insertUser(user)
        Log.v(LOG_TAG, "Call created :" + call.request().body().toString())
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.v(LOG_TAG, "User insertion result: " + response.body().toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
                Log.d(LOG_TAG, "Call failed :" + t.message)
            }
        })
    }

    fun modifyUserAtID(uuid3: UUID, user: User?) {
        val call = RetrofitClient.getInstance()
                .apiClient
                .modifyUserAtID(uuid3, user)
        Log.v(LOG_TAG, "Call created :" + call.request().body().toString())
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.v(LOG_TAG, "User insertion result: " + response.body().toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
                Log.d(LOG_TAG, "Call failed :" + t.message)
            }
        })
    }

    fun insertUserAtID(uuid3: UUID, user: User?) {
        val call = RetrofitClient.getInstance()
                .apiClient
                .insertUserAtID(uuid3, user)
        Log.v(LOG_TAG, "Call created :" + call.request().body().toString())
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.v(LOG_TAG, "User insertion result: " + response.body().toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
                Log.d(LOG_TAG, "Call failed :" + t.message)
            }
        })
    }

    fun onModificationDone() {
        listener?.onModificationDone()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnModificationDoneListener {
        fun onModificationDone()
    }

}
