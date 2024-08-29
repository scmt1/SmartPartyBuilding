package me.flyray.bsin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.SearchVo;
import me.flyray.bsin.server.domain.TzActiveDept;

import javax.servlet.http.HttpServletResponse;

public interface TzActiveDeptMapper extends BaseMapper<TzActiveDept> {

    /**
     * 功能描述：实现分页查询
     * @param tzActiveDept 需要模糊查询的信息
     * @param searchVo 排序参数
     * @param pageVo 分页参数
     * @return 返回获取结果
     */
    public IPage<TzActiveDept> queryTzActiveDeptListByPage(TzActiveDept tzActiveDept, SearchVo searchVo, PageVo pageVo);

    /**
     * 功能描述： 导出
     * @param tzActiveDept 查询参数
     * @param response response参数
     */
    public void download(TzActiveDept tzActiveDept, HttpServletResponse response) ;

}
