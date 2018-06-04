package com.company;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class Controller401 {
    private static String sourceLoc = null;
    private static String destLoc = null;
    private static String fileName = null;

    public Controller401() {
    }

    public static void readAndProcessFiles(String sourceDir, String destDir) throws InterruptedException, BiffException, WriteException {
        File srcDir = new File(sourceDir);
        destLoc = destDir;
        sourceLoc = sourceDir;
        System.out.println("---" + sourceDir);
        String[] arr$ = srcDir.list();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String i = arr$[i$];
            if (!(new File(sourceDir + "\\" + i)).isDirectory()) {
                System.out.println("---" + i);
                fileName = i;
                initiateParsing();
            }
        }

    }

    public static void initiateParsing() throws InterruptedException, BiffException, WriteException {
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(sourceLoc + "\\" + fileName);
            br = new BufferedReader(fr);
            String query = "";
            Boolean flag = false;
            Integer count = 0;

            String sCurrentLine;
            ArrayList listLogPojo;
            for(listLogPojo = new ArrayList(); (sCurrentLine = br.readLine()) != null; query = query + sCurrentLine) {
                if (Pattern.matches("\\[\\d*\\].*$", sCurrentLine)) {
                    if (query != "" && query.contains("SQL string")) {
                        System.out.println("---" + (count = count + 1) + query);
                        listLogPojo.add(parser(query));
                    }

                    query = "";
                }
            }

            writeToCsv(listLogPojo);
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }

                if (fr != null) {
                    fr.close();
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }

        }

    }

    public static void writeToCsv(List<Controller401.LogPOJO> listLogPojo) throws BiffException, IOException, WriteException {
        WritableWorkbook wworkbook = Workbook.createWorkbook(new File(destLoc + "\\" + fileName + ".xls"));
        WritableSheet wsheet = wworkbook.createSheet("Book1", 0);
        ArrayList<String> keySet = new ArrayList();
        keySet.add("STATEMENT");
        keySet.add("CONNECTION_ID");
        keySet.add("CONNECTION_STATUS");
        keySet.add("HOST");
        keySet.add("CREATED_BY");
        keySet.add("IDLE_TIME");
        keySet.add("STATEMENT_STATUS");
        keySet.add("STATEMENT_LIFE_TIME");
        keySet.add("START_MVCC_TIMESTAMP");
        keySet.add("ISOLATION_LEVEL");
        keySet.add("SQL_STRING");
        int c = 1;
        int kCount = 0;

        int c1;
        for(c1 = 0; c1 < 11; ++c1) {
            Label l = new Label(c++, 0, (String)keySet.get(kCount++));
            wsheet.addCell(l);
        }

        c1 = 1;

        for(Iterator i$ = listLogPojo.iterator(); i$.hasNext(); ++c1) {
            Controller401.LogPOJO m = (Controller401.LogPOJO)i$.next();
            int t = 1;
            int var12 = t + 1;
            Label label = new Label(t, c1, m.STATEMENT);
            wsheet.addCell(label);
            label = new Label(var12++, c1, m.CONNECTION_ID);
            wsheet.addCell(label);
            label = new Label(var12++, c1, m.CONNECTION_STATUS);
            wsheet.addCell(label);
            label = new Label(var12++, c1, m.HOST);
            wsheet.addCell(label);
            label = new Label(var12++, c1, m.CREATED_BY);
            wsheet.addCell(label);
            label = new Label(var12++, c1, m.IDLE_TIME);
            wsheet.addCell(label);
            label = new Label(var12++, c1, m.STATEMENT_STATUS);
            wsheet.addCell(label);
            label = new Label(var12++, c1, m.STATEMENT_LIFE_TIME);
            wsheet.addCell(label);
            label = new Label(var12++, c1, m.START_MVCC_TIMESTAMP);
            wsheet.addCell(label);
            label = new Label(var12++, c1, m.ISOLATION_LEVEL);
            wsheet.addCell(label);
            label = new Label(var12++, c1, m.SQL_STRING);
            wsheet.addCell(label);
        }

        wworkbook.write();
        wworkbook.close();
        System.out.println("Finished");
    }

    public static Object parser(String q) {
        Controller401.LogPOJO lPojo = new Controller401.LogPOJO();
        lPojo.STATEMENT = q.substring(q.indexOf("Statement"), q.indexOf("CONNECTION_ID"));
        lPojo.CONNECTION_ID = q.substring(q.indexOf("CONNECTION_ID =") + "CONNECTION_ID =".length(), q.indexOf(", CONNECTION_STATUS"));
        lPojo.CONNECTION_STATUS = q.substring(q.indexOf("CONNECTION_STATUS =") + "CONNECTION_STATUS =".length(), q.indexOf(", HOST"));
        if (q.contains("HOST =") && q.contains(", CREATED_BY")) {
            lPojo.HOST = q.substring(q.indexOf("HOST =") + "HOST =".length(), q.indexOf(", CREATED_BY"));
        }

        System.out.println("---===" + q.contains("CREATED_BY ="));
        if (q.contains("CREATED_BY =") && q.contains(", IDLE_TIME")) {
            lPojo.CREATED_BY = q.substring(q.indexOf("CREATED_BY =") + "CREATED_BY =".length(), q.indexOf(", IDLE_TIME"));
        }

        lPojo.IDLE_TIME = q.substring(q.indexOf("IDLE_TIME =") + "IDLE_TIME =".length(), q.indexOf(", STATEMENT_STATUS"));
        lPojo.STATEMENT_STATUS = q.substring(q.indexOf("STATEMENT_STATUS =") + "STATEMENT_STATUS =".length(), q.indexOf(", STATEMENT_LIFE_TIME"));
        lPojo.STATEMENT_LIFE_TIME = q.substring(q.indexOf("STATEMENT_LIFE_TIME =") + "STATEMENT_LIFE_TIME =".length(), q.indexOf(", START_MVCC_TIMESTAMP"));
        lPojo.START_MVCC_TIMESTAMP = q.substring(q.indexOf("START_MVCC_TIMESTAMP =") + "START_MVCC_TIMESTAMP =".length(), q.indexOf(", ISOLATION_LEVEL"));
        lPojo.ISOLATION_LEVEL = q.substring(q.indexOf("ISOLATION_LEVEL =") + "ISOLATION_LEVEL =".length(), q.indexOf(", SQL string"));
        lPojo.SQL_STRING = q.substring(q.indexOf("SQL string =") + "SQL string =".length(), q.length());
        System.out.println("---" + lPojo.toString());
        return lPojo;
    }

    public static class LogPOJO {
        public String STATEMENT;
        public String CONNECTION_ID;
        public String CONNECTION_STATUS;
        public String HOST;
        public String CREATED_BY;
        public String IDLE_TIME;
        public String STATEMENT_STATUS;
        public String STATEMENT_LIFE_TIME;
        public String START_MVCC_TIMESTAMP;
        public String ISOLATION_LEVEL;
        public String SQL_STRING;

        public LogPOJO() {
        }

        public String toString() {
            return "LogPOJO [STATEMENT=" + this.STATEMENT + ", CONNECTION_ID=" + this.CONNECTION_ID + ", CONNECTION_STATUS=" + this.CONNECTION_STATUS + ", HOST=" + this.HOST + ", CREATED_BY=" + this.CREATED_BY + ", IDLE_TIME=" + this.IDLE_TIME + ", STATEMENT_STATUS=" + this.STATEMENT_STATUS + ", STATEMENT_LIFE_TIME=" + this.STATEMENT_LIFE_TIME + ", START_MVCC_TIMESTAMP=" + this.START_MVCC_TIMESTAMP + ", ISOLATION_LEVEL=" + this.ISOLATION_LEVEL + ", SQL_STRING=" + this.SQL_STRING + "]";
        }
    }
}
