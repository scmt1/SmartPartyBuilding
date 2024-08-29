
/***
 *
 * 功能文件名：数据相关文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/

import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarColorConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

// 从ECharts 的默认配置项里取出需要的部分'legend', 'xAxis', 'yAxis',详见 `src/settings/chartThemes/index`
export const includes = ['legend']

export const option = {
	dataset: dataJson,
	backgroundColor: 'rgb(13,29,78)',
	grid: {
		left: '5%',
		right: '5%',
		bottom: '5%',
		top: '10%',
		containLabel: true,
	},
	tooltip: {
		show: true,
		trigger: 'axis',
		axisPointer: {
			show: true,
			type: 'shadow'
		}
	},
	xAxis: {
		axisLabel: {
			color: 'rgb(255,255,255)', //字体颜色
			width: 100,
		},
		// show: false,
		type: 'value',
		splitLine: {
			show: false, //刻度线
		},
		axisTick: {
			show: false, //刻度点
		},
		axisLine: {
			show: false, //是否显示坐标轴轴线

			// lineStyle: {
			//     color: 'rgb(28,136,190)',
			//     color: '#fff',//字体颜色
			//     width: 1
			// }
		},
	},
	yAxis: [
		{
			data: ["出租居住", "自营", "出租经营", "其他"],
			type: 'category',
			inverse: true,
			axisLabel: {
				show: true,
				textStyle: {
					color: 'rgb(255,255,255)',
				},
			},
			splitLine: {
				show: false,
			},
			axisTick: {
				show: false,
			},
			axisLine: {
				show: false,
			},
		},
		{
			type: 'category',
			inverse: true,
			axisTick: 'none',
			axisLine: 'none',
			show: true,
			axisLabel: {
				textStyle: {
					color: '#ffffff',
					fontSize: '12',
				},    // @ts-ignore
				formatter: function (value) {
					// return value + ' %';//柱状图后面跟百分比
				},
			},
		},
	],
	//轮播属性
	dataZoom: [
		{
			yAxisIndex: 0,// 这里是从X轴的0刻度开始
			show: false, // 是否显示滑动条，不影响使用
			type: "slider", // 这个 dataZoom 组件是 slider 型 dataZoom 组件
			startValue: 0, // 从头开始。
			endValue: 4, // 一次性展示多少个。
		}
	],
	series: [
		{
			name: '完成率1',
			type: 'bar',
			stack: 'aa',
			zlevel: 1,
			itemStyle: {
				normal: {
					barBorderRadius: 0,
					// @ts-ignore
					color: "#02EBFAFF"
				},
			},
			barWidth: 15,
		},
		{
			name: '完成率2',
			type: 'bar',
			stack: 'aa',
			zlevel: 1,
			itemStyle: {
				normal: {
					barBorderRadius: 0,
					// @ts-ignore
					color: "#0984FF"
				},
			},
			barWidth: 20,
		},
		{
			name: '完成率3',
			type: 'bar',
			stack: 'aa',
			zlevel: 1,
			itemStyle: {
				normal: {
					barBorderRadius: 0,
					// @ts-ignore
					color: "#C856D0"
				},
			},
			barWidth: 20,

		},
		{
			name: '完成率4',
			type: 'bar',
			stack: 'aa',
			zlevel: 1,
			itemStyle: {
				normal: {
					barBorderRadius: 0,
					// @ts-ignore
					color: "#DD4C77"
				},
			},
			barWidth: 20,
		}
	],
};

// 柱状图类
export default class Config extends PublicConfigClass implements CreateComponentType {

  public key: string = BarColorConfig.key

  public chartConfig = cloneDeep(BarColorConfig)

  // 图表配置项 进行样式合并
  public option = echartOptionProfixHandle(option, includes)
}
