attribute vec4 a_VertexPos;
attribute vec2 a_TexCoord;
varying vec2 v_TexCoord;
void main(void) 
{
    gl_Position = a_VertexPos;
     v_TexCoord = a_TexCoord;
}
