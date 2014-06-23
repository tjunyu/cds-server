package com.wangyin.cds.server.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wangyin.cds.server.persistence.model.DbGroup;
/**   
 * @author wy   
 */
public interface DbGroupDAO {
	
	public List<DbGroup> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<DbGroup> query(@Param("DbGroup") DbGroup dbGroup);

	public DbGroup load(@Param("DbGroupId") Integer dbGroupId);

	public void update(@Param("DbGroup") DbGroup dbGroup);

	public void insert(@Param("DbGroup") DbGroup dbGroup);

	public void delete(@Param("DbGroupId") Integer dbGroupId);
}
