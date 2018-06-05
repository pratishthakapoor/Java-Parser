package com.company;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class Controller403 {
    private static String sourceLocation = null;
    private static String destinationLocation = null;
    private static String fileName = null;

    public Controller403() {

    }

    public static void readAndProcess(String sourceFolder, String destinationFolder) throws InterruptedException, IOException, BiffException, WriteException {
        File sourceDirectory = new File(sourceFolder);

        destinationLocation = destinationFolder;
        sourceLocation = sourceFolder;
        System.out.println(sourceFolder);
        String[] fileArray$ = sourceDirectory.list();

        int length = fileArray$.length;

        for(int i = 0; i < length; i++)
        {
            String storage = fileArray$[i];
            if (!(new File(sourceFolder + "\\" + storage)).isDirectory()) {
                System.out.println("---" + i);
                fileName = storage;
                initiateParsing();
            }
        }

    }

    private static void initiateParsing() {

        BufferedReader bufferedReader = null;
        FileReader fileReader = null;

        try
        {
            fileReader = new FileReader(sourceLocation + "\\" + fileName);
            bufferedReader = new BufferedReader(fileReader);
            String hostQuery = "";
            Boolean flag = false;
            Integer count = 0;

            String CurrentLine;
            ArrayList logPojo;

            for(logPojo = new ArrayList(); (CurrentLine = bufferedReader.readLine()) != null; hostQuery += CurrentLine )
            {
               // hostQuery += CurrentLine + "\r\n";
                if(Pattern.matches("\\[\\d*\\].*$", CurrentLine))
                {
                    if(hostQuery != null && hostQuery.contains("e")) {
                        System.out.println("---" + (count = count + 1) + hostQuery);
                        logPojo.add(parser(hostQuery));
                    }

                    hostQuery = "";
                }
            }
            writeToCsv(logPojo);
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try
            {
                if(bufferedReader != null)
                    bufferedReader.close();
                if(fileReader != null)
                    fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

   private static void writeToCsv(List<Controller403.LogPOJO> logPojo) throws IOException, WriteException {

       //WritableWorkbook workbook = Workbook.createWorkbook(new File(destinationLocation + "\\" + fileName + ".xls"));
       WritableWorkbook workbook = Workbook.createWorkbook(new File(destinationLocation + "\\" + fileName + ".txt"));
       WritableSheet workSheet = workbook.createSheet("Book", 0);
       ArrayList<String> keySet = new ArrayList<>();
       keySet.add("Timestamp");
       keySet.add("ErrorOccured ");
       keySet.add("ErrorDescription");


       int c= 1;
       int kCount = 0;

       int c1;
       for(c1 = 0; c1 < 3; c1++)
       {
           Label label = new Label(c++, 0, (String)keySet.get(kCount++));
           workSheet.addCell(label);
       }
       c1 = 1;
       int rows = 0;
       for(Iterator iterator = logPojo.iterator(); iterator.hasNext() && rows<64000; ++c1)
       {
           Controller403.LogPOJO pojo = (Controller403.LogPOJO) iterator.next();
           int t =1;
           int newVar = t + 1;
           rows++;
           // label for fetching the Host details

           Label label = new Label(t, c1, pojo.TimeStamp);
           workSheet.addCell(label);

           // label for fetching the PID

           label = new Label(newVar++, c1, pojo.ErrorClass);
           workSheet.addCell(label);

           // label for fetching the current callstack

           label = new Label(newVar++, c1, pojo.ErrorDescription);
           workSheet.addCell(label);

       }

       workbook.write();
       workbook.close();
       System.out.println("Finished writing workbook");

    }

    private static Object parser(String hostQuery) {

        Controller403.LogPOJO lPojo = new Controller403.LogPOJO();
        lPojo.TimeStamp = hostQuery.substring(hostQuery.indexOf(""), hostQuery.indexOf("e"));
        if(hostQuery.contains("e Session") || hostQuery.contains(" e ExprConversionTo") || hostQuery.contains("e SessionRemoteReq")){
            lPojo.ErrorClass = hostQuery.substring(hostQuery.indexOf("e") + "e".length(), hostQuery.indexOf(" : "));
        }
        else
        {
            lPojo.ErrorClass = hostQuery.substring(hostQuery.indexOf("e") + "e".length(), hostQuery.indexOf("  "));
        }

        //lPojo.ErrorClass = hostQuery.substring(hostQuery.indexOf("e") + "e".length(), hostQuery.indexOf("  "));

        lPojo.ErrorDescription = hostQuery.substring(hostQuery.indexOf(" :") + ":".length(), hostQuery.length());

        return lPojo;
    }

    public static class LogPOJO {

        public String TimeStamp;
        public String ErrorClass;
        public String ErrorDescription;

        public LogPOJO() {
        }

        public String toString() {
            return "LogPOJO[TimeStamp=" + this.TimeStamp + ", ErrorClass=" + this.ErrorClass + ", ErrorDescription=" + this.ErrorDescription + "]";
        }

    }
}

