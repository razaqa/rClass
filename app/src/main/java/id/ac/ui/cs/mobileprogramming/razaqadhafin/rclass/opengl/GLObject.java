package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES30;
import android.util.Log;

public class GLObject {
    private int mProgramObject;
    private int mMVPMatrixHandle;
    private int mColorHandle;
    private FloatBuffer mVertices;

    float colorDarkBlue[] = GLColor.darkBlue();
    float colorblue[] = GLColor.blue();

    //vertex shader code
    String vShaderStr =
            "#version 300 es 			  \n"
                    + "uniform mat4 uMVPMatrix;     \n"
                    + "in vec4 vPosition;           \n"
                    + "void main()                  \n"
                    + "{                            \n"
                    + "   gl_Position = uMVPMatrix * vPosition;  \n"
                    + "}                            \n";
    //fragment shader code.
    String fShaderStr =
            "#version 300 es		 			          	\n"
                    + "precision mediump float;					  	\n"
                    + "uniform vec4 vColor;	 			 		  	\n"
                    + "out vec4 fragColor;	 			 		  	\n"
                    + "void main()                                  \n"
                    + "{                                            \n"
                    + "  fragColor = vColor;                    	\n"
                    + "}                                            \n";

    String TAG = "Cube";

    float size0 = 0.25f;
    float sizeXS = 0.15f;
    float sizeXSM = 0.1f;
    float sizeS = 0.05f;
    float sizeM = -0.05f;
    float sizeL = -0.15f;
    float sizeXL = -0.25f;

