package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.events.targets.SnsTopic;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.EmailSubscription;
import software.constructs.Construct;

public class SnsStack extends Stack {

	private final SnsTopic PRODUCT_EVENTS_TOPIC;

	public SnsStack(final Construct scope, final String id) {
		this(scope, id, null);
	}

	public SnsStack(final Construct scope, final String id, final StackProps props) {
		super(scope, id, props);

		//Criando um Topico
		// @formatter:off
 		PRODUCT_EVENTS_TOPIC = SnsTopic.Builder
				.create(Topic.Builder.create(this, "ProductEventsTopic")
				.topicName("product-events")
				.build())
				.build();
 		// @formatter:on
 		
 		
 		//Fazendo a inscrição 
 		// @formatter:off
  		PRODUCT_EVENTS_TOPIC.getTopic()
 				.addSubscription(EmailSubscription.Builder
 				.create("benefrancis@gmail.com")
 				.json(true)
 				.build());
  		// @formatter:on

	}

	public SnsTopic getPRODUCT_EVENTS_TOPIC() {
		return PRODUCT_EVENTS_TOPIC;
	}

}
