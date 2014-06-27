package com.wangyin.cds.server.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.wangyin.cds.server.persistence.model.DbMonitorInstance;
/**   
 * @author wy   
 */
public interface DbMonitorInstanceDAO {
	
	public List<DbMonitorInstance> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<DbMonitorInstance> query(@Param("dbMonitorInstance") DbMonitorInstance dbMonitorInstance);

	public DbMonitorInstance load(@Param("id") Integer id);

	public void update(@Param("dbMonitorInstance") DbMonitorInstance dbMonitorInstance);

	public void insert(@Param("dbMonitorInstance") DbMonitorInstance dbMonitorInstance);

	public void delete(@Param("id") Integer id);
}
