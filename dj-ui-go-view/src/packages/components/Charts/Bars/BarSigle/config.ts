
/***
 * 
 * 功能文件名：数据相关文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/ 

import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarSigleConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

// 从ECharts 的默认配置项里取出需要的部分'legend', 'xAxis', 'yAxis',详见 `src/settings/chartThemes/index`
export const includes = ['legend']

const option = {
	dataset: { ...dataJson },
	tooltip: {
		trigger: 'item',
	},
	xAxis: {
		show: true,
		type: 'category',
		axisLabel: {
			textStyle: {
				color: 'rgba(122,173,212,1)',
				fontSize: 10
			},
			margin: 5, //刻度标签与轴线之间的距离。
			rotate: '-45',
		},
	},
	yAxis: {
		show: true,
		type: 'value',
		splitLine: {
			show: true,
			lineStyle: {
				color: 'rgba(53,65,95,1)',
				type: 'dashed'
			}
		},
		axisLabel: {
			textStyle: {
				color: 'rgba(122,173,212,1)',
				fontSize: 14
			},
		}
	},
	series: [//柱体
		{
			type: 'bar',
			barWidth: 40,
			itemStyle: {
				normal: {
					color: {
						x: 0,
						y: 0,
						x2: 0,
						y2: 1,
						type: "linear",
						global: false,
						colorStops: [{ 
							offset: 0,
							color: "rgba(0,213,255,1)"
						}, {
							offset: 1,
							color: "rgba(0,171,255,1)"
						}]
					}
				}
			}
		}
	]
}

// 柱状图类
export default class Config extends PublicConfigClass implements CreateComponentType {

  public key: string = BarSigleConfig.key

  public chartConfig = cloneDeep(BarSigleConfig)

  // 图表配置项 进行样式合并
  public option = echartOptionProfixHandle(option, includes)
}
