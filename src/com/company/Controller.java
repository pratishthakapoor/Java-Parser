package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


import static java.nio.file.Files.isDirectory;

public class Controller {

    private static String sourceLocation = null;
    private static String destinationLocation = null;
    private static String fileName = null;

    public Controller() {

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

    private static void initiateParsing() throws BiffException, WriteException {
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

            for(logPojo = new ArrayList(); (CurrentLine = bufferedReader.readLine()) != null; )
            {
                hostQuery += CurrentLine + "\r\n";
                if(Pattern.matches("\\[\\d*\\].*$", CurrentLine))
                {
                     if(hostQuery != null && hostQuery.contains("Memory"))
                     {
                         System.out.println("---" + (count = count +1) + hostQuery);
                         logPojo.add(parser(hostQuery));
                     }

                     else if(hostQuery != null && CurrentLine.contains("Executor"))
                     {
                         System.out.println("---" + (count = count +1) + CurrentLine);
                         logPojo.add(parser(CurrentLine));
                     }

                     else if(hostQuery != null && CurrentLine.contains("parallel"))
                     {
                         System.out.println("---" + (count = count + 1) + CurrentLine);
                         logPojo.add(parser(CurrentLine));
                     }

                     hostQuery = "";
                }
            }

            writeToCsv(logPojo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
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

    private static void writeToCsv(List<Controller.LogPOJIO> listLogPojo) throws BiffException, IOException, WriteException {
        WritableWorkbook workbook = Workbook.createWorkbook(new File(destinationLocation + "\\" + fileName + ".xls"));
        WritableSheet workSheet = workbook.createSheet("Book", 0);
        ArrayList<String> keySet = new ArrayList<>();
        keySet.add("Host");
        keySet.add("PID");
        keySet.add("Current callstack");
        keySet.add("Memory error");
        //keySet.add("Limit correction value");
        keySet.add("Executable");
        keySet.add("Executor error");
        keySet.add("parallel error");
        //keySet.add("ReportMemoryProblems.cpp()");

        int c= 1;
        int kCount = 0;

        int c1;
        for(c1 = 0; c1 < 7; c1++)
        {
            Label label = new Label(c++, 0, (String)keySet.get(kCount++));
            workSheet.addCell(label);
        }
        c1 = 1;
        for(Iterator iterator = listLogPojo.iterator(); iterator.hasNext(); ++c1)
        {
            Controller.LogPOJIO pojo = (Controller.LogPOJIO) iterator.next();
            int t =1;
            int newVar = t + 1;

            // label for fetching the Host details

            Label label = new Label(t, c1, pojo.Host);
            workSheet.addCell(label);

            // label for fetching the PID

            label = new Label(newVar++, c1, pojo.PID);
            workSheet.addCell(label);

            // label for fetching the current callstack

            label = new Label(newVar++, c1, pojo.Current_CallStack);
            workSheet.addCell(label);

            // label for fetching the Memory detail

            label = new Label(newVar++, c1, pojo.Memory);
            workSheet.addCell(label);

            // label for fetching the Limit correction value

            /*label = new Label(newVar++, c1, pojo.LimitCorrectionValue);
            workSheet.addCell(label);*/

            label = new Label(newVar++, c1, pojo.Executable);
            workSheet.addCell(label);

            /*label = new Label(newVar++, c1, pojo.ReportMemoryProblems);
            workSheet.addCell(label);*/

            label = new Label(newVar++, c1, pojo.Executor);
            workSheet.addCell(label);

            label =new Label(newVar++, c1, pojo.Parallel_error);
            workSheet.addCell(label);


        }

        workbook.write();
        workbook.close();
        System.out.println("Finished writing workbook");
    }

    private static Controller.LogPOJIO parser(String hostQuery)
    {
         Controller.LogPOJIO logPojo = new Controller.LogPOJIO();
         if(hostQuery.contains("Host:"))
         {
             logPojo.Executor = "null";
             logPojo.Host = hostQuery.substring(hostQuery.indexOf("Host:") + "Host:".length(), hostQuery.indexOf("\r\n",hostQuery.indexOf("Host:")));
             logPojo.Current_CallStack = hostQuery.substring(hostQuery.indexOf("Current callstack:") + "Current callstack:".length(), hostQuery.indexOf("\r\n", hostQuery.indexOf("Current callstack:")));
             logPojo.Executable = hostQuery.substring(hostQuery.indexOf("Executable:") + "Executable:".length(), hostQuery.indexOf("\r\n", hostQuery.indexOf("\r\n",hostQuery.indexOf("Executable"))));
             logPojo.PID = hostQuery.substring(hostQuery.indexOf("PID:") + "PID:".length(), hostQuery.indexOf("\r\n", hostQuery.indexOf("PID:")));
             //logPojo.LimitCorrectionValue = hostQuery.substring(hostQuery.indexOf("Limit correction value ") + "Limit correction value ".length(), hostQuery.indexOf("\r\n", hostQuery.indexOf("Limit correction value")));
             logPojo.Memory = hostQuery.substring(hostQuery.indexOf("Memory") + "Memory".length(), hostQuery.length());
         }

         if(hostQuery.contains("Executor"))
        {
            logPojo.Host = null;
            logPojo.Current_CallStack = null;
            logPojo.Executable = null;
            logPojo.PID = null;
            //logPojo.LimitCorrectionValue = null;
            logPojo.Memory = null;
            logPojo.Executor = hostQuery.substring(hostQuery.indexOf("Executor") + "Executor".length(), hostQuery.length());
        }

        if(hostQuery.contains("Memory"))
           // logPojo.Memory = hostQuery.substring((hostQuery.indexOf("Memory") + "Memory".length(), hostQuery.length());
            logPojo.Memory = hostQuery.substring(hostQuery.indexOf("Memory") + "Memory".length(), hostQuery.length());

         if(hostQuery.contains("parallel"))
             logPojo.Parallel_error = hostQuery.substring(hostQuery.indexOf("parallel") + "e parallel".length(), hostQuery.length());

         return  logPojo;
    }

    public static class LogPOJIO {
        public String Host;
        public String PID;
        public String Current_CallStack;
        public String Memory;
       // public String LimitCorrectionValue;
        public String Executable;
        public String Executor;
        public String Parallel_error;
        //public String ReportMemoryProblems;

        public LogPOJIO() {
        }

        public String toString() {
            return "LogPOJIO [Host:" + this.Host + ", PID:" + this.PID + ", Current_CallStack:" + this.Current_CallStack + ", Memory " + this.Memory + ", Executable:" + this.Executable+ ", Executor" + this.Executor + ", e parallel" + this.Parallel_error + "]"; //, ReportMemoryProblems.cpp():" + this.ReportMemoryProblems + " ", LimitCorrectionValue" + this.LimitCorrectionValue + "" +]";
            //return "LogPOJO [STATEMENT=" + this.STATEMENT + ", CONNECTION_ID=" + this.CONNECTION_ID + ", CONNECTION_STATUS=" + this.CONNECTION_STATUS + ", HOST=" + this.HOST + ", CREATED_BY=" + this.CREATED_BY + ", IDLE_TIME=" + this.IDLE_TIME + ", STATEMENT_STATUS=" + this.STATEMENT_STATUS + ", STATEMENT_LIFE_TIME=" + this.STATEMENT_LIFE_TIME + ", START_MVCC_TIMESTAMP=" + this.START_MVCC_TIMESTAMP + ", ISOLATION_LEVEL=" + this.ISOLATION_LEVEL + ", SQL_STRING=" + this.SQL_STRING + "]";
        }
    }
}
