attribute vec4 a_VertexPos;
attribute vec2 a_TexCoord;
attribute vec4 a_Color;
attribute vec3 a_Normal;

uniform vec3 u_LightPos;
uniform mat4 u_MVPMatrix;
uniform mat4 u_MVMatrix;

varying vec2 v_TexCoord;
varying float v_Diffuse;
varying vec4 v_Color;

void main(void) 
{
    vec3 modelViewVertex = vec3(u_MVMatrix*a_VertexPos);
    vec3 modelViewNormal = vec3(u_MVMatrix*vec4(a_Normal, 0));
    vec3 lightVector = normalize(u_LightPos - modelViewVertex);
    float distant = length(u_LightPos - modelViewVertex);
    float diffuse = max(dot(modelViewNormal, lightVector), 0.1);
    /*diffuse = diffuse * (1.0 / (1.0 + (0.3 * distant)));*/
    /*v_Color = a_Color * diffuse;*/
    v_Color = vec4(0.0, 1.0, 0.0, 1.0) * diffuse;
    /*v_Color = vec4(0.0, 1.0, 0.0, 1.0);*/

    gl_Position = u_MVPMatrix*a_VertexPos;
    v_TexCoord = a_TexCoord;
}
