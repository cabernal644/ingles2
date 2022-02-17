package com.example.speak;

import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.Normalizer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int RECONOCEDOR_VOZ = 7;
    private TextView escuchando;
    private TextView respuesta;
    private String casa = "";
    private ArrayList<Respuesta> respuest;
    private TextToSpeech leer;
    private ImageView cuchara;
    private ImageView bien ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RECONOCEDOR_VOZ){
            ArrayList<String> reconocido = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String escuchado = reconocido.get(0);
            escuchando.setText(escuchado);
            prepararRespuesta(escuchado);
        }
    }

    private void prepararRespuesta(String escuchado) {
        String normalizar = Normalizer.normalize(escuchado, Normalizer.Form.NFD);
        String sintilde = normalizar.replaceAll("[^\\p{ASCII}]", "");

        int resultado;

        String respuesta = respuest.get(0).getRespuestas();
        for (int i = 0; i < respuest.size(); i++) {
            resultado = sintilde.toLowerCase().indexOf(respuest.get(i).getCuestion());
            if(resultado != -1){
                respuesta = respuest.get(i).getRespuestas();

                if (similitud(respuest.get(i).getCuestion(), sintilde).equals("") && !matarImagen(respuest.get(i).getCuestion(), sintilde).equals("") ){
                    respuesta = respuesta + casa ;
                    cuchara.setImageResource(R.drawable.cuchara_mini);
                }
                if (!similitud(respuest.get(i).getCuestion(), sintilde).equals("") && matarImagen(respuest.get(i).getCuestion(), sintilde).equals("") ){
                    respuesta = respuesta;
                    cuchara.setImageResource(0);

                }


            }
        }
        responder(respuesta);


    }
  //**********************************************************************




    private String similitud(String cuestiones, String escuchado){
        String similar = "";
        if(cuestiones.equals("do you like") == escuchado.equals("do you like")){
            similar ="lo logre";
            casa =similar;
        }

        return similar;

    }
    private String matarImagen(String cuestion, String escuchado){
        String matar = "";
        if(cuestion.equals("spoon") == escuchado.equals("")){
            matar ="lo logre";
        }

        return matar;
    }



//***************************************************************************
    private void responder(String respuestita) {
        respuesta.setText(respuestita);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leer.speak(respuestita, TextToSpeech.QUEUE_FLUSH, null, null);
        }else {
            leer.speak(respuestita, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void inicializar(){
        escuchando = (TextView)findViewById(R.id.tvEscuchando);
        respuesta = (TextView)findViewById(R.id.tvRespuesta);
        respuest = proveerDatos();
        cuchara = (ImageView)findViewById(R.id.cuchara_mini);
        bien = (ImageView)findViewById(R.id.bien);
        leer = new TextToSpeech(this, this);
    }

    public void hablar(View v){
        Intent hablar = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //en-US
        hablar.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        startActivityForResult(hablar, RECONOCEDOR_VOZ);
    }

    public ArrayList<Respuesta> proveerDatos(){
        ArrayList<Respuesta> respuestas = new ArrayList<>();
        respuestas.add(new Respuesta("defecto", "intentalo nuevamente"));
        respuestas.add(new Respuesta("hello", "hello let's start studying cual es el verbo ayudar "));
        respuestas.add(new Respuesta("help", "muy bien" + "     "+"como se pregunta ¿cual es tu nombre? "));
        respuestas.add(new Respuesta("is your name", "muy bien"+"     "+"my name is sofia"+"  "+"como se pregunta tu nesesitas "));
        respuestas.add(new Respuesta("need", "muy bien"+"     "+" como se dice esta pregunta en inglés, ¿Te gusta el chocolate?"));
        respuestas.add(new Respuesta("do you like", "muy bien  vamos con otra pregunta como se dice  una cuchara en inglés"))  ;
        respuestas.add(new Respuesta("spoon", "muy bien  como se dice      yo quiero comprar un  carro"));
        //respuestas.add(new Respuesta("likes water", "muy bien   vamos on una pregunta un poco mas dificil what is the dog doing? "));
        respuestas.add(new Respuesta("want to buy a car", "muy bien  sigamos practicando  ella vende"));
        respuestas.add(new Respuesta("sells", "bien  como se pregunta el coloca el carro en el garaje"));
        respuestas.add(new Respuesta("he puts the car in the garage", "bien What is your favorite movie?"));
        respuestas.add(new Respuesta("favorite movie is", "muy bien  sigamos practicando  cual es el verbo ayudar "));
        return respuestas;
    }

    @Override
    public void onInit(int status) {

    }
}

