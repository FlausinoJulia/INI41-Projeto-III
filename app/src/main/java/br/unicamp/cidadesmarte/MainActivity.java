package br.unicamp.cidadesmarte;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    List<Cidade> cidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cidades_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // lendo o arquivo de cidades
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cidadesMarte.json");
        Log.i("cidades", jsonFileString);

        Gson gson = new Gson(); // cria uma instanica da classe gson

        Type listaCidadesType = new TypeToken<List<Cidade>>() { }.getType();
        // converte um vetor de JSON para uma lista de objetos da classe Cidade
        cidades = gson.fromJson(jsonFileString, listaCidadesType);

        // print cidades
        for(int i = 0; i < cidades.size(); i++)
            Log.i("cidades", "> item " + i + "\n" + cidades.get(i));

    }
}