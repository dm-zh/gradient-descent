package gradientdescent;

import java.util.Random;

public class GradientDescentExample {

    public static void main(String[] args) {
        double[] theta = new double[] {0.0, 0.0, 0.0}; // initial values of theta
        double[][] data = generateData(100); // generate some training data
        double alpha = 0.01; // learning rate
        int numIterations = 1000; // number of iterations

        // run gradient descent
        for (int i = 0; i < numIterations; i++) {
            double[] gradients = computeGradients(data, theta); // compute gradients
            for (int j = 0; j < theta.length; j++) {
                theta[j] = theta[j] - alpha * gradients[j]; // update theta
            }
        }

        // print final theta values
        System.out.println("Final theta values:");
        for (int j = 0; j < theta.length; j++) {
            System.out.println("theta[" + j + "] = " + theta[j]);
        }
    }

    // generate some random training data
    private static double[][] generateData(int numExamples) {
        Random rand = new Random();
        double[][] data = new double[numExamples][4];
        for (int i = 0; i < numExamples; i++) {
            double x1 = rand.nextDouble() * 10;
            double x2 = rand.nextDouble() * 10;
            double x3 = rand.nextDouble() * 10;
            double y = 2 * x1 + 3 * x2 + 4 * x3 + rand.nextGaussian(); // add some noise to y
            data[i] = new double[] {x1, x2, x3, y};
        }
        return data;
    }

    // compute gradients for MSE
    private static double[] computeGradients(double[][] data, double[] theta) {
        double[] gradients = new double[3];
        for (int i = 0; i < data.length; i++) {
            double x1 = data[i][0];
            double x2 = data[i][1];
            double x3 = data[i][2];
            double y = data[i][3];
            double yHat = theta[0] * x1 + theta[1] * x2 + theta[2] * x3;
            gradients[0] += (yHat - y) * x1;
            gradients[1] += (yHat - y) * x2;
            gradients[2] += (yHat - y) * x3;
        }
        for (int j = 0; j < gradients.length; j++) {
            gradients[j] /= data.length;
        }
        return gradients;
    }
}

