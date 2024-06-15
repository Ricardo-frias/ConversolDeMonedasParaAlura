
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;


public class ConversorDeMonedas {

    private static final String llaver = "6fc7e41e42967cd3543d39d6";
    private static final String apiUrl = "https://v6.exchangerate-api.com/v6/" + llaver + "/latest/";

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // aqui le pido la cantidad de monedas
            System.out.print("Ingrese la cantidad de moneda a convertir: ");
            double amount = Double.parseDouble(reader.readLine());

            // aqui le pido saber que tipo de moneda es
            System.out.print("Ingrese la moneda de origen (ej. USD, EUR): ");
            String fromCurrency = reader.readLine().toUpperCase();

            // aqui le pido saber a que tipo de moneda quiere cambiar
            System.out.print("Ingrese la moneda de destino (ej. USD, EUR): ");
            String toCurrency = reader.readLine().toUpperCase();

            // aqui realizamos la convercion y mostramo el resultado
            double convertedAmount = convertCurrency(amount, fromCurrency, toCurrency);
            System.out.printf("%.2f %s equivale a %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);

            reader.close();

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static double convertCurrency(double amount, String fromCurrency, String toCurrency) throws IOException {
        // aqui construimos la URL
        URL url = new URL(apiUrl + fromCurrency);

        // aqui abrimos la conexión HTTP, claro que esta parte fue copiar y pegar y editar :-/
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // aqui leemos la respuesta Json
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String jsonResponse = reader.lines().collect(Collectors.joining());
        reader.close();

        // parseamos la respuesta json
        String ratesJson = jsonResponse.substring(jsonResponse.indexOf("\"conversion_rates\":") + 19, jsonResponse.indexOf("\"time_last_update_utc\":") - 1);
        String rate = ratesJson.substring(ratesJson.indexOf("\"" + toCurrency + "\":") + 5, ratesJson.indexOf(",\"" + toCurrency + "DST\":"));
        double exchangeRate = Double.parseDouble(rate);

        // Calculamos el monto convertido
        double convertedAmount = amount * exchangeRate;

       

        return convertedAmount;

        // si el programa tienen algun error de escritura o se podia hacer de una manera mas practica le ruego que me lo haga saber por que lo que deseo es aprender
    }
}
