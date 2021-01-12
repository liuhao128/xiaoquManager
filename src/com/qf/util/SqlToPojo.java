package com.qf.util;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author RW daze
 * @date 2020/12/28 16:50
 */
public class SqlToPojo {
    public static void main(String[] args) throws Exception {
        String pojoPakage = "com.qf.pojo";
        File sqlFile = new File("D:\\learnsoftware\\Sybase\\workspace\\sql文件\\二阶段数据库.sql");
        File fileDir = new File("D:\\learnsoftware\\Sybase\\workspace\\sql文件\\test");
        if (!sqlFile.exists() || !sqlFile.isFile() || !fileDir.isDirectory()) {
            System.out.println("路径不合法！");
        }

        File pojoDir = new File(fileDir + "\\pojo");

        if (!pojoDir.exists()) {
            pojoDir.mkdirs();
        }

        String sqlRegex = "create table (\\w)(\\w+)\\s*\\((.*?)primary key \\((\\w+)\\).*?\\);";
        Pattern sqlPattern = Pattern.compile(sqlRegex, Pattern.MULTILINE | Pattern.DOTALL);
        String lineRegex = "\\s+(\\w)(\\w+)\\s+(\\w+).*,";
        Pattern linePattern = Pattern.compile(lineRegex);
        FileInputStream sqlFis = new FileInputStream(sqlFile);
        InputStreamReader sqlIsr = new InputStreamReader(sqlFis);
        char[] chars = new char[1024];
        int len = 0;
        StringBuilder sb = new StringBuilder();
        while ((len = sqlIsr.read(chars, 0, chars.length)) != -1) {
            sb.append(chars, 0, len);
        }
        sqlIsr.close();
        sqlFis.close();
        Matcher sqlMatcher = sqlPattern.matcher(sb);

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw;
        while (sqlMatcher.find()) {
            String upperName = sqlMatcher.group(1).toUpperCase() + sqlMatcher.group(2);
            String lowerName = sqlMatcher.group(1).toLowerCase() + sqlMatcher.group(2);

            Matcher lineMatcher = linePattern.matcher(sqlMatcher.group(3));
            String Key = sqlMatcher.group(4);
            fos = new FileOutputStream(new File(pojoDir.getAbsolutePath() + "\\" + upperName + ".java"));
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
            bw.write("package " + pojoPakage + ";");
            bw.newLine();
            bw.write("import lombok.*;");
            bw.newLine();
            bw.write("import java.util.Date;");
            bw.newLine();
            bw.write("@Data");
            bw.newLine();
            bw.write("@NoArgsConstructor");
            bw.newLine();
            bw.write("@AllArgsConstructor");
            bw.newLine();
            bw.write("@ToString");
            bw.newLine();
            bw.write("@EqualsAndHashCode");
            bw.newLine();
            bw.write("public class " + upperName + " {");
            bw.newLine();
            while (lineMatcher.find()) {
                bw.write("    private ");
                String type = lineMatcher.group(3);
                String javaType = null;
                if ("int".equalsIgnoreCase(type) || "integer".equalsIgnoreCase(type)) {
                    javaType = "Integer";
                } else if ("Double".equalsIgnoreCase(type)) {
                    javaType = "Double";
                } else if ("Date".equalsIgnoreCase(type) || "DateTime".equalsIgnoreCase(type) || "Timestamp".equalsIgnoreCase(type)) {
                    javaType = "Date";
                } else if ("Varchar".equalsIgnoreCase(type) || "Text".equalsIgnoreCase(type)) {
                    javaType = "String";
                }
                bw.write(javaType + " " + lineMatcher.group(1).toLowerCase() + lineMatcher.group(2) + ";");
                bw.newLine();
            }
            bw.write("}");
            bw.newLine();
            bw.close();
            osw.close();
            fos.close();


        }
    }
}
