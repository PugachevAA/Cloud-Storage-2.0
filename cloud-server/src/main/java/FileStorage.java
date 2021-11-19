import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class FileStorage {

    public FileStorage() {
        log.debug("Инициализация файлового класса");
        if (!Config.ROOT_PATH.toFile().exists()) {
            log.debug("Создание корневой папки, если нет");
            Config.ROOT_PATH.toFile().mkdir();
        }
    }

    public static void createDirectory(String path) {
        log.debug("Создание директории");
        File dir = Path.of(Config.ROOT_PATH.toString(), path).toFile();
        if (!dir.exists()) {
            dir.mkdir();
            log.debug("Создали директорию");
        }
    }
    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<>();
        File dir = Path.of(Config.ROOT_PATH.toString(), path).toFile();
        if (dir.exists() && dir.listFiles() != null) {
            files = Arrays.asList(dir.list());
        } else {
            files.add("No files");
        }

        return files;
    }
}
