
/***
 * 
 * 功能文件名：图表声明文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/ 

import image from '@/assets/images/chart/charts/pie-B.png'
// 公共类型声明
import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
// 当前[信息模块]分类声明
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const PieTypeBConfig: ConfigType = {
  // 唯一key，注意！！！文件夹名称需要修改成与当前组件一致！！！
  key: 'PieTypeB',
  // 图表组件渲染 Components 格式: V + key
  chartKey: 'VPieTypeB',
  // 配置组件渲染 Components 格式: VC + key
  conKey: 'VCPieTypeB',
  // 名称
  title: '饼图-类型B',
  // 子分类目录
  category: ChatCategoryEnum.PIE,
  // 子分类目录
  categoryName: ChatCategoryEnumName.PIE,
  // 包分类
  package: PackagesCategoryEnum.CHARTS,
  // 组件框架类型 (注意！若此 Echarts 图表不支持 dataset 格式，则使用 ChartFrameEnum.COMMON)
  chartFrame: ChartFrameEnum.ECHARTS,
  // 图片 (注意！图片存放的路径必须在 src/assets/images/chart/包分类名称/*)
  // 文件夹名称需要和包分类名称一致: PackagesCategoryEnum.CHARTS
  image: image
}
