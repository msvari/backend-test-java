package com.fcamara.park.service.impl;

import com.fcamara.park.model.Park;
import com.fcamara.park.service.SpotService;
import com.fcamara.park.service.ReportService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import static com.fcamara.park.util.DateUtil.convertToLocalDateTime;
import static com.fcamara.park.util.DateUtil.formatDate;

@Service
@Slf4j
@AllArgsConstructor
public class ReportParkServiceImpl implements ReportService {

    private final ParkServiceImpl parkService;
    private final SpotService spotService;

    public InputStreamResource report(UUID spotId, String initialDate, String finalDate) {
        var spot = spotService.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        String title = "Relatório de Estacionamento - " + spot.getName();

        List<String> columnHeaders = Arrays.asList("Veículo", "Placa", "Tipo", "Data/Hora Entrada", "Data/Hora Saída");

        List<Map<String, String>> rows = new ArrayList<>();

        LocalDateTime formatedInicialDate = Objects.isNull(initialDate) ? null : convertToLocalDateTime(initialDate);
        LocalDateTime formatedFinalDate = Objects.isNull(finalDate) ? null : convertToLocalDateTime(finalDate);
        List<Park> parkList = parkService.findAllBySpot(spot).stream()
                .filter(park -> (formatedInicialDate == null || park.getEntryDate().isAfter(formatedInicialDate) || park.getEntryDate().isEqual(formatedInicialDate))
                        && (formatedFinalDate == null || park.getEntryDate().isBefore(formatedFinalDate) || park.getEntryDate().isEqual(formatedFinalDate)))
                .toList();

        for (Park park : parkList) {
            Map<String, String> row = new HashMap<>();
            row.put("Veículo", park.getVehicle().getModel());
            row.put("Placa", park.getVehicle().getLicencePlate());
            row.put("Tipo", park.getVehicle().getType().getDisplayName());
            row.put("Horário Entrada", formatDate(park.getEntryDate(), "dd/MM/yyyy hh:mm:ss"));
            row.put("Horário Saída", park.getExitDate() != null ? formatDate(park.getExitDate(), "dd/MM/yyyy hh:mm:ss") : "Estacionado");
            rows.add(row);
        }

        Map<String, Long> summary = new HashMap<>();
        summary.put("Total de Entradas", (long) parkList.size());
        summary.put("Total de Saídas", parkList.stream().filter(park -> park.getExitDate() != null).count());

        return generateReport(title, columnHeaders, rows, summary);
    }

    @Override
    public InputStreamResource generateReport(String title, List<String> columnHeaders, List<Map<String, String>> rows, Map<String, Long> summary) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph(title));

            float[] columnWidths = new float[columnHeaders.size()];
            for (int i = 0; i < columnHeaders.size(); i++) {
                columnWidths[i] = 150F;
            }
            Table table = new Table(columnWidths);

            for (String header : columnHeaders) {
                table.addCell(header);
            }

            for (Map<String, String> row : rows) {
                for (String header : columnHeaders) {
                    table.addCell(row.get(header));
                }
            }

            document.add(table);

            document.add(new Paragraph("\nSumário:"));
            for (Map.Entry<String, Long> entry : summary.entrySet()) {
                document.add(new Paragraph(entry.getKey() + ": " + entry.getValue()));
            }

            document.close();
        } catch (Exception e) {
            log.error("state=generate-report-error", e);
        }

        return new InputStreamResource(new ByteArrayInputStream(outStream.toByteArray()));
    }

}
