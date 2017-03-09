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

        public RegressionData(String regressionFileName, String... commandLineArguments) {
            this.commandLineArguments = commandLineArguments;
            this.regressionFileName = regressionFileName;
        }

        @Override
        public String toString() {
            return "javaan " + StringUtils.join(commandLineArguments, " ");
        }
    }

    private static final String TEST_LIBRARY = "src/test/resources/org/javaan/javaan-cli-2.1.jar";

    @Parameterized.Parameters(name = "{0}")
    public static final RegressionData[] regresseionTestData() {
       return new RegressionData[]{
               new RegressionData("help.out", "--help"),
               new RegressionData("classes.out", "classes", TEST_LIBRARY),
               new RegressionData("used-packages.out", "used-packages", TEST_LIBRARY)
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
        FileUtils.writeStringToFile(new File(regressionData.regressionFileName), outputAsString, "UTF8");
        assertArrayEquals(expectedOutput, output);
    }
}
