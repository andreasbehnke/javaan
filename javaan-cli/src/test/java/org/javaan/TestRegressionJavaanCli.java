package org.javaan;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class TestRegressionJavaanCli {

    private static class RegressionData {

        public String[] commandLineArguments;

        public String regressionFileName;

        public boolean sortResult;

        public boolean trimResult;

        public RegressionData(String regressionFileName, boolean sortResult, String... commandLineArguments) {
            this.commandLineArguments = commandLineArguments;
            this.regressionFileName = regressionFileName;
            this.sortResult = sortResult;
        }

        @Override
        public String toString() {
            return "javaan " + StringUtils.join(commandLineArguments, " ");
        }
    }

    private static final String TEST_LIBRARY = "src/test/resources/org/javaan/javaan-cli-2.1.jar";

    private static final String REGRESSION_FILE_PATH = "target/";

    @Parameterized.Parameters(name = "{0}")
    public static final RegressionData[] regresseionTestData() {
        return new RegressionData[]{
                new RegressionData("help.out", false,"--help"),
                new RegressionData("classes.out", false,"classes", TEST_LIBRARY),
                new RegressionData("classes.i.out", false, "classes", "-i", TEST_LIBRARY),
                new RegressionData("classes.s.out", false, "classes", "-s", TEST_LIBRARY),
                new RegressionData("interfaces.spec.out", false, "interfaces", "-spec", TEST_LIBRARY),
                new RegressionData("interfaces.vm.out", false, "interfaces", "-vm", TEST_LIBRARY),
                //new RegressionData("used-packages.out", true, "used-packages", TEST_LIBRARY),
                //new RegressionData("callers.out", true, "callers", TEST_LIBRARY),
                new RegressionData("callers.filter.out", true, "callers", "-method", "org.javaan", TEST_LIBRARY),
                new RegressionData("callees.filter.out", true, "callees", "-method", "org.javaan", TEST_LIBRARY)
        };
    }

    @Parameterized.Parameter()
    public RegressionData regressionData;

    @Test
    public void testCli() throws IOException {
        String[] expectedOutput;
        String[] commandLineArguments;
        try {
            String output = IOUtils.toString(TestRegressionJavaanCli.class.getResourceAsStream(regressionData.regressionFileName), "UTF8");
            expectedOutput = StringUtils.split(output, System.lineSeparator());
            commandLineArguments = regressionData.commandLineArguments;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringWriter writer = new StringWriter();
        new JavaanCli(commandLineArguments, JavaanCli.getCommands(), writer).execute();
        String outputAsString = writer.toString();
        String[] output = StringUtils.split(outputAsString, System.lineSeparator());
        if (regressionData.sortResult) {
            Arrays.sort(output);
            outputAsString = StringUtils.join(output, System.lineSeparator());
        }
        FileUtils.writeStringToFile(new File(REGRESSION_FILE_PATH + regressionData.regressionFileName), outputAsString, "UTF8");
        assertArrayEquals(expectedOutput, output);
    }
}
