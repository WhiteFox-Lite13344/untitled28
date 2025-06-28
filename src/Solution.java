import Matcher.StringMatcherFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Solution {
    public static void main(String[] args) throws Exception {
        // Определяем содержимое шаблонов (сокращенные блоки из xaiArtifact)
        String[] templateContents = {
                // template1.txt
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. <user_name> Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. \\<area_name> Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n" +
                        "<company_name> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \\<user_name> Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. <date> Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n",
                // template2.txt
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. <area_name> Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. \\<user_name> Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n" +
                        "<product_name> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \\<area_name> Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. <user_name> Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n",
                // template3.txt
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. <date> Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, <product_name> quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. \\<area_name> Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n" +
                        "<company_name> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. \\<user_name> Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. <area_name> Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n"
        };
        String[] templateNames = {"template1.txt", "template2.txt", "template3.txt"};

        // Сохраняем файлы (~500 КБ каждый)
        for (int i = 0; i < templateNames.length; i++) {
            StringBuilder fullContent = new StringBuilder();
            while (fullContent.length() < 500_000) {
                fullContent.append(templateContents[i]);
            }
            // Обрезаем до ~500 КБ, чтобы сохранить целостность текста
            String content = fullContent.substring(0, Math.min(fullContent.length(), 500_000));
            Path filePath = Paths.get(templateNames[i]);
            Files.writeString(filePath, content);
            System.out.println("Saved " + templateNames[i] + " with size " + Files.size(filePath) + " bytes");
        }
        // Инициализация TemplateManager
        Path[] templateFiles = {
                Paths.get("template1.txt"),
                Paths.get("template2.txt"),
                Paths.get("template3.txt")
        };
        TemplateManager manager = new TemplateManager(templateFiles);

        // Таблица подстановки
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("user_name", "Иван");
        substitutions.put("area_name", "Москва");
        substitutions.put("company_name", "ООО Ромашка");
        substitutions.put("date", "2025-06-26");
        substitutions.put("product_name", "СуперГаджет");

        // Обработка шаблонов
        for (String templateName : new String[]{"template1.txt", "template2.txt", "template3.txt"}) {
            String result = manager.processTemplate(templateName, substitutions);
            System.out.println("Processed " + templateName + ": " +
                    (result.length() > 100 ? result.substring(0, 100) + "..." : result));
        }

        // Тест с пустым файлом
        Path[] emptyFiles = {Paths.get("empty.txt")};
        Files.writeString(Paths.get("empty.txt"), "");
        TemplateManager emptyManager = new TemplateManager(emptyFiles);
        String result = emptyManager.processTemplate("empty.txt", substitutions);
        System.out.println("Empty template result: " + result); // Должно быть null
        // Тест с throwOnUndefined
        TemplateProcessor processor = new TemplateProcessor(
                StringMatcherFactory.INSTANCE.stringMatcher("<"),
                StringMatcherFactory.INSTANCE.stringMatcher(">"),
                '\\', true, LookupFactory.INSTANCE.mapLookup(substitutions)
        );
        try {
            result = processor.replace("Здравствуйте <unknown>!");
            System.out.println(result);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage()); // Ожидается: Undefined variable: unknown
        }
    }
}
