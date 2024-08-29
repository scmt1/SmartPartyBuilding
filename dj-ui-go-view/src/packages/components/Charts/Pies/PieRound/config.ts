
/***
 * 
 * 功能文件名：数据相关文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/ 

import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { PieRoundConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'

// 从ECharts 的默认配置项里取出需要的部分'legend', 'xAxis', 'yAxis',详见 `src/settings/chartThemes/index`
export const includes = []

const option = {
	tooltip: {
		show: true,
		trigger: 'item'
	},
	legend: {
		show: true,
	},
	dataset: 55,
	title: {
		text: "0",
		x: 'center',
		y: 'center',
		textStyle: {
			fontWeight: 'normal',
			color: '#fff',
			fontSize: '40'
		}
	},
	series: [{
		name: 'circle',
		type: 'pie',
		clockWise: true,
		radius: ['75%', '90%'],
		itemStyle: {
			normal: {
				label: {
					show: false
				},
				labelLine: {
					show: false
				}
			}
		},
		hoverAnimation: false,
		data: [{
				value: 0,
				itemStyle: {
					normal: {
						color: { // 颜色渐变
							colorStops: [{
								offset: 0,
								color: '#4FADFD' // 0% 处的颜色
							}, {
								offset: 1,
								color: '#28E8FA' // 100% 处的颜色1
							}]
						},
						label: {
							show: false
						},
						labelLine: {
							show: false
						}
					}
				}
			}
		]
	}]
}

// 柱状图类
export default class Config extends PublicConfigClass implements CreateComponentType {

  public key: string = PieRoundConfig.key

  public chartConfig = cloneDeep(PieRoundConfig)

  // 图表配置项 进行样式合并
  public option = echartOptionProfixHandle(option, includes)
}
