package ssa.worriorx.com.qnote.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.ui.add_note.AddNoteActivity


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var navView: NavigationView? = null
    private var navController:NavController? = null
    private var fab: FloatingActionButton? = null
    private var currentLabelId: Int? = null

    @SuppressLint("RestrictedApi")
    private val listener = NavController.OnDestinationChangedListener { navController, destination, arguments ->
       Log.d("TAG","destination "+navController.currentDestination!!.id)
        currentLabelId = navController.currentDestination!!.id
        if (navController.currentDestination!!.id == R.id.nav_trash)
            fab?.visibility = View.GONE
        else
            fab?.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        fab = findViewById(R.id.fab)
        fab!!.setOnClickListener { view ->
           startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onStart Called")
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.nav_note,
                        R.id.nav_favorite,
                        R.id.nav_category
                ), drawerLayout
        )
        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView?.setupWithNavController(navController!!)

        navController!!.addOnDestinationChangedListener(listener)

    }

    override fun onResume() {
        super.onResume()
        currentLabelId?.let { navController!!.navigate(it) }
    }

   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true
    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (currentLabelId != R.id.nav_trash) {
            menuInflater.inflate(R.menu.main, menu)
            return super.onCreateOptionsMenu(menu)
        }
        return false
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        navController!!.removeOnDestinationChangedListener(listener)
    }

}