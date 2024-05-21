attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec4 a_color;

uniform mat4 u_proj;
uniform mat4 u_trans;
uniform mat3 u_normal;

uniform vec3 u_camPos;

varying vec3 v_color;

struct PointLight {
    vec3 position;
    vec3 color;
};

#define NR_POINT_LIGHTS 2
uniform PointLight pointLights[NR_POINT_LIGHTS];

vec3 calcPointLight(PointLight light){
    vec4 pos = u_trans * vec4(a_position, 1.0);
    vec3 normal = normalize(u_normal * a_normal);
    vec3 lightDir = normalize(light.position - pos.xyz);

    vec3 specular = vec3(0.0);
    vec3 ref = reflect(-lightDir, normal);
    vec3 eye = normalize(u_camPos - pos.xyz);

    float factor = dot(eye, ref);
    if(factor > 0.0) specular = vec3(pow(factor, 40.0)) * (1.0 - a_color.a);

    vec3 diffuse = (light.color + specular) * vec3(0.01 + clamp((dot(normal, lightDir) + 1.0) / 2.0, 0.0, 1.0));
    return diffuse;
}

void main(){
    vec4 pos = u_trans * vec4(a_position, 1.0);

    vec3 result = calcPointLight(pointLights[0]);
    for(int i = 1; i < NR_POINT_LIGHTS; i++){
        result += calcPointLight(pointLights[i]);
    }
    v_color = a_color.rgb * result;
    gl_Position = u_proj * pos;
}