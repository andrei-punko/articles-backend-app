package by.andd3dfx.controllers;

import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.dto.MethodCallRecord;
import by.andd3dfx.services.ILoggingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
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

    /**
     * Get logged records
     */
    @ApiOperation("Get logged records")
    @GetMapping("/logs")
    public List<MethodCallRecord> getLoggedRecords(@ApiParam(value = "Logging search criteria") LoggingSearchCriteria criteria) {
        return loggingService.getLoggedRecords(criteria);
    }

    /**
     * Push new logging record
     */
    @ApiOperation("Push new logging record")
    @PostMapping("/logs")
    @ResponseStatus(HttpStatus.CREATED)
    public void addLoggingRecord(@RequestBody @Validated MethodCallRecord loggedRecord) {
        loggingService.addLoggingRecord(loggedRecord);
    }
}
