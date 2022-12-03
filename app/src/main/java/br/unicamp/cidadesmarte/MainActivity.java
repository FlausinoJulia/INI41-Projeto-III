package br.unicamp.cidadesmarte;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<Cidade> cidades;
    private List<Caminho> caminhos;
    private final String[] CRITERIOS = {"Distância", "Tempo", "Custo"};
    private String criterioDeComparacao = "Distância";
    private String cidadeDeOrigem, cidadeDeDestino;
    private Grafo oGrafo;
    private Spinner spinnerCriterios, spinnerOrigem, spinnerDestino;
    private ListView lvCaminhos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // associando lvCaminhos com o list view do layout
        lvCaminhos = (ListView) findViewById(R.id.lvCaminhos);

        // preenchendo o spinner de critérios //
        spinnerCriterios  = (Spinner) findViewById(R.id.spinnerCriterios);
        ArrayAdapter<String> adapterSpinnerCriterios = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CRITERIOS);
        adapterSpinnerCriterios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCriterios.setAdapter(adapterSpinnerCriterios);
        spinnerCriterios.setOnItemSelectedListener(this);

        // lendo o arquivo de cidades //
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cidadesMarte.json");

        Gson gson = new Gson(); // cria uma instanica da classe gson
        Type listaCidadesType = new TypeToken<List<Cidade>>() { }.getType();

        // converte um vetor de JSON para uma lista de objetos da classe Cidade
        cidades = gson.fromJson(jsonFileString, listaCidadesType);

        // spinners para o usuário selecionar a cidade de origem e destino desejadas
        spinnerOrigem  = (Spinner) findViewById(R.id.spinner);
        spinnerDestino = (Spinner) findViewById(R.id.spinner2);

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
        oGrafo = new Grafo();

        // preenchendo o grafo //
        // cada cidade vai ser um vértice do grafo
        for (Cidade cidade : cidades)
        {
            oGrafo.novoVertice(cidade.getNome());
        }

        // preenchendo o grafo (inicialmente o critério é distância)
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
            {
                oGrafo.novaAresta(indiceOrigem, indiceDestino, caminho.getDistancia());
            }
        }

        Button btnBacktracking = findViewById(R.id.btnRecursao);
        btnBacktracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBacktracking();
            }
        });



        Button btnDijkstra = findViewById(R.id.btnDijkstra);
        btnDijkstra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDijkstra();
            }
        });
    }

    public void onClickBacktracking()
    {
        cidadeDeOrigem = spinnerOrigem.getSelectedItem().toString();
        cidadeDeDestino = spinnerDestino.getSelectedItem().toString();

        List<String> todosOsCaminhos = null;
        int indiceCidadeOrigem = -1, indiceCidadeDestino = -1;

        for (int i = 0; i < cidades.size(); i++)
            if (cidades.get(i).getNome().equals(cidadeDeOrigem))
                indiceCidadeOrigem = i;

        for (int i = 0; i < cidades.size(); i++)
            if (cidades.get(i).getNome().equals(cidadeDeDestino))
                indiceCidadeDestino = i;

        if (indiceCidadeOrigem != -1 && indiceCidadeDestino != -1)
            try {
                todosOsCaminhos = oGrafo.acharTodosOsCaminhosRec(indiceCidadeOrigem, indiceCidadeDestino);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todosOsCaminhos);
                lvCaminhos.setAdapter(adapter);
            }
            catch (Exception erro)
            {} // já verificamos no if

    }

    public void onClickDijkstra()
    {
        int indiceCidadeOrigem = -1, indiceCidadeDestino = -1;

        for (int i = 0; i < cidades.size(); i++)
            if (cidades.get(i).getNome().equals(cidadeDeOrigem))
                indiceCidadeOrigem = i;

        for (int i = 0; i < cidades.size(); i++)
            if (cidades.get(i).getNome().equals(cidadeDeDestino))
                indiceCidadeDestino = i;

        String menorCaminho = "";

        if (indiceCidadeOrigem != -1 && indiceCidadeDestino != -1)
            menorCaminho = oGrafo.caminho(indiceCidadeOrigem, indiceCidadeDestino);

        TextView tvMenorCaminho = findViewById(R.id.tvMenorCaminho);
        tvMenorCaminho.setText(menorCaminho);
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

    // on item selected dos spinners //
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String texto = parent.getItemAtPosition(pos).toString();

        switch (parent.getId())
        {
            case R.id.spinnerCriterios:
                criterioDeComparacao = texto;

                // atualizando arestas no grafo para representar cada ligação entre cidades
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
                    {
                        switch (criterioDeComparacao)
                        {
                            case "Distância":
                                oGrafo.novaAresta(indiceOrigem, indiceDestino, caminho.getDistancia());
                                break;
                            case "Tempo":
                                oGrafo.novaAresta(indiceOrigem, indiceDestino, caminho.getTempo());
                                break;
                            case "Custo":
                                oGrafo.novaAresta(indiceOrigem, indiceDestino, caminho.getCusto());
                        }

                    }
                }
                break;
            case R.id.spinner:  // spinner de origem
                cidadeDeOrigem = texto;
                break;
            case R.id.spinner2: // spinner de destino
                cidadeDeDestino = texto;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}