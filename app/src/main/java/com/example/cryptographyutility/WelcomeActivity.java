package com.example.cryptographyutility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    Button logout;
    TextView username, mail;
    static JsonObject obj;
    static boolean end = false;
    static String token, name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        SecurePreferences preferences = new SecurePreferences(getApplicationContext(), "login", "dwd@ASDwasd@WAsdaW!Dsa",true);
        token = preferences.getString("token");
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_ROT13, R.id.nav_BASE64, R.id.nav_morse, R.id.nav_AES)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(MainActivity.guest){
            MainActivity.guest=false;
        }
        else{
            getData();
            while (!end){

            }
            end=false;
            View headerView = navigationView.getHeaderView(0);
            username =headerView.findViewById(R.id.username);
            mail=headerView.findViewById(R.id.userEmail);
            mail.setText(email);
            username.setText(name);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch(id)
        {
            case R.id.action_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout");
                builder.setMessage(R.string.logout);
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.action_logout, new
                        DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                SecurePreferences preferences = new SecurePreferences(getApplicationContext(), "login", "dwd@ASDwasd@WAsdaW!Dsa", true);
                                preferences.put("username", "false");
                                preferences.put("password", "false");
                                preferences.put("token", "false");
                                dialog.dismiss();  // quit AlertDialog
                                finish();  // quit activity
                            }
                        });
                builder.setNegativeButton(R.string.cancel, new
                        DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.dismiss();  // quit AlertDialog
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                break;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage(R.string.logout);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.action_logout, new
                DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {
                        SecurePreferences preferences = new SecurePreferences(getApplicationContext(), "login", "dwd@ASDwasd@WAsdaW!Dsa", true);
                        preferences.put("username", "false");
                        preferences.put("password", "false");
                        preferences.put("token", "false");
                        dialog.dismiss();  // quit AlertDialog
                        finish();  // quit activity
                    }
                });
        builder.setNegativeButton(R.string.cancel, new
                DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {
                        dialog.dismiss();  // quit AlertDialog
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void getData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL("http://"+MainActivity.ip+"/php-login-registration-api/user-info.php");// creo l'url
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();//creo la connessione
                    conn.setRequestMethod("GET");//imposto il metodo
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//imposto le propietà della connessione
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Authorization","Bearer "+token);// aggiungo l'autorizzazione con il token dell'utente per ottenere i dati

                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        String json = response.toString();
                        try {
                            obj = JsonParser.parseString(json).getAsJsonObject(); //parsing da string a json

                            Log.i("My App", "parsing di: \"" + json + "\"");
                        } catch (Throwable tx) {
                            Log.e("My App", "errore nel parsing di: \"" + json + "\"");

                        }
                        Log.i("STATUS", String.valueOf(obj.get("user")));
                        JsonObject aux = obj.get("user").getAsJsonObject();
                        Log.i("STATUS",aux.toString());
                        Log.i("STATUS",aux.get("name").getAsString());
                        Log.i("STATUS",aux.get("email").getAsString());
                        name=aux.get("name").getAsString();
                        email=aux.get("email").getAsString();

                    }
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect(); //chiudo la connessione


                } catch (Exception e) {
                    e.printStackTrace();
                }
                end=true; //segnalo la fine del thread
                
            }
        });

        thread.start(); //faccio partire il thread
    }
}
