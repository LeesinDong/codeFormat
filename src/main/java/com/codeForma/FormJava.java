package com.codeForma;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FormJava extends FormCommon{
    public static String format(String code) throws Exception {
        code = formJava(code);
        return code;
    }

        /**
         * @description: 格式化Java
         * @name: formJava
         * @return: java.lang.String
         * @date: 2020/1/19 1:56 下午
         * @auther: leesin
        **/
    public static String formJava(String data) {
        String dataTmp = replaceStrToUUid(data, "\"");
        dataTmp = replaceStrToUUid(dataTmp, "'");
        //原来的换行替换为""，因为传进来的就是有换行的。
        //如果没有换行，就想办法，粘贴的有换行，一般粘贴pdf、word都有换行的。
        //dataTmp = repalceHHFStart(dataTmp,"\n","");
        //dataTmp = repalceHHF(dataTmp,"\n","");
        dataTmp = repalceHHF(dataTmp, "{", "{\n");
        dataTmp = repalceHHF(dataTmp, "}", "}\n");
        dataTmp = repalceHHF(dataTmp, "/*", "\n/*\n");
        dataTmp = repalceHHF(dataTmp, "* @", "\n* @");
        dataTmp = repalceHHF(dataTmp, "*/", "\n*/\n");
        dataTmp = repalceHHF(dataTmp, ";", ";\n");
        //原来的处理注释的地方，这里会造成注释后面跟一行代码不换行
        //dataTmp = repalceHHF(dataTmp,"//","\n//");
        dataTmp = repalceHHFX(dataTmp, "\n");
        for (Map.Entry<String, String> r : mapZY.entrySet()) {
            dataTmp = dataTmp.replace(r.getKey(), r.getValue());
        }
        if (dataTmp == null)
            return data;
        return dataTmp;
    }
    
    /**
     * @description: 格式化Xml，已作废，有好的轮子，不往下写了
     * @name: formXml  
     * @return: java.lang.String
     * @date: 2020/1/19 1:56 下午
     * @auther: leesin
    **/
    public static String  formXml(String data){
        String dataTmp = replaceStrToUUid(data, "\"");
        dataTmp = replaceStrToUUid(dataTmp, "'");
        dataTmp = repalceHHF(dataTmp, ">", ">\n");
        dataTmp = repalceHHF(dataTmp, "<", "\n<");
        //原来的处理注释的地方，这里会造成注释后面跟一行代码不换行
        //dataTmp = repalceHHF(dataTmp,"//","\n//");
        dataTmp = repalceHHFX(dataTmp, "\n");
        for (Map.Entry<String, String> r : mapZY.entrySet()) {
            dataTmp = dataTmp.replace(r.getKey(), r.getValue());
        }
        if (dataTmp == null)
            return data;
        return dataTmp;
    }
    
    public static Map<String, String> mapZY = new HashMap<String, String>();

    /**
     * @说明 ： 循环替换指定字符为随机uuid  并将uui存入全局map:mapZY
     * @参数 ：@param string   字符串
     * @参数 ：@param type    指定字符
     * @作者 ：WangXL
     * @时间 ：2018 11 23
     **/
    public static String replaceStrToUUid(String string, String type) {
        Matcher slashMatcher = Pattern.compile(type).matcher(string);
        boolean bool = false;
        StringBuilder sb = new StringBuilder();
        int indexHome = -1; //开始截取下标
        while (slashMatcher.find()) {
            int indexEnd = slashMatcher.start();
            String tmp = string.substring(indexHome + 1, indexEnd); //获取"号前面的数据
            if (indexHome == -1 || bool == false) {
                sb.append(tmp);
                bool = true;
                indexHome = indexEnd;
            } else {
                if (bool) {
                    String tem2 = "";
                    for (int i = indexEnd - 1; i > -1; i--) {
                        char c = string.charAt(i);
                        if (c == '\\') {
                            tem2 += c;
                        } else {
                            break;
                        }
                    }
                    int tem2Len = tem2.length();
                    if (tem2Len > -1) {
                        //结束符前有斜杠转义符 需要判断转义个数奇偶   奇数是转义了  偶数才算是结束符号
                        if (tem2Len % 2 == 1) {
                            //奇数 非结束符
                        } else {
                            //偶数才算是结束符号
                            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                            uuid = type + uuid + type;
                            mapZY.put(uuid, type + tmp + type);
                            sb.append(uuid);
                            bool = false;
                            indexHome = indexEnd;
                        }
                    }
                }
            }
        }
        sb.append(string.substring(indexHome + 1, string.length()));
        return sb.toString();
    }


    /**
     * @description:处理换行
     * @name: repalceHHF
     * @return: java.lang.String
     * @date: 2020/1/12 10:24 上午
     * @auther: leesin
     **/
    public static String repalceHHF(String data, String a, String b) {
        try {
            data = data.replace(a, "$<<yunwangA>>$<<yunwangB>>");
            String arr[] = data.split("$<<yunwangA>>");
            StringBuilder result = new StringBuilder();
            if (arr != null) {
                for (int i = 0; i < arr.length; i++) {
                    String t = arr[i];
                    result.append(t.trim());
                    if (t.indexOf("//") != -1 && "\n".equals(a)) {
                        result.append("\n");
                    }
                }
            }
            String res = result.toString();
            res = res.replace("$<<yunwangB>>", b);
            res = res.replace("$<<yunwangA>>", "");
            return res;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @param data
     * @param a    这里的a就是"\n"
     * @param b    b就是""
     * @name: repalceHHFStart
     * @description: 开始之前包含//的和@的不要去掉回车，即注释和注解
     * 为什么这里要分成两个方法呢？因为这个方法在每个字符串里面进行了一次替换和判断，可能会影响性能。
     * @return: java.lang.String
     * @date: 2020/1/12 10:07 上午
     * @auther: leesin
     **/
    public static String repalceHHFStart(String data, String a, String b) {
        if (!("\n").equals(a) && a != null) return "a must be \n !";
        data = data.replace(a, "$<<yunwangA>>$<<yunwangB>>");
        String arr[] = data.split("$<<yunwangA>>");
        StringBuilder result = new StringBuilder();
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                //这里的for里面只有一个，因为传进来的就是一个没有/n的字符串
                String t = arr[i];
                //如果字符串中有//和@，即注释或者注解
                if (t.indexOf("//") != -1 || t.indexOf("@") != -1) {
                    //不处理，即不要把\n去掉。
                    System.out.println("enter contains // !!!");
                } else {
                    t = t.replace("$<<yunwangB>>", b);
                    t = t.replace("$<<yunwangA>>", "");
                }
                result.append(t.trim());
                if (t.indexOf("//") != -1 && "\n".equals(a)) {
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }

    /**
     * @description: 处理缩进,分割方法(方法之间加回车)
     * @name: repalceHHFX
     * @return: java.lang.String
     * @date: 2020/1/14 7:20 上午
     * @auther: leesin
     **/
    public static String repalceHHFX(String data, String split) {
        try {
            String arr[] = data.split(split);
            StringBuilder result = new StringBuilder();
            if (arr != null) {
                String zbf = "    ";
                //堆栈里面存的是有几个tab的意思。
                Stack<String> stack = new Stack<String>();
                for (int i = 0; i < arr.length; i++) {
                    //每一行的数据
                    String line = arr[i].trim();
                    if (line.indexOf("{") != -1) {
                        //得到栈顶的数据，但是不弹出
                        String NumberOfSpaces = getStack(stack, false);
                        //首次进去肯定是null
                        if (NumberOfSpaces == null) {
                            //首次添加回车
                            result.append((line + "\n"));
                            //将空的放回给kg
                            NumberOfSpaces = "";
                        } else {
                            //第二次{开头的进来，变成了""+"tab"
                            NumberOfSpaces = NumberOfSpaces + zbf;
                            //返回值是tab加行加回车
                            result.append(NumberOfSpaces + line + "\n");
                        }
                        //将 压入栈，首次则将空的压入栈，第二次将tab压入栈
                        stack.push(NumberOfSpaces);
                    } else if (line.indexOf("}") != -1) {
                        String NumberOfSpaces = getStack(stack, true);
                        if ("".equals(NumberOfSpaces)) {
                            //这里说明是结尾的}，多加一个回车，分割方法
                            if (i == arr.length-1) {
                                //如果已经是最后一个{的话，不多加回车
                                result.append(line+"\n");
                            } else {
                                result.append(line+"\n\n");
                            }
                        } else {
                        }
                    } else {
                        String NumberOfSpaces = getStack(stack, false);
                        if (NumberOfSpaces == null) {
                            result.append(line + "\n");
                        } else {
                            result.append(NumberOfSpaces + zbf + line + "\n");
                        }
                    }
                }
            }
            String res = result.toString();
            return res;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @说明 ： 获得栈数据
     * @参数 ：@param stack
     * @参数 ：@param bool true 弹出  false 获取
     * @时间 ：2018 11 22
     **/
    public static String getStack(Stack<String> stack, boolean bool) {
        String result = null;
        try {
            if (bool) {
                return stack.pop();
            }
            return stack.peek();
        } catch (EmptyStackException e) {
        }
        return result;
    }

    /**
     * @description:拼装字符串，如果是注释或者注解的话，就保留换行符 解决注释//和注解@不换行不兼容的问题
     * @name: getString
     * @return: java.lang.String
     * @date: 2020/1/14 7:00 上午
     * @auther: leesin
     **/
    public static String getCodeString(String code, List<String> starts, List<String> ends) throws Exception {
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(new StringReader(code));
        while ((line = bufferedReader.readLine()) != null) {
            for (int i = 0; i < starts.size(); i++) {
                if (line.startsWith(starts.get(i))) {
                    line = line + "\n";
                }
            }
            for (int i = 0; i < ends.size(); i++) {
                if (line.endsWith(ends.get(i))) {
                    line = line + "\n";
                }
            }
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

}
