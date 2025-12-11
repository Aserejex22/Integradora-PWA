package mx.edu.utez.back.controller;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/test-firebase")
public class FirestoreTestController {

    @GetMapping
    public String testConnection() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        Map<String, Object> data = new HashMap<>();
        data.put("message", "Hello from Spring Boot!");
        data.put("timestamp", System.currentTimeMillis());

        // This will create a collection named "test_collection" and a document within
        // it
        db.collection("test_collection").document("ping").set(data).get();

        return "Connection successful! Check 'test_collection' in your Firebase Console.";
    }
}
