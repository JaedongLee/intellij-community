package com.intellij.codeInspection

import com.intellij.codeInspection.tests.TestOnlyInspectionTestBase
import com.intellij.jvm.analysis.KotlinJvmAnalysisTestUtil
import com.intellij.testFramework.TestDataPath

private const val inspectionPath = "/codeInspection/testonly"

@TestDataPath("\$CONTENT_ROOT/testData$inspectionPath")
class KotlinTestOnlyInspectionTest : TestOnlyInspectionTestBase() {
  override fun getBasePath() = KotlinJvmAnalysisTestUtil.TEST_DATA_PROJECT_RELATIVE_BASE_PATH + inspectionPath

  override val fileExt: String = "kt"

  fun `test @TestOnly on use-site targets`() = testHighlighting("UseSiteTargets")

  fun `test @TestOnly in production code`() = testHighlighting("TestOnlyTest")

  fun `test @VisibleForTesting in production code`() = testHighlighting("VisibleForTestingTest", "VisibleForTestingTestApi")
}