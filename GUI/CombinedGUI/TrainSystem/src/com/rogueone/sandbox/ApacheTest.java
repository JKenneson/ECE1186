/**
 * @Author Jonathan Kenneson
 * @Date February 2017
 * 
 * Testing some Apache POI stuff
 */

package com.rogueone.sandbox;

//Bunch of imports 
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ApacheTest  { 
    
    public static void main(String[] args) throws IOException {
        
        //Testing in reads, if the file is there
        XSSFWorkbook testWorkbook = new XSSFWorkbook(new FileInputStream("test.xlsx"));
        XSSFSheet sheet = testWorkbook.getSheetAt(0);
        
        //Iterate over all rows in the first column
        for (int i = 0 ; i <= sheet.getLastRowNum() ; i++) {
            Row tempRow = sheet.getRow(i);
            Cell tempCellName = tempRow.getCell(0);             //First cell is the name
            Cell tempCellPoints = tempRow.getCell(1);           //Next is points
            

        }        
        
        //Creating a new sheet
        FileOutputStream fileOut = new FileOutputStream("test_out.xlsx");
        XSSFWorkbook outWorkbook = new XSSFWorkbook();
        XSSFSheet outSheet = outWorkbook.createSheet();
        
        //Write the strings "Name", "Points", "Academic", "Social", "Philanthropy"
        XSSFRow tempRow = outSheet.createRow(0);
        Cell tempCellName = tempRow.createCell(0);
        Cell tempCellPoints = tempRow.createCell(1);
        Cell tempCellAcademic = tempRow.createCell(2);
        Cell tempCellSocial = tempRow.createCell(3);
        Cell tempCellPhilanthropy = tempRow.createCell(4);
        
        tempCellName.setCellValue("Name");
        tempCellPoints.setCellValue("Points");
        tempCellAcademic.setCellValue("Academic");
        tempCellSocial.setCellValue("Social");
        tempCellPhilanthropy.setCellValue("Philanthropy");
        
        //Set some bold titles and center all but the Name column
        CellStyle boldStyle = outWorkbook.createCellStyle();
        Font font = outWorkbook.createFont();
        font.setBold(true);
        boldStyle.setFont(font);
        tempRow = outSheet.getRow(0);
        //Set the name column as bold
        Cell cell = tempRow.getCell(0);
        cell.setCellStyle(boldStyle);
        
        //Set the rest of the titles as bold and centered
        boldStyle.setAlignment(HorizontalAlignment.CENTER);
        for(int i = 1 ; i <= 4 ; i++) {
            cell = tempRow.getCell(i);
            cell.setCellStyle(boldStyle);
        }
        
        //Create a new style for centering
        CellStyle centerStyle = outWorkbook.createCellStyle();
        font = outWorkbook.createFont();
        centerStyle.setFont(font);
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        
        //Auto size some columns
        outSheet.autoSizeColumn(0);
        outSheet.autoSizeColumn(2);
        outSheet.autoSizeColumn(4);
        outSheet.autoSizeColumn(7);
        
        //Close up stuff and tell the user we're done
        outWorkbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
        
        
    }
}
