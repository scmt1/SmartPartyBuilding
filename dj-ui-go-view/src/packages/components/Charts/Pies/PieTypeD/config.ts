import {echartOptionProfixHandle, PublicConfigClass} from '@/packages/public'
import {PieTypeDConfig} from './index'
import {CreateComponentType} from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend']


var angle = 0; //角度，用来做简单的动画效果的
let color =  [ "#01A75B", "#F29B14", "#007EFC","#9F17FF", "#FFE400", "#F76F01", "#01A4F7", "#FE2C8A"];
let list = dataJson.source;
const option = {
    dataset: dataJson,
    tooltip : {
        show:true,
        trigger: 'item',
    },
    legend: {
        type: 'plain',
        right: 0,
        bottom: '10%',
        x:'center',
        data: list,
        itemWidth: 16,
        itemHeight: 16,
        itemGap: 30,
        icon:'roundRect',
        textStyle: {
            color: '#fff',
            padding: [10, 1, 10, 0],
        },
        selectedMode: false,
    },
    series: [
        {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
                return {
                    type: 'arc',
                    shape: {
                        cx: api.getWidth() / 2,
                        cy: api.getHeight() / 2,
                        r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.6,
                        startAngle: ((0 + angle) * Math.PI) / 180,
                        endAngle: ((90 + angle) * Math.PI) / 180,
                    },
                    style: {
                        stroke: '#0CD3DB',
                        fill: 'transparent',
                        lineWidth: 1.5,
                    },
                    silent: true,
                };
            },
            data: [0],
        },
        {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
                return {
                    type: 'arc',
                    shape: {
                        cx: api.getWidth() / 2,
                        cy: api.getHeight() / 2,
                        r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.6,
                        startAngle: ((180 + angle) * Math.PI) / 180,
                        endAngle: ((270 + angle) * Math.PI) / 180,
                    },
                    style: {
                        stroke: '#0CD3DB',
                        fill: 'transparent',
                        lineWidth: 1.5,
                    },
                    silent: true,
                };
            },
            data: [0],
        },
        {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
                return {
                    type: 'arc',
                    shape: {
                        cx: api.getWidth() / 2,
                        cy: api.getHeight() / 2,
                        r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65,
                        startAngle: ((270 + -angle) * Math.PI) / 180,
                        endAngle: ((40 + -angle) * Math.PI) / 180,
                    },
                    style: {
                        stroke: '#0CD3DB',
                        fill: 'transparent',
                        lineWidth: 1.5,
                    },
                    silent: true,
                };
            },
            data: [0],
        },
        {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
                return {
                    type: 'arc',
                    shape: {
                        cx: api.getWidth() / 2,
                        cy: api.getHeight() / 2,
                        r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65,
                        startAngle: ((90 + -angle) * Math.PI) / 180,
                        endAngle: ((220 + -angle) * Math.PI) / 180,
                    },
                    style: {
                        stroke: '#0CD3DB',
                        fill: 'transparent',
                        lineWidth: 1.5,
                    },
                    silent: true,
                };
            },
            data: [0],
        },
        {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
                let x0 = api.getWidth() / 2;
                let y0 = api.getHeight() / 2;
                let r = (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65;
                let point = getCirlPoint(x0, y0, r, 90 + -angle);
                return {
                    type: 'circle',
                    shape: {
                        cx: point.x,
                        cy: point.y,
                        r: 4,
                    },
                    style: {
                        stroke: '#0CD3DB', //粉
                        fill: '#0CD3DB',
                    },
                    silent: true,
                };
            },
            data: [0],
        },
        {
            name: 'ring5', //绿点
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
                let x0 = api.getWidth() / 2;
                let y0 = api.getHeight() / 2;
                let r = (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65;
                let point = getCirlPoint(x0, y0, r, 270 + -angle);
                return {
                    type: 'circle',
                    shape: {
                        cx: point.x,
                        cy: point.y,
                        r: 4,
                    },
                    style: {
                        stroke: '#0CD3DB', //绿
                        fill: '#0CD3DB',
                    },
                    silent: true,
                };
            },
            data: [0],
        },
        {
            name: '吃猪肉频率',
            type: 'pie',
            radius: ['50%', '35%'],
            silent: true,
            clockwise: true,
            startAngle: 180,
            z: 0,
            zlevel: 0,
            color:color,
            label: {
                normal: {
                    position: 'inside',
                    formatter: function(params: { percent: string }) {
                        return params.percent +'%'
                    },
                    color:"#fff"
                },
            },
            data:list
        },
        {
            type: 'pie',
            zlevel: 3,
            silent: true,
            radius: ['30%', '31%'],
            label: {
                normal: {
                    show: false
                },
            },
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: dotArr()
        }
    ],
};
function dotArr() {
    let dataArr = [];
    for (var i = 0; i < 80; i++) {
        if (i % 2 === 0) {
            dataArr.push({
                name: (i + 1).toString(),
                value: 1,
                itemStyle: {
                    normal: {
                        color: "#0CD3DB",
                        borderWidth: 1,
                        borderColor: "#0CD3DB"
                    }
                }
            })
        } else {
            dataArr.push({
                name: (i + 1).toString(),
                value: 2,
                itemStyle: {
                    normal: {
                        color: "rgba(0,0,0,0)",
                        borderWidth: 0,
                        borderColor: "rgba(0,0,0,0)"
                    }
                }
            })
        }

    }
    return dataArr
}

//获取圆上面某点的坐标(x0,y0表示坐标，r半径，angle角度)
function getCirlPoint(x0: number, y0: number, r: number, angle: number) {
    let x1 = x0 + r * Math.cos((angle * Math.PI) / 180);
    let y1 = y0 + r * Math.sin((angle * Math.PI) / 180);
    return {
        x: x1,
        y: y1,
    };
}
setInterval(function () {
    angle = angle + 3;
}, 100);


export default class Config extends PublicConfigClass implements CreateComponentType {
    public key: string = PieTypeDConfig.key

    public chartConfig = cloneDeep(PieTypeDConfig)

    // 图表配置项
    public option = echartOptionProfixHandle(option, includes)
}


