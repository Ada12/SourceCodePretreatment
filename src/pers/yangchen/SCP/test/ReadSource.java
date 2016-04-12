package pers.yangchen.SCP.test;
import java.io.File;
import java.io.IOException;

/**
 * Created by yangchen on 16-4-1.
 */
public class ReadSource {

    public static void main(String[] args){
        File[] files = new File("/home/yangchen/ycdoc/apache-tomcat-7/java").listFiles();
        showFiles(files);
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

}
