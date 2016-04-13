package pers.yangchen.SCP;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchen on 16-4-1.
 */
public class ReadSource {

    public static void main(String[] args){
//        File[] files = new File("C:\\Users\\yangchen\\Desktop\\apache-tomcat-7.0.68-src\\java").listFiles();
//        showFiles(files);

//        String filePath = "C:\\Users\\yangchen\\Desktop\\apache-tomcat-7.0.68-src\\java\\org\\apache\\catalina\\ant\\AbstractCatalinaCommandTask.java";
//        BasicHandle basicHandle = new BasicHandle();
//        try {
//            basicHandle.HandleCode(filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        test();
        List<String> lw  = testSeparate("BuildException");
        for(int i = 0; i < lw.size(); i ++){
            System.out.println(lw.get(i));
        }
    }

    private static void showFiles(File[] files) {
        for(File file : files){
            if(file.isDirectory()){
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles());
            }else{
                System.out.println("File: " + file.getName());
            }
        }
    }

    public static void test(){
        String a = "C:\\Users\\yangchen\\Desktop\\apache-tomcat-7.0.68-src\\java\\org\\apache\\catalina\\ant\\AbstractCatalinaCommandTask.java";
        String[] aa = a.split("\\\\|\\.");
        for (int i = 0; i< aa.length; i++){
            System.out.println(aa[i]);
        }
    }

    public static List<String> testSeparate(String wordString){
        List<String> words = new ArrayList<String>();
        String word = "" + wordString.charAt(0);
        for(int i = 1; i < wordString.length(); i++){
            char l = wordString.charAt(i);
            if (Character.isLowerCase(l)){
                word = word + wordString.charAt(i);
            }else{
                words.add(word);
                word = "" + wordString.charAt(i);
            }
        }
        words.add(word);
        return words;
    }

}
