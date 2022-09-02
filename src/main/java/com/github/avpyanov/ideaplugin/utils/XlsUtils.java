package com.github.avpyanov.ideaplugin.utils;

import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.model.TestStep;
import com.intellij.psi.PsiMethod;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class XlsUtils {

    public static void exportToXls(final Map<PsiMethod, TestCase> testCasesMap, final String fileName) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Tests");
            Row header = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();

            XSSFFont font = workbook.createFont();
            font.setFontName("Calibri");
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);

            Cell headerCell = header.createCell(0);
            headerCell.setCellValue("Epic");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("Feature");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(2);
            headerCell.setCellValue("Story");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(3);
            headerCell.setCellValue("Test name");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(4);
            headerCell.setCellValue("Test steps");
            headerCell.setCellStyle(headerStyle);

            int startRowIndex = 1;

            CellStyle style = workbook.createCellStyle();
            style.setVerticalAlignment(VerticalAlignment.TOP);
            style.setWrapText(true);

            for (Map.Entry<PsiMethod, TestCase> entry : testCasesMap.entrySet()) {

                Row row = sheet.createRow(startRowIndex);
                Cell cell = row.createCell(0);
                cell.setCellValue(entry.getValue().getEpic());
                cell.setCellStyle(style);

                cell = row.createCell(1);
                cell.setCellValue(entry.getValue().getFeature());
                cell.setCellStyle(style);

                cell = row.createCell(2);
                cell.setCellValue(entry.getValue().getStory());
                cell.setCellStyle(style);

                cell = row.createCell(3);
                cell.setCellValue(entry.getValue().getName());
                cell.setCellStyle(style);

                for (TestStep step : entry.getValue().getSteps()) {
                    Row currentRow = sheet.getRow(startRowIndex);
                    if (currentRow == null) {
                        currentRow = sheet.createRow(startRowIndex);
                    }
                    cell = currentRow.createCell(4);
                    cell.setCellStyle(style);
                    cell.setCellValue(step.getName());
                    startRowIndex++;
                }
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private XlsUtils() {
    }
}
