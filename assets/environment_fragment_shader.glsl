precision mediump float;
uniform sampler2D u_sampler;
varying vec2 v_TexCoord;
varying float v_Diffuse;
varying vec4 v_Color;
void main(void) 
{
    gl_FragColor = v_Color*v_Diffuse;
}
