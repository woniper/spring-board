package demo.generic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by woniper on 15. 4. 27..
 */
public abstract class GenericService<T, K extends Serializable, R extends JpaRepository> {

    @Autowired
    protected R repository;

    List<T> getAll() {
        return repository.findAll();
    }

    T get(K key) {
        return (T) repository.findOne(key);
    }

    void add(T domain) {
        repository.save(domain);
    }
}
