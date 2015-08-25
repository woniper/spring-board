package net.woniper.board.config;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by woniper on 15. 8. 25..
 */
@Configuration
@EnableSwagger
public class SwaggerConfig {

    @Bean
    public SwaggerSpringMvcPlugin springSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        return new SwaggerSpringMvcPlugin(springSwaggerConfig)
                .apiInfo(new ApiInfo("board", "spring restful board", null, null, null, null))
                .useDefaultResponseMessages(false)
                .includePatterns("/users/*", "/boards/*")
                .build();
    }
}
