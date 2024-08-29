import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { ScatterAirBubbleConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import { graphic } from 'echarts/core'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']

var plantCap = [{
  name: '知识产权',
  value: ''
}, {
  name: '食品生产',
  value: ''
}, {
  name: '道路客运',
  value: ''
}, {
  name: '建筑施工企业',
  value: ''
}, {
  name: '社会保障',
  value: ''
}, {
  name: '食品药品质量安全',
  value: ''
}, {
  name: '环境保护',
  value: ''
}, {
  name: '旅游',
  value: ''
}, {
  name: '文化',
  value: ''
}, {
  name: '物业服务',
  value: ''
}, {
  name: '医疗卫生',
  value: ''
}, {
  name: '生产',
  value: ''
}, {
  name: '价格',
  value: ''
}, {
  name: '法律服务人员',
  value: ''
}, {
  name: '税务',
  value: ''
}, {
  name: '法律服务机构',
  value: ''
}];
var datalist = [{
  fontSize:16,
  fontWeight:"bold",
  offset: [56, 48],
  symbolSize: 110,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(67, 186, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(67, 186, 255)'
  }]),
  opacity:1

}, {
  offset: [30, 70],
  symbolSize: 70,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(67, 186, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(255,226,17)'
  }]),
  opacity:1
}, {
  offset: [5, 43],
  symbolSize: 60,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(27, 96, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(27, 96, 255)'
  }]),
  opacity:1

}, {
  offset: [93, 30],
  symbolSize: 80,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(48, 191, 191, 0)',
  }, {
    offset: 1,
    color: 'rgb(48, 191, 191)'
  }]),
  opacity:1
}, {
  offset: [26, 25],
  symbolSize: 65,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(0, 166, 166, 0)',
  }, {
    offset: 1,
    color: 'rgb(0, 166, 166)'
  }]),
  opacity:1
}, {
  offset: [75, 75],
  symbolSize: 100,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(211,78,255)'
  }]),
  opacity:1

}, {
  offset: [65, 55],
  symbolSize: 60,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(84, 136, 255)'
  }]),
  opacity:1

}, {
  offset: [45, 55],
  symbolSize: 60,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(255,209,9)'
  }]),
  opacity:1

}, {
  offset: [35, 45],
  symbolSize: 60,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(84, 136, 255)'
  }]),
  opacity:1

}, {
  offset: [25, 85],
  symbolSize: 60,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(255,52,239)'
  }]),
  opacity:1

}, {
  offset: [45, 35],
  symbolSize: 60,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(84, 136, 255)'
  }]),
  opacity:1

}, {
  offset: [65, 85],
  symbolSize: 60,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(46,255,247)'
  }]),
  opacity:1

}, {
  offset: [35, 55],
  symbolSize: 60,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(84, 136, 255)'
  }]),
  opacity:1

}, {
  offset: [15, 25],
  symbolSize: 80,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(20,238,255)'
  }]),
  opacity:1

}, {
  offset: [25, 65],
  symbolSize: 60,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(84, 136, 255)'
  }]),
  opacity:1

}, {
  offset: [75, 85],
  symbolSize: 80,
  color: new graphic.RadialGradient(0.4, 0.3, 1, [{
    offset: 0,
    color: 'rgba(84, 136, 255, 0)',
  }, {
    offset: 1,
    color: 'rgb(193,56,255)'
  }]),
  opacity:1

}];

var datas = [];
for (var i = 0; i < plantCap.length; i++) {
  var item = plantCap[i];
  var itemToStyle = datalist[i];
  datas.push({
    name: item.value + '\n' + item.name,
    value: itemToStyle.offset,
    symbolSize: itemToStyle.symbolSize,
    label: {
      normal: {
        textStyle: {
          fontSize: 14
        }
      }
    },
    itemStyle: {
      normal: {
        color: itemToStyle.color,
        opacity: itemToStyle.opacity
      }
    },
  })
}

export const option = {
  dataset: dataJson,
  grid: {
    show: false,
    top: 10,
    bottom: 10
  },

  xAxis: [{
    gridIndex: 0,
    type: 'value',
    show: false,
    min: 0,
    max: 100,
    nameLocation: 'middle',
    nameGap: 5
  }],

  yAxis: [{
    gridIndex: 0,
    min: 0,
    show: false,
    max: 100,
    nameLocation: 'middle',
    nameGap: 30
  }],
  series: [{
    type: 'effectScatter',//scatter
    symbol: 'circle',
    symbolSize: 120,
    label: {
      normal: {
        show: true,
        formatter: '{b}',
        color: '#FFF',
        lineHeight: 15,
        textStyle: {
          fontSize: '30'
        }
      },
    },
    itemStyle: {
      normal: {
        borderWidth: '1',
        borderType: 'solid',
        borderColor: '#00ffff',
        shadowColor: 'rgba(0, 0, 0, .6)',
        shadowBlur: 10,

      }
    },
    data: datas
  }]
}

export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = ScatterAirBubbleConfig.key
  public chartConfig = cloneDeep(ScatterAirBubbleConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
