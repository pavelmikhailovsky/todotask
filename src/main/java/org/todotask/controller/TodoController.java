package org.todotask.controller;

import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.todotask.model.Todo;
import org.todotask.service.TodoService;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Map;

import static org.todotask.controller.SchemaDefaultValue.SCHEMA_DEFAULT_VALUE_MESSAGE;

@Tag(name = "Todo", description = "The todo API")
@RestController
@RequestMapping("/todo")
public class TodoController {

    private TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "Get all todo for current user", tags = "todo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returned todo records",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Todo.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_MESSAGE)
                            )
                    }
            )
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Todo> getAllTodo(@RequestHeader("Authorization") String authorizationHeader) {
        return todoService.getAllTodoCurrentUser(authorizationHeader);
    }

    @Operation(summary = "Create todo for current user", tags = "todo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Returned status code 201 if it was successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_MESSAGE)
                            )
                    }
            )
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTodo(@RequestHeader("Authorization") String authorizationHeader,
                           @RequestParam("text") String text) {
        todoService.createTodo(authorizationHeader, text);
    }

    @Operation(summary = "Update text todo recording", tags = "todo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returned status code 201 if it was successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Todo is not find",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_MESSAGE)
                            )
                    }
            )
    })
    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateTodo(@RequestParam("userId") Long userId,
                           @RequestParam("text") String text) {
        todoService.updateTodo(userId, text);
    }

    @Operation(summary = "Delete todo", tags = "todo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Returned status code 204 if it was successfully deleting"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Todo is not find",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_MESSAGE)
                            )
                    }
            )
    })
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@RequestParam("todoId") Long todoId) {
        todoService.deleteTodo(todoId);
    }

    @Operation(summary = "Update status complete", tags = "todo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returned status code 200 if it was successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Todo is not find",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_MESSAGE)
                            )
                    }
            )
    })
    @PatchMapping("/update-status-complete")
    @ResponseStatus(HttpStatus.OK)
    public void updateStatusComplete(@RequestParam("todoId") Long todoId) {
        todoService.updateCompleteStatusTodo(todoId);
    }

    @ExceptionHandler({SQLDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> sqlException(Exception e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler({SignatureException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> tokenException(Exception e) {
        return Map.of("message", "token is not valid");
    }
}
