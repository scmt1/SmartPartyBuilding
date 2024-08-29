import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { CreateComponentType } from '@/packages/index.d'
import {SunburstConfig} from './index'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

let itemFirst = {
    r0: '0',
    r: '40',
    itemStyle: {
        color: '#fff',
    },
    label: {
        color: '#000',
        fontWeight: 'bold',
        fontSize: 24,
        padding: [0, 0, 40, 0],
    },
}

const itemList: any = () =>{
    let itemFirstR:any = Number(itemFirst.r)
    let itemList = []
    const size = 30
    for (let i = 0; i < dataJson[0].maxLevels; i++) {
        itemList.push({
            r0: itemFirstR+size*i,
            r: itemFirstR+size*(i+1),
            itemStyle: {
                color: color[i%color.length]
            }
        })
    }
    return itemList
}

const color = ['#1b0dda', '#f5050e', '#ebb40f']
export const includes = []

export const option = {
    tooltip: {
        show: true,
        formatter: (params: any) => {
            return params.name;
        },
    },
    color: color,
    dataset: dataJson,
    series: {
        type: 'sunburst',
        data: dataJson,
        radius: ['20%', '40%'],
        center: ['50%', '50%'],
        sort: undefined,
        emphasis: {
            focus: 'ancestor',
        },
        label: {
            rotate: 'tangential',
            color: '#fff',
            fontSize: 14,
            formatter: (params: any) => {
                if (params.name.length > 4) {
                    return params.name.substring(0, 4) + '...';
                } else {
                    return params.name;
                }
            },
        },
        levels: [
            {
                // 留给数据下钻点的空白配置
            },
            {
                r0: '0',
                r: '40',
                itemStyle: {
                    color: '#fff',
                },
                label: {
                    color: '#000',
                    fontWeight: 'bold',
                    fontSize: 24,
                    padding: [0, 0, 40, 0],
                },
            }
        ].concat(itemList()),
    },
}

export default class Config extends PublicConfigClass implements CreateComponentType
{
    public key = SunburstConfig.key
    public chartConfig = cloneDeep(SunburstConfig)
    public option = echartOptionProfixHandle(option, includes)
}
