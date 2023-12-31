package com.example.slangworddictionary;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Dictionary {
    public static String DATA_DIR = "assets/data/slang.txt";
    public static String DATA_RAW = "assets/data/slang_raw.txt";

    public static TreeMap<String, Set<String>> data;

    public static void loadData(String path) throws IOException {
        data = new TreeMap<String, Set<String>>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String line;
            while ((line = br.readLine()) != null) {
                String[] slangDef = line.split("`");

                if (slangDef.length == 2) {
                    String[] definitions = slangDef[1].split("\\|");
                    Set<String> setDef = Arrays.stream(definitions).collect(Collectors.toSet());
                    if (data.containsKey(slangDef[0])) {
                        data.get(slangDef[0]).addAll(setDef);
                    } else {
                        data.put(slangDef[0], setDef);
                    }
                }
            }
            br.close();
        } finally {
//            System.out.println(data);
        }
    }

    public static String getDataDir() {
        return DATA_DIR;
    }

    public static TreeMap<String, Set<String>> getData() {
        return data;
    }
}
