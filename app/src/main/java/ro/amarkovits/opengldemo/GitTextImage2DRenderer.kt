package ro.amarkovits.opengldemo

import android.opengl.GLES20.*
import org.intellij.lang.annotations.Language
import pl.droidsonroids.gif.GifTexImage2D
import java.nio.Buffer

/**
 * This class is responsibile for rendering gifs
 */
class GitTextImage2DRenderer() {

    private val TAG = GitTextImage2DRenderer::class.java.simpleName

    private var program = 0
    private var positionLocation = -1
    private var textureLocation = -1
    private var coordinateLocation = -1
    private var uMatrixLocation = -1

    @Language("GLSL")
    private val vertexShaderCode = """
        uniform mat4 u_matrix;

        attribute vec4 position;
        attribute vec4 coordinate;

        varying vec2 textureCoordinate;

        void main() {
            gl_Position = u_matrix * position;
            textureCoordinate = vec2(coordinate.s, 1.0 - coordinate.t);
        }
        """

    @Language("GLSL")
    private val fragmentShaderCode = """
        varying mediump vec2 textureCoordinate;
        uniform sampler2D texture;
        void main() {
             gl_FragColor = texture2D(texture, textureCoordinate);
        }
        """

    private val textureBuffer = floatArrayOf(0f, 0f, 1f, 0f, 0f, 1f, 1f, 1f).toFloatBuffer()

    fun initialize() {

        //shaders and program
        val vertexShader = loadShader(GL_VERTEX_SHADER, vertexShaderCode)
        val pixelShader = loadShader(GL_FRAGMENT_SHADER, fragmentShaderCode)
        program = glCreateProgram()
        glAttachShader(program, vertexShader)
        glAttachShader(program, pixelShader)
        glLinkProgram(program)
        glDeleteShader(pixelShader)
        glDeleteShader(vertexShader)

        //locations
        positionLocation = glGetAttribLocation(program, "position")
        textureLocation = glGetUniformLocation(program, "texture")
        coordinateLocation = glGetAttribLocation(program, "coordinate")
        uMatrixLocation = glGetUniformLocation(program, "u_matrix")
    }


    fun draw(gifTexImage2D: GifTexImage2D, texName: Int, projectionMatrix: FloatArray, vertices: Buffer) {
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texName)
        glUseProgram(program)
        glUniform1i(textureLocation, 0)
        glEnableVertexAttribArray(coordinateLocation)
        glVertexAttribPointer(coordinateLocation, 2, GL_FLOAT, false, 0, textureBuffer)
        glEnableVertexAttribArray(positionLocation)
        glVertexAttribPointer(positionLocation, 2, GL_FLOAT, false, 0, vertices)
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)
        gifTexImage2D.glTexSubImage2D(GL_TEXTURE_2D, 0)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
    }
}

