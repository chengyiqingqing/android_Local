package com.amber.wen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.regex.*;
import java.io.*;
import java.util.*;
//import org.jdom2.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
//import org.jdom2.input.SAXBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class myClass {

    public static File[] files=null;
    public static ArrayList<String> arrTitle;
    public static String out_path="e:\\installapk\\";
    public static WritableWorkbook book=null;
    public static boolean isValues=false;

    public static void main(String[] args){
        //adlib,amberstore,locationlib,lockerlib,lwplib,resouce,weatherdatalib
//        E:\androidwork\weather-widget-libs\mul_store_library\src\main\res
//        String path="C://Users//Amber//Desktop//testValue//weatherdatalib";G:\res
//        String path="E:\\androidwork\\weather-widget-libs\\pull_to_refresh_library\\src\\main\\res";
        String path="E:\\nicework";
        File dire=new File(path);
        File inputFile = null;
        if (dire.isDirectory()){
            files=dire.listFiles();
        }
        out_path+="yangliu"+".xls";
        arrTitle=new ArrayList<>();
        HashMap<String,ArrayList<String>> hashMap=new HashMap<>();
        for (int i=0;i<files.length;i++){
            //files为文件列表；||resDirectoryFiles[i].getName().contains("values-de")||resDirectoryFiles[i].getName().contains("values-ar")
            if (files[i].getName().contains("values")){
                System.out.println("wenwenwenwen"+files[i].getName());
                if (files[i].getName().equals("values")){
                    isValues=true;
                }else{
                    isValues=false;
                }
                arrTitle.add(files[i].getName());
                if(files[i].isDirectory()){
                    File[] filess=(File[]) files[i].listFiles();
                    for (File file:filess)//基本都是一个strings.xml,可能会有demens,colors;
                        if (file.getName().contains("string")&&file.getName().contains(".xml")){
                            System.out.println(""+file.getName()+"----"+it);
                            try {
                                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                Document doc = (Document) dBuilder.parse(file);
                                doc.getDocumentElement().normalize();
                                System.out.println("Root element:"+doc.getDocumentElement().getNodeName());
                                NodeList nodeList=doc.getElementsByTagName("string");
                                System.out.print("----------"+nodeList.getLength());
                                ArrayList<String> arrayList;
                                for (int n=0;n<nodeList.getLength();n++){
                                    Node nNode = nodeList.item(n);
                                    if (nNode.getNodeType()==Node.ELEMENT_NODE){
                                        Element eElement = (Element) nNode;
                                        String key=eElement.getAttribute("name");
                                        String value=eElement.getTextContent();
//                                        System.out.println("name:"+key+"---"+value+"------"+n);
                                        //如果含有该Key；
                                        if (hashMap.containsKey(key)){
//                                            System.out.print("含有此key");
                                            ArrayList<String> arrayListContainKey=hashMap.get(key);
                                            arrayListContainKey.add(value);
                                            hashMap.put(key, arrayListContainKey);
                                        }else{//没有此key;
                                            if (isValues){
                                                //                                            System.out.println("不含有此key");
                                                arrayList=new ArrayList<>();
                                                //第几个文件夹就是几-1了；
                                                for(int wen_i=0;wen_i<arrTitle.size()-1;wen_i++){
                                                    arrayList.add("   ");
                                                }
                                                arrayList.add(value);
                                                hashMap.put(key,arrayList);
                                            }
                                        }

                                    }
                                }

                                /*for(String hashKey : hashMap.keySet()){
                                    ArrayList<String> arrlist=hashMap.get(hashKey);
                                    System.out.print(arrlist.size()
                                            +"--"+arrTitle.size());
                                    if (arrlist.size()<arrTitle.size()){
                                        System.out.println("执行了");
                                        arrlist.add("  ");
                                    }
                                    hashMap.put(hashKey,arrlist);
                                }*/
                            } catch (Exception e) {
                                e.printStackTrace();
                             }
                            it=true;
                        }
                    for(String hashKey : hashMap.keySet()){
                        ArrayList<String> arrlist=hashMap.get(hashKey);
                        System.out.print(arrlist.size()
                                +"--"+arrTitle.size());
                        if (arrlist.size()<arrTitle.size()){
                            System.out.println("执行了");
                            arrlist.add("   ");
                        }
                        hashMap.put(hashKey,arrlist);
                    }
                }
            }

        }
        System.out.println();
//        System.out.println("---+++++++++++++++++----"+hashMap.size());
        System.out.println();
        for (Map.Entry<String,ArrayList<String>> entry:hashMap.entrySet()){
            System.out.print(""+entry.getKey());
            ArrayList<String> arrayList1 =entry.getValue();
            for (String string:arrayList1){
//                System.out.print("--"+string+"--");
            }
//            System.out.println();
        }
        outPutExcel(hashMap,out_path);
    }
    public static boolean it=false;
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(File file) {
//        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb=new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = "";
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            do{
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
                sb.append(tempString);
                line++;
            }while(!(tempString = reader.readLine()).contains("</resources>"));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException e1) {
                }
            }
            return sb.toString();
        }
    }
//    public static ArrayList<String> arrayList;
    public static void outPutExcel(HashMap<String,ArrayList<String>> hashMap,String path){
//        path=path+".xml";
        try {
            book= Workbook.createWorkbook(new File(path));
            // 设置工作表名(某行的列，某行，数据)；
            WritableSheet sheet = book.createSheet("testForExcel", 0);
//            for(int i=0;i<hashMap.size();i++){//控制key;
            sheet.addCell(new Label(0,0,"KEY"));
            for (int title=0;title<arrTitle.size();title++){
                sheet.addCell(new Label(title+1,0,arrTitle.get(title)));
            }
            int r=2;
            ArrayList<String> arrayList;
            Set<Map.Entry<String,ArrayList<String>>> entrySet=hashMap.entrySet();
            for (Map.Entry<String,ArrayList<String>> entry:hashMap.entrySet()){
                int c=0;
                String key=entry.getKey();
                arrayList=entry.getValue();
                sheet.addCell(new Label(c,r,key));
                for (int arr_i=0;arr_i<arrayList.size();arr_i++){

                    sheet.addCell(new Label(++c,r,arrayList.get(arr_i)));
                }
                r++;
            }
            book.write();
            book.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("有异常");
        }finally {
            System.out.println();
            System.out.println("去e盘找文件吧");
        }
    }
}
