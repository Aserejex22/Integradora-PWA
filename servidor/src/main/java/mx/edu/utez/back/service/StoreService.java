package mx.edu.utez.back.service;

import mx.edu.utez.back.model.Store;
import mx.edu.utez.back.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> findAll() {
        try {
            return storeRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error finding stores", e);
        }
    }

    public Optional<Store> findById(Long id) {
        try {
            return storeRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding store", e);
        }
    }

    public Optional<Store> findByCode(String code) {
        try {
            return storeRepository.findByCode(code);
        } catch (Exception e) {
            throw new RuntimeException("Error finding store by code", e);
        }
    }

    public Store create(Store store) {
        return save(store);
    }

    public Store update(Store store) {
        return save(store);
    }

    public Store save(Store store) {
        try {
            return storeRepository.save(store);
        } catch (Exception e) {
            throw new RuntimeException("Error saving store", e);
        }
    }

    public void delete(Long id) {
        try {
            storeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting store", e);
        }
    }
}