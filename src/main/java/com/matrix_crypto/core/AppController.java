package com.matrix_crypto.core;

import com.matrix_crypto.model.Matrix;
import com.matrix_crypto.service.MatrixCryptService;
import com.matrix_crypto.service.MatrixFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppController {

    @Autowired
    private MatrixFactory<Double> matrixFactory;

    @Autowired
    private MatrixCryptService<Double> matrixCryptService;

    private Scanner input;

    public void start() {
        System.out.println("" +
                ":: Matrix Crypto application ::\n\n" +
                "Choose application mode:\n" +
                "1) Encrypt\n" +
                "2) Decrypt\n"
        );

        System.out.println("Enter your choice and press ENTER");
        input = new Scanner(System.in);

        switch (input.nextInt()) {
            case 1:
                encryptFlow();
                break;
            case 2:
                decryptFlow();
                break;
            default:
                start();
                break;
        }
    }

    private void encryptFlow() {
        int vectorLength;
        String plaintext;

        System.out.println("Please enter the length of the encryption vectors");
        input = new Scanner(System.in);

        //Invalid input restart flow
        if ((vectorLength = input.nextInt()) == 0) {
            encryptFlow();

            return;
        }

        //Create new random square matrix with the dimensions of the vectorLength x vectorLength
        Matrix<Double> matrix = matrixFactory.createInvertableMatrix(vectorLength);

        System.out.println("Please enter plaintext to be encrypted");
        input = new Scanner(System.in);

        //Invalid input restart flow
        if ((plaintext = input.nextLine()).length() == 0) {
            encryptFlow();

            return;
        }

        System.out.println(
                String.format("Plaintext was encrypted with the matrix:\n%s", matrix)
        );

        System.out.println(
                String.format(
                        "Encryption result -> cipher text:\n%s",
                        matrixCryptService.getCipherText(matrix, plaintext)
                )
        );
    }

    private void decryptFlow() {
        int vectorLength;
        String cipherText;
        List<String> matrixVectors = new ArrayList<>();

        System.out.println("Please enter the length of the vectors used in the encryption");
        input = new Scanner(System.in);

        //Invalid input restart flow
        if ((vectorLength = input.nextInt()) == 0) {
            decryptFlow();

            return;
        }

        for (int i = 0; i < vectorLength; i++) {
            System.out.println(
                    String.format(
                            "Please enter the %s row of the matrix which was used in the message encryption(Values should be separated by space character)",
                            i + 1
                    )
            );

            input = new Scanner(System.in);
            matrixVectors.add(input.nextLine());
        }

        //Invalid input restart flow
        if (String.join("", matrixVectors).isEmpty()) {
            decryptFlow();

            return;
        }

        System.out.println("Please enter the cipher text to be decrypted");
        input = new Scanner(System.in);

        //Invalid input restart flow
        if ((cipherText = input.nextLine()).isEmpty()) {
            decryptFlow();

            return;
        }

        try {
            System.out.println(
                    String.format(
                            "Decyrption result:\n%s",
                            matrixCryptService.getPlaintext(
                                    matrixFactory.createFromString(String.join("\n", matrixVectors)),
                                    cipherText
                            )
                    )
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
