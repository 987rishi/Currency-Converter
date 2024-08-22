package CURRENCY_CONVERTER_USING_EXCHANGE_RATE_API;

import java.net.URL;
// import java.net.http.HttpRequest;
// import java.util.HashMap;
import java.util.Scanner;
import org.json.JSONObject;

// import com.sun.tools.javac.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class currencyConverter {
    String fromCode;
    String toCode;
    Double amount;

    public currencyConverter(String from, String to, Double amt) {
        this.amount = amt;
        this.fromCode = from;
        this.toCode = to;
    }

    @SuppressWarnings("deprecation")
    private Double getRate(String fromCode, String toCode) throws IOException {

        String getUrl = "https://open.er-api.com/v6/latest/" + fromCode;
        // System.out.println(getUrl);

        URL apiUrl = new URL(getUrl);
        HttpURLConnection httpConnection = (HttpURLConnection) apiUrl.openConnection();
        int responseCode = httpConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // System.out.println(" " + response.toString());

            JSONObject json = new JSONObject(response.toString());
            Double exRate = json.getJSONObject("rates").getDouble(toCode);
            // System.out.println(json.getJSONObject("rates"));
            // System.out.println(exRate);
            return exRate;
        } else {
            System.out.println("FAILED TO CONNECT TO URL");
            throw new IOException();
        }
    }

    private Double convert(Double amt, Double convRate) {
        if (convRate == 0.0)
            throw new IllegalArgumentException("RATE CANNOT ME 0");
        return (double) amt * convRate;
    }

    public Double convertedAmount() throws IOException {
        return convert(this.amount, getRate(fromCode, toCode));
    }

    public static void main(String[] args) throws IOException {
        // currencyConverter cc = new currencyConverter();
        System.out.println("Welcome to my currency converter");
        System.out.println("Enter from code. Ex-INR");
        Scanner inp = new Scanner(System.in);
        String fromCode = inp.next();
        fromCode = fromCode.toUpperCase();
        System.out.println("Enter to code. Ex-INR");
        String toCode = inp.next();
        toCode = toCode.toUpperCase();
        System.out.println("Enter amount.");
        Double money = inp.nextDouble();
        currencyConverter cc = new currencyConverter(fromCode, toCode, money);
        System.out.println("Converted amount--" + cc.convertedAmount() + " " + toCode);
        inp.close();

    }
}
