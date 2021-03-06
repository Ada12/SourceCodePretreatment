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
 * Created by yangchen on 16-4-18.
 */
public class DocumentCitation {

    private static List<String> filePaths;

    //get all citation
    public static void getDocumentCitation() throws IOException {
        Map<String, String> docId = DocumentCitation.getDocId();
        File[] files = new File("/home/yangchen/ycdoc/apache-tomcat-7/java").listFiles();
        List<String> paths = new ArrayList<String>();
        DocumentCitation.setFilePaths(paths);
        showFiles(files);
        List<String> lastCitation = new ArrayList<String>();
        for(int j = 0; j < filePaths.size(); j ++){
            String[] f = filePaths.get(j).split("\\.");
            if(f[f.length-1].equals("java")){
                InputStream isd = new FileInputStream(filePaths.get(j));
                String lined;
                BufferedReader readerd = new BufferedReader(new InputStreamReader(isd));
                lined = readerd.readLine();
                while (lined != null){
                    String[] im = lined.split(" ");
                    String[] pac = lined.split(" |\\.");
                    if((im.length>0)&&(pac.length > 1)){
                        if(("import".equalsIgnoreCase(im[0]))&&(("org".equalsIgnoreCase(pac[1]))||("javax".equalsIgnoreCase(pac[1])))){
                            String nowDoc = im[1].substring(0, im[1].length()-1) + ".txt";
                            if(docId.get(nowDoc) != null){
                                String[] no =  filePaths.get(j).split("\\/|\\.");
                                String noString = "";
                                for(int n = 5; n < no.length - 2; n ++){
                                    noString = noString + no[n + 1] + ".";
                                }
                                String nowId = docId.get(noString + "txt");
                                String imId = docId.get(nowDoc);
                                if(Integer.parseInt(imId) > Integer.parseInt(nowId)){
                                    lastCitation.add(nowId + "," + imId);
                                }else{
                                    lastCitation.add(imId + "," + nowId);
                                }
                            }
                        }
                    }
                    lined = readerd.readLine();
                }
            }
        }
        String lastCitationStr = "";
        List<String> lCitation = removeDuplicate(lastCitation);
        for(int k = 0; k < lCitation.size(); k ++){
            lastCitationStr = lastCitationStr + lCitation.get(k) + "\n";
        }
        WriteToFile.contentToTextFile("/home/yangchen/ycdoc/lastCitation", lastCitationStr);
    }

    // sort citation from small to big
    public static void sortCitation() throws IOException {
        String filePath = "/home/yangchen/ycdoc/lastCitation";
        InputStream is = new FileInputStream(filePath);
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        int i = 0;
        String[] c = new String[4524];
        while (line != null){
            c[i] = line;
            i ++;
            line = reader.readLine();
        }
        reader.close();
        is.close();
        int flag = Integer.parseInt(c[0].split("\\,")[0]);
        int index = 0;
        String newC = "";
        int length = c.length;
        for(int j = 0; j < c.length; j++){
            for(int k = 1; k < length; k ++){
                if(Integer.parseInt(c[k].split("\\,")[0]) < flag){
                    flag = Integer.parseInt(c[k].split("\\,")[0]);
                    index = k;
                }
            }
            newC = newC + c[index] + "\n";
            for(int n = index; n < c.length-1; n++){
                c[n] = c[n + 1];
            }
            length --;
            index = 0;
            flag = Integer.parseInt(c[0].split("\\,")[0]);
        }
        WriteToFile.contentToTextFile("/home/yangchen/ycdoc/nlastCitation", newC);
    }

