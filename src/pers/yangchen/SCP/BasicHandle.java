package pers.yangchen.SCP;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchen on 2016/4/13.
 */
public class BasicHandle {

    public void HandleCode(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        List<String> handeldContext = new ArrayList<String>();
        int flag = 0;
        String fileNameLast = "";
        int myline = 0;
        while (line != null){
            line = line.trim();
            String[] words = line.split(" ");
            if(words[0].equals("package")){
                flag = 1;
                fileNameLast = words[1].substring(0, words[1].length()-2);
            }
            if((flag == 0)&&((words[0].equals("/*"))||(words[0].equals("*"))||(words[0].equals("*/"))||(words[0].equals("package")))){
                line = reader.readLine();
            }else{
                for(int i = 0; i < words.length; i++){
                    if(words[i].length() > 1){
                        String[] separatedWord = words[i].split("\\(|\\)|\\;|\\{|\\}|\\?|\\=|\\&|\\!|\"|\\'|\\,|\\>|\\<|\\*|\\.|\\/|\\-|\\@|\\:");
                        for(int j = 0; j < separatedWord.length; j++){
                            if(!separatedWord[j].isEmpty()){
                                handeldContext.add(separatedWord[j]);
                            }
                        }
                    }
                }
                line = reader.readLine();
            }
        }
        reader.close();
        is.close();
        String lastContext = "";
        for(int k = 0; k < handeldContext.size(); k++){
            lastContext = lastContext + " " + handeldContext.get(k);
        }
        String[] name = filePath.split("\\\\|\\.");
        fileNameLast = "C:\\Users\\yangchen\\Desktop\\experimentdata\\"+ name[name.length-2] +".txt";
        contentToTextFile(fileNameLast,lastContext.trim());
    }

    // determine if a word is in an array
    public boolean isIn(String[] array, String word){
        for(int i = 0; i < array.length; i++){
            if(array[i].equals(word)){
                return true;
            }
        }
        return false;
    }

    // remove words useless word
    public List<String> RemoveUselessWord(String[] words){
        List<String> newWords = new ArrayList<String>();
        return newWords;
    }

    // handled well content write to file
    public static void contentToTextFile(String filePath, String content) {
        String str = new String(); // original content
        String s1 = new String();// update content
        try {
            File f = new File(filePath);
            if (f.exists()) {
                System.out.print("File exits");
            } else {
                System.out.print("File not exits");
                f.createNewFile();// create one
            }
            BufferedReader input = new BufferedReader(new FileReader(f));

            while ((str = input.readLine()) != null) {
                s1 += str + "\n";
            }
            System.out.println(s1);
            input.close();
            s1 += content;

            BufferedWriter output = new BufferedWriter(new FileWriter(f));
            output.write(s1);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
