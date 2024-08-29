
/***
 *
 * 功能文件名：数据相关文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/

import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { PieTypeAConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

// 从ECharts 的默认配置项里取出需要的部分'legend', 'xAxis', 'yAxis',详见 `src/settings/chartThemes/index`
export const includes = ['legend']
// var color = [
//     '#0CD2E6',
//     '#3751E6',
//     '#FFC722',
//     '#886EFF',
// ];
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
    tooltip : {
        trigger: 'item',
        formatter: "占比<br/>{b} : ({d}%)"
    },
    legend: {
        icon: "circle",
        orient: 'horizontal',
        textStyle: {
            color: "#fff"
        },
        itemGap: 20
    },
    series : [
        {
            type:'pie',
            label: {
                normal: {
                  position: 'inner',
                  formatter: '{d}%',
                  textStyle: {
                        color: '#FFF',
                        fontWeight: 'bold',
                        fontSize: 16
                    }
                }
            },
        }
    ]
}

// 柱状图类
export default class Config extends PublicConfigClass implements CreateComponentType {

  public key: string = PieTypeAConfig.key

  public chartConfig = cloneDeep(PieTypeAConfig)

  // 图表配置项 进行样式合并
  public option = echartOptionProfixHandle(option, includes)
}
