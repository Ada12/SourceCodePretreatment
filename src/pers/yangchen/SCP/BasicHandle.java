package pers.yangchen.SCP;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    String[] separatedWord = words[i].split("\\(|\\)|\\;|\\{|\\}|\\?|\\=|\\&|\\!|\"|\\'|\\,|\\>|\\<|\\*|\\.|\\/|\\-|\\@|\\:|\\#|\\%|\\^|\\[|\\]|\\+");
                    for(int j = 0; j < separatedWord.length; j++){
                        if(!separatedWord[j].isEmpty()){
                            List<String> lw = separateWords(separatedWord[j]);
                            for(int m = 0; m < lw.size(); m ++){
                                if(lw.get(m).length() > 3){
                                    if((!lw.get(m).equals("package"))&&(!lw.get(m).equals("org"))&&(!lw.get(m).equals("apache"))&&(!lw.get(m).equals("import"))&&(!lw.get(m).equals("java"))&&
                                            (!lw.get(m).equals("string"))&&(!lw.get(m).equals("public"))&&(!lw.get(m).equals("private"))&&(!lw.get(m).equals("protected"))&&(!lw.get(m).equals("static"))&&
                                            (!lw.get(m).equals("final"))&&(!lw.get(m).equals("boolean"))&&(!lw.get(m).equals("void"))&&(!lw.get(m).equals("object"))&&(!lw.get(m).equals("interface "))&&
                                            (!lw.get(m).equals("extends"))&&(!lw.get(m).equals("javax"))&&(!lw.get(m).equals("class"))&&(!lw.get(m).equals("beans"))&&(!lw.get(m).equals("persistence"))&&(!lw.get(m).equals("type"))&&
                                            (!lw.get(m).equals("null"))&&(!lw.get(m).equals("return"))&&(!lw.get(m).equals("lang"))&&(!lw.get(m).equals("new"))&&(!lw.get(m).equals("else"))&&(!lw.get(m).equals("throws"))&&
                                            (!lw.get(m).equals("whit"))&&(!lw.get(m).equals("that"))&&(!lw.get(m).equals("long"))&&(!lw.get(m).equals("args"))&&(!hasDigit(lw.get(m)))) {
                                        handeldContext.add(lw.get(m).toLowerCase());
                                    }
                                }
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
        String nameString = "";
        for(int n = 7; n < name.length - 2; n ++){
            nameString = nameString + name[n + 1] + ".";
        }
        fileNameLast = "C:\\Users\\yangchen\\Desktop\\experimentdata\\"+ nameString +"txt";
        contentToTextFile(fileNameLast,lastContext.trim());
    }

    // determin whether the string has digit
    public boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches())
            flag = true;
        return flag;
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
                System.out.print(f.getName() + " exits");
            } else {
                System.out.print(f.getName() + " not exits");
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

    public static List<String> separateWords(String wordString){
        String[] wordsUnderline = wordString.split("\\_");
        int flag;
        List<String> words = new ArrayList<String>();
        for(int j = 0; j < wordsUnderline.length; j ++){
            if(!wordsUnderline[j].isEmpty()){
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
        }
        return words;
    }
}
