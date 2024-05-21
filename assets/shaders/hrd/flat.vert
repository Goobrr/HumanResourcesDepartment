attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec4 a_color;

uniform mat4 u_proj;
uniform mat4 u_trans;
uniform mat3 u_normal;

uniform vec3 u_color;
uniform vec3 u_camPos;

varying vec3 v_color;

void main(){
    vec4 pos = u_trans * vec4(a_position, 1.0);

    v_color = u_color;
    gl_Position = u_proj * pos;
}