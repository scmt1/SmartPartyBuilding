
/***
 * 
 * 功能文件名：数据相关文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/ 

import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { PieRoundBConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'

// 从ECharts 的默认配置项里取出需要的部分'legend', 'xAxis', 'yAxis',详见 `src/settings/chartThemes/index`
export const includes = []

const option = {
	dataset: 808,
	title: {
		text: 808,
		x: 'center',
		y: 'center',
		textStyle: {
			color: "#56B9F8",
			fontSize: 30
		}
	},
	xAxis: {
		data: [],
		axisTick: {
			show: false,
		},
		axisLine: {
			show: false,
		},
		axisLabel: {
			show: false,
		},
	},
	yAxis: {
		splitLine: {
			show: false,
		},
		axisTick: {
			show: false,
		},
		axisLine: {
			show: false,
		},
		axisLabel: {
			show: false,
		},
	},
	series: [{
			name: '外圆',
			type: 'pie',
			radius: ['80%', '90%'],
			silent: true,
			clockwise: true,
			startAngle: 100,
			label: {
				normal: {
					position: 'center',
				},
			},
			data: [100],
			z: 2,
			zlevel: 2,
			itemStyle: {
				normal: {
					color: {
						type: 'linear',
						x: 0,
						y: 0,
						x2: 0,
						y2: 1,
						colorStops: [{
								offset: 0,
								color: 'rgba(24, 134, 144, 1)', // 0% 处的颜色
							},
							{
								offset: 1,
								color: 'rgba(76, 228, 230, 1)', // 100% 处的颜色
							},
						],
					},
				},
			}
		},
		{
			name: '内圆',
			type: 'pie',
			radius: [0, '80%'], //蓝色
			hoverAnimation: false,
			data: [100],
			z: 3,
			itemStyle: {
				normal: {
					shadowBlur: 10,
					shadowColor: 'red',
					color: {
						type: 'radial',
						x: 0.5,
						y: 0.5,
						r: 0.5,
						colorStops: [{
								offset: 0,
								color: 'rgba(76, 228, 230, 1)', // 0% 处的颜色
							},
							{
								offset: 1,
								color: 'rgba(24, 134, 144, 0.8)', // 100% 处的颜色
							},
						],
					},
				},
			}
		},
	]
}

// 柱状图类
export default class Config extends PublicConfigClass implements CreateComponentType {
  public key: string = PieRoundBConfig.key

  public chartConfig = cloneDeep(PieRoundBConfig)
  // 图表配置项 进行样式合并
  public option = echartOptionProfixHandle(option, includes)
}
