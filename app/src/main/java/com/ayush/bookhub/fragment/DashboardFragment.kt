package com.ayush.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bookhub.kartikey.bookhub.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ayush.bookhub.R
import com.ayush.bookhub.adapter.DashboardRecyclerAdapter
import com.ayush.bookhub.model.Book
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    //progress bar
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    //comparator func
    var ratingComparator = Comparator<Book> { book1, book2 ->

        if (book1.bookRating.compareTo(book2.bookRating, true) == 0) {
            book1.bookName.compareTo(book2.bookName, true)
        } else {
            book1.bookRating.compareTo(book2.bookRating, true)
        }
    }
    

    val bookInfoList = arrayListOf<Book>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setHasOptionsMenu(true)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.rlProgress)

        progressLayout.visibility = View.VISIBLE



        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        //we don;t use this keyword for fragments
        layoutManager = LinearLayoutManager(activity)


        //GET request for the list of books
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"
        Log.d("status1", "yes")
        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    try {
                        progressLayout.visibility = View.GONE
                        val success = it.getBoolean("success")
                        Log.d("status1", "yes")
                        if (success) {
                            //extract data
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJSON = data.getJSONObject(i)
                                val book = Book(
                                    //extracting element wise data
                                    bookJSON.getString("book_id"),
                                    bookJSON.getString("name"),
                                    bookJSON.getString("author"),
                                    bookJSON.getString("rating"),
                                    bookJSON.getString("price"),
                                    bookJSON.getString("image")
                                )
                                bookInfoList.add(book)
                                recyclerAdapter =
                                    DashboardRecyclerAdapter(activity as Context, bookInfoList)
                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager
                                Toast.makeText(
                                    activity as Context,
                                    "everything is fine till now",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }
                        } else {
                            //failure in getting data
                            if (activity != null) {
                                Toast.makeText(
                                    activity as Context,
                                    "Error Occurred!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }

                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some unexpected error occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {
                    //handle the error
                    Toast.makeText(
                        activity as Context,
                        "Volley Error Occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "6f445c937e89ce"
                        return headers
                    }

                }
            queue.add(jsonObjectRequest)

        } else {
            Log.d("status2", "yes")
            progressLayout.visibility=View.GONE
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Failure")
            dialog.setMessage("No Connection found")
            dialog.setPositiveButton("Open Settings") {
                //adding click listener to text
                    text, listener ->
                //open setting
                val openSetting = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(openSetting)
                activity?.finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                //exit
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }
        return view
    }

    //for the sort option menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_sort) {
            //will return in decreasing order of rating
            Collections.sort(bookInfoList, ratingComparator)
            //we want increasing,hence reverse
            bookInfoList.reverse()
        }
        //notfy adapter of changes made
       // recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }


}
