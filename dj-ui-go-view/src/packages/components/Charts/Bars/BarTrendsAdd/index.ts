import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const BarTrendsAddConfig: ConfigType = {
  key: 'BarTrendsAdd',
  chartKey: 'VBarTrendsAdd',
  conKey: 'VCBarTrendsAdd',
  title: '横状图_动态追加',
  category: ChatCategoryEnum.BAR,
  categoryName: ChatCategoryEnumName.BAR,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'bar_trendsadd.png'
}
