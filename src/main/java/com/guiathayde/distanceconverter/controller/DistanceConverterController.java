package com.guiathayde.distanceconverter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class DistanceConverterController {

    private static final double MILES_TO_METERS_FACTOR = 1609.34;
    private static final double FEET_TO_METERS_FACTOR = 0.3048;

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public String convertDistance(@RequestParam(required = false) String conversionType,
            @RequestParam(required = false) Double value,
            Model model, Locale locale) {

        if (conversionType == null && value == null) {
            return "index"; // Initial page load
        }

        if (value == null) {
            model.addAttribute("errorKey", "error.missingValue");
            model.addAttribute("conversionType", conversionType); // Keep selected type
            return "index";
        }

        if (conversionType == null || conversionType.trim().isEmpty()) {
            model.addAttribute("errorKey", "error.missingOption");
            model.addAttribute("value", value); // Keep entered value
            model.addAttribute("conversionType", conversionType); // Keep selected type (even if empty)
            return "index";
        }

        double convertedValue;
        String originalUnitKey;
        String convertedUnitKey;

        switch (conversionType) {
            case "mi-m":
                convertedValue = value * MILES_TO_METERS_FACTOR;
                originalUnitKey = "unit.miles";
                convertedUnitKey = "unit.meters";
                break;
            case "m-mi":
                convertedValue = value / MILES_TO_METERS_FACTOR;
                originalUnitKey = "unit.meters";
                convertedUnitKey = "unit.miles";
                break;
            case "ft-m":
                convertedValue = value * FEET_TO_METERS_FACTOR;
                originalUnitKey = "unit.feet";
                convertedUnitKey = "unit.meters";
                break;
            case "m-ft":
                convertedValue = value / FEET_TO_METERS_FACTOR;
                originalUnitKey = "unit.meters";
                convertedUnitKey = "unit.feet";
                break;
            default:
                model.addAttribute("errorKey", "error.invalidInput");
                model.addAttribute("value", value);
                model.addAttribute("conversionType", conversionType);
                return "index";
        }

        model.addAttribute("originalValue", value);
        model.addAttribute("convertedValue", convertedValue);
        model.addAttribute("originalUnitKey", originalUnitKey); // Pass keys for i18n
        model.addAttribute("convertedUnitKey", convertedUnitKey);
        model.addAttribute("conversionType", conversionType); // For result page to display context

        return "result";
    }
}
