
/***
 *
 * 功能文件名：数据相关文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/

import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarPictorialConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

// 从ECharts 的默认配置项里取出需要的部分'legend', 'xAxis', 'yAxis',详见 `src/settings/chartThemes/index`
export const includes = ['legend']

const tempsymbol = "path://M12.000,-0.000 C12.000,-0.000 16.074,60.121 22.731,60.121 C26.173,60.121 -3.234,60.121 0.511,60.121 C7.072,60.121 12.000,-0.000 12.000,-0.000 Z";

const option = {
    legend: {
		textStyle: {
			fontSize: 18,
			color: "rgba(101, 213, 255, 1)"
		},
		itemWidth:18, // 设置宽度
		itemHeight:18, // 设置高度、
		itemGap: 12 // 设置间距
	},
	tooltip: {
		show: true,
		trigger: "axis", //axis , item
		backgroundColor: "RGBA(0, 49, 85, 1)",
		borderColor: "rgba(0, 151, 251, 1)",
		borderWidth: 1,
		borderRadius: 0,
		textStyle: {
			color: "#BCE9FC",
			fontSize: 16,
			align: "left"
		}
	},
	xAxis: {
		type: "category",
		boundaryGap: true,
		axisLabel: {
		//坐标轴刻度标签的相关设置。
		interval: 0, //设置为 1，表示『隔一个标签显示一个标签』
		textStyle: {
			color: "#65D5FF",
			fontStyle: "normal",
			fontSize: 16
			}
		},
		axisTick: {
			//坐标轴刻度相关设置。
			show: false
		},
		axisLine: {
		//坐标轴轴线相关设置
		lineStyle: {
			color: "rgba(77, 128, 254, 0.2)"
		}
		},
		splitLine: {
			//坐标轴在 grid 区域中的分隔线。
			show: true,
			lineStyle: {
				color: "rgba(77, 128, 254, 0.2)"
			}
		}
	},
	yAxis: [
		{
		type: "value",
		splitNumber: 3,
		axisLabel: {
			textStyle: {
			color: "#65D5FF",
			fontStyle: "normal",
			fontSize: 16
			}
		},
		axisLine: {
			show: false
		},
		axisTick: {
			show: false
		},
		splitLine: {
			show: true,
			lineStyle: {
			color: "rgba(77, 128, 254, 0.2)"
			}
		}
		}
	],
	dataset: { ...dataJson },
	series: [
		{
			type: "pictorialBar",
			stack: "总量",
			itemStyle: {
				normal: {
					color: {
						type: "linear",
						x: 0,
						y: 0,
						x2: 0,
						y2: 1,
						colorStops: [{
							offset: 0,
							color: "rgba(0, 151, 251, 1)" // 0% 处的颜色
						},
						{
							offset: 1,
							color: "rgba(0, 34, 66, 0.2)" // 100% 处的颜色
						}
					],
					} //渐变颜色
				}
			},
			symbol:tempsymbol
		},
		{
			type: "pictorialBar",
			stack: "总量",
			itemStyle: {
				normal: {
					color: {
						type: "linear",
						x: 0,
						y: 0,
						x2: 0,
						y2: 1,
						colorStops: [{
							offset: 0,
							color: "rgba(48, 236, 166, 1)" // 0% 处的颜色
						},
						{
							offset: 1,
							color: "rgba(0, 34, 66, 0.2)" // 100% 处的颜色
						}
					],
					} //渐变颜色
				}
			},
			symbol:tempsymbol
		}
	],
	//轮播属性
	dataZoom: [
		{
			xAxisIndex: 0,// 这里是从X轴的0刻度开始
			show: false, // 是否显示滑动条，不影响使用
			type: "slider", // 这个 dataZoom 组件是 slider 型 dataZoom 组件
			startValue: 0, // 从头开始。
			endValue: 4, // 一次性展示多少个。
		}
	]
}

// 柱状图类
export default class Config extends PublicConfigClass implements CreateComponentType {

  public key: string = BarPictorialConfig.key

  public chartConfig = cloneDeep(BarPictorialConfig)

  // 图表配置项 进行样式合并
  public option = echartOptionProfixHandle(option, includes)
}
