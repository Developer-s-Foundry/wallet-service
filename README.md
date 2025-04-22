# Wallet Service for DF foundry project

To run the project ensure you have [Java](https://www.java.com/en/download/help/download_options.html) 17 and [Maven](https://maven.apache.org/install.html) installed on your machine.

- `cd` into the project directory and run `mvn spring-boot:run` 

The project runs on port 8080, therefore your base-url would be http://localhost:8080

## Endpoints

1. create wallet
    `POST baseUrl/api/v1/wallet/create-wallet`
   - payload = `{
       "walletName": "Josh wallet2",
       "walletTag": "123Tag",
       "currency": "NGN",
       "userId": "3",
       "dailyTransactionLimit": "5000",
       "transactionPin":"1234"
   }`


2. Enable wallet `PUT baseUrl/api/v1/wallet/enable/{id}`
   - payload = `{
     "walletName": "Josh wallet",
     "walletTag": "123Tag",
     "currency": "NGN",
     "userId": "4321",
     "dailyTransactionLimit": "5000",
     "transactionPin":"1234"
     }`


3. Get wallet balance `GET baseUrl/api/v1/wallet/balance/{id}`


4. Withdraw from wallet `POST baseUrl/api/v1/wallet/withdraw/{id}`
   - payload = `{
     "amount": "10",
     "transactionPin":"1235"
     }`


5. Wallet transfer `POST baseUrl/api/v1/wallet/transfer`
   - payload = `{
       "sourceWalletId":"12345",
       "destinationWalletId": "54321",
       "amount": "500",
       "pin": "1234"
   }`


6. View transactions performed on a wallet `GET baseUrl/api/v1/wallet/get-wallet-transactions/{id}`


7. Disable wallet `PUT baseUrl/api/v1/wallet/disable/{id}`


8. Limit wallet amount `PUT baseUrl/api/v1/wallet/limit/{id}?limit=1000`


9. Remove wallet limit amount `PUT baseUrl/api/v1/wallet/limit/remove/{id}`


10. Fund wallet `POST baseUrl/api/v1/wallet/fund`
    - payload = `{ "walletId": "123", "amount": "1000"}`
