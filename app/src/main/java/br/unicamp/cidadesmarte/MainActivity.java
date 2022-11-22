package br.unicamp.cidadesmarte;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    List<Cidade> cidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // lendo o arquivo de cidades //
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cidadesMarte.json");

        Gson gson = new Gson(); // cria uma instanica da classe gson
        Type listaCidadesType = new TypeToken<List<Cidade>>() { }.getType();

        // converte um vetor de JSON para uma lista de objetos da classe Cidade
        cidades = gson.fromJson(jsonFileString, listaCidadesType);

        // spinners para o usuário selecionar a cidade de origem e destino desejadas
        Spinner spinnerOrigem  = (Spinner) findViewById(R.id.spinner);
        Spinner spinnerDestino = (Spinner) findViewById(R.id.spinner2);

        // array adapter das cidades de origem para o spinner origem
        ArrayAdapter<Cidade> adapter = new ArrayAdapter<Cidade>(this, android.R.layout.simple_spinner_item, cidades);
        // especificando o layout da lista que aparece quando clicamos no spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicando o adapter para o spinner
        spinnerOrigem.setAdapter(adapter);

        // array adapter das cidades de destino para o spinner destino
        ArrayAdapter<Cidade> adapter2 = new ArrayAdapter<Cidade>(this, android.R.layout.simple_spinner_item, cidades);
        // especificando o layout da lista que aparece quando clicamos no spinner
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicando o adapter de cidades de desitno para o spinner destino
        spinnerDestino.setAdapter(adapter2);


    }

    public void desenharNoMapa(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapa);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(60, 50, 25, paint);

        ImageView imageView = (ImageView)findViewById(R.id.imgMapa);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(bitmap);
    }

}