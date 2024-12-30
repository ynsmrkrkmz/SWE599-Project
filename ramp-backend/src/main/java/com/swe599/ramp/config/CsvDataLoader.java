package com.swe599.ramp.config;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.swe599.ramp.model.Journal;
import com.swe599.ramp.repository.JournalRepository;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CsvDataLoader {

    private final JournalRepository journalRepository;
    private final Environment environment;

    @Bean
    public CommandLineRunner loadCsvData() {
        return args -> {
            // Check if data already exists
            if (journalRepository.count() == 0) {
                System.out.println("No data found in the database. Importing data...");

                String csvFilePath = environment.getProperty("app.csv.file-path");
                assert csvFilePath != null;
                try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFilePath))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build()) {
                    List<Journal> journals = new ArrayList<>();

                    String[] line;
                    csvReader.readNext(); // Skip the header row
                    while ((line = csvReader.readNext()) != null) {
                        double mep = Double.parseDouble(line[7].replace(",", ".")); // Column H: MEP
                        if (mep > 1) { // Only process rows where MEP > 1
                            Journal journal = new Journal();
                            double payment = Double.parseDouble(line[5]);
                            journal.setShortTitle(line[1]); // Kısa Başlık
                            journal.setFullTitle(line[2]);  // Başlık
                            journal.setIssn(line[3]);       // ISSN
                            journal.setEissn(line[4]);      // EISSN
                            journal.setPayment(Double.parseDouble(line[5])); // Ödeme (TL)
                            journal.setYear(Integer.parseInt(line[6]));      // Yıl
                            journal.setMep(mep);     // MEP
                            journal.setSci(line[8]);        // SCI
                            journal.setSoc(line[9]);        // SOC
                            journal.setAhci(line[10]);      // AHCI
                            journal.setSource(line[11]);

                            journals.add(journal);
                        }
                    }

                    journalRepository.saveAll(journals);
                    System.out.println("Data imported successfully!");
                }
            } else {
                System.out.println("Data already exists. Skipping import.");
            }
        };
    }
}

