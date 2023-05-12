package gradientdescent;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.math3.stat.StatUtils;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class GradientDescentCalculator {

    private final static List<String> inputs = List.of("sqft_lot", "floors", "view");
    private final static String output = "price";

    @SneakyThrows
    public Answer calculateDefault() {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/kc_house_data.csv"))) {
            var inputParams = parseData(reader);
            var normalizedInputParams = normalizeInputParams(inputParams.subList(0, 3));
            var price = normalizeInputParams(inputParams.subList(3, 4)).get(0);
//            System.out.println(normalizedParams);

            double[] gradient = computeGradients(normalizedInputParams, price);

//            System.out.println(inputParams);

//            Object[][] data = new Object[price.length + 1][4];
//            Object[] headers = new Object[]{"sqft_lot", "floors", "view", "price"};
//            data[0] = headers;
//            for (int i = 0; i < price.length; i++) {
//                Object[] row = new Object[4];
//                row[0] = normalizedInputParams.get(0)[i];
//                row[1] = normalizedInputParams.get(1)[i];
//                row[2] = normalizedInputParams.get(2)[i];
//                row[3] = price[i];
//                data[i+1] = row;
//            }
            Object[][] sqft_lot = new Object[price.length + 1][2];
            Object[] headers = new Object[]{"sqft_lot", "price"};
            sqft_lot[0] = headers;
            for (int i = 0; i < price.length; i++) {
                Object[] row = new Object[2];
                row[0] = normalizedInputParams.get(2)[i];
//                row[1] = normalizedInputParams.get(1)[i];
//                row[2] = normalizedInputParams.get(2)[i];
                row[1] = price[i];
                sqft_lot[i+1] = row;
            }
            return Answer.builder().gradient(gradient).sqft_lot(sqft_lot).build();
        }
    }

    private double[] computeGradients(List<double[]> normalizedParams, double[] price) {
        var rand = new Random();

        double[] x1 = normalizedParams.get(0);
        double[] x2 = normalizedParams.get(1);
        double[] x3 = normalizedParams.get(2);
        double[] y = price;

        int samplesLength = price.length;

        double w0 = rand.nextDouble();
        double w1 = rand.nextDouble();
        double w2 = rand.nextDouble();
        double w3 = rand.nextDouble();
        double learningRate = 0.01;

        var iterations = 10000;


        for (int i = 0; i < iterations; i++) {


            double[] yPred = new double[samplesLength];
            for (int j = 0; j < samplesLength; j++) {
                yPred[j] = w0 + w1 * x1[j] + w2 * x2[j] + w3 * x3[j];
            }

            // Compute gradients
            double grad0 = 0;
            double grad1 = 0;
            double grad2 = 0;
            double grad3 = 0;
            for (int j = 0; j < samplesLength; j++) {
                grad0 += (yPred[j] - y[j]);
                grad1 += (yPred[j] - y[j]) * x1[j];
                grad2 += (yPred[j] - y[j]) * x2[j];
                grad3 += (yPred[j] - y[j]) * x3[j];
            }
            grad0 *= 2.0 / samplesLength;
            grad1 *= 2.0 / samplesLength;
            grad2 *= 2.0 / samplesLength;
            grad3 *= 2.0 / samplesLength;

            // Update weights
            w0 -= learningRate * grad0;
            w1 -= learningRate * grad1;
            w2 -= learningRate * grad2;
            w3 -= learningRate * grad3;

            // Compute and print loss
            double loss = 0;
            for (int j = 0; j < samplesLength; j++) {
                double error = yPred[j] - y[j];
                loss += error * error;
            }
            loss /= samplesLength;

            System.out.println("Iteration " + (i + 1) + ": loss = " + loss);
            System.out.println(Arrays.toString(new double[]{w0, w1, w2, w3}));
        }

        return new double[]{w0, w1, w2, w3};
    }


    public static List<double[]> normalizeInputParams(List<double[]> inputParams) {
        List<double[]> normalizedParams = new ArrayList<>();
        for (int i = 0; i < inputParams.size(); i++) {
            double[] dataBeforeNormalization = inputParams.get(i);
//            System.out.println(Arrays.toString(dataBeforeNormalization));

            double min = StatUtils.min(dataBeforeNormalization);
            double max = StatUtils.max(dataBeforeNormalization);
            double[] dataAfterNormalization = new double[dataBeforeNormalization.length];
            for (int i1 = 0; i1 < dataBeforeNormalization.length; i1++) {
                dataAfterNormalization[i1] = (dataBeforeNormalization[i1] - min) / (max - min);
            }
            normalizedParams.add(dataAfterNormalization);

        }
        return normalizedParams;
    }

    public static List<double[]> parseData(CSVReader reader) throws IOException, CsvException {
        List<double[]> inputParams = new ArrayList<>();

        List<String[]> rawData = reader.readAll();
        List<String> headers = List.of(rawData.get(0));
        for (String input : inputs) {
            int i = headers.indexOf(input);
            double[] answer = new double[rawData.size() - 1];

            for (int i1 = 1; i1 < rawData.size(); i1++) {
                String v = rawData.get(i1)[i];
                answer[i1 - 1] = (Double.valueOf(v));
            }
            inputParams.add(answer);
        }

        {
            int i = headers.indexOf(output);
            double[] answer = new double[rawData.size() - 1];

            for (int i1 = 1; i1 < rawData.size(); i1++) {
                String v = rawData.get(i1)[i];
                answer[i1 - 1] = (Double.valueOf(v));
            }
            inputParams.add(answer);
        }
        return inputParams;
    }
}
