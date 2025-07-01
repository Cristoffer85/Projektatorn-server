package cristoffer85.com.projektatornserver.E2EENCRYPTION.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cristoffer85.com.projektatornserver.E2EENCRYPTION.model.PrivateKey;
import cristoffer85.com.projektatornserver.E2EENCRYPTION.repository.PrivateKeyRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/private-key")
public class PrivateKeyController {

    @Autowired
    private PrivateKeyRepository encryptedPrivateKeyRepository;

    @PostMapping("/{username}/encrypted-private-key")
    public ResponseEntity<?> uploadEncryptedPrivateKey(@PathVariable String username, @RequestBody Map<String, String> encrypted) {
        PrivateKey entity = new PrivateKey();
        entity.setUsername(username);
        entity.setCiphertext(encrypted.get("ciphertext"));
        entity.setSalt(encrypted.get("salt"));
        entity.setIv(encrypted.get("iv"));
        encryptedPrivateKeyRepository.save(entity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/encrypted-private-key")
    public ResponseEntity<Map<String, String>> getEncryptedPrivateKey(@PathVariable String username) {
        PrivateKey entity = encryptedPrivateKeyRepository.findById(username).orElse(null);
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