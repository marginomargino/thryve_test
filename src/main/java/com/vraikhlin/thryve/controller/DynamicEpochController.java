package com.vraikhlin.thryve.controller;

import com.vraikhlin.thryve.model.DynamicEpoch;
import com.vraikhlin.thryve.model.DynamicEpochData;
import com.vraikhlin.thryve.model.HeartRateData;
import com.vraikhlin.thryve.service.DynamicEpochService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.vraikhlin.thryve.controller.DynamicEpochController.DYNAMIC_EPOCHS_ENDPOINT;

@RestController
@RequestMapping(DYNAMIC_EPOCHS_ENDPOINT)
@Validated
public class DynamicEpochController {

    //TODO Add ExceptionHandler

    @Autowired
    public DynamicEpochController(DynamicEpochService service) {
        this.service = service;
    }

    public static final String DYNAMIC_EPOCHS_ENDPOINT = "/dynamic-epochs";

    private final DynamicEpochService service;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postDynamicEpoch(@RequestBody List<DynamicEpoch> dynamicEpoches) {
        int count = service.persist(dynamicEpoches);
        return count > 0 ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DynamicEpochData>> getDynamicEpoch(@RequestParam("startTimestampUnix") Long startTimestamp,
                                                                   @RequestParam("endTimestampUnix") Long endTimestamp,
                                                                   @RequestParam("userId") String userId,
                                                                   @RequestParam("dynamicValueType") Integer dynamicValueType){
        List<DynamicEpochData> data = service.getEpochData(startTimestamp, endTimestamp, userId, dynamicValueType);
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/{user_id}/average-heart-rate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HeartRateData> getAverageHeartRate(@PathVariable("user_id") @NotBlank String userId,
                                                             @RequestParam("startTimestampUnix") Long startTimestamp,
                                                             @RequestParam("endTimestampUnix") Long endTimestamp){
        HeartRateData heartRate = service.getAverageHeartRate(startTimestamp, endTimestamp, userId);
        return ResponseEntity.ok(heartRate);
    }


}
