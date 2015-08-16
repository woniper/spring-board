package demo.generic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by woniper on 15. 4. 27..
 */
@RestController
public abstract class GenericController<T, K extends Serializable, S extends GenericService> {

    @Autowired
    protected S service;

    @RequestMapping(method = RequestMethod.GET) List<T> get() {
        return service.getAll();
    }

    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET) T get(@PathVariable("boardId") K key) {
        return (T) service.get(key);
    }

    @RequestMapping(method = RequestMethod.POST) void add(@RequestBody T domain) {
        service.add(domain);
    }
}
