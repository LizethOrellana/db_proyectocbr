package com.proyectocbr.demo.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite CORS para todas las rutas
                .allowedOrigins("http://ec2-18-218-153-234.us-east-2.compute.amazonaws.com:8080") // Permite solicitudes desde Angular (ajusta el puerto si es necesario)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permite los métodos HTTP especificados
                .allowedHeaders("*"); // Permite todos los encabezados
    }
}
