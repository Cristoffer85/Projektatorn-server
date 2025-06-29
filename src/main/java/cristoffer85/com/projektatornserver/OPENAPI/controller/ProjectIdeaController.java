package cristoffer85.com.projektatornserver.OPENAPI.controller;

import org.springframework.web.bind.annotation.RestController;

import cristoffer85.com.projektatornserver.OPENAPI.dto.ProjectIdeaRequestDTO;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import java.util.List;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/project-ideas")
public class ProjectIdeaController {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @PostMapping
    public ResponseEntity<List<String>> generateIdeas(@RequestBody ProjectIdeaRequestDTO params) {
        String prompt = String.format(
            "Generate 10 short, creative programming project ideas for a %s project using %s, suitable for a project lasting up to %s weeks. List each idea as a short bullet point.",
            params.type, params.languages, params.length
        );

        List<String> ideas = callOpenAI(prompt, openaiApiKey);
        return ResponseEntity.ok(ideas);
    }

    @SuppressWarnings("unchecked")
    private List<String> callOpenAI(String prompt, String apiKey) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare request
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo");
        request.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        request.put("max_tokens", 500);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        // Call OpenAI API
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            "https://api.openai.com/v1/chat/completions",
            org.springframework.http.HttpMethod.POST,
            entity,
            new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body == null) throw new RuntimeException("No response from OpenAI");

        List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
        if (choices == null || choices.isEmpty()) throw new RuntimeException("No choices in OpenAI response");

        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        String content = (String) message.get("content");

        // Split into ideas (assuming OpenAI returns a numbered or bulleted list)
        return Arrays.stream(content.split("\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
    }
}
