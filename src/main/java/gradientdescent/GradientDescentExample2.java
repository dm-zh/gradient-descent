package gradientdescent;

import com.opencsv.CSVReader;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.util.Random;

import static gradientdescent.GradientDescentCalculator.normalizeInputParams;
import static gradientdescent.GradientDescentCalculator.parseData;

public class GradientDescentExample2 {
    @SneakyThrows
    public static void main(String[] args) {
        // Generate random data
//        int numDataPoints = 1000;
//        double[] x1 = new double[numDataPoints];
//        double[] x2 = new double[numDataPoints];
//        double[] x3 = new double[numDataPoints];
//        double[] y = new double[numDataPoints];
        Random rand = new Random();
//        for (int i = 0; i < numDataPoints; i++) {
//            x1[i] = rand.nextDouble();
//            x2[i] = rand.nextDouble();
//            x3[i] = rand.nextDouble();
//            y[i] = 2*x1[i] + 3*x2[i] + 4*x3[i] + rand.nextGaussian();
//        }


        CSVReader reader = new CSVReader(new FileReader("src/main/resources/kc_house_data.csv"));
        var inputParams = parseData(reader);
        var normalizedParams = normalizeInputParams(inputParams);
        var price = inputParams.get(3);

        double[] x1 = normalizedParams.get(0);
        double[] x2 = normalizedParams.get(1);
        double[] x3 = normalizedParams.get(2);
        double[] y = price;
        int numDataPoints = price.length;

        // Initialize weights
        double w0 = rand.nextDouble();
        double w1 = rand.nextDouble();
        double w2 = rand.nextDouble();
        double w3 = rand.nextDouble();
        double learningRate = 0.01;

        // Train model using gradient descent
        int numIterations = 10000;
        for (int i = 0; i < numIterations; i++) {
            // Compute predictions
            double[] yPred = new double[numDataPoints];
            for (int j = 0; j < numDataPoints; j++) {
                yPred[j] = w0 +  w1 * x1[j] + w2 * x2[j] + w3 * x3[j];
            }

            // Compute gradients
            double grad0 = 0;
            double grad1 = 0;
            double grad2 = 0;
            double grad3 = 0;
            for (int j = 0; j < numDataPoints; j++) {
                grad0 += (yPred[j] - y[j]);
                grad1 += (yPred[j] - y[j]) * x1[j];
                grad2 += (yPred[j] - y[j]) * x2[j];
                grad3 += (yPred[j] - y[j]) * x3[j];
            }
            grad0 *= 2.0 / numDataPoints;
            grad1 *= 2.0 / numDataPoints;
            grad2 *= 2.0 / numDataPoints;
            grad3 *= 2.0 / numDataPoints;

            // Update weights
            w0 -= learningRate * grad0;
            w1 -= learningRate * grad1;
            w2 -= learningRate * grad2;
            w3 -= learningRate * grad3;

            // Compute and print loss
            double loss = 0;
            for (int j = 0; j < numDataPoints; j++) {
                double error = yPred[j] - y[j];
                loss += error * error;
            }
            loss /= numDataPoints;
            System.out.println("Iteration " + (i + 1) + ": loss = " + loss);
        }

        // Print final weights
        System.out.println("Final weights: w1 = " + w1 + ", w2 = " + w2 + ", w3 = " + w3 + ", w0 = " + w0);
    }

}
