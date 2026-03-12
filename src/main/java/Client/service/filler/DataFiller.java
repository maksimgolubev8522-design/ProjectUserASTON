package Client.service.filler;

import Client.model.User;
import Client.service.file.FileReaderService;
import Client.service.file.FileWriterService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Класс для заполнения данных из файла
 * Использует FileReaderService и FileWriterService
 */
public class DataFiller {

    private final FileReaderService fileReader;
    private final FileWriterService fileWriter;
    private final Scanner scanner;

    public DataFiller() {
        this.fileReader = new FileReaderService();
        this.fileWriter = new FileWriterService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Заполнение данных из файла
     *
     * @return List<User> с загруженными пользователями или null при ошибке/выходе
     */
    public List<User> fillFromFile() {
        System.out.print("\nVvedite put k failu dlya chteniya: ");
        String filePath = scanner.nextLine().trim();

        try {
            // Проверяем существование файла
            if (!fileReader.fileExists(filePath)) {
                System.out.println("Fail ne nayden: " + fileReader.getAbsolutePath(filePath));
                return null;
            }

            // Проверяем доступность для чтения
            if (!fileReader.isReadable(filePath)) {
                System.out.println("Fail nedostupen dlya chteniya: " + filePath);
                return null;
            }

            // Читаем пользователей из файла
            List<User> users = fileReader.readFromFile(filePath);

            if (users.isEmpty()) {
                System.out.println("Ne udalos zagruzit polzovateley iz faila.");
                return null;
            }

            System.out.println("Uspeшно zagruzheno polzovateley: " + users.size());
            return users;

        } catch (IOException e) {
            System.out.println("Oshibka chteniya faila: " + e.getMessage());
            return null;
        }
    }

    /**
     * Сохранение данных в файл
     *
     * @param users список пользователей для сохранения
     * @return true если сохранение успешно, false при ошибке
     */
    public boolean saveToFile(List<User> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("Net dannyh dlya sohraneniya.");
            return false;
        }

        System.out.print("Vvedite put dlya sohraneniya faila: ");
        String filePath = scanner.nextLine().trim();

        try {
            // Проверяем доступность для записи
            if (!fileWriter.isWritable(filePath)) {
                System.out.println("Fail nedostupen dlya zapisi: " + filePath);
                return false;
            }

            System.out.println("\nVyberite rezhim zapisi:");
            System.out.println("1. Perepisat fail");
            System.out.println("2. Dopisat v konec");
            System.out.print("Vash vybor: ");

            int mode = Integer.parseInt(scanner.nextLine());

            if (mode == 1) {
                fileWriter.writeToFile(users, filePath);
                System.out.println("Dannye sohraneny v fail: " + filePath);
                return true;
            } else if (mode == 2) {
                fileWriter.appendToFile(users, filePath);
                System.out.println("Dannye dobavleny v fail: " + filePath);
                return true;
            } else {
                System.out.println("Nepravilnyy vybor. Sohranenie otmeneno.");
                return false;
            }

        } catch (IOException e) {
            System.out.println("Oshibka pri zapisi v fail: " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            System.out.println("Oshibka: vvedite korrektnoe chislo");
            return false;
        }
    }

}