package com.testXml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class MainOut {

    public static File[] resDirectoryFiles =null;
    public static ArrayList<String> arrTitle;
    public static String out_path="e:\\nicework\\sww_test.xls";
    public static WritableWorkbook book=null;
    public static boolean isValues=false;

    public static void main(String[] args){
        String path="E:\\nicework";
        File dire=new File(path);
        File inputFile = null;
        if (dire.isDirectory()){
            resDirectoryFiles =dire.listFiles();
        }
        arrTitle=new ArrayList<>();
        HashMap<String,ArrayList<String>> hashMap=new HashMap<>();
        for (int i = 0; i< resDirectoryFiles.length; i++){
            //files为文件列表；
            if (resDirectoryFiles[i].getName().contains("values")){
                if (resDirectoryFiles[i].getName().equals("values")){
                    isValues=true;
                }else{
                    isValues=false;
                }
                arrTitle.add(resDirectoryFiles[i].getName());
                if(resDirectoryFiles[i].isDirectory()) {
                    File[] valueDirectoryFiles = (File[]) resDirectoryFiles[i].listFiles();
                    for (File file : valueDirectoryFiles) {// 基本都是一个strings.xml,可能会有demens,colors;
                        // 对单个string.xml文件进行操作
                        analyzeStringXmlFile(file, hashMap);
                    }
                    // 进行补全，因为string_xx.xml文件的个数会少于value目录下的string的个数，所以进行补全。
                    fillUpVaule(hashMap);
                }
            }
        }
        outputExcel(hashMap,out_path);
    }

    public static void analyzeStringXmlFile(File file,HashMap<String, ArrayList<String>> hashMap){
        if (file.getName().contains("string")&&file.getName().contains(".xml")){
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = (Document) dBuilder.parse(file);
                doc.getDocumentElement().normalize();
                System.out.println("Root element:"+doc.getDocumentElement().getNodeName());
                NodeList nodeList=doc.getElementsByTagName("string");
                ArrayList<String> arrayList;
                for (int n=0;n<nodeList.getLength();n++){
                    Node nNode = nodeList.item(n);
                    if (nNode.getNodeType()==Node.ELEMENT_NODE){
                        Element eElement = (Element) nNode;
                        String key=eElement.getAttribute("name");
                        String value=eElement.getTextContent();
                        //如果含有该Key；
                        if (hashMap.containsKey(key)){
                            ArrayList<String> arrayListContainKey=hashMap.get(key);
                            arrayListContainKey.add(value);
                            hashMap.put(key, arrayListContainKey);
                        }else{//没有此key;
                            if (isValues){
                                arrayList=new ArrayList<>();
                                for(int wen_i=0;wen_i<arrTitle.size()-1;wen_i++){
                                    arrayList.add(null);
                                }
                                arrayList.add(value);
                                hashMap.put(key,arrayList);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void fillUpVaule(HashMap<String, ArrayList<String>> hashMap){
        for(String hashKey : hashMap.keySet()) {
            ArrayList<String> arrlist = (ArrayList<String>) hashMap.get(hashKey);
            if (arrlist.size() < arrTitle.size()) {
                arrlist.add(null);
            }
            hashMap.put(hashKey, arrlist);
        }
    }

    public static void outputExcel(HashMap<String,ArrayList<String>> hashMap, String path){
        try {
            book= Workbook.createWorkbook(new File(path));
            // 设置工作表名(某行的列，某行，数据)；
            WritableSheet sheet = book.createSheet("testForExcel", 0);
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
            System.out.println("有异常,报错了");
        }finally {
            System.out.println();
            System.out.println("去e盘installapk找文件吧");
        }
    }
}

