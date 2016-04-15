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
                                    if((!"abstract".equalsIgnoreCase(lw.get(m)))&&(!"assert".equalsIgnoreCase(lw.get(m)))&&(!"boolean".equalsIgnoreCase(lw.get(m)))&&(!"break".equalsIgnoreCase(lw.get(m)))&&(!"byte".equalsIgnoreCase(lw.get(m)))&&
                                            (!"case".equalsIgnoreCase(lw.get(m)))&&(!"catch".equalsIgnoreCase(lw.get(m)))&&(!"char".equalsIgnoreCase(lw.get(m)))&&(!"class".equalsIgnoreCase(lw.get(m)))&&(!"const".equalsIgnoreCase(lw.get(m)))&&
                                            (!"continue".equalsIgnoreCase(lw.get(m)))&&(!"default".equalsIgnoreCase(lw.get(m)))&&(!"do".equalsIgnoreCase(lw.get(m)))&&(!"double".equalsIgnoreCase(lw.get(m)))&&(!"else".equalsIgnoreCase(lw.get(m)))&&
                                            (!"enum".equalsIgnoreCase(lw.get(m)))&&(!"extends".equalsIgnoreCase(lw.get(m)))&&(!"final".equalsIgnoreCase(lw.get(m)))&&(!"finally".equalsIgnoreCase(lw.get(m)))&&(!"float".equalsIgnoreCase(lw.get(m)))&&
                                            (!"for".equalsIgnoreCase(lw.get(m)))&&(!"if".equalsIgnoreCase(lw.get(m)))&&(!"implements".equalsIgnoreCase(lw.get(m)))&&(!"import".equalsIgnoreCase(lw.get(m)))&&(!"instanceof".equalsIgnoreCase(lw.get(m)))&&
                                            (!"int".equalsIgnoreCase(lw.get(m)))&&(!"interface".equalsIgnoreCase(lw.get(m)))&&(!"long".equalsIgnoreCase(lw.get(m)))&&(!"native".equalsIgnoreCase(lw.get(m)))&&(!"new".equalsIgnoreCase(lw.get(m)))&&
                                            (!"package".equalsIgnoreCase(lw.get(m)))&&(!"private".equalsIgnoreCase(lw.get(m)))&&(!"protected".equalsIgnoreCase(lw.get(m)))&&(!"public".equalsIgnoreCase(lw.get(m)))&&(!"return".equalsIgnoreCase(lw.get(m)))&&
                                            (!"short".equalsIgnoreCase(lw.get(m)))&& (!"static".equalsIgnoreCase(lw.get(m)))&& (!"strictfp".equalsIgnoreCase(lw.get(m)))&& (!"super".equalsIgnoreCase(lw.get(m)))&& (!"switch".equalsIgnoreCase(lw.get(m)))&&
                                            (!"synchronized".equalsIgnoreCase(lw.get(m)))&& (!"this".equalsIgnoreCase(lw.get(m)))&& (!"throw".equalsIgnoreCase(lw.get(m)))&& (!"throws".equalsIgnoreCase(lw.get(m)))&& (!"transient".equalsIgnoreCase(lw.get(m)))&&
                                            (!"try".equalsIgnoreCase(lw.get(m)))&& (!"void".equalsIgnoreCase(lw.get(m)))&& (!"volatile".equalsIgnoreCase(lw.get(m)))&&(!"while".equalsIgnoreCase(lw.get(m)))&&(!"javax".equalsIgnoreCase(lw.get(m)))&&
                                            (!"annotation".equalsIgnoreCase(lw.get(m)))&&(!"persistence".equalsIgnoreCase(lw.get(m)))&&(!"coyote".equalsIgnoreCase(lw.get(m)))&&(!"tomcat".equalsIgnoreCase(lw.get(m)))&&(!"apache".equalsIgnoreCase(lw.get(m)))&&
                                            (!"tribes".equalsIgnoreCase(lw.get(m)))&&(!"catalina".equalsIgnoreCase(lw.get(m)))&&(!"ioexception".equalsIgnoreCase(lw.get(m)))&&(!"string".equalsIgnoreCase(lw.get(m)))&&(!"null".equalsIgnoreCase(lw.get(m)))&&
                                            (!"util".equalsIgnoreCase(lw.get(m)))&&(!"factory".equalsIgnoreCase(lw.get(m)))&&(!"param".equalsIgnoreCase(lw.get(m)))&&(!"override".equalsIgnoreCase(lw.get(m)))&&(!"list".equalsIgnoreCase(lw.get(m)))&&
                                            (!"iterator".equalsIgnoreCase(lw.get(m)))&&(!"async".equalsIgnoreCase(lw.get(m)))&&(!"type".equalsIgnoreCase(lw.get(m)))&&(!"object".equalsIgnoreCase(lw.get(m)))&&(!"error".equalsIgnoreCase(lw.get(m)))&&
                                            (!"mbean".equalsIgnoreCase(lw.get(m)))&&(!"exception".equalsIgnoreCase(lw.get(m)))&&(!"mbean".equalsIgnoreCase(lw.get(m)))&&(!"chars".equalsIgnoreCase(lw.get(m)))&&(!"arrays".equalsIgnoreCase(lw.get(m)))&&
                                            (!"encoded".equalsIgnoreCase(lw.get(m)))&&(!"encode".equalsIgnoreCase(lw.get(m)))&&(!"line".equalsIgnoreCase(lw.get(m)))&&(!"nonce".equalsIgnoreCase(lw.get(m)))&&
                                            (!hasDigit(lw.get(m)))){
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
        String[] name = filePath.split("\\/|\\.");
        String nameString = "";
        for(int n = 5; n < name.length - 2; n ++){
            nameString = nameString + name[n + 1] + ".";
        }
        fileNameLast = "/home/yangchen/ycdoc/tomcatpre/"+ nameString +"txt";
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
