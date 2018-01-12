package com.amber.wen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by Amber on 2017/11/7.
 */

public class testExcel {
    public static String path="e:\\testwenwenExcel.xls";
    public static WritableWorkbook book=null;
    public static ArrayList<String> arrayList;
    public static void main(String[] args){
        System.out.println("dsfdf");
        arrayList=new ArrayList<>();
        arrayList.add("张三");
        arrayList.add("李四");
        arrayList.add("王二");
        arrayList.add("麻子");
        arrayList.add("wenwen");
        outPutExcel(arrayList,path);
        System.out.println("Hello Excel");
    }
    public static void outPutExcel(ArrayList<String> arrayList,String path){
        try {
            book= Workbook.createWorkbook(new File(path));
            // 设置工作表名
            WritableSheet sheet = book.createSheet("testForExcel", 0);
            for (int i=0;i<arrayList.size();i++){
                sheet.addCell(new Label(i,0,arrayList.get(i)));
            }
            book.write();
            book.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("有异常");
        }finally {
            System.out.println("去桌面找文件吧");
        }
    }
}
