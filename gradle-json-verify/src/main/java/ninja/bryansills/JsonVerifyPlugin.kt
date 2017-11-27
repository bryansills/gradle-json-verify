package ninja.bryansills

import org.gradle.api.Plugin
import org.gradle.api.Project

class JsonVerifyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("jsonVerify", JsonVerifyExtension::class.java)
        val jsonVerifyTask = project.tasks.create("jsonVerify", JsonVerifyTask::class.java)

        val tasks = project.getTasksByName("preBuild", true)
        if (tasks.isEmpty()) {
            throw RuntimeException("I cannot find the preBuild task to hook into.")
        } else {
            tasks.forEach { task -> task.dependsOn(jsonVerifyTask) }
        }
    }
}
