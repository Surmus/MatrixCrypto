package com.matrix_crypto.core;

import com.matrix_crypto.config.Config;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);

        AppController controller = (AppController) applicationContext.getBean("appController");

        controller.start();
    }
}
