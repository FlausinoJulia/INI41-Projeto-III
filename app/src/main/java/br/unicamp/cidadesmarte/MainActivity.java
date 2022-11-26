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
    List<Caminho> caminhos;

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

        desenharNoMapa();

        // lendo o arquivo de caminhos //
        String jsonFileString2 = Utils.getJsonFromAssets(getApplicationContext(), "caminhosEntreCidadesMarte.json");

        Type listaCaminhosType = new TypeToken<List<Caminho>>() { }.getType();

        // converte um vetor de JSON para uma lista de objetos da classe Cidade
        caminhos = gson.fromJson(jsonFileString2, listaCaminhosType);

        Grafo grafo = new Grafo();
        for (Cidade cidade : cidades)
        {
            grafo.novoVertice(cidade.getNome());
        }

        for (Caminho caminho : caminhos)
        {
            caminho.getCidadeDeOrigem();

            Cidade origem = new Cidade(caminho.getCidadeDeOrigem());
            Cidade destino = new Cidade(caminho.getCidadeDeDestino());

            grafo.novaAresta(cidades.indexOf(origem), cidades.indexOf(destino));
        }

        // teste 1 - funcionou //
        /*
        grafo.novoVertice("A");
        grafo.novoVertice("B");
        grafo.novoVertice("C");
        grafo.novoVertice("D");
        grafo.novoVertice("E");

        grafo.novaAresta(0, 1); // A e B
        grafo.novaAresta(0, 2); // A e C
        grafo.novaAresta(0, 4); // A e E

        grafo.novaAresta(1, 2); // B e C
        grafo.novaAresta(1, 4); // B e E

        grafo.novaAresta(2, 0); // C e A
        grafo.novaAresta(2, 1); // C e B
        grafo.novaAresta(2, 3); // C e D

        grafo.novaAresta(3, 0); // D e A
        grafo.novaAresta(3, 1); // D e B
        grafo.novaAresta(3, 2); // D e C
        grafo.novaAresta(3, 4); // D e E

        grafo.novaAresta(4, 1); // E e B

        List<String> todosCaminhos;
        todosCaminhos = grafo.acharTodosOsCaminhosRec(0, 1);
         */

        // teste 2 - funcionou //
        /*
        grafo.novoVertice("1");
        grafo.novoVertice("2");
        grafo.novoVertice("3");

        grafo.novaAresta(0,1);  // 1 e 2
        grafo.novaAresta(0, 2); // 1 e 3

        grafo.novaAresta(1, 0); // 2 e 1
        grafo.novaAresta(1, 2); // 2 e 3

        List<String> todosCaminhos;
        todosCaminhos = grafo.acharTodosOsCaminhosRec(0, 2);
        */

        // teste 3 - funcionou //
        /*
        grafo.novoVertice("A");
        grafo.novoVertice("B");
        grafo.novoVertice("C");
        grafo.novoVertice("D");
        grafo.novoVertice("E");

        grafo.novaAresta(0,1);
        grafo.novaAresta(0,2);
        grafo.novaAresta(0,4);

        grafo.novaAresta(1,0);
        grafo.novaAresta(1,2);
        grafo.novaAresta(1,3);
        grafo.novaAresta(1,4);

        grafo.novaAresta(2,0);
        grafo.novaAresta(2,1);
        grafo.novaAresta(2,4);

        grafo.novaAresta(3,1);
        grafo.novaAresta(3,2);
        grafo.novaAresta(3,4);

        grafo.novaAresta(4,0);
        grafo.novaAresta(4,2);

        List<String> todosCaminhos;
        todosCaminhos = grafo.acharTodosOsCaminhosRec(0, 3);
         */

        // teste 4 //
        /*
        grafo.novoVertice("Um");
        grafo.novoVertice("Dois");
        grafo.novoVertice("Três");

        List<String> todosCaminhos;
        try
        {
            todosCaminhos = grafo.acharTodosOsCaminhosRec(0, 7);
        }
        catch (Exception e)
        {}

         */





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