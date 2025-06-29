package cristoffer85.com.projektatornserver.GEMINIAPI.controller;

import org.springframework.web.bind.annotation.RestController;
import cristoffer85.com.projektatornserver.GEMINIAPI.dto.ProjectIdeaRequestDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/project-ideas")
public class ProjectIdeaController {

    @Value("${google.gemini.api.key}")
    private String geminiApiKey;

    @PostMapping
    public ResponseEntity<List<String>> generateIdeas(@RequestBody ProjectIdeaRequestDTO params) {
        String prompt = String.format(
            "Generate 10 short, creative programming project ideas for a %s project using %s, suitable for a project lasting up to %s weeks. List each idea as a short bullet point.",
            params.type, params.languages, params.length
        );

        List<String> ideas = callGemini(prompt, geminiApiKey);
        return ResponseEntity.ok(ideas);
    }

    @SuppressWarnings("unchecked")
    private List<String> callGemini(String prompt, String apiKey) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey;

        // Gemini expects a "contents" array with "parts"
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(Map.of("text", prompt)))
            )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            url,
            org.springframework.http.HttpMethod.POST,
            entity,
            new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body == null) throw new RuntimeException("No response from Gemini");

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
        if (candidates == null || candidates.isEmpty()) throw new RuntimeException("No candidates in Gemini response");

        Map<String, Object> firstCandidate = candidates.get(0);
        Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        Map<String, Object> firstPart = parts.get(0);
        String text = (String) firstPart.get("text");

        // Split into ideas (assuming Gemini returns a bulleted or numbered list)
        return Arrays.stream(text.split("\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
    }
}