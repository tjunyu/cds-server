package com.wangyin.cds.server.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.wangyin.cds.server.persistence.model.DbUnit;
/**   
 * @author wy   
 */
public interface DbUnitDAO {
	
	public List<DbUnit> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<DbUnit> query(@Param("dbUnit") DbUnit dbUnit);

	public DbUnit load(@Param("id") Integer id);

	public void update(@Param("dbUnit") DbUnit dbUnit);

	public void insert(@Param("dbUnit") DbUnit dbUnit);

	public void delete(@Param("id") Integer id);
}
