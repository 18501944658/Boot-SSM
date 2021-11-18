package com.itszt.controller;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class TestApi {


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Administrator\\Desktop\\detail.csv")));
        BufferedWriter br1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Administrator\\Desktop\\detailnew.csv")));
        String data = null;
        while ((data = br.readLine()) != null) {
            String start = data.substring(0, data.indexOf("|")).replaceAll("\"", "");
            String end = data.substring( data.lastIndexOf("|")+1);
            System.out.println("start = " + start);
            System.out.println("end = " + end);
            String substring = data.substring(data.indexOf("|") + 1, data.lastIndexOf("|"));
            String[] split = substring.split(",");
            List<String> strings = Arrays.asList(split);
            if (!strings.isEmpty()) {
                strings.stream().forEach(m -> {
                    StringBuilder str = new StringBuilder();
                    str.append(start);
                    str.append(m);
                    str.append(end);
                    try {
                        br1.write(str.toString());
                        br1.flush();
                        br1.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }else {
                StringBuilder str = new StringBuilder();
                str.append(start);
                str.append(end);
                try {
                    br1.write(str.toString());
                    br1.flush();
                    br1.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
