package br.com.valinostech.dto;

import java.math.BigDecimal;

public class CieloPagamentoRequestDTO {

    private BigDecimal value;
    private CreditCardDTO creditCard = new CreditCardDTO();
    private CreditCardHolderInfoDTO creditCardHolderInfo = new CreditCardHolderInfoDTO();

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public CreditCardDTO getCreditCard() { return creditCard; }
    public void setCreditCard(CreditCardDTO creditCard) { this.creditCard = creditCard; }
    public CreditCardHolderInfoDTO getCreditCardHolderInfo() { return creditCardHolderInfo; }
    public void setCreditCardHolderInfo(CreditCardHolderInfoDTO creditCardHolderInfo) { this.creditCardHolderInfo = creditCardHolderInfo; }

    public static class CreditCardDTO {
        private String number;
        private String holderName;
        private String expiryMonth;
        private String expiryYear;
        private String ccv;

        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        public String getHolderName() { return holderName; }
        public void setHolderName(String holderName) { this.holderName = holderName; }
        public String getExpiryMonth() { return expiryMonth; }
        public void setExpiryMonth(String expiryMonth) { this.expiryMonth = expiryMonth; }
        public String getExpiryYear() { return expiryYear; }
        public void setExpiryYear(String expiryYear) { this.expiryYear = expiryYear; }
        public String getCcv() { return ccv; }
        public void setCcv(String ccv) { this.ccv = ccv; }
    }

    public static class CreditCardHolderInfoDTO {
        private String name;
        private String cpfCnpj;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCpfCnpj() { return cpfCnpj; }
        public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
    }
}