
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        // Пути к шаблонам
        Path[] templateFiles = {
                Path.of("template1.txt"),
                Path.of("template2.txt"),
                Path.of("template3.txt")
        };
        // Инициализация TemplateManager
        TemplateManager templateManager = new TemplateManager(templateFiles);

        // Пример подстановок для пользователей
        List<Map<String, String>> substitutionsList = Arrays.asList(
                Map.of("user_name", "Иван", "area_name", "Москва"),
                Map.of("user_name", "Анна", "area_name", "Сочи"),
                Map.of("user_name", "Петр", "area_name", "Казань")
        );

        // Список названий шаблонов
        List<String> templateNames = Arrays.asList("template1.txt", "template2.txt", "template3.txt");

        // Каталог для результатов
        Path outputDir = Path.of("output");
        MultithreadedTemplateService multiService = new MultithreadedTemplateService(templateManager, 4, outputDir);

        // Обработка всех шаблонов для всех подстановок с сохранением в файлы
        Map<String, List<Path>> allResults = multiService.processAllTemplates(templateNames, substitutionsList);

        // Выводим имена созданных файлов
        for (Map.Entry<String, List<Path>> entry : allResults.entrySet()) {
            System.out.println("=== " + entry.getKey() + " ===");
            entry.getValue().forEach(System.out::println);
        }

        multiService.shutdown();

    }
}