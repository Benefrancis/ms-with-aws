package com.myorg;

import software.amazon.awscdk.App;

public class CursoAwsCdkApp {
	public static void main(final String[] args) {
		App app = new App();

		VpcStack vpcStack = new VpcStack(app, "VirtualPrivateCloud");

		ClusterStack clusterStack = new ClusterStack(app, "Cluster", null, vpcStack.getVpc());

		clusterStack.addDependency(vpcStack);

		RdsStack rdsStack = new RdsStack(app, "RDS", vpcStack.getVpc());

		rdsStack.addDependency(vpcStack);

		SnsStack snsStack = new SnsStack(app, "SNS");

		Service01Stack service01Stack = new Service01Stack(app, "Service01", clusterStack.getCluster(),
				snsStack.getPRODUCT_EVENTS_TOPIC());
		service01Stack.addDependency(clusterStack);
		service01Stack.addDependency(rdsStack);
		service01Stack.addDependency(snsStack);
		app.synth();
	}
}
