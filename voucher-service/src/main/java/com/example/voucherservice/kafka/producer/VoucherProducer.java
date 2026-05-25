package com.example.voucherservice.kafka.producer;

import com.example.dtocommon.kafka.Order_Voucher.UseVoucherResultEvent;
import com.example.voucherservice.kafka.config.JsonConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoucherProducer {
    KafkaTemplate<String, String > kafkaTemplate;
    JsonConverter jsonConverter;
    final static String rollback_order = "use-voucher-response";

    public void sendRollbackToOrder(UseVoucherResultEvent orderVoucherEvent){
        kafkaTemplate.send(rollback_order, jsonConverter.toJson(orderVoucherEvent));
    }
}
