/*
 * Copyright 2020. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.storagegateway;

import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import org.junit.Test;

import java.util.Map;

public class StorageGatewayMonitorITest {
	
	private StorageGatewayMonitor classUnderTest = new StorageGatewayMonitor();
	
	@Test
	public void testMetricsCollectionCredentialsEncrypted() throws Exception {
		Map<String, String> args = Maps.newHashMap();
		args.put("config-file","src/test/resources/conf/itest-encrypted-config.yml");

		TaskOutput result = classUnderTest.execute(args, null);
		assertTrue(result.getStatusMessage().contains("Monitor AWSStorageGatewayMonitor completes."));
	}
	
	@Test
	public void testMetricsCoyllectionWithProxy() throws Exception {
		Map<String, String> args = Maps.newHashMap();
		args.put("config-file","src/test/resources/conf/itest-proxy-config.yml");
		
		TaskOutput result = classUnderTest.execute(args, null);
		assertTrue(result.getStatusMessage().contains("Monitor AWSStorageGatewayMonitor completes."));
	}	
}
