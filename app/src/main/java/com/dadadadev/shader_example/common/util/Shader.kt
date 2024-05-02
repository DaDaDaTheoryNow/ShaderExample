package com.dadadadev.shader_example.common.util

import java.io.InputStream
import java.io.FileNotFoundException

object ShaderUtils {
    fun getShaderSource(pathToShader: String): String {
        val resourceStream: InputStream = javaClass.classLoader?.getResourceAsStream(pathToShader)
            ?: throw FileNotFoundException("Resource not found: $pathToShader")

        return resourceStream.bufferedReader().readText()
    }
}
