package com.example.cryptographyutility;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.cliftonlabs.json_simple.Jsoner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    EditText email, password, name;
    Button register;
    ProgressBar loading;
    static JsonObject obj;
    static Boolean end = false;
    static Boolean registrato = false;
    static Boolean username = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.txtEmail);
        name = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        register = findViewById(R.id.btnRegister);
        loading = findViewById(R.id.loadingR);
        register.setOnClickListener(new View.OnClickListener() {  //se il bonnone viene cliccato
            @Override
            public void onClick(View view) {
                if(!(password.getText().length() <8)) { //verifico se la password ha più di 8 caratteri
                    try {
                        signup(); //eseguo metodo per la registrazione
                        loading.setVisibility(View.VISIBLE);
                        while (!end) { //loop con immagine del caricamento attiva fino ache il thread non ha finito

                        }
                        loading.setVisibility(View.INVISIBLE);
                        if(!username) {// se l'username è libero e quindi false
                            if (registrato) { //verifico se è registrato corrttamente e segnalo la corretta registrazione
                                Toast.makeText(RegisterActivity.this, R.string.signup_succes, Toast.LENGTH_LONG).show();
                                finish();
                            } else { //altrimenti segnalo che la registrazione è fallita
                                Toast.makeText(RegisterActivity.this, R.string.signup_failed, Toast.LENGTH_LONG).show();
                            }
                        }
                        else{ //se l'username non è libero segnalo che la mail è gia in uso
                            Toast.makeText(RegisterActivity.this, R.string.invalid_username, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, R.string.signup_error, Toast.LENGTH_LONG).show();
                    }
                }
                else{//scrivo all'utente che la password deve avere più di 8 caratteri
                    Toast.makeText(RegisterActivity.this, R.string.invalid_password , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signup() { //metodo per la registrazione di un nuovo user
        Thread thread = new Thread(new Runnable() { //inizializzo un thread per la connessione al server e l'ottenimento dei dati
            @Override
            public void run() {
                try {
                    URL url = new URL("http://"+MainActivity.ip+"/php-login-registration-api/register.php"); //imposto url dell'api di registrazione
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //creo la connessione
                    conn.setRequestMethod("POST"); //imposto il metodo POST per la connessione
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); //imposto la proprietà content-type per segnalare l'invio di dati in formato json con charset utf8
                    conn.setRequestProperty("Accept","application/json"); //imposto la proprietà Accept per accettare una risposta in formato json
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject(); // creo un oggetto json che invierò nella connessione
                    jsonParam.put("name", name.getText()); // metto il parametro name nell'oggetto json
                    jsonParam.put("email", email.getText());// metto il parametro email nell'oggetto json
                    jsonParam.put("password", password.getText());// metto il parametro password nell'oggetto json


                    DataOutputStream os = new DataOutputStream(conn.getOutputStream()); //inizializzo lo stream di output

                    os.writeBytes(jsonParam.toString());// inserisco l'oggetto json

                    os.flush();
                    os.close(); // invio e chiudo lo stream di output

                    try(BufferedReader br = new BufferedReader( //inizializzo un buffer reader per ottenere la risposta
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) { //loop per ottenere tutta la risposta
                            response.append(responseLine.trim());
                        }
                        String json = response.toString(); //trasformo la risposta in una stringa
                        try {
                            obj = JsonParser.parseString(json).getAsJsonObject(); //casting della stringa per trasformarla in json

                        } catch (Throwable tx) {
                            Log.e("My App", "errore nel parsing di: \"" + json + "\""); //log in caso di errore nel parsing
                            registrato=false;
                        }
                        if(obj.get("success").getAsString().equals("1")){ //verifico se la registrazione è avvenuta con successo
                            registrato=true;
                        }
                        else if(obj.get("success").getAsString().equals("0")){ //verifico se la registrazione non è avvenuta con successo
                            username=true;
                            registrato=false;
                        }
                        else //verifico se la registrazione è fallita completamente
                            registrato=false;
                    }
                    Log.i("STATUS", String.valueOf(conn.getResponseCode())); //log del status di connessione
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
