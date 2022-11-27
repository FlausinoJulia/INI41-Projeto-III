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
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    private List<Cidade> cidades;
    private List<Caminho> caminhos;
    private final String[] CRITERIOS = {"Distância", "Tempo", "Custo"};
    private String criterioDeComparacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // preenchendo o spinner de critérios //
        Spinner spinnerCriterios  = (Spinner) findViewById(R.id.spinnerCriterios);
        ArrayAdapter<String> adapterSpinnerCriterios = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CRITERIOS);
        adapterSpinnerCriterios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCriterios.setAdapter(adapterSpinnerCriterios);

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

        desenharNoMapa();

        // lendo o arquivo de caminhos //
        String jsonFileString2 = Utils.getJsonFromAssets(getApplicationContext(), "caminhosEntreCidadesMarte.json");

        Type listaCaminhosType = new TypeToken<List<Caminho>>() { }.getType();

        // converte um vetor de JSON para uma lista de objetos da classe Cidade
        caminhos = gson.fromJson(jsonFileString2, listaCaminhosType);

        // instanciando um grafo
        Grafo grafo = new Grafo();

        // cada cidade vai ser um vértice do grafo
        for (Cidade cidade : cidades)
        {
            grafo.novoVertice(cidade.getNome());
        }

        // criando arestas no grafo para representar cada ligação entre cidades
        for (Caminho caminho : caminhos)
        {
            int indiceOrigem = -1, indiceDestino = -1;

            for (int i = 0; i < cidades.size(); i++) {
                if (cidades.get(i).getNome().equals(caminho.getCidadeDeOrigem()))
                {
                    indiceOrigem = i;
                    break;
                }

            }

            for (int i = 0; i < cidades.size(); i++) {
                if (cidades.get(i).getNome().equals(caminho.getCidadeDeDestino()))
                {
                    indiceDestino = i;
                    break;
                }
            }

            if (indiceOrigem != -1 && indiceDestino != -1)
                grafo.novaAresta(indiceOrigem, indiceDestino);

        }






    }

    public void desenharNoMapa(){
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapa,myOptions);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(60, 50, 25, paint);

        ImageView imageView = (ImageView)findViewById(R.id.imgMapa);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);
    }

}