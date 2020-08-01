package ru.adelf.idea.dotenv.tests.dotenv;

import ru.adelf.idea.dotenv.indexing.DotEnvKeyValuesIndex;
import ru.adelf.idea.dotenv.tests.DotEnvLightCodeInsightFixtureTestCase;

public class DotEnvFileTest extends DotEnvLightCodeInsightFixtureTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureFromExistingVirtualFile(myFixture.copyFileToProject(".env"));
    }

    protected String getTestDataPath() {
        return basePath + "dotenv/fixtures";
    }

    public void testEnvKeys() {
        assertIndexContains(DotEnvKeyValuesIndex.KEY, "TEST", "TEST2", "TEST3", "EMPTY_KEY", "OFFSET_KEY");
    }

    public void testEnvKeyValues() {
        assertContainsKeyAndValue("TEST", "1");

        assertContainsKeyAndValue("TEST2", "2");
        assertContainsKeyAndValue("TEST3", "3");
        assertContainsKeyAndValue("OFFSET_KEY", "offset");
    }

    public void testEnvCommentedVars() {
        assertContainsKeyAndValue("COMMENTED_VAR", "123");
        assertContainsKeyAndValue("COMMENTED_VAR2", "123 #comment");
        assertContainsKeyAndValue("COMMENTED_VAR3", "123 #com\\\"ment");

        assertContainsKeyAndValue("COMMENTED_VAR4", "1");
    }

    public void testEnvEmptyCommentedVars() {
        assertContainsKeyAndValue("COMMENTED_EMPTY", "");
        assertContainsKeyAndValue("COMMENTED_EMPTY2", "");
    }

    public void testEnvComments() {
        assertIndexNotContains(DotEnvKeyValuesIndex.KEY, "Comment", "#Comment", "#Another comment");
    }

    public void testSlashInTheEndOfQuoted() {
        assertIndexContains(DotEnvKeyValuesIndex.KEY, "SLASH_IN_THE_END_OF_QUOTED", "AFTER");

        assertContainsKeyAndValue("SLASH_IN_THE_END_OF_QUOTED", "123 #com\\\\");
        assertContainsKeyAndValue("AFTER", "1");
    }

    public void testMultiLine() {
        assertContainsKeyAndValue("MULTI_LINE", "MULTI...");
    }

    public void testEnvExportKeys() {
        assertIndexContains(DotEnvKeyValuesIndex.KEY, "EXPORTED");
    }
}
