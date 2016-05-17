package pers.yangchen.SCP;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangchen on 16-4-26.
 */
public class NewPartitionMethod {

    private static List<String> filePaths;




    public static void mergeFiles() throws IOException, ParserConfigurationException, SAXException {
        FileChannel outChannel = null;
        int BUFSIZE = 1024 * 100;
        File[] files = new File("/home/yangchen/ycdoc/cataTemp").listFiles();
        List<String> paths = new ArrayList<String>();
        NewPartitionMethod.setFilePaths(paths);
        showFiles(files);
        Map<String, List<String>> all = new HashMap<String, List<String>>();
        for(int m = 0; m < filePaths.size(); m++){
            InputStream is = new FileInputStream(filePaths.get(m));
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine();
            List<String> fileNames = new ArrayList<String>();
            while (line != null){
                String[] content = line.split("\\/");
                fileNames.add(content[content.length-1]);
                line = reader.readLine();
            }
            String[] p = filePaths.get(m).split("\\/");
            String key = "org.apache.catalina." + p[p.length-1];
            all.put(key, fileNames);
            reader.close();
            is.close();
        }
        Map<String, String> keyNames = new HashMap<String, String>();
        keyNames.put("0", "org.apache.catalina.0.txt");
        keyNames.put("1", "org.apache.catalina.ant.txt");
        keyNames.put("2", "org.apache.catalina.authenticator.txt");
        keyNames.put("3", "org.apache.catalina.comet.txt");
        keyNames.put("4", "org.apache.catalina.connector.txt");
        keyNames.put("5", "org.apache.catalina.core.txt");
        keyNames.put("6", "org.apache.catalina.deploy.txt");
        keyNames.put("7", "org.apache.catalina.filters.txt");
        keyNames.put("8", "org.apache.catalina.ha.txt");
        keyNames.put("9", "org.apache.catalina.loader.txt");
        keyNames.put("10", "org.apache.catalina.manager.txt");
        keyNames.put("11", "org.apache.catalina.mbeans.txt");
        keyNames.put("12", "org.apache.catalina.realm.txt");
        keyNames.put("13", "org.apache.catalina.security.txt");
        keyNames.put("14", "org.apache.catalina.servlets.txt");
        keyNames.put("15", "org.apache.catalina.session.txt");
        keyNames.put("16", "org.apache.catalina.ssi.txt");
        keyNames.put("17", "org.apache.catalina.startup.txt");
        keyNames.put("18", "org.apache.catalina.tribes.txt");
        keyNames.put("19", "org.apache.catalina.users.txt");
        keyNames.put("20", "org.apache.catalina.util.txt");
        keyNames.put("21", "org.apache.catalina.valves.txt");
        keyNames.put("22", "org.apache.catalina.websocket.txt");

        for(int i = 0; i < all.size(); i ++){
            List<String> module = all.get(keyNames.get(String.valueOf(i)));
            String outFile = "/home/yangchen/ycdoc/cataModuleFull/" + keyNames.get(String.valueOf(i));
            outChannel = new FileOutputStream(outFile).getChannel();
            if(module != null) {
                for(int j = 0; j < module.size(); j ++){
                    String filePath = "/home/yangchen/ycdoc/tomcatpre/" + module.get(j);
                    File file = new File(filePath);
                    if(file.exists()) {
                        FileChannel fc = new FileInputStream(filePath).getChannel();
                        ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                        while (fc.read(bb) != -1) {
                            bb.flip();
                            outChannel.write(bb);
                            bb.clear();
                        }
                        fc.close();
                    }
                }
            }
        }

    }


