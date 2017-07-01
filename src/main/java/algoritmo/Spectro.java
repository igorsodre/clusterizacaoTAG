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
        System.out.println("Valores em 0 : " + clusters[0] + "\nValores em 1: " + clusters[1] + "\nValores em 2: " + clusters[2] + "\nValores em 3: "  + clusters[3]);
    }

    private void setValoresCentroides(double[][] centroides) {

        centroides[0][0] = -1.5F;
        centroides[0][1] = -1.5F;
        centroides[0][2] = -1.5F;
        centroides[0][3] = -1.5F;

        centroides[1][0] = -0.5F;
        centroides[1][1] = -0.5F;
        centroides[1][2] = -0.5F;
        centroides[1][3] = -0.5F;

        centroides[2][0] = 0.5F;
        centroides[2][1] = 0.5F;
        centroides[2][2] = 0.5F;
        centroides[2][3] = 0.5F;

        centroides[3][0] = 1.5F;
        centroides[3][1] = 1.5F;
        centroides[3][2] = 1.5F;
        centroides[3][3] = 1.5F;

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