// 公共类型声明
import { ConfigType, PackagesCategoryEnum } from '@/packages/index.d'
// 当前[信息模块]分类声明
import { ChatCategoryEnum,ChatCategoryEnumName } from '../../index.d'

export const DataFlowConfig: ConfigType = {
  // 唯一key
  key: 'DataFlow',
  // 图表组件渲染 Components 格式: V + key
  chartKey: 'VDataFlow',
  // 配置组件渲染 Components 格式: VC + key
  conKey: 'VCDataFlow',
  // 名称
  title: '数据流向',
  // 子分类目录
  category: ChatCategoryEnum.MORE,
  // 子分类目录
  categoryName: ChatCategoryEnumName.MORE,
  // 包分类
  package: PackagesCategoryEnum.CHARTS,
  // 图片
  image: 'dataFlow.png'
}
