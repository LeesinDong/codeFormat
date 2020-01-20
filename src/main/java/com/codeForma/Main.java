package com.codeForma;

import java.util.ArrayList;
import java.util.List;


public class Main {
    /**
     * @description: 格式化代码
     * @name: main
     * @return: void
     * @date: 2020/1/14 9:25 上午
     * @auther: leesin
    **/
    public static void main(String[] args) throws Exception {

        //同时处理的注释、注解换行的问题，
        //以starts开头和以ends结尾的都保留换行
        String[] startsString = new String[]{"//","@","<!--"};
        String[] endsString = new String[]{"-->"};
        List<String> starts = new ArrayList<String>();
        List<String> ends = new ArrayList<String>();
        for (int i = 0; i < startsString.length; i++) {
            starts.add(startsString[i]);
        }
        for (int i = 0; i < ends.size(); i++) {
            ends.add(endsString[i]);
        }
        //  /Users/leesin/我的坚果云/_posts/study/架构师内功心法/经典框架中常见的设计模式/aaa.md
        //codeString是没有回车的，fileString是原版
        String fileString = FormCommon.getFileString(args[0]);
        String codeString = FormJava.getCodeString(fileString,starts,ends);
        StringBuilder stringBuilder = new StringBuilder();
        String[] fileSplit = fileString.split("\\`\\`\\`");
        String[] codeSplit = codeString.split("\\`\\`\\`");
        for (int i = 0; i < fileSplit.length; i++) {
            String fileSplitTheOne = fileSplit[i];
            String codeSplitTheOne = codeSplit[i];
            //排除没有```的
            if (fileSplitTheOne.startsWith("java")) {
                String code = codeSplitTheOne.substring(4);
                code = FormJava.format(code);
                stringBuilder.append("```java\n"+code+"```");
            } else if (fileSplitTheOne.startsWith("xml")) {
                String code = codeSplitTheOne.substring(3);
                 code = FormatXml.format(code);
                stringBuilder.append("```xml\n"+code+"```");
            } else {
                //开始第一个不是代码的直接加
                stringBuilder.append(fileSplitTheOne);
            }
        }
        //System.out.println(stringBuilder);
        //直接把源文件内容替换掉
        FormCommon.saveAsFile(args[0],stringBuilder.toString());
    }
}