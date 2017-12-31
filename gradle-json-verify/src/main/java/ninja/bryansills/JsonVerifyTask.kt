package ninja.bryansills

import com.squareup.moshi.Moshi
import okio.Okio
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import java.io.File

open class JsonVerifyTask : SourceTask() {

    lateinit var output: File

    @TaskAction
    fun action(inputs: IncrementalTaskInputs) {
        inputs.outOfDate { inputFileDetails ->
            if (".json" == getFileExtension(inputFileDetails.file)) {
                verifyJsonFile(inputFileDetails.file)
            }
        }
    }

    private fun verifyJsonFile(file: File) {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(Map::class.java)

        try {
            val jsonMap = jsonAdapter.fromJson(Okio.buffer(Okio.source(file)))
            println("${file.path} is good")
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