    float[] mVerticesData = new float[]{
            // left column front
            size0,   size0,  size0,
            size0, sizeXL,  size0,
            sizeXS,   size0,  size0,
            size0, sizeXL,  size0,
            sizeXS, sizeXL,  size0,
            sizeXS,   size0,  size0,

            // top rung front
            sizeXS,   size0,  size0,
            sizeXS,  sizeXS,  size0,
            sizeM,   size0,  size0,
            sizeXS,  sizeXS,  size0,
            sizeM,  sizeXS,  size0,
            sizeM,   size0,  size0,

            // middle rung front
            sizeXSM,  sizeS,  size0,
            sizeXSM,  sizeM,  size0,
            sizeM,  sizeS,  size0,
            sizeXSM,  sizeM,  size0,
            sizeM,  sizeM,  size0,
            sizeM,  sizeS,  size0,

            // left column back
            size0,   size0,  sizeXS,
            sizeXS,   size0,  sizeXS,
            size0, sizeXL,  sizeXS,
            size0, sizeXL,  sizeXS,
            sizeXS,   size0,  sizeXS,
            sizeXS, sizeXL,  sizeXS,

            // top rung back
            sizeXS,   size0,  sizeXS,
            sizeM,   size0,  sizeXS,
            sizeXS,  sizeXS,  sizeXS,
            sizeXS,  sizeXS,  sizeXS,
            sizeM,   size0,  sizeXS,
            sizeM,  sizeXS,  sizeXS,

            // middle rung back
            sizeXSM,  sizeS,  sizeXS,
            sizeM,  sizeS,  sizeXS,
            sizeXSM,  sizeM,  sizeXS,
            sizeXSM,  sizeM,  sizeXS,
            sizeM,  sizeS,  sizeXS,
            sizeM,  sizeM,  sizeXS,

            // top
            size0,   size0,   size0,
            sizeL,   size0,   size0,
            sizeL,   size0,  sizeXS,
            size0,   size0,   size0,
            sizeL,   size0,  sizeXS,
            size0,   size0,  sizeXS,

            // top rung right
            sizeM,   size0,   size0,
            sizeM,  sizeXS,   size0,
            sizeM,  sizeXS,  sizeXS,
            sizeM,   size0,   size0,
            sizeM,  sizeXS,  sizeXS,
            sizeM,   size0,  sizeXS,

            // under top rung
            sizeXS,   sizeXS,   size0,
            sizeXS,   sizeXS,  sizeXS,
            sizeM,  sizeXS,  sizeXS,
            sizeXS,   sizeXS,   size0,
            sizeM,  sizeXS,  sizeXS,
            sizeM,  sizeXS,   size0,

            // between top rung and middle
            sizeXS,   sizeXS,   size0,
            sizeXS,   sizeM,  sizeXS,
            sizeXS,   sizeXS,  sizeXS,
            sizeXS,   sizeXS,   size0,
            sizeXS,   sizeM,   size0,
            sizeXS,   sizeM,  sizeXS,

            // left side middle rung
            sizeXSM,   sizeS,   size0,
            sizeXSM,   sizeS,  sizeXS,
            sizeXSM, sizeM,  sizeXS,
            sizeXSM,   sizeS,   size0,
            sizeXSM, sizeM,  sizeXS,
            sizeXSM, sizeM,   size0,

            // top of middle rung
            sizeXSM,   sizeS,   size0,
            sizeM,   sizeS,  sizeXS,
            sizeXSM,   sizeS,  sizeXS,
            sizeXSM,   sizeS,   size0,
            sizeM,   sizeS,   size0,
            sizeM,   sizeS,  sizeXS,

            // right of middle rung
            sizeS,   sizeS,   size0,
            sizeS,   sizeM,  sizeXS,
            sizeS,   sizeS,  sizeXS,
            sizeS,   sizeS,   size0,
            sizeS,   sizeM,   size0,
            sizeS,   sizeM,  sizeXS,

            // bottom of middle rung.
            sizeXSM,   sizeM,   size0,
            sizeXSM,   sizeM,  sizeXS,
            sizeL,   sizeM,  sizeXS,
            sizeXSM,   sizeM,   size0,
            sizeL,   sizeM,  sizeXS,
            sizeL,   sizeM,   size0,

            // right of bottom
            sizeXS,   sizeM,   size0,
            sizeXS,  sizeXL,  sizeXS,
            sizeXS,   sizeM,  sizeXS,
            sizeXS,   sizeM,   size0,
            sizeXS,  sizeXL,   size0,
            sizeXS,  sizeXL,  sizeXS,

            // bottom
            size0,   sizeXL,   size0,
            size0,   sizeXL,  sizeXS,
            sizeXS,  sizeXL,  sizeXS,
            size0,   sizeXL,   size0,
            sizeXS,  sizeXL,  sizeXS,
            sizeXS,  sizeXL,   size0,

            // left side
            size0,   size0,   size0,
            size0,   size0,  sizeXS,
            size0, sizeXL,  sizeXS,
            size0,   size0,   size0,
            size0, sizeXL,  sizeXS,
            size0, sizeXL,   size0,

            // right column front top
            sizeM,   size0,  size0,
            sizeM, sizeM,  size0,
            sizeL,   size0,  size0,
            sizeM, sizeM,  size0,
            sizeL, sizeM,  size0,
            sizeL,   size0,  size0,

            // right column back top
            sizeM,   size0,  sizeXS,
            sizeL,   size0,  sizeXS,
            sizeM, sizeM,  sizeXS,
            sizeM, sizeM,  sizeXS,
            sizeL,   size0,  sizeXS,
            sizeL, sizeM,  sizeXS,

            // right side top
            sizeL,   size0,   size0,
            sizeL,   size0,  sizeXS,
            sizeL, sizeM,  sizeXS,
            sizeL,   size0,   size0,
            sizeL, sizeM,  sizeXS,
            sizeL, sizeM,   size0,

            // between top rung and middle top
            sizeM,   sizeXS,   size0,
            sizeM,   sizeM,  sizeXS,
            sizeM,   sizeXS,  sizeXS,
            sizeM,   sizeXS,   size0,
            sizeM,   sizeM,   size0,
            sizeM,   sizeM,  sizeXS,

            // right column front bottom
            sizeS,   sizeM,  size0,
            sizeS, sizeXL,  size0,
            sizeM,   sizeM,  size0,
            sizeS, sizeXL,  size0,
            sizeM, sizeXL,  size0,
            sizeM,   sizeM,  size0,

            // right column back bottom
            sizeS,   sizeM,  sizeXS,
            sizeM,   sizeM,  sizeXS,
            sizeS, sizeXL,  sizeXS,
            sizeS, sizeXL,  sizeXS,
            sizeM,   sizeM,  sizeXS,
            sizeM, sizeXL,  sizeXS,

            // right side bottom
            sizeM,   sizeM,   size0,
            sizeM,   sizeM,  sizeXS,
            sizeM, sizeXL,  sizeXS,
            sizeM,   sizeM,   size0,
            sizeM, sizeXL,  sizeXS,
            sizeM, sizeXL,   size0,

            // between top rung and middle bottom
            sizeS,   sizeM,   size0,
            sizeS,   sizeM,  sizeXS,
            sizeS, sizeXL,  sizeXS,
            sizeS,   sizeM,   size0,
            sizeS, sizeXL,  sizeXS,
            sizeS, sizeXL,   size0,

            // right bottom
            sizeS,   sizeXL,   size0,
            sizeS,   sizeXL,  sizeXS,
            sizeM,  sizeXL,  sizeXS,
            sizeS,   sizeXL,   size0,
            sizeM,  sizeXL,  sizeXS,
            sizeM,  sizeXL,   size0,

            // bottom rung front
            sizeM,   sizeL,  size0,
            sizeM,  sizeXL,  size0,
            sizeL,   sizeL,  size0,
            sizeM,  sizeXL,  size0,
            sizeL,  sizeXL,  size0,
            sizeL,   sizeL,  size0,

            // bottom rung back
            sizeM,   sizeL,  sizeXS,
            sizeL,   sizeL,  sizeXS,
            sizeM,  sizeXL,  sizeXS,
            sizeM,  sizeXL,  sizeXS,
            sizeL,   sizeL,  sizeXS,
            sizeL,  sizeXL,  sizeXS,

            // top of bottom rung
            sizeM,   sizeL,   size0,
            sizeL,   sizeL,   size0,
            sizeL,   sizeL,  sizeXS,
            sizeM,   sizeL,   size0,
            sizeL,   sizeL,  sizeXS,
            sizeM,   sizeL,  sizeXS,

            // bottom rung right
            sizeL,   sizeL,   size0,
            sizeL,  sizeXL,   size0,
            sizeL,  sizeXL,  sizeXS,
            sizeL,   sizeL,   size0,
            sizeL,  sizeXL,  sizeXS,
            sizeL,   sizeL,  sizeXS,

            // under bottom rung
            sizeM,   sizeXL,   size0,
            sizeM,   sizeXL,  sizeXS,
            sizeL,  sizeXL,  sizeXS,
            sizeM,   sizeXL,   size0,
            sizeL,  sizeXL,  sizeXS,
            sizeL,  sizeXL,   size0,
    };

