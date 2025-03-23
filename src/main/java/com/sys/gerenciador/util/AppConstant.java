package com.sys.gerenciador.util;

/**
 * Classe de constantes da aplicação que armazena valores usados em diferentes partes do sistema.
 * Organizada por categorias para facilitar manutenção e localização.
 */
public final class AppConstant {

    /**
     * Constantes relacionadas a segurança e autenticação
     */
    public static final class Security {
        /**
         * Tempo de duração do bloqueio da conta em milissegundos após tentativas falhas (3 segundos para teste)
         * Para produção, recomenda-se um valor maior como 1 hora (3600000 ms)
         */
        public static final long UNLOCK_DURATION_TIME = 3000;

        /**
         * Número máximo de tentativas de login falhas antes de bloquear a conta
         */
        public static final int MAX_FAILED_ATTEMPTS = 3;

        /**
         * Tempo de expiração do token de redefinição de senha em milissegundos (24 horas)
         */
        public static final long RESET_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;

        /**
         * Tempo de vida da sessão em segundos (30 minutos)
         */
        public static final int SESSION_TIMEOUT = 30 * 60;

        private Security() {
            // Construtor privado para prevenir instanciação
        }
    }

    /**
     * Constantes relacionadas a datas e tempo
     */
    public static final class Time {
        /**
         * Número de meses no ano
         */
        public static final int MONTHS_PER_YEAR = 12;

        /**
         * Número de dias em uma semana
         */
        public static final int DAYS_PER_WEEK = 7;

        /**
         * Número de horas em um dia
         */
        public static final int HOURS_PER_DAY = 24;

        private Time() {
            // Construtor privado para prevenir instanciação
        }
    }

    /**
     * Constantes relacionadas a paginação
     */
    public static final class Pagination {
        /**
         * Tamanho padrão da página
         */
        public static final int DEFAULT_PAGE_SIZE = 10;

        /**
         * Número máximo de itens por página
         */
        public static final int MAX_PAGE_SIZE = 100;

        private Pagination() {
            // Construtor privado para prevenir instanciação
        }
    }

    /**
     * Previne a instanciação da classe principal
     */
    private AppConstant() {
        throw new AssertionError("Classe utilitária não deve ser instanciada");
    }
}