package com.cedricziel.idea.concourse.psi

import com.cedricziel.idea.concourse.BaseConcoursePluginTest
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.testFramework.UsefulTestCase
import com.intellij.util.containers.ContainerUtil
import junit.framework.TestCase
import org.jetbrains.yaml.psi.YAMLScalar

class ResourceReferenceContributorTest : BaseConcoursePluginTest() {
    fun testCanResolve() {
        doTest("pipeline.yml", "booklit")
    }

    private fun doTest(filePath: String, expectedResolve: String) {
        myFixture.apply {
            val file = copyFileToProject(filePath, "pipeline.yml")
            configureFromExistingVirtualFile(file)

            val reference = getReferenceAtCaretPosition() as PsiPolyVariantReference?
            TestCase.assertNotNull(reference)

            val resolveResults = reference?.multiResolve(true)
            ContainerUtil.map(resolveResults) {
                it?.element.let { element ->
                    TestCase.assertNotNull(element)
                    UsefulTestCase.assertInstanceOf(element, YAMLScalar::class.java)
                    UsefulTestCase.assertEquals(expectedResolve, element?.text)
                }
            }
        }
    }
}
