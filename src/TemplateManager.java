

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class TemplateManager {
    private final Map<String, String> templateCache;

    public TemplateManager(Path[] templateFiles) throws IOException {
        Map<String, String> templates = new HashMap<>();
        for (Path file : templateFiles) {
            if (file != null) {
                String content = Files.readString(file);
                templates.put(file.getFileName().toString(), content);
            }
        }
        this.templateCache = Map.copyOf(templates);
    }

    public String processTemplate(String templateName, Map<String, String> substitutions) {
        String template = templateCache.get(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateName);
        }
        Lookup lookup = LookupFactory.INSTANCE.mapLookup(substitutions);
        TemplateProcessor processor = new TemplateProcessor(lookup);
        return processor.replace(template);
    }
}
