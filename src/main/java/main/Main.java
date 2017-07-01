package main;

import algoritmo.Spectro;
import entidades.Grafo;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Grafo grafo = new Grafo();
        new Spectro(grafo);
    }
}
