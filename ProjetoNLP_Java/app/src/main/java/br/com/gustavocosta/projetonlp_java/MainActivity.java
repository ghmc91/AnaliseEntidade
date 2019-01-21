package br.com.gustavocosta.projetonlp_java;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    //Variáveis
    private EditText textoEditText;
    private Button analisarButton;
    private ProgressBar progressBar;
    private TextView resultadoTextView;

    /**
     * URL para chamada da API. É necessário criar chaves de credenciais
     * e coloca-las no lugar de YOUR_API_KEY
     */
    private static final String URL_API = "https://language.googleapis.com/v1/documents:analyzeEntitySentiment?key=YOUR_API_KEY";

    // Conexão entre as variavéis do código e os elementos do layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoEditText = findViewById(R.id.texto_editText);
        analisarButton = findViewById(R.id.analisar_button);
        progressBar = findViewById(R.id.progressBar);
        resultadoTextView = findViewById(R.id.resultado_textView);

        //Listener para associar ações entre o butão e os outros elementos do app
        analisarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                resultadoTextView.setText("");

                //Instanciando elemento da classe documento  e passando os parâmetros
                // type e contente
                Documento documento = new Documento();
                documento.type = "PLAIN_TEXT";
                documento.content = textoEditText.getText().toString();

                /*documento.content = "I love R&B music. Marvin Gaye is the best." +
                        "'What's Going On' is one of my favorite songs." +
                        "It was so sad when Marvin Gaye died.";*/

                /**
                 * Instanciando elemento da classe AnaliseSentimento onde serão passados
                 * as informações para API do Google analisar
                 */
                AnaliseSentimento as = new AnaliseSentimento();
                as.document = documento;
                //Não é necessário passar a formatação
                //as.encondingType = "UTF8";

                /**
                 * Convertendo a o objeto AnaliseSentimento em JSON
                 */
                Ion.with(MainActivity.this)
                        .load(URL_API)
                        .setJsonPojoBody(as)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                progressBar.setVisibility(View.INVISIBLE);
                                //Recebendo os resultados da análise da API
                                if (e != null)
                                    resultadoTextView.setText("Erro: " + e.getMessage());
                                else {
                                    Gson gson = new Gson();

                                    // Deserializando objeto Json
                                    AnaliseSentimentoResposta asr =
                                            gson.fromJson(result, AnaliseSentimentoResposta.class);

                                    String r = "";

                                    //Loop for para extrair as informações feitas pela análise da API
                                    if (asr.entities != null) {
                                        for (Entidade ent : asr.entities) {
                                            r = r + ent.name + " - " + ent.sentiment.score + " - " + ent.sentiment.magnitude + "\n";
                                        }
                                    } else r = "Sem entidades";

                                    resultadoTextView.setText(r);

                                }
                            }
                        });


            }
        });
    }
}
