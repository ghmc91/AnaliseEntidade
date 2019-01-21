package br.com.gustavocosta.projetonlp_java;

import java.util.List;

public class AnaliseSentimentoResposta {

    public List<Entidade> entities;
}

class Entidade {

    public String name;
    public String type;
    public Sentimento sentiment;
}

class Sentimento {
    public float magnitude;
    public float score;
}
