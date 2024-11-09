package com.example;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import java.util.concurrent.TimeUnit;

public class TemperatureMonitoring {

    public static void main(String[] args) throws InterruptedException {
        // Simulación de temperatura en 10 ciudades de Colombia
        Observable<String> cityTemperatures = Observable.just(
                "Bogotá: 16",
                "Medellín: 22",
                "Cali: 28",
                "Barranquilla: 30",
                "Cartagena: 32",
                "Bucaramanga: 18",
                "Pereira: 20",
                "Cúcuta: 19",
                "Santa Marta: 34",
                "Manizales: 17"
        );

        //1. `map` - Transformamos la cadena de texto para obtener solo la temperatura
        cityTemperatures
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String cityTemperature) {
                        // Extraemos solo la temperatura
                        String[] parts = cityTemperature.split(": ");
                        return parts[1] + "°C";
                    }
                })
                .subscribe(System.out::println);

        //2. `filter` - Filtramos solo las temperaturas mayores de 20°C
        System.out.println("\nTemperaturas mayores de 20°C:");
        cityTemperatures
                .filter(cityTemperature -> {
                    String[] parts = cityTemperature.split(": ");
                    return Integer.parseInt(parts[1]) > 20;
                })
                .map(cityTemperature -> cityTemperature.split(": ")[0] + ": " + cityTemperature.split(": ")[1] + "°C")
                .subscribe(System.out::println);

        //3. `flatMap` - Simulamos que cada ciudad tiene múltiples registros por hora de temperatura
        System.out.println("\nTemperaturas por hora (con flatMap):");
        cityTemperatures
                .flatMap(cityTemperature -> Observable.just(
                        cityTemperature + " - 9 AM",
                        cityTemperature + " - 12 PM",
                        cityTemperature + " - 3 PM"))
                .subscribe(System.out::println);

        //4. `merge` - Combinamos flujos de datos (simulando datos de temperatura de dos fuentes)
        System.out.println("\nCombinando datos de dos flujos (merge):");
        Observable<String> cityTemperatureSource1 = Observable.just("Bogotá: 16", "Medellín: 22");
        Observable<String> cityTemperatureSource2 = Observable.just("Cali: 28", "Barranquilla: 30");

        Observable.merge(cityTemperatureSource1, cityTemperatureSource2)
                .subscribe(System.out::println);

        //5. `zip` - Combinamos temperaturas con tiempos específicos (simulación de tiempo real)
        System.out.println("\nCombinando temperaturas con tiempos (zip):");
        Observable<String> temperatures = Observable.just("16", "22", "28", "30", "32", "18", "20", "19", "34", "17");
        Observable<String> times = Observable.just("9 AM", "12 PM", "3 PM", "6 PM", "9 PM", "12 AM", "3 AM", "6 AM", "9 AM", "12 PM");

        Observable.zip(temperatures, times, (temperature, time) -> time + ": " + temperature + "°C")
                .subscribe(System.out::println);

        //6. `debounce` - Simulamos que las temperaturas se actualizan con un retraso y solo mostramos el último valor después de cierto tiempo de inactividad
        System.out.println("\nTemperaturas con debounce:");
        cityTemperatures
                .debounce(1, TimeUnit.SECONDS) //Si no hay nuevas emisiones en 1 segundo, toma la última
                .subscribe(cityTemperature -> System.out.println("Última temperatura: " + cityTemperature));

        //spera para que las emisiones ocurran antes de que el programa termine
        TimeUnit.SECONDS.sleep(10);
    }
}
