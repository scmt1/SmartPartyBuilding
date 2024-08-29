import { echartOptionProfixHandle,PublicConfigClass } from '@/packages/public'
import { CreateComponentType } from '@/packages/index.d'
import { DataFlowConfig } from './index'
import cloneDeep from 'lodash/cloneDeep'
import { graphic } from 'echarts/core'
export const includes = ['legend']

export const option = {
}
export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = DataFlowConfig.key
  public chartConfig = cloneDeep(DataFlowConfig)
  public option = echartOptionProfixHandle(option,includes)
}
