package br.com.valinostech.controller;



public class PagamentoForm {

    // ADICIONE ESTA LINHA: Guardará se a escolha foi "CARTAO", "PIX" ou "BOLETO"

    private String metodoPagamento;



    private Cartao creditCard = new Cartao();

    private InfoTitular creditCardHolderInfo = new InfoTitular();



    // GETTER E SETTER DO METODO DE PAGAMENTO

    public String getMetodoPagamento() { return metodoPagamento; }

    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }



    // Seus Getters e Setters originais mantidos

    public Cartao getCreditCard() { return creditCard; }

    public void setCreditCard(Cartao creditCard) { this.creditCard = creditCard; }



    public InfoTitular getCreditCardHolderInfo() { return creditCardHolderInfo; }

    public void setCreditCardHolderInfo(InfoTitular creditCardHolderInfo) { this.creditCardHolderInfo = creditCardHolderInfo; }



    // Subclasse para os dados do Cartão (Mantida idêntica)

    public static class Cartao {

        private String number;

        private String holderName;

        private String expiryMonth;

        private Integer expiryYear;

        private String ccv;



        public String getNumber() { return number; }

        public void setNumber(String number) { this.number = number; }

        public String getHolderName() { return holderName; }

        public void setHolderName(String holderName) { this.holderName = holderName; }

        public String getExpiryMonth() { return expiryMonth; }

        public void setExpiryMonth(String expiryMonth) { this.expiryMonth = expiryMonth; }

        public Integer getExpiryYear() { return expiryYear; }

        public void setExpiryYear(Integer expiryYear) { this.expiryYear = expiryYear; }

        public String getCcv() { return ccv; }

        public void setCcv(String ccv) { this.ccv = ccv; }

    }



    // Subclasse para a validação do Comprador (Mantida idêntica)

    public static class InfoTitular {

        private String name;



        public String getName() { return name; }

        public void setName(String name) { this.name = name; }

    }

}