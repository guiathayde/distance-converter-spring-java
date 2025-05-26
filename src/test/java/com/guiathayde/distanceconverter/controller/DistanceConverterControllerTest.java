package com.guiathayde.distanceconverter.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is; // For model().attribute check with double precision
import static org.hamcrest.Matchers.closeTo; // For double comparison

@WebMvcTest(DistanceConverterController.class)
public class DistanceConverterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetRoot_thenReturnsIndexView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeDoesNotExist("originalValue", "convertedValue", "errorKey", "conversionType", "value"));
    }

    @Test
    void whenPostMiToM_thenReturnsResultViewWithConversion() throws Exception {
        mockMvc.perform(post("/")
                        .param("conversionType", "mi-m")
                        .param("value", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("originalValue", 10.0))
                .andExpect(model().attribute("convertedValue", closeTo(16093.4, 0.001)))
                .andExpect(model().attribute("originalUnitKey", "unit.miles"))
                .andExpect(model().attribute("convertedUnitKey", "unit.meters"))
                .andExpect(model().attribute("conversionType", "mi-m"))
                .andExpect(model().attributeDoesNotExist("errorKey"));
    }

    @Test
    void whenPostMToMi_thenReturnsResultViewWithConversion() throws Exception {
        mockMvc.perform(post("/")
                        .param("conversionType", "m-mi")
                        .param("value", "1000"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("originalValue", 1000.0))
                .andExpect(model().attribute("convertedValue", closeTo(0.6213711922, 0.000001)))
                .andExpect(model().attribute("originalUnitKey", "unit.meters"))
                .andExpect(model().attribute("convertedUnitKey", "unit.miles"))
                .andExpect(model().attribute("conversionType", "m-mi"))
                .andExpect(model().attributeDoesNotExist("errorKey"));
    }

    @Test
    void whenPostFtToM_thenReturnsResultViewWithConversion() throws Exception {
        mockMvc.perform(post("/")
                        .param("conversionType", "ft-m")
                        .param("value", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("originalValue", 10.0))
                .andExpect(model().attribute("convertedValue", closeTo(3.048, 0.001)))
                .andExpect(model().attribute("originalUnitKey", "unit.feet"))
                .andExpect(model().attribute("convertedUnitKey", "unit.meters"))
                .andExpect(model().attribute("conversionType", "ft-m"))
                .andExpect(model().attributeDoesNotExist("errorKey"));
    }

    @Test
    void whenPostMToFt_thenReturnsResultViewWithConversion() throws Exception {
        mockMvc.perform(post("/")
                        .param("conversionType", "m-ft")
                        .param("value", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("originalValue", 2.0))
                .andExpect(model().attribute("convertedValue", closeTo(6.56167979, 0.000001)))
                .andExpect(model().attribute("originalUnitKey", "unit.meters"))
                .andExpect(model().attribute("convertedUnitKey", "unit.feet"))
                .andExpect(model().attribute("conversionType", "m-ft"))
                .andExpect(model().attributeDoesNotExist("errorKey"));
    }

    @Test
    void whenPostMissingValue_thenReturnsIndexViewWithError() throws Exception {
        mockMvc.perform(post("/")
                        .param("conversionType", "mi-m")) // No value parameter
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorKey", "error.missingValue"))
                .andExpect(model().attribute("conversionType", "mi-m"))
                .andExpect(model().attributeDoesNotExist("originalValue", "convertedValue", "value"));
    }

    @Test
    void whenPostMissingConversionType_thenReturnsIndexViewWithError() throws Exception {
        mockMvc.perform(post("/")
                        .param("value", "10")) // No conversionType parameter
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorKey", "error.missingOption"))
                .andExpect(model().attribute("value", 10.0))
                .andExpect(model().attributeDoesNotExist("originalValue", "convertedValue", "conversionType", "originalUnitKey"));
    }
    
    @Test
    void whenPostEmptyConversionType_thenReturnsIndexViewWithError() throws Exception {
        mockMvc.perform(post("/")
                        .param("conversionType", "") 
                        .param("value", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorKey", "error.missingOption"))
                .andExpect(model().attribute("value", 10.0))
                .andExpect(model().attribute("conversionType", ""))
                .andExpect(model().attributeDoesNotExist("originalValue", "convertedValue", "originalUnitKey"));
    }

    @Test
    void whenPostInvalidConversionType_thenReturnsIndexViewWithError() throws Exception {
        mockMvc.perform(post("/")
                        .param("conversionType", "invalid-type")
                        .param("value", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorKey", "error.invalidInput"))
                .andExpect(model().attribute("value", 10.0))
                .andExpect(model().attribute("conversionType", "invalid-type"))
                .andExpect(model().attributeDoesNotExist("originalValue", "convertedValue", "originalUnitKey"));
    }
}
