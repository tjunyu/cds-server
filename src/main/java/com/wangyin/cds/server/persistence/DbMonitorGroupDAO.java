package com.wangyin.cds.server.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wangyin.cds.server.persistence.model.DbMonitorGroup;
/**   
 * @author wy   
 */
public interface DbMonitorGroupDAO {
	
	public List<DbMonitorGroup> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<DbMonitorGroup> query(@Param("DbGroup") DbMonitorGroup dbGroup);

	public DbMonitorGroup load(@Param("DbGroupId") Integer dbGroupId);

	public void update(@Param("DbGroup") DbMonitorGroup dbGroup);

	public void insert(@Param("DbGroup") DbMonitorGroup dbGroup);

	public void delete(@Param("DbGroupId") Integer dbGroupId);
}
