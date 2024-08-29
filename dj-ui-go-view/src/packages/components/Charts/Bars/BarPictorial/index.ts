
/***
 * 
 * 功能文件名：图表声明文件
 * 创建日期：2023-02-14
 * 创建人：LuoCX
 ***/ 

import image from '@/assets/images/chart/charts/bar-pictorial.png'
// 公共类型声明
import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
// 当前[信息模块]分类声明
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const BarPictorialConfig: ConfigType = {
  // 唯一key，注意！！！文件夹名称需要修改成与当前组件一致！！！
  key: 'BarPictorial',
  // 图表组件渲染 Components 格式: V + key
  chartKey: 'VBarPictorial',
  // 配置组件渲染 Components 格式: VC + key
  conKey: 'VCBarPictorial',
  // 名称
  title: '柱状图-象形柱状图',
  // 子分类目录
  category: ChatCategoryEnum.BAR,
  // 子分类目录
  categoryName: ChatCategoryEnumName.BAR,
  // 包分类
  package: PackagesCategoryEnum.CHARTS,
  // 组件框架类型 (注意！若此 Echarts 图表不支持 dataset 格式，则使用 ChartFrameEnum.COMMON)
  chartFrame: ChartFrameEnum.ECHARTS,
  // 图片 (注意！图片存放的路径必须在 src/assets/images/chart/包分类名称/*)
  // 文件夹名称需要和包分类名称一致: PackagesCategoryEnum.CHARTS
  image: image
}
