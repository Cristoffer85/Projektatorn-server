package cristoffer85.com.projektatornserver.E2EENCRYPTION.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import cristoffer85.com.projektatornserver.E2EENCRYPTION.model.PublicKey;
import cristoffer85.com.projektatornserver.E2EENCRYPTION.repository.PublicKeyRepository;

import com.fasterxml.jackson.core.type.TypeReference;

@RestController
@RequestMapping("/public-key")
public class PublicKeyController {

    @Autowired
    private PublicKeyRepository publicKeyRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/{username}/public-key")
    public ResponseEntity<?> uploadPublicKey(@PathVariable String username, @RequestBody Map<String, Object> jwk) throws Exception {
        PublicKey entity = new PublicKey();
        entity.setUsername(username);
        entity.setJwkJson(objectMapper.writeValueAsString(jwk));
        publicKeyRepository.save(entity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/public-key")
    public ResponseEntity<Map<String, Object>> getPublicKey(@PathVariable String username) throws Exception {
        PublicKey entity = publicKeyRepository.findById(username).orElse(null);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> jwk = objectMapper.readValue(
            entity.getJwkJson(),
            new TypeReference<Map<String, Object>>() {}
        );
        return ResponseEntity.ok(jwk);
    }
}