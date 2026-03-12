package Client.service.filler;


import Client.model.User;
import Client.service.filler.temp.TempFileReader;
import Client.service.filler.temp.TempValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileFiller implements DataFiller {
    private final TempFileReader fileReader;
    private final String filename;

    public FileFiller(String filename) {
        this.fileReader = new TempFileReader();
        this.filename = filename;
    }

    @Override
    public List<User> fill(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество пользователей должно быть положительным");
        }

        try {
            List<User> allUsers = fileReader.readUsersFromFile(filename);
            List<User> validUsers = new ArrayList<>();

            for (User user : allUsers) {
                if (TempValidator.isValidUser(user)) {
                    validUsers.add(user);
                } else {
                    System.err.println("Предупреждение: пропущен невалидный пользователь: " +
                            user + " - " + TempValidator.getValidationErrors(user));
                }
            }

            if (validUsers.size() >= count) {
                return validUsers.subList(0, count);
            } else {
                System.err.println("Предупреждение: в файле только " + validUsers.size() +
                        " валидных пользователей (запрошено " + count + ")");
                return new ArrayList<>(validUsers);
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + filename, e);
        }
    }


    public boolean isFileValid() {
        try {
            return fileReader.isValidFileFormat(filename);
        } catch (Exception e) {
            return false;
        }
    }

    public TempFileReader.FileStats getFileStats() throws IOException {
        return fileReader.getFileStats(filename);
    }
}