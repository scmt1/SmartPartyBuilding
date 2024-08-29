
/***
 *
 * 功能文件名：数据相关文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/

import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { PieTypeBConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
// 数据源
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
    legend: {
		orient: 'horizontal',
		icon: 'rect',
		top: 'bottom',
		left: 'center',
		itemGap: 10,
		itemWidth: 16,
		textStyle: {
			color: '#fff',
			lineHeight: 10,
			fontFamily: 'Regular',
			fontSize: '14px',
		},
	},
    tooltip: {
        trigger: 'item',
        formatter: '占比<br/>{b} :({d}%)',
    },
    series: [ {
        showEmptyCircle: true,
        type: 'pie',
        selectedOffset: 50, // 设置偏离距离
        minAngle: 10,
        label: {
            normal: {
                position: 'inner',
                formatter: '{d}%',
                textStyle: {
                    color: '#0f99df',
                    fontSize: 18
                }
            }
        },
        itemStyle: {
                normal: {
                    borderColor: '#0d0a3a',
                    borderWidth: '5',
                },
            },
        },
    ],
}

// 柱状图类
export default class Config extends PublicConfigClass implements CreateComponentType {

  public key: string = PieTypeBConfig.key

  public chartConfig = cloneDeep(PieTypeBConfig)

  // 图表配置项 进行样式合并
  public option = echartOptionProfixHandle(option, includes)
}
