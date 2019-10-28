package com.mywatch.amber.testskin;

import org.w3c.dom.NodeList;

import jxl.write.WritableWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
//import org.jdom2.input.SAXBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;

public class testSkinString {

    public static WritableWorkbook book=null;
    public static String path="E:\\androidwork";
    public static final String middlePath="src";
    public static final String SKIN="skin";
    public static final String MODULE="apex";

    public static File mSkinFile;
    public static File mModuleFile;
    public static String mPkgName;

    public static String skin_out_path ="e:\\installapk\\testSkin.xls";//testSkinXml
    public static WritableSheet sheet;
    public static ArrayList<String> arrTitle=new ArrayList<>();
    public static int mRowCount=2;

    static{
        arrTitle.add("skin");
        arrTitle.add("module");
        arrTitle.add("pkgName");
        arrTitle.add("key");
        arrTitle.add("value");
    }
    public static void main(String[] args)  {
        try {
            book= Workbook.createWorkbook(new File(skin_out_path));
            // 设置工作表名(某行的列，某行，数据)；
            sheet = book.createSheet("testSkin", 0);
            for (int title_i=0;title_i<arrTitle.size();title_i++){
                //列，行，value;
                sheet.addCell(new Label(title_i,0,arrTitle.get(title_i)));
            }
            //1.遍历筛选Skin;
            File mainFile=new File(path);
            File[] skinFiles;
            ArrayList<File> moduleFiles;
            if (mainFile.isDirectory()){
                skinFiles=mainFile.listFiles();
                if (skinFiles.length>0){
                    for (int i=0;i<skinFiles.length;i++){
                        if (skinFiles[i].getName().contains(SKIN)) {
                            mSkinFile=skinFiles[i];//存一下当前skin;
    //                        System.out.println("" + skinFiles[i]);
                            //2.去判断是否为module;是的话返回module集合;-->scanSkinForModules(skinFiles[i])
                            moduleFiles=scanSkinForModules(skinFiles[i]);
                            for (int moduleFiles_i=0;moduleFiles_i<moduleFiles.size();moduleFiles_i++){
    //                            System.out.println(moduleFiles.get(moduleFiles_i));
                                mModuleFile=moduleFiles.get(moduleFiles_i);//存一下当前module；
                                scanModuleForPkgName(skinFiles[i].getName(),moduleFiles.get(moduleFiles_i));
                            }
                        }
                    }
                }
            }
            //2.遍历Skin；
            //2.1遍历skin内的file --> 获取module
            //3.获取包名；
            //4.获取key和value;
            int diffCount=2;
            sheet.addCell(new Label(8,0,"不同value值"));
            for (String hashKey:hashMap.keySet() ){
                sheet.addCell(new Label(8,diffCount,hashKey));
                diffCount++;
            }
            book.write();
            book.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("有异常");
            System.err.println("有异常");
            System.err.println("有异常");
            System.err.println("有异常");
        }
    }

    //2.去判断是否为module;是的话返回module集合;
    public static ArrayList<File> scanSkinForModules(File skinFile){
        ArrayList<File> moduleFiles=new ArrayList<>();
        File[] skinInsideFiles = skinFile.listFiles();//含有.idea和module等文件;
//        for (int i=0;i<skinInsideFiles.length;i++){
        for (int i=0;i<skinInsideFiles.length;i++){
            if (skinInsideFiles[i].isDirectory()){
                File[]  moduleInsideFiles=skinInsideFiles[i].listFiles();//含有src和build.gradle等文件;
                for (int j=0;j<moduleInsideFiles.length;j++){
                    if (moduleInsideFiles[j].getName().equals("src")){
//                        System.out.println(""+skinInsideFiles[i].getName());
                        moduleFiles.add(skinInsideFiles[i]);
                        break;
                    }
                }
            }
        }
        return moduleFiles;
    }

