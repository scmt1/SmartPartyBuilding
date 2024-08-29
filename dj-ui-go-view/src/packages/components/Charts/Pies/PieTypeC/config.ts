
/***
 *
 * 功能文件名：数据相关文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/

import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { PieTypeCConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

// 从ECharts 的默认配置项里取出需要的部分'legend', 'xAxis', 'yAxis',详见 `src/settings/chartThemes/index`
export const includes = ['legend']

var colors: never[] = [];
for (let i = 0; i < dataJson.source.length; i++) {
    let a = parseInt(String(Math.random() * 255));
    let b = parseInt(String(Math.random() * 255));
    let c = parseInt(String(Math.random() * 255));
    let color:any = `rgb(${a},${b},${c})`;
    // @ts-ignore
    colors.push(color)
}
const option = {
    dataset: { ...dataJson },
    color: colors,
    grid: {
        top: '15%',
        left: 0,
        right: '1%',
        bottom: 5,
    },
    tooltip : {
        trigger: 'item',
        formatter:"{b} : ({d}%)"
    },
    legend: [{
        orient: 'vertical',
        top: 'center',
        icon:"circle",
        right: 180,
        textStyle:{
            color:'#ffffff',
            fontSize:16
        },
        data: [],
    },{
        orient: 'vertical',
        top: 'center',
        icon:"circle",
        right: 40,
        textStyle:{
            color:'#ffffff',
            fontSize:16
        },
    }],
    series: [{
        type: 'pie',
        center: ['40%', '50%'],
        radius: ['50%', '75%'],
        showEmptyCircle :true,
        label: {
            normal: {
                show: true,
                position: 'inside',
                formatter: '{d}%',
                rich: {
                    value: {
                        fontSize: 20,
                        color:'#ffffff',
                    },
                },
            }
        },
    }]
}

// 柱状图类
export default class Config extends PublicConfigClass implements CreateComponentType {

  public key: string = PieTypeCConfig.key

  public chartConfig = cloneDeep(PieTypeCConfig)

  // 图表配置项 进行样式合并
  public option = echartOptionProfixHandle(option, includes)
}
