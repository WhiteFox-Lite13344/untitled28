import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class MultithreadedTemplateService {
    private final TemplateManager templateManager;
    private final ExecutorService executor;
    private final Path outputDir;

    public MultithreadedTemplateService(TemplateManager templateManager, int threadCount, Path outputDir) throws IOException {
        this.templateManager = templateManager;
        this.executor = Executors.newFixedThreadPool(threadCount);
        this.outputDir = outputDir;
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }
    }

    /**
     * Обработать один шаблон для каждого набора подстановок (все задачи параллельно)
     * Каждый результат сохраняется в файл: templateName_N.txt
     */
    public List<Path> processAll(String templateName, List<Map<String, String>> substitutionsList) {
        List<Future<Path>> futures = new ArrayList<>();
        for (int i = 0; i < substitutionsList.size(); i++) {
            final int idx = i;
            Map<String, String> subs = substitutionsList.get(i);
            futures.add(executor.submit(() -> processAndSave(templateName, subs, idx)));
        }
        return futures.stream().map(f -> {
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * Обработать все шаблоны для каждого набора подстановок (все задачи параллельно)
     * Каждый результат сохраняется в файл: templateName_N.txt
     * На выходе: Map шаблон -> список путей к файлам
     */
    public Map<String, List<Path>> processAllTemplates(List<String> templateNames, List<Map<String, String>> substitutionsList) {
        Map<String, List<Path>> results = new LinkedHashMap<>();
        for (String templateName : templateNames) {
            List<Path> files = processAll(templateName, substitutionsList);
            results.put(templateName, files);
        }
        return results;
    }

    /**
     * Заменяет якоря и сохраняет результат в файл
     */
    private Path processAndSave(String templateName, Map<String, String> substitutions, int idx) throws IOException {
        String processed = templateManager.processTemplate(templateName, substitutions);
        String outFileName = templateName + "_" + idx + ".txt";
        Path outPath = outputDir.resolve(outFileName);
        Files.writeString(outPath, processed);
        return outPath;
    }

    public void shutdown() {
        executor.shutdown();
    }
}