package com.example.demo.model;

public class BSFAccountResponse {
        private String accountNumber;
        private String iban;
        private String englishName;
        private String arabicName;
        private String bank;
        private String countryName;

        // Getters and Setters

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getIban() {
            return iban;
        }

        public void setIban(String iban) {
            this.iban = iban;
        }

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public String getArabicName() {
            return arabicName;
        }

        public void setArabicName(String arabicName) {
            this.arabicName = arabicName;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }
}

