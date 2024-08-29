package me.flyray.bsin.server.impl;
import java.io.InputStream;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzXxqgService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.TzXxqgMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 具体投票项 服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
public class TzXxqgServiceImpl extends ServiceImpl<TzXxqgMapper, TzXxqg> implements TzXxqgService {
    @Autowired
    private TzXxqgMapper tzXxqgapper;

    @Override
    public Response  downTemplate(Map<String, Object> requestMap) throws Exception {

        try {
            //读取Excel模板文件
            //构造数据
            List<List<Object>> data = new ArrayList<>();
            List<Object> row1 = new ArrayList<>();
            row1.add("学号");
            row1.add("姓名");
            row1.add("年龄");
            data.add(row1);
            for(int i = 0; i < 10; i++) {
                List<Object> row = new ArrayList<>();
                row.add("2021000" + i);
                row.add("张三" + i);
                row.add(20 + i);
                data.add(row);
            }

//使用EasyExcel将数据写入到输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").build();
            excelWriter.write(data, writeSheet);
            excelWriter.finish();

//构建Response对象
            byte[] dataBytes = outputStream.toByteArray();
            Response.ResponseBuilder responseBuilder = Response.ok(dataBytes);
            responseBuilder.header("Content-Disposition", "attachment;filename=myfile.xlsx");
            responseBuilder.header("Content-Type", "application/vnd.ms-excel");
            responseBuilder.header("Content-Length", dataBytes.length);
            return responseBuilder.build();
        } catch (IORuntimeException e) {
            e.printStackTrace();
            System.out.println("<<<<<<<<<<<<<<<<<"+e.getMessage());

            return null;
        }


        /*    File templateFile = new File("E:/project/dj_cloud_platform/bsin-server-dwgl/scaffold-server/src/main/resources/excelTemplate/学习强国模板下载.xlsx");
            //创建输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Workbook workbook = WorkbookFactory.create(templateFile);
            //创建工作表对象
            Sheet sheet = workbook.createSheet("Sheet1");

            //向工作表中写入数据
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("学号");
            row.createCell(1).setCellValue("姓名");
            row.createCell(2).setCellValue("年龄");
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("20210001");
            row1.createCell(1).setCellValue("张三");
            row1.createCell(2).setCellValue(20);

            //将工作簿写入到输出流中
            workbook.write(outputStream);
            outputStream.flush();

            //将输出流转换成byte[]类型，构建Response对象并返回
            byte[] dataBytes = outputStream.toByteArray();
            Response.ResponseBuilder responseBuilder = Response.ok(dataBytes);
            responseBuilder.header("Content-Disposition", "attachment;filename=myfile.xlsx");
            responseBuilder.header("Content-Type", "application/vnd.ms-excel");
            responseBuilder.header("Content-Length", dataBytes.length);
            return responseBuilder.build();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(">>>>>>>>>>>>>>>>>>>>"+e.getMessage());
            return null;
        }*/

    /*    Resource resource = null;
            resource = new ClassPathResource("excelTemplate/党员基本信息采集表.xlsx");
            InputStream in = resource.getStream();

            //读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);

            String fileName = "组织架构信息表.xlsx";
            response.setHeader("content-Type", "application/ms-excel");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            OutputStream out = response.getOutputStream();
            wb.write(out);
            IoUtil.close(out);
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }*/
    }

    @Override
    public Map<String, Object> xxx(Map<String, Object> requestMap,HttpServletResponse response) throws IOException {
        System.out.println(requestMap);
        return null;
    }

    @Override
    public Map<String, Object> queryByPage(Map<String, Object> requestMap) {
        try {
            String tenantId = (String) requestMap.get("bizTenantId");
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), SearchVo.class);
            TzXxqg xxqg = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzXxqg.class);
            int page = 1;
            int limit = 10;
            if (pageVo != null) {
                if (pageVo.getPageNumber() != 0) {
                    page = pageVo.getPageNumber();
                }
                if (pageVo.getPageSize() != 0) {
                    limit = pageVo.getPageSize();
                }
            }
            Page<TzXxqg> pageData = new Page<>(page, limit);
            QueryWrapper<TzXxqg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_xxqg.del_flag",0).orderByAsc("tz_xxqg.sort");
            IPage<TzXxqg> result = tzXxqgapper.selectPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> addXxqgList(Map<String, Object> requestMap) {
        try {
            String tenantId = (String) requestMap.get("bizTenantId");
            List<TzXxqg> tzXxqgs = JSON.parseObject(JSON.toJSONString(requestMap.get("tzXxqgs")), new TypeReference<List<TzXxqg>>(){});
            QueryWrapper<TzXxqg>q = new QueryWrapper<>();
            //q.eq("tz_xxqg.dept_id",deptId);
            int delete = tzXxqgapper.delete(q);
            if(tzXxqgs==null||tzXxqgs.size()<0){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无数据可添加");
            }
            for (TzXxqg tzXxqg : tzXxqgs) {
                tzXxqg.setDelFlag(0);
                //tzXxqg.setTenantId(tenantId);
                //tzXxqg.setDeptId(deptId);
                tzXxqg.setImportTime(new Date());
                int insert = tzXxqgapper.insert(tzXxqg);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"导入失败");
        }
    }
}
