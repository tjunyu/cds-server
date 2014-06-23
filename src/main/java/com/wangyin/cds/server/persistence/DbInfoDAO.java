package com.wangyin.cds.server.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wangyin.cds.server.persistence.model.DbInfo;
/**   
 * @author wy   
 */
public interface DbInfoDAO {
	
	public List<DbInfo> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<DbInfo> query(@Param("DbInfo") DbInfo dbInfo);

	public DbInfo load(@Param("id") Integer id);

	public void update(@Param("DbInfo") DbInfo dbInfo);

	public void insert(@Param("DbInfo") DbInfo dbInfo);

	public void delete(@Param("id") Integer id);
}
