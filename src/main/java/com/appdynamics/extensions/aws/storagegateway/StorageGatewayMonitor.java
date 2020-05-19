/*
 * Copyright 2020. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.storagegateway;

import com.appdynamics.extensions.aws.SingleNamespaceCloudwatchMonitor;
import com.appdynamics.extensions.aws.collectors.NamespaceMetricStatisticsCollector;
import com.appdynamics.extensions.aws.config.Configuration;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessor;
import static com.appdynamics.extensions.aws.storagegateway.util.Constants.DEFAULT_METRIC_PREFIX;
import static com.appdynamics.extensions.aws.storagegateway.util.Constants.MONITOR_NAME;
import com.appdynamics.extensions.logging.ExtensionsLoggerFactory;
import com.google.common.collect.Lists;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @author Satish Muddam
 */
public class StorageGatewayMonitor extends SingleNamespaceCloudwatchMonitor<Configuration> {

    private static final Logger LOGGER = ExtensionsLoggerFactory.getLogger(StorageGatewayMonitor.class);

    public StorageGatewayMonitor() {
        super(Configuration.class);
        LOGGER.info(String.format("Using AWS StorageGateway Monitor Version [%s]",
                this.getClass().getPackage().getImplementationTitle()));
    }

    @Override
    protected NamespaceMetricStatisticsCollector getNamespaceMetricsCollector(Configuration config) {
        MetricsProcessor metricsProcessor = createMetricsProcessor(config);
        return new NamespaceMetricStatisticsCollector.Builder(config.getAccounts(),
                config.getConcurrencyConfig(),
                config.getMetricsConfig(),
                metricsProcessor,
                config.getMetricPrefix())
                .withCredentialsDecryptionConfig(config.getCredentialsDecryptionConfig())
                .withProxyConfig(config.getProxyConfig())
                .build();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getDefaultMetricPrefix() {
        return DEFAULT_METRIC_PREFIX;
    }

    @Override
    public String getMonitorName() {
        return MONITOR_NAME;
    }

    @Override
    protected List<Map<String, ?>> getServers() {
        return Lists.newArrayList();
    }

    private MetricsProcessor createMetricsProcessor(Configuration config) {
        return new StorageGatewayMetricsProcessor(config.getMetricsConfig().getIncludeMetrics(), config.getDimensions());
    }

//    public static void main(String[] args) throws TaskExecutionException {
//
//        StorageGatewayMonitor monitor = new StorageGatewayMonitor();
//
//        final Map<String, String> taskArgs = new HashMap<String, String>();
//
//        taskArgs.put("config-file", "src/main/resources/conf/config.yml");
//
//        monitor.execute(taskArgs, null);
//
//    }
}
