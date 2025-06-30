package cristoffer85.com.projektatornserver.RABBITMQ.E2EENCRYPTION.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cristoffer85.com.projektatornserver.RABBITMQ.E2EENCRYPTION.model.EncryptedPrivateKeyEntity;
import cristoffer85.com.projektatornserver.RABBITMQ.E2EENCRYPTION.repository.EncryptedPrivateKeyRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/private-key")
public class EncryptedPrivateKeyController {

    @Autowired
    private EncryptedPrivateKeyRepository encryptedPrivateKeyRepository;

    @PostMapping("/{username}/encrypted-private-key")
    public ResponseEntity<?> uploadEncryptedPrivateKey(@PathVariable String username, @RequestBody Map<String, String> encrypted) {
        EncryptedPrivateKeyEntity entity = new EncryptedPrivateKeyEntity();
        entity.setUsername(username);
        entity.setCiphertext(encrypted.get("ciphertext"));
        entity.setSalt(encrypted.get("salt"));
        entity.setIv(encrypted.get("iv"));
        encryptedPrivateKeyRepository.save(entity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/encrypted-private-key")
    public ResponseEntity<Map<String, String>> getEncryptedPrivateKey(@PathVariable String username) {
        EncryptedPrivateKeyEntity entity = encryptedPrivateKeyRepository.findById(username).orElse(null);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, String> result = new HashMap<>();
        result.put("ciphertext", entity.getCiphertext());
        result.put("salt", entity.getSalt());
        result.put("iv", entity.getIv());
        return ResponseEntity.ok(result);
    }
}