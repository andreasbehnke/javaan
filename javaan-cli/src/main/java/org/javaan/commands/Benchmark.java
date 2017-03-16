package org.javaan.commands;

import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.javaan.CommandContext;
import org.javaan.ReturnCodes;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.bytecode.JarFileLoader;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Type;
import org.javaan.print.PrintUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Command for running benchmarks
 */
public class Benchmark  extends BaseCommand {

    private final static String NAME = "benchmark";

    private final static String DESCRIPTION = "run benchmark and print result values as CSV list";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getHelpCommandLine() {
        return "javaan benchmark <repository path>";
    }

    @Override
    public Options buildCommandLineOptions(Options options) {
        return options;
    }

    private void setLoggerLevel(Level level) {
        Logger logger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            handler.setLevel(level);
        }
        logger.setLevel(level);
    }

    @Override
    public ReturnCodes execute(CommandContext commandContext) {
        setLoggerLevel(Level.INFO);
        String baseDirectory = commandContext.getArguments()[0];
        try (Writer output = new OutputStreamWriter(System.out)) {
            LOG.info("Run warmup...");
            new ArrayList<>(FileUtils.listFiles(new File(baseDirectory), new String[]{"jar"}, true))
                    .stream().limit(10)
                    .forEach(file -> runWarmup(file.getAbsolutePath()));
            LOG.info("Processing benchmarks");
            PrintUtil.println(output, "filename, component, count, time");
            FileUtils.listFiles(new File(baseDirectory), new String[]{"jar"}, true)
                    .forEach(file -> runBenchmark(file.getAbsolutePath(), file.getAbsolutePath().replace(baseDirectory,""), output));
        } catch (IOException ioe) {
            return ReturnCodes.errorCommand;
        }
        return ReturnCodes.ok;
    }

    private void runWarmup(String fileName) {
        try {
            List<Type> types =  new JarFileLoader().loadJavaClasses(fileName);
            ClassContext context = new ClassContextBuilder(types).build();
            CallGraph callGraph = new CallGraphBuilder(context, true, true).build();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private void runBenchmark(String fileName, String name, Writer output) {
        try {
            LOG.info("Running benchmark for " + fileName);
            long start = System.nanoTime();
            List<Type> types =  new JarFileLoader().loadJavaClasses(fileName);
            printCsvRow(
                    output,
                    name,
                    "JarFileLoader",
                    types.size(),
                    System.nanoTime() - start
            );

            start = System.nanoTime();
            ClassContext context = new ClassContextBuilder(types).build();
            printCsvRow(
                    output,
                    name,
                    "ClassContextBuilder",
                    types.size(),
                    System.nanoTime() - start
            );

            start = System.nanoTime();
            CallGraph callGraph = new CallGraphBuilder(context, true, true).build();
            printCsvRow(
                    output,
                    name,
                    "CallGraphBuilder",
                    callGraph.size(),
                    System.nanoTime() - start
            );

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private void printCsvRow(Writer output, String fileName, String component, long count, long duration) {
        if (count > 0) {
            PrintUtil.format(output, "%s,%s,%s,%s", fileName, component , count, duration);
        }
    }
}
