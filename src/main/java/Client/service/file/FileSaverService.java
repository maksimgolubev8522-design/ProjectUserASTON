package Client.service.file;

import Client.model.User;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileSaverService {

    private final FileWriterService fileWriter;

    public FileSaverService() {
        this.fileWriter = new FileWriterService();
    }

    public void saveToFile(List<User> users, String filePath, SaveMode mode) throws IOException {
        switch (mode) {
            case OVERWRITE:
                fileWriter.writeToFile(users, filePath);
                break;
            case APPEND:
                fileWriter.appendToFile(users, filePath);
                break;
            case APPEND_WITH_TIMESTAMP:
                appendWithTimestamp(users, filePath);
                break;
        }
    }

    private void appendWithTimestamp(List<User> users, String filePath) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<String> timestampedUsers = users.stream()
                .map(user -> String.format("# %s%n%s", timestamp, formatUser(user)))
                .toList();

        Path path = Paths.get(filePath);

        if (path.getParent() != null && !Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        Files.write(path, timestampedUsers, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        System.out.println(" Данные добавлены с временной меткой. Всего: " + users.size());
    }

    private String formatUser(User user) {
        return String.format("%s;%s;%s", user.getName(), user.getPassword(), user.getMail());
    }

    public enum SaveMode {
        OVERWRITE,
        APPEND,
        APPEND_WITH_TIMESTAMP
    }
}