package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class VpcStack extends Stack {
	
	private Vpc vpc;
	
	public VpcStack(final Construct scope, final String id) {
		this(scope, id, null);
	}

	public VpcStack(final Construct scope, final String id, final StackProps props) {
		super(scope, id, props);

		// The code that defines your stack goes here
		// example resource
		vpc =Vpc.Builder.create(this, "VPC01")
	      .maxAzs(2)
	      .natGateways(0)
	      .build();
	}
	
	
	public Vpc getVpc() {
		return vpc;
	}
}
