import {echartOptionProfixHandle, PublicConfigClass} from '@/packages/public'
import {PieTrendsConfig} from './index'
import {CreateComponentType} from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend']


let angle = 0; //角度


// @ts-ignore
let timerId;

const backgroundColor = '#101736';
const title = {
    text: '',
    textStyle: {
        color: '#fff',
        fontSize: 16,
    },
    padding: 0,
    top: 35,
    left: 'center',
};
const legend = {
    show: false,
};
const tooltip = {
    show: false,
};
const colors = ['#A098FC', '#4386FA', '#4FADFD', '#0CD3DB', '#646CF9'];
const innerRingColor = {
    type: 'linear',
    x: 0,
    y: 0,
    x2: 0,
    y2: 1,
    colorStops: [0, .3, .6, .8, 1].map((offset, index) => ({offset, color: colors[index]})),
    global: false, // 缺省为 false
};

const option = {
    dataset: dataJson,
    backgroundColor,
    series: [
        {
            data: [
                {value: dataJson.value[0], name: '完成', itemStyle: {color: innerRingColor, opacity: 1}},
                {
                    value: dataJson.value[1], name: '未完成', label: {
                        show: false
                    }, itemStyle: {color: '#282c40'}
                },
            ], //数组从大到小排序
            name: '',
            type: 'pie',
            center: ['50%', '50%'], //圆心的位置
            radius: ['32%', '35%'], //环形图的本质就在这里 [内半径!=0，外半径] 外半径越大，圆越大
            avoidLabelOverlap: false, //做同心圆用到
            clockwise: false, //顺时针排列
            startAngle: 90, //起始角度 影响不大
            label: {
                show: true, //false不显示饼图上的标签
                position: 'center', //inside（在饼图上显示）,outside(默认就会出现引导线) center
                formatter: '{d}%',
                fontSize: 50,
                fontWeight: 'bold',
                color: '',
                fontFamily: '',
                foontWeight: ''
            },

            emphasis: {
                scale: false,

            },
        },
        {
            name: '',
            type: 'pie',
            center: ['50%', '50%'], //圆心的位置
            radius: ['40%', '50%'], //环形图的本质就在这里 [内半径!=0，外半径] 外半径越大，圆越大
            avoidLabelOverlap: false, //做同心圆用到
            clockwise: false, //顺时针排列
            startAngle: 90, //起始角度 影响不大

            label: {
                show: false, //false不显示饼图上的标签
            },
            emphasis: {
                scale: false,
            },
            data: [
                {value: dataJson.value[0], name: '完成', itemStyle: {color: innerRingColor, opacity: 1}},
                {value: dataJson.value[1], name: '未完成', itemStyle: {color: '#282c40'}},
            ], //数组从大到小排序
        },
        //colors[0]line
        {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            // @ts-ignore
            renderItem: function (params, api) {
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
                        stroke: colors[0],
                        fill: 'transparent',
                        lineWidth: 1.5,
                    },
                    silent: true,
                };
            },
            data: [0],
        },
        {
            name: 'ring5', //        //colors[0]dot
            type: 'custom',
            coordinateSystem: 'none',
            // @ts-ignore
            renderItem: function (params, api) {
                let x0 = api.getWidth() / 2;
                let y0 = api.getHeight() / 2;
                let r = (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.6;
                let point = getCirlPoint(x0, y0, r, 90 + angle);
                return {
                    type: 'circle',
                    shape: {
                        cx: point.x,
                        cy: point.y,
                        r: 4,
                    },
                    style: {
                        stroke: colors[0],
                        fill: colors[0],
                    },
                    silent: true,
                };
            },
            data: [0],
        },
        // 蓝色

        {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            // @ts-ignore
            renderItem: function (params, api) {
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
                        stroke: colors[1],
                        fill: 'transparent',
                        lineWidth: 1.5,
                    },
                    silent: true,
                };
            },
            data: [0],
        },
        {
            name: 'ring5', // 蓝色
            type: 'custom',
            coordinateSystem: 'none',
            // @ts-ignore
            renderItem: function (params, api) {
                let x0 = api.getWidth() / 2;
                let y0 = api.getHeight() / 2;
                let r = (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.6;
                let point = getCirlPoint(x0, y0, r, 180 + angle);
                return {
                    type: 'circle',
                    shape: {
                        cx: point.x,
                        cy: point.y,
                        r: 4,
                    },
                    style: {
                        stroke: colors[1], //绿
                        fill: colors[1],
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
            // @ts-ignore
            renderItem: function (params, api) {
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
                        stroke: colors[2],
                        fill: 'transparent',
                        lineWidth: 1.5,
                    },
                    silent: true,
                };
            },
            data: [0],
        },
        // 橘色

        {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            // @ts-ignore
            renderItem: function (params, api) {
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
                        stroke: colors[2],
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
            // @ts-ignore
            renderItem: function (params, api) {
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
                        stroke: colors[3], //粉
                        fill: colors[3],
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
            // @ts-ignore
            renderItem: function (params, api) {
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
                        stroke: colors[3], //绿
                        fill: colors[3],
                    },
                    silent: true,
                };
            },
            data: [0],
        },
    ],
};

//获取圆上面某点的坐标(x0,y0表示坐标，r半径，angle角度)
// @ts-ignore
function getCirlPoint(x0, y0, r, angle) {
    let x1 = x0 + r * Math.cos((angle * Math.PI) / 180);
    let y1 = y0 + r * Math.sin((angle * Math.PI) / 180);

    return {
        x: x1,
        y: y1,
    };
}

setTimeout(function () {
    // @ts-ignore
    if (timerId) {
        // @ts-ignore
        clearInterval(timerId);
    }
    timerId = setInterval(function () {
        //用setInterval做动画感觉有问题
        angle = angle + 3;
    }, 100);
}, 500);


export default class Config extends PublicConfigClass implements CreateComponentType {
    public key: string = PieTrendsConfig.key

    public chartConfig = cloneDeep(PieTrendsConfig)

    // 图表配置项
    public option = echartOptionProfixHandle(option, includes)
}


