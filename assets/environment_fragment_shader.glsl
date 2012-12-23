precision mediump float;
uniform sampler2D u_sampler;
varying vec2 v_TexCoord;
varying float v_Diffuse;
void main(void) 
{
    gl_FragColor = texture2D(u_sampler, v_TexCoord)*v_Diffuse;
    /*gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);*/
    /*gl_FragColor = v_Color;*/
}
