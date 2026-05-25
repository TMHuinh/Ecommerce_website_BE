package com.example.voucherservice.kafka.consumer;

import com.example.dtocommon.kafka.Order_Voucher.UseVoucherEvent;
import com.example.dtocommon.kafka.Order_Voucher.UseVoucherResultEvent;
import com.example.voucherservice.dto.response.ApiResponse;

import com.example.voucherservice.kafka.config.JsonConverter;
import com.example.voucherservice.kafka.producer.VoucherProducer;
import com.example.voucherservice.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoucherConsumer {
    VoucherService voucherService;
    JsonConverter jsonConverter;
    VoucherProducer voucherProducer;

    @KafkaListener(topics = "use-voucher", groupId = "voucher-group")
    public void handleUseVoucher(String data) {
        UseVoucherEvent event = jsonConverter.fromJson(data, UseVoucherEvent.class);
        ApiResponse<?> result = voucherService.useVoucher(event.getVoucherID());

        UseVoucherResultEvent useVoucherResultEvent = UseVoucherResultEvent.builder()
                .orderID(event.getOrderID())
                .voucherID(event.getVoucherID())
                .build();

        if(result.getCode()==1000){
            useVoucherResultEvent.setResult(true);
        }else {
            useVoucherResultEvent.setResult(false);
        }

        voucherProducer.sendRollbackToOrder(useVoucherResultEvent);
    }
}
