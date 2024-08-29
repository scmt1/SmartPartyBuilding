import {echartOptionProfixHandle, PublicConfigClass} from '@/packages/public'
import {PieTypeEConfig} from './index'
import {CreateComponentType} from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend']

const radius = ['30%', '70%'];
const option = {
    dataset: dataJson.source,
    tooltip : {
        trigger: 'item',
        formatter:"{b} : ({d}%)"
    },
    legend: {
        show:true,
        x:'center',
        orient: 'horizontal',
        icon:"circle",
        right: 180,
        textStyle:{
            color:'#ffffff',
            fontSize:16
        },
    },
    series: [{
        type: 'pie',
        id: 'distribution',
        radius: radius,
        label: {
            normal: {
                show: true,
                position: 'inside',
                formatter: '{d}%',
                textStyle: {
                    color: '#FFF',
                    fontWeight: 'bold',
                    fontSize: 16
                }
            }
        },
        universalTransition: true,
        animationDurationUpdate: 2000,
        data: dataJson.source
    }]
};
export default class Config extends PublicConfigClass implements CreateComponentType {
    public key: string = PieTypeEConfig.key

    public chartConfig = cloneDeep(PieTypeEConfig)

    // 图表配置项
    public option = echartOptionProfixHandle(option, includes)
}


