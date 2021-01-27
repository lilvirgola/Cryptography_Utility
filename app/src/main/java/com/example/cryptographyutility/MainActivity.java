package com.example.cryptographyutility;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.provider.Settings;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    CheckBox remember;
    Button login, register, loginG;
    ProgressBar loading;
    static JsonObject obj;
    static String ip = "10.6.0.1";
    static String token;
    static Boolean guest=false;
    static Boolean accesso=true;
    static Boolean automatic=false;
    static Boolean finish=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.username);
        remember = findViewById(R.id.remainRegistered);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        password = findViewById(R.id.password);
        loginG = findViewById(R.id.loginG);
        loading = findViewById(R.id.loading);
        SecurePreferences preferences = new SecurePreferences(getApplicationContext(), "login", "password",true); //cambia password con una password
        String check =preferences.getString("username");
        if(check==null || check.equals("false")|| check.equals("")){// verifica se l'opzione rimani registrato non era stata attivata attraverso il controllo delle preferences
            Toast.makeText(MainActivity.this, R.string.please, Toast.LENGTH_LONG ).show(); //chiede all'utente di registrarsi
        }
        else{ //altrimenti fa il login in automatico
            automatic=true;
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
        login.setOnClickListener(new View.OnClickListener() { //se si preme il bottone di login
            @Override
            public void onClick(View view) {
                try {
                    login();// metodo per fare il login
                    loading.setVisibility(View.VISIBLE);
                    while (!finish){ //loop fino alla fine del thread con barra di caricamento visibile

                    }

                    loading.setVisibility(View.INVISIBLE);
                    if(accesso) {//se l'accesso è avvenuto correttaente
                        if (remember.isChecked()) { //verifico se l'utente ha spuntato la check box e salvo tutti i dati nella securepreferences
                            token = obj.get("token").getAsString();
                            SecurePreferences preferences = new SecurePreferences(getApplicationContext(), "login", "password", true); //cambia password con una password
                            preferences.put("username", String.valueOf(email.getText()));
                            preferences.put("password", String.valueOf(password.getText()));
                            preferences.put("token", String.valueOf(token));

                        } else if (!remember.isChecked()) {//altrimenti salvo solo il token
                            token = obj.get("token").getAsString();
                            SecurePreferences preferences = new SecurePreferences(getApplicationContext(), "login", "password", true); //cambia password con una password
                            preferences.put("username", "false");
                            preferences.put("password", "false");
                            preferences.put("token", String.valueOf(token));
                        }
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(intent);// faccio partire la nuova activity
                    }
                    else{// altrimenti avviso l'utente che l'username o la psw sono errati
                        accesso=true;
                        Toast.makeText(MainActivity.this, R.string.login_failed, Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, R.string.online_error, Toast.LENGTH_LONG).show();
                }
            }
        });
        loginG.setOnClickListener(new View.OnClickListener() {// se il bottone accedi come guest è premuto
            @Override
            public void onClick(View view) {
                guest=true; //setto il flag guest come vero
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);//faccio partire la nuova activity
            }
        });
        register.setOnClickListener(new View.OnClickListener() {//se il pulsante registrati viene premuto
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);//faccio partire la activity di registrazione
            }
        });
    }
    @Override
    public void onBackPressed(){ //se viene premuto il pulsante back chiedo all'utente se è sicuro
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setMessage(R.string.quit);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.close, new
                DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {
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
    public void login() { // metodo per il login
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://"+MainActivity.ip+"/php-login-registration-api/login.php");//url per l'api
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //creo la connessione
                    conn.setRequestMethod("POST"); //imposto il metodo POST per la connessione
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); //imposto la proprietà content-type per segnalare l'invio di dati in formato json con charset utf8
                    conn.setRequestProperty("Accept","application/json"); //imposto la proprietà Accept per accettare una risposta in formato json
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject(); // creo un oggetto json che invierò nella connessione
                    jsonParam.put("email", email.getText());// metto il parametro email nell'oggetto json
                    jsonParam.put("password", password.getText());// metto il parametro password nell'oggetto json

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }

                        try {
                            obj = JsonParser.parseString(response.toString()).getAsJsonObject(); //parsing della risposta da string in formato json
                            Log.i("My App", obj.get("token").getAsString());
                            Log.i("My App", "parsing di: \"" + response + "\"");
                        } catch (Throwable tx) {
                            Log.e("My App", "errore nel parsing di: \"" + response + "\"");
                            accesso=false;
                        }
                        if(obj.get("success").getAsString().equals("0")){
                            accesso = false;
                        }
                        else
                            accesso=true;

                    }
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();//chiudo la connessione

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish=true; //segnalo la fine del thread
            }
        });

        thread.start();//faccio partire il thread
    }
}