    //remove duplicate
    public static List<String> removeDuplicate(List list) {
        for ( int i = 0 ; i < list.size() - 1 ; i ++ ) {
            for ( int j = list.size() - 1 ; j > i; j -- ) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    //write module citation to file
    public static void getModuleCitation(int topicNum) throws IOException {
        Map<String, List<String>> citation = getCitation();
        Map<String, List<String>> moduleTopic = getModuleTopic(topicNum);
        Map<String, String> moduleDoc = getModuleDoc(topicNum);
        Map<String, Map<String, Integer>> moduleCitation = new HashMap<String, Map<String, Integer>>();
        for(int i = 0; i < topicNum; i++){
            List<String> docsId = moduleTopic.get(String.valueOf(i));
            Map<String, Integer> imsCount = new HashMap<String, Integer>();
            if(docsId != null) {
                for (int k = 0; k < topicNum; k++) {
                    imsCount.put(String.valueOf(k), 0);
                }
                for (int j = 0; j < docsId.size(); j++) {
                    List<String> imsId = citation.get(docsId.get(j));
                    if (imsId != null) {
                        for (int n = 0; n < imsId.size(); n++) {
                            String imsModuleId = moduleDoc.get(imsId.get(n));
                            int count = imsCount.get(imsModuleId) + 1;
                            imsCount.put(imsModuleId, count);
                        }
                    }
                }
                moduleCitation.put(String.valueOf(i), imsCount);
            }
        }

        String moduleCitationStr = "";
        for(int x = 0; x < moduleCitation.size(); x ++){
            Map<String, Integer> imsCountMap = moduleCitation.get(String.valueOf(x));
            if(imsCountMap != null){
                for(int y = 0; y < imsCountMap.size(); y ++){
                    if(x != y){
                        moduleCitationStr = moduleCitationStr + (x+1) + " " + (y+1) + " " +imsCountMap.get(String.valueOf(y)) + "\n";
                    }
                }
            }
        }
        WriteToFile.contentToTextFile("/home/yangchen/tomcatTempData/test/"+ topicNum +".txt", moduleCitationStr);
    }

    //get module docs whit the format of map, the key is topicId, and the value is docId
    public static Map<String, List<String>> getModuleTopic(int topicNum) throws IOException {
        Map<String, List<String>> module = new HashMap<String, List<String>>();
        Map<String, String> docId = getDocId();
        for(int i = 0; i < topicNum; i ++){
            List<String> docs = new ArrayList<String>();
            String filePaths = "/home/yangchen/tomcatTempData/temp" + String.valueOf(topicNum) + "/" + String.valueOf(i) + ".txt";
            File file = new File(filePaths);
            if(file.exists()){
                InputStream is = new FileInputStream(filePaths);
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                line = reader.readLine();
                while (line != null){
                    String[] content = line.split("\t");
                    String[] doc = content[1].split("\\/");
                    docs.add(docId.get(doc[doc.length-1]));
                    line = reader.readLine();
                }
                module.put(String.valueOf(i), docs);
            }
        }
        return module;
    }

    //get module docs whit the format of map, the key is  docId, and the value is topicId
    public static Map<String, String> getModuleDoc(int topicNum) throws IOException {
        Map<String, String> module = new HashMap<String, String>();
        Map<String, String> docId = getDocId();
        for(int i = 0; i < topicNum; i ++){
            String filePaths = "/home/yangchen/tomcatTempData/temp" + String.valueOf(topicNum) + "/" + String.valueOf(i) + ".txt";
            File file = new File(filePaths);
            if(file.exists()) {
                InputStream is = new FileInputStream(filePaths);
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                line = reader.readLine();
                while (line != null) {
                    String[] content = line.split("\t");
                    String[] doc = content[1].split("\\/");
                    module.put(docId.get(doc[doc.length - 1]), String.valueOf(i));
                    line = reader.readLine();
                }
            }
        }
        return module;
    }

    //get all citations of docs whit the format of map
    public static Map<String, List<String>> getCitation() throws IOException {
        String filePath = "/home/yangchen/ycdoc/nlastCitation";
        InputStream is = new FileInputStream(filePath);
        String line;
        Map<String, List<String>> citation = new HashMap<String, List<String>>();
        List<String> ims = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        String flag = line.split("\\,")[0];
        while (line != null){
            String[] id = line.split("\\,");
            if(!flag.equals(id[0])){
                citation.put(flag, ims);
                ims = new ArrayList<String>();
            }
            ims.add(id[1]);
            flag = id[0];
            line = reader.readLine();
        }
        citation.put(flag ,ims);
        reader.close();
        is.close();
        return citation;
    }

    //get docs id and the key is name, value is id
    public static Map<String, String> getDocId() throws IOException {
        String filePath = "/home/yangchen/ycdoc/filelist";
        InputStream is = new FileInputStream(filePath);
        String line;
        Map<String, String> docId = new HashMap<String, String>();
        int i = 1;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        while (line != null){
            String[] name = line.split("\\/");
            docId.put(name[5], String.valueOf(i));
            i ++;
            line = reader.readLine();
        }
        reader.close();
        is.close();
        return docId;
    }

    //get docs id and the key is name, value is id
    public static Map<String, String> getDocName() throws IOException {
        String filePath = "/home/yangchen/ycdoc/filelist";
        InputStream is = new FileInputStream(filePath);
        String line;
        Map<String, String> docId = new HashMap<String, String>();
        int i = 1;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        while (line != null){
            String[] name = line.split("\\/");
            docId.put(String.valueOf(i),name[5]);
            i ++;
            line = reader.readLine();
        }
        reader.close();
        is.close();
        return docId;
    }

    //get all files
    private static void showFiles(File[] files) {
        for(File file : files){
            if(file.isDirectory()){
//                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles());
            }else{
//                System.out.println(file.getAbsolutePath());
                filePaths.add(file.getAbsolutePath());
            }
        }
    }


    public static void setFilePaths(List<String> filePaths) {
        DocumentCitation.filePaths = filePaths;
    }

    public static List<String> getFilePaths() {
        return DocumentCitation.filePaths;
    }


    public static Map<String, List<String>> getModule(int topicNum) throws IOException {
        Map<String, List<String>> module = new HashMap<String, List<String>>();
        for(int i = 0; i < topicNum; i ++){
            List<String> docs = new ArrayList<String>();
            String filePaths = "/home/yangchen/tomcatTempData/temp" + String.valueOf(topicNum) + "/" + String.valueOf(i) + ".txt";
            File file = new File(filePaths);
            if(file.exists()){
                InputStream is = new FileInputStream(filePaths);
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                line = reader.readLine();
                while (line != null){
                    String[] content = line.split("\t");
                    String[] doc = content[1].split("\\/");
                    docs.add(doc[doc.length-1]);
                    line = reader.readLine();
                }
                module.put(String.valueOf(i), docs);
            }
        }
        return module;
    }

    //get topic doc names
    public static void moduleContent(int topicNum) throws IOException {
        Map<String, List<String>> all = getModule(topicNum);
        for(int i = 0; i < all.size(); i ++){
            List<String> module = all.get(String.valueOf(i));
            String content = "";
            for(int j = 0; j < module.size(); j ++){
                String filePath = "/home/yangchen/ycdoc/tomcatpre/" + module.get(j);
                InputStream is = new FileInputStream(filePath);
                String line;
                Map<String, String> docId = new HashMap<String, String>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                line = reader.readLine();
                while(line != null){
                    content = content + line + "\n";
                }
            }
            WriteToFile.contentToTextFile("//home/yangchen/ycdoc/module/" + i + ".txt", content);
        }
    }

    public static List<String> getFileNames(int topicNum) throws IOException {
        Map<String, List<String>> all = getModule(topicNum);
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < all.size(); i ++) {
            List<String> module = all.get(String.valueOf(i));
            List<String> oneName = new ArrayList<String>();
            for(int j = 0; j < module.size(); j ++){
                String[] name = module.get(j).split("\\/|\\.");
                for(int n = 0; n < name.length -1; n++){
                    oneName.add(name[n]);
                }
            }
            String reStr = "";
            List<String> re = removeDuplicate(oneName);
            if(re.size() > 15){
                for(int k = 0; k < 15; k ++){
                    reStr = reStr + re.get(k) + ".";
                }
            }else{
                for(int k = 0; k < re.size(); k ++){
                    reStr = reStr + re.get(k) + ".";
                }
            }
            result.add(reStr+"txt");
        }
        return result;
    }

    public static void mergeFiles(int topicNum) throws IOException, ParserConfigurationException, SAXException {
        FileChannel outChannel = null;
        int BUFSIZE = 1024 * 100;
        Map<String, List<String>> all = getModule(topicNum);
        List<String> names = GetRTCNames.getNames("/usr/local/mallet-2.0.7/tomcat" + topicNum + "/topic-phrases.xml");
        for(int i = 0; i < all.size(); i ++){
            List<String> module = all.get(String.valueOf(i));
            String outFile = "/home/yangchen/ycdoc/module/m"+ topicNum +"/" + names.get(i);
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


    //write module citation to file
    public static void getModuleCitationOnlyOne(int topicNum) throws IOException {
        Map<String, List<String>> citation = getCitation();
        Map<String, List<String>> moduleTopic = getModuleTopic(topicNum);
        Map<String, String> moduleDoc = getModuleDoc(topicNum);
        Map<String, Map<String, Integer>> moduleCitation = new HashMap<String, Map<String, Integer>>();
        for(int i = 0; i < topicNum; i++){
            List<String> docsId = moduleTopic.get(String.valueOf(i));
            Map<String, Integer> imsCount = new HashMap<String, Integer>();
            if(docsId != null) {
                for (int k = 0; k < topicNum; k++) {
                    imsCount.put(String.valueOf(k), 0);
                }
                for (int j = 0; j < docsId.size(); j++) {
                    List<String> imsId = citation.get(docsId.get(j));
                    if (imsId != null) {
                        for (int n = 0; n < imsId.size(); n++) {
                            String imsModuleId = moduleDoc.get(imsId.get(n));
                            int count = imsCount.get(imsModuleId) + 1;
                            imsCount.put(imsModuleId, count);
                        }
                    }
                }
                moduleCitation.put(String.valueOf(i), imsCount);
            }
        }

        String moduleCitationStr = "";
        for(int x = 0; x < moduleCitation.size(); x ++){
            Map<String, Integer> imsCountMap = moduleCitation.get(String.valueOf(x));
            if(imsCountMap != null){
                for(int y = 0; y < imsCountMap.size(); y ++){
                    if(x != y){
                        moduleCitationStr = moduleCitationStr + (x+1) + " " + (y+1) + " 1" + "\n";
                    }
                }
            }
        }
        WriteToFile.contentToTextFile("/home/yangchen/tomcatTempData/test/"+ topicNum +".txt", moduleCitationStr);
    }

    public static void getCitationRate(int topicNum) throws IOException {
        String filePath = "/home/yangchen/tomcatTempData/topicmodule/" + topicNum + ".txt";
        InputStream is = new FileInputStream(filePath);
        String line;
        Map<String, String> docId = new HashMap<String, String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        int num = 0;
        while (line != null){
            String[] content = line.split("\t");
            num = num + Integer.valueOf(content[2]);
            line = reader.readLine();
        }
        System.out.print(num);
    }

    public static void getTest() throws IOException {
        String filePath = "/home/yangchen/ycdoc/nlastCitation";
        InputStream is = new FileInputStream(filePath);
        String line;
        Map<String, String> docId = new HashMap<String, String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        String nl = "";
        while (line != null) {
            String[] t = line.split("\\,");
            nl = nl + t[0] + " " + t[1] + " "+ "1\n";
            line = reader.readLine();
        }
        WriteToFile.contentToTextFile("/home/yangchen/ycdoc/cita", nl);
    }



    //write new method module citation to file
    public static void getNewMethodModuleCitation() throws IOException {
        Map<String, List<String>> citation = getCitation();

        File[] files = new File("/home/yangchen/ycdoc/cataTemp").listFiles();
        List<String> paths = new ArrayList<String>();
        DocumentCitation.setFilePaths(paths);
        showFiles(files);
        //get module docs : all
        Map<String, List<String>> all = new HashMap<String, List<String>>();
        Map<String, String> moduleBelongTo = new HashMap<String, String>();
        for(int m = 0; m < filePaths.size(); m++) {
            InputStream is = new FileInputStream(filePaths.get(m));
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine();
            List<String> fileNames = new ArrayList<String>();
            String[] p = filePaths.get(m).split("\\/");
            String key = "org.apache.catalina." + p[p.length-1];
            while (line != null) {
                String[] content = line.split("\\/");
                fileNames.add(content[content.length - 1]);
                moduleBelongTo.put(content[content.length - 1], key);
                line = reader.readLine();
            }
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

        //should konw which part one doc belongs to

        Map<String, String> newkeyNames = new HashMap<String, String>();
        newkeyNames.put("org.apache.catalina.0.txt","0");
        newkeyNames.put("org.apache.catalina.ant.txt","1");
        newkeyNames.put("org.apache.catalina.authenticator.txt","2");
        newkeyNames.put("org.apache.catalina.comet.txt","3");
        newkeyNames.put("org.apache.catalina.connector.txt","4");
        newkeyNames.put("org.apache.catalina.core.txt","5");
        newkeyNames.put("org.apache.catalina.deploy.txt","6");
        newkeyNames.put("org.apache.catalina.filters.txt","7");
        newkeyNames.put("org.apache.catalina.ha.txt","8");
        newkeyNames.put("org.apache.catalina.loader.txt","9");
        newkeyNames.put("org.apache.catalina.manager.txt","10");
        newkeyNames.put("org.apache.catalina.mbeans.txt","11");
        newkeyNames.put("org.apache.catalina.realm.txt","12");
        newkeyNames.put("org.apache.catalina.security.txt","13");
        newkeyNames.put("org.apache.catalina.servlets.txt","14");
        newkeyNames.put("org.apache.catalina.session.txt","15");
        newkeyNames.put("org.apache.catalina.ssi.txt","16");
        newkeyNames.put("org.apache.catalina.startup.txt","17");
        newkeyNames.put("org.apache.catalina.tribes.txt","18");
        newkeyNames.put("org.apache.catalina.users.txt","19");
        newkeyNames.put("org.apache.catalina.util.txt","20");
        newkeyNames.put("org.apache.catalina.valves.txt","21");
        newkeyNames.put("org.apache.catalina.websocket.txt","22");




        //Map<String, List<String>> moduleTopic = getModuleTopic(topicNum);
        //Map<String, String> moduleDoc = getModuleDoc(topicNum);

        Map<String, String> getId = getDocId();
        Map<String, String> getName = getDocName();
        Map<String, Map<String, Integer>> moduleCitation = new HashMap<String, Map<String, Integer>>();
        for(int i = 0; i < all.size(); i++){
            List<String> docsId = all.get(keyNames.get(String.valueOf(i)));
            Map<String, Integer> imsCount = new HashMap<String, Integer>();
            if(docsId != null) {
                for (int k = 0; k < all.size(); k++) {
                    imsCount.put(String.valueOf(k), 0);
                }
                for (int j = 0; j < docsId.size(); j++) {
                    List<String> imsId = citation.get(getId.get(docsId.get(j)));
                    if (imsId != null) {
                        for (int n = 0; n < imsId.size(); n++) {
                            if(getName.get(imsId.get(n)) != null) {
                                String imsModuleName = moduleBelongTo.get(getName.get(imsId.get(n)));
                                if(imsModuleName != null) {
                                    String imsModuleId = newkeyNames.get(imsModuleName);
                                    int count = imsCount.get(imsModuleId) + 1;
                                    imsCount.put(imsModuleId, count);
                                }
                            }
                        }
                    }
                }
                moduleCitation.put(String.valueOf(i), imsCount);
            }
        }

        String moduleCitationStr = "";
        for(int x = 0; x < moduleCitation.size(); x ++){
            Map<String, Integer> imsCountMap = moduleCitation.get(String.valueOf(x));
            if(imsCountMap != null){
                for(int y = 0; y < imsCountMap.size(); y ++){
                    if(x != y){
                        moduleCitationStr = moduleCitationStr + (x+1) + " " + (y+1) + " 1" + "\n";
                    }
                }
            }
        }
        WriteToFile.contentToTextFile("/home/yangchen/ycdoc/cataModuleFull/newMethod.txt", moduleCitationStr);
    }

}
