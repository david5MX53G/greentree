package com.greentree.model.services.config;

/*
 * The MIT License
 *
 * Copyright 2018 david5MX53G.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


import com.greentree.model.business.manager.GreenTreeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is the Spring Inversion of Control (IoC) Container {@link 
 * Configuration} class. It uses <a href="https://docs.spring.io/spring/docs/5.1.0.RC2/spring-framework-reference/core.html#beans-java">
 * Java-based container configuration</a>.
 * 
 * @author david5MX53G
 */
@Configuration
public class AppConfig {
    /**
     * This registers, "a bean definition within an ApplicationContext of the 
     * type specified as the method’s return value. By default, the bean name 
     * will be the same as the method name." (Source: <a href="https://docs.spring.io/spring/docs/5.1.0.RC2/spring-framework-reference/core.html#beans-java-bean-annotation">
     * Using the @Bean annotation</a>.
     * 
     * @return the instance of {@link GreenTreeManager}
     */
    @Bean
    public GreenTreeManager GreenTreeManager() {
        return GreenTreeManager.getInstance();
    }
}
