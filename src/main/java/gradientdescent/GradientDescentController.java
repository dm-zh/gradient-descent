package gradientdescent;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequiredArgsConstructor
public class GradientDescentController {

    private final GradientDescentCalculator calculator;

    @GetMapping("/solve")
    public Answer solve() {
        return calculator.calculate();
    }

    @GetMapping(value = "/solve2", produces = "text/csv")
    @SneakyThrows
    public ResponseEntity<Resource> solve2(HttpServletResponse response) {
        Answer answer = calculator.calculate();
//        CSVWriter writer = new CSVWriter(response.getWriter());
//        Object[][] data = answer.getData();
//        Object[] headers = data[0];
//
//        writer.writeNext(new String[]{(String) headers[0], (String) headers[1], (String) headers[2], (String) headers[3]});
//
//        for (int i = 1; i < data.length; i++) {
//
//        }
//
////        for (Object[] datum : data) {
////            writer.writeNext((String[]) datum);
////        }
//        writer.close();



        String[] csvHeader = {
                "sqft_lot", "floors", "view", "price"
        };

        // replace this with your data retrieving logic
//        List<List<String>> csvBody = new ArrayList<>();
//        csvBody.add(Arrays.asList("Patricia", "Williams", "25"));
//        csvBody.add(Arrays.asList("John", "Smith", "44"));
//        csvBody.add(Arrays.asList("Douglas", "Brown", "31"));

        ByteArrayInputStream byteArrayOutputStream;

        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                // defining the CSV printer
                CSVPrinter csvPrinter = new CSVPrinter(
                        new PrintWriter(out),
                        // withHeader is optional
                        CSVFormat.DEFAULT.withHeader(csvHeader)
                );
        ) {
            // populating the CSV content

            for (int i = 1; i < answer.getData().length; i++) {
                csvPrinter.printRecord(answer.getData()[i]);

            }
//            for (List<String> record : csvBody)
//                csvPrinter.printRecord(record);

            // writing the underlying stream
            csvPrinter.flush();

            byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);

        String csvFileName = "answer.csv";

        // setting HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
        // defining the custom Content-Type
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

        return new ResponseEntity<>(
                fileInputStream,
                headers,
                HttpStatus.OK
        );


    }

}
