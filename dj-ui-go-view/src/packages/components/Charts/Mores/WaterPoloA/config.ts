import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { CreateComponentType } from '@/packages/index.d'
import { WaterPoloAConfig } from './index'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const shapes = [
    {
        label: '圆形',
        value: 'circle'
    },
    {
        label: '正方形',
        value: 'rect'
    },
    {
        label: '带圆角的正方形',
        value: 'roundRect'
    },
    {
        label: '正三角形',
        value: 'triangle'
    },
    {
        label: '菱形',
        value: 'diamond'
    },
    {
        label: '水滴',
        value: 'pin'
    },
    {
        label: '箭头',
        value: 'arrow'
    },
]

export const direction = [
    {
        label: '左',
        value: 'left'
    },{
        label: '右',
        value: 'right'
    }
]
export const includes = []

export const option = {
    title: [
        {
            text: dataJson[1].value,
            left: '50%',
            top: "58%",
            textAlign: 'center',
            textStyle: {
                fontSize: '20',
                fontWeight: '400',
                color: '#fff',
                textAlign: 'center',
            },
        },
        {
            text: dataJson[2].value,
            left: '50%',
            top: "64%",
            textAlign: 'center',
            textStyle: {
                fontSize: '20',
                fontWeight: '400',
                color: '#fff',
                textAlign: 'center',
            },
        },
        {
            text: (Number(dataJson[0].value) * 100).toFixed(0)+'%',
            left: '47.5%',
            top: '30%',
            textAlign: 'center',
            textStyle: {
                fontSize: 70,
                color: '#fff',
            },
        },
    ],
    dataset: dataJson,
    series: [
        {
            type: 'liquidFill',
            shape: shapes[0].value,
            radius: '70%',
            z: 6,
            center: ['50%', '50%'],
            amplitude: 20,
            backgroundStyle: {
                borderWidth: 1,
                color: 'rgba(201,219,252, 1)', // 球体
            },
            color: [
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 1,
                            color: '#133480',
                        },
                        {
                            offset: 0,
                            color: '#5F8EE8',
                        },
                    ],
                    globalCoord: false,
                },
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 1,
                            color: '#133480',
                        },
                        {
                            offset: 0,
                            color: '#5F8EE8',
                        },
                    ],
                    globalCoord: false,
                },
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 1,
                            color: '#5F8DE8',
                        },
                        {
                            offset: 0,
                            color: '#5F8DE8',
                        },
                    ],
                    globalCoord: false,
                },
            ],
            label: {
                normal: {
                    formatter: '',
                }
            },
            outline: {
                show: true,
                itemStyle: {
                    borderWidth: 0,
                },
                borderDistance: 0,
            }
        }
    ]
}

export default class Config extends PublicConfigClass implements CreateComponentType
{
    public key = WaterPoloAConfig.key
    public chartConfig = cloneDeep(WaterPoloAConfig)
    public option = echartOptionProfixHandle(option, includes)
}
