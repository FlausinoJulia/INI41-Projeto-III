package br.unicamp.cidadesmarte;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

/*
    Danyelle Nogueira França 21232
    Julia Flausino da Silva  21241
*/

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Cidade[] cidades;
    private List<Caminho> caminhos;
    private final String[] CRITERIOS = {"Distância", "Tempo", "Custo"};
    private String criterioDeComparacao = "Distância";
    private String cidadeDeOrigem, cidadeDeDestino;
    private Grafo oGrafo;
    private Spinner spinnerCriterios, spinnerOrigem, spinnerDestino;
    private ListView lvCaminhos;
    private TextView tvMenorCaminho;

    // para desenhar no mapa //
    private Canvas canvas;
    private Bitmap bitmap, workingBitmap, mutableBitmap;
    private Paint paint;
    private ImageView imageView;
    // desenhar caminhos //
    private Bitmap bitmapCaminho = null;
    private Canvas canvasCaminho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // associando lvCaminhos com o list view do layout
        lvCaminhos = (ListView) findViewById(R.id.lvCaminhos);
        // definindo o evento click dos itens do lvCaminhos
        lvCaminhos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                paint.setColor(Color.BLUE); // a cor dos elementos desenhados vai ser azul

                view.setSelected(true); // marcamos a view como selected

                // desenhamos o caminho selecionado
                String caminho = (String) adapterView.getItemAtPosition(i);
                desenharCaminho(caminho);
            }
        });

        // associando tvMenorCaminho com o text view do layout
        tvMenorCaminho = findViewById(R.id.tvMenorCaminho);

        // preenchendo o spinner de critérios //
        spinnerCriterios  = (Spinner) findViewById(R.id.spinnerCriterios);
        ArrayAdapter<String> adapterSpinnerCriterios = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CRITERIOS);
        adapterSpinnerCriterios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCriterios.setAdapter(adapterSpinnerCriterios);
        spinnerCriterios.setOnItemSelectedListener(this);

        // lendo o arquivo de cidades //
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cidadesMarte.json");

        Gson gson = new Gson(); // cria uma instanica da classe gson
        Type listaCidadesType = new TypeToken<Cidade[]>() { }.getType();

        // converte um vetor de JSON para uma lista de objetos da classe Cidade
        cidades = gson.fromJson(jsonFileString, listaCidadesType);

        // spinners para o usuário selecionar a cidade de origem e destino desejadas
        spinnerOrigem  = (Spinner) findViewById(R.id.spinner);
        spinnerDestino = (Spinner) findViewById(R.id.spinner2);

        spinnerOrigem.setOnItemSelectedListener(this);
        spinnerDestino.setOnItemSelectedListener(this);

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

        // desenhamos as cidades no mapa
        imageView = (ImageView)findViewById(R.id.imgMapa);
        desenharNoMapa();

        // lendo o arquivo de caminhos //
        String jsonFileString2 = Utils.getJsonFromAssets(getApplicationContext(), "caminhosEntreCidadesMarte.json");

        Type listaCaminhosType = new TypeToken<List<Caminho>>() { }.getType();

        // converte um vetor de JSON para uma lista de objetos da classe Cidade
        caminhos = gson.fromJson(jsonFileString2, listaCaminhosType);

        // instanciando o grafo
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

            // procuramos o indice das cidades de origem e de destino
            indiceOrigem = procurarIndiceDaCidade(caminho.getCidadeDeOrigem());
            indiceDestino = procurarIndiceDaCidade(caminho.getCidadeDeDestino());

            if (indiceOrigem != -1 && indiceDestino != -1)
            {
                oGrafo.novaAresta(indiceOrigem, indiceDestino, caminho.getDistancia());
            }
        }

        // setamos o evento click do btnBacktracking
        Button btnBacktracking = findViewById(R.id.btnRecursao);
        btnBacktracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBacktracking(); // chamamos o método de click do botao
            }
        });

        // setamos o evento click do btnDijkstra
        Button btnDijkstra = findViewById(R.id.btnDijkstra);
        btnDijkstra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDijkstra(); // chamamos o método de click do botao
            }
        });
    }

    // método que busca o indice da cidade passada como parâmetro no vetor de cidades //
    public int procurarIndiceDaCidade(String nome)
    {
        // percorremos todo o vetor e se acharmos uma cidade com o mesmo nome passado
        // como parâmetro retornamos o seu indice
        for (int i = 0; i < cidades.length; i++) {
            if (cidades[i].getNome().equals(nome))
            {
                return i;
            }
        }

        return -1; // se não achamos, retornamos -1
    }

    // método click do botão de backtracking //
    public void onClickBacktracking()
    {
        // pegamos as cidades de origem e de destino selecionadas
        cidadeDeOrigem = spinnerOrigem.getSelectedItem().toString();
        cidadeDeDestino = spinnerDestino.getSelectedItem().toString();

        int indiceCidadeOrigem = -1, indiceCidadeDestino = -1;

        // procuramos os indices das cidades de origem e de destino
        indiceCidadeOrigem = procurarIndiceDaCidade(cidadeDeOrigem);
        indiceCidadeDestino = procurarIndiceDaCidade(cidadeDeDestino);

        List<String> todosOsCaminhos = null; // lista que armazenará todos os caminhos
        if (indiceCidadeOrigem != -1 && indiceCidadeDestino != -1)
            try {
                // a lista todosOsCaminhos é preenchida
                todosOsCaminhos = oGrafo.acharTodosOsCaminhosRec(indiceCidadeOrigem, indiceCidadeDestino);

                if (todosOsCaminhos.size() == 0)
                    // se nenhum caminho foi encontrado, avisamos o usuário
                    Toast.makeText(this, "Nenhum caminho encontrado.", Toast.LENGTH_LONG).show();
                else
                {
                    // se achamos caminhos, exibimos eles no lvCaminhos
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view, R.id.itemDaLista, todosOsCaminhos);
                    lvCaminhos.setAdapter(adapter);
                }
            }
            catch (Exception erro)
            {} // já verificamos no if
    }

    // método click do botão de dijkstra //
    public void onClickDijkstra()
    {
        // pegamos as cidades de origem e de destino selecionadas
        cidadeDeOrigem = spinnerOrigem.getSelectedItem().toString();
        cidadeDeDestino = spinnerDestino.getSelectedItem().toString();

        int indiceCidadeOrigem = -1, indiceCidadeDestino = -1;

        // procuramos os indices das cidades de origem e de destino
        indiceCidadeOrigem = procurarIndiceDaCidade(cidadeDeOrigem);
        indiceCidadeDestino = procurarIndiceDaCidade(cidadeDeDestino);

        String menorCaminho = ""; // string que vai guardar o menor caminho

        if (indiceCidadeOrigem != -1 && indiceCidadeDestino != -1)
            // pegamos o menor caminho utilizando dijkstra
            menorCaminho = oGrafo.menorCaminho(indiceCidadeOrigem, indiceCidadeDestino);

        // exibimos o caminho no tvMenorCaminho
        if (menorCaminho.equals("Não há caminho.") || menorCaminho.equals(""))
            tvMenorCaminho.setText("Não há caminho.");
        else {
            tvMenorCaminho.setText("Menor caminho: " + menorCaminho);
        }

        // exibimos o caminho em vermelho
        paint.setColor(Color.parseColor("#B12D2D"));
        desenharCaminho(menorCaminho);
    }

    public void desenharCaminho(String caminho)
    {
        // limpar caminho desenhado
        bitmapCaminho = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvasCaminho = new Canvas(bitmapCaminho);
        canvasCaminho.drawBitmap(mutableBitmap, 0, 0, paint);

        // se houver caminho a ser desenhado
        if (!caminho.isEmpty() && !caminho.equals("Não há caminho."))
        {
            // pegamos o nome de cada cidade e guardamos num vetor
            String[] cidadesDoCaminho = caminho.split(" --> ");

            Cidade origem = null, destino = null;

            // vamos desenhando cada movimento do caminho, da primeira até a penúltima cidade
            // (porque na última não damos mais nenhum passo)
            for (int i = 0; i < cidadesDoCaminho.length - 1; i++)
            {
                // procurando a cidade de origem no vetor de cidades
                for (Cidade cidade : cidades)
                {
                    if (cidade.getNome().equals(cidadesDoCaminho[i]))
                    {
                        origem = cidade;
                        break;
                    }
                }

                // procurando a cidade de destino no vetor de cidades
                for (Cidade cidade : cidades)
                {
                    if (cidade.getNome().equals(cidadesDoCaminho[i + 1]))
                    {
                        destino = cidade;
                        break;
                    }
                }

                // se encontramos as duas cidades no vetor de cidades
                if (origem != null && destino != null)
                {
                    float width = bitmap.getWidth();
                    float height = bitmap.getHeight();

                    // coordenadas da cidade de inicio do movimento
                    float xOrigem = (float) origem.getX() * width;
                    float yOrigem = (float) origem.getY() * height;

                    // coordenadas da cidade de destino do movimento
                    float xDestino = (float) destino.getX() * width;
                    float yDestino = (float) destino.getY() * height;

                    // pintamos de azul a bolinha e o nome das cidades de origem e de destino
                    canvasCaminho.drawCircle(xOrigem, yOrigem, 20, paint);
                    canvasCaminho.drawText(origem.getNome(), xOrigem + 10, yOrigem - 30, paint);

                    canvasCaminho.drawCircle(xDestino, yDestino, 20, paint);
                    canvasCaminho.drawText(destino.getNome(), xDestino + 10, yDestino - 30, paint);

                    // desenhamos a linha que liga as duas cidades
                    canvasCaminho.drawLine(xOrigem, yOrigem, xDestino, yDestino, paint);
                }
            }
        }

        // exibimos no imageView
        imageView.setImageBitmap(bitmapCaminho);
    }

    // método para desenhar no mapa os pontinhos e o nome de cada cidade //
    public void desenharNoMapa(){
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        myOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapa, myOptions);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK); // desenhando na cor preta
        paint.setStrokeWidth(20); // tamanho das linhas a serem desenhadas
        paint.setTextAlign(Paint.Align.CENTER); // alinhamento do texto
        paint.setTextSize(80); // tamanho do texto
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD)); // negrito
        paint.setStyle(Paint.Style.FILL);

        workingBitmap = Bitmap.createBitmap(bitmap);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        canvas = new Canvas(mutableBitmap);

        // desenhamos cada cidade
        for (Cidade cidade : cidades)
        {
            // coordenadas x e y da cidade a ser desenhada
            float x = (float) cidade.getX() * bitmap.getWidth();
            float y = (float) cidade.getY() * bitmap.getHeight();
            String nome = cidade.getNome(); // nome da cidade

            // desenhamos o circulo e o nome
            canvas.drawCircle(x, y, 20, paint);
            canvas.drawText(nome, x + 10, y - 30, paint);
        }

        // exibimos no image view
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);
    }

    // on item selected dos spinners //
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String texto = parent.getItemAtPosition(pos).toString();

        // se temos um caminho desenhado no mapa, limpamos ele
        if (bitmapCaminho != null) {
            bitmapCaminho = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvasCaminho = new Canvas(bitmapCaminho);
            canvasCaminho.drawBitmap(mutableBitmap, 0, 0, paint);
            imageView.setImageBitmap(bitmapCaminho);
        }

        switch (parent.getId())
        {
            case R.id.spinnerCriterios:
                if (!texto.equals(criterioDeComparacao))
                {
                    // limpamos dijkstra e mudamos o critério
                    tvMenorCaminho.setText("");
                    criterioDeComparacao = texto;

                    // atualizando arestas no grafo para representar cada ligação entre cidades
                    for (Caminho caminho : caminhos)
                    {
                        int indiceOrigem = -1, indiceDestino = -1;

                        // procuramos os indices das cidades de origem e de destino
                        indiceOrigem = procurarIndiceDaCidade(caminho.getCidadeDeOrigem());
                        indiceDestino = procurarIndiceDaCidade(caminho.getCidadeDeDestino());

                        // se encontramos as cidades no vetor
                        if (indiceOrigem != -1 && indiceDestino != -1)
                        {
                            // preenchemos o grafo de acordo com o critério escolhido
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
                }
                break;
            case R.id.spinner:  // spinner de origem
                if (!texto.equals(cidadeDeOrigem))
                {
                    // limpamos backtracking e dijkstra
                    tvMenorCaminho.setText("");
                    lvCaminhos.setAdapter(null);

                    // trocamos a cidade de origem
                    cidadeDeOrigem = texto;
                }
                break;
            case R.id.spinner2: // spinner de destino
                if (!texto.equals(cidadeDeDestino))
                {
                    // limpamos backtracking e dijkstra
                    tvMenorCaminho.setText("");
                    lvCaminhos.setAdapter(null);

                    // trocamos a cidade de destino
                    cidadeDeDestino = texto;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}