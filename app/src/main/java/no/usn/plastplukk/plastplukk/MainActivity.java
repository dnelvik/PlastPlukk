package no.usn.plastplukk.plastplukk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import no.usn.plastplukk.plastplukk.login.LoginActivity;
import no.usn.plastplukk.plastplukk.registration.CategoryActivity;
import no.usn.plastplukk.plastplukk.registration.PhotoGPSActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        clearPreferences();

        //Resetter lagret verdier
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
       if (prefs.getString("User", null) == null){
            Intent loggInnIntent = new Intent(this, LoginActivity.class);
            startActivity(loggInnIntent);
        }

        View headerView = navigationView.getHeaderView(0);
        TextView userId = headerView.findViewById(R.id.user_id_tv);
        userId.setText(prefs.getString("User", null));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public void registrerPlast(View view){
        Intent messageIntent = new Intent(this, CategoryActivity.class);
        startActivity(messageIntent);
    }
    public void openCamera(View view){
        Intent messageIntent = new Intent(this, PhotoGPSActivity.class);
        startActivity(messageIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_my_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangePasswordFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Logout(View view) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("User");
        editor.apply();
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
    }
    private void clearPreferences(){
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("Kategori");
        editor.remove("Underkategori");
        editor.remove("Størrelse");

        int size = prefs.getInt("Checksvar" + "_size", 0);
        for (int i = 0; i < size; i++)
            editor.remove("Checksvar" + "_" + i);
        editor.remove("Checksvar_size");
        editor.apply();
    }

    public void goToWeb(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://itfag.usn.no/grupper/v19gr2/plast/web/index"));
        startActivity(browserIntent);
    }
}