    public GLObject() {
        mVertices = ByteBuffer
                .allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mVerticesData);
        mVertices.position(0);

        int vertexShader;
        int fragmentShader;
        int programObject;
        int[] linked = new int[1];

        vertexShader = GLRender.LoadShader(GLES30.GL_VERTEX_SHADER, vShaderStr);
        fragmentShader = GLRender.LoadShader(GLES30.GL_FRAGMENT_SHADER, fShaderStr);

        programObject = GLES30.glCreateProgram();

        if (programObject == 0) {
            Log.e(TAG, "So some kind of error, but what?");
            return;
        }

        GLES30.glAttachShader(programObject, vertexShader);
        GLES30.glAttachShader(programObject, fragmentShader);

        GLES30.glBindAttribLocation(programObject, 0, "vPosition");
        GLES30.glLinkProgram(programObject);
        GLES30.glGetProgramiv(programObject, GLES30.GL_LINK_STATUS, linked, 0);

        if (linked[0] == 0) {
            Log.e(TAG, "Error linking program:");
            Log.e(TAG, GLES30.glGetProgramInfoLog(programObject));
            GLES30.glDeleteProgram(programObject);
            return;
        }

        mProgramObject = programObject;
    }

    public void draw(float[] mvpMatrix) {
        GLES30.glUseProgram(mProgramObject);

        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgramObject, "uMVPMatrix");
        GLRender.checkGlError("glGetUniformLocation");

        mColorHandle = GLES30.glGetUniformLocation(mProgramObject, "vColor");

        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLRender.checkGlError("glUniformMatrix4fv");

        int VERTEX_POS_INDX = 0;
        mVertices.position(VERTEX_POS_INDX);

        GLES30.glVertexAttribPointer(VERTEX_POS_INDX, 3, GLES30.GL_FLOAT,
                false, 0, mVertices);
        GLES30.glEnableVertexAttribArray(VERTEX_POS_INDX);

        int startPos = 0;
        int verticesPerface = 6;

        for (int i = 0; i < 31; i++) {
            if (i < 21) {
                GLES30.glUniform4fv(mColorHandle, 1, colorDarkBlue, 0);
            } else {
                GLES30.glUniform4fv(mColorHandle, 1, colorblue, 0);
            }
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, startPos, verticesPerface);
            startPos += verticesPerface;
        }
    }
}
