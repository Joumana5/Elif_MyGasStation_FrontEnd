package com.elife.mygasstationproject

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.elife.mygasstationproject.fragments.ClientDashboardFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialiser la Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialiser le DrawerLayout et NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Ajouter un ActionBarDrawerToggle pour gérer l'icône du menu
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,  // Texte pour l'ouverture du menu
            R.string.close_nav  // Texte pour la fermeture du menu
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Si c'est la première fois que l'activité est lancée, afficher ClientDashboardFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_contrainer, ClientDashboardFragment())  // Remplacer le fragment
                .commit()
            navigationView.setCheckedItem(R.id.nav_home)  // Mettre en surbrillance le menu "Home"
        }
    }

    override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Lorsque l'élément "Home" est sélectionné, afficher le fragment ClientDashboardFragment
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_contrainer, ClientDashboardFragment())
                    .commit()
            }
            R.id.nav_contract -> {
                // Lorsque l'élément "Contact" est sélectionné, afficher le fragment ContactFragment
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_contrainer, ContactFragment())
                    .commit()
            }
            else -> {
                Toast.makeText(this, "Option non implémentée", Toast.LENGTH_SHORT).show()
            }
        }

        // Fermer le menu une fois un élément sélectionné
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        // Si le menu est ouvert, le fermer, sinon quitter l'activité
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
