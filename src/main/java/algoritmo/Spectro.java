package algoritmo;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import ca.pjer.ekmeans.EKmeans;
import entidades.Grafo;

import java.text.MessageFormat;
import java.util.Random;

public class Spectro {
    private Grafo grafo;
    private Matrix matrizLaplace;
    private Matrix autoVetor;
    private double[][] matrizAutoVetor;
    public double[][] matrizNormalizada;

    public Spectro(Grafo grafo) {
        this.grafo = grafo;
        matrizLaplace = new Matrix(grafo.getMatrizLaplace());
        EigenvalueDecomposition evd = matrizLaplace.eig();
        double[] lambda = evd.getRealEigenvalues();
        autoVetor = evd.getV();
        matrizAutoVetor = autoVetor.getArray();

        normalizaColuna(matrizAutoVetor, 4);

    }

    private void normalizaColuna(double[][] matrizAutoVetor, int numLinhas) {
        int numColunas = matrizAutoVetor.length;

        matrizNormalizada = new double[numLinhas][numColunas];
        for (int j = 0; j < numColunas; j++) {
            double denominador = getSomaColuna(matrizAutoVetor, j, numLinhas);
            for (int i = 0; i < numLinhas; i++) {
                matrizNormalizada[i][j] = matrizAutoVetor[i][j] / denominador;
            }
        }
        kmeans();
    }

    private void kmeans() {
        int numDados = matrizAutoVetor.length;
        int numKlusters = 4;
        Random random = new Random(System.currentTimeMillis());
        double[][] centroides = new double[numKlusters][numKlusters];

        setValoresCentroides(centroides);
        Matrix normalizada = new Matrix(matrizNormalizada);
        normalizada = normalizada.transpose();
        double[][] transposta = normalizada.getArray();
        EKmeans ekmeans = new EKmeans(centroides, transposta);
        ekmeans.setIteration(32);
        ekmeans.run();

        imprimirClusters(numDados, ekmeans);
    }

    private void imprimirClusters(int numDados, EKmeans ekmeans) {
        int[] assignments = ekmeans.getAssignments();
        int[] clusters = new int[4];
        for (int i = 0; i < numDados; i++) {
            System.out.println(MessageFormat.format("point {0} is assigned to cluster {1}", i, assignments[i]));
            switch (assignments[i]) {
                case 0:
                    clusters[0]++;
                    break;
                case 1:
                    clusters[1]++;
                    break;
                case 2:
                    clusters[2]++;
                    break;
                case 3:
                    clusters[3]++;
                    break;
            }
        }
        System.out.println("Valores no grupo 0: " + clusters[0] + "\nValores no grupo 1: " + clusters[1] + "\nValores no grupo 2: " + clusters[2] + "\nValores no grupo 3: "  + clusters[3]);
    }

    private void setValoresCentroides(double[][] centroides) {
        centroides[0][0] = 0.8620237570241243;
        centroides[0][1] = 0.2226356379637249;
        centroides[0][2] = 0.4415966497793408;
        centroides[0][3] = 0.11108921612002348;

        centroides[1][0] = -0.2077484041997292;
        centroides[1][1] = 0.04733246314486677;
        centroides[1][2] = 0.9612319695173597;
        centroides[1][3] = -0.17502382484360068;

        centroides[2][0] = -0.285953056403149;
        centroides[2][1] = -0.3505921575617276;
        centroides[2][2] = 0.8848879031347996;
        centroides[2][3] = 0.11085750978444221;

        centroides[3][0] = 0.27557410928867676;
        centroides[3][1] = -0.4586600684802537;
        centroides[3][2] = 0.016018025469504024;
        centroides[3][3] = -0.8446497941345275;
    }

    private double getSomaColuna(double[][] matrizAutoVetor, int j, int numLinhas) {
        double result = 0;
        for (int i = 0; i < numLinhas; i++) {
            result += Math.pow(matrizAutoVetor[i][j], 2);
        }
        return Math.sqrt(result);
    }

    public void doIt() {
        Grafo grafo;
    }

    public Grafo getGrafo() {
        return grafo;
    }

    public void setGrafo(Grafo grafo) {
        this.grafo = grafo;
    }

    public Matrix getMatrizLaplace() {
        return matrizLaplace;
    }

    public void setMatrizLaplace(Matrix matrizLaplace) {
        this.matrizLaplace = matrizLaplace;
    }

    public Matrix getAutoVetor() {
        return autoVetor;
    }

    public void setAutoVetor(Matrix autoVetor) {
        this.autoVetor = autoVetor;
    }
}