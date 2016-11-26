package com.matrix_crypto.service;

import com.matrix_crypto.model.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NumericMatrixStringCryptService implements MatrixCryptService<Double> {

    private MatrixMultiplicationService<Double> multiplicationService;
    private MatrixInvertService<Double> invertService;

    public NumericMatrixStringCryptService(
            MatrixMultiplicationService<Double> multiplicationService,
            MatrixInvertService<Double> invertService
    ) {
        this.multiplicationService = multiplicationService;
        this.invertService = invertService;
    }

    public String getPlaintext(Matrix<Double> salt, String cipherText) {
        if (salt.getWidth() != salt.getHeight()) {
            throw new IllegalArgumentException("'salt' matrix argument must be square matrix");
        }

        if (!salt.isFull()) {
            throw new IllegalArgumentException("All slots from given 'salt' must have assigned value");
        }

        //Collect cipher text vectors from the ciphertext
        ArrayList<Matrix<Double>> cipherVectors = getCipherVectors(salt.getHeight(), cipherText);

        //Try to find inverse matrix from the given 'salt' matrix
        Matrix<Double> inversedSalt = invertService.invert(salt);

        return cipherVectors.stream()
                .map(s -> convertToPlainText(multiplicationService.multiply(inversedSalt, s)))
                .reduce(
                        "",
                        (a, b) -> a + b
                );
    }

    private String convertToPlainText(Matrix<Double> encryptedVector) {
        StringBuilder outputBuffer = new StringBuilder();

        for (int i = 0; i < encryptedVector.getHeight(); i++) {
            //Skip zero values, as they were used as placeholders, when entire matrix could not be filled
            if (encryptedVector.get(i, 0) == 0) {
                continue;
            }

            outputBuffer.append((char) Math.round(encryptedVector.get(i, 0)));
        }

        return outputBuffer.toString();
    }

    private ArrayList<Matrix<Double>> getCipherVectors(int vectorLength, String cipherText) {
        String[] cipherChunks = cipherText.split("[ ]");
        ArrayList<Matrix<Double>> cipherVectors = new ArrayList<>();
        Matrix<Double> currentVector = createEmptyVector(vectorLength);
        int counter = 0;

        for (String cipherChunk : cipherChunks) {
            if (vectorLength == counter) {
                counter = 0;
                cipherVectors.add(currentVector);
                currentVector = createEmptyVector(vectorLength);
            }

            currentVector.set(counter, 0, Double.valueOf(cipherChunk));
            counter++;
        }

        //Loop did end before the last chunk was added into messageVectors, add the last one too
        cipherVectors.add(currentVector);

        return cipherVectors;
    }

    public String getCipherText(Matrix<Double> salt, String plaintext) {
        if (salt.getWidth() != salt.getHeight()) {
            throw new IllegalArgumentException("'salt' matrix argument must be square matrix");
        }

        if (!salt.isFull()) {
            throw new IllegalArgumentException("All slots from given 'salt' must have assigned value");
        }

        //Divide plaintext into pre defined chunks and convert each characters into ascii
        ArrayList<Matrix<Double>> messageVectors = createMessageVectors(salt.getHeight(), plaintext);

        //Encrypt the plaintext numeric chunks with given "salt" Matrix
        List<String> cipherTextChunks = messageVectors.stream()
                //Transpose the nx1 matrix, so it would create one readable line of string when printing out all the chunks
                .map(s -> multiplicationService.multiply(salt, s).transpose().toString())
                .collect(
                        Collectors.toList()
                );

        return String.join(" ", cipherTextChunks);
    }

    private ArrayList<Matrix<Double>> createMessageVectors(int vectorLength, String plaintext) {
        int counter = 0;
        ArrayList<Matrix<Double>> messageVectors = new ArrayList<>();
        Matrix<Double> currentMessageVector = createEmptyVector(vectorLength);

        //Divide the plaintext string into vector sized chunks
        for (char ch: plaintext.toCharArray()) {
            if (vectorLength == counter) {
                counter = 0;
                messageVectors.add(currentMessageVector);
                currentMessageVector = createEmptyVector(vectorLength);
            }

            currentMessageVector.set(counter, 0, (double) ch);
            counter++;
        }

        //Loop did end before the last chunk was added into messageVectors, add the last one too
        messageVectors.add(currentMessageVector);

        return messageVectors;
    }

    /**
     * Add zeros into the nx1 message vector NULL slots,
     * in order to avoid possible null pointer exception when multiplying the message vector with "salt" matrix
     */
    private Matrix<Double> createEmptyVector(int vectorLength) {
        Matrix<Double> messageVector = new Matrix<>(vectorLength, 1);

        for (int i = 0; i < vectorLength; i++) {
            messageVector.set(i, 0, 0.0);
        }

        return messageVector;
    }
}
