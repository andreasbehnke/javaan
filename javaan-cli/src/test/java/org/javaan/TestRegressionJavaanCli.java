package org.javaan;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
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
    }

    private static final String TEST_LIBRARY = "src/test/resources/org/javaan/javaan-cli-2.1.jar";

    @Parameterized.Parameters
    public static final RegressionData[] regresseionTestData() {
       return new RegressionData[]{
          new RegressionData("classes.out", "classes", TEST_LIBRARY)
       };
    }

    private final List<String> expectedOutput;

    private final String[] commandLineArguments;

    public TestRegressionJavaanCli(RegressionData regressionData) {
        try {
            this.expectedOutput = IOUtils.readLines(TestRegressionJavaanCli.class.getResourceAsStream(regressionData.regressionFileName), "UTF8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
        this.commandLineArguments = regressionData.commandLineArguments;
    }

    @Test
    public void testCli() {
        new JavaanCli(commandLineArguments, JavaanCli.getCommands()).execute(); //TODO: inject writer and compare output after refactoring
    }
}
