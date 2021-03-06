package ninja.bryansills

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import java.io.File

class JsonVerifyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("jsonVerify", JsonVerifyExtension::class.java)

        project.plugins.all {
            when (it) {
                is AppPlugin -> configureTask(project, extension,
                        project.extensions.getByType(AppExtension::class.java).applicationVariants)
                is LibraryPlugin -> configureTask(project, extension,
                        project.extensions.getByType(LibraryExtension::class.java).libraryVariants)
            }
        }
    }

    private fun <T : BaseVariant> configureTask(project: Project, extension: JsonVerifyExtension, variants: DomainObjectSet<T>) {
        variants.all { variant ->
            val files = mutableListOf<ConfigurableFileTree>()
            variant.sourceSets.forEach { sourceSet ->
                val sourceSetFilePath = "${project.projectDir.path}/src/${sourceSet.name}/${extension.srcDir}"
                val variantFolder = project.fileTree(sourceSetFilePath)

                files.add(variantFolder)
            }

            val taskName = "jsonVerify${variant.name.capitalize()}"
            val task = project.tasks.create(taskName, JsonVerifyTask::class.java)
            val outputDir = File("${project.buildDir}/generated/source/latte/${variant.dirName}")

            task.apply {
                source = project.files(files).asFileTree
                output = outputDir
                include("*.json")
            }

            variant.registerJavaGeneratingTask(task, outputDir)
        }
    }
}
