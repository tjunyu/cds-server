package com.wangyin.cds.server.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wangyin.cds.server.persistence.model.DbMonitor;

public interface DbMonitorDAO {
	
	public List<DbMonitor> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<DbMonitor> query(@Param("dbMonitor") DbMonitor dbMonitor);

	public DbMonitor load(@Param("id") Integer id);

	public void update(@Param("dbMonitor") DbMonitor dbMonitor);

	public void insert(@Param("dbMonitor") DbMonitor dbMonitor);

	public void delete(@Param("id") Integer id);
}
