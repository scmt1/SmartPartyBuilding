import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { ScatterDiagramConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import { graphic } from 'echarts/core'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']

export const option = {
  backgroundColor: new graphic.RadialGradient(0.3, 0.3, 0.8, [{
    offset: 0,
    color: '#FFFFFF'
  }, {
    offset: 1,
    color: '#FFFFFF'
  }]),
  tooltip: {},
  animationDuration: 3000,
  animationEasingUpdate: 'quinticInOut',
  series: [{
    name: '监管领域',
    type: 'graph',
    layout: 'force',
    radius: ['80%'],
    force: {
      repulsion: 1500
    },
    data: [{
      'name': '监管领域',
      'symbolSize': 150,
      'draggable': 'true',
      'value': ''

    }, {
      'name': '知识产权',
      'value': '',
      'symbolSize': 92,
      'category': '知识产权',
      'draggable': 'true'
    }, {
      'name': '食品生产',
      'symbolSize': 42,
      'category': '食品生产',
      'draggable': 'true',
      'value': ''
    }, {
      'name': '道路客运',
      'symbolSize': 27,
      'category': '道路客运',
      'draggable': 'true',
      'value': ''
    }, {
      'name': '建筑施工',
      'symbolSize': 53,
      'category': '建筑施工',
      'draggable': 'true',
      'value': ''
    }, {
      'name': '社会保障',
      'value': '',
      'symbolSize': 28,
      'category': '社会保障',
      'draggable': 'true'
    }, {
      'name': '食品药品质量安全',
      'symbolSize': 35,
      'category': '食品药品质量安全',
      'draggable': 'true',
      'value': ""
    }, {
      'name': '环境保护',
      'symbolSize': 22,
      'category': '环境保护',
      'draggable': 'true',
      'value': ""
    }, {
      'name': '旅游',
      'symbolSize': 19,
      'category': '旅游',
      'draggable': 'true',
      'value': ""
    }, {
      'name': '文化',
      'symbolSize': 33,
      'category': '文化',
      'draggable': 'true',
      'value': ""
    }, {
      'name': '物业服务',
      'symbolSize': 18,
      'category': '物业服务',
      'draggable': 'true',
      'value': ""
    }, {
      'name': '医疗卫生',
      'value': "",
      'symbolSize': 21,
      'category': '医疗卫生',
      'draggable': 'true'
    }, {
      'name': '生产',
      'value': "",
      'symbolSize': 25,
      'category': '生产',
      'draggable': 'true'
    }, {
      'name': '价格',
      'value': "",
      'symbolSize': 21,
      'category': '价格',
      'draggable': 'true'
    }, {
      'name': '法律服务人员',
      'value': "",
      'symbolSize': 28,
      'category': '法律服务人员',
      'draggable': 'true'
    }, {
      'name': '税务',
      'value': "",
      'symbolSize': 26,
      'category': '税务',
      'draggable': 'true'
    }, {
      'name': '法律服务机构',
      'value': "",
      'symbolSize': 26,
      'category': '法律服务机构',
      'draggable': 'true'
    }],
    links: [{
      'source': '监管领域',
      'target': '知识产权'
    }, {
      'source': '监管领域',
      'target': '食品生产'
    }, {
      'source': '监管领域',
      'target': '道路客运'
    }, {
      'source': '监管领域',
      'target': '建筑施工'
    }, {
      'source': '监管领域',
      'target': '社会保障'
    }, {
      'source': '监管领域',
      'target': '食品药品质量安全'
    }, {
      'source': '监管领域',
      'target': '环境保护'
    }, {
      'source': '监管领域',
      'target': '旅游'
    }, {
      'source': '监管领域',
      'target': '文化'
    }, {
      'source': '监管领域',
      'target': '物业服务'
    }, {
      'source': '监管领域',
      'target': '医疗卫生'
    }, {
      'source': '监管领域',
      'target': '生产'
    }, {
      'source': '监管领域',
      'target': '价格'
    }, {
      'source': '监管领域',
      'target': '法律服务人员'
    }, {
      'source': '监管领域',
      'target': '税务'
    }, {
      'source': '监管领域',
      'target': '法律服务机构'
    }],
    categories: [{
      'name': '知识产权'
    }, {
      'name': '食品生产'
    }, {
      'name': '道路客运'
    }, {
      'name': '建筑施工'
    }, {
      'name': '社会保障'
    }, {
      'name': '食品药品质量安全'
    }, {
      'name': '环境保护'
    }, {
      'name': '旅游'
    }, {
      'name': '文化'
    }, {
      'name': '物业服务'
    }, {
      'name': '医疗卫生'
    }, {
      'name': '生产'
    }, {
      'name': '价格'
    }, {
      'name': '法律服务人员'
    }, {
      'name': '税务'
    }, {
      'name': '法律服务机构'
    }],

    focusNodeAdjacency: true,
    roam: true,
    label: {
      normal: {
        show: true,
        position: 'top',

      }
    },
    lineStyle: {
      normal: {
        color: 'source',
        curveness: 0,
        type: 'solid'
      }
    }
  }]
}

export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = ScatterDiagramConfig.key
  public chartConfig = cloneDeep(ScatterDiagramConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
