package com.wangyin.cds.server.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.wangyin.cds.server.persistence.model.CdsSessionDO;
/**   
 * @author wy   
 */
public interface CdsSessionDAO {
	
	public List<CdsSessionDO> getPageList(@Param("start") Integer start, @Param("limit") Integer limit);
	
	public List<CdsSessionDO> query(@Param("cdsSession") CdsSessionDO cdsSession);

	public CdsSessionDO load(@Param("id") Integer id);

	public void update(@Param("cdsSession") CdsSessionDO cdsSession);

	public void insert(@Param("cdsSession") CdsSessionDO cdsSession);

	public void delete(@Param("id") String id);
	
	public Integer count();
}
