package com.wangyin.cds.server.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.wangyin.cds.server.persistence.model.App;
/**   
 * @author wy   
 */
public interface AppDAO {
	
	public List<App> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<App> query(@Param("app") App app);

	public App load(@Param("id") Integer id);

	public void update(@Param("app") App app);

	public void insert(@Param("app") App app);

	public void delete(@Param("id") Integer id);
}
