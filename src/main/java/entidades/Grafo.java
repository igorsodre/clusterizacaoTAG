package entidades;

import java.io.*;

public class Grafo {

    private double[][] matrizAfinidade;
    private double[][] matrizDiagonal;
    private double[][] matrizLaplace;
    private int[] resultadoOriginal;
    private double sigma;


    public Grafo() throws IOException {
        this.sigma = (double) 50;
        construirMatriz();
    }

    private void construirMatriz() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("./car.data"));
        int numeroLinhas = countLines("./car.data");


        inicializaMatriz(numeroLinhas);
        double[][] data = new double[numeroLinhas][6];


        for (int i = 0; i < numeroLinhas; i++) {
            String linha = br.readLine();
            String[] dados = linha.split(",");
            data[i][0] = trataBuyingData(dados[0]);
            data[i][1] = trataMaintData(dados[1]);
            data[i][2] = trataDoorsData(dados[2]);
            data[i][3] = trataPersonsData(dados[3]);
            data[i][4] = trataLugData(dados[4]);
            data[i][5] = trataSafetyData(dados[5]);
            this.resultadoOriginal[i] = trataResultado(dados[6]);

        }
        br.close();
        constroiMatrizAfinidade(data, numeroLinhas);
        constroiMatrizDiagonalLaplace(numeroLinhas);
    }


    private void constroiMatrizDiagonalLaplace(int numeroLinhas) {
        double soma = 0;
        for (int i = 0; i < numeroLinhas; i++) {
            for (int j = 0; j < numeroLinhas; j++) {
                soma += matrizAfinidade[i][j];
                matrizDiagonal[i][j] = (double) 0;

                if (i != j) matrizLaplace[i][j] = -matrizAfinidade[i][j];
            }
            matrizDiagonal[i][i] = soma;
            matrizLaplace[i][i] = matrizDiagonal[i][i];
            soma = 0;
        }

    }


    private void constroiMatrizAfinidade(double[][] data, int numeroLinhas) {
        for (int i = 0; i < numeroLinhas; i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) matrizAfinidade[i][j] = (double) 0;
                else {
                    matrizAfinidade[i][j] = Math.exp(-(euclidDist(data, i, j)) / (2 * sigma * sigma));
                    matrizAfinidade[j][i] = matrizAfinidade[i][j];
                }
            }
        }

    }


    private double euclidDist(double[][] data, int i, int j) {
        double x0 = data[i][0] - data[j][0];
        double x1 = data[i][1] - data[j][1];
        double x2 = data[i][2] - data[j][2];
        double x3 = data[i][3] - data[j][3];
        double x4 = data[i][4] - data[j][4];
        double x5 = data[i][5] - data[j][5];
        return Math.sqrt(Math.pow(x0, 2) + Math.pow(x1, 2) + Math.pow(x2, 2)
                + Math.pow(x3, 2) + Math.pow(x4, 2) + Math.pow(x5, 2));
    }


    private int trataResultado(String string) {
        int resultado = 0;

        if (string.equals("unacc")) resultado = 0;
        if (string.equals("acc")) resultado = 1;
        if (string.equals("good")) resultado = 2;
        if (string.equals("vgood")) resultado = 3;

        return resultado;
    }


    private double trataSafetyData(String string) {
        double data = 0;

        if (string.equals("low")) data = 0;
        if (string.equals("med")) data = 1;
        if (string.equals("high")) data = 2;

        return data;
    }


    private double trataLugData(String string) {
        double data = 0;

        if (string.equals("small")) data = 0;
        if (string.equals("med")) data = 1;
        if (string.equals("big")) data = 2;


        return data;
    }


    private double trataPersonsData(String string) {
        double data = 0;

        if (string.equals("2")) data = 0;
        if (string.equals("4")) data = 1;
        if (string.equals("more")) data = 2;

        return data;
    }


    private double trataDoorsData(String string) {
        double data = 0;

        if (string.equals("2")) data = 0;
        if (string.equals("3")) data = 1;
        if (string.equals("4")) data = 2;
        if (string.equals("5more")) data = 3;

        return data;
    }


    private double trataMaintData(String string) {
        double data = 0;

        if (string.equals("vhigh")) data = 0;
        if (string.equals("high")) data = 1;
        if (string.equals("med")) data = 2;
        if (string.equals("low")) data = 3;

        return data;
    }


    private double trataBuyingData(String string) {
        double data = 0;

        if (string.equals("vhigh")) data = 0;
        if (string.equals("high")) data = 1;
        if (string.equals("med")) data = 2;
        if (string.equals("low")) data = 3;

        return data;

    }


    private void inicializaMatriz(int numeroLinhas) {
        matrizAfinidade = new double[numeroLinhas][numeroLinhas];
        matrizDiagonal = new double[numeroLinhas][numeroLinhas];
        matrizLaplace = new double[numeroLinhas][numeroLinhas];
        resultadoOriginal = new int[numeroLinhas];

    }


    //retirado de https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }


    public double[][] getMatrizLaplace() {
        return matrizLaplace;
    }


    public void setMatrizLaplace(double[][] matrizLaplace) {
        this.matrizLaplace = matrizLaplace;
    }


    public double[][] getMatrizDiagonal() {
        return matrizDiagonal;
    }


    public void setMatrizDiagonal(double[][] matrizDiagonal) {
        this.matrizDiagonal = matrizDiagonal;
    }


    public double[][] getMatrizAfinidade() {
        return matrizAfinidade;
    }


    public void setMatrizAfinidade(double[][] matrizAfinidade) {
        this.matrizAfinidade = matrizAfinidade;
    }


}
