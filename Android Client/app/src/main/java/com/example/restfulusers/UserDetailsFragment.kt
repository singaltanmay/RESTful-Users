package com.example.restfulusers

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.restfulusers.API.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UserDetailsFragment(var uuid: UUID?) : Fragment() {

    private val LOG_TAG = this.javaClass.simpleName

    private var listenerUser: OnEditUserFabClickListener? = null
    private var parentView: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditUserFabClickListener) {
            listenerUser = context
        } else {
            throw RuntimeException("$context must implement OnEditUserFabClickListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        parentView = inflater.inflate(R.layout.fragment_user_details, container, false)


        if (uuid != null) {
            getUserByID()
        } else uuid = UUID.randomUUID()

        val user = User(uuid, "John", "Doe", "13491780")


        setupUserInfo(user)

        val floatingActionButton: FloatingActionButton = parentView!!.findViewById(R.id.user_edit_fab)
        floatingActionButton.setOnClickListener { view: View ->
            val drawable = resources.getDrawable(R.drawable.ic_edit_to_done_anim) as AnimatedVectorDrawable
            (view as FloatingActionButton).setImageDrawable(drawable)
            drawable.start()
            listenerUser?.onEditUserFabClicked(uuid)
        }


        return parentView
    }

    private fun setupUserInfo(user: User) {

        if (parentView != null) {
            (parentView?.findViewById<View>(R.id.user_details_user_first_name) as TextView).text = user.firstName
            (parentView?.findViewById<View>(R.id.user_details_user_last_name) as TextView).text = user.lastName
            (parentView?.findViewById<View>(R.id.user_details_user_phone) as TextView).text = user.phoneNumber
            (parentView?.findViewById<View>(R.id.user_details_user_uuid) as TextView).text = user.uuid.toString()
        }

    }

    private fun getUserByID() {
        val call = RetrofitClient.getInstance()
                .apiClient
                .getUserByID(uuid)
        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val body = response.body()
                body?.let { setupUserInfo(it) }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                call.cancel()
                Log.d(LOG_TAG, "Call failed :$call")
            }
        }
        )
    }

    override fun onDetach() {
        super.onDetach()
        listenerUser = null
    }

    interface OnEditUserFabClickListener {
        fun onEditUserFabClicked(uuid: UUID?)
    }

}
