package ro.amarkovits.opengldemo

import android.opengl.GLES20.*
import org.intellij.lang.annotations.Language
import pl.droidsonroids.gif.GifTexImage2D
import java.nio.Buffer

class GitTextImage2DRenderer(private val gifTexImage2D: GifTexImage2D) {

    private val TAG = GitTextImage2DRenderer::class.java.simpleName

    private val U_MATRIX = "u_Matrix"

    val height = gifTexImage2D.height
    val width = gifTexImage2D.width
    val currentFrameDuration = gifTexImage2D.getFrameDuration(gifTexImage2D.currentFrameIndex)

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
    private val verticesArray = floatArrayOf(0f, gifTexImage2D.height.toFloat(), gifTexImage2D.width.toFloat(), gifTexImage2D.height.toFloat(), 0f, 0f, gifTexImage2D.width.toFloat(), 0f)

    fun initialize() {

        //textures
        val texNames = intArrayOf(0)
        glGenTextures(1, texNames, 0)
        glBindTexture(GL_TEXTURE_2D, texNames[0])
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

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


    fun draw(projectionMatrix: FloatArray, vertices: Buffer) {
        glActiveTexture(GL_TEXTURE0)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, gifTexImage2D.width, gifTexImage2D.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null)
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
