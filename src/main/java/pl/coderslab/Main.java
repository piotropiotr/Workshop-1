package pl.coderslab;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String[][] tasks;
    static String sciezka = "/home/piotr/Workshop-1/program1/src/main/java/pl/coderslab/tasks.csv";

    public static void main(String[] args) {
        wczytaniePliku(sciezka);
        Scanner inputScanner = new Scanner(System.in);
        String input;

        do {
            displayOptions();
            input = inputScanner.nextLine();
            switch (input) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask();
                    break;
                case "list":
                    listTasks();
                    break;
                case "exit":
                    System.out.println("Zamykanie programu.");
                    saveTasksToFile(); // Zapisz dane do pliku przed zamknięciem
                    break;
                default:
                    System.out.println("Proszę wybrać poprawną opcję.");
            }
        } while (!input.equals("exit"));
    }

    public static void addTask() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("Proszę podać opis zadania:");
        String description = inputScanner.nextLine();

        System.out.println("Proszę podać termin zadania (np. 2020-11-29):");
        String dueDate = inputScanner.nextLine();

        System.out.println("Czy zadanie jest ważne? true/false:");
        boolean isImportant = inputScanner.nextBoolean();

        String[][] noweZadania = Arrays.copyOf(tasks, tasks.length + 1);

        noweZadania[noweZadania.length - 1] = new String[]{description, dueDate, String.valueOf(isImportant)};

        tasks = noweZadania;

        System.out.println("Zadanie dodane:");
        System.out.println("Opis: " + description);
        System.out.println("Termin: " + dueDate);
        System.out.println("Ważne: " + isImportant);
    }

    public static void removeTask() {
        if (tasks.length == 0) {
            System.out.println("Brak zadań do usunięcia.");
            return;
        }

        Scanner inputScanner = new Scanner(System.in);

        int indeksZadania;
        while (true) {
            try {
                System.out.print("Podaj numer zadania do usunięcia: ");
                String input = inputScanner.nextLine();
                indeksZadania = Integer.parseInt(input);

                if (indeksZadania < 0 || indeksZadania >= tasks.length) {
                    throw new IndexOutOfBoundsException();
                }

                break;

            } catch (NumberFormatException e) {
                System.out.println("Niepoprawny numer. Proszę podać liczbę.");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Niepoprawny numer. Proszę podać liczbę z zakresu 0-" + (tasks.length - 1));
            }
        }

        tasks = removeTaskAtIndex(tasks, indeksZadania);

        System.out.println("Zadanie o numerze " + indeksZadania + " zostało usunięte.");
    }

    private static String[][] removeTaskAtIndex(String[][] tablica, int indeks) {
        String[][] nowaTablica = new String[tablica.length - 1][3];
        int nowyIndeksTablicy = 0;

        for (int i = 0; i < tablica.length; i++) {
            if (i != indeks) {
                nowaTablica[nowyIndeksTablicy++] = tablica[i];
            }
        }

        return nowaTablica;
    }

    private static void saveTasksToFile() {
        try {
            List<String> listaDanych = new ArrayList<>();

            for (String[] zadanie : tasks) {
                String linia = String.join(",", zadanie);
                listaDanych.add(linia);
            }

            Files.write(new File(sciezka).toPath(), listaDanych);
        } catch (Exception e) {
            System.err.println("Nie można zapisać pliku: " + e.getMessage());
        }
    }

    public static void listTasks() {
        System.out.println("Lista zadań:");
        if (tasks.length == 0) {
            System.out.println("Brak zadań.");
        } else {
            for (int i = 0; i < tasks.length; i++) {
                System.out.println(i + " : " + String.join(" ", tasks[i]));
            }
        }
    }

    public static void displayOptions() {
        String[] opcje = {"add", "remove", "list", "exit"};
        System.out.println(ConsoleColors.BLUE + "Proszę wybrać opcję:");
        for (String opcja : opcje) {
            System.out.println(ConsoleColors.RESET + opcja);
        }
    }

    public static void wczytaniePliku(String nazwaPliku) {
        try {
            Scanner skaner = new Scanner(new File(nazwaPliku));

            int liczbaWierszy = 0;
            while (skaner.hasNextLine()) {
                String linia = skaner.nextLine();
                if (!linia.isEmpty()) {
                    liczbaWierszy++;
                }
            }

            tasks = new String[liczbaWierszy][3];

            skaner = new Scanner(new File(nazwaPliku));

            int wiersz = 0;
            while (skaner.hasNextLine()) {
                String linia = skaner.nextLine();
                if (!linia.isEmpty()) {
                    String[] elementy = linia.split(",");
                    tasks[wiersz] = elementy;
                    wiersz++;
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Nie znaleziono pliku: " + e.getMessage());
        }
    }
}
