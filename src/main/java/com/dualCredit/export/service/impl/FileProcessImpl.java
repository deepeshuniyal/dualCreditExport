package com.dualCredit.export.service.impl;

import com.dualCredit.entities.DualCreditExportFinalTbl;
import com.dualCredit.export.service.FileProcess;
import com.dualCredit.repository.DualCreditExportFinalTblRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.*;


@Service
public class FileProcessImpl implements FileProcess {

    @Value("${exl.filepath}")
    private String exlFilepath;

    @Value("${exl.SheetName}")
    private String exlSheetName;

    @Autowired
    DualCreditExportFinalTblRepository dualCreditExportFinalTblRepository;

    public static final String DATE_FORMAT_DD_MM_YYYY = "yyyy-MM-dd HH:mm:ss";
    @Override
    public void exlFileProcess() {
        try {

            System.out.println("hi");
            List<String> headerList = new ArrayList<>();
            List<List<String>> dataList = new ArrayList<>();
            List<DualCreditExportFinalTbl> dualCreditExportFinalTbls = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(new File(exlFilepath));
            Sheet sheet = getSheet(workbook);
            if(null == sheet) {
                System.out.println("The sheet name is wrong please check the name in config file...");
                return;
            }

            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                List<String> data = new ArrayList<String>(Collections.nCopies(200, ""));
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    String cellValue = printCellValue(cell);
                    if (cell.getAddress().getRow() == 0) {
                        headerList.add(cellValue);
                    }
                    else {
                        data.add(cell.getAddress().getColumn(),cellValue);
                    }
                }
                dataList.add(data);
            }
            Iterator<List<String>> listIterator = dataList.iterator();
            while (listIterator.hasNext()) {
                List<String> listData = listIterator.next();
                if(listData.size()<4) {
                    continue;
                }
                if(null == listData.get(3) || listData.get(3).trim().length()==0) {
                    continue;
                }

                String cleanEmail = "";
                if(listData.get(1).indexOf("@stu")>1) {
                    //dualCreditExportFinalTbl.setCleanEmail(listData.get(1));
                    cleanEmail = listData.get(1);
                }
                else {
                    //dualCreditExportFinalTbl.setCleanEmail(listData.get(122));
                    cleanEmail = listData.get(122);
                }
                for(int i=12; i<122; i++) {
                    if(listData.size()>=i) {
                        DualCreditExportFinalTbl dualCreditExportFinalTbl = new DualCreditExportFinalTbl();
                        dualCreditExportFinalTbl.setCleanEmail(cleanEmail);
                        if(listData.get(i).toUpperCase().indexOf("OPT IN") >-1) {
                            checkAndAddData("OPT IN", headerList.get(i), listData.get(3), dualCreditExportFinalTbl, dualCreditExportFinalTbls);
                        }
                        if(listData.get(i).toUpperCase().indexOf("OPT OUT") >-1) {
                            checkAndAddData("OPT OUT", headerList.get(i), listData.get(3), dualCreditExportFinalTbl, dualCreditExportFinalTbls);
                        }
                    }
                }

            }

            //System.out.println(dualCreditExportFinalTbls);
            dualCreditExportFinalTblRepository.saveAll(dualCreditExportFinalTbls);

        }catch (Exception ex) {
            System.out.println("Error in exlFileProcess {"+ex.getMessage()+"}");
            ex.printStackTrace();
        }

    }

    public void checkAndAddData(String optValue, String header,String date, DualCreditExportFinalTbl dualCreditExportFinalTbl, List<DualCreditExportFinalTbl> dualCreditExportFinalTblList) throws Exception{
        dualCreditExportFinalTbl.setOpt(optValue);
        dualCreditExportFinalTbl.setCourse(header);
        if(dualCreditExportFinalTbl.getCleanEmail().equals("4GVxD8d4vBgbZhhB2ZxRz9s8k7nwXZ")) {
            dualCreditExportFinalTbl.setCleanEmail(dualCreditExportFinalTbl.getCleanEmail());
        }
        Date date1=new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY).parse(date.replace("(CST)","").trim());
        dualCreditExportFinalTbl.setStageSubmitDate(date1);
        if(dualCreditExportFinalTblList.contains(dualCreditExportFinalTbl)) {
            DualCreditExportFinalTbl dualCreditExportFinalTblold = dualCreditExportFinalTblList.get(dualCreditExportFinalTblList.indexOf(dualCreditExportFinalTbl));
            if(dualCreditExportFinalTbl.getStageSubmitDate().after(dualCreditExportFinalTblold.getStageSubmitDate())) {
                dualCreditExportFinalTblList.remove(dualCreditExportFinalTblold);
                dualCreditExportFinalTblList.add(dualCreditExportFinalTbl);
            }
        }
        else {
            dualCreditExportFinalTblList.add(dualCreditExportFinalTbl);
        }
    }

    public Sheet getSheet(Workbook workbook) {
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            if(sheet.getSheetName().equals(exlSheetName)) {
                return sheet;
            }
        }

        return null;

    }

    private  String printCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                //System.out.print(cell.getBooleanCellValue());
                return cell.getBooleanCellValue()+"";
            case STRING:
                //System.out.print(cell.getRichStringCellValue().getString());
                return  cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    //System.out.print(cell.getDateCellValue());
                    return cell.getDateCellValue().toString();
                } else {
                    //System.out.print(cell.getNumericCellValue());
                    return cell.getNumericCellValue()+"";
                }
            case FORMULA:
                //System.out.print(cell.getCellFormula());
                return cell.getCellFormula().toString();
            case BLANK:
                return "";
            default:
                return "";
        }

        //return "\t";
    }
}