    //write composite.txt to new partition method
    public static void getNewModuleData(int topicNum) throws IOException {
        File[] files = new File("/home/yangchen/ycdoc/topicModel/catalinacata").listFiles();
        List<String> paths = new ArrayList<String>();
        NewPartitionMethod.setFilePaths(paths);
        showFiles(files);
        Map<String, String> moduleBelongTo = new HashMap<String, String>();
        for(int i = 0; i < filePaths.size(); i ++){
            InputStream is = new FileInputStream(filePaths.get(i));
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine();
            String[] p = filePaths.get(i).split("\\/|\\.");
            String key = p[p.length-2];
            while (line != null){
                moduleBelongTo.put(line.trim(), key);
                line = reader.readLine();
            }
            reader.close();
            is.close();
        }
        String ratePath = "/usr/local/mallet-2.0.7/catalina/catalina"+ topicNum +"/composition.txt";
        InputStream nis = new FileInputStream(ratePath);
        String nline;
        BufferedReader nreader = new BufferedReader(new InputStreamReader(nis));
        nline = nreader.readLine();
        nline = nreader.readLine();
        while (nline != null){
            String[] rates = nline.split("\t");
            String belong = moduleBelongTo.get(rates[1].trim());
            if(belong != null){
                //npath path to save
                String npath = "/home/yangchen/ycdoc/topicModel/basedOnCatalina/catalina" + topicNum + "/" + belong + ".txt";
                String newLine = rates[0] + "\t" + rates[1];
                int flag = Integer.parseInt(rates[2]);
                int index = 2;
                int length = rates.length;
                for(int j = 0; j< (rates.length-2)/2; j ++){
                    for(int i = 2; i <length-1; i = i + 2){
                        if(Integer.parseInt(rates[i]) < flag){
                            flag = Integer.parseInt(rates[i]);
                            index = i;
                        }
                    }
                    newLine = newLine + "\t" + rates[index] + "\t" + rates[index+1];
                    for(int k=index; k < rates.length-3; k ++){
                        rates[k] = rates[k + 2];
                    }
                    length = length -2;
                    index = 2;
                    flag = Integer.parseInt(rates[2]);
                }
                WriteToFile.contentToTextFile(npath, newLine + "\n");
            }
            nline = nreader.readLine();
        }
        nreader.close();
        nis.close();
    }

    //new module path save to one path
    public static void getNewModule() throws IOException {
        String path = "/home/yangchen/ycdoc/topicModel/tribes";
        InputStream is = new FileInputStream(path);
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        int length = 0;
        String l = "";
        while (line != null){
            String[] content = line.split("\\/|\\.");
            if(content.length > length){
                length = content.length;
                l = line;
            }
            if(content.length == 7){
                String names = "file:/usr/local/mallet-2.0.7/source-data1/tribesPre/org.apache.catalina";
                for(int i = 4; i<6;i++){
                    names = names + content[i] + ".";
                }
                names = names + "txt\n";
                WriteToFile.contentToTextFile("/home/yangchen/ycdoc/topicModel/tribestri/0.txt", names);
            }else{
                String names = "file:/usr/local/mallet-2.0.7/source/catalinaPre/org.apache.";
                for(int j = 4; j < content.length-1; j ++){
                    names = names + content[j] + ".";
                }
                names = names + "txt\n";
                WriteToFile.contentToTextFile("/home/yangchen/ycdoc/topicModel/tribestri/"+ content[5] +".txt", names);
            }
            line = reader.readLine();
        }
        System.out.print(length);
        System.out.print(l);
    }

    public static void getNewPartitionMethod(){
        //Map<String, String> docId = DocumentCitation.getDocId();
        File[] files = new File("/home/yangchen/ycdoc/catalina").listFiles();
        List<String> paths = new ArrayList<String>();
        NewPartitionMethod.setFilePaths(paths);
        showFiles(files);
        String pack = "";
        for(int i = 0; i< filePaths.size(); i++){
            String[] f = filePaths.get(i).split("\\.");
            if(f[f.length-1].equals("java")){
                pack = pack + filePaths.get(i) + "\n";
            }
        }
        WriteToFile.contentToTextFile("/home/yangchen/ycdoc/catalinaCatalogs", pack);
    }

    private static void showFiles(File[] files) {
        for(File file : files){
            if(file.isDirectory()){
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles());
            }else{
                System.out.println(file.getAbsolutePath());
                filePaths.add(file.getAbsolutePath());
            }
        }
    }

    public static void setFilePaths(List<String> filePaths) {
        NewPartitionMethod.filePaths = filePaths;
    }

    public static List<String> getFilePaths() {
        return NewPartitionMethod.filePaths;
    }

}
