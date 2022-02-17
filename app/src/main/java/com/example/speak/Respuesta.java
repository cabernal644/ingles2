package com.example.speak;

public class Respuesta {
    private String cuestion;
    private String respuestas;

    public Respuesta(String cuestion, String respuestas) {
        this.cuestion = cuestion;
        this.respuestas = respuestas;
    }

    public String getCuestion() {
        return cuestion;
    }

    public String getRespuestas() {
        return respuestas;
    }
}
