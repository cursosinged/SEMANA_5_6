/*
 * Copyright (c) 2022 Nextiva, Inc. to Present.
 * All rights reserved.
 */

package com.wizeline.maven.learningjavamaven.consumer;

import java.util.List;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;

@Component
public class KafkaConsumer {
	private static final Logger LOG = Logger.getLogger(KafkaConsumer.class.getName());

	@KafkaListener(id = "sampleGroup", topics = "useraccount-topic", containerFactory = "jsonKafkaListenerContainerFactory")
	public void consumeMessage(ConsumerRecord<String, List<BankAccountDTO>> cr, @Payload BankAccountDTO account) {
		LOG.info("Received: " + account.getUserName());
	}
}
