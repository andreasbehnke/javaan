package org.javaan.bytecode;

import org.apache.commons.io.FileUtils;
import org.javaan.print.PrintUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class BenchmarkJarFileLoader {

    private final static String outputPath = "target/benchmark_%s_%s.csv";

    public static void main(String[] args) throws IOException {
        String baseDirectory = args[0];
        try (Writer output = new FileWriter(String.format(outputPath, JarFileLoader.class.getName(), System.nanoTime()))) {
            PrintUtil.println(output, "filename, count, noneParallel, parallel");
            FileUtils.listFiles(new File(baseDirectory), new String[]{"jar"}, true).stream().forEach(file -> {
                runBenchmark(file.getAbsolutePath(), output);
            });
        }
    }

    public static void runBenchmark(String fileName, Writer output) {

    }
}
