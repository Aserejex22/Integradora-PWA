package mx.edu.utez.back.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public abstract class AbstractFirestoreRepository<T> {

    protected final Firestore firestore;
    protected final String collectionName;
    protected final Class<T> classType;

    public AbstractFirestoreRepository(String collectionName, Class<T> classType) {
        this.firestore = FirestoreClient.getFirestore();
        this.collectionName = collectionName;
        this.classType = classType;
    }

    public CollectionReference getCollection() {
        return firestore.collection(collectionName);
    }

    public String save(T entity, String id) throws ExecutionException, InterruptedException {
        String docId = (id == null || id.isEmpty()) ? UUID.randomUUID().toString() : id;
        ApiFuture<WriteResult> result = getCollection().document(docId).set(entity);
        result.get(); // Wait for result
        return docId;
    }

    public T findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = getCollection().document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            T object = document.toObject(classType);
            // Set the document ID on the object
            try {
                java.lang.reflect.Field idField = classType.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(object, document.getId());
            } catch (Exception e) {
                // If no id field exists, just return the object as-is
            }
            return object;
        } else {
            return null;
        }
    }

    public List<T> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCollection().get();
        List<com.google.cloud.firestore.QueryDocumentSnapshot> documents = future.get().getDocuments();
        return documents.stream()
                .map(doc -> {
                    T obj = doc.toObject(classType);
                    // Set the document ID on the object
                    try {
                        java.lang.reflect.Field idField = classType.getDeclaredField("id");
                        idField.setAccessible(true);
                        idField.set(obj, doc.getId());
                    } catch (Exception e) {
                        // If no id field exists, just return the object as-is
                    }
                    return obj;
                })
                .collect(Collectors.toList());
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = getCollection().document(id).delete();
        writeResult.get();
    }
}
