package com.wangyin.cds.server.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.wangyin.cds.server.persistence.model.DbEvent;
/**
 * @author wy
 */
public interface DbEventDAO {
	
	public List<DbEvent> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<DbEvent> query(@Param("dbEvent") DbEvent dbEvent);

	public DbEvent load(@Param("id") Integer id);

	public void update(@Param("dbEvent") DbEvent dbEvent);

	public void insert(@Param("dbEvent") DbEvent dbEvent);

	public void delete(@Param("id") Integer id);
}
