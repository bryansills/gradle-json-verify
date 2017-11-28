package ninja.bryansills

import com.squareup.moshi.Moshi
import okio.Okio
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class JsonVerifyTask : DefaultTask() {
    @TaskAction
    fun action() {
        val extension = project.extensions.findByName("jsonVerify") as JsonVerifyExtension
        val configFiles = project.fileTree("${project.projectDir.path}/${extension.srcDir}")

        configFiles.forEach { file ->
            if (".json" == getFileExtension(file)) {
                verifyJsonFile(file)
            }
        }
    }

    private fun verifyJsonFile(file: File) {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(Map::class.java)

        try {
            val jsonMap = jsonAdapter.fromJson(Okio.buffer(Okio.source(file)))
        } catch (exception: Exception) {
            throw JsonVerifyException("${exception.message} in file: ${file.path}", exception)
        }

    }

    private fun getFileExtension(file: File): String {
        val name = file.name

        return try {
            name.substring(name.lastIndexOf("."))
        } catch (e: Exception) {
            ""
        }

    }
}
