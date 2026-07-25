package br.com.valinostech.dto;

public class CartaoCreditoDTO {
    private String holderName;
    private String number;
    private String expiryMonth;
    private String expiryYear;
    private String ccv;

    // Construtor padrão obrigatório para o Spring Boot framework
    public CartaoCreditoDTO() {}

    // Métodos Getters e Setters
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getExpiryMonth() { return expiryMonth; }
    public void setExpiryMonth(String expiryMonth) { this.expiryMonth = expiryMonth; }

    public String getExpiryYear() { return expiryYear; }
    public void setExpiryYear(String expiryYear) { this.expiryYear = expiryYear; }

    public String getCcv() { return ccv; }
    public void setCcv(String ccv) { this.ccv = ccv; }
}
