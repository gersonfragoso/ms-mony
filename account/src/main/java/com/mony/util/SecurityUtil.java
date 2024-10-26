package com.mony.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Classe utilitária para gerar códigos seguros usados em processos de autenticação e verificação.
 */
public class SecurityUtil {

    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final int SIZE_OTP = 20;

    /**
     * Gera um código de Senha Única (OTP) composto por caracteres alfanuméricos.
     * O código OTP é dividido em segmentos separados por hífens para melhor legibilidade.
     *
     * @return Um código OTP formatado como uma String.
     */
    public static String gerarSenhaUnica() {
        SecureRandom geradorSeguro = new SecureRandom();
        StringBuilder construtorOtp = new StringBuilder(SIZE_OTP);

        for (int i = 0; i < SIZE_OTP; i++) {
            int indice = geradorSeguro.nextInt(CHARACTERS.length());
            char caractere = CHARACTERS.charAt(indice);
            construtorOtp.append(caractere);
            // Insere um hífen a cada 4 caracteres, exceto no final
            if (i % 4 == 3 && i < SIZE_OTP - 1) {
                construtorOtp.append('-');
            }
        }
        return construtorOtp.toString();
    }

    /**
     * Gera um código de verificação de 6 dígitos, útil para cenários que requerem confirmação.
     * O código de verificação é preenchido com zeros à esquerda para garantir que tenha sempre 6 dígitos.
     *
     * @return Um código de verificação de 6 dígitos como uma String.
     */
    public static String generateOtpCode() {
        int numeroAleatorio = new Random().nextInt(999999);
        return String.format("%06d", numeroAleatorio);
    }
}
