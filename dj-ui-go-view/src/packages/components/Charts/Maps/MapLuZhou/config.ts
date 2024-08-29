
/***
 * 
 * 功能文件名：数据相关文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/ 

import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { MapLuZhouConfig } from './index'
import { chartInitConfig } from '@/settings/designSetting'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
// 地图JSON数据
import dataJson from './data.json'

// 从ECharts 的默认配置项里取出需要的部分'legend', 'xAxis', 'yAxis',详见 `src/settings/chartThemes/index`
export const includes = []

const covertdata = [
    {name:'江阳区',value:[105.405085,28.852643,'1042']},
    {name:'纳溪区',value:[105.38985,28.642428,'842']},
    {name:'龙马潭区',value:[105.44851,28.968352,'542']},
    {name:'合江县',value:[105.834098,28.810325,'342']},
    {name:'古蔺县',value:[105.929959,27.975923,'942']},
    {name:'叙永县',value:[105.437775,28.167919,'442']},
    {name:'泸县',value:[105.498675,29.11915,'122']}
]

const splitList = [
    {
        name: '江阳区',
        itemStyle:{
            color: '#236da8',
            opacity: 1,
            borderWidth: 1.5, //分界线宽度
            borderColor: '#207fce', //分界线颜色
        }
    },
    {
        name: '纳溪区',
        itemStyle:{
            color: '#2884db',
            opacity: 1,
            borderWidth: 1.5, //分界线宽度
            borderColor: '#207fce', //分界线颜色
        }
    },
    {
        name: '龙马潭区',
        itemStyle:{
            color: '#244779',
            opacity: 1,
            borderWidth: 1.5, //分界线宽度
            borderColor: '#207fce', //分界线颜色
        }
    },
    {
        name: '泸县',
        itemStyle:{
            color: '#246198',
            opacity: 1,
            borderWidth: 1.5, //分界线宽度
            borderColor: '#207fce', //分界线颜色
        }
    },
    {
        name: '合江县',
        itemStyle:{
            color: '#2884db',
            opacity: 1,
            borderWidth: 1.5, //分界线宽度
            borderColor: '#207fce', //分界线颜色
        }
    },
    {
        name: '古蔺县',
        itemStyle:{
            color: '#2884db',
            opacity: 1,
            borderWidth: 1.5, //分界线宽度
            borderColor: '#207fce', //分界线颜色
        }
    },
    {
        name: '叙永县',
        itemStyle:{
            color: '#244779',
            opacity: 1,
            borderWidth: 1.5, //分界线宽度
            borderColor: '#207fce', //分界线颜色
        }
    }
];

const option = {
    dataset: dataJson,
    mapRegion: {
        adcode: '泸州',
        showHainanIsLands: true
    },
    geo3D: {
        map: '泸州',
        roam: true,
        boxDepth: 120, //地图倾斜度
        regionHeight:6, //地图厚度
        regions:splitList,
        realisticMaterial: {
            detailTexture: '#fff', // 纹理贴图
            textureTiling: 1, // 纹理平铺，1是拉伸，数字表示纹理平铺次数
            roughness: 0, // 材质粗糙度，0完全光滑，1完全粗糙
            metalness: 0, // 0材质是非金属 ，1金属
            roughnessAdjust: 0,
        },
        viewControl: {
            distance: 135, // 地图视角 控制初始大小
            rotateSensitivity: 1, // 旋转
            zoomSensitivity: 1, // 缩放
            autoRotate:true,
        },
        itemStyle: {
            areaColor: '#1d5e98',
            opacity: 1,
            borderWidth: 1.5, //分界线宽度
            borderColor: '#207fce', //分界线颜色
        },
        label: {
            show: true,
            textStyle: {
                color: '#000', //地图初始化区域字体颜色
                fontSize: 8,
                opacity: 1,
                backgroundColor: 'rgba(0,23,11,0)'
            },
        },
        emphasis: { //当鼠标放上去  地区区域是否显示名称
            label: {
                show: true,
                textStyle: {
                    color: '#fff',
                    fontSize: 3,
                    backgroundColor: 'rgba(0,23,11,0)'
                }
            }
        },
        light: { //光照阴影
            main: {
                color: '#FFF', //光照颜色
                intensity: 1.2, //光照强度
                shadow: true, //是否显示阴影
                alpha:55,
                beta:10
            },
             ambient: {
                intensity: 0.3
            }
        }
    },
	series: [
        //柱状图
		{
			name: 'bar3D',
			type: "bar3D",
			coordinateSystem: 'geo3D',
			barSize: 2, //柱子粗细
			shading: 'lambert',
			opacity: 1,
			bevelSize: 0.3,
			label: {
				show: false,
				formatter: '{b}'
			},
            itemStyle: {
                color: '#eba438',
                opacity: 1,
            },
			data: covertdata,
		},
    ],

}

export const MapDefaultConfig = { ...option }
// 类
export default class Config extends PublicConfigClass implements CreateComponentType {

  public key: string = MapLuZhouConfig.key
  public attr = { ...chartInitConfig, w: 750, h: 800, zIndex: -1 }
  public chartConfig = cloneDeep(MapLuZhouConfig)

  // 图表配置项 进行样式合并
  public option = echartOptionProfixHandle(option, includes)
}
