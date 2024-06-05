package com.benjsoft.akitabackendservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(value = "*", maxAge = 3600L)
public class TodoController {

    private final TodoRepository todoRepository;


    /**
     * Constructs a new `TodoController` instance with the provided `TodoRepository`.
     *
     * @param repository the `TodoRepository` instance to be used by this controller
     */
    public TodoController(TodoRepository repository) {
        this.todoRepository = repository;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<TodoModel>> getTodos() {
        return ResponseEntity.ok(this.todoRepository.findAll());
    }

    /**
     * Adds a new todo item to the system.
     *
     * @param entity The todo item to be added.
     * @return The saved todo item.
     */
    @PostMapping("/todos")
    public ResponseEntity<TodoModel> addTodo(@RequestBody TodoModel entity) {
        entity.set_id(UUID.randomUUID().toString());
        TodoModel savedEntity = this.todoRepository.save(entity);
        return ResponseEntity.ok(savedEntity);
    }

    /**
     * Deletes a todo item by the specified ID.
     *
     * @param id The ID of the todo item to delete.
     */
    @DeleteMapping("/todos/{id}")
    public void deletedTodo(@PathVariable long id) {
        this.todoRepository.deleteById(id);
    }

    /**
     * Updates an existing todo item in the database.
     *
     * @param id The ID of the todo item to update.
     * @param changed The updated todo item data.
     * @return The updated todo item, or a 404 Not Found response if the todo item does not exist.
     */
    @PutMapping("/todos/{id}")
    public ResponseEntity<TodoModel> updateTodo(@PathVariable long id, @RequestBody TodoModel changed) {
        final boolean[] canUpdate = {false};
        final TodoModel[] response = {null};

        this.todoRepository.findById(id)
                .ifPresentOrElse((model) -> {
                   // Update the model fields here
                    if(Objects.nonNull(changed.getTitle())) {
                        model.setTitle(changed.getTitle());
                    }
                    if(Objects.nonNull(changed.getDescription())) {
                        model.setDescription(changed.getDescription());
                    }
                    if(Objects.nonNull(changed.getStatus())) {
                        model.setStatus(changed.getStatus());
                    }
                    if(Objects.nonNull(changed.get_id())) {
                        model.set_id(changed.get_id());
                    }
                    canUpdate[0] = true;
                    response[0] = model;
                }, () -> {
                    canUpdate[0] = false;
                });

        if(canUpdate[0] && Objects.nonNull(response[0])) {
            return ResponseEntity.ok(response[0]);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
