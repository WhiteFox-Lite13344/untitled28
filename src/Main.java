
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        // ���� � ��������
        Path[] templateFiles = {
                Path.of("template1.txt"),
                Path.of("template2.txt"),
                Path.of("template3.txt")
        };
        // ������������� TemplateManager
        TemplateManager templateManager = new TemplateManager(templateFiles);

        // ������ ����������� ��� �������������
        List<Map<String, String>> substitutionsList = Arrays.asList(
                Map.of("user_name", "����", "area_name", "������"),
                Map.of("user_name", "����", "area_name", "����"),
                Map.of("user_name", "����", "area_name", "������")
        );

        // ������ �������� ��������
        List<String> templateNames = Arrays.asList("template1.txt", "template2.txt", "template3.txt");

        // ������� ��� �����������
        Path outputDir = Path.of("output");
        MultithreadedTemplateService multiService = new MultithreadedTemplateService(templateManager, 4, outputDir);

        // ��������� ���� �������� ��� ���� ����������� � ����������� � �����
        Map<String, List<Path>> allResults = multiService.processAllTemplates(templateNames, substitutionsList);

        // ������� ����� ��������� ������
        for (Map.Entry<String, List<Path>> entry : allResults.entrySet()) {
            System.out.println("=== " + entry.getKey() + " ===");
            entry.getValue().forEach(System.out::println);
        }

        multiService.shutdown();

    }
}