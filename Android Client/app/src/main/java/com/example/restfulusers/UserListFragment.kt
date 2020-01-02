package com.example.restfulusers

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class UserListFragment : Fragment() {

    private val LOG_TAG = this.javaClass.simpleName
    private var mAdapter: ListAdapter? = null
    private var mRecyclerView: RecyclerView? = null

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

        mRecyclerView = view.findViewById(R.id.users_list)
        mAdapter = ListAdapter(context, ArrayList())
        mRecyclerView?.adapter = mAdapter
        mRecyclerView?.layoutManager = LinearLayoutManager(context)

        val mToolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_user_list);
        mToolbar.setOnMenuItemClickListener {

            if (it.itemId == R.id.action_delete_all_users) {
                deleteAllUsers()
                Toast.makeText(context, "Deleted All", Toast.LENGTH_SHORT).show()
            } else if (it.itemId == R.id.action_refresh_users_list) {
                Toast.makeText(context, "Refreshed", Toast.LENGTH_SHORT).show()
            } else if (it.itemId == R.id.action_change_server_ip) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Server IP")
                alertDialog.setMessage("Enter IP address of server")

                val editText = EditText(context)
                val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )

                editText.layoutParams = layoutParams

                alertDialog.setView(editText)
                alertDialog.setPositiveButton("Done") { dialogInterface: DialogInterface, i: Int ->
                    val preferences = context?.getSharedPreferences("myApp", Context.MODE_PRIVATE)
                    preferences?.edit()?.putString("baseURL", editText.text.toString())?.apply()
                }

                alertDialog.setNegativeButton(android.R.string.cancel) { dialog, which ->
                    dialog.dismiss()
                }

                alertDialog.show()

            } else return@setOnMenuItemClickListener false

            loadAllUsers()

            true
        }

        val fab: ExtendedFloatingActionButton = view.findViewById(R.id.user_add_fab)
        fab.setOnClickListener { view: View? -> listener?.onNewUserFabClicked() }

        return view
    }
    override fun onStart() {
        loadAllUsers()
        enableSwipe()
        super.onStart()
    }

    private fun enableSwipe() {

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                val data = mAdapter?.data
                val element = data?.get(position)

                data?.remove(element)
                mAdapter?.notifyItemRemoved(position)
                element?.uuid?.let { deleteItemByID(it) }

                // showing snack bar with Undo option
                val snackbar = Snackbar.make(viewHolder.itemView, "User deleted", Snackbar.LENGTH_LONG)

                snackbar.setAction("UNDO") {
                    data?.add(position, element)
                    mAdapter?.notifyItemInserted(position)
                    element?.uuid?.let {
                        insertUserAtID(it, element)
                    }
                }

                snackbar.show()

            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
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

    private fun deleteAllUsers() {
        val call = RetrofitClient.getInstance()
                .apiClient
                .deleteAllUsers()
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                loadAllUsers()
                Log.v(LOG_TAG, "Deleted all users")
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                call.cancel()
                Log.d(LOG_TAG, "Call failed :" + t.message)
            }
        })
    }

    fun deleteItemByID(uuid: UUID) {

        val call = RetrofitClient.getInstance()
                .apiClient
                .deleteUserByID(uuid)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Log.v(LOG_TAG, "Deleted user having UUID: $uuid")
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                call.cancel()
                Log.d(LOG_TAG, "Call failed :" + t.message)
            }
        })
    }

    fun loadAllUsers() {
        val call = RetrofitClient.getInstance()
                .apiClient
                .allUsers
        call.enqueue(object : Callback<List<User?>> {
            override fun onResponse(call: Call<List<User?>>, response: Response<List<User?>>) {
                val users = response.body()!!
                mAdapter?.data = users as ArrayList<User?>
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

    private class ListAdapter(val context: Context?, var data: ArrayList<User?>) : RecyclerView.Adapter<ListAdapter.VH>() {

        override fun onBindViewHolder(holder: VH, position: Int) {
            val convertView = holder.itemView

            convertView.setOnClickListener {
                (context as OnFragmentInteractionListener).onUserClicked(getUUIDAtIndex(position))
            }

            try {
                val name = convertView.findViewById<TextView>(R.id.list_item_user_name)
                val phone = convertView.findViewById<TextView>(R.id.list_item_user_phone)
                val uuid = convertView.findViewById<TextView>(R.id.list_item_user_uuid)
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
