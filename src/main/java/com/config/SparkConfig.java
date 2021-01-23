package com.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

	//@Value("${spark.home}")
	//private String sparkHome;
	@Value("${spark.app.name}")
	private String appName;
	@Value("${spark.master}")
	private String masterUri;

	//@Bean
	//public SparkConf conf() {
	//	return new SparkConf().setAppName(appName).setMaster(masterUri);
	//}

	//@Bean
	//public JavaSparkContext javaSparkContext() {
	//	return new JavaSparkContext(new SparkConf().setAppName(appName).setMaster(masterUri));
	//}

}
