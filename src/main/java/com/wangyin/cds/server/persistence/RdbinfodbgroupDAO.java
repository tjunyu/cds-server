package com.wangyin.cds.server.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.wangyin.cds.server.persistence.model.Rdbinfodbgroup;
/**   
 * @author wy   
 */
public interface RdbinfodbgroupDAO {
	
	public List<Rdbinfodbgroup> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<Rdbinfodbgroup> query(@Param("rdbinfodbgroup") Rdbinfodbgroup rdbinfodbgroup);

	public Rdbinfodbgroup load(@Param("rDbInfoDbGroupId") Integer rDbInfoDbGroupId);

	public void update(@Param("rdbinfodbgroup") Rdbinfodbgroup rdbinfodbgroup);

	public void insert(@Param("rdbinfodbgroup") Rdbinfodbgroup rdbinfodbgroup);

	public void delete(@Param("rDbInfoDbGroupId") Integer rDbInfoDbGroupId);
}
