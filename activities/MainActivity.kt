package com.example.navigationdrawer.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navigationdrawer.*
import com.example.navigationdrawer.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<PostAdapter.PostViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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
        val retrofit = Retrofit.Builder()
            .baseUrl("http://tgryl.pl/shoutbox/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI::class.java)
        val call = jsonPlaceholderAPI.getPosts()

        call.enqueue(object : Callback<List<ExamplePost>>{
            override fun onResponse(call: Call<List<ExamplePost>>, response: Response<List<ExamplePost>>) {
                if (!response.isSuccessful){
                    Toast.makeText(this@MainActivity, "Code: ${response.code()}", Toast.LENGTH_SHORT).show()
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

            }

            override fun onFailure(call: Call<List<ExamplePost>>, t: Throwable) {
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

    fun send(view: View) {

    }
}