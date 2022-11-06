package com.cmxz.snakesmartmemo.util;

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidDataSourceFactory extends PooledDataSourceFactory {
    public DruidDataSourceFactory(){
        this.dataSource = new DruidDataSource();
    }
}
