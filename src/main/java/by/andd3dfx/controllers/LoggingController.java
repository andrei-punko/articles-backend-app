package by.andd3dfx.controllers;

import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.dto.MethodCallRecord;
import by.andd3dfx.services.ILoggingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoggingController {

    protected final ILoggingService loggingService;

    @Operation(summary = "Get logged records")
    @GetMapping("/logs")
    public List<MethodCallRecord> getLoggedRecords(
        @ParameterObject LoggingSearchCriteria criteria) {
        return loggingService.getLoggedRecords(criteria);
    }

    @Operation(summary = "Push new logging record")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Record created")
    })
    @PostMapping("/logs")
    @ResponseStatus(HttpStatus.CREATED)
    public void addLoggingRecord(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Logged method call record",
            required = true,
            content = @Content(schema = @Schema(implementation = MethodCallRecord.class)))
        @RequestBody @Validated MethodCallRecord loggedRecord) {
        loggingService.addLoggingRecord(loggedRecord);
    }
}
