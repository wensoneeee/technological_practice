package com.technokratos.bookingservice.service.classes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.technokratos.bookingservice.service.interfaces.CurrencyRateLoader;
import com.technokratos.bookingservice.service.interfaces.LoggingService;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyRateLoaderImpl implements CurrencyRateLoader {
    private static final String CBR_DAILY_CUR = "https://www.cbr-xml-daily.ru/daily_json.js";
    private String response;
    private BigDecimal usdRate;


    private final RestTemplate restTemplate;
    private final LoggingService loggingService;

    @Override
    @Scheduled(cron = "0 1 18 * * *")
    public void getCbrDailyCur(){
        try {
            String response = restTemplate.getForObject(CBR_DAILY_CUR, String.class);
            log.info("курсы валют успешно получены");
            this.response = response;
            updateUsdRate();
        } catch (RestClientException e) {
            loggingService.log("ERROR", "getCbrDailyCur", "CurrencyRateLoaderImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUsdRate(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);
            JsonNode rateNode = jsonNode.get("Valute").path("USD");
            if(!rateNode.isNull()){
                double rate = rateNode.path("Value").asDouble();
                double nominal = rateNode.path("Nominal").asDouble();
                this.usdRate = BigDecimal.valueOf(rate / nominal);
                log.info("Курс USD обновлен: {}", usdRate);
            }
        } catch (Exception e) {
            loggingService.log("ERROR", "updateUsdRate", "CurrencyRateLoaderImpl", "метод выбросил исключение: "+e.getMessage(), loggingService.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigDecimal getUsdRate(){
        if(usdRate == null){
            getCbrDailyCur();
        }
        return usdRate;
    }
}
