package com.benjsoft.akitabackendservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(value = "*", maxAge = 3600L)
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository repository) {
        this.todoRepository = repository;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<TodoModel>> getTodos() {
        return ResponseEntity.ok(this.todoRepository.findAll());
    }

    @PostMapping("/todos")
    public ResponseEntity<TodoModel> addTodo(@RequestBody TodoModel entity) {
        entity.set_id(UUID.randomUUID().toString());
        TodoModel savedEntity = this.todoRepository.save(entity);

        // Build the URI location for the created entity
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(savedEntity.getId())
//                .toUri();

//        return ResponseEntity.created(location).build();

        return ResponseEntity.ok(savedEntity);
    }

    @DeleteMapping("/todos/{id}")
    public void deletedTodo(@PathVariable long id) {
        this.todoRepository.deleteById(id);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<TodoModel> updateTodo(@PathVariable long id, @RequestBody TodoModel changed) {
        Optional<TodoModel> found = this.todoRepository.findById(id);
        if(found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TodoModel model = found.get();

        if(changed.getTitle() != null) {
            model.setTitle(changed.getTitle());
        }

        if(changed.getDescription() != null) {
            model.setDescription(changed.getDescription());
        }

        if(changed.getStatus() != null) {
            model.setStatus(changed.getStatus());
        }

        if(changed.get_id() != null) {
            model.set_id(changed.get_id());
        }

        return ResponseEntity.ok(this.todoRepository.save(model));
    }
}
