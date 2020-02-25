package com.xiaoshu.test;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestPOI {
    @Test
    public void testOut(){
        List<Student> students = new ArrayList<>();
        students.add(new Student(1,"张三1"));
        students.add(new Student(2,"张三2"));
        students.add(new Student(3,"张三3"));
        students.add(new Student(4,"张三4"));
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("id");
        titleRow.createCell(1).setCellValue("姓名");
        for (Student s: students) {
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(s.getId());
            dataRow.createCell(1).setCellValue(s.getName());
        }
        try {
            FileOutputStream outputStream = new FileOutputStream("d:/student.xls");
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testIn(){
        try {
            FileInputStream inputStream = new FileInputStream("d:/student.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <=lastRowNum ; i++) {
                Student student = new Student();
                HSSFRow dataRow = sheet.getRow(i);
                Integer id =(int) dataRow.getCell(0).getNumericCellValue();
                String name = dataRow.getCell(1).getStringCellValue();
                System.out.println(id+"-----"+name);
                student.setId(id);
                student.setName(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
