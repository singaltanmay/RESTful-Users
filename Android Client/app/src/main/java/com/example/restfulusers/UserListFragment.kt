package com.example.restfulusers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restfulusers.API.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UserListFragment : Fragment() {

    private val LOG_TAG = this.javaClass.simpleName
    private var mAdapter: ListAdapter? = null

    private var listener: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        val mRecyclerView = view.findViewById<RecyclerView>(R.id.users_list)
        mAdapter = ListAdapter(context, ArrayList<User>())
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        val fab: FloatingActionButton = view.findViewById(R.id.user_add_fab)
        fab.setOnClickListener { view: View? -> listener?.onNewUserFabClicked() }

        return view
    }

    override fun onStart() {
        loadAllUsers()
        super.onStart()
    }

    fun loadAllUsers() {
        val call = RetrofitClient.getInstance()
                .apiClient
                .allUsers
        call.enqueue(object : Callback<List<User?>> {
            override fun onResponse(call: Call<List<User?>>, response: Response<List<User?>>) {
                val users = response.body()!!
                mAdapter?.data = users
                mAdapter?.notifyDataSetChanged()
                Log.v(LOG_TAG, "Call successful. Items received: " + users.size)
            }

            override fun onFailure(call: Call<List<User?>>, t: Throwable) {
                Toast.makeText(context, "Server not reachable", Toast.LENGTH_SHORT).show()
                Log.d(LOG_TAG, "Call failed :" + t.message)
            }
        })
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onUserClicked(uuid: UUID)
        fun onNewUserFabClicked()
    }

    private class ListAdapter(val context: Context?, var data: List<User?>) : RecyclerView.Adapter<ListAdapter.VH>() {

        override fun onBindViewHolder(holder: VH, position: Int) {
            val convertView = holder.itemView

            convertView.setOnClickListener {
                (context as OnFragmentInteractionListener).onUserClicked(getUUIDAtIndex(position))
            }

            try {
                val name = convertView?.findViewById<TextView>(R.id.list_item_user_name)
                val phone = convertView?.findViewById<TextView>(R.id.list_item_user_phone)
                val uuid = convertView?.findViewById<TextView>(R.id.list_item_user_uuid)
                val user = data[position]
                if (user != null) {
                    name?.text = "${user.firstName} ${user.lastName}"
                    phone?.text = user.phoneNumber
                    uuid?.text = user.uuid.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            return VH((context as MainActivity).layoutInflater.inflate(R.layout.list_item, parent, false))
        }

        fun getUUIDAtIndex(position: Int): UUID {
            return data[position]!!.uuid
        }

        override fun getItemCount() = data.size
    }

}
