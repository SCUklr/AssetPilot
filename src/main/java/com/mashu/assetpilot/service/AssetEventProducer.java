package com.mashu.assetpilot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashu.assetpilot.dto.AssetChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j // 自动为当前类生成一个日志对象 log，无需手动写 LoggerFactory.getLogger(...)
@Component // 将类标记为 Spring 容器管理的组件（Bean），让 Spring 自动扫描并实例化
@RequiredArgsConstructor // 为类中所有 final 修饰的字段 生成一个构造方法，配合 Spring 的构造器注入

// 封装 kafka 生产者
public class AssetEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "asset.change.events";

    public void sendAssetChangeEvent(AssetChangeEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            String key = String.valueOf(event.getAssetId());

            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(TOPIC, key, message);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Kafka 消息发送成功: topic={}, partition={}, offset={}, message={}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset(),
                            message);
                } else {
                    log.error("Kafka 消息发送失败：{}", message, ex);
                }
            });
        }   catch(JsonProcessingException e) {
                log.error("资产变更事件序列化失败", e);
                throw new RuntimeException("事件序列化失败", e);
            }
            }
}