    public static final String srcToValuesPath="\\src\\main\\res\\values";
    //3.获取包名；
    public static ArrayList<String> scanModuleForPkgName(String skinName,File modulefile){
//        String path="E:\\androidwork\\"+skinName+"\\"+modulefile.getName()+srcToValuesPath;
        String path=modulefile+srcToValuesPath;
        File stringfile=new File(path);
        File[] getStringFiles=stringfile.listFiles();
        System.out.println(mSkinFile.getName()+"--"+mModuleFile.getName());

        System.out.println("ssssss"+stringfile.getName());
//        if (stringfile.getName().equals())
        ArrayList<String> pkgArrayList=new ArrayList<>();
        File[] moduleInsideFile = modulefile.listFiles();
        for (int i=0;i<moduleInsideFile.length;i++){
            if (moduleInsideFile[i].getName().equals("build.gradle")){//如果是build.gradle文件；
                System.out.println(""+modulefile.getName()+"---"+moduleInsideFile[i].getName());
                readFileByLines(moduleInsideFile[i]);
            }
        }

        //获取string.xml文件；
        for (int s=0;s<getStringFiles.length;s++){
            if (getStringFiles[s].getName().equals("strings.xml")){
                System.out.println("wenwenwen"+getStringFiles[s].getName());
                //写解析它的文件；
                getKeyAndValue(getStringFiles[s]);
            }
        }


        return pkgArrayList;
    }

    //读取build.gradle文件；返回pkgName的集合；
    public static ArrayList<String> readFileByLines(File file) {
        ArrayList<String> arrPkgName=new ArrayList<>();
        BufferedReader reader = null;
        String tempString = "";
        String[] findPkgName;
        try {
            reader = new BufferedReader(new FileReader(file));
            int line = 1;
            do{
                if (tempString.contains("applicationId \"")){
                    findPkgName=tempString.split("applicationId \"");
                    String pkgName=findPkgName[1].trim();
                    pkgName=pkgName.substring(0,pkgName.length()-1);
                    arrPkgName.add(pkgName);
                    System.out.println(""+pkgName);
                    mPkgName=pkgName;
                    break;
                }
                line++;
            }while(!(tempString = reader.readLine()).contains("dependencies {"));
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
            return arrPkgName;
        }
    }

    public static HashMap<String,String> hashMap=new HashMap<>();
    //做解析；
    public static HashMap<String,String>  getKeyAndValue(File stringFile){
        LinkedHashMap<String,String> linkHashMap=new LinkedHashMap<>();
//        HashMap<String,String> hashMap=new HashMap<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = null;
            doc = (Document) dBuilder.parse(stringFile);
            System.out.println("Root element:"+doc.getDocumentElement().getNodeName());
            //获取string的一个列表；
            NodeList nodeList=doc.getElementsByTagName("string");
            System.out.println(""+nodeList.getLength());
            for (int i = 0; i <nodeList.getLength(); i++) {
                Node stringNode = nodeList.item(i);
                if (stringNode.getNodeType()==Node.ELEMENT_NODE){
                    Element eElement= (Element) stringNode;
                    String key=eElement.getAttribute("name");
                    String value=eElement.getTextContent();
                    System.out.println("key--"+key);
                    System.out.println("value--"+value);
                    hashMap.put(value,key);
                    for (int sheet_col_i=0;sheet_col_i<5;sheet_col_i++){
                        if (sheet_col_i==0)
                            sheet.addCell(new Label(sheet_col_i,mRowCount,mSkinFile.getName()));
                        else if (sheet_col_i==1){
                            sheet.addCell(new Label(sheet_col_i,mRowCount,mModuleFile.getName()));
                        }else if (sheet_col_i==2){
                            sheet.addCell(new Label(sheet_col_i,mRowCount,mPkgName));
                        }else if (sheet_col_i==3){
                            sheet.addCell(new Label(sheet_col_i,mRowCount,key));
                        }else if (sheet_col_i==4){
                            sheet.addCell(new Label(sheet_col_i,mRowCount,value));
                        }
                    }
                    mRowCount++;

                    //生成表格；
                    linkHashMap.put(key,value);
                }
            }
            System.out.println(""+linkHashMap.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMap;
    }

//    public static WritableWorkbook book=null;
    public static void outPutExcel(LinkedHashMap<String,String> linkedHashMap){

    }
}
