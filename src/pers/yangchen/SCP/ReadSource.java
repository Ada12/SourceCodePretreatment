package pers.yangchen.SCP;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchen on 16-4-1.
 */
public class ReadSource {

    private static List<String> filePaths;

    public static void main(String[] args){
        File[] files = new File("C:\\Users\\yangchen\\Desktop\\apache-tomcat-7.0.68-src\\java").listFiles();
        List<String> paths = new ArrayList<String>();
        ReadSource.setFilePaths(paths);
        showFiles(files);
        for(int i = 0; i < filePaths.size(); i ++){
            String[] f = filePaths.get(i).split("\\.");
            if(f[f.length-1].equals("java")){
                BasicHandle basicHandle = new BasicHandle();
                try {
                    basicHandle.HandleCode(filePaths.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        test();
//        List<String> lw  = testSeparate("configErrMsg");
//        for(int i = 0; i < lw.size(); i ++){
//            System.out.println(lw.get(i));
//        }
    }

    private static void showFiles(File[] files) {
        for(File file : files){
            if(file.isDirectory()){
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles());
            }else{
               // System.out.println("File: " + file.getAbsolutePath());
                filePaths.add(file.getAbsolutePath());
            }
        }
    }

    public static void test(){
        String a = "C:\\Users\\yangchen\\Desktop\\apache-tomcat-7.0.68-src\\java\\org\\apache\\catalina\\ant\\AbstractCatalinaCommandTask.java";
        String[] name = a.split("\\\\|\\.");
        String nameString = "";
        for(int n = 7; n < name.length - 2; n ++){
                nameString = nameString + name[n + 1] + ".";
        }
        System.out.print(nameString);
    }

    public static List<String> testSeparate(String wordString){
        String[] wordsUnderline = wordString.split("\\_");
        int flag;
        List<String> words = new ArrayList<String>();
        for(int j = 0; j < wordsUnderline.length; j ++){
            String word = "" + wordsUnderline[j].charAt(0);
            if(Character.isLowerCase(wordsUnderline[j].charAt(0))){
                flag = 0;//lowercase is 0
            }else{
                flag = 1;//capital is 1
            }
            for(int i = 1; i < wordsUnderline[j].length(); i++){
                char l = wordsUnderline[j].charAt(i);
                if ((!Character.isLowerCase(l))&&(flag == 0)){
                    words.add(word);
                    word = "" + wordsUnderline[j].charAt(i);
                    flag = 1;
                }else if((!Character.isLowerCase(l))&&(flag == 1)){
                    word = word + wordsUnderline[j].charAt(i);
                    flag = 1;
                }else{
                    word = word + wordsUnderline[j].charAt(i);
                    flag = 0;
                }
            }
            words.add(word);
        }
        return words;
    }

    public static void setFilePaths(List<String> filePaths) {
        ReadSource.filePaths = filePaths;
    }

    public static List<String> getFilePaths() {
        return ReadSource.filePaths;
    }
}
