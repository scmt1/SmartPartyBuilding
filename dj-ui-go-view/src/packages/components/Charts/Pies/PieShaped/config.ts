
/***
 *
 * 功能文件名：数据相关文件
 * 创建日期：2023-02-14
 * 创建人：HXH
 ***/

import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { PieShapedConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

// 从ECharts 的默认配置项里取出需要的部分'legend', 'xAxis', 'yAxis',详见 `src/settings/chartThemes/index`
export const includes = ['legend']

let title = '信用信息占比';
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
    dataset:{ ...dataJson },
    legend: {
        top: 'top'
    },
    tooltip: {
        trigger: 'item',
        formatter: '{b} : {d}%'
    },
    color: colors,
    title: [{
        text: '{name|' + title + '}',
        top: 'center',
        left: 'center',
        textStyle: {
            rich: {
                name: {
                    fontSize: 20,
                    fontWeight: 'bold',
                    color: '#fff',
                    padding: [10, 0]
                }
            }
        }
    }],
    series: [
        {
            name: title,
            type: 'pie',
            radius: ['30%', '100%'],
            center: ['50%', '50%'],
            roseType: 'area',
            itemStyle: {
                borderRadius: 8,
                normal : {
                    label : {
                        show : false
                    },
                    labelLine : {
                        show : false
                    }
                }
            },
        }
    ]
}

// 柱状图类
export default class Config extends PublicConfigClass implements CreateComponentType {

  public key: string = PieShapedConfig.key

  public chartConfig = cloneDeep(PieShapedConfig)

  // 图表配置项 进行样式合并
  public option = echartOptionProfixHandle(option, includes)
}
