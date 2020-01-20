package com.codeForma;

import java.io.*;

/**
 * @description:
 * @author: leesin
 * @date: Created in 2020/1/19 5:30 下午
 * @version:
 * @modified By:
 */
public abstract class FormCommon {
    /**
     * @description: 获取文件中的内容
     * @name: getFileString
     * @return: org.apache.xpath.operations.String
     * @date: 2020/1/19 4:05 下午
     * @auther: leesin
     **/
    public static String getFileString(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("file do not exists");
        }
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        return stringBuilder.toString();
    }

    public static void saveAsFile(String filePath, String content) {
        FileWriter fwriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
//                fwriter = new FileWriter(filePath, true);
            fwriter = new FileWriter(filePath);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

