package com.company;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, WriteException, BiffException {
	// write your code here
        //String filename = "C:/Users/ictsadmin/Desktop/PDD Alerts/402 type/indexserver_alert_shlh1pdd0402.trc";

        //source and destination folder address for the error 402 type

        /*String sourceFolder = "C:/Users/ictsadmin/Desktop/PDD Alerts/402 type";
        String destinationFolder = "C:/Users/ictsadmin/Desktop/PDD Alerts/402 result";*/

        /*String sourceFolder401 = "C:/Users/ictsadmin/Desktop/PDD Alerts/401 type";
        String destinationFolder401 = "C:/Users/ictsadmin/Desktop/PDD Alerts/401 result";*/

        // call to the reandAndProcess method of the controller class

        // Controller.readAndProcess(sourceFolder, destinationFolder);

        /*Controller401.readAndProcessFiles(sourceFolder401, destinationFolder401);*/

        String sourceFolder = "C:/Users/ictsadmin/Desktop/PDD Alerts/403 type";
        String destinationFolder = "C:/Users/ictsadmin/Desktop/PDD Alerts/403 result";

        //Folder details for common alert folders

        /*String sourceFolder = "C:/Users/ictsadmin/Desktop/PDD Alerts/alerts";
        String destinationFolder = "C:/Users/ictsadmin/Desktop/PDD Alerts/alert results";*/

        Controller403.readAndProcess(sourceFolder, destinationFolder);
    }

    private static void ReadFile() throws IOException {

        // name the file to be open

       /* String filenames = "C:/Users/ictsadmin/Desktop/PDD Alerts/402 type/indexserver_alert_shlh1pdd0402.trc";

        String line = null;*/

        //String[] filenames = new String[]{"C:/Users/ictsadmin/Desktop/PDD Alerts/402 type/indexserver_alert_shlh1pdd0402.trc", "C:/Users/ictsadmin/Desktop/PDD Alerts/402 type/indexserver_alert_shlh1pdd0402_20180315051952"};
       /* StringBuffer strContent = new StringBuffer("");

        for(String filename: filenames)
        {
            File file = new File(filename);

            int characterRead;
            FileInputStream stream = null;
            try
            {
                stream = new FileInputStream(file);
                while ((characterRead = stream.read()) != -1)
                {
                    strContent.append((char) characterRead);
                    System.out.println(strContent);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                stream.close();
            }
        }*/

        /*try
        {
            FileReader fileReader = new FileReader(String.valueOf(filenames));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null)
            {
               System.out.println(line);

            }

        }
        catch (FileNotFoundException ex)
            {
                System.out.println("Unable to open file '" + filenames + "'");
                ex.printStackTrace();
            }
        catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
