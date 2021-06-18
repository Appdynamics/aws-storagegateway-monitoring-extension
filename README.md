# AWS StorageGateway Monitoring Extension

## Use Case
Captures StorageGateway statistics from Amazon CloudWatch and displays them in the AppDynamics Metric Browser.

**Note : By default, the Machine agent can only send a fixed number of metrics to the controller. This extension potentially reports thousands of metrics, so to change this limit, please follow the instructions mentioned [here](https://docs.appdynamics.com/display/PRO40/Metrics+Limits).**

## Prerequisites
1. Please give the following permissions to the account being used to with the extension.
    ```
    cloudwatch:ListMetrics
    cloudwatch:GetMetricStatistics
    ```
2. In order to use this extension, you do need a [Standalone JAVA Machine Agent](https://docs.appdynamics.com/display/PRO44/Standalone+Machine+Agents) or [SIM Agent](https://docs.appdynamics.com/display/PRO44/Server+Visibility).  For more details on downloading these products, please  visit [here](https://download.appdynamics.com/).
3. The extension needs to be able to connect to AWS Cloudwatch in order to collect and send metrics. To do this, you will have to either establish a remote connection in between the extension and the product using access key and secret key, or have an agent running on EC2 instance, which you can use with instance profile.
<p><strong>Agent Compatibility:</strong></p>
<p><strong>Note: This extension is compatible with Machine Agent version 4.5.13 or later.</strong></p>
<ol>
<li>
<p>If you are seeing warning messages while starting the Machine Agent, update the http-client and http-core JARs in <code>{MACHINE_AGENT_HOME}/monitorsLibs</code> to <code>httpclient-4.5.9</code> and <code>httpcore-4.4.12</code> to make this warning go away.</p>
</li>
<li>
<p>To make this extension work on Machine Agent &lt; 4.5.13, the http-client and http-core JARs in <code>{MACHINE_AGENT_HOME}/monitorsLibs</code> need to be updated to <code>httpclient-4.5.9</code> and <code>httpcore-4.4.12</code>.</p>
</li>
</ol>

## Installation

1. Run 'mvn clean install' from aws-storagegateway-monitoring-extension
2. Copy and unzip AWSStorageGatewayMonitor-\<version\>.zip from 'target' directory into \<machine_agent_dir\>/monitors/
3. Edit config.yml file in AWSStorageGatewayMonitor/conf and provide the required configuration (see Configuration section)
4. Restart the Machine Agent.

## Configuration

### config.yml

**Note: Please avoid using tab (\t) when editing yml files. You may want to validate the yml file using a [yaml validator](http://www.yamllint.com/).**

| Section | Fields | Description | Example |
| ----- | ----- | ----- | ----- |
| **metricPrefix**| | The path prefix for viewing metrics in the metric browser. | "Custom Metrics\|Amazon StorageGateway\|", <br> "Server\|Component:<TIER_ID>\|Custom Metrics\|Amazon StorageGateway\|" |
| **accounts** | | Fields under this section can be repeated for multiple accounts config |  |
| | awsAccessKey | AWS Access Key |  |
| | awsSecretKey | AWS Secret Key |  |
| | displayAccountName | Display name used in metric path | "MyAWSStorageGateway" |
| | regions | Regions where StorageGateway is registered | |
| **credentialsDecryptionConfig** | ----- | ----- | ----- |
| | enableDecryption | If set to "true", then all aws credentials provided (access key and secret key) will be decrypted - see AWS Credentials Encryption section |  |
| | encryptionKey | The key used when encrypting the credentials |  |
| **proxyConfig** | ----- | ----- | ----- |
| | host | The proxy host (must also specify port) |  |
| | port | The proxy port (must also specify host) |  |
| | username | The proxy username (optional)  |  |
| | password | The proxy password (optional)  |  |
| **dimensions** | ----- | ----- | ----- |
|| name |Dimension name | [refer: AWS Storage Gateway](https://docs.aws.amazon.com/storagegateway/latest/userguide/Main_monitoring-gateways-common.html) for available Dimensions |
||displayName|displayName to be appended in metricPath||
||values|accepts an array of dimension values| put ".*" to match all|
| **metricsConfig** | ----- | ----- | ----- |
| includeMetrics | | Fields under this section can be repeated for multiple metric types override | Configure it with the list of StorageGateway metrics which should be monitored |
| | name | The metric name | "AvailabilityNotifications" |
| | alias | Alias for the metrric name yo be used in metricPath | |
| | ----- | ----- | ----- |
| metricsTimeRange |  |  |  |
| | startTimeInMinsBeforeNow | The no of mins to deduct from current time for start time of query | 5 |
| | endTimeInMinsBeforeNow | The no of mins to deduct from current time for end time of query.<br>Note, this must be less than startTimeInMinsBeforeNow | 0 |
| | ----- | ----- | ----- |
| | maxErrorRetrySize | The max number of retry attempts for failed retryable requests | 1 |
| **concurrencyConfig** |  |  |  |
| | noOfAccountThreads | The no of threads to process multiple accounts concurrently | 3 |
| | noOfRegionThreadsPerAccount | The no of threads to process multiple regions per account concurrently | 3 |
| | noOfMetricThreadsPerRegion | The no of threads to process multiple metrics per region concurrently | 3 |
| | ----- | ----- | ----- |
| **cloudWatchMonitoring** | Basic | Monitoring Types. For more details:  [refer](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-cloudwatch-new.html )| **Allowed Values** <br/> "Basic", <br/> "Detailed"|
| | ----- | ----- | ----- |


**Below is an example config for monitoring multiple accounts and regions:**

~~~

metricPrefix: "Custom Metrics|Amazon StorageGateway|"
accounts:
  - awsAccessKey: "XXXXXXXX1"
    awsSecretKey: "XXXXXXXXXX1"
    displayAccountName: "TestAccount_1"
    regions: ["us-east-1","us-west-1","us-west-2"]

  - awsAccessKey: "XXXXXXXX2"
    awsSecretKey: "XXXXXXXXXX2"
    displayAccountName: "TestAccount_2"
    regions: ["eu-central-1","eu-west-1"]

credentialsDecryptionConfig:
    enableDecryption: "false"
    encryptionKey:

proxyConfig:
    host:
    port:
    username:
    password:
    
dimensions:
  - name: "GatewayId"
    displayName: "Gateway Id"
    values: [".*"]
  - name: "VolumeId"
    displayName: "Volume Id"
    values: [".*"]

metricsConfig:
    includeMetrics:
       - name: "AvailabilityNotifications"
         alias: "Availability Notifications"
         statType: "sum"
         aggregationType: "AVERAGE"
         timeRollUpType: "AVERAGE"
         clusterRollUpType: "INDIVIDUAL"
         delta: false
         multiplier: 1
       - name: "CacheHitPercent"
         alias: "CacheHit Percent"
         statType: "sum"
         aggregationType: "AVERAGE"
         timeRollUpType: "AVERAGE"
         clusterRollUpType: "INDIVIDUAL"
         delta: false
         multiplier: 1
       - name: "CacheUsed"
         alias: "Cache Used"
         statType: "sum"
         aggregationType: "AVERAGE"
         timeRollUpType: "AVERAGE"
         clusterRollUpType: "INDIVIDUAL"
         delta: false
         multiplier: 1
       - name: "IndexEvictions"
         alias: "Index Evictions"
         statType: "sum"
         aggregationType: "AVERAGE"
         timeRollUpType: "AVERAGE"
         clusterRollUpType: "INDIVIDUAL"
         delta: false
         multiplier: 1

    metricsTimeRange:
      startTimeInMinsBeforeNow: 5
      endTimeInMinsBeforeNow: 0
      
    getMetricStatisticsRateLimit: 400
    maxErrorRetrySize: 0

concurrencyConfig:
  noOfAccountThreads: 3
  noOfRegionThreadsPerAccount: 3
  noOfMetricThreadsPerRegion: 3
  #Thread timeout in seconds
  threadTimeOut: 30

cloudWatchMonitoring: "Basic"
~~~

### AWS Credentials Encryption
To set an encrypted awsAccessKey and awsSecretKey in config.yml, follow the steps below:

1. Download the util jar to encrypt the AWS Credentials from [here](https://github.com/Appdynamics/maven-repo/blob/master/releases/com/appdynamics/appd-exts-commons/1.1.2/appd-exts-commons-1.1.2.jar).
2. Run command:

   	~~~   
   	java -cp appd-exts-commons-1.1.2.jar com.appdynamics.extensions.crypto.Encryptor EncryptionKey CredentialToEncrypt

   	For example:
   	java -cp "appd-exts-commons-1.1.2.jar" com.appdynamics.extensions.crypto.Encryptor test myAwsAccessKey

   	java -cp "appd-exts-commons-1.1.2.jar" com.appdynamics.extensions.crypto.Encryptor test myAwsSecretKey
   	~~~

3. Set the decryptionKey field in config.yml with the encryption key used, as well as the resulting encrypted awsAccessKey and awsSecretKey in their respective fields.

Please visit [this page](https://community.appdynamics.com/t5/Knowledge-Base/How-to-use-Password-Encryption-with-Extensions/ta-p/29397) to get detailed instructions on password encryption. The steps in this document will guide you through the whole process.

## Metrics
Typical metric path: **Application Infrastructure Performance|\<Tier\>|Custom Metrics|Amazon StorageGateway|\<Account Name\>|\<Region\>|Gateway Id|\<Gateway Id\>|Gateway Name|\<Gateway Name\>** followed by the metrics defined in the link below:

- [StorageGateway Metrics](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/awssg-metricscollected.html)


## Credentials Encryption ##

Please visit [Encryption Guidelines](https://community.appdynamics.com/t5/Knowledge-Base/How-to-use-Password-Encryption-with-Extensions/ta-p/29397) to get detailed instructions on password encryption. The steps in this document will guide you through the whole process.
If you want to use password encryption, please send arguments as connectionProperties. You will have to fill in the encrypted Password and Encryption Key fields in the config but you will also have to give an empty "" value to the password field and the encrypted password will be automatically picked up.

## Extensions Workbench

Workbench is an inbuilt feature provided with each extension in order to assist you to fine tune the extension setup before you actually deploy it on the controller. Please review the following document on [How to use the Extensions WorkBench](https://community.appdynamics.com/t5/Knowledge-Base/How-to-use-the-Extensions-WorkBench/ta-p/30130)

## Troubleshooting

Please follow the steps listed in this [troubleshooting-document](https://community.appdynamics.com/t5/Knowledge-Base/How-to-troubleshoot-missing-custom-metrics-or-extensions-metrics/ta-p/28695) in order to troubleshoot your issue. These are a set of common issues that customers might have faced during the installation of the extension. If these don't solve your issue, please follow the last step on the [troubleshooting-document](https://community.appdynamics.com/t5/Knowledge-Base/How-to-troubleshoot-missing-custom-metrics-or-extensions-metrics/ta-p/28695) to contact the support team.

## Support Tickets

If after going through the [Troubleshooting Document](https://community.appdynamics.com/t5/Knowledge-Base/How-to-troubleshoot-missing-custom-metrics-or-extensions-metrics/ta-p/28695) you have not been able to get your extension working, please file a ticket and add the following information.

Please provide the following in order for us to assist you better.

1. Stop the running machine agent.
2. Delete all existing logs under `<MachineAgent>/logs`.
3. Please enable debug logging by editing the file `<MachineAgent>/conf/logging/log4j.xml`. Change the level value of the following `<logger>` elements to debug.
   ```
   <logger name="com.singularity">
   <logger name="com.appdynamics">
   ```
4. Start the machine agent and please let it run for 10 mins. Then zip and upload all the logs in the directory `<MachineAgent>/logs/*`.
5. Attach the zipped `<MachineAgent>/conf/*` directory here.
6. Attach the zipped `<MachineAgent>/monitors/ExtensionFolderYouAreHavingIssuesWith` directory here.
   
For any support related questions, you can also contact [help@appdynamics.com](mailto:help@appdynamics.com).

## Contributing

Always feel free to fork and contribute any changes directly here on [GitHub](https://github.com/Appdynamics/aws-storagegateway-monitoring-extension).

## Version
   |          Name            |  Version   |
   |--------------------------|------------|
   |Extension Version         |2.0.2      |
   |Controller Compatibility  |4.5 or Later|
   |Agent Compatibility        | 4.5.13 or later|
   |Last Update               |4 Jun, 2021 |
List of changes to this extension can be found [here](https://github.com/Appdynamics/aws-storagegateway-monitoring-extension/blob/master/CHANGELOG.md)
