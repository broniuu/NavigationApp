package com.example.navigationdrawer.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.navigationdrawer.*
import com.example.navigationdrawer.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.FieldPosition

class MainActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: PostAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://tgryl.pl/shoutbox/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //GET
        reRun()

        swipeRefreshLayout = findViewById(R.id.swipe_layout)
        initializeRefreshListener()

    }

    private fun reRun(){
        Thread {
            try {
                while (true) {
                    getPosts()
                    Thread.sleep(60000)
                }
            } catch (e: InterruptedException) {
                Log.e("tagErr", e.message.toString())
            }
        }.start()
    }

    private fun getPosts() {

        val call = jsonPlaceholderAPI.getPosts()

        call.enqueue(object : Callback<ArrayList<ExamplePost>> {
            override fun onResponse(
                call: Call<ArrayList<ExamplePost>>,
                response: Response<ArrayList<ExamplePost>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Code: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val posts = response.body()

                //Recycler view
                mRecyclerView = findViewById(R.id.recyclerView)
                mRecyclerView.setHasFixedSize(true)
                mLayoutManager = LinearLayoutManager(this@MainActivity)
                mAdapter = PostAdapter(posts!!)

                mRecyclerView.layoutManager = mLayoutManager
                mRecyclerView.adapter = mAdapter

                val swipeHandler = object : SwipeToDeleteCallback(this@MainActivity) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = mRecyclerView.adapter as PostAdapter
                        val id = adapter.getPost(viewHolder.adapterPosition).id
                        adapter.removeAt(viewHolder.adapterPosition)
                        deletePost(id)
                    }
                }
                val itemTouchHelper = ItemTouchHelper(swipeHandler)
                itemTouchHelper.attachToRecyclerView(mRecyclerView)

                mAdapter.setOnItemClickListener(object : PostAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
                        val myLogin = sharedPreferences.getString("MY_LOGIN_KEY", "Default value").toString()
                        val post = posts.get(position)
                        val login = post.login
                        if(!myLogin.equals(login)){
                            Toast.makeText(
                                this@MainActivity,
                                "It's not your comment!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        val id = post.id
                        val date = post.date
                        val content = post.content
                        saveDataAndStartItemActivity(login, date, content, id)
                    }
                })

            }

            override fun onFailure(call: Call<ArrayList<ExamplePost>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun goToLogin(view: View){
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun saveDataAndStartItemActivity(login: String, date: String, content: String, id:String) {
        intent = Intent(this, ItemActivity::class.java)

        intent.putExtra("KEY_ID", id)
        intent.putExtra("KEY_LOGIN", login)
        intent.putExtra("KEY_DATE", date)
        intent.putExtra("KEY_CONTENT", content)

        startActivity(intent)
    }

    fun createPost(view: View){
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        val login = sharedPreferences.getString("MY_LOGIN_KEY", "Default value").toString();
        val editText = findViewById<EditText>(R.id.contentEditText2)
        val content = editText.text.toString()
        val post = Post(content, login)

        val call = jsonPlaceholderAPI.postData(post)

        call.enqueue(object : Callback<ExamplePost>{
            override fun onResponse(call: Call<ExamplePost>, response: Response<ExamplePost>) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Code: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                Toast.makeText(
                    this@MainActivity,
                    "posted succesfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<ExamplePost>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initializeRefreshListener(){
        swipeRefreshLayout.setOnRefreshListener {
            getPosts()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun deletePost(id: String){
        val call = jsonPlaceholderAPI.deletePost(id)
        call.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Code: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                Toast.makeText(
                    this@MainActivity,
                    "Post Deleted!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}