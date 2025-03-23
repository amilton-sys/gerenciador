package com.sys.gerenciador;

import com.sys.gerenciador.util.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GerenciadorApplication {
    public static void main(String[] args) {
        System.setProperty("SESSION_TIMEOUT", AppConstant.Security.SESSION_TIMEOUT + "s");
        SpringApplication.run(GerenciadorApplication.class, args);
    }
}
