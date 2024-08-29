import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import {RadarAConfig} from "./index"
import dataJson from './data.json'

export const includes = []

export const labelShow = [
    {
        label: '是',
        value: true,
    },
    {
        label: '否',
        value: false,
    }
]
export const labelPosition = [
    {
        label: '上',
        value: 'top'
    },
    {
        label: '左',
        value: 'left'
    },
    {
        label: '右',
        value: 'right'
    },
    {
        label: '下',
        value: 'bottom'
    },
    {
        label: '内侧',
        value: 'inside'
    },
    {
        label: '内侧偏右',
        value: 'insideLeft'
    },
    {
        label: '内侧偏左',
        value: 'insideRight'
    },
    {
        label: '内侧偏下',
        value: 'insideTop'
    },
    {
        label: '内侧偏上',
        value: 'insideBottom'
    },
    {
        label: '内侧右下',
        value: 'insideTopLeft'
    },
    {
        label: '内侧右上',
        value: 'insideBottomLeft'
    },
    {
        label: '内侧左下',
        value: 'insideTopRight'
    },
    {
        label: '内侧左上',
        value: 'insideBottomRight'
    }
]

export const setRadarData = (data: any) =>{
    let list = []
    for (let i = 0; i < data.length; i++) {
        list.push({
            name: data[i].name,
            label: {
                position: labelPosition[0].value,
                fontSize: 14
            },
            value: data[i].value
        })
    }
    return list
}

export const setSunburst = (data: any) =>{
    let list = []
    for (let i = 0; i < data.length; i++) {
        list.push({
            name: data[i].name,
            value: data[i].value,
            label: {
                position: 'inside',
                rotate: 'tangential',
                fontSize: 14
            },
            itemStyle: {
                color: ''
            }
        })
    }
    return list
}

export const option = {
    dataset: { ...dataJson },
    radar: {
        indicator: dataJson.radarIndicator,
        center: ['50%', '50%'],
        radius: '60%',
    },
    series: [
        {
            type: 'radar',
            zlevel: 2,
            tooltip: {
                show: true,
            },
            lineStyle: {
                width: 1,
                opacity: 1,
            },
            areaStyle: {
                color: 'rgba(255, 204, 50, 0.2)',
            },
            symbolSize: 3,
            label: {
                show: labelShow[0].value,
            },
            data: setRadarData(dataJson.seriesRadarData)
        },
        {
            type: 'sunburst',
            center: ['50%', '50%'],
            nodeClick: false,
            levels: [
                {},
                {
                    r0: '90%',
                    r: '100%',
                },
            ],
            sort: 'asc',
            data: setSunburst(dataJson.seriesSunburstData)
        }
    ]
}

export default class Config extends PublicConfigClass implements CreateComponentType {
    public key: string = RadarAConfig.key

    public chartConfig = cloneDeep(RadarAConfig)

    // 图表配置项
    public option = echartOptionProfixHandle(option, includes)
}
