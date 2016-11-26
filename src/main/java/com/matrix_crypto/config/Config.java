package com.matrix_crypto.config;

import com.matrix_crypto.core.AppController;
import com.matrix_crypto.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.matrix_crypto"})
public class Config {

    @Bean(name = "appController")
    public AppController getAppController() {
        return new AppController();
    }

    @Bean(name = "matrixFactory")
    public MatrixFactory<Double> getMatrixFactory() {
        return new NumericMatrixFactory(getMatrixDeterminantService());
    }

    @Bean(name = "matrixDeterminantService")
    public MatrixDeterminantService<Double> getMatrixDeterminantService() {
        return new NumericMatrixDeterminantService();
    }

    @Bean(name = "multiplicationService")
    public MatrixMultiplicationService<Double> getMultiplicationService() {
        return new NumericMatrixMultiplicationService();
    }

    @Bean(name = "matrixInvertService")
    public MatrixInvertService<Double> getMatrixInvertService() {
        return new NumericMatrixInvertService(getMatrixDeterminantService());
    }

    @Bean(name = "matrixCryptService")
    public MatrixCryptService<Double> matrixCryptService() {
        return new NumericMatrixStringCryptService(
                getMultiplicationService(),
                getMatrixInvertService()
        );
    }
}
