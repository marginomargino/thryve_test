package com.vraikhlin.thryve.controller;

import com.vraikhlin.thryve.model.DynamicEpochData;
import com.vraikhlin.thryve.model.HeartRateData;
import com.vraikhlin.thryve.service.DynamicEpochService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.vraikhlin.thryve.controller.DynamicEpochController.DYNAMIC_EPOCHS_ENDPOINT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DynamicEpochController.class)
@DisplayName("DynamicEpoch Controller Test")
class DynamicEpochControllerTest {

    private static final Long START_TIMESTAMP = 1605595957000L;
    private static final Long END_TIMESTAMP = 1605595998000L;
    private static final Double HEART_RATE = 78D;
    private static final String USER_ID = "123456";
    private static final String DATA_SOURCE_ID = "8";
    private static final int DYNAMIC_VALUE_TYPE = 3000;
    private static final String VALUE_TYPE = "LONG";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @MockBean
    private DynamicEpochService service;

    @Test
    @DisplayName("Test dynamic epoch post endpoint")
    void testDynamicEpochPost() throws Exception {

        when(service.persist(anyList())).thenReturn(1);
        String postPayload = getResourceContent("classpath:post_payload.json");

        mvc.perform(post(DYNAMIC_EPOCHS_ENDPOINT)
                .content(postPayload)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        postPayload = getResourceContent("classpath:post_payload_400.json");

        mvc.perform(post(DYNAMIC_EPOCHS_ENDPOINT)
                .content(postPayload)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Test dynamic epoch get endpoint")
    void testDynamicEpochGet() throws Exception {

        List<DynamicEpochData> data = List.of(DynamicEpochData.builder()
                .userId(USER_ID)
                .dataSourceId(DATA_SOURCE_ID)
                .startTimestamp(START_TIMESTAMP)
                .endTimestamp(END_TIMESTAMP)
                .createdAt(START_TIMESTAMP)
                .dynamicValueType(DYNAMIC_VALUE_TYPE)
                .valueType(VALUE_TYPE)
                .value(String.valueOf(HEART_RATE))
                .build());

        when(service.getEpochData(eq(START_TIMESTAMP), eq(END_TIMESTAMP), eq(USER_ID), eq(DYNAMIC_VALUE_TYPE)))
                .thenReturn(data);

        mvc.perform(get(DYNAMIC_EPOCHS_ENDPOINT)
                .param("startTimestampUnix", String.valueOf(START_TIMESTAMP))
                .param("endTimestampUnix", String.valueOf(END_TIMESTAMP))
                .param("userId", USER_ID)
                .param("dynamicValueType", String.valueOf(DYNAMIC_VALUE_TYPE))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].userId", equalTo(USER_ID)))
                .andExpect(jsonPath("$.[0].dataSourceId", equalTo(DATA_SOURCE_ID)))
                .andExpect(jsonPath("$.[0].startTimestampUnix", equalTo(START_TIMESTAMP)))
                .andExpect(jsonPath("$.[0].endTimestampUnix", equalTo(END_TIMESTAMP)))
                .andExpect(jsonPath("$.[0].createdAtUnix", equalTo(START_TIMESTAMP)))
                .andExpect(jsonPath("$.[0].dynamicValueType", equalTo(DYNAMIC_VALUE_TYPE)))
                .andExpect(jsonPath("$.[0].valueType", equalTo(VALUE_TYPE)))
                .andExpect(jsonPath("$.[0].value", equalTo(String.valueOf(HEART_RATE))));
    }

    @Test
    @DisplayName("Test average heart rate get endpoint")
    void testAverageHeartRateGet() throws Exception {

        HeartRateData heartRateData = HeartRateData.builder()
                .startTimestamp(START_TIMESTAMP)
                .endTimestamp(END_TIMESTAMP)
                .averageHeartRate(HEART_RATE)
                .build();

        when(service.getAverageHeartRate(eq(START_TIMESTAMP), eq(END_TIMESTAMP), eq(USER_ID))).thenReturn(heartRateData);

        mvc.perform(get(DYNAMIC_EPOCHS_ENDPOINT + "/" + USER_ID + "/average-heart-rate")
                .param("startTimestampUnix", String.valueOf(START_TIMESTAMP))
                .param("endTimestampUnix", String.valueOf(END_TIMESTAMP)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.startTimestampUnix", equalTo(START_TIMESTAMP)))
                .andExpect(jsonPath("$.endTimestampUnix", equalTo(END_TIMESTAMP)))
                .andExpect(jsonPath("$.averageHeartRate", equalTo(HEART_RATE)));
    }

    private String getResourceContent(String resourceLocation) throws IOException {
        Resource resource = resourceLoader.getResource(resourceLocation);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

    }

}