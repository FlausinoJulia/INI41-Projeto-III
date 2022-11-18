package br.unicamp.cidadesmarte;

public class Caminho {
    private byte tamanhoNome = 15;
    private String cidOrigem, cidDestino;
    private int distancia, tempo, custo; 

        int tamanhoRegistro = tamanhoNome +  // tamanho do nome da cidade de origem
            tamanhoNome +  // tamanho do nome da cidade de destino
            sizeof(int) +
    sizeof(int) +
    sizeof(int);

    public Caminho() // construtor default (sem parametros)
    {
        this.CidOrigem  = "";
        this.CidDestino = "";
        this.Distancia = 0;
        this.Tempo = 0;
        this.Custo = 0;
    }

    public Caminho(String origem, String destino, int dist, int temp, int custo) // construtor parametrizado
    {
        this.CidOrigem  = origem;
        this.CidDestino = destino;
        this.Distancia  = dist;
        this.Tempo      = temp;
        this.Custo      = custo;
    }

    public Caminho(String origem, String destino) // construtor que recebe só a chave (nome) como parâmetro
    {
        this.CidOrigem  = origem;
        this.CidDestino = destino;
    }

    public int TamanhoRegistro = tamanhoRegistro;

    public String CidOrigem{
        get{
            cidOrigem;
        }
        set{
        cidOrigem = value.PadRight(tamanhoNome, ' ').Substring(0, tamanhoNome);
        } 
    }

    public String CidDestino
    {
        get => cidDestino;
        set => cidDestino = value.PadRight(tamanhoNome, ' ').Substring(0, tamanhoNome);
    }

    public int Distancia { get => distancia; set => distancia = value; }

    public int Tempo { get => tempo; set => tempo = value; }

    public int Custo { get => custo; set => custo = value; }

    public int CompareTo(Caminho outroCaminho)
    {
        return cidOrigem.CompareTo(outroCaminho.CidOrigem) + cidDestino.CompareTo(outroCaminho.CidDestino);
    }

    public override string ToString()
    {
        return CidOrigem + CidDestino;
    }

    public void GravarRegistro(BinaryWriter arquivo)
    {
        if (arquivo != null)
        {
            char[] origem = new char[tamanhoNome];  // cria vetor de char para armazenar o nome da cidOrigem
            for (int i = 0; i < tamanhoNome; i++)
                origem[i] = cidOrigem[i];           // copia os caracteres do campo cidOrigem para o vetor de char
            arquivo.Write(origem);                  // grava o vetor de char no arquivo (com tamanho 15)

            char[] destino = new char[tamanhoNome]; // cria vetor de char para armazenar o nome da cidDestino
            for (int i = 0; i < tamanhoNome; i++)
                destino[i] = cidOrigem[i];          // copia os caracteres do campo cidDestino para o vetor de char
            arquivo.Write(destino);                 // grava o vetor de char no arquivo (com tamanho 15)

            arquivo.Write(Distancia);               // escreve os 4 bytes da distancia da origem ao destino no arquivo
            arquivo.Write(Tempo);                   // escreve os 4 bytes do tempo de percurso no arquivo
            arquivo.Write(Custo);                   // escreve os 4 bytes do custo do percurso no arquivo 
        }
    }

    public void LerRegistro(BinaryReader arquivo, long qualRegistro)
    {
        if (arquivo != null)  // arquivo de leitura foi instanciado (já fica aberto)
            try
            {
                long qtosBytes = qualRegistro * TamanhoRegistro;       // (número de bytes pulados para posicionar no registro desejado)
                arquivo.BaseStream.Seek(qtosBytes, SeekOrigin.Begin);  // posicionamento no byte inicial do registro

                // lemos cada um dos campos do registro separadamente
                char[] origem = new char[tamanhoNome];   // criamos um vetor de 15 caracteres 
                origem = arquivo.ReadChars(tamanhoNome); // lemos 15 caracteres do arquivo e guardamosno vetor origem
                string nomeLido = "";
                for (int i = 0; i < tamanhoNome; i++)    // montamos uma variável string com os caracteres do vetor origem
                    nomeLido += origem[i];
                CidOrigem = nomeLido;                    // armazenamos a string montada acima no campo cidOrigem

                char[] destino = new char[tamanhoNome];  // criamos um vetor de 15 caracteres 
                destino = arquivo.ReadChars(tamanhoNome);// lemos 15 caracteres do arquivo e guardamosno vetor destino
                nomeLido = "";
                for (int i = 0; i < tamanhoNome; i++)    // montamos uma variável string com os caracteres do vetor destino
                    nomeLido += destino[i];
                CidOrigem = nomeLido;                    // armazenamos a string montada acima no campo cidDestino

                Distancia = arquivo.ReadInt32(); // lê um int de 4 bytes
                Tempo = arquivo.ReadInt32();     // lê um int de 4 bytes
                Custo = arquivo.ReadInt32();     // lê um int de 4 bytes
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message);
            }
    }
}
}
