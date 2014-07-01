package com.wangyin.cds.server.persistence;

import com.wangyin.cds.server.persistence.model.DbAlarmInstance;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author wy
 */
public interface DbAlarmInstanceDAO {
	
	public List<DbAlarmInstance> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<DbAlarmInstance> query(@Param("dbAlarmInstance") DbAlarmInstance dbAlarmInstance);

	public DbAlarmInstance load(@Param("id") Integer id);

	public void update(@Param("dbAlarmInstance") DbAlarmInstance dbAlarmInstance);

	public void insert(@Param("dbAlarmInstance") DbAlarmInstance dbAlarmInstance);

	public void delete(@Param("id") Integer id);
}
