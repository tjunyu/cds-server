package com.wangyin.cds.server.persistence;

import com.wangyin.cds.server.persistence.model.DbAlarm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author wy
 */
public interface DbAlarmDAO {
	
	public List<DbAlarm> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<DbAlarm> query(@Param("dbAlarm") DbAlarm dbAlarm);

	public DbAlarm load(@Param("id") Integer id);

	public void update(@Param("dbAlarm") DbAlarm dbAlarm);

	public void insert(@Param("dbAlarm") DbAlarm dbAlarm);

	public void delete(@Param("id") Integer id);
}
